FBL.ns(function () { with (FBL) {

	const Cc = Components.classes;
	const Ci = Components.interfaces;
	
	const ioService = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);
	const chromeReg = Cc["@mozilla.org/chrome/chrome-registry;1"].createInstance(Ci.nsIToolkitChromeRegistry);
	
	const fbmStatusIcon = $('firebugmonkeyStatusBarIcon');
	
	var RETURN_CONTINUE = Ci.jsdIExecutionHook.RETURN_CONTINUE;

	Firebug.firebugmonkey_Model = extend(Firebug.Module, { 
		sourcemap:{},
		
		initialize: function() {					
			//Firebug.filterSystemURLs = false;
			//extensions.firebug.service.filterSystemURLs
			
			//Firebug.showAllSourceFiles = true;
			//extensions.firebug.service.showAllSourceFiles;false
			
			this.SANDBOX_XUL_PATH = "chrome://firebugmonkey/content/sandbox.xul";
			Firebug.firebugmonkey.init();
			
			this.setPrefForDebug();
			this.setFunctionForDebug();
		},
	
		initializeUI: function(){
			var fbfbToolbar = document.getElementById("fbToolbar");
			var fbMenu = document.getElementById("fbPanelBar1-buttons");
			var fbmMenu = document.getElementById("firebugmonkeyMenu");	
			fbmMenu.insertafter="fbBreakOnNextButton";
			fbMenu.appendChild(fbmMenu);
		},
	
		setPrefForDebug : function(){	
//			var filterSystemURLs = Application.prefs.getValue("extensions.firebug.service.filterSystemURLs", true);
//			if(filterSystemURLs)
//				Application.prefs.setValue("extensions.firebug.service.filterSystemURLs", false);
//			
//			var showAllSourceFiles = Application.prefs.getValue("extensions.firebug.service.showAllSourceFiles", false);
//			if(!showAllSourceFiles)
//				Application.prefs.setValue("extensions.firebug.service.showAllSourceFiles", true);
			
			Firebug.filterSystemURLs = false;
			Firebug.showAllSourceFiles = true;
		},
		
		setFunctionForDebug : function(){	
			var getFrameContext = function(frame){
				var win = getFrameScopeWindowAncestor(frame);
				return win ? TabWatcher.getContextByWindow(win) : null;
			}
			
			var getFrameScopeWindowAncestor = function(frame){  // walk script scope chain to bottom, null unless a Window
				var scope = frame.scope;
				if (scope){	
					while(scope.jsParent)
						scope = scope.jsParent;
				
					if (scope.jsClassName == "Window" || scope.jsClassName == "ChromeWindow")
						return  scope.getWrappedValue();
				}else
					return null;
			}
			
			var fbm_supportsGlobal = 
				'var cc = ((frameWin && TabWatcher) ? TabWatcher.getContextByWindow(frameWin) : null);'		
				+'if (Firebug.firebugmonkey.enable && !cc){'
				//+ 'Application.console.log("fbm_supportsGlobal url = " +  frameWin.location.toString());'
		 		+	'if(frameWin.location!=undefined && frameWin.location.toString() == Firebug.firebugmonkey_Model.SANDBOX_XUL_PATH){'
				+		'for (var i = 0; i < TabWatcher.contexts.length; ++i){'			
				+			'if(TabWatcher.contexts[i].window.location.toString() == Firebug.firebugmonkey.testURL){'
				+				'cc = TabWatcher.contexts[i];'
	        	+				'this.breakContext = cc;'
	        	+				'return !!cc;'
				+			'}'
			    +		'}'
		        +	'}'
	            +'}';	
			if ('supportsGlobal' in Firebug.Debugger) {
			  eval('Firebug.Debugger.supportsGlobal = '+
			    Firebug.Debugger.supportsGlobal.toSource().replace(
			      '{',
			      '$&' + fbm_supportsGlobal
			    )
			  );
			}	
	
//			var firebug_resetBreakpoints =  Firebug.Debugger.fbs.resetBreakpoints;
//			Firebug.Debugger.fbs.resetBreakpoints = function(sourceFile, lastLineNumber){		
//				if(Firebug.firebugmonkey.enable 
//					&& Firebug.firebugmonkey.sourcehrefs 
//					&& Firebug.firebugmonkey_Model.hasSourcehref(sourceFile.href)){
//					Firebug.firebugmonkey_Model.sourcemap[sourceFile.href] = sourceFile;
//				}
//				firebug_resetBreakpoints(sourceFile, lastLineNumber);
//			}
			var fbm_resetBreakpoints = 
				'if(Firebug.firebugmonkey.enable '
				+	'&& Firebug.firebugmonkey.sourcehrefs){ '
				+	'Firebug.firebugmonkey_Model.sourcemap[sourceFile.href] = sourceFile;'
				+'}';
				//'Firebug.firebugmonkey_Model.setSourceMap(sourceFile);';
			if ('setJSDBreakpoint' in Firebug.Debugger.fbs) {
				  eval('Firebug.Debugger.fbs.setJSDBreakpoint = '+
						  Firebug.Debugger.fbs.setJSDBreakpoint.toSource().replace(
				      '{',
				      '$&' + fbm_resetBreakpoints
				    )
				  );
				}			
			
			var fbm_onBreak = 
			  'const TYPE_DEBUGGER_KEYWORD = Components.interfaces.jsdIExecutionHook.TYPE_DEBUGGER_KEYWORD;'
				+ 'if(!this.breakContext && !getFrameContext(frame)){'
				//+ 'Application.console.log("fbm_onBreak = " + frame.script.fileName);'
	        	+	'if(Firebug.firebugmonkey.enable && Firebug.firebugmonkey.sourcehrefs && Firebug.firebugmonkey_Model.hasSourcehref(frame.script.fileName)){'
				+		'let context;'
			    +		'for (var i = 0; i < TabWatcher.contexts.length; ++i){'
			    +       	'if(TabWatcher.contexts[i].window.location!=undefined && TabWatcher.contexts[i].window.location.toString() == Firebug.firebugmonkey.testURL){'
			    +           	'context = TabWatcher.contexts[i];'
				+				'break;'
			    +            '}'
			    +        '}'	
				+		'if(!context){'
				+			'return RETURN_CONTINUE;'
				+		'}'
				+		'return this.stop(context, frame, type);'
	        	+	'}'
				+'}';	
			if ('onBreak' in Firebug.Debugger) {
			  eval('Firebug.Debugger.onBreak = '+
			    Firebug.Debugger.onBreak.toSource().replace(
			      '{',
			      '$&' + fbm_onBreak
			    )
			  );
			}
	     
			Firebug.firebugmonkey_Model.fbmScript='';
				 
			var fbm_makeURI = 
			    'try{'			
			 	+	'if(Firebug.firebugmonkey.enable){'
			 	//+ 'Application.console.log("fbm_makeURI urlString = " +  urlString);'
				+		'if(urlString.indexOf(Firebug.firebugmonkey_Model.SANDBOX_XUL_PATH) != -1){'
				+			'Firebug.firebugmonkey_Model.fbmScript = Firebug.firebugmonkey_Model.getFbmScriptFromblistUrl(urlString);'
			    +    		'return ioService.newURI(Firebug.firebugmonkey_Model.SANDBOX_XUL_PATH, null, null);'
				+		'}'
		    	+	'}'			
			    +'}catch(exc){Components.utils.reportError("fbm_makeURI  : " + exc);}';	
			if ('makeURI' in FBL) {
			  eval('FBL.makeURI = '+
			    FBL.makeURI.toSource().replace(
			      '{',
			      '$&' + fbm_makeURI
			    )
			  );
			}
					
			var fbm_getResource = 
				 'if(aURL.indexOf("chrome/content/sandbox.xul")>0){'		
				+	'var spec = Firebug.firebugmonkey_Model.getFbmScriptSpecFromSandbox(aURL);'
				+	'if(spec){'
				+		'aURL = spec;'
				+	'}'
	            +'}';	
			if ('getResource' in FBL) {
			  eval('FBL.getResource = '+
			    FBL.getResource.toSource().replace(
			      '{',
			      '$&' + fbm_getResource
			    )
			  );
			}
		
			
			var fbm_unwrapObject =
				'try {'
				//+'	var currentFunction = arguments.callee.caller.toString();'
				//+'Application.console.log("currentFunction = " +currentFunction);'
				//+'	if(currentFunction.indexOf("var insecureObject = unwrapObject(object);") != -1){'
				+'	if(Firebug.firebugmonkey_Model.isTargetFunction(arguments.callee.caller)){'
				+'		for (var name in object) {'
				+'			if (typeof(object[name]) == "xml" && object[name].toString().indexOf("<") != -1 && object[name].toString().indexOf(">") != -1){'
				//+'				Application.console.log("object name = " +name + " : " + object[name]);'
				+'				object[name] = object[name].toString();'
				+'			}'
				+'		}'
				+'	}'
				+'} catch (exc) {'
				+'	 Application.console.log("fbm_unwrapObject exc = " +exc);'
				+'}';
	
			try{
				if ('unwrapObject' in FBL) {
					eval('FBL.unwrapObject = '+
							FBL.unwrapObject.toSource().replace(
					      '{',
					      '$&' + fbm_unwrapObject
					    )
					  );
				}
			}catch(exc){
				Application.console.log("fbm_unwrapObject exc= " + exc); 
			}	
			
			Firebug.firebugmonkey_Model.hasSourcehref = function(href){		
				if(href.indexOf(this.SANDBOX_XUL_PATH + ' -> ') == -1) return false;
				
				var hrefs = href.split(' -> ');
				var sourcehref = hrefs[1];
				if(sourcehref.indexOf("file:/", 0) == 0 && sourcehref.indexOf("file:///", 0) == -1){
					sourcehref = sourcehref.replace("file:\/", "file:\/\/\/");
				}
				return Firebug.firebugmonkey.sourcehrefs.indexOf(sourcehref, 0) == -1 ? false : true;		
			}
		},
		
	    shutdown: function(){
			Firebugmonkey_ConsoleListener.unregisterListener();
	    },
	    
	    isTargetFunction: function(func){
	    	var strfunc = func.toString();
	    	return strfunc.indexOf("var insecureObject = unwrapObject(object);") != -1;
	    },
		isE4XObject: function(obj){
    		return (typeof(obj) == "xml" 
    			&& obj.toString().indexOf("<") != -1 
    			&& obj.toString().indexOf(">") != -1);
    	}, 
	
		getFbmScriptSpecFromSandbox : function(spec){	
			try{
	        	var uri = chromeReg.convertChromeURL(ioService.newURI(this.SANDBOX_XUL_PATH, null, null));
	        	if(uri.spec == spec){
	        		return Firebug.firebugmonkey_Model.fbmScript;
	        	}	
			}catch(e){
				//Application.console.log("getFbmScriptSpecFromSandbox ERROR = " + e);
			}
		
			return false;
		},	
		
		getFbmScriptFromblistUrl : function(href){	
			if(href.indexOf(this.SANDBOX_XUL_PATH + ' -> ') == -1) return;	
				
			var hrefs = href.split(' -> ');	
			var sourcehref = hrefs[1];
			if(sourcehref.indexOf("file:/", 0) == 0 && sourcehref.indexOf("file:///", 0) == -1){
				sourcehref = sourcehref.replace("file:\/", "file:\/\/\/");
			}	
			
			return sourcehref;
		},	
		
		showAboutDialog: function(){
	      var extensionManager = CCSV("@mozilla.org/extensions/manager;1", "nsIExtensionManager");
	      openDialog("chrome://mozapps/content/extensions/about.xul", "",
	          "chrome,centerscreen,modal", "urn:mozilla:item:firebugmonkey@konkon.jp", extensionManager.datasource);
	  	},
	  	
	  	onToggleEnableMenu : function(menuitem){ 		
	  		this.update();
	  	},
	  	
	  	onClickStatusIcon : function(event){		
	  		this.update();
	  	},
	  	
	  	onPopupShowing : function(){
	  		var enablemenu = document.getElementById("firebugmonkeyEnableMenu");
	  		enablemenu.setAttribute("checked", Firebug.firebugmonkey.enable);		
	  		
	  		var enablestatusbar = document.getElementById("firebugmonkeyEnableMenu-statusbar");
	  		enablestatusbar.setAttribute("checked", Firebug.firebugmonkey.enable);	
	  	},
	 
	  	showPreference : function(){
	  		var fu = "chrome,titlebar,toolbar,centerscreen,dialog=no,resizable";
	  		openDialog("chrome://firebugmonkey/content/preference.xul", "Preferences", fu);	
	  	},
	  	
	  	openDoc : function(){
	  		var docurl = "http://orange.zero.jp/zbn39616.pine/addon/firebugmonkey/index.html";
	  		getBrowser().addTab(docurl);
	  		//var bundle = document.getElementById('firebugmonkey-bundle');
	  		//var locate = bundle.getString("LOCATE");
	  	},
	  	
	  	update : function(isenable){
	  		Firebug.firebugmonkey.enable = !Firebug.firebugmonkey.enable; 		
	  		Application.prefs.setValue("extensions.firebugmonkey.enable", Firebug.firebugmonkey.enable);
	  		fbmStatusIcon.setAttribute("enable", Firebug.firebugmonkey.enable == true?"on":"off");
	  	}
	});
	
	var Firebugmonkey_ConsoleListener = {
		aConsoleService:null,
		
		init : function(){
			this.aConsoleService = Cc["@mozilla.org/consoleservice;1"].getService(Ci.nsIConsoleService);
		},
	    
	    registerListener : function(){
	    	this.aConsoleService.registerListener(this); 
	    },
	    
	    unregisterListener : function(){
	    	this.aConsoleService.unregisterListener(this); 
	    },
	    
	    observe:function( aMessage ){
		    try{
				if (!(aMessage instanceof Ci.nsIScriptError)) return;
	
				var category = aMessage.category
				if(category == "chrome javascript" || category == "XPConnect JavaScript"){	
					if(Firebug.firebugmonkey_Model.hasSourcehref(aMessage.sourceName)){
						for(s in Firebug.firebugmonkey.enablescripts){
							var script = Firebug.firebugmonkey.enablescripts[s];
							if(aMessage.sourceName.indexOf(script.tmpFileUri.spec) != -1){
								var rev = script._offsets;
								for ( var off = rev.length-1; off >= 0; off--){
									if(rev[off].offset <= aMessage.lineNumber){
										var filespec = rev[off].filespec;
										var line = aMessage.lineNumber - rev[off].offset;		
										var scriptError = Cc["@mozilla.org/scripterror;1"].createInstance(Ci.nsIScriptError);
										scriptError.init(aMessage.errorMessage, filespec, null, line, aMessage.columnNumber, aMessage.errorFlag, aMessage.category);
	  									this.aConsoleService.logMessage(scriptError);
										return;								
									}
								}
							}
						}
					}
				}
		    }catch (exc){
	        	//alert("Error " + exc);
	            // Errors prior to console init will come out here, eg error message from Firefox startup jjb.
	        }
	    },
	    
	    QueryInterface: function (iid) {
			if (!iid.equals(Ci.nsIConsoleListener) && !iid.equals(Ci.nsISupports)) {
				throw Components.results.NS_ERROR_NO_INTERFACE;
			}
	        return this;
	    }
	};

	try{
		Firebugmonkey_ConsoleListener.init();
		Firebugmonkey_ConsoleListener.registerListener();
	}catch(exc){
		Components.utils.reportError("Firebugmonkey consoleListener init error  : " + exc);
	}

	Firebug.registerModule(Firebug.firebugmonkey_Model); 

}});