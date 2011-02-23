
function js_BuildByID(source){
	jsview.rebuildByID(source);
}

function js_BuildInsertByID(insertBefore, source){
	jsview.rebuildInsertByID(insertBefore, source);
}

function js_ClearAll() { 
   jsview.ClearAll();
}

function js_Remove(id){
	jsview.Remove(id);
}

		function jstest(){
				return jsview.getContainerByID(1);
		}

 jsview = {

	getWikiParser : function()
	{
		if(!this.wikiParser)
			this.wikiParser = new WikiParser(document);
		return this.wikiParser;
	},
	getBody : function(){
		if(!this.body)
			this.body = document.getElementsByTagName("body")[0];
		return this.body;
	},
	getContainerByID : function(id){
		var c = document.getElementById(id);
		if(!c){
			c = document.createElement('div');
			//c.className = "text";
			c.id = id;
			var b = this.getBody();
			b.appendChild(c);
		}
		return c;
	},
	Remove : function(id){
		$('#' + id).remove();
	},
	ClearAll : function(){
		var body = this.getBody();
		while(body.childNodes.length > 0) 
			body.removeChild(body.firstChild);
	},

	rebuildInsertByID:function(beforeid, value){
		var container = this.rebuildByID(value);
		$('#' + beforeid).before(container);

	},

	rebuildByID:function(value){
		var json = null;
		try {
			json = $.parseJSON(value);
		} catch(e) {
			
		}
		var source = json["text"];
		var id = json["id"];

		var pageElement = this.getWikiParser().parse(source);
		var container = this.getContainerByID(id);
		//alert("pageElement = " + pageElement + " container = " + container);
		
		while(container.childNodes.length > 0) 
			container.removeChild(container.firstChild);

		var hr = document.createElement('hr');
		hr.className = "list";
		container.appendChild(hr);

		var date = document.createElement('div');
		date.align="right";
		date.className = "tools";
		date.appendChild(document.createTextNode(json["date"]));
		container.appendChild(date);

		var tools = document.createElement('div');
		tools.align="right";
		tools.className = "tools";

		var eelem = document.createElement("a");
		eelem.href = "command://edit/" + id;
		var str = document.createTextNode("Edit");
		eelem.appendChild(str);
		tools.appendChild(eelem);

		var delem = document.createElement("a");
		delem.href = "command://delete/" + id;
		delem.className = "operate";
		var str = document.createTextNode("Delete");
		delem.appendChild(str);
		tools.appendChild(delem);
		
		container.appendChild(tools);
		container.appendChild(pageElement);	
		return container;
	}
	
}