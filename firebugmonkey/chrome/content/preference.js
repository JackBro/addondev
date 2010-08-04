
const Cc = Components.classes;
const Ci = Components.interfaces;
const UConv = Cc["@mozilla.org/intl/scriptableunicodeconverter"].createInstance(Ci.nsIScriptableUnicodeConverter);


var scriptTreeView = null;
var editorFile = null;

var scriptDir = null;

var util = {};

var strbundle = null;

function init() {	
	Components.utils.import("resource://fbm_modules/utils.js", util);
	
	scriptDir = util.Utils.FbmScriptDir;
	var data = util.Utils.loadSetting();	
	
	scriptTreeView = new ScriptTreeView(data.files);
    document.getElementById("scriptTree").view = scriptTreeView;

    document.getElementById("editor-path").value = Application.prefs.getValue("extensions.firebugmonkey.option.editor.path", "");
    document.getElementById("editor-args").value = Application.prefs.getValue("extensions.firebugmonkey.option.editor.args", "%path%");
    
    document.getElementById("openfolder-path").value = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.path", "");
    document.getElementById("openfolder-args").value = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.args", "%path%");
    
    document.getElementById("enablexpcom").checked = Application.prefs.getValue("extensions.firebugmonkey.option.enablexpcom", false);
    
    document.getElementById("remove-confirm").checked = Application.prefs.getValue("extensions.firebugmonkey.option.remove.confirm", true);
    
    document.getElementById("scripttemplate").value = util.Utils.getScriptTemplate();
    
    document.getElementById("use-encode").checked = Application.prefs.getValue("extensions.firebugmonkey.option.encode.use", false);
    document.getElementById("form-encode").value = Application.prefs.getValue("extensions.firebugmonkey.option.encode.from", null);

    update();
}

function update(){
	 var isencode = document.getElementById("use-encode").checked;
	 document.getElementById("form-encode").disabled = !isencode;
}

function getStrbundleString(str){
	if(!strbundle ){
		strbundle = document.getElementById("firebugmonkey-bundle");
	}
	return strbundle.getString(str);
}

function getScriptTemplate(file){
	var name = util.FileUtils.getFileNameExceptExt(file.leafName);
	var tmplate = util.Utils.getScriptTemplate();

	return tmplate.replace(/\{name\}/g, name);
}

//////////////////////////////////////////////////////////////
// Script
function moveItem(aUpDown) {
    if (scriptTreeView.selection.count != 1)
        return false;
    var sourceIndex = scriptTreeView.selection.currentIndex;
    var targetIndex = sourceIndex + aUpDown;
    scriptTreeView.moveItem(sourceIndex, targetIndex);
}

function newItem(){
    try{
	    var filePicker = Cc["@mozilla.org/filepicker;1"].createInstance(Ci.nsIFilePicker);
	    filePicker.appendFilter('JavaScript', '*.js');
	    filePicker.appendFilter('All', '*');
	    filePicker.init(window, "save file.", filePicker.modeSave);
	    if (filePicker.show() == filePicker.returnOK) {
	        let newfile = filePicker.file;
	        if(hasItem(newfile.path)){
	        	showMessageBox("Info", getStrbundleString("HasFileMessage"));
	        	return;
	        }
	        
		    let dirname = util.FileUtils.getFileNameExceptExt(newfile.leafName);
		    let dir = util.FileUtils.makeDir(scriptDir, dirname, true);    
		    util.FileUtils.write(newfile, getScriptTemplate(newfile));
		    
		    scriptTreeView.appendItem({dir:dir.leafName, filename:newfile.leafName, fullpath:newfile.path, enable:true});
		    save();
    	}
    }catch(e){
    	showMessageBox("Error", getStrbundleString("FaultSaveFileMessage"));
    	Components.utils.reportError("firebugmonkey : error newfile save : " + e);
    }
}

function browseFile(){
	var filePicker = Cc["@mozilla.org/filepicker;1"].createInstance(Ci.nsIFilePicker);
	filePicker.appendFilter('JavaScript', '*.js');
	filePicker.appendFilter('All', '*');
	filePicker.init( window, "Select File", filePicker.modeOpen);
	var res = filePicker.show();
	if (res == filePicker.returnOK){
		var file = filePicker.file;
		
		if(hasItem(file.path)){
	    	showMessageBox("Info", getStrbundleString("HasFileMessage"));
	    	return;
	    }
		
		var dirname = util.FileUtils.getFileNameExceptExt(file.leafName);
	    var dir = util.FileUtils.makeDir(scriptDir, dirname, true);
		
		scriptTreeView.appendItem({dir:dir.leafName, filename:file.leafName, fullpath:file.path, enable:true});
		save();
	}
}

