var EXPORTED_SYMBOLS = ["server", "client"];

const Cc = Components.classes;
const Ci = Components.interfaces;
const CC = Components.Constructor;

var Application = Cc["@mozilla.org/fuel/application;1"].getService(Ci.fuelIApplication);
var log = Application.console.log;

server = {

	isWorking:false,
	
	init:function(port)
	{
		this.port = port;
		this.socket = null;
		this.pathHandler = null;
	},
	
	pathHandler : function(queryString, postdata){},

	start: function(){
		this.isWorking = true;
		this.socket = Cc["@mozilla.org/network/server-socket;1"].createInstance( Ci.nsIServerSocket );
        try
		{
			var allowAccessesFromRemote = false;
    		this.socket.init( this.port, !allowAccessesFromRemote, -1 );
    		this.socket.asyncListen( this );
    	}
    	catch(ex)
    	{
    		Application.console.log("server start error : " + ex);
    	}
	},
	
 	stop: function(){
    	if(this.server) this.server.close();
 		this.socket = null;
		this.pathHandler = null;   
 	},
  
 	onSocketAccepted: function(aServer, aTransport){
  		//Application.console.log("server onSocketAccepted");
    	new HttpServerListener(aServer, aTransport);
 	},

 	onStopListening: function(aServer,aTransport){
    //dump("HttpServer.onStopListening\n");
	}
};


function HttpServerListener(aServer, aTransport){
	this._server = aServer;
	this._transport = aTransport;
	this.end = false;
	this.init();
}

HttpServerListener.prototype = {
  init: function(){
    //dump("Init HttpServerListener\n");
    
    //OPEN_BLOCKING
    //OPEN_UNBUFFERED
    this._input = this._transport.openInputStream(0, 0, 0);
    this._output = this._transport.openOutputStream(0, 0, 0);

    var streamPump = Cc["@mozilla.org/network/input-stream-pump;1"].createInstance(Ci.nsIInputStreamPump);
    streamPump.init(this._input, -1, -1, 0, 0, false);
    streamPump.asyncRead(this, null);
  },

  sendResponse: function(body){
    var responseHeader = new Array(
        "HTTP/1.0 200 OK",
        "Date: " + new Date().toUTCString(),
        "Content-Type: text/html; charset=utf-8",
        "Connection: close",
        "",
        "").join("\r\n");
        var datalist=this._data.slice("\r\n\r\n");
        var post=datalist[1];
    var responseBody = body ? body:"accept";
    var response = responseHeader + responseBody;

    this._output.write(response, response.length);
    this._output.close();
    this._input.close();
    //this._bInputStream.close();
  },
  onStartRequest: function(aRequest, aContext){
    //dump("onStartRequest\n");

    this._data = "";
    this._bInputStream = Cc["@mozilla.org/binaryinputstream;1"].createInstance(Ci.nsIBinaryInputStream);
  },

  onStopRequest: function(aRequest, aContext, aStatus){
    //dump("onStopRequest\n");
	  //log("###onStopRequest");
  },

  onDataAvailable: function(aRequest, aContext, aInputStream, aOffset, aCount){
	try
	{		
    this._bInputStream.setInputStream(aInputStream);
    var availableData = this._bInputStream.readBytes(aCount);

    this._data += availableData;
     //Application.console.log("server _data : " + this._data);
		var datas = this._data.split("\r\n\r\n");
		
		var header = datas[0];
		
		var headers =header.split("\r\n");
		var methods = headers[0].split(/\s/);
		
		//Application.console.log("onDataAvailable  methods[1] : " + methods[1]);
		
		var queryStrings = methods[1].match(/^(\/\?)(.*)/);
		var queryString;
		if(queryStrings) queryString = queryStrings[2];
		
		//Application.console.log("onDataAvailable queryStrings : " +queryStrings);
		
		if(methods[0] == "POST")
		{
			var postdata = datas[1]; 
			//var responseBody ;
			//if(this._data.match("(<\/xml>)$")){
			if(this._data.match(/<\/xml>/)){
				//log("server post data : " + methods[1] + " : " + postdata);
				let responseBody = server.pathHandler(queryString, postdata);
				//log("server responseBody : " + responseBody);
				//Application.console.log("server post data : " + methods[1] + " : " + postdata);
				this.sendResponse(responseBody);
			}
		}
		else
		{
			let responseBody = server.pathHandler(queryString, postdata);
			//Application.console.log("server get : " + methods[1]);
			this.sendResponse(responseBody);
		}
    }
	catch(ex)
    {
     	Application.console.log("onDataAvailable error : " + ex);
     }
  }

};
