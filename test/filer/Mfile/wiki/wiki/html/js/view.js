
/// <reference path="jquery-1.5.min.js"/>
/// <reference path="jquery.json-2.2.min.js"/>

//function js_BuildByID(source) {
//    jsview.ClearAll();
//    jsview.rebuildByID(source);
//}

function js_BuildByID(source, comefrom) {
    jsview.ClearAll();
    if (!comefrom) {
        //alert("js_BuildByID source");
        jsview.rebuildByID(Util.toJson(source));
    } else {
        //alert("js_BuildByID source, comefrom");
        var jsons = cnvJson(source, comefrom);
        //alert(jsons["Text"]);
        jsview.rebuildByID(jsons);
    }
}

function js_BuildInsertByID(insertBefore, value) {
    //jsview.rebuildInsertByID(insertBefore, source);
//    var json = null;
//    if (typeof value == 'string') {
//        try {
//            json = eval( '(' + value + ')');
//        } catch (e) {
//        }
//    } else {
//        json = value;
//    }
    var json = Util.toJson(value);
    var container;
    var insertid = insertBefore
    if (json instanceof Array) {
        for (var i in json) {
            container = jsview.rebuild(json[i]);
            $('#' + insertid).before(container);
            insertid = container.id;
        }
    } else {
        //alert(json.Text);
        container = jsview.rebuild(json);
        $('#' + insertid).before(container);
    }
}

function js_editContent(value) {
    jsview.editContent(Util.toJson(value));
}


function js_ClearAll() {
   jsview.ClearAll();
}

function js_Remove(id){
	jsview.Remove(id);
}

function js_getComeFrom() {
    //alert("js_getComeFrom " + jsview.cf);
    return $.toJSON(jsview.cf);
}

function cnvJson(value, comefromvalue) {

    var jsons = Util.toJson(value);
    var comefromwords = Util.toJson(comefromvalue);
    var myRe = new RegExp("\\[\\[(.*?)\\]\\]", "g");
    var words = comefromwords.join('|');
    var repRex = new RegExp(words, "g");

    function myfunction(json) {
        var text = json["Text"];

        var links = new Array;
        var myArray;
        while ((myArray = myRe.exec(text)) != null) {
            var index = myRe.lastIndex - myArray[0].length;
            links.push({ "index": index, "last": myRe.lastIndex });
        }

        var reptext = text.replace(repRex,
            function () {
                var ihs = false;
                var index = arguments[arguments.length - 2];
                var len = arguments[0].length;
                for (var ln in links) {
                    if (index >= links[ln]["index"] && (index + len) <= links[ln]["last"]) {
                        ihs = true;
                        break;
                    }
                }
                if (ihs == false) {
                    return "[[<<" + arguments[0] + "]]";
                } else {
                    return arguments[0];
                }
            });
            json["Text"] = reptext;        
    }

    if (jsons instanceof Array) {
        for (var i in jsons) {
            myfunction(jsons[i]);
        }
    } else {
        myfunction(jsons);
    }

    return jsons;

//    for (var i in jsons) {
//        var text = jsons[i]["Text"];

//        var links = new Array;
//        var myArray;
//        while ((myArray = myRe.exec(str)) != null) {
//            var index = myRe.lastIndex - myArray[0].length;
//            links.push({ "index": index, "last": myRe.lastIndex });
//        }

//        str1 = str.replace(repRex,
//            function () {
//                var ihs = false;
//                var index = arguments[arguments.length - 2];
//                var len = arguments[0].length;
//                for (var ln in links) {
//                    if (index >= links[ln]["index"] && (index + len) <= links[ln]["last"]) {
//                        ihs = true;
//                        break;
//                    }
//                }
//                if (ihs == false) {
//                    return "[[<<" + arguments[0] + "]]";
//                } else {
//                    return arguments[0];
//                }
//            });
//        jsons[i]["Text"] = str1;
//    }
}

var Util = {
    toJson: function (value) {
        var json = null;
        if (typeof value == 'string') {
            try {
                json = eval('(' + value + ')');
                //alert("toJson " + value);
                //json = $.evalJSON(value);
            } catch (e) {
                alert("Util.toJson = " + e);
            }
        } else {
            json = value;
        }
        return json;
    },

		parseParams : function(value){
			var json ={};
			
			if(value.indexOf(',')>=0){
				var params = value.split(',');
				for(var i in params){
					var ps = params[i];
					if (/e\s*:\s*(.+),*/.test(ps)) {
						json["e"]=RegExp.$1;
					}else if(/a\s*:\s*(.+),*/.test(ps)) {
						json["a"]=RegExp.$1;
					}else if(/fs\s*:\s*(.+),*/.test(ps)) {
						var res = [];
						var fs = RegExp.$1;
						if(fs.indexOf(';')>=0 && /^\[(.+)\]$,*/.test(fs)){
							//fs = fs.replace(/;/g, ',');
							var reg = RegExp.$1;
							fs = reg.replace(/\s*;\s*/g, ';');
							fss = fs.split(';');
							for(var j in fss){
								res.push($.quoteString(fss[j]));
							}
							json["fs"]=res;
						}else{
							res.push($.quoteString(fs));
							json["fs"]=res;
						}
					
					}else if(/wd\s*:\s*(.+),*/.test(ps)) {
						json["wd"]=RegExp.$1;
					}else if(/wait\s*:\s*(.+),*/.test(ps)) {
						json["wait"]=RegExp.$1;
					}
				}
				json["fileonly"]=false;
			}else{
				json["e"]=value;
				json["fileonly"]=true;
			}
			return json;
		}
}

