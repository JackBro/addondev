function Object(){};
/**
 * function toString() 
 * @type    String
 * @memberOf   Object
 * @returns {String}
*/  
Object.prototype.toString = function(){};

function String(){};
String.prototype = new Object();
/**
 * Property length
 * @type    Number
 * @memberOf   String
*/ 
String.prototype.length;

///**
// * function charAt
// * @type    String
// * @memberOf   String
// * @returns {String}
//*/ 
//String.prototype.charAt= function(index){};
//
///**
// * function concat
// * @type    String
// * @memberOf   String
// * @returns {String}
//*/ 
//String.prototype.concat= function(value){};
//
//
function Number(){};
Number.prototype = new Object();

/*
 * @type Number;
 */
Number.MAX_VALUE;

//function Number(){};
//
//
function Array(){};
Array.prototype = new Object();

/**
 * @type    Number
 * @memberOf   Array
*/
Array.prototype.length;

/**
 * @type    Object
 * @memberOf   Array
*/
Array.prototype.pop = function(){};
//Array.prototype.push = function(args){};
//
//
////FULE
//function Application(){};
//Application.console = {};
//Application.console.log = function(args){};
//Application.prefs.getValue = function(args0, args1){};
//Application.storage.set = function(args0, args1){};
//Application.storage.get = function(args){};
//
//function document(){};
//document.createElement = function(args){};

var Math = function(){};

/**
 * @type Number
 */
Math.abs = function(x){};

/**
 * @type Number
 */
Math.sin = function(x){};

/**
 * @type Number
 */
Math.cos = function(x){};

/**
 * @type Number
 */
Math.tan = function(x){};
