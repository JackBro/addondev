

const reDBG = /DBG_(.*)/; ()=function
const reXUL = /\.xul$|\.xml$/;

// ************************************************************************************************
// Globals

var jsd, fbs, prefs;
var consoleService;

var contextCount = 0;

var urlFilters = [
    'chrome://',
    'XStringBundle',
    'x-jsd:ppbuffer?type=function', // internal script for pretty printing
    ];


var clients = [];
var debuggers = [];
var netDebuggers = [];
var scriptListeners = [];

var stepMode = 0;
var stepFrame;
var stepFrameLineId;
var stepStayOnDebuggr; // if set, the debuggr we want to stay within
var stepFrameCount;
var hookFrameCount = 0;

var haltDebugger = null;

var breakpoints = {};
var breakpointCount = 0;
var disabledCount = 0;
var monitorCount = 0;
var conditionCount = 0;
var runningUntil = null;

var errorBreakpoints = [];

var profileCount = 0;
var profileStart;

var enabledDebugger = false;
var reportNextError = false;
var breakOnNextError = false;
var errorInfo = null;

var timer = Timer.createInstance(nsITimer);
var waitingForTimer = false;

var FBTrace = null;

// ************************************************************************************************

//function FirebugService()の場合, thisの扱いがまだ

function FirebugService()
{

    FBTrace = Cc["@joehewitt.com/firebug-trace-service;1"]
                 .getService(Ci.nsISupports).wrappedJSObject.getTracer("extensions.firebug");

    if (FBTrace.DBG_FBS_ERRORS)
        FBTrace.sysout("FirebugService Starting");

    fbs = this;

    //this.wrappedJSObject = this; //loop
    //this.testm = this;
    
    this.timeStamp = new Date();  /* explore */
    this.breakpoints = breakpoints; // so chromebug can see it /* explore */
    this.onDebugRequests = 0;  // the number of times we called onError but did not call onDebug
    fbs._lastErrorDebuggr = null;


    if(FBTrace.DBG_FBS_ERRORS)
        this.osOut("FirebugService Starting, FBTrace should be up\n");

    this.enabled = false;
    this.profiling = false;

    prefs = PrefService.getService(nsIPrefBranch2);
    fbs.prefDomain = "extensions.firebug.service."
    prefs.addObserver(fbs.prefDomain, fbs, false);

    var observerService = Cc["@mozilla.org/observer-service;1"]
        .getService(Ci.nsIObserverService);
    observerService.addObserver(QuitApplicationGrantedObserver, "quit-application-granted", false);
    observerService.addObserver(QuitApplicationRequestedObserver, "quit-application-requested", false);
    observerService.addObserver(QuitApplicationObserver, "quit-application", false);

    this.scriptsFilter = "all";
    // XXXjj For some reason the command line will not function if we allow chromebug to see it.?
    this.alwayFilterURLsStarting = ["chrome://chromebug", "x-jsd:ppbuffer", "chrome://firebug/content/commandLine.js"];  // TODO allow override
    this.onEvalScriptCreated.kind = "eval";
    this.onTopLevelScriptCreated.kind = "top-level";
    this.onEventScriptCreated.kind = "event";

    this.onXScriptCreatedByTag = {}; // fbs functions by script tag
    this.nestedScriptStack = Components.classes["@mozilla.org/array;1"]
                        .createInstance(Components.interfaces.nsIMutableArray);  // scripts contained in leveledScript that have not been drained

}

