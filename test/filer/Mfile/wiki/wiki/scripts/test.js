
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
    var e = { "txt": "D:\\program\\tpad\\TeraPad.exe",
        "program": "C:\\Program Files"
    };

    //if((^!.+).test(args)){
    //print("args = " + args);
    var index = args.indexOf(' ');
    print(index);
    if (index >= 0) {
        var sp = args.split(' ');
        var exefile = sp[0].substring(0, sp[0].length);
        exefile = sprintf(exefile, h);
        var arg = args.substring(index, args.length);
        arg = sprintf(arg, h);
        print(exefile + " -" + arg);
        cs_exe(exefile, arg);
    } else {
        var efile = e[getExtention(args)];
        if (!efile){
            cs_exe(args);
        }else{
            cs_exe(efile, args);
        }
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