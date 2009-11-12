const nsISupports           = Components.interfaces.nsISupports;
const nsICategoryManager    = Components.interfaces.nsICategoryManager;
const nsIComponentRegistrar = Components.interfaces.nsIComponentRegistrar;
const nsICommandLineHandler = Components.interfaces.nsICommandLineHandler;
const nsIFactory            = Components.interfaces.nsIFactory;
const nsIModule             = Components.interfaces.nsIModule;

// CHANGEME: change the contract id, CID, and category to be unique
// to your application.
const clh_contractID = "@mozilla.org/commandlinehandler/general-startup;1?type=chrome_eclipse";

// use uuidgen to generate a unique ID
const clh_CID = Components.ID("{2991c315-b871-42cd-b33f-bfee4fcbf682}");

// category names are sorted alphabetically. Typical command-line handlers use a
// category that begins with the letter "m".
const clh_category = "chrome_eclipse";

/**
 * The XPCOM component that implements nsICommandLineHandler.
 * It also implements nsIFactory to serve as its own singleton factory.
 */
const chrome_eclipseHandler = {
  /* nsISupports */
  QueryInterface : function clh_QI(iid)
  {
    if (iid.equals(nsICommandLineHandler) ||
        iid.equals(nsIFactory) ||
        iid.equals(nsISupports))
      return this;

    throw Components.results.NS_ERROR_NO_INTERFACE;
  },

  /* nsICommandLineHandler */
  handle : function clh_handle(cmdLine)
  {
	  //-p "35chromebug" -no-remote -ce_eport 8084 -ce_cport 8083 -chrome chrome://chromebug/content/chromebug.xul
	  //-chromebug -p "35chromebug" -no-remote
    try {
    	var ce_eport = cmdLine.handleFlagWithParam("ce_eport", false);
    	var ce_cport = cmdLine.handleFlagWithParam("ce_cport", false);
    	
    	var Application = Components.classes["@mozilla.org/fuel/application;1"].getService(Components.interfaces.fuelIApplication);
    	Application.storage.set('ce_eport', ce_eport);	
    	Application.storage.set('ce_cport', ce_cport);	
   
    	//cmdLine.preventDefault = true;
    }
    catch (e) {
    	Components.utils.reportError("incorrect parameter passed to -viewapp on the command line. " + e);
    }
  },

  helpInfo : "  -dport <port>       Open My Application\n" +
             "  -eport <port>       View and edit the URI in My Application,\n" +
             "                      wrapping this description\n",

  /* nsIFactory */
  createInstance : function clh_CI(outer, iid)
  {
    if (outer != null)
      throw Components.results.NS_ERROR_NO_AGGREGATION;

    return this.QueryInterface(iid);
  },

  lockFactory : function clh_lock(lock)
  {
    /* no-op */
  }
};

/**
 * The XPCOM glue that implements nsIModule
 */
const chromeeclipseHandlerModule = {
  /* nsISupports */
  QueryInterface : function mod_QI(iid)
  {
    if (iid.equals(nsIModule) ||
        iid.equals(nsISupports))
      return this;

    throw Components.results.NS_ERROR_NO_INTERFACE;
  },

  /* nsIModule */
  getClassObject : function mod_gch(compMgr, cid, iid)
  {
    if (cid.equals(clh_CID))
      return chrome_eclipseHandler.QueryInterface(iid);

    throw Components.results.NS_ERROR_NOT_REGISTERED;
  },

  registerSelf : function mod_regself(compMgr, fileSpec, location, type)
  {
    compMgr.QueryInterface(nsIComponentRegistrar);

    compMgr.registerFactoryLocation(clh_CID,
                                    "chrome_eclipseHandler",
                                    clh_contractID,
                                    fileSpec,
                                    location,
                                    type);

    var catMan = Components.classes["@mozilla.org/categorymanager;1"].
      getService(nsICategoryManager);
    catMan.addCategoryEntry("command-line-handler",
                            clh_category,
                            clh_contractID, true, true);
  },

  unregisterSelf : function mod_unreg(compMgr, location, type)
  {
    compMgr.QueryInterface(nsIComponentRegistrar);
    compMgr.unregisterFactoryLocation(clh_CID, location);

    var catMan = Components.classes["@mozilla.org/categorymanager;1"].
      getService(nsICategoryManager);
    catMan.deleteCategoryEntry("command-line-handler", clh_category);
  },

  canUnload : function (compMgr)
  {
    return true;
  }
};

/* The NSGetModule function is the magic entry point that XPCOM uses to find what XPCOM objects
 * this component provides
 */
function NSGetModule(comMgr, fileSpec)
{
  return chromeeclipseHandlerModule;
}
