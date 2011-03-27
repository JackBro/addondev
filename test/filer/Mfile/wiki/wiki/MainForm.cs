using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using mshtml;
using System.IO;
using Jint;
using System.Diagnostics;
using System.Text.RegularExpressions;
using Sgry.Azuki.WinForms;
using System.Runtime.InteropServices;
using wiki.control;
using KaoriYa.Migemo;

namespace wiki
{
    public partial class MainForm : Form
    {
        private ItemManager manager;
        private Migemo migemo;

        private long baseticks;

        private HttpServer httpServer;
        private BackgroundWorker serveBW;

        private Config config;

        //SearchControl _browserSearchControl;
        
        ScriptManager sm = new ScriptManager();

        Regex regShow = new Regex(@"\/item\/(\d+)$", RegexOptions.Compiled);
        Regex regEdit = new Regex(@"\/item\/(\d+)\/(edit)$", RegexOptions.Compiled);
        Regex regExe = new Regex(@"\/item\/(exe)$", RegexOptions.Compiled);
        Regex regMove = new Regex(@"\/item\/(\d+)\/(move)$", RegexOptions.Compiled);
        Regex regGoto = new Regex(@"\/item\/(goto)$", RegexOptions.Compiled);
        Regex regComeFrom = new Regex(@"\/item\/(\d+)\/(comefrom)$", RegexOptions.Compiled);

        private Dictionary<string, string> reqparam = new Dictionary<string, string>();

        private TabPage AllPage;

        //private AzukiControlEx Editor;
       
