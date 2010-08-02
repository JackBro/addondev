
const Cc = Components.classes;
const Ci = Components.interfaces;
const BUGMONKEY_SCRIPT_DIR = "fbm_scripts";
const BUGMONKEY_SCRIPTLIST_FILE = "fbm_scripts.json";

var scriptTreeView = null;
var editorFile = null;

var scriptDir = null;
var scriptFile = null;

var util = {};

var strbundle = null;

function init() {	
	
	Components.utils.import("resource://fbm_modules/fileutils.js", util);
	
	scriptDir = util.FileUtils.makeDir(util.FileUtils.getProfileDir(), BUGMONKEY_SCRIPT_DIR);
	scriptFile = util.FileUtils.getFile(scriptDir, BUGMONKEY_SCRIPTLIST_FILE);
	
	var data = [];
	data = util.FileUtils.loadPref(scriptFile);
//	if(scriptFile.exists()){
//		let jsonstr = util.FileUtils.getContent(scriptFile);
//		let result = JSON.parse(jsonstr);
//		for(let key in result){
//			data.push(result[key]);
//		}
//	}	
	
	scriptTreeView = new ScriptTreeView(data);
    document.getElementById("scriptTree").view = scriptTreeView;

    document.getElementById("editor-path").value = Application.prefs.getValue("extensions.firebugmonkey.option.editor.path", "");
    document.getElementById("editor-args").value = Application.prefs.getValue("extensions.firebugmonkey.option.editor.args", "%path%");
    
    document.getElementById("openfolder-path").value = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.path", "");
    document.getElementById("openfolder-args").value = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.args", "%path%");
    
    document.getElementById("enablexpcom").checked = Application.prefs.getValue("extensions.firebugmonkey.option.enablexpcom", false);
}

function getStrbundleString(str){
	if(!strbundle ){
		strbundle = document.getElementById("firebugmonkey-bundle");
	}
	return strbundle.getString(str);
}

function getScriptTemplete(){
	return "";
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
	
    let findfile=false;
    let ret;
    while(true){
    	findfile = false;
	    ret = window.prompt("Enter file name.", "", "");
	    if (!ret)
	        return;
	       
		for (var index = 0; index < scriptTreeView.rowCount; index++) {
			var script = scriptTreeView._data[index];
			if(script.filename == ret){
				findfile = true;
				break;
			}
		}
		if(findfile)
			alert(getStrbundleString("EnterOtherFilename"));
		else
			break;
    }
    
    let dirname = util.FileUtils.getFileNameExceptExt(ret);
    let dir = util.FileUtils.makeDir(scriptDir, dirname);
    let script = util.FileUtils.getFile(dir, ret);
    
    util.FileUtils.write(script, getScriptTemplete());
    
    scriptTreeView.appendItem({dir:dirname, filename:ret, fullpath:script.path, enable:true});
    save();
}

function browseFile(){
	var fp = Cc["@mozilla.org/filepicker;1"].createInstance(Ci.nsIFilePicker);
	fp.init( window, "Select File", fp.modeOpen);
	var res = fp.show();
	if (res == fp.returnOK){
		var file = fp.file;
		var dirname = util.FileUtils.getFileNameExceptExt(file.leafName);
	    util.FileUtils.makeDir(scriptDir, dirname);
		
		scriptTreeView.appendItem({dir:dirname, filename:file.leafName, fullpath:file.path, enable:true});
		save();
	}
}

function editItem(){	 
//	var file;
//	if(scriptTreeView.getSelectionData.selectfilepath){
//		file = util.FileUtils.getFile(scriptTreeView.getSelectionData.selectfilepath);
//	}else{
//		file = util.FileUtils.getFile(scriptDir, scriptTreeView.getSelectionData.dir);
//		file.append(scriptTreeView.getSelectionData.filename);
//	}
	var file = util.FileUtils.getFile(scriptDir, scriptTreeView.getSelectionData.fullpath);
	var args = document.getElementById("editor-args").value;
	args = args.replace('%path%', file.path);
	var argary = args.split(' ');
	
	var editorPath= document.getElementById("editor-path").value;
	if(!launchProgram(editorPath, argary)){
		showMessageBox("Error", getStrbundleString("EditorNotFindMessage"));
	}
}

function deleteItem(){
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
	var args = document.getElementById("openfolder-args").value;
	args = args.replace('%path%', scriptDir.path);
	var argary = args.split(' ');

	var exepath = document.getElementById("openfolder-path").value;
	
	if(!launchProgram(exepath, argary)){
		showMessageBox("Error", getStrbundleString("FolderNotFindMessage"));
	}
}

function save(){	
	var data = [];	
	for (var index = 0; index < scriptTreeView.rowCount; index++) {
		var script = scriptTreeView._data[index];
		data.push(script);
	}	
	var jsonstr = JSON.stringify(data);
	
	try{
		util.FileUtils.write(scriptFile, jsonstr);
	}catch(e){
		Components.utils.reportError("firebugmonkey : save error. " + e);
	}
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
	
	save();
}

var launchProgram = function(exePath, args){
	if (exePath == null || (exePath !=null && exePath.length == 0))
		return false;
	
    try {
        var file = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
        file.initWithPath(exePath);
        if (!file.exists())
            return false;
        
        var process = Cc["@mozilla.org/process/util;1"].createInstance(Ci.nsIProcess);
        process.init(file);
        process.run(false, args, args.length, {});
       
    }catch(exc){
        Components.utils.reportError("launch error " + exc);
        return false;
    }
    return true;
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
        	case 0: return this._data[row].filename;
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
    			this._data[row].filename = value;
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
    	//Application.console.log("performActionOnCell row= " + row);
    	//Application.console.log("performActionOnCell col= " + col);
    	//Application.console.log("performActionOnCell action= " + action);
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