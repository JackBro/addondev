
(function () {

    var sprintf = function (format, hash) {
        return format.replace(/\{([a-z]+)\}/g, function (all, group) {
            //alert("function " + group);
            return hash[group];
        });
    }
    var h = { "tera": "C:\\Program Files\\tpad\\TeraPad.exe",
        "program": "C:\\Program Files"
    };

    function getExtention(fileName) {
        var ret;
        if (!fileName) {
            return ret;
        }
        var fileTypes = fileName.split(".");
        var len = fileTypes.length;
        if (len === 0) {
            return ret;
        }
        ret = fileTypes[len - 1];
        return ret;
    }
    var e = { "txt": "D:\\program\\tpad\\TeraPad.exe"
    };
		var dirs = {
        "cprogram": "C:\\Program Files\\"
		};

//void Exe(string exe, string args, string WorkingDirectory, bool wait) {
//var value = "f:D:\\program\\tpad\\Readme.txt, a:-l, fs:[j; k; k], w:tmp";
//    print(args);

		var arg = eval( '(' + args + ')');

		var f = typeof arg['e']==="undefined"?null:arg['e'];
		var a = typeof arg['a']==="undefined"?"":arg['a'];
		var fs = typeof arg['fs']==="undefined"?null:arg['fs'];
		var wd = typeof arg['wd']==="undefined"?null:arg['wd'];
		var wait = typeof arg['wait']==="undefined"?false:arg['wait'];
		var fileonly = typeof arg['fileonly']==="undefined"?false:arg['fileonly'];

		if(f!=null){
			if(fileonly){
				if(getExtention(f) == 'mdf'){
					var mount = "C:\\Program Files\\Elaborate Bytes\\VirtualCloneDrive\\VCDMount.exe"
					cs_exeWait(mount, "/u", null, true);
					print("cs_exeWait /u");
					cs_exeWait(mount, "/d " + f, null, true);
					print("cs_exeWait /d=1");
					return;
				}
				var efile = e[getExtention(f)];
				if(efile==="undefined"){
					cs_exeWait(f, null, wd, wait);
				}else{
					cs_exeWait(efile, f, wd, wait);
				}
			}else{
				if(fs!=null){
					
					for(var j in fs){
						a += " " + fs[j];
					}
				}
				cs_exeWait(f, a, wd, wait);
			}
		}
})();
