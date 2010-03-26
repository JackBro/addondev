

var MyObject = function (){};
MyObject.prototype = {
    message: null,
    hello: function (){
		this.h=100;
        return this.message;
    }
};

var c = new MyObject();
