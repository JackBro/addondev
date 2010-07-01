var EXPORTED_SYMBOLS =["copy", "write", "read", "makeDir", "getURLFromFilePath", "getFileFromURLSpec", "getURIFromnsIFile", "getBinaryContents", "getContents"];

const Cc = Components.classes;
const Ci = Components.interfaces;

const IOService = Cc["@mozilla.org/network/io-service;1"].createInstance(Ci.nsIIOService);

var FileUtils = {
		
	_getStream:function(file){
		
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


