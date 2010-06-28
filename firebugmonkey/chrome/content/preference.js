
const Cc = Components.classes;
const Ci = Components.interfaces;
const BUGMONKEY_SCRIPT_DIR = "fbm_scripts";
const BUGMONKEY_SCRIPTLIST_FILE = "fbm_scripts.xml";

var scriptTreeView = null;
var editorFile = null;
var scriptDirPath = null;

var fileutil = {};

function init() {	
	Components.utils.import("resource://fbm_modules/fileutil.js", fileutil);
	
	var profiledir = Cc["@mozilla.org/file/directory_service;1"].getService(Ci.nsIProperties).get("ProfD", Ci.nsILocalFile);
	scriptDirPath = fileutil.makeDir(profiledir.path, BUGMONKEY_SCRIPT_DIR).path;
	
	var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
	file.initWithPath(scriptDirPath);
	file.append(BUGMONKEY_SCRIPTLIST_FILE);
	var scriptsxmlfile = file.path;
	
	var data = [];
	
	if(file.exists()) 
	{
		var xmlutil ={};
		Components.utils.import("resource://fbm_modules/xmlutil.js", xmlutil);
		
		let xmldata = fileutil.read(scriptsxmlfile);
		let result = xmlutil.XML2Obj.parseFromString(xmldata);

	 	for(let i=0;i<result.length; i++)
	 	{	 		
	 		var filename = result[i]["src"];
	 		var enable   = result[i]["enable"];
	 		if(filename)
	 		{	 	
	 			data.push({filename:filename, enable:enable});
	 		}
	 	}
	}	
	
	scriptTreeView = new ScriptTreeView(data);
    document.getElementById("scriptTree").view = scriptTreeView;

    document.getElementById("editor-path").value = Application.prefs.getValue("extensions.firebugmonkey.option.editor.path", "");
    document.getElementById("editor-args").value = Application.prefs.getValue("extensions.firebugmonkey.option.editor.args", "%path%");
    
    document.getElementById("openfolder-path").value = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.path", "");
    document.getElementById("openfolder-args").value = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.args", "%path%");
    
    document.getElementById("enablexpcom").checked = Application.prefs.getValue("extensions.firebugmonkey.option.enablexpcom", false);
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

function newItem()
{
    var ret = window.prompt("Enter file name.", "", "");
    if (!ret)
        return;
        
    var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
    file.initWithPath(scriptDirPath);
    file.append(ret);
    //if(!file.exists())
    //{
    	scriptTreeView.appendItem({filename:ret, enable:true});
		fileutil.write("", file.path);   	
    //}
}

function editItem()
{	 
	var file = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
	file.initWithPath(scriptDirPath);
	file.append(scriptTreeView.getSelectionData.filename);
	
	var args = Application.prefs.getValue("extensions.firebugmonkey.option.editor.args", ""); //document.getElementById("editor-args").value;
	args = args.replace('%path%', file.path);
	var argary = args.split(' ');
	
	var editorPathField = document.getElementById("editor-path");
	launchProgram(editorPathField.value, argary);
}

function deleteItem()
{
    var rows = scriptTreeView.selectedIndexes;
    
    Application.console.log("rows = " + rows);
    
    for (var i = rows.length - 1; i >= 0; i--) {
    	scriptTreeView.removeItemAt(rows[i]);
    }	
}

function openfolder()
{
	var args = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.args", ""); //document.getElementById("openfolder-args").value;
	args = args.replace('%path%', scriptDirPath);
	var argary = args.split(' ');
	
	var exepath = Application.prefs.getValue("extensions.firebugmonkey.option.openfolder.path", "");
	launchProgram(exepath, argary);
}

function save()
{
	var data = "";	
	for (var index = 0; index < scriptTreeView.rowCount; index++) {
		var script = scriptTreeView._data[index];
		data += '<script src="' + script.filename + '" ' + 'enable="' + script.enable + '"/>\n'
	}
	
	data = "<xml>\n" + data + "</xml>";
	
	try
	{
		var file = Cc['@mozilla.org/file/local;1'].createInstance(Ci.nsILocalFile);
		file.initWithPath(scriptDirPath);
		file.append(BUGMONKEY_SCRIPTLIST_FILE);	
		fileutil.write(data, file.path);
	}
	catch(e)
	{
		Components.utils.reportError("save error. " + e);
	}
}

//////////////////////////////////////////////////////////////
//Editor
function selectFile(node) {
    var filePicker = Cc["@mozilla.org/filepicker;1"].createInstance(Ci.nsIFilePicker);
    filePicker.init(window, "Choose a file.", filePicker.modeOpen);
    if (filePicker.show() == filePicker.returnOK) {
        editorFile = filePicker.file;
        //var editorPathField = document.getElementById("editor-path");
        //editorPathField.value = editorFile.path;
        node.value = editorFile.path;
    }
}

function onDialogAccept()
{
	Application.prefs.setValue("extensions.firebugmonkey.option.editor.path", document.getElementById("editor-path").value);
	Application.prefs.setValue("extensions.firebugmonkey.option.editor.args", document.getElementById("editor-args").value);
	
	Application.prefs.setValue("extensions.firebugmonkey.option.openfolder.path", document.getElementById("openfolder-path").value);
	Application.prefs.setValue("extensions.firebugmonkey.option.openfolder.args", document.getElementById("openfolder-args").value);
	
	Application.prefs.setValue("extensions.firebugmonkey.option.enablexpcom", document.getElementById("enablexpcom").checked);
	
	save();
}

var launchProgram = function(exePath, args)
{
    try 
    {
        var file = Cc["@mozilla.org/file/local;1"].createInstance(Ci.nsILocalFile);
        file.initWithPath(exePath);
        if (!file.exists())
            return false;
        var process = Cc["@mozilla.org/process/util;1"].createInstance(Ci.nsIProcess);
        process.init(file);
        process.run(false, args, args.length, {});
        return true;
    }
    catch(exc)
    {
        Components.utils.reportError("launch error " + exc);
    }
    return false;
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
    	Application.console.log("performActionOnCell row= " + row);
    	Application.console.log("performActionOnCell col= " + col);
    	Application.console.log("performActionOnCell action= " + action);
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
	
    get getSelectionData()
    {
    	var sel = this.selectedIndexes;
    	if(sel.length >0)
    		return this._data[sel[0]];
    		
    	return null;
    }
};
