FBL.ns(function () { with (FBL) {
	
const Cc = Components.classes;
const Ci = Components.interfaces;

const ioService = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);

//const SANDBOX_XUL_PATH = "chrome://firebugmonkey/content/sandbox.xul";
const FBM_SCRIPT_DIR = "fbm_scripts";
const FBM_SCRIPTLIST_FILE = "fbm_scripts.json";

const fbmStatusIcon = $('firebugmonkeyStatusBarIcon');

Firebug.firebugmonkey = {
	testURL : null,
	sourcehrefs : null,
	enable : false,
	enablescripts : null,
	  	
	init : function() {	
		Components.utils.import("resource://fbm_modules/fileutils.js", this);
	
  		this.enable = Application.prefs.getValue("extensions.firebugmonkey.enable", false);
  		fbmStatusIcon.setAttribute("enable", this.enable == true?"on":"off");
  		
		Components.utils.import("resource://fbm_modules/greasemonkeyutil.js", this);
    	var loader = Cc["@mozilla.org/moz/jssubscript-loader;1"].getService(Ci.mozIJSSubScriptLoader);
    	loader.loadSubScript("chrome://firebugmonkey/content/convert2RegExp.js", this);
        loader.loadSubScript("chrome://global/content/XPCNativeWrapper.js");
  		
        this.GM_listen(window, "load", this.GM_hitch(this, "chromeLoad"));
  		this.GM_listen(window, "unload", this.GM_hitch(this, "chromeUnload"));
		this.sourcefiles = [];
		
		this.enablescripts =[];
		
		this.stringBundle = null;
	},

	chromeLoad : function(e) {	
		this.appContent = document.getElementById("appcontent");	
		this.GM_listen(this.appContent, "DOMContentLoaded", this.GM_hitch(this, "contentLoad"));
	},

	chromeUnload : function(e) {
		//this.tabBrowser.removeProgressListener(this);
		//Application.console.log("chromeUnload = " + e.target.URL);
	},
	
	contentUnload : function(e) {	
	},

	contentLoad : function(e) {

		if(!this.enable) return;

		var profiledir = this.FileUtils.getProfileDir(); 
		var scriptdir = this.FileUtils.makeDir(profiledir, FBM_SCRIPT_DIR);
		var jsonfile = this.FileUtils.getFile(scriptdir, FBM_SCRIPTLIST_FILE);
		
		if(!jsonfile.exists()) 
		{
			Application.console.log("firebugmonkey : not find " + jsonfile.path);
			return;
		}

		let jsonstr = this.FileUtils.getContent(jsonfile);
		let result = JSON.parse(jsonstr);
		
	 	this.GM_Scripthrefs.clear();
	 	
	 	var scripts = [];
	 	this.sourcehrefs = [];

	 	for(let key in result){	 		
	 		var dirname = result[key]["dir"];
	 		var filename = result[key]["filename"];
	 		var enable   = result[key]["enable"];

			if(dirname && filename && (enable == true || enable == "true") ){
	 			//try{
	 				var dir = this.FileUtils.getFile(scriptdir, dirname);
	 				//Application.console.log("contentLoad dir = " + dirname);
	 				//Application.console.log("contentLoad dirfile = " + dir.path);

	 				var fbmscript = new Firebug.firebugmonkey.Script(e.target.URL, dir, filename, enable);
	 				fbmscript.init();
	 				scripts.push(fbmscript);
	 			//}catch(e){
	 			//	Components.utils.reportError('firebugmonkey error : ' + e);
	 			//}	 			
	 		}
	 	}

	 	this.enablescripts = (function() {
		    function testMatch(script) {
		      return script.enable && script.matchesURL(e.target.URL);
		    }
	    	return scripts.filter(testMatch);
	 	})();
	 	
	 	if(this.enablescripts.length >0){
			//Firebug.filterSystemURLs = false;
			//Firebug.showAllSourceFiles = true;
			if(!Firebug.firebugmonkey_Model.setPrefForDebug()){
				return;
			}
			
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
		    
			for ( let i = 0; i < this.enablescripts.length; i++) {
		    	let script = this.enablescripts[i];
	    		let src = script.concatSrc;
	    		if(src){
	    			try{
	    				this.FileUtils.write(script.tmpFile, src);
	    			}catch(e){
	    				Components.utils.reportError('firebugmonkey error copy : ' + e);
	    				continue;
	    			}
	    		}else{
	    			Components.utils.reportError('firebugmonkey : error not execute ' + script.uri.spec);
	    			continue;
	    		}
		    	
		 		this.sourcehrefs.push(script.tmpUri.spec);
		 		this.GM_Scripthrefs.setHref(script.tmpUri.spec);
		    	
		    	fbmsandbox = {};	
			    fbmsandbox.window = safeWindow;
			    fbmsandbox.document = fbmsandbox.window.document;
			    fbmsandbox.unsafeWindow = unsafeWin;
			    fbmsandbox.location = unsafeLoc;
	
				fbmsandbox.GM_log = gmutil.GM_log;			
				fbmsandbox.GM_xmlhttpRequest =  gmutil.GM_hitch(new gmutil.GM_xmlhttpRequester(fbmsandbox.unsafeWindow, appSvc.hiddenDOMWindow), "contentStartRequest");
				fbmsandbox.XPathResult = Components.interfaces.nsIDOMXPathResult;
				
				fbmsandbox.GM_addStyle = gmutil.GM_hitch(gmutil, "GM_addStyle", fbmsandbox.document);
				
				//var storage = new gmutil.GM_ScriptStorage(script.ID);
				//Application.console.log("script.namespace = "+script.namespace);
				var storage = new gmutil.GM_ScriptStorage(script.namespace);
				fbmsandbox.GM_setValue = gmutil.GM_hitch(storage, "setValue");
				fbmsandbox.GM_getValue = gmutil.GM_hitch(storage, "getValue");
				fbmsandbox.GM_deleteValue = gmutil.GM_hitch(storage, "deleteValue");
				fbmsandbox.GM_listValues = gmutil.GM_hitch(storage, "listValues");
				
				fbmsandbox.GM_openInTab = gmutil.GM_hitch(gmutil, "GM_openInTab", window);
				fbmsandbox.GM_registerMenuCommand = function(title, func){};
				
				var resources = new gmutil.GM_Resources(script);
				fbmsandbox.GM_getResourceURL = gmutil.GM_hitch(resources, "getResourceURL");
				fbmsandbox.GM_getResourceText = gmutil.GM_hitch(resources, "getResourceText");	
				
				fbmsandbox.GM_registerMenuCommand = gmutil.GM_registerMenuCommand;	
				
				if(!Application.prefs.getValue("extensions.firebugmonkey.option.enablexpcom", false))
					fbmsandbox.Components = null;
				
				Application.storage.set(script.ID, fbmsandbox);	    	
			}
			
			this.evalInSandbox(this.sourcehrefs, 0);
		}else{
		}
	},
		  
	evalInSandbox : function(sourcefiles, level){
		try{						
		  	var env = document.createElement('browser');
		  	env.setAttribute('type', 'content-frame');
		  	env.setAttribute('src', Firebug.firebugmonkey_Model.SANDBOX_XUL_PATH);
		  	document.documentElement.appendChild(env);
		  	
		  	env.addEventListener('load', function(){
		  	
			var doc = env.contentDocument.wrappedJSObject || env.contentDocument;
		    var win = env.contentWindow.wrappedJSObject || env.contentWindow;	    
		    var loaded = 0;
		    
		    function clearSandbox(){
		    	
		    	env.parentNode.removeChild(env);
		    }
		    win.clearSandbox = clearSandbox;
		    
		    function onLoad(){
		      loaded++;   
		      if(loaded == uris.length){
		      	clearSandbox();
		      	if(sourcefiles.length-1 > level){
		      		Firebug.firebugmonkey.evalInSandbox(sourcefiles, ++level);
		      	}
		      }	
		    }
		    
		    var uris = [];
		    uris.push(sourcefiles[level]);		    
		    uris.forEach(function(uri){
		      	var script = doc.createElementNS('http://www.w3.org/1999/xhtml', 'script');
		      	script.setAttribute('type', 'application/javascript; version=1.8');
		      	script.setAttribute('src',   uri);
		      	doc.documentElement.appendChild(script);
		      	 
		      	script.addEventListener('load', onLoad, true);
		      	script.addEventListener('error', onLoad, true);
		    });
		    
		  }, true);	  
		}catch(e){}

		return env;	
	},
	
	getStrbundleString: function(str){
		if(!this.stringBundle ){
			this.stringBundle = document.getElementById("firebugmonkey-bundle");
		}
		return strbundle.getString(str);
	}
};

}});
