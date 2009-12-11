package org.addondev.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.addondev.plugin.AddonDevPlugin;
import org.addondev.preferences.PrefConst;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import com.sun.org.apache.xpath.internal.XPathAPI;

public class AddonDevUtil {

	
	private ILaunchConfiguration fLaunchConfiguration;
	private IPreferenceStore fStore;
	
	private static IProject[] debugprojects;
	//private String[] commandline;
	
	private Map<String, List<List<String>>> mapProject2Chrome = new HashMap<String, List<List<String>>>(); // projectname:[chrome, path],[chrome, path]}
	private Map<String, List<String>> mapChrome2Projec = new HashMap<String, List<String>>(); // chrome:[projectname, path]
	
	private Map<String, List<String>> chromeMap = new HashMap<String, List<String>>(); // projectname:[chrome, path],[chrome, path]}
	
	private static Pattern contentlinepattern = Pattern.compile("^(content)[\\s\\t]+.*");
	private static Pattern contentslipitpattern = Pattern.compile("[\\s\\t]+");
	private static Pattern p = Pattern.compile("^(chrome://)(.+)(/content/)(.*)$"); //chrome://hello/content/hello.js
		
	public AddonDevUtil(ILaunchConfiguration configuration)
	{
		fLaunchConfiguration = configuration;
		fStore = AddonDevPlugin.getDefault().getPreferenceStore();	
	}
	
	public void init() throws CoreException, IOException, ParserConfigurationException, SAXException, TransformerException
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();	
		//try
		//{
			debugprojects = getDebugProjects(fLaunchConfiguration, root.getProjects());
			setContentMap(debugprojects);
			
			String path =  fLaunchConfiguration.getAttribute(PrefConst.FIREFOX_PROFILE_PATH, "");
			checkAddonFile(path, debugprojects);
			
			contentMap(debugprojects);
		//}
		//catch (Exception e) {
		//	// TODO: handle exception
		//	e.printStackTrace();
		//	return false;
		//}
		//return true;
			
		//	commandline = getCommandLine(configuration);
	}
	
//	public String[] getProcessCommandLine()
//	{
//		return commandline;
//	}
	
	private IProject[] getDebugProjects(ILaunchConfiguration configuration, IProject[] projects) throws CoreException
	{
		List<IProject> result= new ArrayList<IProject>();
		List<String> checkedprojects = configuration.getAttribute(PrefConst.FIREFOX_DEBUGTARGETADDONS, new ArrayList<String>());
		for (int i = 0; i < projects.length; i++) 
		{
			if(checkedprojects.contains(projects[i].getName()))
			{
				result.add(projects[i]);
			}
		}
		
		return result.toArray(new IProject[result.size()]);
	}
	
	private void setContentMap(IProject[] debugprojects) throws CoreException, IOException
	{
		mapProject2Chrome.clear();
		mapChrome2Projec.clear();		
		
		for (int i = 0; i < debugprojects.length; i++) 
		{
			String projectname = debugprojects[i].getName();		
			IFile file = debugprojects[i].getFile("chrome.manifest");
			List<List<String>> list = new ArrayList<List<String>>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents(), "UTF-8"));
			while (reader.ready()) 
			{
				String line = reader.readLine();
				if(contentlinepattern.matcher(line).matches())
				{
					String[] strs = contentslipitpattern.split(line);
					if(strs.length >= 3)
					{	
						List<String> list2 = new ArrayList<String>();
						list2.add(strs[1]); //chrome
						list2.add(strs[2]); //path
						list.add(list2);
						
						if(!mapChrome2Projec.containsKey(strs[1]))
						{
							List<String> plist = new ArrayList<String>();
							plist.add(projectname);
							plist.add(strs[2]);
							mapChrome2Projec.put(strs[1], plist);
						}
					}
				}
			}
			mapProject2Chrome.put(projectname, list);
		}
	}
	
	private void contentMap(IProject[] debugprojects) throws CoreException, IOException
	{
		chromeMap.clear();	
		
		for (int i = 0; i < debugprojects.length; i++) 
		{
			String projectname = debugprojects[i].getName();		
			IFile file = debugprojects[i].getFile("chrome.manifest");
			List<String> list = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents(), "UTF-8"));
			while (reader.ready()) 
			{
				String line = reader.readLine();
				if(contentlinepattern.matcher(line).matches())
				{
					String[] strs = contentslipitpattern.split(line);
					if(strs.length >= 3)
					{	
						list.add(strs[1]);
						list.add(strs[2]);
						chromeMap.put(projectname, list);
						break;
					}
				}
			}
		}
	}
	
	public String convertChrome(IProject project, String filePath)
	{
		if(chromeMap.containsKey(project.getName()))
		{
			String root = project.getFile("chrome.manifest").getFullPath().removeLastSegments(1).toPortableString();
			
			filePath = filePath.replaceFirst(root, "");
			
			List<String> list = chromeMap.get(project.getName());
			String addonname = list.get(0);
			String content = list.get(1);
			
			String chromepath = "chrome:/" + filePath.replace(content, addonname + "/content/");
			
			return chromepath;
		}
		return null;
	}
	
	private void checkAddonFile(String profiledir, IProject[] debugprojects) throws IOException, ParserConfigurationException, SAXException, TransformerException, CoreException
	{	
		IPath profilepath = new Path(profiledir).append("extensions");
		for (int i = 0; i < debugprojects.length; i++) {
			IProject project = debugprojects[i];
			String id =getIDFrominstallrdf(project);
			String path = profilepath.append(id).toOSString();
			File file = new File(path);	
			if(file.exists())
			{
				
			}
			else
			{		
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path));
				BufferedWriter bw = new BufferedWriter(osw);
				bw.write(project.getLocation().toOSString());
				bw.close();
				osw.close();
			}
		}
	}
	
	private String getIDFrominstallrdf(IProject project) throws ParserConfigurationException, SAXException, IOException, TransformerException, CoreException 
	{
		InputStream in = project.getFile("install.rdf").getContents();
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fact.newDocumentBuilder();
		Document doc = builder.parse(in);

        Node n;		
        NodeIterator nl;
        
        String id = null;
        nl = XPathAPI.selectNodeIterator(doc, "/RDF/Description/*");
        while ((n = nl.nextNode()) != null) {
            if(n.getNodeName().equals("em:id"))
            {
            	id = n.getTextContent();
            	break;
            }
        }              
        return id;
	}
	
	
