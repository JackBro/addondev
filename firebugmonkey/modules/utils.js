var EXPORTED_SYMBOLS =["FileUtils", "Utils"];

const Cc = Components.classes;
const Ci = Components.interfaces;

const IOService = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);
const Application = Cc["@mozilla.org/fuel/application;1"].getService(Ci.fuelIApplication);

//Application.console.log();

var FileUtils = {
	
	/**
	 * getAbsoluteFile("../test.js", base)
	 * @param string relative path 
	 * @param nsIFile base path 
	 */
	getAbsoluteFile:function(path, base){
		var basefile = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	 	if(base instanceof Ci.nsIFile){
	 		basefile.initWithPath(base.path);
	 	}else if(isString(base)){
	 		basefile.initWithPath(base);
	 		//Application.console.log("basefile = " + basefile.path);
	 	};
		var uri = IOService.newURI(IOService.newFileURI(basefile).spec, null, null).QueryInterface(Ci.nsIURL);

		return this.getFileFromURI(uri.resolve(path));
	},
		
	/**
	 * 
	 * @param string filename
	 */
	getFileNameExceptExt:function(filename){
		var name = filename;
		if ((n = filename.lastIndexOf(".")) != -1) {
		    name = filename.substring(0, n);
		}
		return name;
	},
	
	/**
	 * 
	 * @param nsIFile or string path path 
	 * @param string name name 
	 * 
	 * @return nsIFile
	 */
	getFile:function(path, name)
	{
		var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
		if(path instanceof Ci.nsIFile){
	 		file.initWithPath(path.path);
		}else if(isString(path)){
	 		file.initWithPath(path);
		}
		if(name) file.append(name);
		return file;
	},
	
	/**
	 * get text
	 * @param nsIFile
	 */
	getContent:function(file, charset){
		var data = "";
		if( !charset ) {
			charset = "UTF-8"
		 }
		try {
			var fis = Cc['@mozilla.org/network/file-input-stream;1'].createInstance(Ci.nsIFileInputStream);
			fis.init(file, 1, 0, false);
			
			const replacementChar = Ci.nsIConverterInputStream.DEFAULT_REPLACEMENT_CHARACTER;
			var is = Cc["@mozilla.org/intl/converter-input-stream;1"].createInstance(Ci.nsIConverterInputStream);
			is.init(fis, charset, 1024, replacementChar);
			var str = {};
			while (is.readString(4096, str) != 0) {
				data+=str.value;
			}
		}catch(e){
			this.ERROR = e;
			data = null;
		}finally{
			if(fis)fis.close();
		}
		return data;
	},

	/**
	 * 
	 * @param URI or string uri
	 * 
	 */
	getContentFromURI:function(uri, charset){

		var ioService=Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);
		if( !charset ) {
			charset = "UTF-8"
		}
		  
		if(isString(uri)){
			uri = ioService.newURI(uri, null, null);
		}

		var text = null;
		try {
			var scriptableStream=Cc["@mozilla.org/scriptableinputstream;1"].getService(Ci.nsIScriptableInputStream);
			var unicodeConverter = Cc["@mozilla.org/intl/scriptableunicodeconverter"].createInstance(Ci.nsIScriptableUnicodeConverter);
			unicodeConverter.charset = charset;
	
			var channel = ioService.newChannelFromURI(uri);
			var input=channel.open();
			scriptableStream.init(input);
			var str=scriptableStream.read(input.available());
			text = unicodeConverter.ConvertToUnicode(str);
			
			if(scriptableStream) scriptableStream.close();
			if(input) input.close();
		} catch( e ) {
			this.ERROR = e;
			text = null;
		}finally{
			if(scriptableStream) scriptableStream.close();
			if(input) input.close();
		}
		return text;
	},
	
	/**
	 * 
	 * @param nsIFile
	 */
	getBinaryContent:function(file){
         var istream = Cc["@mozilla.org/network/file-input-stream;1"].createInstance(Ci.nsIFileInputStream);
         istream.init(file, -1, -1, false);

         var bstream = Cc["@mozilla.org/binaryinputstream;1"].createInstance(Ci.nsIBinaryInputStream);
         bstream.setInputStream(istream);

         var bytes = bstream.readBytes(bstream.available());
         return bytes;
	},
	
	/**
	 * 
	 * @param string url
	 * @param nsIFile savefile
	 */
	getBinaryFileFromURI:function(uri, savefile) {
		var res = true;
		try{
		    var ioService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);
	
			if(isString(uri)){
				uri = ioService.newURI(uri, null, null);
			}
		    
		    var channel = ioService.newChannelFromURI(uri);
		    var input = channel.open();
	
		    var bstream = Cc["@mozilla.org/binaryinputstream;1"].createInstance(Ci.nsIBinaryInputStream);
		    bstream.setInputStream(input);
			
		    //Application.console.log("this.bstream.available() = " + bstream.available());
		    
		    var bytes = bstream.readBytes(bstream.available());

		    input.close();
		    bstream.close();
		    
		    this.writeBinary(savefile, bytes);
		    
	    }catch(e){
			this.ERROR = e;
			res = false;
		}
	    return res;
	},
	
	/**
	 * 
	 * @param string url
	 * @param nsIFile savefile
	 * 
	 * @return bool
	 */
	saveTextFileFromURI:function(url, savefile){
		var res = true;
		try{
			//var iurl = Cc["@mozilla.org/network/standard-url;1"].createInstance(Ci.nsIURL);
			//iurl.spec = url;

			//var wbp = Cc["@mozilla.org/embedding/browser/nsWebBrowserPersist;1"].createInstance(Ci.nsIWebBrowserPersist);
			//wbp.saveURI(iurl, null, null, null, null, savefile);
			var text = this.getContentFromURI(url);
			this.write(savefile, text);
		}catch(e){
			this.ERROR = e;
			res = false;
		}
		return res;
	},
	
	/**
	 * 
	 * @param nsIFile file
	 * @param string text
	 * 
	 * @return bool OK:true, NG:false
	 */
	write:function(file, text){
		var res = true;
		try {
			if (!file.exists()) {
				file.create(file.NORMAL_FILE_TYPE, 0666);
			}
			var fos = Cc['@mozilla.org/network/file-output-stream;1'].createInstance(Ci.nsIFileOutputStream);
			fos.init(file, 0x02 | 0x20 , 0x200, false);
			
			var charset = "UTF-8";
			var os = Cc["@mozilla.org/intl/converter-output-stream;1"].createInstance(Ci.nsIConverterOutputStream);
			os.init(fos, charset, 0, 0x0000);
			os.writeString(text);

		}catch(e){
			this.ERROR = e;
			res = false;
		}finally{
			if(os) os.close();
		}
		return res;
	},
	
	/**
	 * 
	 * @param nsIFile file
	 * @param byte byte
	 * 
	 * @return bool OK:true, NG:false
	 */
	writeBinary:function(file, bytes){
		var res = true;
		try {
			//file.createUnique( Ci.nsIFile.NORMAL_FILE_TYPE, 600);
			            
			var stream = Cc["@mozilla.org/network/safe-file-output-stream;1"].createInstance(Ci.nsIFileOutputStream);
			stream.init(file, 0x04 | 0x08 | 0x20, 664, 0);
			            
			stream.write(bytes, bytes.length);
			if (stream instanceof Components.interfaces.nsISafeOutputStream) {
			    stream.finish();
			} else {
			    stream.close();
			}
		}catch(e){
			this.ERROR = e;
			res = false;
		}finally{
			if(stream) stream.close();
		}
		return res;
	},
	
	/**
	 * 
	 * @param nsIFile or string basedir
	 * @param string newdir
	 * @param bool unique name
	 * 
	 * @return nsIFile newdir
	 */
	makeDir:function(basedir, newdir, isunique)
	{
		var dir = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
		if(basedir instanceof Ci.nsIFile){
	 		dir.initWithPath(basedir.path);
		}else if(isString(basedir)){
	 		dir.initWithPath(basedir);	
		}
		
		dir.append(newdir);
		if(isunique){
			dir.createUnique(Ci.nsIFile.DIRECTORY_TYPE, 0755);
		}else{
			if(!dir.exists())
				dir.create(Ci.nsIFile.DIRECTORY_TYPE, 0755);
		}
		return dir;
	},
	
	/**
	 * 
	 * @param nsIFile file
	 * @param bool recursive
	 */
	deleteFile:function(file, recursive){
		if (file instanceof Components.interfaces.nsILocalFile){
			if(file.exists()){
				//Application.console.log("deleteFile = " + file.path);
  				file.remove(recursive);
			}
		}
	},
	
	getFileFromURI:function(uri) 
	{
		try {		
			var fileHandler = IOService.getProtocolHandler('file').QueryInterface(Ci.nsIFileProtocolHandler);				
			return fileHandler.getFileFromURLSpec(uri);
		}catch(e){
			this.ERROR = e;
		}
		return null;
	},
	
	getProfileDir:function(){
		return Cc["@mozilla.org/file/directory_service;1"].getService(Ci.nsIProperties).get("ProfD", Ci.nsILocalFile);
	}
}