function editItem(){	 
	if(scriptTreeView.getSelectionData == null) return;
	
	var fullpath = scriptTreeView.getSelectionData.fullpath;
	var args = document.getElementById("editor-args").value;
	args = args.replace('%path%', cnvEncode(fullpath));
	var argary = args.split(' ');
	
	var editorPath= document.getElementById("editor-path").value;
	
	var result = launchProgram(editorPath, argary);
	if(!result.res){
		showMessageBox("Error", result.error);
	}
}

function removeItem(){
	if(document.getElementById("remove-confirm").checked){
		var prompts = Cc["@mozilla.org/embedcomp/prompt-service;1"].getService(Ci.nsIPromptService);		
		//ok true
		//cancal false
		result = prompts.confirm(window, getStrbundleString("DeleteConfirmDialogTitle"), getStrbundleString("DeleteConfirmDialogMessage"));    
	    if(!result) return;
	}
	
    var rows = scriptTreeView.selectedIndexes;
    for (var i = rows.length - 1; i >= 0; i--) {
    	try{
	    	var script = scriptTreeView._data[rows[i]];
	    	var file = util.FileUtils.getFile(scriptDir, script.dir);
			if(file.exists()){
				util.FileUtils.deleteFile(file, true);
			}    	
    	}catch(e){
    		Components.utils.reportError("firebugmonkey : delete error. " + e);
    		continue;
    	}
    	scriptTreeView.removeItemAt(rows[i]);
    }	
    save();
}

function openfolder(){
	
	//var formencode = document.getElementById("form-encode").value;
	//Application.console.log("formencode = " + formencode);
	
	var args = document.getElementById("openfolder-args").value;
	if(arguments.length == 1 && arguments[0] == 'fbmscripts'){
		args = args.replace('%path%', cnvEncode(scriptDir.path));
	}else{
		if(scriptTreeView.getSelectionData == null) return;
		var file = util.FileUtils.getFile(scriptTreeView.getSelectionData.fullpath);
		
		//args = args.replace('%path%', file.parent.path);
		args = args.replace('%path%', cnvEncode(file.parent.path));
	}
	
	var argary = args.split(' ');

	var exepath = document.getElementById("openfolder-path").value;
	
	var result = launchProgram(exepath, argary);
	if(!result.res){
		showMessageBox("Error", result.error);
	}
}

function save(){	
	var scripts = [];
	for (var index = 0; index < scriptTreeView.rowCount; index++) {
		var script = scriptTreeView._data[index];
		scripts.push(script);
	}	
	util.Utils.saveSetting(scripts);
}

function cnvEncode(value){
	var check = document.getElementById("use-encode").checked;
	var encode = document.getElementById("form-encode").value;
	if(check && encode!=null){
		UConv.charset = document.getElementById("form-encode").value;
		return UConv.ConvertFromUnicode(value);	
	}
	return value;
}

//////////////////////////////////////////////////////////////
//Editor
function selectFile(node) {
    var filePicker = Cc["@mozilla.org/filepicker;1"].createInstance(Ci.nsIFilePicker);
    filePicker.init(window, "Choose a file.", filePicker.modeOpen);
    if (filePicker.show() == filePicker.returnOK) {
        editorFile = filePicker.file;
        node.value = editorFile.path;
    }
}

function onDialogAccept(){
	Application.prefs.setValue("extensions.firebugmonkey.option.editor.path", document.getElementById("editor-path").value);
	Application.prefs.setValue("extensions.firebugmonkey.option.editor.args", document.getElementById("editor-args").value);
	
	Application.prefs.setValue("extensions.firebugmonkey.option.openfolder.path", document.getElementById("openfolder-path").value);
	Application.prefs.setValue("extensions.firebugmonkey.option.openfolder.args", document.getElementById("openfolder-args").value);
	
	Application.prefs.setValue("extensions.firebugmonkey.option.enablexpcom", document.getElementById("enablexpcom").checked);
	
	Application.prefs.setValue("extensions.firebugmonkey.option.remove.confirm", document.getElementById("remove-confirm").checked);
	
    Application.prefs.setValue("extensions.firebugmonkey.option.encode.use", document.getElementById("use-encode").checked);
    Application.prefs.setValue("extensions.firebugmonkey.option.encode.from", document.getElementById("form-encode").value);	
	
	save();
}

var launchProgram = function(exePath, args){
	if (exePath == null || (exePath !=null && exePath.length == 0))
		return {res:false, error:getStrbundleString("LaunchExeNotFindMessage")};
	
    try {
        var file = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
        file.initWithPath(exePath);
        if (!file.exists())
            return {res:false, error:file.path + " : " + getStrbundleString("LaunchFileNotFindMessage")};

        var process = Cc["@mozilla.org/process/util;1"].createInstance(Ci.nsIProcess);
        process.init(file);
        process.run(false, args, args.length, {});
       
    }catch(e){
        //Components.utils.reportError("launch error " + exc);
    	return {res:false, error:e};
    }
    return {res:true, error:null};
};


