FBL.ns(function () { with (FBL) {
	
const Cc = Components.classes;
const Ci = Components.interfaces;

const ioService = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);

const SANDBOX_XUL_PATH = "chrome://firebugmonkey/content/sandbox.xul";
const FBM_SCRIPT_DIR = "fbm_scripts";
const FBM_SCRIPTLIST_FILE = "fbm_scripts.xml";

const fbmStatusIcon = $('firebugmonkeyStatusBarIcon');

Firebug.firebugmonkey = {
	testURL : null,
	sourcehrefs : null,
	srctmpmap : null,
	enable : false,
	enablescripts : null,
	  	
	init : function() {	

  		this.enable = Application.prefs.getValue("extensions.firebugmonkey.enable", false);
  		fbmStatusIcon.setAttribute("enable", this.enable == true?"on":"off");
  		
		Components.utils.import("resource://fbm_modules/greasemonkeyutil.js", this);
    	var loader = Cc["@mozilla.org/moz/jssubscript-loader;1"].getService(Ci.mozIJSSubScriptLoader);
    	loader.loadSubScript("chrome://firebugmonkey/content/convert2RegExp.js", this);
        loader.loadSubScript("chrome://global/content/XPCNativeWrapper.js");
  		
        this.GM_listen(window, "load", this.GM_hitch(this, "chromeLoad"));
  		this.GM_listen(window, "unload", this.GM_hitch(this, "chromeUnload"));
		this.sourcefiles = [];
		
		this.fileutil = {};
		Components.utils.import("resource://fbm_modules/fileutil.js", this.fileutil);	
		
		this.enablescripts =[];
	},

	chromeLoad : function(e) {	
		//this.tabBrowser = document.getElementById("content");
		this.appContent = document.getElementById("appcontent");	
		this.GM_listen(this.appContent, "DOMContentLoaded", this.GM_hitch(this, "contentLoad"));
		//    Components.lookupMethod(appContent, "addEventListener")(
	   // "DOMContentLoaded", this.contentLoad, null);
		
	},

	chromeUnload : function(e) {
		//this.tabBrowser.removeProgressListener(this);
		//Application.console.log("chromeUnload = " + e.target.URL);
	},
	
	contentUnload : function(e) {	
	},

	contentLoad : function(e) {
		if(!this.enable) return;
		
		var profiledir = Cc["@mozilla.org/file/directory_service;1"].getService(Ci.nsIProperties).get("ProfD", Ci.nsILocalFile);
		var scriptdir = this.fileutil.makeDir(profiledir.path, FBM_SCRIPT_DIR);
		var scriptmpdir = this.fileutil.makeDir(scriptdir.path, "tmp");
		
		var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
		file.initWithPath(scriptdir.path);
		file.append(FBM_SCRIPTLIST_FILE);
		var scriptsxmlfile = file.path;

		if(!file.exists()) 
		{
			Components.utils.reportError("!file.exists() " + file.path);
			return;
		}
		
		var XMLUtil ={};
		Components.utils.import("resource://fbm_modules/xmlutil.js", XMLUtil);
		var data = this.fileutil.read(scriptsxmlfile);
		var result = XMLUtil.XML2Obj.parseFromString(data);

	 	this.GM_Scripthrefs.clear();
	 	
	 	var scripts = [];
	 	this.sourcehrefs = [];

	 	for(let i=0;i<result.length; i++)
	 	{	 		
	 		
	 		var filename = result[i]["src"];
	 		var enable   = result[i]["enable"] == 'true'?true:false;
	 		if(filename && enable && enable == true)
	 		{	 	
	 			try
	 			{
	 				var fbmscript = new Firebug.firebugmonkey.Script(e.target.URL, scriptdir, scriptmpdir, filename, enable);
	 				fbmscript.init();
	 				scripts.push(fbmscript);
	 			}
	 			catch(e)
	 			{
	 				Components.utils.reportError('firebugmonkey error : ' + e);
	 			}	 			
	 		}
	 	}

	 	this.enablescripts = (function() 
	 	{
		    function testMatch(script) {
		      return script.enable && script.matchesURL(e.target.URL);
		    }
	    	return scripts.filter(testMatch);
	 	})();
	 	
	 	if(this.enablescripts.length >0)
		{
			//Firebug.Debugger.fbs.filterSystemURLs = false;
			//Firebug.filterSystemURLs = false;
			//Firebug.showAllSourceFiles = true;
			
	 		this.testURL = e.target.URL;
			var fbmsandbox;	
		  	var safeWin = e.target.defaultView;
		  	var unsafeWin = safeWin.wrappedJSObject;
		  	var href = safeWin.location.href;
		  	var unsafeLoc = new XPCNativeWrapper(unsafeWin, "location").location;
		  	var href = new XPCNativeWrapper(unsafeLoc, "href").href;
		    var safeWindow = new XPCNativeWrapper(unsafeWin);

			var appSvc = Components.classes["@mozilla.org/appshell/appShellService;1"].getService(Components.interfaces.nsIAppShellService);
			var gmutil = {};
			Components.utils.import("resource://fbm_modules/greasemonkeyutil.js", gmutil);
		    for ( var int = 0; int < this.enablescripts.length; int++) 
		    {		    	
		    	var enablescript = this.enablescripts[int];
		    	try
		    	{
		    		this.fileutil.write(enablescript.getConcatText, this.fileutil.getFileFromURLSpec(enablescript.tmpFileUri.spec).path);
		    	}
		    	catch(e)
		    	{
		    		Components.utils.reportError('firebugmonkey error : ' + e);
		    	}
		    	
		 		this.sourcehrefs.push(enablescript.tmpFileUri.spec);
		 		this.GM_Scripthrefs.setHref(enablescript.tmpFileUri.spec);
		    	
		    	fbmsandbox = {};	
			    fbmsandbox.window = safeWindow;
			    fbmsandbox.document = fbmsandbox.window.document;
			    fbmsandbox.unsafeWindow = unsafeWin;
			    fbmsandbox.location = unsafeLoc;
	
				fbmsandbox.GM_log = gmutil.GM_log;			
				fbmsandbox.GM_xmlhttpRequest =  gmutil.GM_hitch(new gmutil.GM_xmlhttpRequester(fbmsandbox.unsafeWindow, appSvc.hiddenDOMWindow), "contentStartRequest");
				fbmsandbox.XPathResult = Components.interfaces.nsIDOMXPathResult;
				
				fbmsandbox.GM_addStyle = gmutil.GM_hitch(gmutil, "GM_addStyle", fbmsandbox.document);
				
				var storage = new gmutil.GM_ScriptStorage(enablescript.ID);
				fbmsandbox.GM_setValue = gmutil.GM_hitch(storage, "setValue");
				fbmsandbox.GM_getValue = gmutil.GM_hitch(storage, "getValue");
				
				fbmsandbox.GM_openInTab = gmutil.GM_hitch(gmutil, "GM_openInTab", window);
				fbmsandbox.GM_registerMenuCommand = function(title, func){};
				
				var resources = new gmutil.GM_Resources(enablescript);
				fbmsandbox.GM_getResourceURL = gmutil.GM_hitch(resources, "getResourceURL");
				fbmsandbox.GM_getResourceText = gmutil.GM_hitch(resources, "getResourceText");	
				
				if(!Application.prefs.getValue("extensions.firebugmonkey.option.enablexpcom", false))
					fbmsandbox.Components = null;
				
				Application.storage.set(enablescript.ID, fbmsandbox);	    	
			}
			
			this.evalInSandbox(this.sourcehrefs, 0);
		}
		else
		{
		}
	},
		  
	evalInSandbox : function(sourcefiles, level)
	{
		try
		{				
			/*
	      	var script = document.createElementNS('http://www.w3.org/1999/xhtml', 'script');
	    	//var script = doc.createElementNS('http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul', 'script');
	      	script.setAttribute('type', 'application/javascript; version=1.8');
	      	script.setAttribute('src',   sourcefiles[0]);
	      	document.documentElement.appendChild(script);
			*/
			
		  	var env = document.createElement('browser');
		  	//var env = document.createElement('iframe');
		  	//var env = document.createElementNS('http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul', 'browser');
		  	env.setAttribute('type', 'content-frame');
		  	env.setAttribute('src', SANDBOX_XUL_PATH);
		  	document.documentElement.appendChild(env);
		  	
		  	env.addEventListener('load', function(){
		  	
			var doc = env.contentDocument.wrappedJSObject || env.contentDocument;
		    var win = env.contentWindow.wrappedJSObject || env.contentWindow;	    
			//var doc = env.contentDocument;
		    //var win = env.contentWindow;	
			//var doc = env.contentDocument.wrappedJSObject;
		    //var win = env.contentWindow.wrappedJSObject;	  
		    
		    var loaded = 0;
		    
		    function clearSandbox(){
		    	
		    	env.parentNode.removeChild(env);
		    }
		    win.clearSandbox = clearSandbox;
		    
		    function onLoad(){
		      loaded++;   
		      if(loaded == uris.length){
		      	clearSandbox();
		      	if(sourcefiles.length-1 > level)
		      	{
		      		Firebug.firebugmonkey.evalInSandbox(sourcefiles, ++level);
		      	}
		      }	
		    }
		    
		    var uris = [];
		    uris.push(sourcefiles[level]);		    
		    uris.forEach(function(uri){
		      	var script = doc.createElementNS('http://www.w3.org/1999/xhtml', 'script');
		      	//var script = doc.createElement('script');
		    	//var script = doc.createElementNS('http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul', 'script');
		      	script.setAttribute('type', 'application/javascript; version=1.8');
		      	script.setAttribute('src',   uri);
		      	//script.setAttribute('src',   "chrome://firebugmonkey/content/cal.js");
		      	doc.documentElement.appendChild(script);
		      	
//		      	var old = "alert(10);";
//		      	var script = doc.getElementById("contentyy");
//		      	Application.console.log("script = " + script);
//		      	 var dataURI = "data:application/vnd.mozilla.xul+xml," + encodeURIComponent(old);
//		      	//script.setAttribute("src",dataURI);
//		      	script.location = dataURI;
//		      	//doc.documentElement.appendChild(script);
		      
		      	script.addEventListener('load', onLoad, true);
		      	script.addEventListener('error', onLoad, true);
		    });
		    
		  }, true);	  
		}
		catch(e)
		{	
		}

		return env;	
	}
};


}});
