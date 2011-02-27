
(function(){
	//if((^!.+).test(args)){
		var index = args.indexOf(' ');
		print(index);
		if(index>=0){
			var sp = args.split(' ');
			var exefile = sp[0].substring(1, sp[0].length-1);
			var arg = args.substring(index, args.length-index);
			cs_exe(exefile, arg);
		}else{
			cs_exe(args);
		}

		//arg = eval( '(' + args + ')');
		//print(arg);
		//var res = "";
		//for(var key in arg){
		//print(key);
		//	res += arg[key] + "\n"
		//}
		//print(res);
	//}
})();