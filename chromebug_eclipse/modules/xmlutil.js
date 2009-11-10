var EXPORTED_SYMBOLS = ["XML2Obj"];

const Cc = Components.classes;
const Ci = Components.interfaces;

/**
 * result = XMLUtil.parseFromString(data);
 * postdata xml(string)
 * 
 * for(i=0;i<result.length; i++)
 *		file = result[i]["filename"];
 *		line = parseInt(result[i]["line"]);
 */
var XML2Obj=
{
	result:null,
	xmlReader:Cc["@mozilla.org/saxparser/xmlreader;1"].createInstance(Ci.nsISAXXMLReader),
	
	parseFromString:function(data)
	{
		this.result = [];
		this.xmlReader.parseFromString(data, "text/xml");
		
		return this.result;
	},
	
	startElement:function(uri, localName, qName, attributes)
	{
		if(qName != "xml")
		{
		var attrs ={};
	    for(var i=0; i<attributes.length; i++) 
	    {
	    	var attrqname = attributes.getQName(i);
	    	attrs[attrqname] = decodeURIComponent(attributes.getValue(i));
	    }	
	    this.result.push(attrs);	
		}
	}
	
}

XML2Obj.xmlReader.contentHandler = {

  //nsISAXContentHandler
  startDocument: function() {
  },
  
  endDocument: function() {
  },
  
  startElement: function(uri, localName, qName, /*nsISAXAttributes*/ attributes) {
	  XML2Obj.startElement(uri, localName, qName, attributes);
  },
  
  endElement: function(uri, localName, qName) {
  },
  
  characters: function(value) {
  },
  
  processingInstruction: function(target, data) {
  },
  
  ignorableWhitespace: function(whitespace) {
  },
  
  startPrefixMapping: function(prefix, uri) {
  },
  
  endPrefixMapping: function(prefix) {
  },
  
  // nsISupports
  QueryInterface: function(iid) {
    if(!iid.equals(Components.interfaces.nsISupports) &&
       !iid.equals(Components.interfaces.nsISAXContentHandler))
      throw Components.results.NS_ERROR_NO_INTERFACE;
    return this;
  }
};