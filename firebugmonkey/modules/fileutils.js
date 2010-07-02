var EXPORTED_SYMBOLS =["FileUtils"];

const Cc = Components.classes;
const Ci = Components.interfaces;

const IOService = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);

var FileUtils = {
	
	/**
	 * getAbsoluteFile("../test.js", base)
	 * @param string relative path 
	 * @param nsIFile base path 
	 */
	getAbsoluteFile:function(path, base){
		var basefile;
	 	switch(typeof(base)){
	 	 case 'file':
	 		 basefile = base;
	 		 break;
	 	 case 'string':
	 		basefile = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	 		basefile.initWithPath(base);
	 		//Application.console.log("basefile = " + basefile.path);
	 		 break;
	 	 default: 
	 		 break;
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
	 * @param string path path 
	 * @param string name name 
	 * 
	 * @return nsIFile
	 */
	getFile:function(path, name)
	{
		var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
		file.initWithPath(path);
		if(!name) file.append(name);
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
			//Application.console.log("getAbsolutePath err= " + e);
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
		  
		if(typeof(uri) == "string"){
			uri = ioService.newURI(uri, null, null);
		}
		
		  var scriptableStream=Cc["@mozilla.org/scriptableinputstream;1"].getService(Ci.nsIScriptableInputStream);
		  var unicodeConverter = Cc["@mozilla.org/intl/scriptableunicodeconverter"].createInstance(Ci.nsIScriptableUnicodeConverter);
		  unicodeConverter.charset = charset;

		  var channel = ioService.newChannelFromURI(uri);
		  var input=channel.open();
		  scriptableStream.init(input);
		  var str=scriptableStream.read(input.available());
		  
		  scriptableStream.close();
		  input.close();

		  try {
		    return unicodeConverter.ConvertToUnicode(str);
		  } catch( e ) {
		    return str;
		  }		
	},
	
	/**
	 * 
	 * @param nsIFile
	 */
	getBinaryContent:function(file){
		var ioService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);
         var url = ioService.newURI(ioService.newFileURI(file).spec, null, null);

         if (!url || !url.schemeIs("file")) throw "Expected a file URL.";

         var pngFile = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
         pngFile.initWithPath(url.path);

         var istream = Cc["@mozilla.org/network/file-input-stream;1"].createInstance(Ci.nsIFileInputStream);
         istream.init(pngFile, -1, -1, false);

         var bstream = Cc["@mozilla.org/binaryinputstream;1"].createInstance(Ci.nsIBinaryInputStream);
         bstream.setInputStream(istream);

         var bytes = bstream.readBytes(bstream.available());
         return bytes;
	},
	
	/**
	 * 
	 * 
	 * @param uri URI
	 */
	getBinaryContentFromURI:function(uri) {
	    var ioService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);

	    var channel = ioService.newChannelFromURI(uri);
	    var input = channel.open();

	    var bstream = Cc["@mozilla.org/binaryinputstream;1"].createInstance(Ci.nsIBinaryInputStream);
	    bstream.setInputStream(input);

	    var bytes = bstream.readBytes(bstream.available());

	    bstream.close();
	    input.close();
	    
	    return bytes;
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
	 * @param nsIFile or string basedir
	 * @param string newdir
	 * 
	 * @return nsIFile newdir
	 */
	makeDir:function(basedir, newdir)
	{
		var dir = null;
		if(typeof(basedir) == "string"){
			dir = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
			dir.initWithPath(basedir);	
			dir.append(newdir);
		}else if(typeof(basedir) == "file"){
			basedir.append(newdir);
			dir = basedir;
		}
		if(!dir.exists())
			dir.create(Ci.nsIFile.DIRECTORY_TYPE, 0755);
			
		return dir;
	},
	
	getFileFromURI:function(uri) 
	{
		try {		
			var fileHandler = IOService.getProtocolHandler('file').QueryInterface(Ci.nsIFileProtocolHandler);				
			return fileHandler.getFileFromURLSpec(uri);
		}catch(e){
			this.ERROR = e;
			//ConsoleService.logStringMessage("getFileFromURLSpec error = " + e);
		}
		return null;
	}
}
var copy = function(sourcefile, destdir, distfile)
{
	  var aFile = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
	  if (!aFile) return false;

	  var aDir = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
	  if (!aDir) return false;

	  aFile.initWithPath(sourcefile);
	  aDir.initWithPath(destdir);
	  aFile.copyTo(aDir, distfile);
}

var write = function(data, path)
{
	var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	file.initWithPath(path);

	if (!file.exists()) {
		file.create(file.NORMAL_FILE_TYPE, 0666);
	}

	var charset = 'UTF-8';
	var string = data;
	var fileStream = Cc['@mozilla.org/network/file-output-stream;1'].createInstance(Ci.nsIFileOutputStream);
	fileStream.init(file, 0x02 | 0x20 , 0x200, false);
	var converterStream = Cc['@mozilla.org/intl/converter-output-stream;1'].createInstance(Ci.nsIConverterOutputStream);
	converterStream.init(fileStream, charset, string.length, Ci.nsIConverterInputStream.DEFAULT_REPLACEMENT_CHARACTER);

	converterStream.writeString(string);
	converterStream.close();
	fileStream.close();
}

var read = function(path)
{
	var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);

	file.initWithPath(path);
	var charset = 'UTF-8';
	var fileStream = Cc['@mozilla.org/network/file-input-stream;1'].createInstance(Ci.nsIFileInputStream);
	fileStream.init(file, 1, 0, false);

	var converterStream = Cc['@mozilla.org/intl/converter-input-stream;1'].createInstance(Ci.nsIConverterInputStream);
	converterStream.init(fileStream, charset, fileStream.available(),
	converterStream.DEFAULT_REPLACEMENT_CHARACTER);

	var out = {};
	converterStream.readString(fileStream.available(), out);
	var fileContents = out.value;
	converterStream.close();
	fileStream.close();
	return fileContents;
}

