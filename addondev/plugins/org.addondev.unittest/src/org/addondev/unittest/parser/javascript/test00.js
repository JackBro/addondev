
var stacklink_106ec9de_7db3_40c6_93c2_39563e25a8d6 = {};
(function(){
	var $ = stacklink_106ec9de_7db3_40c6_93c2_39563e25a8d6;
	
var MyObject = 
{
	hello3: function (arg0, arg1){
	
		var lh = $.util;

		//aa = 100;
		openDialog("chrome://stacklink/content/preference.xul", "Preferences", fu);
		
	},
	
	_unload: function()
	{
		if (this._panel.state == 'open')
		{ 
			if(!this.isItemAppended)
			{
				this._mainPanel.init(this.tmpitems, this.itemClick);
				this.tmprestoredata = null;
				this.tmpitems = null;
				this.isItemAppended = true;
			}
//			else
//			{
//				//this._addItems(this.tmpitems);
//			}
			return 0;
		}

		return setTimeout($_ + '.stackpanel._addItemsWaitShowPopup();', 200);		
	}
	,
	_tt: function()
	{
		if(e.target == 'close')
		{
			
			$.stackpanel._mainPanel.itemPanel.removeItem(e.element);
		}
		else if(e.target == 'item')
		{	
			var key = $.util.getKey(e.event);		
			
//			if($.stackpanel.mousefuncmap[$.stackpanel.map[key]])
//			{
//				$.stackpanel.mousefuncmap[$.stackpanel.map[key]](e);
//			}
			
		}	
	}

}


$.util =
{ 
	loadCurrentTab:function(e)
	{
	}
};



})();