        public MainForm(){
            InitializeComponent();


            try {	        
        		config = XMLSerializer.Deserialize<Config>(Config.SettingPath);
	        }
	        catch (Exception){
		        //throw;
                config = null;
	        }
            if (config == null) {
                config = new Config();
            }
            //config = new Config();
            config.htmlPath = Path.GetFullPath(@"..\..\html\wiki_parser.html");
            config.ScriptDirPath = Path.GetFullPath(@"..\..\scripts");
            config.MigemoDictPath = Path.GetFullPath(@"..\..\migemo\dict\migemo-dict");

            initKeyMap();
            initEditor();
            initSearch();
            initBrowser();

            sm.init();
            sm.ScriptDir = config.ScriptDirPath;


            reqparam.Add("method", string.Empty);
            reqparam.Add("url", string.Empty);
            reqparam.Add("id", string.Empty);
            reqparam.Add("data", string.Empty);

            httpServer = new HttpServer(config.Port);
            httpServer.RequestEvent += (sender, e) => {
                var url = e.Request.RawUrl;
                if(url.IndexOf('?')>=0){
                    url = e.Request.RawUrl.Split('?')[0];
                }
                var methd = e.Request.HttpMethod;
                e.Response = "accept";

                var m = regShow.Match(url);
                if (m.Success) {
                    var idlist = new List<long>();
                    var idstr = m.Groups[1].Value;
                    if (idstr.IndexOf(',') > 0) {
                        var ids = idstr.Split(',');
                        foreach (var id in ids) {
                            idlist.Add(long.Parse(id));
                        }
                    }else{
                        var id = long.Parse(idstr);
                        idlist.Add(id);
                    }
                    //var id = long.Parse(m.Groups[1].Value);
                    var items = manager.GetItem(idlist);
                    var res =string.Empty;
                    switch (methd) {
                        case "DELETE":
                            reqparam["method"] = "delete";
                            reqparam["id"] = m.Groups[1].Value;
                            serveBW.ReportProgress(1, reqparam);
                            break;
                        case "GET":
                            List<Data> list = items;// new List<Data>() { item };
                            res = JsonSerializer.Serialize(list);
                            e.Response = res;
                            break;
                        default:
                            break;
                    }
                    
                    return;
                }

                m = regExe.Match(url);
                if (m.Success) {
                    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                    reqparam["method"] = "exe";
                    reqparam["data"] = _RequestBody;
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }

                m = regEdit.Match(url);
                if (m.Success) {
                    var id = m.Groups[1].Value;

                    reqparam["method"] = "edit";
                    reqparam["url"] = url;
                    reqparam["id"] = id;
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }

                m = regMove.Match(url);
                if (m.Success) {
                    var id = m.Groups[1].Value;

                    reqparam["method"] = "move";
                    reqparam["url"] = url;
                    reqparam["id"] = id;
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }

                m = regComeFrom.Match(url);
                if (m.Success) {
                    reqparam["method"] = "comefrom";
                    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                    reqparam["data"] = _RequestBody;
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }

                m = regGoto.Match(url);
                if (m.Success) {
                    reqparam["method"] = "goto";
                    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                    reqparam["data"] = _RequestBody;
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }

            };
            serveBW = new BackgroundWorker();
            serveBW.WorkerReportsProgress = true;
            serveBW.WorkerSupportsCancellation = true;
            serveBW.ProgressChanged += (sender, e) => {
                //if (serveBW.CancellationPending) {
                //    httpServer.stop();
                //    return;
                //}
                var param = e.UserState as Dictionary<string, string>; 
                switch (param["method"]) {
                    case "edit": {
                            var id = long.Parse(param["id"]);
                            this.EditItem(id);
                        }
                        break;
                    case "delete": {
                            var id = long.Parse(param["id"]);
                            this.DeleteItem(id);
                        }
                        break;
                    case "exe":
                        var args = param["data"];
                        sm.Run("test.js", args);
                        break;
                    case "move": {
                            var id = long.Parse(param["id"]);
                            this.Moves(id);
                        }
                        break;
                    case "goto": {
                            var word = param["data"];
                            var s = CreateSearchObj(word, SearchMode.Normal);
                            ItemTabControl.SelectedTab = CreateListViewTabPage(word, s);
                        }
                        break;
                    case "comefrom": {
                            
                    }
                        break;
                    default:
                        break;
                }
            };

            serveBW.DoWork += (sender, e) => {
                httpServer.start();
            };
            serveBW.RunWorkerCompleted += (sender, e) => {
                
            };
            serveBW.RunWorkerAsync();
            //TimeSpan utcOffset = System.TimeZoneInfo.Local.BaseUtcOffset;
            //baseticks = new DateTime(1601, 01, 01).Ticks + utcOffset.Ticks;
            baseticks = 0;

            this.Load += (sender1, e1) => {
                

            };
            this.FormClosing += (sender, e) => {
                if (manager.IsDirty) {
                    manager.Save();
                }
                //serveBW.CancelAsync();
                //serveBW.ReportProgress(0);
                //httpServer.stop();
                XMLSerializer.Serialize<Config>(Config.SettingPath, config);
            };

            //this.SizeChanged += (sender, e) => {
            //    for (int i = 0; i < ItemTabControl.TabPages.Count; i++) {
            //        var page = ItemTabControl.TabPages[i];
            //        var listview = GetTabControl(page);
            //        listview.Columns["title"].Width = -2;
            //    }
            //};

            _editor.TextChanged += (sender, e) => {
                
                if (dirty) {
                    var item = manager.EditingData;
                    if (item != null) {
                        item.Text = _editor.Text;
                        manager.UpDate(item);
                    }
                }
                dirty = true;
            };
            _editor.LostFocus += (sender, e) => {
                
            };

            ItemTabControl.Selecting += (sender, e) => {
            };

            ItemTabControl.SelectedIndexChanged += (sender, e) => {
                var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                var list = getCurrentPageDatas(listview, config.ShowNum);
                //webBrowser1.Document.InvokeScript("testClearAll");
                InvokeScript("js_ClearAll");
                //reBuild(listview.DataItems);
                reBuild(list);
            };
            //ItemTabControl.MouseDown

            //en = new JintEngine();
            //en.DisableSecurity();
            //en.AllowClr = true;
            ////ren();
            ////en.Run("function test(){ return 10; }");
            ////var r = en.Run(@"x=eval( [ ""hoge"":""hogehoge"", ""foo"":""j.png"" ]); return x;");
            ////var r2 = en.Run("return 100;");
            ////var r3 = en.Run("x=test()*100; return x;");
            ////en.SetFunction("testf", new Jint.Native.JsFunction());
            //en.SetFunction("square", new Action(() => { CreateItem(); }));
            //en.SetFunction("deleteitem", new Action<object>((id) => {
            //    Console.WriteLine("deleteitem id = " + id.ToString());
            //    this.DeleteItem(long.Parse(id.ToString()));
            //}));
            //en.SetFunction("edititem", new Action<object>((id) => {
            //    Console.WriteLine("edititem id = " + id.ToString());
            //    this.EditItem(long.Parse(id.ToString()));
            //}));

            NewItemToolStripButton.Click += (sender, e) => {             
                CreateItem();
            };

            manager = new ItemManager();
            manager.DataPath = "data.bin";
            manager.Load();
            manager.eventHandler += (sender, e) => {
                switch (e.type) {
                    case ChangeType.Insert:
                        {
                            //ItemTabControl.SelectedIndex = 0;
                            ItemTabControl.SelectedTab = AllPage;
                            //var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                            var listview = GetTabControl(AllPage);
                            var iindex = listview.AddItem(e.Item);
                            if (iindex == (listview.DataItems.Count - 1)) {
                                reBuild(e.Item);
                            }
                            else {
                                //reBuild(listview.DataItems[iindex+1].ID, e.Item);
                                reBuild(listview.DataItems[iindex + 1].ID, e.Item);
                            }
                            //listview.AddItem(e.Index, e.Item);         
                        }
                        break;
                    case ChangeType.UpDate:
                        //reBuild(e.Item);
                        editContent(e.Item);
                        var cf = InvokeScript("js_getComeFrom");
                        var list = JsonSerializer.Deserialize<List<string>>(cf.ToString());
                        //Console.WriteLine("cf = " + cf);
                        config.ComeFormWords.Union(list);
                        break;
                    case ChangeType.Delete:
                        for (int i = 0; i < ItemTabControl.TabPages.Count; i++) {
                            var listview = GetTabControl(ItemTabControl.TabPages[i]);
                            listview.DeleteItem(e.Item);
                        }
                        InvokeScript("js_Remove", e.Item.ID.ToString());
                        break;
                    default:
                        break;
                }
            };


            //var all = new SearchAll();
            //AllPage = CreateListViewTabPage("All", manager.Filter(x => { return true; }));
            AllPage = CreateListViewTabPage("All", new SearchAll());
            //reBuild(newlistview.DataItems);
            //webBrowser1.ScriptErrorsSuppressed = true;
            //webBrowser1.ScrollBarsEnabled = true;
            
            webBrowser1.IsWebBrowserContextMenuEnabled = false;
            var p = config.htmlPath;// Path.GetFullPath(@"..\..\html\wiki_parser.html");
            webBrowser1.Navigate(p);
            webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
            //webBrowser1.Navigating += new WebBrowserNavigatingEventHandler(webBrowser1_Navigating);
            webBrowser1.StatusTextChanged += (sender, e) => {
                toolStripStatusLabel1.Text = webBrowser1.StatusText;
            };

            if (config.ShowType == ShowType.List) {
                ShowLargeToolStripMenuItem.Checked = false;
                ShowListToolStripMenuItem.Checked = true;
                ToggleShowToolStripSplitButton.Image = global::wiki.Properties.Resources.win_show_detail;
                ToggleShowToolStripSplitButton.Text = "List";
            } else if (config.ShowType == ShowType.Large) {
                ShowLargeToolStripMenuItem.Checked = true;
                ShowListToolStripMenuItem.Checked = false;
                ToggleShowToolStripSplitButton.Image = global::wiki.Properties.Resources.win_show_largeIcon;
                ToggleShowToolStripSplitButton.Text = "Large";
            }
            this.KeyDown += (sender, e) => {
                var ee = e.KeyValue;
            };
            
            ToggleShowToolStripSplitButton.ButtonClick += (sender, e) => {
                ShowLargeToolStripMenuItem.Checked = !ShowLargeToolStripMenuItem.Checked;
                ShowListToolStripMenuItem.Checked = !ShowListToolStripMenuItem.Checked;

                var islist = ShowListToolStripMenuItem.Checked;
                if (islist) {
                    config.ShowType = ShowType.List;
                    ToggleShowToolStripSplitButton.Image = global::wiki.Properties.Resources.win_show_detail;
                    //initPage();
                    //var listview =  GetSelctedTabControl();
                    //List<Data> list = new List<Data>();
                    //var last = listview.DataItems.Count < config.ShowNum ? listview.DataItems.Count : config.ShowNum;
                    //for (int i = 0; i < last; i++) {
                    //    list.Add(listview.DataItems[i]);
                    //}
                    //reBuild(list);    
                }
                else {
                    config.ShowType = ShowType.Large;
                    ToggleShowToolStripSplitButton.Image = global::wiki.Properties.Resources.win_show_largeIcon;

                    //var listview = GetSelctedTabControl();
                    //var item = listview.GetSelectedItem();
                    //reBuild(item);
                }
                ToggleShow(config.ShowType);
            };
            ShowLargeToolStripMenuItem.Click += (sender, e) => {
                if (!ShowLargeToolStripMenuItem.Checked) {
                    ShowLargeToolStripMenuItem.Checked = true;
                    ShowListToolStripMenuItem.Checked = false;
                    config.ShowType = ShowType.Large;
                    ToggleShow(config.ShowType);
                }
            };
            ShowListToolStripMenuItem.Click += (sender, e) => {
                if (!ShowListToolStripMenuItem.Checked) {
                    ShowListToolStripMenuItem.Checked = true;
                    ShowLargeToolStripMenuItem.Checked = false;
                    config.ShowType = ShowType.List;
                    ToggleShow(config.ShowType);
                }
            };
            /*
            int max = 5;

            initPage();
            */
            PreToolStripButton.Click += (sender, e) => {
                
                var l = GetSelctedTabControl();
                l.Page--;

                //var si = l.Page * max;
                //var cnt = l.DataItems.Count;
                //var last = (si + max) > cnt ? cnt : (si + max);
                //List<Data> elist = new List<Data>();
                //for (int i = si; i < last; i++) {
                //    elist.Add(l.DataItems[i]);
                //}
                var elist = getCurrentPageDatas(l, config.ShowNum);
                InvokeScript("js_ClearAll");
                reBuild(elist);

                if ((l.Page - 1) * config.ShowNum <= 0) {
                    PreToolStripButton.Enabled = false;
                }
                NextToolStripButton.Enabled = true;
            };
            NextToolStripButton.Click += (sender, e) => {

                var l = GetSelctedTabControl();
                l.Page++;

                //var si = l.Page * max;
                //var cnt = l.DataItems.Count;
                //var last = (si + max) > cnt ? cnt : (si + max);
                //List<Data> elist = new List<Data>();
                //for (int i = si; i < last; i++) {
                //    elist.Add(l.DataItems[i]);
                //}
                var elist = getCurrentPageDatas(l, config.ShowNum);
                InvokeScript("js_ClearAll");
                reBuild(elist);

                if ((l.Page + 1) * config.ShowNum >= l.DataItems.Count) {
                    NextToolStripButton.Enabled = false;
                }
                PreToolStripButton.Enabled = true;
            };

            ReloadToolStripButton.Click += (sender, e) => {
                //InvokeScript("jsview.jsmsg", new string[] { "jsview.jsmsg test" });
                //manager.Insert(new Data { ID = manager.GetNewID(), Text = "after", CreationTime = new DateTime(DateTime.Now.Ticks * 2) });
                webBrowser1.Navigate(config.htmlPath);
                webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
            };

            
            //BrowserSearchToolStripButton.CheckedChanged += (sender, e) => {
            //    if (BrowserSearchToolStripButton.Checked) {
            //        if (_browserSearchControl == null) {
            //            _browserSearchControl = new SearchControl();
            //            _browserSearchControl.Dock = DockStyle.Bottom;
            //            _browserSearchControl.NextButton.Click += (ss, se) => {
                            
            //                var collect = webBrowser1.Document.All;
            //                HtmlDocument doc = webBrowser1.Document;
            //                mshtml.IHTMLDocument2 doc2 = doc.DomDocument as mshtml.IHTMLDocument2;
            //                var textRange = doc2.selection.createRange() as mshtml.IHTMLTxtRange;
            //                mshtml.IHTMLElement iee;
            //                IHTMLDOMNode child;
                            

            //            };
            //        }
            //        ViewEditorSplitContainer.Panel1.Controls.Add(_browserSearchControl);
            //    }
            //    else {
            //        ViewEditorSplitContainer.Panel1.Controls.Remove(_browserSearchControl);
            //    }
            //};
 
        }