var makeDir = function(dir, dirname)
{
	var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	file.initWithPath(dir);	
	file.append(dirname);
	if(!file.exists())
		file.create(Ci.nsIFile.DIRECTORY_TYPE, 0755);
		
	return file;
}

function makeURIFromSpec(aURI)
{
	try {
		var newURI;
		aURI = aURI || '';
		if (aURI && aURI.match(/^file:/)) {
			var fileHandler = IOService.getProtocolHandler('file').QueryInterface(Ci.nsIFileProtocolHandler);
			var tempLocalFile = fileHandler.getFileFromURLSpec(aURI);

			newURI = IOService.newFileURI(tempLocalFile);
		}
		else {
			newURI = IOService.newURI(aURI, null, null);
		}

		return newURI;
	}
	catch(e){
		//ConsoleService.logStringMessage("makeURIFromSpec error = " + e);
	}
	return null;
}


function getURIFromnsIFile(file)
{
	return IOService.newURI(IOService.newFileURI(file).spec, null,null);
}

function getFileFromURLSpec(aURI) 
{
	try {		
		if (!aURI) aURI = '';
	
		if (aURI.indexOf('chrome://') == 0) 
		{
			var ChromeRegistry = Cc["@mozilla.org/chrome/chrome-registry;1"].getService(Ci.nsIChromeRegistry);
			aURI = ChromeRegistry.convertChromeURL(makeURIFromSpec(aURI)).spec;
		}
	
		if (aURI.indexOf('file://') != 0) return null;
	
		var fileHandler = IOService.getProtocolHandler('file')
							.QueryInterface(Ci.nsIFileProtocolHandler);
						
		return fileHandler.getFileFromURLSpec(aURI);
	
	}catch(e){
		//ConsoleService.logStringMessage("getFileFromURLSpec error = " + e);
	}
	return null;
}

function makeFileWithPath(aPath)
{
	var newFile = Cc['@mozilla.org/file/local;1']
					.createInstance(Ci.nsILocalFile);
	newFile.initWithPath(aPath);
	return newFile;
}

function getURLFromFile(aFile) 
{
	return IOService.newFileURI(aFile);
}


var getURLFromFilePath = function(aPath) 
{
	var tempLocalFile = makeFileWithPath(aPath);
	return getURLFromFile(tempLocalFile);
}

function getBinaryContents(file) {
    var ioService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);

    var channel = ioService.newChannelFromURI(getURLFromFile(file));
    var input = channel.open();

    var bstream = Cc["@mozilla.org/binaryinputstream;1"].createInstance(Ci.nsIBinaryInputStream);
    bstream.setInputStream(input);

    var bytes = bstream.readBytes(bstream.available());

    bstream.close();
    input.close();
    
    return bytes;
}

function getContents(file, charset) {
  if( !charset ) {
    charset = "UTF-8"
  }
  var ioService=Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);
  var scriptableStream=Cc["@mozilla.org/scriptableinputstream;1"].getService(Ci.nsIScriptableInputStream);
  var unicodeConverter = Cc["@mozilla.org/intl/scriptableunicodeconverter"].createInstance(Ci.nsIScriptableUnicodeConverter);
  unicodeConverter.charset = charset;

  var channel = ioService.newChannelFromURI(getURLFromFile(file));
  var input=channel.open();
  scriptableStream.init(input);
  var str=scriptableStream.read(input.available());
  
  scriptableStream.close();
  input.close();

  try {
    return unicodeConverter.ConvertToUnicode(str);
  } catch( e ) {
    return str;
  }
}


