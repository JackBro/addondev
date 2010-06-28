var EXPORTED_SYMBOLS = ["GM_log", "GM_xmlhttpRequester", "GM_Scripthrefs", "GM_ScriptStorage", "GM_listen", "GM_hitch", "GM_addStyle", "GM_openInTab", "GM_Resources"];

//const Cc = Components.classes;
//const Ci = Components.interfaces;
var Application = Components.classes["@mozilla.org/fuel/application;1"].getService(Components.interfaces.fuelIApplication);
var consoleService = Components.classes["@mozilla.org/consoleservice;1"].getService(Components.interfaces.nsIConsoleService);

var GM_Scripthrefs = 
{
	hrefs:[],
	clear : function()
	{
		this.hrefs = [];
	},
	setHref : function(href)
	{
		if(this.hrefs.indexOf(href) == -1)
		{
			this.hrefs.push(href);
		}
	}
}

const gmSvcFilename = Components.stack.filename;

// Examines the stack to determine if an API should be callable.
function GM_apiLeakCheck(apiName) {
  var stack = Components.stack;

  do {
    // Valid stack frames for GM api calls are: native and js when coming from
    // chrome:// URLs and the greasemonkey.js component's file:// URL.
    if (2 == stack.language) {
      // NOTE: In FF 2.0.0.0, I saw that stack.filename can be null for JS/XPCOM
      // services. This didn't happen in FF 2.0.0.11; I'm not sure when it
      // changed.
      if (stack.filename != null &&
          stack.filename != gmSvcFilename &&
          stack.filename.substr(0, 6) != "chrome" &&
          GM_Scripthrefs.hrefs.indexOf(stack.filename) == -1) {
        GM_log(new Error("Greasemonkey access violation: unsafeWindow " +
                    "cannot call " + apiName + "." + stack.filename + ":" + gmSvcFilename));
                    for(var hh in GM_Scripthrefs.hrefs)
                    {
                    	GM_log("hh = " + GM_Scripthrefs.hrefs[hh]);
                    }
        return false;
      }
    }

    stack = stack.caller;
  } while (stack);

  return true;
}

///////////////////////////////////////////////////
//GM_listen
function GM_listen(source, event, listener, opt_capture) {
  Components.lookupMethod(source, "addEventListener")(
    event, listener, opt_capture);
}

///////////////////////////////////////////////////
//GM_hitch
function GM_hitch(obj, meth) {
  if (!obj[meth]) {
    throw "method '" + meth + "' does not exist on object '" + obj + "'";
  }

  var staticArgs = Array.prototype.splice.call(arguments, 2, arguments.length);

  return function() {
    // make a copy of staticArgs (don't modify it because it gets reused for
    // every invocation).
    var args = staticArgs.concat();

    // add all the new arguments
    for (var i = 0; i < arguments.length; i++) {
      args.push(arguments[i]);
    }

    // invoke the original function with the correct this obj and the combined
    // list of static and dynamic arguments.
    return obj[meth].apply(obj, args);
  };
}

//////////////////////////////////////////////////
//GM_log
function GM_log(message, force) 
{
	consoleService.logStringMessage(message);
	//Application.console.log(message);
}

//////////////////////////////////////////////////
//GM_xmlhttpRequester
function GM_xmlhttpRequester(unsafeContentWin, chromeWindow) {
  this.unsafeContentWin = unsafeContentWin;
  this.chromeWindow = chromeWindow;
  GM_log("< GM_xmlhttpRequester this.unsafeContentWin " + this.unsafeContentWin);
}

// this function gets called by user scripts in content security scope to
// start a cross-domain xmlhttp request.
//
// details should look like:
// {method,url,onload,onerror,onreadystatechange,headers,data}
// headers should be in the form {name:value,name:value,etc}
// can't support mimetype because i think it's only used for forcing
// text/xml and we can't support that
GM_xmlhttpRequester.prototype.contentStartRequest = function(details) {
  if (!GM_apiLeakCheck("GM_xmlhttpRequest")) {
    return;
  }

  // don't actually need the timer functionality, but this pops it
  // out into chromeWindow's thread so that we get that security
  // context.
  GM_log("> GM_xmlhttpRequest.contentStartRequest");

  // important to store this locally so that content cannot trick us up with
  // a fancy getter that checks the number of times it has been accessed,
  // returning a dangerous URL the time that we actually use it.
  var url = details.url;

  // make sure that we have an actual string so that we can't be fooled with
  // tricky toString() implementations.
  if (typeof url != "string") {
    throw new Error("Invalid url: url must be of type string");
  }

  var ioService = Components.classes["@mozilla.org/network/io-service;1"]
                  .getService(Components.interfaces.nsIIOService);
  var scheme = ioService.extractScheme(url);

  // This is important - without it, GM_xmlhttpRequest can be used to get
  // access to things like files and chrome. Careful.
  switch (scheme) {
    case "http":
    case "https":
    case "ftp":
      this.chromeWindow.setTimeout(
        GM_hitch(this, "chromeStartRequest", url, details), 0);
      break;
    default:
      throw new Error("Invalid url: " + url);
  }

  GM_log("< GM_xmlhttpRequest.contentStartRequest");
};