        private void initPage() {
            if (config.ShowType == ShowType.Large) {
                PreToolStripButton.Enabled = false;
                NextToolStripButton.Enabled = false;
            } else if (config.ShowType == ShowType.List) {
                int max = config.ShowNum;
                var l = GetSelctedTabControl();
                if ((l.Page - 1) * max <= 0) {
                    PreToolStripButton.Enabled = false;
                }
                if ((l.Page + 1) * max >= l.DataItems.Count) {
                    NextToolStripButton.Enabled = false;
                }
            }
        }

        void ToggleShow(ShowType showtype) {
            if (showtype == ShowType.List) {
                //config.ShowType = ShowType.List;
                initPage();
                var listview = GetSelctedTabControl();
                List<Data> list = new List<Data>();
                var last = listview.DataItems.Count < config.ShowNum ? listview.DataItems.Count : config.ShowNum;
                for (int i = 0; i < last; i++) {
                    list.Add(listview.DataItems[i]);
                }
                reBuild(list);

            } else {
                //config.ShowType = ShowType.Large;
                var listview = GetSelctedTabControl();
                var item = listview.GetSelectedItem();
                reBuild(item);
            }
        }

        void webBrowser1_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e) {
            webBrowser1.DocumentCompleted -= webBrowser1_DocumentCompleted;
            //Console.WriteLine("webBrowser1_DocumentCompleted : " + e.Url);
            InvokeScript("setPort", httpServer.Port.ToString());
            //webBrowser1.Document.ContextMenuShowing += new HtmlElementEventHandler(Document_ContextMenuShowing);
            //webBrowser1.Document.Click += new HtmlElementEventHandler(Document_Click);
            
            
            //var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
            //List<Data> elist = new List<Data>();
            //var last = listview.DataItems.Count < 5 ? listview.DataItems.Count : 5;
            //for (int i = 0; i < last; i++) {
            //    elist.Add(listview.DataItems[i]);
            //}
            //reBuild(elist);

            //var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
            var listview = GetTabControl(AllPage);
            var items = listview.DataItems;
            if (items.Count > 0) {
                listview.Items[0].Selected = true;
                //reBuild(items[0]);
                ToggleShow(config.ShowType);
            }


            //this.webBrowser1.Document.MouseDown += (ss, se) => {
            //    var ee = e;
            //};
        }

