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

        //private Dictionary<string, TabControl> Tabs = new Dictionary<string,TabControl>();
        //private Dictionary<int, TabControl> Tabs = new Dictionary<int, TabControl>();
        private Migemo migemo;

        private HttpServer httpServer;
        private BackgroundWorker serveBW;

        internal Config config;
        
        ScriptManager sm = new ScriptManager();

        Regex regShow = new Regex(@"\/item\/(\d+)$", RegexOptions.Compiled);
        Regex regEdit = new Regex(@"\/item\/(\d+)\/(edit)$", RegexOptions.Compiled);
        Regex regExe = new Regex(@"\/item\/(exe)$", RegexOptions.Compiled);
        Regex regMove = new Regex(@"\/item\/(\d+)\/(move)$", RegexOptions.Compiled);
        Regex regGoto = new Regex(@"\/item\/(goto)$", RegexOptions.Compiled);
        Regex regComeFrom = new Regex(@"\/item\/(\d+)\/(comefrom)$", RegexOptions.Compiled);

        Regex regNew = new Regex(@"\/item\/(new)$", RegexOptions.Compiled);
        Regex regScript = new Regex(@"\/item\/(script)$", RegexOptions.Compiled);

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

            Editor.Font = config.EditorFont;
            Editor.ForeColor = config.EditorFontColor;
            Editor.BackColor = config.EditorBackColor;
            Editor.DrawsTab = config.ShowTab;
            Editor.DrawsSpace = config.ShowSpace;
            Editor.DrawsFullWidthSpace = config.ShowZenSpace;
            Editor.DrawsEolCode = config.ShowEol;
            EditorWrapToolStripButton.Checked = config.EdiorWrap;
            CloseEditor();

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
                    }
                    else {
                        var id = long.Parse(idstr);
                        idlist.Add(id);
                    }
                    //var manager = category.getManger(getSelectedCategory());
                    //var items = manager.GetItem(idlist);
                    var items = category.GetItem(idlist);
                    var res = string.Empty;
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

                m = regScript.Match(url);
                if (m.Success) {
                    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();
                    reqparam["method"] = "script";
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

                if (serveBW.CancellationPending) {
                    httpServer.stop();
                    return;
                }
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
                    case "script":
                        var script = param["data"];
                        sm.Eval(script);
                        break;
                    case "move": {
                            var id = long.Parse(param["id"]);
                            this.Moves(id);
                        }
                        break;
                    case "goto": {
                            var word = param["data"];
                            var s = CreateSearchObj(word, SearchMode.Normal);
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
                //foreach (var key in Tabs.Keys) {
                //    var list = new List<KeyValuePair<string, SearchMode>>();
                //    var tabs = Tabs[key].TabPages;
                //    for (int i = 0; i < tabs.Count; i++) {
                //        var listview = GetTabListViewControl(tabs[i]);
                //        var s = listview.search;
                //        if (s.Mode != SearchMode.All) {
                //            var mode = s.Mode;
                //            var pt = s.Pattern;
                //            list.Add(new KeyValuePair<string, SearchMode>(pt, mode));
                //        }
                //    }
                //    TabList.Add(key, list);
                //}

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
                    var manager = category.getManger(getSelectedCategory());
                    var item = manager.EditingData;
                    if (item != null) {
                        item.Text = _editor.Text;
                        manager.UpDate(item);
                    }
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
                    case ChangeType.Insert: {
                            //if (Tabs[e.Name] == GetCurrentTabControl()) {
                            if (TabPanel.Controls[e.Name] == GetCurrentTabControl()) {
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
                                this.EditItem(e.Item.ID);
                            }
                            else {
                                //var tabc = Tabs[e.Name];

                                //var listview = GetTabListViewControl(Tabs[e.Name].TabPages[0]);
                                var listview = GetTabListViewControl(((TabControl)TabPanel.Controls[e.Name]).TabPages[0]);
                                listview.AddItem(e.Item);
                            }
                        }
                        break;
                    case ChangeType.UpDate:
                        //if (Tabs[e.Name] == GetCurrentTabControl()) {
                        if (TabPanel.Controls[e.Name] == GetCurrentTabControl()) {
                            editContent(e.Item);
                            var cf = InvokeScript("js_getComeFrom");
                            var list = JsonSerializer.Deserialize<List<string>>(cf.ToString());
                            config.ComeFormWords.Union(list);
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

                       
                        for (int i = 0; i < CategoryListView.Items.Count; i++) {
                            var item = CategoryListView.Items[i];
                            var tabpages = ((TabControl)TabPanel.Controls[item.Name]).TabPages;
                            for (int j = 0; j < tabpages.Count; j++) {
                                var listview = GetTabListViewControl(tabpages[j]);
                                listview.DeleteItem(e.Item);
                            }
                        }
                        if (e.Name == GetCurrentTabControl().Name) {
                            InvokeScript("js_Remove", e.Item.ID.ToString());
                        }

                        break;
                    case ChangeType.Clear:
                        InvokeScript("js_ClearAll");
                        break;
                    default:
                        break;
                }
            };

            if (config.Categorys.Count == 0) {
                config.Categorys.Add("new");
            }
            if (!config.Categorys.Contains("Trust")) {
                config.Categorys.Add("Trust");
            }
            category.Load(config.Categorys);

            {
                var TabList = JsonSerializer.Deserialize<Dictionary<string, List<KeyValuePair<string, SearchMode>>>>(config.TabListJson);
                foreach (var c in config.Categorys) {
                    //CategoryListView.Items.Add(item);
                    var item = new ListViewItem(c, c == "Trust" ? 1 : 0);
                    item.Name = c;
                    CategoryListView.Items.Add(item);

                    if (TabList != null) {
                        var tabc = getTabControl(c);
                        if (TabList.ContainsKey(c)) {
                            var list = TabList[c];
                            foreach (var l in list) {
                                var sobj = CreateSearchObj(l.Key, l.Value);
                                var p = CreateListViewTabPage(c, tabc, sobj);
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

            if (config.ShowType == ShowType.List) {
                ShowLargeToolStripMenuItem.Checked = false;
                ShowListToolStripMenuItem.Checked = true;
                ToggleShowToolStripSplitButton.Image = global::wiki.Properties.Resources.win_show_detail;
                ToggleShowToolStripSplitButton.Text = "List";
            }
            else if (config.ShowType == ShowType.Large) {
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
                        category.getManger(item.Name).Insert(data);
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
                            config.Categorys.Remove(name);
                            config.Categorys.Insert(ins, name);
                            //CategoryListView.Sort();
                        }

                        removeitem.Selected = true;
                    }
                }
            };

            CategoryListView.BeforeLabelEdit += (s, e) => {

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
                    EditItem(lv.DataItems[index].ID);
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

        private void saveItem() {
            
        }

        private void initPage() {
            if (config.ShowType == ShowType.Large) {
                PrevPageToolStripButton.Enabled = false;
                NextPageToolStripButton.Enabled = false;
            } else if (config.ShowType == ShowType.List) {
                int max = config.ShowNum;
                var l = GetSelctedTabListViewControl();
                if ((l.Page - 1) * max <= 0) {
                    PrevPageToolStripButton.Enabled = false;
                }
                if ((l.Page + 1) * max >= l.DataItems.Count) {
                    NextPageToolStripButton.Enabled = false;
                }
            }
        }

        void ToggleShow(ShowType showtype) {
            if (showtype == ShowType.List) {
                //config.ShowType = ShowType.List;
                initPage();
                var listview = GetSelctedTabListViewControl();
                List<Data> list = new List<Data>();
                var last = listview.DataItems.Count < config.ShowNum ? listview.DataItems.Count : config.ShowNum;
                for (int i = 0; i < last; i++) {
                    list.Add(listview.DataItems[i]);
                }
                reBuild(list);

            } else {
                //config.ShowType = ShowType.Large;
                var listview = GetSelctedTabListViewControl();
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
            //var listview = GetTabListViewControl(AllPage);
            var listview = GetSelctedTabListViewControl();
            //var listview = GetTabListViewControl(AllPage);
            var items = listview.DataItems;
            if (items.Count > 0) {
                listview.Items[0].Selected = true;
                //reBuild(items[0]);
                ToggleShow(config.ShowType);
            }

            //webBrowser1.Document.Window.Error += new HtmlElementErrorEventHandler(Window_Error);

            //webBrowser1.Document.Window.Error += (ss, se) => {
            //    //se.
            //    se.Handled = true;
            //};
            //this.webBrowser1.Document.MouseDown += (ss, se) => {
            //    var ee = e;
            //};
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

            config.Categorys.Clear();
            for (int i = 0; i < CategoryListView.Items.Count; i++) {
                config.Categorys.Add(CategoryListView.Items[i].Name);
            }

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
            config.Categorys.Remove(name);
        }

        internal void CreateItem() {
            var manager = category.getManger(getSelectedCategory());
            var id = category.GetNewID();
            //manager.Insert(new Data { ID=manager.GetNewID(), Text = "!new", CreationTime = DateTime.Now });
            manager.Insert(new Data { ID = id, Text = "!new", CreationTime = DateTime.Now });
        }
        internal void DeleteItem(long id) {
            //var manager = category.getManger(getSelectedCategory());
            //manager.Remove(id);
            category.DeleteItem(getSelectedCategory(), id);
        }
        internal void DeleteItem() {
            var lv = GetSelctedTabListViewControl();
            if (lv.SelectedIndices.Count > 0) {
                for (int i = 0; i < lv.SelectedIndices.Count; i++) {
                    var index = lv.SelectedIndices[i];
                    this.DeleteItem(lv.DataItems[index].ID);
                }
                //var index = lv.SelectedIndices[0];
                //this.DeleteItem(lv.DataItems[index].ID);
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
                //var cnt = lv.DataItems.Count;
                //for (int i = 0; i < cnt; i++) {
                //    this.DeleteItem(lv.DataItems[i].ID);
                //}
                category.ClearItem(cate);
            }   
        }
        internal void EditItem(long id) {
            var manager = category.getManger(getSelectedCategory());
            var item = manager.GetItem(id);
            if (item == null) return;

            this.OpenEditor();
            EditorInfoToolStripLabel.Text = "Editing id = " + id.ToString();

            manager.EditingData = item;
            dirty = false;
            _editor.Text = item.Text;
            _editor.Focus();
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
                var manager = category.getManger(getSelectedCategory());
                var item = manager.GetItem(id);
                reBuild(item);
            } else if (config.ShowType == ShowType.List) {
            
                //var view = GetSelctedTabControl();
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

        private ListViewEx GetTabListViewControl(TabPage page) {
            return page.Controls[0] as ListViewEx; 
        }

        private ListViewEx GetSelctedTabListViewControl() {
            return GetTabListViewControl(getTabControl(getSelectedCategory()).SelectedTab);
        }

        private TabControl GetCurrentTabControl() {
            return getTabControl(getSelectedCategory());
        }

        //private Dictionary<Search, TabPage> searchtabdic = new Dictionary<Search, TabPage>();
        //private Dictionary<TabControl, Dictionary<Search, TabPage>> searchtabdic = new Dictionary<TabControl, Dictionary<Search, TabPage>>();
        private bool dirty = false;
        private TabPage CreateListViewTabPage(string categoryname, TabControl tabcontrol, Search search) {
            //if (searchtabdic.ContainsKey(tabcontrol)) {
            //    var dic = searchtabdic[tabcontrol];
            //    foreach (var item in dic.Keys) {
            //        if (item.Equals(search)) {
            //            var p = dic[search];
            //            var lv = GetTabListViewControl(p);
            //            var manager = category.getManger(getSelectedCategory());
            //            lv.DataItems = manager.Filter(search.getSearch());
            //            return p;
            //        }
            //    }
            //} else {
            //    searchtabdic.Add(tabcontrol, new Dictionary<Search, TabPage>());
            //}
            for (int i = 0; i < tabcontrol.TabPages.Count; i++) {
                var tabpage = tabcontrol.TabPages[i];
                var lw = GetTabListViewControl(tabpage);
                if (lw.search == search) {
                    var manager = category.getManger(getSelectedCategory());
                    lw.DataItems = manager.Filter(search.getSearch());
                    return tabpage;
                }
            }

            var items = category.getManger(categoryname).Filter(search.getSearch());
            var listview = new ListViewEx(items);

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

            //listview.SelectedIndexChanged += (sender, e) => {
            //    if (config.ShowType == ShowType.Large && listview.SelectedIndices.Count == 1) {
            //        var selindex = listview.SelectedIndices[0];
            //        var item = listview.DataItems[selindex];
            //        //var cf = InvokeScript("js_getComeFrom");
            //        //var dic = JsonSerializer.Deserialize<Dictionary<string, string>>(cf.ToString());
            //        //Console.WriteLine("cf = " + cf);
            //        reBuild(item);
            //    }
            //};
            listview.DoubleClick += (sender, e) => {
                if (config.ShowType == ShowType.List && listview.SelectedIndices.Count == 1) {
                    var selindex = listview.SelectedIndices[0];
                    var id = listview.DataItems[selindex].ID;
                    this.Moves(id);
                }
            };

            listview.MouseMove += (s, e) => {
                if (e.Button == MouseButtons.Left) {
                    if(listview.SelectedIndices.Count>0){
                        var index = listview.SelectedIndices[0];
                        var data = listview.DataItems[index];
                        listview.DoDragDrop(data, DragDropEffects.Move);
                    }
                }
            };

            listview.Dock = DockStyle.Fill;

            var t = new TabPage(search.Pattern);
            t.Controls.Add(listview);
            //ItemTabControl.TabPages.Add(t);

            //searchtabdic[tabcontrol].Add(search, t);

            return t;
        }

        private string getSelectedCategory() {
            //return CategoryListView.SelectedItems[0].Text;
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

        private void timeToolStripMenuItem_Click(object sender, EventArgs e) {

        }

        private void toolStripMenuItem1_Click(object sender, EventArgs e) {
            var ae = webBrowser1.Document.ActiveElement;
            var h = ae.GetAttribute("href");
          
            
        }

    }

    
}
