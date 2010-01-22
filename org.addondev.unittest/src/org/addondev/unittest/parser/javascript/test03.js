/* See license.txt for terms of usage */

// Debug lines are marked with  at column 120
// Use variable name "fileName" for href returned by JSD, file:/ not same as DOM
// Use variable name "url" for normalizedURL, file:/// comparable to DOM
// Convert from fileName to URL with normalizeURL
// We probably don't need denormalizeURL since we don't send .fileName back to JSD

// ************************************************************************************************
// Constants


var st = "t";
const CLASS_ID = Components.ID("{a380e9c0-cb39-11da-a94d-0800200c9a66}");
//var MyObject = 
//{
//    message3: null,
//    hello3: function (arg0){
//		arg0=100;
//		var h= arg0. ;
//	}
//}
////
////MyObject.hello3("b");
//MyObject.message3=st;
//MyObject.add = {};

var st_l = st.length;

function FirebugService()
{

    FBTrace = Cc["@joehewitt.com/firebug-trace-service;1"]
                 .getService(Ci.nsISupports).wrappedJSObject.getTracer("extensions.firebug");

    if (FBTrace.DBG_FBS_ERRORS)
        FBTrace.sysout("FirebugService Starting");

    fbs = this;

    this.wrappedJSObject = this;
    //this.timeStamp = new Date();  /* explore */
//    this.breakpoints = breakpoints; // so chromebug can see it /* explore */
//    this.onDebugRequests = 0;  // the number of times we called onError but did not call onDebug
//    fbs._lastErrorDebuggr = null;
//
//
//    if(FBTrace.DBG_FBS_ERRORS)
//        this.osOut("FirebugService Starting, FBTrace should be up\n");
//
//    this.enabled = false;
//    this.profiling = false;
//
//    prefs = PrefService.getService(nsIPrefBranch2);
//    fbs.prefDomain = "extensions.firebug.service."
//    prefs.addObserver(fbs.prefDomain, fbs, false);
//
//    var observerService = Cc["@mozilla.org/observer-service;1"]
//        .getService(Ci.nsIObserverService);
//    observerService.addObserver(QuitApplicationGrantedObserver, "quit-application-granted", false);
//    observerService.addObserver(QuitApplicationRequestedObserver, "quit-application-requested", false);
//    observerService.addObserver(QuitApplicationObserver, "quit-application", false);
//
//    this.scriptsFilter = "all";
//    // XXXjj For some reason the command line will not function if we allow chromebug to see it.?
//    this.alwayFilterURLsStarting = ["chrome://chromebug", "x-jsd:ppbuffer", "chrome://firebug/content/commandLine.js"];  // TODO allow override
//    this.onEvalScriptCreated.kind = "eval";
//    this.onTopLevelScriptCreated.kind = "top-level";
//    this.onEventScriptCreated.kind = "event";
//
//    this.onXScriptCreatedByTag = {}; // fbs functions by script tag
//    this.nestedScriptStack = Components.classes["@mozilla.org/array;1"]
//                        .createInstance(Components.interfaces.nsIMutableArray); 
}
FirebugService.prototype =
{
    osOut: function(str)
    {
    
    },
	get lastErrorWindow()
	{
	}
}
//
//function func0(arg0)
//{
//	this.h0=0;
//	this.h0=10;
//	h0=3;
//	
//	this.h1=0;
//	arg0=0;
//	arg=p;
//	arg=100;
//	var ttt=0;
//	ttt = 100;
//}
//
//func0("e");

//
//MyObject3.test={
//		nn:null
//}
//MyObject3.prototype = 
//{
//    message3: null,
//    hello3: function (arg0){
//    }
//}
//
//MyObject3.test.add = {};

//var mobj = new Object();
//mobj.start = function() {
//  this.obj = "obj";
//  this.prop = "prop";	
//}
//

//function Accelimation(obj, prop, to, time, zip, unit) {
//  this.obj = obj;
//  this.prop = prop;
//}
//
//Accelimation = function(o) {
//	this.test0 = o;
//	this.test1 = 1;
//	//var hh=0;
//};
//
//Accelimation._add = function(o) {
//	this.test = 1;
//};
//
//Accelimation.prototype.start = function() {
//		var tt=0;
//  this.t0 = Time;
//  this.t1 = Time;
//  Accelimation._add(this);
//  //var hh2=0;
//};


//
//function ArrayEnumerator(array)
//{
//	var tt=0;
//	this.index = 0;
//	this.index2 = 0;
//	//this.array = array;
//	this.hasMoreElements = function()
//	{
//		//return (this.index < array.length);
//	}
//	this.getNext = function()
//	{
//		//return this.array[++this.index];
//	}
//}

//var test0, tes1, test2;

//Firebug.Debugger = extend(Firebug.ActivableModule,
//{
//	fbs: fbs,
//	
//    initialize: function()
//    {
//        this.nsICryptoHash = Components.interfaces["nsICryptoHash"];
//        this.debuggerName =  window.location.href+"--"+FBL.getUniqueId(); /*@explore*/
//        this.toString = function() { return this.debuggerName; } /*@explore*/ //bug
//        this.hash_service = CCSV("@mozilla.org/security/hash;1", "nsICryptoHash");
//
//        this.wrappedJSObject = this;  // how we communicate with fbs
//        this.panelName = "script";
//
//        Firebug.broadcast = function encapsulateFBSBroadcast(message, args)
//        {
//            fbs.broadcast(message, args);
//        }
//        
//        this.onFunctionCall = bind(this.onFunctionCall, this);  
//        Firebug.ActivableModule.initialize.apply(this, arguments);
//    },
//    
//    getCurrentFrameKeys: function()
//    {
//		this.hhhh=200;
//		var tt = 10;
//    },
//    
//    focusWatch: function(context)
//    {
//        if (context.detached)
//            context.chrome.focus();
//        else
//            Firebug.toggleBar(true);
//
//        context.chrome.selectPanel("script");
//
//        var watchPanel = context.getPanel("watches", true);
//        if (watchPanel)
//        {
//        	Firebug.CommandLine.isReadyElsePreparing({title:"reaady", tf:function(){var p = tr;}});
//            watchPanel.editNewWatch();
//        }
//    },
//});
//FBL.ns(function() {
//	var tt=0;
////	const p=0;
////	function aa()
////	{
////		
////	}
////	aa();
////	h=o;
//	var Firebug.Debugger = extend(Firebug.ActivableModule,
//	{
//	    fbs: fbs, // access to firebug-service in chromebug under browser.xul.DOM.Firebug.Debugger.fbs /*@explore*/
//	    getCurrentFrameKeys: function(context)
//	    {        
//	        this.getFrameKeys(context);
//	    }
//	});
////	aa();
//	
//	//var tt=0;
//	//nn=0;
//});
//
//function Car(brand) {
//   this.brand = brand;
//}
//Car.prototype.getBrand = function() {
//	this.ch = 10;
//   return this.brand;
//}
//
//Car.prototype.getBrand.bb = function() {
//	this.ch2 = 10;
//}
//
//var b = new Car("");

//for (var index = 0, test=0; index < array.length; index++) {
//	m=0;
//	m++;
//}
//
//CmdUtils.CreateCommand({
//	   name:'macro',
//	   author: { name: "Kurt Cagle", email: "kurt@oreilly.com"}
//	});