        internal void CreateItem() {
            manager.Insert(new Data { ID=manager.GetNewID(), Text = "!new", CreationTime = DateTime.Now });
        }
        private void DeleteItem(long id) {
            manager.Delete(id);   
        }
        private void EditItem(long id) {
            var item = manager.GetItem(id);
            if (item == null) return;

            this.OpenEditor();

            manager.EditingData = item;
            dirty = false;
            _editor.Text = item.Text;
        }

        private List<Data> getCurrentPageDatas(ListViewEx listview, int max) {
            var si = listview.Page * max;
            var cnt = listview.DataItems.Count;
            var last = (si + max) > cnt ? cnt : (si + max);
            var list = new List<Data>();
            for (int i = si; i < last; i++) {
                list.Add(listview.DataItems[i]);
            }
            return list;
        }

        private void Moves(long id) {
            if (config.ShowType == ShowType.Large) {
                var item = manager.GetItem(id);
                reBuild(item);
            } else if (config.ShowType == ShowType.List) {
            
                var view = GetSelctedTabControl();
                var index = view.DataItems.FindIndex((n) => {
                    return (n.ID == id);
                });
                if (index == -1) {
                    ItemTabControl.SelectedTab = AllPage;
                    view = GetSelctedTabControl();
                    index = view.DataItems.FindIndex((n) => {
                        return (n.ID == id);
                    });
                }

            //if (index != -1) {
                index++;
                var page = 0;
                if (index <= config.ShowNum) {
                    page = 0;
                }
                else {
                    if (index % config.ShowNum == 0) {
                        page = index / config.ShowNum - 1;
                    }
                    else {
                        page = index / config.ShowNum;
                    }
                }
                if (view.Page != page) {
                    var list = getCurrentPageDatas(view, config.ShowNum);
                    reBuild(list);
                }
                var p = config.htmlPath + "#" + id;
                webBrowser1.Navigate(p);

            //}
            //else {
            //}
            }
        }

