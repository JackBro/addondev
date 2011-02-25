
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

 var jsview = {

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
	
	rebuild:function(json){
		var source = json["Text"];
		var id = json["ID"];
		var creationtime = json["CreationTime"];
		alert(source +"--"+ id + "--"+creationtime);
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
		date.appendChild(document.createTextNode(creationtime));
		container.appendChild(date);

		var tools = document.createElement('div');
		tools.align="right";
		tools.className = "tools";

		var eelem = document.createElement("a");
		//eelem.href = "command://edit/" + id;
		eelem.href = "javascript:void(0)";
		var str = document.createTextNode("Edit");
		eelem.appendChild(str);
		tools.appendChild(eelem);
		$(eelem).click(function(event){
			$.ajax({
				type: "GET",
				async: false,
				dataType:"text",
				url: requrl+ "/" + id + "/edit",
				success: function(data){
					alert("data = " + data);
					
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					alert("error textStatus = " + errorThrown);
			   	}
			});
			return false;
		});

		var delem = document.createElement("a");
		delem.href = "javascript:void(0)";
		//delem.href = "command://delete/" + id;
		delem.className = "operate";
		var str = document.createTextNode("Delete");
		delem.appendChild(str);
		tools.appendChild(delem);
		$(delem).click(function(event){
			$.ajax({
				type: "DELETE",
				//type: "POST",
				async: false,
				dataType:"text",
				//data:$.toJSON({ "hoge":"hogehoge", "foo":"file:///d/date/j.png" }),
				url: requrl+ "/" + id,
				//url: requrl+ "/exe",
				success: function(data){
					alert("data = " + data);
					
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					alert("error textStatus = " + errorThrown);
			   	}
			});
			return false;
		})
		
		container.appendChild(tools);
		container.appendChild(pageElement);	
		//return container;		
	},
	
	rebuildByID:function(value){
		var json = null;
		if(typeof value=='string'){
			try {
				//json = $.parseJSON(value);
				json = eval(value);
			} catch(e) {
				
			}		
		}else{
			json = value;
		}
		
		if(json instanceof Array){
			
			for(var i in json){
				//alert(json[i]);
				this.rebuild(json[i]);
			}
		}else{
			alert(value);
			this.rebuild(json);
		}
		
	//	
	//	var source = json["text"];
	//	var id = json["id"];
	//
	//	var pageElement = this.getWikiParser().parse(source);
	//	var container = this.getContainerByID(id);
	//	//alert("pageElement = " + pageElement + " container = " + container);
	//	
	//	while(container.childNodes.length > 0) 
	//		container.removeChild(container.firstChild);
	//
	//	var hr = document.createElement('hr');
	//	hr.className = "list";
	//	container.appendChild(hr);
	//
	//	var date = document.createElement('div');
	//	date.align="right";
	//	date.className = "tools";
	//	date.appendChild(document.createTextNode(json["date"]));
	//	container.appendChild(date);
	//
	//	var tools = document.createElement('div');
	//	tools.align="right";
	//	tools.className = "tools";
	//
	//	var eelem = document.createElement("a");
	//	//eelem.href = "command://edit/" + id;
	//	eelem.href = "javascript:void(0)";
	//	var str = document.createTextNode("Edit");
	//	eelem.appendChild(str);
	//	tools.appendChild(eelem);
	//	$(eelem).click(function(event){
	//		$.ajax({
	//			type: "GET",
	//			async: false,
	//			dataType:"text",
	//			url: requrl+ "/" + id + "/edit",
	//			success: function(data){
	//				alert("data = " + data);
	//				
	//			},
	//			error:function(XMLHttpRequest, textStatus, errorThrown){
	//				alert("error textStatus = " + errorThrown);
	//		   	}
	//		});
	//		return false;
	//	});
	//
	//	var delem = document.createElement("a");
	//	delem.href = "javascript:void(0)";
	//	//delem.href = "command://delete/" + id;
	//	delem.className = "operate";
	//	var str = document.createTextNode("Delete");
	//	delem.appendChild(str);
	//	tools.appendChild(delem);
	//	$(delem).click(function(event){
	//		$.ajax({
	//			//type: "DELETE",
	//			type: "POST",
	//			async: false,
	//			dataType:"text",
	//			data:$.toJSON({ "hoge":"hogehoge", "foo":"file:///d/date/j.png" }),
	//			//url: requrl+ "/" + id,
	//			url: requrl+ "/exe",
	//			success: function(data){
	//				alert("data = " + data);
	//				
	//			},
	//			error:function(XMLHttpRequest, textStatus, errorThrown){
	//				alert("error textStatus = " + errorThrown);
	//		   	}
	//		});
	//		return false;
	//	})
	//
	//	
	//	container.appendChild(tools);
	//	container.appendChild(pageElement);	
	//	return container;
	}
	
}