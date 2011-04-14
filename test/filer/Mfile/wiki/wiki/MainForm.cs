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
using System.Collections;

namespace wiki
{
    public partial class MainForm : Form
    {
        private class ListViewIndexComparer : IComparer {
            #region IComparer メンバ

            public int Compare(object x, object y) {
                return ((ListViewItem)x).Index - ((ListViewItem)y).Index;
            }

            #endregion
        }

        private string categoryname;
        private Category category;

        private Migemo migemo;

        private HttpServer httpServer;
        private BackgroundWorker serveBW;

        internal Config config;
        
        ScriptManager sm = new ScriptManager();

        Regex regID = new Regex(@"\/item\/(\d+)$", RegexOptions.Compiled);

        //Regex regEdit = new Regex(@"\/item\/(\d+)\/(edit)$", RegexOptions.Compiled);
        //Regex regMove = new Regex(@"\/item\/(\d+)\/(move)$", RegexOptions.Compiled);
        //Regex regComeFrom = new Regex(@"\/item\/(\d+)\/(comefrom)$", RegexOptions.Compiled);

        Regex regIDMethod = new Regex(@"\/item\/(\d+)\/(.+)$", RegexOptions.Compiled);

        //Regex regExe = new Regex(@"\/item\/(exe)$", RegexOptions.Compiled);
        //Regex regGoto = new Regex(@"\/item\/(goto)$", RegexOptions.Compiled);
        //Regex regNew = new Regex(@"\/item\/(new)$", RegexOptions.Compiled);
        //Regex regScript = new Regex(@"\/item\/(script)$", RegexOptions.Compiled);

        Regex regMethod = new Regex(@"\/item\/(\w+)$", RegexOptions.Compiled);

        private Dictionary<string, string> reqparam = new Dictionary<string, string>();