//	private String[] getCommandLine(ILaunchConfiguration configuration) throws CoreException
//	{
//		
//		int eclispport = AddonDevPlugin.getDefault().getPreferenceStore().getInt(PrefConst.ECLIPSE_PORT);
//		int debuggerport = AddonDevPlugin.getDefault().getPreferenceStore().getInt(PrefConst.DEBUGGER_PORT);	
//		
//		String firefoxpath = configuration.getAttribute(PrefConst.FIREFOX_PATH, "");
//		String profilename = configuration.getAttribute(PrefConst.FIREFOX_PROFILE_NANE, "");
//		String firefoxargs = configuration.getAttribute(PrefConst.FIREFOX_ARGS, "");
//		
//		List<String> commandList = new ArrayList<String>();		
//		commandList.add(firefoxpath);
//	
//		//-p "chromebugtest" -no-remote -ce_eport 8084 -ce_cport 8083 -chrome chrome://chromebug/content/chromebug.xul		
//		commandList.add("-p");
//		commandList.add(profilename);
//		
//		commandList.add("-no-remote");
//		
//		commandList.add("-ce_eport");
//		commandList.add(String.valueOf(eclispport));
//		commandList.add("-ce_cport");
//		commandList.add(String.valueOf(debuggerport));
//		
//		commandList.add("-chrome");
//		commandList.add("chrome://chromebug/content/chromebug.xul");
//		
//		String[] args = firefoxargs.split(" ");
//		for (String string : args) {
//			commandList.add(string);
//		}
//		
//		return commandList.toArray(new String[commandList.size()]);
//	}
	
	
	public String[] getDebugStartCommandLine() throws CoreException
	{
		List<String> commands = new ArrayList<String>();	
		
		String firefoxpath = fLaunchConfiguration.getAttribute(PrefConst.FIREFOX_PATH, "");
		String profilepath = fLaunchConfiguration.getAttribute(PrefConst.FIREFOX_PROFILE_PATH , "");		
		
		commands.add(firefoxpath);
		
		commands.add("-no-remote");
		
		commands.add("-profile");
		commands.add("\"" + profilepath + "\"");
		
		int eclispport = fStore.getInt(PrefConst.ECLIPSE_PORT);
		int debuggerport = fStore.getInt(PrefConst.DEBUGGER_PORT);
		
		commands.add("-ce_eport");
		commands.add(String.valueOf(eclispport));
		commands.add("-ce_cport");
		commands.add(String.valueOf(debuggerport));
		
		commands.add("-chrome");
		commands.add("chrome://chromebug/content/chromebug.xul");
		
		String firefoxargs = fLaunchConfiguration.getAttribute(PrefConst.FIREFOX_ARGS, "");
		String[] args = firefoxargs.split(" ");
		for (String string : args) {
			commands.add(string);
		}		
		
		return commands.toArray(new String[commands.size()]);
	}
	
	// /hello/content/main/hello.js -> chrome://hello/content/hello.js
	
	//mapProject2Chrome projectname:{chrome, path},{chrome, path}
	// helloworld-0.0.6/chrome/content/helloworld/helloWorld.js
	public String getURI(IPath path)
	{
		String projectname = path.segment(0);
		String repath = path.removeFirstSegments(1).removeLastSegments(1).toString() + "/";

		if(mapProject2Chrome.containsKey(projectname))
		{
			List<List<String>> list = mapProject2Chrome.get(projectname);
			int cnt = list.size();
			for (int i = 0; i < cnt; i++) 
			{	
				if(list.get(i).get(1).equals(repath))
				{
					return "chrome://" +list.get(i).get(0)+ "/content/" + path.lastSegment();
				}
			}
		}
		return null;
	}
	
	// chrome://hello/content/hello.js -> /hello/content/main/hello.js
	// chrome:[projectname, path]
	public IPath getPath(String URI)
	{
		
		IPath path = new Path(URI);
		return path;
		//return ResourcesPlugin.getWorkspace().getRoot().getFile(projectpath.append(path).append(file)).getLocation();
//		Matcher m = p.matcher(URI);
//		if (m.find()) 
//		{
//			String chrome = m.group(2);
//			if(mapChrome2Projec.containsKey(chrome))
//			{
//				List<String> list = mapChrome2Projec.get(chrome);
//				String projectname = list.get(0);
//				String path = list.get(1);
//				String file = m.group(4);
//				
//				IPath projectpath = new Path(projectname);
//				
//				return ResourcesPlugin.getWorkspace().getRoot().getFile(projectpath.append(path).append(file)).getLocation();
//			}
//		}
//		throw new NullPointerException();
	}
}