////////////////////////////////////////////////////////////////
// Custom Tree View

function ScriptTreeView(aData) {
    this._data = aData;
}

ScriptTreeView.prototype = {

    /**
     * nsITreeBoxObject
     */
    _treeBoxObject: null,

    ////////////////////////////////////////////////////////////////
    // implements nsITreeView

    get rowCount() {
        return this._data.length;
    },
    selection: null,
    getRowProperties: function(index, properties) {},
    getCellProperties: function(row, col, properties) {},
    getColumnProperties: function(col, properties) {},
    isContainer: function(index) { return false; },
    isContainerOpen: function(index) { return false; },
    isContainerEmpty: function(index) { return false; },
    isSeparator: function(index) {},
    isSorted: function() { return false; },
    canDrop: function(targetIndex, orientation) { return false; },
    drop: function(targetIndex, orientation) {},
    getParentIndex: function(rowIndex) { return -1; },
    hasNextSibling: function(rowIndex, afterIndex) { return false; },
    getLevel: function(index) { return 0; },
    getImageSrc: function(row, col) {},
    getProgressMode: function(row, col) {},
    getCellValue: function(row, col) {
    	return this._data[row].enable;
    },
    getCellText: function(row, col) {
        switch (col.index) {
        	//case 0: return this._data[row].filename;
        	case 0: return this._data[row].fullpath;
        }
    },
    setTree: function(tree) {
        this._treeBoxObject = tree;
    },
    toggleOpenState: function(index) {},
    cycleHeader: function(col) {},
    selectionChanged: function() {},
    cycleCell: function(row, col) {},
    isEditable: function(row, col) { 
    	return (col.index == 1);
    },
    isSelectable: function(row, col) {},
    setCellValue: function(row, col, value) {
    	switch (col.index) {
    		case 0:
    			//this._data[row].filename = value;
    			this._data[row].fullpath = value;
    			break;
    		case 1: 
    			this._data[row].enable = value;
    			break;
    	}
        this._treeBoxObject.invalidateCell(row, col);
    },
    setCellText: function(row, col, value) {},
    performAction: function(action) {},
    performActionOnRow: function(action, row) {},
    performActionOnCell: function(action, row, col) {
    },
    
    /**
     * @param String aName The name of the new item.
     */
    appendItem: function(aName) {
        this._data.push(aName);
        var newIdx = this.rowCount - 1;
        this._treeBoxObject.rowCountChanged(newIdx, 1);
        // select the new item now
        this.selection.select(newIdx);
        this._treeBoxObject.ensureRowIsVisible(newIdx);
        this._treeBoxObject.treeBody.focus();
    },    
    
    /**
     * @param Number aSourceIndex The array index wherefrom move.
     * @param Number aTargetIndex The array index whereto move.
     */
    moveItem: function(aSourceIndex, aTargetIndex) {
        if (aTargetIndex < 0 || aTargetIndex > this.rowCount - 1)
            return;
        var removedItems = this._data.splice(aSourceIndex, 1);
        this._data.splice(aTargetIndex, 0, removedItems[0]);
        this._treeBoxObject.invalidate();
        // select and focus the item now
        this.selection.clearSelection();
        this.selection.select(aTargetIndex);
        this._treeBoxObject.ensureRowIsVisible(aTargetIndex);
        this._treeBoxObject.treeBody.parentNode.focus();
    },
    
    removeItemAt: function(aRow) {
        this._data.splice(aRow, 1);
        this._treeBoxObject.rowCountChanged(aRow, -1);
        this.selection.clearSelection();
    },   
    
    /**
     * readonly property to get selected row indexes as array.
     */
    get selectedIndexes() {
        var ret = [];
        var sel = this.selection;    // nsITreeSelection
        for (var rc = 0; rc < sel.getRangeCount(); rc++) {
            var start = {}, end = {};
            sel.getRangeAt(rc, start, end);
            for (var idx = start.value; idx <= end.value; idx++) {
                ret.push(idx);
            }
        }
        return ret;
    },
	
    get getSelectionData(){
    	var sel = this.selectedIndexes;
    	if(sel.length >0)
    		return this._data[sel[0]];
    		
    	return null;
    }
};


////////////////////////////////////////////////////////////////
//messagebox
function showMessageBox(title, message) {
	var prompts = Cc["@mozilla.org/embedcomp/prompt-service;1"].getService(Ci.nsIPromptService);
    prompts.alert(window, title, message);
};

/***
 * 
 * @param {string} fullpath
 * 
 * @return {bool} if has fullpath, return true
 */
function hasItem(fullpath){
	for (var index = 0; index < scriptTreeView.rowCount; index++) {
		var script = scriptTreeView._data[index];
		if(script.fullpath == fullpath){
			return true;
		}
	}
	
	return false;
};


