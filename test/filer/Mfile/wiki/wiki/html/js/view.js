
/// <reference path="jquery-1.5.min.js"/>
/// <reference path="jquery.json-2.2.min.js"/>

function js_BuildByID(source){
    jsview.rebuildByID(source);
}

function js_BuildInsertByID(insertBefore, value) {
    //jsview.rebuildInsertByID(insertBefore, source);
    
    var json = null;
    if (typeof value == 'string') {
        try {
            json = eval(value);
        } catch (e) {

        }
    } else {
        json = value;
    }

    var container;
    var insertid = insertBefore
    if (json instanceof Array) {
        for (var i in json) {
            container = jsview.rebuild(json[i]);
            $('#' + insertid).before(container);
            insertid = container.id;
        }
    } else {
        alert(json.Text);
        container = jsview.rebuild(json);
        $('#' + insertid).before(container);
    }
}

function js_ClearAll() {
   jsview.ClearAll();
}

function js_Remove(id){
	jsview.Remove(id);
}

var jsview = {

    getWikiParser: function () {
        if (!this.wikiParser)
            this.wikiParser = new WikiParser(document);
        return this.wikiParser;
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
        //var container = this.rebuildByID(value);
        var container = this.rebuild(value);
        $('#' + beforeid).before(container);

    },

    rebuild: function (json) {
        var source = json["Text"];
        var id = json["ID"];
        var creationtime = json["CreationTime"];
        var pageElement = this.getWikiParser().parse(source);
        var container = this.getContainerByID(id);

        while (container.childNodes.length > 0)
            container.removeChild(container.firstChild);

        var hr = document.createElement('hr');
        hr.className = "list";
        container.appendChild(hr);

        var date = document.createElement('div');
        //date.align = "right";
        date.className = "tools";
        date.appendChild(document.createTextNode(id + " " + creationtime.toLocaleString()));
        container.appendChild(date);

        var tools = document.createElement('div');
        tools.align = "right";
        tools.className = "tools";

        var eelem = document.createElement("a");
        eelem.href = "javascript:void(0)";
        eelem.appendChild(document.createTextNode("Edit"));
        tools.appendChild(eelem);
        $(eelem).click(function (event) {
            $.ajax({
                type: "GET",
                async: false,
                dataType: "text",
                url: requrl + "/" + id + "/edit",
                success: function (data) {
                    alert("data = " + data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("error textStatus = " + errorThrown);
                }
            });
            return false;
        });

        var delem = document.createElement("a");
        delem.href = "javascript:void(0)";
        //delem.href = "command://delete/" + id;
        delem.className = "operate";
        delem.appendChild(document.createTextNode("Delete"));
        tools.appendChild(delem);
        $(delem).click(function (event) {
            $.ajax({
                type: "DELETE",
                async: false,
                dataType: "text",
                url: requrl + "/" + id,
                success: function (data) {
                    alert("data = " + data);
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

    rebuildByID: function (value) {
        var json = null;
        if (typeof value == 'string') {
            try {
                //json = $.parseJSON(value);
                json = eval(value);
            } catch (e) {

            }
        } else {
            json = value;
        }

        var b = this.getBody();
        var cont;
        //alert(b);
        if (json instanceof Array) {

            for (var i in json) {
                //alert(json[i]);
                cont = this.rebuild(json[i]);
                //c = document.getElementById(cont.id);
                if (!document.getElementById(cont.id)) {
                    b.appendChild(cont);
                }
            }
        } else {
            cont = this.rebuild(json[i]);
            //c = document.getElementById(cont.id);
            if (!document.getElementById(cont.id)) {
                //alert(value);
                b.appendChild(cont);
            }
        }
    }

}