FBL.ns(function () { with (FBL) {
	
	const Cc = Components.classes;
	const Ci = Components.interfaces;	
	
	const ioservice = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);	
	const consolesvc = Cc["@mozilla.org/consoleservice;1"].getService(Ci.nsIConsoleService);
/**
 * @param string url target page 
 * @param nsIFile scriptdir 
 * @param object script {dir,filename, fullpath, enable}
 */
Firebug.firebugmonkey.Script = function(url, scriptdir, script){
	
	Components.utils.import("resource://fbm_modules/utils.js", this);	
	
	this._url = url;
	this._scriptdir = scriptdir;
	this._filename = script.filename;
	this._fullpath = script.fullpath;
	this._enable = script.enable;
		
	this._includes = [];
	this._excludes = [];
	this._requires =[];
	this._resources = [];
	
	this._offsets = [];
}

Firebug.firebugmonkey.Script.prototype = 
{
	get	enable(){ return this._enable; },
	
	get ID(){ return this._url + "." + this._filename; },
	
	get namespace(){ return this._namespace; },
	
	get url(){ return this._url; },
	
	get uri(){ return this._scripturi;	 },
	
	get tmpUri(){ return this._scripttmpuri; },
	get tmpFile(){ return this._scripttmpfile; },
	
	get resources() { return this._resources.concat(); },
	
	matchesURL: function(url) {
    	function test(page) {
      		return Firebug.firebugmonkey.convert2RegExp(page).test(url);
    	}

    	return this._includes.some(test) && !this._excludes.some(test);
	},
	
	get concatSrc(){
		var scriptSrc = null;
		try{
		var offset = 1;
 		var requiresrc=[];
 		for(var key in this._requires){
 			let url = this._requires[key];
 			this._offsets.push({url:url, offset:offset});	
 			
 			//Application.console.log("url = " + url);
 			
 			let src = this.FileUtils.getContentFromURI(url);
 			offset += src.split("\n").length;
 			requiresrc.push(src);
 		}
 		
 		this._offsets.push({url:this.uri.spec, offset:offset});
 		var requires = requiresrc.length==0 ? "" : requiresrc.join("\n");
       	scriptSrc = 
       		"var sandbox = Application.storage.get('" + this.ID + "', null);"
       		+"with(sandbox){(function(){\n"
            + requires + "\n"
            + this.source
            +"\n})()}";	
		}catch(e){
			scriptSrc = null;
	    	Components.utils.reportError("firebugmonkey : error concatSrc " + e);
	    	this._enable = false;
		}
        return scriptSrc;		
	},
	
	init : function(){
		this._scriptfile = this.FileUtils.getFile(this._fullpath);
		
		//Application.console.log("this._scriptdir = " + this._scriptdir.path);
		//Application.console.log("this._filename = " + this._filename);

		this._scripttmpfile = this.FileUtils.getFile(this._scriptdir, this._filename);
		this._scripturi = ioservice.newFileURI(this.FileUtils.getFile(this._scriptfile));

		//Application.console.log("this._scriptfile = " + this._scriptfile.path);
						
		this._scripttmpuri = ioservice.newFileURI(this.FileUtils.getFile(this._scripttmpfile));
		
		//Application.console.log("this._scripttmpfile = " + this._scripttmpfile.path);	
		
		this.source = this.FileUtils.getContent(this._scriptfile);
		//Application.console.log("this._scriptfile src = " + this.source);	
		this.parse();	
	},
	
	parse : function(){	
		var lines = this.source.match(/.+/g);
		if(lines == null){
			//Application.console.log(this._scripturi.spec + " is empty");	
			return;
		}
	    var lnIdx = 0;
	    var result = {};
	    var foundMeta = false;
	    
	    var requireUrls = [];
	    var resourceUrls = [];
	

	    while ((result = lines[lnIdx++])) {	
	      //if (result.indexOf("// ==UserScript==") == 0) {
	      if (result.match(/\/\/\s*==UserScript==/) != null) {

	        foundMeta = true;
	        break;
	      }
	    }
	
	    //gather up meta lines
	    if (foundMeta) {
	      while ((result = lines[lnIdx++])) {
	        //if (result.indexOf("// ==/UserScript==") == 0) {
	        if (result.match(/\/\/\s*==UserScript==/) != null) {
	          break;
	        }
	
	        var match = result.match(/\/\/\s*\@(\S+)(?:\s+([^\n]+))?/);
	        if (match === null) continue;
	
	        var header = match[1];
	        var value = match[2];
	        if (value) { // @header <value>
	          switch (header) {
	          	case "name":
	          		break;
	          	case "namespace":
	          		this._namespace = value;
	          		
	          		break;
	          	case "include":
	          		this._includes.push(value);
	          		break;
	          	case "exclude":
		          	this._excludes.push(value);
		          	break;
	            case "require":          		
            		if(/^[a-z]+:[/][/]/i.test(value)){
            			requireUrls.push({url:value, line:lnIdx});
            		}else{ 
            			//local
            			let reqfile = this.FileUtils.getAbsoluteFile(value, this._scriptfile.parent);
            			if(reqfile.exists()){
            				let requrl = ioservice.newFileURI(this.FileUtils.getFile(reqfile.path)).spec;
            				this._requires.push(requrl);
            			}else{
            				this._scriptError("error not find require " + reqfile.path, 
            						this._scripturi.spec, lnIdx);
            			}
            		}
	            	break;
	            case "resource":
	            	var res = value.match(/(\S+)\s+(.*)/);
	            	var resName = res[1];
	            	var resPath = res[2];
	            	if(/^[a-z]+:[/][/]/i.test(resPath)){
	            		resourceUrls.push({name:resName, url:resPath, line:lnIdx});
	            	}else{
	            		//local
            			let resfile = this.FileUtils.getAbsoluteFile(resPath, this._scriptfile.parent);
            			if(resfile.exists()){
            				var scriptResource = new Firebug.firebugmonkey.ScriptResource();
            				scriptResource._name = resName;
            				scriptResource._file = resfile;
            				this._resources.push(scriptResource);	
            			}else{
            				this._scriptError("error not find resource " + resfile.path, 
            						this._scripturi.spec, lnIdx);
            			}
	            	}
	            	break;
	          	}
	        }
	      }
	      
	      this.loadRequires(requireUrls);
	      this.loadResources(resourceUrls);
	      
	    } else {
	    	Components.utils.reportError("firebugmonkey : not meta data " + this._scriptfile.path);
	    	this._enable = false;
	    }   
	},
	
	loadRequires:function(requireUrls){
		for(let key in requireUrls){
			let url = requireUrls[key].url;
			let name = this._getLastSegment(url);
			name = name.replace(/\?/g,'_');
			let localfile = this.FileUtils.getFile(this._scriptfile.parent, name)
			if(!localfile.exists()){
				let res = this.FileUtils.saveTextFileFromURI(url, localfile);
				if(!res){
					Components.utils.reportError("firebugmonkey : error get file" 
							+ localfile.path + " " + this.FileUtils.ERROR);
					this._enable = false;
				}
			}
			var localurl = ioservice.newFileURI(localfile).spec;
			this._requires.push(localurl);
		}
	},
	
	loadResources:function(resourceUrls){
		for(let key in resourceUrls){
			let url = resourceUrls[key].url;
			let name = this._getLastSegment(url);
			name = name.replace(/\?/g,'_');
			let localfile = this.FileUtils.getFile(this._scriptfile.parent, name);
			if(!localfile.exists()){
				let res = this.FileUtils.getBinaryFileFromURI(url, localfile);
				if(!res){
					Components.utils.reportError("firebugmonkey : error file get " 
							+ localfile.path + " " + this.FileUtils.ERROR);
					this._enable = false;
				}
			}
			var localurl = ioservice.newFileURI(localfile).spec;
			var scriptResource = new Firebug.firebugmonkey.ScriptResource();
			scriptResource._name = resourceUrls[key].name;
			scriptResource._file = localfile;
			this._resources.push(scriptResource);
		}		
	},
	
	/**
	 * 
	 * @param string url
	 * 
	 * @return string
	 */
	_getLastSegment:function(url){
		let name = null;
		let n;
		if ((n = url.lastIndexOf("/")) != -1) {
		    name = url.substring(n+1);
		}
		return name;	
	},
	
	_scriptError:function(msg, fileurl, line){
		this._enable = false;
		var scriptError = Cc["@mozilla.org/scripterror;1"].createInstance(Ci.nsIScriptError);
		scriptError.init("firebugmonkey : " + msg, fileurl, line, line, 0, scriptError.errorFlag, null);
		consolesvc.logMessage(scriptError);
	}
}

Firebug.firebugmonkey.ScriptResource = function() {
	Components.utils.import("resource://fbm_modules/utils.js", this);	

	this._name = null;	
	this._file = null;
	this._mimetype = null;
	this._charset = null;
}

Firebug.firebugmonkey.ScriptResource.prototype = {
		get name() { return this._name; },	
		
		get textContent() { 
			return this.FileUtils.getContent(this._file); 
		},

		get dataContent() {
			var mimeService = Cc['@mozilla.org/mime;1'].getService(Ci.nsIMIMEService);
		    var mimetype = mimeService.getTypeFromFile(this._file);
		    
		    var appSvc = Cc["@mozilla.org/appshell/appShellService;1"].getService(Ci.nsIAppShellService);

		    var window = appSvc.hiddenDOMWindow;
		    var binaryContents = this.FileUtils.getBinaryContent(this._file);

		    //if (this._charset && this._charset.length > 0) {
		    //  mimetype += ";charset=" + this._charset;
		    //}

		    return "data:" + mimetype + ";base64," +
		      window.encodeURIComponent(window.btoa(binaryContents));
		}
}

}});