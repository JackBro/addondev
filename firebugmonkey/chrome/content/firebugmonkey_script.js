FBL.ns(function () { with (FBL) {
	
	const Cc = Components.classes;
	const Ci = Components.interfaces;	
	
	const ioservice = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);	
/**
 * @param string url  
 * @param scriptdir nsILocalFile
 * @param scriptmpdir nsILocalFile
 * @param string filename 
 * 
 */
Firebug.firebugmonkey.Script = function(url, scriptdir, scriptmpdir, filename, enbale)
{
	this._url = url;
	//this._ioservice = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);
	this._localfile = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	
	this._fileutil = {};
	Components.utils.import("resource://fbm_modules/fileutil.js", this._fileutil);
	
	this._filename = filename;
	this._enable = enbale;
	
	this._localfile.initWithPath(scriptdir.path);
	this._localfile.append(this._filename);
	this._basedirUri = ioservice.newURI(ioservice.newFileURI(this._localfile).spec, null,null);

	this._fileuri = ioservice.newURI(this._basedirUri.resolve(this._filename), null, null);

	this._localfile.initWithPath(scriptmpdir.path);
	this._localfile.append(this._filename);
	this._tmpfileuri = ioservice.newURI(ioservice.newFileURI(this._localfile).spec, null, null);
	
	this._includes = [];
	this._excludes = [];
	this._requireUris =[];
	this._resources = [];
	
	this._offsets = [];
}

Firebug.firebugmonkey.Script.prototype = 
{
	get	enable(){ return this._enable; },
		
	//get ID(){ return this._url + this._filename; },
	get ID(){ return this._filename; },
	
	get url(){ return this._url; },
	
	get tmpFileUri()
	{	
		return this._tmpfileuri;	
	},
	
	get resources() { return this._resources.concat(); },
	
	matchesURL: function(url) {
    	function test(page) {
      		return Firebug.firebugmonkey.convert2RegExp(page).test(url);
    	}

    	return this._includes.some(test) && !this._excludes.some(test);
	},
	
	get getText()
	{
		return this.source;
	},
	
	get getConcatText()
	{	
		var srcoffset = 1;
 		var requireSources=[];
 		for(var uri in this._requireUris)
 		{
 			//Application.console.log("_requireUris uri = " + this._requireUris[uri].spec);
 			this._offsets.push({filespec:this._requireUris[uri].spec, offset:srcoffset});	
 			var src = this.loadText(this._requireUris[uri]);
 			srcoffset += src.split("\n").length;
 			requireSources.push(src);
 		}
 		
 		this._offsets.push({filespec:this._basedirUri.spec, offset:srcoffset});
 		var reqsrc = requireSources.length==0 ? "" : requireSources.join("\n") + "\n";
       	var scriptSrc = 
       		"var sandbox = Application.storage.get('" + this.ID + "', null);"
       		+"with(sandbox){(function(){\n"
            + reqsrc
            + this.source
            +"\n})()}";	
                         
        return scriptSrc;
	},
	
	init : function()
	{
		this.source = this.loadText(this._fileuri);
		this.parse();		
	},
	
	parse : function()
	{
		//var _source = this.source;
		var basedirUri = this._basedirUri;
		
		var lines = this.source.match(/.+/g);
	    var lnIdx = 0;
	    var result = {};
	    var foundMeta = false;
	
	    while ((result = lines[lnIdx++])) {
	      if (result.indexOf("// ==UserScript==") == 0) {
	        foundMeta = true;
	        break;
	      }
	    }
	
	    //gather up meta lines
	    if (foundMeta) {
	      while ((result = lines[lnIdx++])) {
	        if (result.indexOf("// ==/UserScript==") == 0) {
	          break;
	        }
	
	        var match = result.match(/\/\/ \@(\S+)(?:\s+([^\n]+))?/);
	        if (match === null) continue;
	
	        var header = match[1];
	        var value = match[2];
	        if (value) { // @header <value>
	          switch (header) {
	          	case "name":
	          	break;
	          	case "namespace":
	          	break;
	          	case "include":
	          		this._includes.push(value);
	          	break;
	          	case "exclude":
		          	this._excludes.push(value);
		          	break;
	            case "require":
	            	try
	            	{
	            		var requirepath = basedirUri.resolve(value);
	            		//Application.console.log("requirepath = " + requirepath);
	            		this._requireUris.push(ioservice.newURI(requirepath, null, null));
	            	}
	            	catch(e)
	            	{
	            		Components.utils.reportError("incorrect require " + e);
	            	}
	            	break;
	            case "resource":
	            	try
	            	{
	            	var res = value.match(/(\S+)\s+(.*)/);
	            	var resName = res[1];
	            	var scriptResource = new Firebug.firebugmonkey.ScriptResource();
	            	scriptResource._name = resName;
	            	var resfile = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	            	var resPath = this._fileutil.getFileFromURLSpec(basedirUri.resolve(res[2])).path;
	            	
	            	resfile.initWithPath(resPath);
	    	        if (!resfile.exists())
	    	        {
	    	        	
	    	        	var consoleSvc = Cc["@mozilla.org/consoleservice;1"].getService(Ci.nsIConsoleService);
	    	        	  var scriptError = Cc["@mozilla.org/scripterror;1"].createInstance(Ci.nsIScriptError);
	    	        	           //scriptError.init(aMessage, aSourceName, aSourceLine, aLineNumber, 
	    	        	           //                 aColumnNumber, aFlags, aCategory);
	    	        	  scriptError.init("error !resfile.exists()", resPath, 1, 1, 0, scriptError.errorFlag, null);
	    	        	  consoleSvc.logMessage(scriptError);
	
	    	        
	    	        	//Components.utils.reportError("error !resfile.exists()");
	    	        	//throw "ERROR : not exists " + resPath;
	    	            //return false;
	    	        }
	            	scriptResource._file = resfile;
	            	this._resources.push(scriptResource);
	            	}
	            	catch(e)
	            	{
	            		Components.utils.reportError("incorrect resource " + e);
	            	}
	            	break;
	          }
	        }
	      }
	    }
	    else
	    {
	    	
	    }   
	},
	
	loadText : function(aURI)
	{
	  try {	
		  var channel = ioservice.newChannelFromURI(aURI);
		  var stream  = channel.open();
		
		  var scriptableStream = Cc['@mozilla.org/scriptableinputstream;1'].createInstance(Ci.nsIScriptableInputStream);
		  scriptableStream.init(stream);
				    
		  var fileContents = scriptableStream.read(scriptableStream.available());
		
		  scriptableStream.close();
		  stream.close();
		
//		  var unicodeConverter = Cc['@mozilla.org/intl/scriptableunicodeconverter'].createInstance(Ci.nsIScriptableUnicodeConverter);
//		  unicodeConverter.charset = 'UTF-8';  
//		  return unicodeConverter.ConvertToUnicode(fileContents);
		  
		  return fileContents;
	  }
	  catch(e) 
	  {
	  	Components.utils.reportError("error loadText " + e);
	  }
	}
}

Firebug.firebugmonkey.ScriptResource = function() {
	this.fileutil = {};
	Components.utils.import("resource://fbm_modules/fileutil.js", this.fileutil);	

	this._name = null;	
	this._file = null;
	this._mimetype = null;
	this._charset = null;
}

Firebug.firebugmonkey.ScriptResource.prototype = {
		get name() { return this._name; },	
		
		get textContent() { return this.fileutil.getContents(this._file); },

		get dataContent() {
			var mimeService = Cc['@mozilla.org/mime;1'].getService(Ci.nsIMIMEService);
		    var mimetype = mimeService.getTypeFromFile(this._file);
		    
		    var appSvc = Cc["@mozilla.org/appshell/appShellService;1"].getService(Ci.nsIAppShellService);

		    var window = appSvc.hiddenDOMWindow;
		    var binaryContents = this.fileutil.getBinaryContents(this._file);

		    //if (this._charset && this._charset.length > 0) {
		    //  mimetype += ";charset=" + this._charset;
		    //}

		    return "data:" + mimetype + ";base64," +
		      window.encodeURIComponent(window.btoa(binaryContents));
		}		
}

}});