        public MainForm() {
            InitializeComponent();

            try {
                config = XMLSerializer.Deserialize<Config>(Config.SettingPath, new Config());
            }
            catch (Exception) {
                //throw;
                config = null;
            }
            if (config == null) {
                config = new Config();
            }

            config.htmlPath = Path.GetFullPath(@"..\..\html\wiki_parser.html");
            config.ScriptDirPath = Path.GetFullPath(@"..\..\scripts");
            config.MigemoDictPath = Path.GetFullPath(@"..\..\migemo\dict\migemo-dict");
            config.DataDirPath = Path.GetFullPath(@".\data");
            config.SnippetListPath = Path.GetFullPath(@".\SnippetList.xml");
            config.LoadSnippetList();
            this.WindowState = config.WindowState;
            if (config.WindowState != FormWindowState.Maximized) {
                this.Size = config.WindowSize;
            }
            this.Location = config.WindowPos;
            splitContainer1.SplitterDistance = config.CategoryListViewW;
            ViewEditorSplitContainer.SplitterDistance = config.BrowserH;
            ListViewSplitContainer.Orientation = config.TabListView_BrowserOri;
            ListViewSplitContainer.SplitterDistance = config.ListViewSize;
            
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
                if (url.IndexOf('?') >= 0) {
                    url = e.Request.RawUrl.Split('?')[0];
                }
                var httpmathod = e.Request.HttpMethod;
                e.Response = "accept";

                var m = regID.Match(url);
                if (m.Success) {
                    var idlist = new List<long>();
                    var idstr = m.Groups[1].Value;
                    if (idstr.IndexOf(',') > 0) {
                        var ids = idstr.Split(',');
                        foreach (var id in ids) {
                            idlist.Add(long.Parse(id));
                        }
                    }
                    else {
                        var id = long.Parse(idstr);
                        idlist.Add(id);
                    }
                    //var manager = category.getManger(getSelectedCategory());
                    //var items = manager.GetItem(idlist);
                    var items = category.GetItem(idlist);
                    var res = string.Empty;
                    switch (httpmathod) {
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

                //m = regEdit.Match(url);
                //if (m.Success) {
                //    var id = m.Groups[1].Value;

                //    reqparam["method"] = "edit";
                //    //reqparam["url"] = url;
                //    reqparam["id"] = id;
                //    serveBW.ReportProgress(1, reqparam);
                //    return;
                //}
                //m = regMove.Match(url);
                //if (m.Success) {
                //    var id = m.Groups[1].Value;

                //    reqparam["method"] = "move";
                //    //reqparam["url"] = url;
                //    reqparam["id"] = id;
                //    serveBW.ReportProgress(1, reqparam);
                //    return;
                //}
                //m = regComeFrom.Match(url);
                //if (m.Success) {
                //    reqparam["method"] = "comefrom";
                //    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                //    reqparam["data"] = _RequestBody;
                //    serveBW.ReportProgress(1, reqparam);
                //    return;
                //}
                m = regIDMethod.Match(url);
                if (m.Success) {
                    var id = m.Groups[1].Value;
                    var method = m.Groups[2].Value;
                    reqparam["id"] = id;
                    reqparam["method"] = method;
                    if (httpmathod == "POST") {
                        var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                        reqparam["data"] = _RequestBody;                       
                    }
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }


                //m = regExe.Match(url);
                //if (m.Success) {
                //    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                //    reqparam["method"] = "exe";
                //    reqparam["data"] = _RequestBody;
                //    serveBW.ReportProgress(1, reqparam);
                //    return;
                //}
                //m = regGoto.Match(url);
                //if (m.Success) {
                //    reqparam["method"] = "goto";
                //    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                //    reqparam["data"] = _RequestBody;
                //    serveBW.ReportProgress(1, reqparam);
                //    return;
                //}
                //m = regScript.Match(url);
                //if (m.Success) {
                //    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                //    reqparam["method"] = "script";
                //    reqparam["data"] = _RequestBody;
                //    serveBW.ReportProgress(1, reqparam);
                //    return;
                //}
                m = regMethod.Match(url);
                if (m.Success) {
                    var method = m.Groups[1].Value;
                    reqparam["method"] = method;
                    if (httpmathod == "POST") {
                        var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                        reqparam["data"] = _RequestBody;
                    }
                    serveBW.ReportProgress(1, reqparam);
                    return;
                }
            };
            serveBW = new BackgroundWorker();
            serveBW.WorkerReportsProgress = true;
            serveBW.WorkerSupportsCancellation = true;
            serveBW.ProgressChanged += (sender, e) => {

                if (serveBW.CancellationPending) {
                    httpServer.stop();
                    return;
                }
                var param = e.UserState as Dictionary<string, string>;
                switch (param["method"]) {
                    case "edit": {
                            var id = long.Parse(param["id"]);
                            this.EditItem(id, true);
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
                    case "script":
                        var script = param["data"];
                        sm.Eval(script);
                        break;
                    case "move": {
                            var id = long.Parse(param["id"]);
                            this.Moves(id);
                        }
                        break;
                    case "select": {
                            var id = long.Parse(param["id"]);
                            var lv = GetSelctedTabListViewControl();
                            var index = lv.DataItems.FindIndex(n => {
                                return n.ID == id;
                            });
                            if (index >= 0 && index<lv.Items.Count) {
                                lv.Items[index].Selected = true;
                                lv.Items[index].EnsureVisible();
                            }
                        }
                        break;
                    case "goto": {
                            var word = param["data"];
                            var s = CreateSearchObj(word, SearchMode.Text);
                            //ItemTabControl.SelectedTab = CreateListViewTabPage(word, s);
                            var seltabc = getTabControl(getSelectedCategory());
                            seltabc.SelectedTab = CreateListViewTabPage(getSelectedCategory(), seltabc, s);
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
                try {
                    httpServer.start();
                }
                //catch (System.Net.HttpListenerException ex) {
                catch (Exception ex) {
                    throw;
                }     
            };
            serveBW.RunWorkerCompleted += (sender, e) => {
                if (e.Error is System.Net.HttpListenerException) {
                    var ret = MessageBox.Show("port", "ERROR", MessageBoxButtons.OKCancel);
                    this.Option();
                }
                
            };
            serveBW.RunWorkerAsync();

            this.Load += (sender1, e1) => {

                initPage();

            };
            this.FormClosing += (sender, e) => {

                //if (manager.IsDirty) {
                //    manager.Save();
                //}
                category.Save();
                if (migemo != null) {
                    migemo.Dispose();
                }
                serveBW.CancelAsync();
                serveBW.ReportProgress(0);
                //httpServer.stop();

                config.WindowState = this.WindowState;
                config.WindowSize = this.Size;
                config.WindowPos = this.Location;
                config.CategoryListViewW = splitContainer1.SplitterDistance;
                config.BrowserH = ViewEditorSplitContainer.SplitterDistance;
                config.TabListView_BrowserOri = ListViewSplitContainer.Orientation;
                config.ListViewSize = ListViewSplitContainer.SplitterDistance;

                config.EdiorWrap = EditorWrapToolStripButton.Checked;

                config.SaveSnippetList();

                var TabList = new Dictionary<string, List<KeyValuePair<string, SearchMode>>>();
                for (int i = 0; i < CategoryListView.Items.Count; i++) {
                    var list = new List<KeyValuePair<string, SearchMode>>();
                    var item = CategoryListView.Items[i];
                    var tabs = ((TabControl)TabPanel.Controls[item.Name]).TabPages;
                    for (int j = 0; j < tabs.Count; j++) {
                        var listview = GetTabListViewControl(tabs[j]);
                        var s = listview.search;
                        if (s.Mode != SearchMode.All) {
                            var mode = s.Mode;
                            var pt = s.Pattern;
                            list.Add(new KeyValuePair<string, SearchMode>(pt, mode));
                        }
                    }
                    TabList.Add(item.Name, list);
                }

                config.TabListJson = JsonSerializer.Serialize(TabList);
                XMLSerializer.Serialize<Config>(Config.SettingPath, config);
            };

            _editor.TextChanged += (sender, e) => {

                if (dirty) {
                    //var manager = category.getManger(getSelectedCategory());
                    //var item = manager.EditingData;
                    //var item = category.GetItem(category.EditingID);
                    //if (item != null) {
                        //item.Text = _editor.Text;
                        //manager.UpDate(item);
                        //category.UpDate(item);
                        category.UpDateText(category.EditingID, _editor.Text);
                    //}
                }
                dirty = true;
            };

            NewItemToolStripButton.Click += (sender, e) => {
                CreateItem();
            };

            NewFileToolStripButton.Click += (sender, e) => {
                CreateNewFile("new");
            };

            category = new Category();
            category.DataDir = config.DataDirPath;
            category.eventHandler = (sender, e) => {
                switch (e.type) {
                    case ChangeType.Create: {
                            //if (Tabs[e.Name] == GetCurrentTabControl()) {
                            var name = category.getCategoryName(e.FromCategoryID);
                            if (TabPanel.Controls[name] == GetCurrentTabControl()) {
                                //ItemTabControl.SelectedTab = AllPage;
                                GetCurrentTabControl().SelectedIndex = 0;
                                var listview = GetSelctedTabListViewControl();// GetTabListViewControl(AllPage);
                                var iindex = listview.AddItem(e.Item);
                                if (iindex == (listview.DataItems.Count - 1)) {
                                    reBuild(e.Item);
                                }
                                else {
                                    reBuild(listview.DataItems[iindex + 1].ID, e.Item);
                                }
                                this.EditItem(e.Item.ID, true);
                            }
                            else {
                                //var tabc = Tabs[e.Name];

                                //var listview = GetTabListViewControl(Tabs[e.Name].TabPages[0]);
                                var listview = GetTabListViewControl(((TabControl)TabPanel.Controls[name]).TabPages[0]);
                                listview.AddItem(e.Item);
                            }
                        }
                        break;
                    case ChangeType.UpDateText: {
                            //if (Tabs[e.Name] == GetCurrentTabControl()) {
                            var name = category.getCategoryName(e.FromCategoryID);
                            if (TabPanel.Controls[name] == GetCurrentTabControl()) {
                                editContent(e.Item);
                                //var cf = InvokeScript("js_getComeFrom");
                                //var list = JsonSerializer.Deserialize<List<string>>(cf.ToString());
                                //config.ComeFormWords.Union(list);
                            }
                        }
                        break;
                    case ChangeType.UpDateCreationTime: {
                            var name = category.getCategoryName(e.FromCategoryID);
                            var tabc = getTabControl(name);
                            var s = new DateTimeComparer();
                            for (int i = 0; i < tabc.TabPages.Count; i++) {
                                var lv = GetTabListViewControl(tabc.TabPages[i]);
                                if (lv.DataItems.IndexOf(e.Item) >= 0) {
                                    lv.DataItems.Sort(s);
                                    break;
                                }
                            }
                        }
                        break;
                    case ChangeType.UpDateCategory: {
                            var form = e.FromCategoryID;
                            var to = e.ToCategoryID;

                            //if (Tabs[e.Name] == GetCurrentTabControl()) {
                            var fromname = category.getCategoryName(e.FromCategoryID);
                            var fromtabc = getTabControl(fromname);
                            for (int i = 0; i < fromtabc.TabPages.Count; i++) {
                                var lv = GetTabListViewControl(fromtabc.TabPages[i]);
                                if (lv.DataItems.IndexOf(e.Item) >= 0) {
                                    lv.DeleteItem(e.Item);
                                    break;
                                }
                            }

                            var toname = category.getCategoryName(e.ToCategoryID);
                            var totabc = getTabControl(toname);
                            for (int i = 0; i < totabc.TabPages.Count; i++) {
                                var lv = GetTabListViewControl(totabc.TabPages[i]);
                                lv.AddItem(e.Item);
                            }
                        }
                        break;
                    case ChangeType.Delete:
                        //foreach (var item in Tabs.Values) {
                        //    for (int i = 0; i < item.TabPages.Count; i++) {
                        //        var listview = GetTabListViewControl(item.TabPages[i]);
                        //        listview.DeleteItem(e.Item);
                        //    }
                        //}
                        ////for (int i = 0; i < ItemTabControl.TabPages.Count; i++) {
                        ////    var listview = GetTabListViewControl(ItemTabControl.TabPages[i]);
                        ////    listview.DeleteItem(e.Item);
                        ////}
                        //if (Tabs[e.Name] == GetCurrentTabControl()) {
                        //    InvokeScript("js_Remove", e.Item.ID.ToString());
                        //}

                        {
                            for (int i = 0; i < CategoryListView.Items.Count; i++) {
                                var item = CategoryListView.Items[i];
                                var tabpages = ((TabControl)TabPanel.Controls[item.Name]).TabPages;
                                for (int j = 0; j < tabpages.Count; j++) {
                                    var listview = GetTabListViewControl(tabpages[j]);
                                    listview.DeleteItem(e.Item);
                                }
                            }
                            var name = category.getCategoryName(e.FromCategoryID);
                            if (name == GetCurrentTabControl().Name) {
                                InvokeScript("js_Remove", e.Item.ID.ToString());
                            }
                        }
                        break;
                    case ChangeType.Clear:
                        InvokeScript("js_ClearAll");
                        break;
                    default:
                        break;
                }
            };
            category.Load();

            //if (config.Categorys.Count == 0) {
            //    config.Categorys.Add("new");
            //}
            //if (!config.Categorys.Contains("Trust")) {
            //    config.Categorys.Add("Trust");
            //}
            //category.Load(config.Categorys);

            {
                var TabList = JsonSerializer.Deserialize<Dictionary<string, List<KeyValuePair<string, SearchMode>>>>(config.TabListJson);
                //foreach (var c in config.Categorys) {
                foreach (var c in category.categorylist) {
                    //CategoryListView.Items.Add(item);
                    var item = new ListViewItem(c.Name, c.Name == "Trust" ? 1 : 0);
                    item.Name = c.Name;
                    CategoryListView.Items.Add(item);

                    if (TabList != null) {
                        var tabc = getTabControl(c.Name);
                        if (TabList.ContainsKey(c.Name)) {
                            var list = TabList[c.Name];
                            foreach (var l in list) {
                                var sobj = CreateSearchObj(l.Key, l.Value);
                                var p = CreateListViewTabPage(c.Name, tabc, sobj);
                                tabc.TabPages.Add(p);
                                //t.BringToFront();
                                //t.SelectedTab = p;
                            }
                        }
                    }
                }

            }
            //reBuild(newlistview.DataItems);
            //webBrowser1.ScriptErrorsSuppressed = true;
            //webBrowser1.ScrollBarsEnabled = true;
            webBrowser1.ScriptErrorsSuppressed = true;
            webBrowser1.IsWebBrowserContextMenuEnabled = false;
            //var p = config.htmlPath;// Path.GetFullPath(@"..\..\html\wiki_parser.html");
            webBrowser1.Navigate(config.htmlPath);
            webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
            //webBrowser1.Navigating += new WebBrowserNavigatingEventHandler(webBrowser1_Navigating);
            webBrowser1.StatusTextChanged += (sender, e) => {
                BrowserToolStripStatusLabel.Text = webBrowser1.StatusText;
            };
            ScriptErrorToolStripStatusLabel.Click+=(s,e)=>{
               
                
            };
            webBrowser1.Navigated += (s, e) => {
                webBrowser1.Document.Window.Error += (ss, se) => {
                    //se.
                    se.Handled = true;
                    
                    ScriptErrorToolStripStatusLabel.Text = "ScriptError";
                };
            };


            this.KeyDown += (sender, e) => {
                var ee = e.KeyValue;
            };

            PrevPageToolStripButton.Click += (sender, e) => {

                var l = GetSelctedTabListViewControl();
                l.Page--;

                var elist = getCurrentPageDatas(l, config.ShowNum);
                InvokeScript("js_ClearAll");
                reBuild(elist);

                if ((l.Page - 1) * config.ShowNum <= 0) {
                    PrevPageToolStripButton.Enabled = false;
                }
                NextPageToolStripButton.Enabled = true;
            };
            NextPageToolStripButton.Click += (sender, e) => {

                var l = GetSelctedTabListViewControl();
                l.Page++;

                var elist = getCurrentPageDatas(l, config.ShowNum);
                InvokeScript("js_ClearAll");
                reBuild(elist);

                if ((l.Page + 1) * config.ShowNum >= l.DataItems.Count) {
                    NextPageToolStripButton.Enabled = false;
                }
                PrevPageToolStripButton.Enabled = true;
            };
            //initPage();

            ReloadToolStripButton.Click += (sender, e) => {
                //InvokeScript("jsview.jsmsg", new string[] { "jsview.jsmsg test" });
                //manager.Insert(new Data { ID = manager.GetNewID(), Text = "after", CreationTime = new DateTime(DateTime.Now.Ticks * 2) });
                webBrowser1.Navigate(config.htmlPath);
                webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
            };


            OptionToolStripButton.Click += (sender, e) => {
                Option();
            };

            HorizontalToolStripButton.Click += (sender, e) => {
                ListViewSplitContainer.Orientation = Orientation.Horizontal;
            };

            VerticalToolStripButton.Click += (sender, e) => {
                ListViewSplitContainer.Orientation = Orientation.Vertical;
            };

            CategoryContextMenuStrip.Opened += (s, e) => {
                
                if (CategoryListView.SelectedItems.Count > 0) {
                    //CategoryContextMenuStrip.Enabled = true;
                    var item = CategoryListView.SelectedItems[0];
                    //CategoryNewFileToolStripMenuItem.Enabled = item.Name != Category.Trust;
                    CategoryDeleteToolStripMenuItem.Enabled = item.Name != Category.Trust;
                    CategoryEmptyToolStripMenuItem.Enabled = item.Name == Category.Trust;
                } else {
                    //CategoryContextMenuStrip.Enabled = false;
                    CategoryDeleteToolStripMenuItem.Enabled = false;
                    CategoryEmptyToolStripMenuItem.Enabled = false;
                }
                //CategoryNewFileToolStripMenuItem.Enabled = true;
            };

            CategoryNewFileToolStripMenuItem.Click += (sender, e) => {
                CreateNewFile("new");
            };
            CategoryDeleteToolStripMenuItem.Click += (sender, e) => {
                if (CategoryListView.SelectedItems.Count > 0) {
                    var name = CategoryListView.SelectedItems[0].Name;
                    DeleteFile(name);
                }
            };
            CategoryEmptyToolStripMenuItem.Click += (sender, e) => {
                this.ClearItem();
            };


            CategoryListView.Items[0].Selected = true;
            CategoryListView.ItemSelectionChanged += (s, e) => {
                if (e.IsSelected) {
                    var item = e.Item;
                    categoryname = item.Text;
                    var tabc = getTabControl(item.Text);
                    tabc.BringToFront();

                    if (tabc.SelectedTab != null) {
                        var listview = GetTabListViewControl(tabc.SelectedTab);
                        var datas = getCurrentPageDatas(listview, config.ShowNum);
                        InvokeScript("js_ClearAll");
                        reBuild(datas);

                        if (datas.Count > 0 && listview.SelectedIndices.Count == 0) {
                            listview.Items[0].Selected = true;
                        }
                        else if (datas.Count > 0) {
                            var index  = listview.SelectedIndices[0];
                            this.EditItem(listview.DataItems[index].ID, false);
                        }

                        initPage();
                    }
                }
            };

            CategoryListView.KeyDown += (s, e) => {
                if (_CategoryLsitViewKeyMap.ContainsKey(e.KeyData)) {
                    _CategoryLsitViewKeyMap[e.KeyData](this);
                    e.Handled = true;
                    e.SuppressKeyPress = true;
                }
            };

            CategoryListView.ListViewItemSorter = new ListViewIndexComparer();
            CategoryListView.AllowDrop = true;

            CategoryListView.MouseMove += (s, e) => {
                if (e.Button == MouseButtons.Left) {
                    if (CategoryListView.SelectedItems.Count > 0) {
                        var item = CategoryListView.SelectedItems[0];
                        CategoryListView.DoDragDrop(item.Name, DragDropEffects.Move);
                    }
                }
            };

            CategoryListView.DragEnter += (s, e) => {
                if (e.Data.GetDataPresent(typeof(Data)) || e.Data.GetDataPresent(typeof(string)))
                    e.Effect = DragDropEffects.Move;
                else
                    e.Effect = DragDropEffects.None;

            };
            CategoryListView.DragOver += (s, e) => {
                var point = CategoryListView.PointToClient(new Point(e.X, e.Y));
                var item = CategoryListView.GetItemAt(point.X, point.Y);
                if (e.Data.GetDataPresent(typeof(Data)) && (item != null && item.Name != getSelectedCategory()))
                    e.Effect = DragDropEffects.Move;
                else if(e.Data.GetDataPresent(typeof(string)) && (item.Name != getSelectedCategory()))
                    e.Effect = DragDropEffects.Move;
                else
                    e.Effect = DragDropEffects.None;

            };
            CategoryListView.DragDrop += (s, e) => {
                if (e.Data.GetDataPresent(typeof(Data))) {
                    var data = (Data)e.Data.GetData(typeof(Data));
                    var point = CategoryListView.PointToClient(new Point(e.X, e.Y));
                    var item = CategoryListView.GetItemAt(point.X, point.Y);
                    if (item != null && item.Name != getSelectedCategory()) {
                        DeleteItem(data.ID);
                        //category.getManger(item.Name).Insert(data);
                    }
                }
                else if (e.Data.GetDataPresent(typeof(string))) {
                    var name = (string)e.Data.GetData(typeof(string));
                    var point = CategoryListView.PointToClient(new Point(e.X, e.Y));
                    var item = CategoryListView.GetItemAt(point.X, point.Y);

                    var index = CategoryListView.Items.IndexOfKey(name);
                    if (index >= 0) {
                        var key = item.Name;
                        var removeitem = CategoryListView.Items[index];

                        var i = CategoryListView.Items.IndexOfKey(item.Name);
                        if (i == 0 || (i < CategoryListView.Items.Count - 1)) {
                            CategoryListView.Items.Remove(removeitem);
                            var ni = CategoryListView.Items.IndexOfKey(item.Name);
                            var ins = i == 0 ? 0 : ni + 1;
                            CategoryListView.Items.Insert(ins, removeitem);
                            //config.Categorys.Remove(name);
                            //config.Categorys.Insert(ins, name);
                            //CategoryListView.Sort();
                        }

                        removeitem.Selected = true;
                    }
                }
            };

            CategoryListView.AfterLabelEdit += (s, e) => {
                //e.CancelEdit = true;
                if (File.Exists(Path.Combine(config.DataDirPath, e.Label + ".xml"))) {
                    e.CancelEdit = true;
                }
                else {
                    var item = CategoryListView.Items[e.Item];
                    if (category.RenameFile(item.Name, e.Label)) {
                        item.Text = e.Label;
                        item.Name = e.Label;
                    }
                }
            };


            TabListViewEditItemToolStripMenuItem.Click += (s, e) => {
                var lv = GetSelctedTabListViewControl();
                if (lv.SelectedIndices.Count > 0) {
                    var index = lv.SelectedIndices[0];
                    EditItem(lv.DataItems[index].ID, true);
                }
            };
            TabListViewNewItemToolStripMenuItem.Click += (s, e) => {
                this.CreateItem();
            };
            TabListViewDeleteItemToolStripMenuItem.Click += (s, e) => {
                this.DeleteItem();
            };
            TabListViewEditDateTimeToolStripMenuItem.Click += (s, e) => {
                this.EditDateTime();
            };
        }

        TabControl getTabControl(string categoryname) {
            if (TabPanel.Controls[categoryname] == null){
            //if(category.getCategoryID(categoryname)<0){
            //if (!Tabs.ContainsKey(categoryname)) {
                var t = new TabControl();
                t.Name = categoryname;
                t.Appearance = TabAppearance.FlatButtons;
                t.Multiline = true;
                t.ItemSize = new Size(70, 20);
                t.SizeMode = TabSizeMode.Fixed;
                t.TabPages.Add(CreateListViewTabPage(categoryname, t, new SearchAll()));
                t.SelectedIndexChanged += (s, e) => {
                    var listview = GetTabListViewControl(t.SelectedTab);
                    var datas = getCurrentPageDatas(listview, config.ShowNum);
                    InvokeScript("js_ClearAll");
                    reBuild(datas);
                };
                t.Dock = DockStyle.Fill;
                TabPanel.Controls.Add(t);
                //Tabs.Add(categoryname, t);
            }
            return TabPanel.Controls[categoryname] as TabControl;
            //var tab = Tabs[categoryname];           
            //return tab;
        }

        void refreshTab() {
            
        }

        private void saveItem() {
            
        }

        private void initPage() {
            int max = config.ShowNum;
            var l = GetSelctedTabListViewControl();

            PrevPageToolStripButton.Enabled = !((l.Page - 1) * max <= 0);
            NextPageToolStripButton.Enabled = !((l.Page + 1) * max >= l.DataItems.Count);
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
            //var listview = GetTabListViewControl(AllPage);
            var listview = GetSelctedTabListViewControl();
            //var listview = GetTabListViewControl(AllPage);
            var items = listview.DataItems;
            if (items.Count > 0) {
                listview.Items[0].Selected = true;
                //reBuild(items[0]);
                //ToggleShow(config.ShowType);
            }
        }

        internal void Option() {
            var cf = new ConfigForm(config);
            cf.StartPosition = FormStartPosition.CenterParent;
            var res = cf.ShowDialog(this);
            if (res == DialogResult.OK) {
                if (cf.editorconfig.IsFontChanged) {
                    Editor.Font = config.EditorFont;
                }
                if (cf.editorconfig.IsColorChanged) {
                    Editor.ForeColor = config.EditorFontColor;
                    Editor.BackColor = config.EditorBackColor;
                }
                if (cf.editorconfig.IsViewChanged) {
                    Editor.DrawsTab = config.ShowTab;
                    Editor.DrawsSpace = config.ShowSpace;
                    Editor.DrawsFullWidthSpace = config.ShowZenSpace;
                    Editor.DrawsEolCode = config.ShowEol;
                }
                if (cf.mainconfig.IsPortChanged) {
                    if (serveBW.IsBusy) {
                        
                    }
                }
            }
            cf.Close();
        }

        internal void CreateNewFile(string name) {
            if (CategoryListView.Items.IndexOfKey(name) >= 0) {
                int index=2;
                while(true){
                    var newname = name + index.ToString();
                    if (CategoryListView.Items.IndexOfKey(newname) < 0) {
                        break;
                    }
                    index++;
                }
                name = name + index.ToString();
            }

            var item = new ListViewItem(name, 0);
            item.Name = name;
            CategoryListView.Items.Insert(0, item);

            category.CreateFile(name);

            item.Selected = true;
        }

        internal void DeleteFile(string name) {
            var index = CategoryListView.Items.IndexOfKey(name);
            if (index >= 0) {

                try {
                    category.DeleteFile(name);
                } catch (Exception) {
                    MessageBox.Show("DeleteFile ERROR");
                    return;
                }
                CategoryListView.Items.RemoveAt(index);
                CategoryListView.Items[0].Selected = true;
            }
            //config.Categorys.Remove(name);
        }

        internal void CreateItem() {
            if (category.getCategoryID(getSelectedCategory()) >= 0) {
                category.Create(config.ItemTemplete, DateTime.Now, getSelectedCategory());
            }
        }
        internal void DeleteItem(long id) {
            category.Delete(id);
        }
        internal void DeleteItem() {
            var lv = GetSelctedTabListViewControl();
            if (lv.SelectedIndices.Count > 0) {
                for (int i = 0; i < lv.SelectedIndices.Count; i++) {
                    var index = lv.SelectedIndices[i];
                    this.DeleteItem(lv.DataItems[index].ID);
                }
            }
        }
        internal void ClearItem() {
            var cate = getSelectedCategory();
            if (cate == Category.Trust) {
                TabPage alltabpage = null;
                var list = new List<TabPage>();
                var tabc = getTabControl(cate);
                for (int i = 0; i < tabc.TabPages.Count; i++) {
                    if (GetTabListViewControl(tabc.TabPages[i]).search.Mode == SearchMode.All) {
                        alltabpage = tabc.TabPages[i];
                    } else {
                        list.Add(tabc.TabPages[i]);
                    }
                }
                tabc.SelectedTab = alltabpage;
                foreach (var tabpage in list) {
                    tabc.TabPages.Remove(tabpage);
                }

                GetTabListViewControl(alltabpage).ClearItem(); ;
                category.Clear(cate);
            }   
        }

        internal void EditItem(long id, bool isfocus) {
            EditItem(id, isfocus, true);
        }

        internal void EditItem(long id, bool isfocus, bool isopen) {
            if (EditorPinToolStripButton.Checked) return;

            var item = category.GetItem(id);
            if (item == null) return;

            if (isopen) {
                this.OpenEditor();
            }
            EditorInfoToolStripLabel.Text = "ID = " + id.ToString();
            category.EditingID = id;
            dirty = false;
            _editor.Text = item.Text;
            _editor.ClearHistory();
            initEditorToolStripButton();
            if (isfocus) {
                _editor.Focus();
            }
        }

        private List<Data> getCurrentPageDatas(ListViewEx listview, int max) {
            var list = new List<Data>();
            if (listview.DataItems.Count <= max) {
                list = listview.DataItems;
            }
            else {
                var p = listview.DataItems.Count / max;
                p = listview.DataItems.Count % max == 0 ? p : p + 1;
                var si = (p - listview.Page) * max;
                var cnt = listview.DataItems.Count;
                var last = (si + max) > cnt ? cnt : (si + max);
                
                for (int i = si; i < last; i++) {
                    list.Add(listview.DataItems[i]);
                }
            }
            return list;
        }

        private void Moves(long id) {
            var view = GetSelctedTabListViewControl();
            var index = view.DataItems.FindIndex((n) => {
                return (n.ID == id);
            });
            if (index == -1) {
                GetCurrentTabControl().SelectedIndex = 0;
                view = GetSelctedTabListViewControl();
                index = view.DataItems.FindIndex((n) => {
                    return (n.ID == id);
                });
            }
            var max = config.ShowNum;
            //if (index != -1) {
            index++;
            bool res = false;
            int cnt = 1;
            while (true) {
                var t = view.DataItems.Take(max * cnt);
                res = t.Any(n => {
                    return n.ID == id;
                });
                if (res) {
                    break; 
                }
                if (t.Count() < max) {
                    break;
                }
                cnt++;
            }
            cnt--;

            var p = view.DataItems.Count / max;
            p = view.DataItems.Count % max == 0 ? p : p + 1;
            var page = p-cnt;// -view.Page;
            if (view.Page != page) {
                view.Page = page;
                var list = getCurrentPageDatas(view, config.ShowNum);
                reBuild(list);

                initPage();
            }
            var url = config.htmlPath + "#" + id;
            webBrowser1.Navigate(url);
        }

        private ListViewEx GetTabListViewControl(TabPage page) {
            return page.Controls[0] as ListViewEx; 
        }

        private ListViewEx GetSelctedTabListViewControl() {
            return GetTabListViewControl(getTabControl(getSelectedCategory()).SelectedTab);
        }

        private TabControl GetCurrentTabControl() {
            return getTabControl(getSelectedCategory());
        }

        private bool dirty = false;
        private TabPage CreateListViewTabPage(string categoryname, TabControl tabcontrol, Search search) {
            for (int i = 0; i < tabcontrol.TabPages.Count; i++) {
                var tabpage = tabcontrol.TabPages[i];
                var lw = GetTabListViewControl(tabpage);
                if (lw.search == search) {
                    lw.DataItems = category.Filter(getSelectedCategory(), search.getSearch());
                    return tabpage;
                }
            }

            var listview = new ListViewEx(category.Filter(categoryname, search.getSearch()));
            var page = listview.DataItems.Count / config.ShowNum;
            listview.Page = listview.DataItems.Count % config.ShowNum == 0 ? page : page + 1;

            if (categoryname == Category.Trust) {
                listview.MultiSelect = true;
            }
            listview.search = search;
            listview.ContextMenuStrip = TabListViewContextMenuStrip;

            listview.MouseUp += (sender, e) => {
                var item = listview.GetItemAt(e.X, e.Y);
                if (item == null && listview.FocusedItem !=null) {
                    listview.FocusedItem.Selected = true;
                }
            };

            listview.ItemSelectionChanged += (sender, e) => {
                if (e.IsSelected) {
                    var d = listview.DataItems[e.ItemIndex];
                    this.EditItem(d.ID, false, false);
                }
            };

            listview.DoubleClick += (sender, e) => {
                if (listview.SelectedIndices.Count == 1) {
                    var selindex = listview.SelectedIndices[0];
                    var id = listview.DataItems[selindex].ID;
                    this.Moves(id);
                }
            };

            listview.ItemDrag += (s, e) => {
                if (listview.SelectedIndices.Count > 0) {
                    var index = listview.SelectedIndices[0];
                    var data = listview.DataItems[index];
                    listview.DoDragDrop(data, DragDropEffects.Move);
                }
            };

            listview.Dock = DockStyle.Fill;

            var t = new TabPage(search.Pattern);
            t.Controls.Add(listview);
            return t;
        }

        private string getSelectedCategory() {
            return this.categoryname;
        }

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

        internal Migemo getMigemo() {
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
            List<string> wo = new List<string>() { "test", "FireBug" };
            var words = JsonSerializer.Serialize(wo);
            var json = JsonSerializer.Serialize(item);
            InvokeScript("js_BuildByID", json, words);
        }

        private void reBuild(long insertBefore,  Data item) {
            var json = JsonSerializer.Serialize(item);
            InvokeScript("js_BuildInsertByID", insertBefore.ToString(), json);
        }

        private void webBrowser1_ContextMenuStripChanged(object sender, EventArgs e) {

        }

        private void timeToolStripMenuItem_Click(object sender, EventArgs e) {

        }

        private void toolStripMenuItem1_Click(object sender, EventArgs e) {
            var ae = webBrowser1.Document.ActiveElement;
            var h = ae.GetAttribute("href");
        
        }
    }  
}