var Utils = {
	FBM_SCRIPT_DIR:"fbm_scripts",
	FBM_SCRIPTJSON:"fbm_scripts.json",
	version:30,
	_scriptTemplateFile:null, 
	_FbmScriptDir:null,
	
	get FbmScriptDir(){
		if(this._FbmScriptDir == null){
			this._FbmScriptDir = FileUtils.makeDir(FileUtils.getProfileDir(), this.FBM_SCRIPT_DIR);
		}
		return this._FbmScriptDir;
	},
	
	get FbmScriptTemplateFile(){
		if(this._scriptTemplateFile == null){
			this._scriptTemplateFile = FileUtils.getFile(this.FbmScriptDir, "scripttemplate");
		}
		return this._scriptTemplateFile;
	},
	
	loadSetting:function(){
		var file = FileUtils.getFile(this.FbmScriptDir, this.FBM_SCRIPTJSON);
		var data = {version:this.version, files:[]};
		if(file.exists()){
			try{
				var jsonstr = FileUtils.getContent(file);
				var result = JSON.parse(jsonstr);
	
				if(result.version == undefined){
					data.version = this.version;
					var scriptDir = FileUtils.getFile(FileUtils.getProfileDir(), this.FBM_SCRIPT_DIR);
					for(let key in result){	
						var elm = result[key];
						var newdir = FileUtils.makeDir(scriptDir, elm.dir, true);
						var fullpath = FileUtils.getFile(FileUtils.getFile(scriptDir, elm.dir), elm.filename).path;
						data.files.push({dir:newdir.leafName, filename:elm.filename, fullpath:fullpath, enable:elm.enable});
						//Application.console.log("loadSetting data elm.dir = " + elm.dir);
						//Application.console.log("loadSetting data elm.filename = " + elm.filename);
						//Application.console.log("loadSetting data fullpath = " + fullpath);
						//Application.console.log("loadSetting data elm.enable = " + elm.enable);
					}
					var jsonstr = JSON.stringify(data);
					FileUtils.write(file, jsonstr);
				}else{
					data = result;
				}
			}catch(e){
				this.ERROR = e;
			}
		}
		return data;
	},
	
	saveSetting:function(scripts){
		try{
			var file = FileUtils.getFile(this.FbmScriptDir, this.FBM_SCRIPTJSON);
			var data = {version:this.version, files:scripts};
			var jsonstr = JSON.stringify(data);
			FileUtils.write(file, jsonstr);
		}catch(e){
			Components.utils.reportError("firebugmonkey : fault save " + file.leafName + " : "+ e);
		}		
	},
	
	makeDefaultScriptTemplate:function(){
		try{
			var templatefile = this.FbmScriptTemplateFile;
			if(!templatefile.exists()){
				FileUtils.write(templatefile, Utils.getDefaultScriptTemplate());
			}
		}catch(e){
			Components.utils.reportError("firebugmonkey : error make script template file : " + e);
		}
	},
	
	getDefaultScriptTemplate:function(){
		return "// ==UserScript== \n" +
		"// @name           {name} \n"+
		"// @namespace      http://{name}/ \n"+
		"// @description    script template \n" +
		"// @include        * \n"
		"// ==UserScript== \n"
		;
	},
	
	getScriptTemplate:function(){
		try{
			var templatefile = this.FbmScriptTemplateFile;
			if(templatefile.exists()){
				return FileUtils.getContent(templatefile);
			}
		}catch(e){
			Components.utils.reportError("firebugmonkey : error make script template file : " + e);
		}		
		return getDefaultScriptTemplate();
	},
	
	saveScriptTemplate:function(template){
		try{
			var templatefile = this.FbmScriptTemplateFile;
			FileUtils.write(templatefile, template);
		}catch(e){
			Components.utils.reportError("firebugmonkey : error save script template file : " + e);
		}
	}
}

function isString(obj)
{
   if (obj == null)
      return false;
   return (typeof(obj) == "string" || obj instanceof String);
}