var jsview = {
    cf: null,

    getWikiParser: function () {
        if (!this.wikiParser)
            this.wikiParser = new WikiParser(document);
        return this.wikiParser;
    },
    getWikiParserP: function (parent) {
        return new WikiParser(parent);
    },
    getBody: function () {
        if (!this.body)
            this.body = document.getElementsByTagName("body")[0];
        return this.body;
    },
    getContainerByID: function (id) {
        var c = document.getElementById(id);
        if (!c) {
            c = document.createElement('div');
            //c.className = "text";
            c.id = id;
            //var b = this.getBody();
            //b.appendChild(c);
        }
        return c;
    },
    Remove: function (id) {
        $('#' + id).remove();
    },
    ClearAll: function () {
        var body = this.getBody();
        while (body.childNodes.length > 0)
            body.removeChild(body.firstChild);
    },

    rebuildInsertByID: function (beforeid, value) {
        var container = this.rebuild(value);
        $('#' + beforeid).before(container);

    },

		quote: function (json) {
        var source = json["Text"];
        var id = json["ID"];
        var creationtime = json["CreationTime"];
        var pageElement = this.getWikiParser().parse(source, id);

        var container = document.createElement('div');

        var title = document.createElement('div');
        title.className = "tools";
        title.appendChild(document.createTextNode(id + " " + creationtime.toLocaleString()));
        container.appendChild(title);
        container.appendChild(pageElement);
        return container;
		},

		editContent : function(json){
      var text = json["Text"];
      var id = json["ID"];
			var container = document.getElementById(id);//$('#' + id);
			var pageElement = this.getWikiParser().parse(text, id);
			$('#page' + id).remove();
			pageElement.id = 'page' + id;
			container.appendChild(pageElement);
this.cf = this.getWikiParser().cf;
		},

    rebuild: function (json) {
        var source = json["Text"];
        var id = json["ID"];
        var creationtime = json["CreationTime"];
        var pageElement = this.getWikiParser().parse(source, id);
				pageElement.id = 'page' + id;
        this.cf = this.getWikiParser().cf;
        //alert(this.cf);

        var container;
//        if (!pparent) {
//alert("!pparent = " + pparent);
            container = this.getContainerByID(id);
//        } else {
//alert("pparent = " + pparent);
//            container = pparent;
//        }

        while (container.childNodes.length > 0)
            container.removeChild(container.firstChild);

        //        $(container).unbind('click');
        //        $(container).click(function () {
        //            $('.tools2').removeClass('tools2');
        //            $(container).addClass('tools2');
        //            //$('#date' + id).addClass('tools2');
        //            //$('#tools' + id).addClass('tools2');
        //        });


        var hr = document.createElement('hr');
        hr.className = "list";
        container.appendChild(hr);

        var did = document.createElement('a');
        did.id = id;
        container.appendChild(did);

        var date = document.createElement('div');
        date.id = 'date' + id;
        date.className = "tools";
        date.appendChild(document.createTextNode(id + " " + creationtime.toLocaleString()));
        container.appendChild(date);

        var tools = document.createElement('div');
        tools.id = 'tools' + id;
        tools.align = "right";
        tools.className = "tools";

        var eelem = document.createElement("a");
        eelem.href = "javascript:void(0)";
        eelem.appendChild(document.createTextNode("Edit"));
        tools.appendChild(eelem);
        $(eelem).click(function (event) {
            $.ajax({
                type: "GET",
                cache:false,
                async: false,
                dataType: "text",
                url: requrl + "/" + id + "/edit",
                success: function (data) {
                    //alert("data = " + data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("error textStatus = " + errorThrown);
                }
            });
            return false;
        });

        var delem = document.createElement("a");
        delem.href = "javascript:void(0)";
        delem.className = "operate";
        delem.appendChild(document.createTextNode("Delete"));
        tools.appendChild(delem);
        $(delem).click(function (event) {
            $.ajax({
                type: "DELETE",
                async: false,
                cache:false,
                dataType: "text",
                url: requrl + "/" + id,
                success: function (data) {
                    //alert("data = " + data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("error textStatus = " + errorThrown);
                }
            });
            return false;
        })

        container.appendChild(tools);
        container.appendChild(pageElement);
        return container;
    },

    rebuildByID: function (json) {
        //        var json = null;
        //        if (typeof value == 'string') {
        //            try {
        //                json = eval('(' + value + ')');
        //            } catch (e) {
        //                alert(e);
        //            }
        //        } else {
        //            alert(value);
        //            json = value;
        //        }

        var b = this.getBody();
        var cont;
        if (json instanceof Array) {

            for (var i in json) {
                cont = this.rebuild(json[i]);

                if (!document.getElementById(cont.id)) {
                    b.appendChild(cont);
                }
            }
        } else {
            cont = this.rebuild(json);
            if (!document.getElementById(cont.id)) {
                b.appendChild(cont);
            }
        }
    }

}