///////////////////////////////////////
//(function() {
//	var lastContext = 0;
//	test1 = 10;
//	this.ff=0;
//})();


//FBL.ns(function() {
//	var lastContext = 0;
//});
//
//function func0(arg0)
//{
//	this.h0=0;
//	this.h0=10;
//	this.h1=0;
//	arg0=0;
//	arg=p;
//	var ttt=0;
//	ttt = 100;
//}
//
func0("ff");
//
//CmdUtils.CreateCommand({
//	   name:'macro',
//	   author: { name: "Kurt Cagle", email: "kurt@oreilly.com"}
//	});
//	
//CmdUtils.CreateCommand(tesarg);

///////////////////////////////////////
//(function() { 
//	with (XPCOMUtils) {
//		var lastContext = 0;
//		test2 = 10;
//	}
//	var ok=0;
//})();
//
///////////////////////////////////////
//(function() {
//	var lastContext = 0;
//	test3 = 10;
//});
//
///////////////////////////////////////
//(function() { with (XPCOMUtils) {
//	var lastContext = 0;
//	test4 = 10;
//}});
//
/////////////////////////////////
//FBL.ns(function() { with (FBL) {
//	test5 = 10;
//}
//});
//
//FBL.ns(function() {
//	var lastContext = 0;
//});

///////////////////////////
//var noscriptOverlay = noscriptUtil.service ? 
//{} : {}




//function initClock() {
//  showCurrentTime();
//  window.setInterval(showCurrentTime, 1000);
//}

/*
function showCurrentTime() {
  var textbox = document.getElementById("currentTime");
  textbox.value = new Date().toLocaleTimeString();
  textbox.select();
}
*/

//var MyObject = function (){};
//MyObject.prototype = {
//    message: null,
//    hello: function (){
//        return this.message;
//    }
//};
//var h;
//
//var MyObject2 = 
//{
//    message2: null,
//    hello2: function (){
//        return this.message;
//    }
//}
//
//MyObject3 = 
//{
//    message3: null,
//    hello3: function (){
//        return this.message;
//    }
//}
//
//j=0;
//mm sk


//MyObject2.hello2();
//MyObject3.hello3();
BreakpointsNon =100;
BreakpointsNon = domplate(Firebug.Rep);

Breakpoints = domplate(Firebug.Rep,
{
	tag0:null,
	getSourceLink0: function(bp)
	{
		
	}
});

var BreakpointsTemplate = domplate(Firebug.Rep,
{
    tag:null,

    getSourceLink: function(bp)
    {
        return new SourceLink(bp.href, bp.lineNumber, "js");
    },

    onClick: function(event)
    {

    }
});
