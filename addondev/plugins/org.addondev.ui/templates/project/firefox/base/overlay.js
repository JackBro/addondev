
var ${name} = {
	onLoad: function() {
		${overlay_onload_js}
	},
	
	${overlay_props_js}
	
	onUnLoad: function() {
		${overlay_onunload_js}
	}	
}

window.addEventListener("load", ${name}.onLoad, false);
window.addEventListener("unload", ${name}.onUnLoad, false);