// this function is intended to be called in chrome's security context, so
// that it can access other domains without security warning
GM_xmlhttpRequester.prototype.chromeStartRequest = function(safeUrl, details) {
  GM_log("> GM_xmlhttpRequest.chromeStartRequest");
  var req = new this.chromeWindow.XMLHttpRequest();

  this.setupRequestEvent(this.unsafeContentWin, req, "onload", details);
  this.setupRequestEvent(this.unsafeContentWin, req, "onerror", details);
  this.setupRequestEvent(this.unsafeContentWin, req, "onreadystatechange",
                         details);

  req.open(details.method, safeUrl);

  if (details.overrideMimeType) {
    req.overrideMimeType(details.overrideMimeType);
  }

  if (details.headers) {
    for (var prop in details.headers) {
      req.setRequestHeader(prop, details.headers[prop]);
    }
  }

  req.send((details.data) ? details.data : null);
  GM_log("< GM_xmlhttpRequest.chromeStartRequest");
}

// arranges for the specified 'event' on xmlhttprequest 'req' to call the
// method by the same name which is a property of 'details' in the content
// window's security context.
GM_xmlhttpRequester.prototype.setupRequestEvent =
function(unsafeContentWin, req, event, details) {
  GM_log("> GM_xmlhttpRequester.setupRequestEvent");

  if (details[event]) {
    req[event] = function() {
      GM_log("> GM_xmlhttpRequester -- callback for " + event);

      var responseState = {
        // can't support responseXML because security won't
        // let the browser call properties on it
        responseText:req.responseText,
        readyState:req.readyState,
        responseHeaders:(req.readyState == 4 ?
                         req.getAllResponseHeaders() :
                         ""),
        status:(req.readyState == 4 ? req.status : 0),
        statusText:(req.readyState == 4 ? req.statusText : ""),
        finalUrl:(req.readyState == 4 ? req.channel.URI.spec : "")
      }

      GM_log("< GM_xmlhttpRequester unsafeContentWin " + unsafeContentWin);

      // Pop back onto browser thread and call event handler.
      // Have to use nested function here instead of GM_hitch because
      // otherwise details[event].apply can point to window.setTimeout, which
      // can be abused to get increased priveledges.
      new XPCNativeWrapper(unsafeContentWin, "setTimeout()")
        .setTimeout(function(){details[event](responseState);}, 0);

      GM_log("< GM_xmlhttpRequester -- callback for " + event);
    }
  }

  GM_log("< GM_xmlhttpRequester.setupRequestEvent");
};

//////////////////////////////////////////////////////////////////////////
//GM_ScriptStorage
function GM_ScriptStorage(scripturl) {
	this.strage = {};
	this._url = scripturl;
}

GM_ScriptStorage.prototype.setValue = function(name, val) {
  if (!GM_apiLeakCheck("GM_setValue")) {
    return;
  }

  //this.strage[name] = val;
  Application.storage.set(this._url + "." + name, val);
};

GM_ScriptStorage.prototype.getValue = function(name, defVal) {
  if (!GM_apiLeakCheck("GM_getValue")) {
    return;
  }

  //return this.strage[name] ? this.strage[name] : defVal;
  return Application.storage.get(this._url + "." + name, defVal);
};

/////////////////////////////////////////////////////////////////////////
//GM_addStyle
function GM_addStyle(doc, css) {
  var head, style;
  head = doc.getElementsByTagName("head")[0];
  if (!head) { return; }
  style = doc.createElement("style");
  style.type = "text/css";
  style.innerHTML = css;
  head.appendChild(style);
}

/////////////////////////////////////////////////////////////////////////
//GM_openInTab
function GM_openInTab(unsafewindow, uri)
{
	if(unsafewindow && unsafewindow.gBrowser)
		unsafewindow.gBrowser.addTab(uri);
}

/////////////////////////////////////////////////////////////////////////
//GM_Resources
function GM_Resources(script){
	this.script = script;
}

GM_Resources.prototype.getResourceURL = function(name) {
  if (!GM_apiLeakCheck("GM_getResourceURL")) {
    return;
  }

  return this.getDep_(name).dataContent;
};

GM_Resources.prototype.getResourceText = function(name) {
  if (!GM_apiLeakCheck("GM_getResourceText")) {
    return;
  }

  return this.getDep_(name).textContent;
};

GM_Resources.prototype.getDep_ = function(name) {
  var resources = this.script.resources;
  for (var i = 0, resource; resource = resources[i]; i++)
    if (resource.name == name)
      return resource;
  throw new Error("No resource with name: " + name); // NOTE: Non localised string
};