        private ListViewEx GetTabControl(TabPage page) {
            return page.Controls[0] as ListViewEx; 
        }

        private ListViewEx GetSelctedTabControl() {
            return GetTabControl(ItemTabControl.SelectedTab);
        }

        private Dictionary<Search, TabPage> searchtabdic = new Dictionary<Search, TabPage>(); 
        private bool dirty = false;
        private TabPage CreateListViewTabPage(string name, Search search) {

            foreach (var item in searchtabdic.Keys) {
                if(item.Equals(search)){
                    var p = searchtabdic[search];
                    var lv = GetTabControl(p);
                    lv.DataItems = manager.Filter(search.getSearch());
                    return p;
                }
            }
         
            var items = manager.Filter(search.getSearch());
            var listview = new ListViewEx(items);

            listview.MouseUp += (sender, e) => {
                var item = listview.GetItemAt(e.X, e.Y);
                if (item == null && listview.FocusedItem !=null) {
                    listview.FocusedItem.Selected = true;
                }
            };

            listview.SelectedIndexChanged += (sender, e) => {
                if (config.ShowType== ShowType.Large && listview.SelectedIndices.Count == 1) {
                    var selindex = listview.SelectedIndices[0];
                    var item = listview.DataItems[selindex];
                    //var cf = InvokeScript("js_getComeFrom");
                    //var dic = JsonSerializer.Deserialize<Dictionary<string, string>>(cf.ToString());
                    //Console.WriteLine("cf = " + cf);
                    reBuild(item);

                }
            };
            listview.DoubleClick += (sender, e) => {
                if (config.ShowType == ShowType.List && listview.SelectedIndices.Count == 1) {
                    var selindex = listview.SelectedIndices[0];
                    var id = listview.DataItems[selindex].ID;
                    //var p = config.htmlPath + "#" + id;
                    //webBrowser1.Navigate(p);

                    this.Moves(id);
                }
            };

            listview.Dock = DockStyle.Fill;

            var t = new TabPage(name);
            t.Controls.Add(listview);
            ItemTabControl.TabPages.Add(t);

            searchtabdic.Add(search, t);

            return t;
        }

