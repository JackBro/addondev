
var ${name} = {
	onLoad: function() {
		${context_overlay1.js}
	},
	
	${context_overlay2.js}
	${menu_overlay.js}
	${toolbarbutton_overlay.js}
	
	onUnLoad: function() {
		
	}	
}

window.addEventListener("load", ${name}.onLoad, false);
window.addEventListener("unload", ${name}.onUnLoad, false);