        private TabPage CreateListViewTabPage(string name, List<Data> items) {
            
            var listview = new ListViewEx(items);

            listview.SelectedIndexChanged += (sender, e) => {
                if (config.ShowType == ShowType.Large && listview.SelectedIndices.Count == 1) {
                    var selindex = listview.SelectedIndices[0];
                    var item = listview.DataItems[selindex];
                    //var cf = InvokeScript("js_getComeFrom");
                    //var dic = JsonSerializer.Deserialize<List<string>>(cf.ToString());
                    //Console.WriteLine("cf = " + cf);
                    reBuild(item);

                }
            };
            listview.DoubleClick += (sender, e) => {
                if (config.ShowType == ShowType.List && listview.SelectedIndices.Count == 1) {
                    var selindex = listview.SelectedIndices[0];
                    var id = listview.DataItems[selindex].ID;
                    //var p = config.htmlPath + "#" + id;
                    //webBrowser1.Navigate(p);

                    this.Moves(id);
                }
            };

            listview.Dock = DockStyle.Fill;

            var t = new TabPage(name);
            t.Controls.Add(listview);
            ItemTabControl.TabPages.Add(t);

            return t;
        }
        
        //void Request(Uri request) {
        //    var bp = Path.GetFullPath(@"..\..\scripts");
        //    var rs = request.Scheme; //request.Split(new char[] { '/' });
        //    var path = Path.Combine(bp, rs+".js");
        //    if (!File.Exists(path)) return;

        //    var script = File.ReadAllText(path);
        //    en.SetParameter("request", request.AbsoluteUri);
        //    //en.SetFunction("deleteitem", new Action<int>((id) => {
        //    //    Console.WriteLine("deleteitem id = " + id.ToString());
        //    //}));
        //    object result = en.Run(script);
        //    int k = 0;
        //}

        //void ren() {
        //    var path = Path.GetFullPath(@"..\..\scripts\test.js");
        //    if (!File.Exists(path)) return;

        //    var script = File.ReadAllText(path);
        //    en.SetParameter("para", script);
        //    var r = en.Run(@"x=eval( '(' + para + ')' ); return x[0].date;");
        //    int k = 0;
        //}

        private Object InvokeScript(string function, params string[] param) {
            return webBrowser1.Document.InvokeScript(function, param);
        }
        private Object InvokeScript(string function) {
            return webBrowser1.Document.InvokeScript(function);
        }
        
        void Document_Click(object sender, HtmlElementEventArgs e) {
            //throw new NotImplementedException();
            var en = e;
        }

        void Document_ContextMenuShowing(object sender, HtmlElementEventArgs e) {
            var ae = webBrowser1.Document.ActiveElement;


        }

        void webBrowser1_Navigating(object sender, WebBrowserNavigatingEventArgs e)
        {
            var l = e.Url;
            if (!e.Url.AbsoluteUri.Contains("sub.html") && !e.Url.AbsoluteUri.Contains(".jpg")) {
                //e.Cancel = true;
            }
            if(e.Url.AbsoluteUri == "test://test/"){
                //reBuild(GetSelctedTabControl().DataItems[0]);
                //InvokeScript("test");
                //e.Cancel = true;
            }
            
            //this.Request(e.Url);
            //EditItem(1);
        }

        private Migemo getMigemo() {
            if (migemo == null) {
                migemo = new Migemo(config.MigemoDictPath);
            }
            return migemo;
        }

        private void reBuild(List<Data> items) {
            var json = JsonSerializer.Serialize(items);
            InvokeScript("js_BuildByID", json);
        }

        private void editContent(Data item) {
            var json = JsonSerializer.Serialize(item);
            InvokeScript("js_editContent", json);
        }

        private void reBuild(Data item) {
            //var list = new List<Data>() { item };
            //var json = JsonSerializer.Serialize(list);
            //InvokeScript("js_BuildByID", json);
            List<string> wo = new List<string>() { "test", "FireBug" };
            var words = JsonSerializer.Serialize(wo);
            var json = JsonSerializer.Serialize(item);
            InvokeScript("js_BuildByID", json, words);
        }

        private void reBuild(long insertBefore,  Data item) {
            //var list = new List<Data>() { item };
            //var json = JsonSerializer.Serialize(list);
            //InvokeScript("js_BuildInsertByID", insertBefore.ToString(), json);
            var json = JsonSerializer.Serialize(item);
            InvokeScript("js_BuildInsertByID", insertBefore.ToString(), json);
        }

        private void webBrowser1_ContextMenuStripChanged(object sender, EventArgs e) {

        }

        //private string cnv(List<Data> items) {
            
        //    items.ForEach(n => {
        //        n.Text.
        //    });
        //    foreach (var item in items) {
                
        //    }
        //    var json = JsonSerializer.Serialize(list);
        //}

        private void timeToolStripMenuItem_Click(object sender, EventArgs e) {
            DateTimeForm f = new DateTimeForm();
            f.Time = DateTime.Now;
            var res = f.ShowDialog();
            var restime = f.Time;
        }

        private void toolStripMenuItem1_Click(object sender, EventArgs e) {
            var ae = webBrowser1.Document.ActiveElement;
            var h = ae.GetAttribute("href");
            
            var mm=0;
           
            
        }

        private void OpenEditor() {
            ViewEditorSplitContainer.Panel2Collapsed = false;
        }
    }

    
}
