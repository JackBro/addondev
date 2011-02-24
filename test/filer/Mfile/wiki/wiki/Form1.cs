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

namespace wiki
{


    public partial class Form1 : Form
    {
        private ItemManager manager;
        //private List<Data> CurrentList;

        private long baseticks;

        private HttpServer httpServer;
        private BackgroundWorker serveBW;
        
        JintEngine en = new JintEngine();

        Regex regShow = new Regex(@"\/item\/(\d+)$", RegexOptions.Compiled);
        Regex regEdit = new Regex(@"\/item\/(\d+)\/(edit)$", RegexOptions.Compiled);
        Regex regExe = new Regex(@"\/item\/(exe)$", RegexOptions.Compiled);


        private Dictionary<string, string> reqparam = new Dictionary<string, string>();
       
        public Form1(){
            InitializeComponent();

            //ItemTabControl.SelectedIndex = 0;
            //ActiveListView = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
            //int ii = 0;
            //CurrentList = ActiveListView.DataItems;

            //var b = regshow.IsMatch("/item/55");
            //var mm = regEdit.Match("/item/55/edit");
            //var dd = m.Groups[1].Value;

            //var b2 = regshow.IsMatch("/item/");

            reqparam.Add("methd", string.Empty);
            reqparam.Add("url", string.Empty);

            httpServer = new HttpServer();
            httpServer.RequestEvent += (sender, e) => {

                //var url = e.Request.RawUrl.Split(new string[]{"/"}, StringSplitOptions.RemoveEmptyEntries);
                var url = e.Request.RawUrl;
                var methd = e.Request.HttpMethod;
                e.Response = "OK";

                var m = regShow.Match(url);
                if (m.Success) {
                    var id = long.Parse(m.Groups[1].Value);
                    var item = manager.GetItem(id);
                    var res =string.Empty;
                    switch (methd) {
                        case "DELETE":
                            break;
                        case "GET":
                            List<Data> list = new List<Data>() { item };
                            res = JsonSerializer.Serialize(list);
                            break;
                        default:
                            break;
                    }
                    e.Response = res;
                    return;
                }

                m = regExe.Match(url);
                if (m.Success) {
                    //e.Request.InputStream
                    var _RequestBody = new StreamReader(e.Request.InputStream).ReadToEnd();

                    int k=0;
                }

                m = regEdit.Match(url);
                if (m.Success) {
                    var id = long.Parse(m.Groups[1].Value);

                    //var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                    //List<Data> elist = new List<Data>();
                    //var last = listview.DataItems.Count < 5 ? listview.DataItems.Count : 5;
                    //for (int i = 0; i < last; i++) {
                    //    elist.Add(listview.DataItems[i]);
                    //}
                    //List<Data> list = CurrentList;
                    //ttt(ref list);

                    //var list = Invoke(new SetFocusDelegate(ttt)) as List<Data>;
                    //Invoke(new SetFocusDelegate(ttt));
                    reqparam["methd"] = methd;
                    reqparam["url"] = url;
                    serveBW.ReportProgress(1, reqparam);
                    //reBuild(elist);
                    e.Response = "OK";
                    //e.Response = JsonSerializer.Serialize(list);
                    int k = 0;
                }

            };
            serveBW = new BackgroundWorker();
            serveBW.WorkerReportsProgress = true;
            serveBW.ProgressChanged += (sender, e) => {
                //var list = e.UserState as List<Data>;
                //var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                //var last = listview.DataItems.Count < 5 ? listview.DataItems.Count : 5;
                //for (int i = 0; i < last; i++) {
                //    list.Add(listview.DataItems[i]);
                //}
                var param = e.UserState as Dictionary<string, string>;
                var url = param["url"];

                //this.EditItem(id);

            };
            //serveBW.RunWorkerCompleted += (sender, e) => {
            //    int ee = 0;
            //};
            serveBW.DoWork += (sender, e) => {
                httpServer.start();
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
            };

            //this.SizeChanged += (sender, e) => {
            //    for (int i = 0; i < ItemTabControl.TabPages.Count; i++) {
            //        var page = ItemTabControl.TabPages[i];
            //        var listview = GetTabControl(page);
            //        listview.Columns["title"].Width = -2;
            //    }
            //};

            textBox1.TextChanged += (sender, e) => {
                
                if (dirty) {
                    var item = manager.EditingData;
                    if (item != null) {
                        item.Text = textBox1.Text;
                        manager.UpDate(item);
                    }
                }
                dirty = true;
            };
            textBox1.LostFocus += (sender, e) => {
                
            };

            ItemTabControl.Selecting += (sender, e) => {
            };

            ItemTabControl.SelectedIndexChanged += (sender, e) => {
                var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                //webBrowser1.Document.InvokeScript("testClearAll");
                InvokeScript("js_ClearAll");
                reBuild(listview.DataItems);
            };

            en = new JintEngine();
            en.DisableSecurity();
            en.AllowClr = true;
            ren();
            //en.Run("function test(){ return 10; }");
            var r = en.Run(@"x=eval( [ ""hoge"":""hogehoge"", ""foo"":""j.png"" ]); return x;");
            //var r2 = en.Run("return 100;");
            //var r3 = en.Run("x=test()*100; return x;");
            //en.SetFunction("testf", new Jint.Native.JsFunction());
            en.SetFunction("square", new Action(() => { CreateItem(); }));
            en.SetFunction("deleteitem", new Action<object>((id) => {
                Console.WriteLine("deleteitem id = " + id.ToString());
                this.DeleteItem(long.Parse(id.ToString()));
            }));
            en.SetFunction("edititem", new Action<object>((id) => {
                Console.WriteLine("edititem id = " + id.ToString());
                this.EditItem(long.Parse(id.ToString()));
            }));

            NewItemToolStripButton.Click += (sender, e) => {
//                var en = new JintEngine();
//                en.DisableSecurity();
//                en.AllowClr = true;
//                en.SetFunction("square", new Action<string>(a => { MessageBox.Show(a); }));
//                object result = en.Run(@"
//square('test');
//System.Diagnostics.Process.Start('notepad.exe');
//return 21 * 2");
//                Console.WriteLine(result); // Displays 42

                object result = en.Run(@"
                square();
                return 21 * 2");               
//                CreateItem();
            };

            manager = new ItemManager();
            manager.DataPath = "data.bin";
            manager.Load();
            manager.eventHandler += (sender, e) => {
                switch (e.type) {
                    case ChangeType.Insert:
                        {
                            ItemTabControl.SelectedIndex = 0;
                            var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                            var iindex = listview.AddItem(e.Item);
                            if (iindex == (listview.DataItems.Count-1)) {
                                reBuild(e.Item);
                            } else {
                                reBuild(listview.DataItems[iindex+1].ID, e.Item);
                            }
                            //listview.AddItem(e.Index, e.Item);
                            
                        }
                        break;
                    case ChangeType.UpDate:
                        reBuild(e.Item);
                        break;
                    case ChangeType.Delete:
                        for (int i = 0; i < ItemTabControl.TabPages.Count; i++) {
                            var page = ItemTabControl.TabPages[i];
                            var listview = GetTabControl(page);
                            listview.DeleteItem(e.Item);
                        }
                        break;
                    default:
                        break;
                }
            };

            var te = manager.Filter(x => { return true; }).ToString();

            var newlistview = CreateListViewTabPage("All", manager.Filter(x => { return true; }));
            //reBuild(newlistview.DataItems);
            //webBrowser1.ScriptErrorsSuppressed = true;
            //webBrowser1.ScrollBarsEnabled = true;
            
            webBrowser1.IsWebBrowserContextMenuEnabled = false;
            var p = Path.GetFullPath(@"..\..\html\wiki_parser.html");
            webBrowser1.Navigate(p);
            webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
            //webBrowser1.Navigating += new WebBrowserNavigatingEventHandler(webBrowser1_Navigating);

            comboBox1.AutoCompleteMode = AutoCompleteMode.Suggest;
            comboBox1.AutoCompleteSource = AutoCompleteSource.CustomSource;
            var s = new AutoCompleteStringCollection();
            comboBox1.AutoCompleteCustomSource = s;

            comboBox1.KeyDown += (sender, e) => {
                if (e.KeyCode == Keys.Return && comboBox1.Text.Length>0) {
                    CreateListViewTabPage(comboBox1.Text, manager.Filter(x => 
                    {
                        return x.Text.Contains(comboBox1.Text);; 
                    }));
                }
            };

            int max = 5;
            PreToolStripButton.Click += (sender, e) => {
                
                var l = GetSelctedTabControl();
                l.Page--;
                var si = l.Page * max;
                var cnt = l.DataItems.Count;
                var last = (si + max) > cnt ? cnt : (si + max);
                List<Data> elist = new List<Data>();
                for (int i = si; i < last; i++) {
                    elist.Add(l.DataItems[i]);
                }
                InvokeScript("js_ClearAll");
                reBuild(elist);

                if ((l.Page -1) * max <= 0) {
                    PreToolStripButton.Enabled = false;
                }
                NextToolStripButton.Enabled = true;
            };
            NextToolStripButton.Click += (sender, e) => {

                var l = GetSelctedTabControl();
                l.Page++;

                var si = l.Page * max;
                var cnt = l.DataItems.Count;
                var last = (si + max) > cnt ? cnt : (si + max);
                List<Data> elist = new List<Data>();
                for (int i = si; i < last; i++) {
                    elist.Add(l.DataItems[i]);
                }
                InvokeScript("js_ClearAll");
                reBuild(elist);

                if ((l.Page + 1) * max >= l.DataItems.Count) {
                    NextToolStripButton.Enabled = false;
                }
                PreToolStripButton.Enabled = true;
            };
        }

        void webBrowser1_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e) {
            webBrowser1.DocumentCompleted -= webBrowser1_DocumentCompleted;
            Console.WriteLine("webBrowser1_DocumentCompleted : " + e.Url);
            //webBrowser1.Document.ContextMenuShowing += new HtmlElementEventHandler(Document_ContextMenuShowing);
            //webBrowser1.Document.Click += new HtmlElementEventHandler(Document_Click);
            var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
            List<Data> elist = new List<Data>();
            var last = listview.DataItems.Count < 5 ? listview.DataItems.Count : 5;
            for (int i = 0; i < last; i++) {
                elist.Add(listview.DataItems[i]);
            }
            reBuild(elist);
            //reBuild(listview.DataItems);
        }

        private void CreateItem() {
            manager.Insert(new Data { ID=manager.GetNewID(), Text = "!new", CreationTime = new DateTime(DateTime.Now.Ticks+baseticks) });

            //webBrowser1.Document.InvokeScript("test", new String[] { "called from client code" });
        }
        private void DeleteItem(long id) {
            manager.Delete(id);
            webBrowser1.Document.InvokeScript("js_Remove", new string[] { id.ToString() });
            
        }
        private void EditItem(long id) {
            var item = manager.GetItem(id);
            if (item == null) return;
            manager.EditingData = item;
            dirty = false;
            textBox1.Text = item.Text;
        }

        private ListViewEx GetTabControl(TabPage page) {
            return page.Controls[0] as ListViewEx; 
        }

        private ListViewEx GetSelctedTabControl() {
            return GetTabControl(ItemTabControl.SelectedTab);
        }

        private bool dirty = false;
        private ListViewEx CreateListViewTabPage(string name, List<Data> items) {

            var listview = new ListViewEx(items);
            listview.ItemSelectionChanged += (sender, e) => {
                var s = e.ItemIndex;
                if (e.IsSelected) {
                    var text = items[s].Text;
                    //textBox1.Text = text;
                    if (MouseButtons == MouseButtons.Left) {
                        dirty = false;
                        //SetText(text);

                    } else if (MouseButtons == MouseButtons.Middle) {
                    
                    }
                    //reBuild(text);
                }              
            };

            listview.Dock = DockStyle.Fill;

            var t = new TabPage(name);
            t.Controls.Add(listview);
            ItemTabControl.TabPages.Add(t);

            return listview;
        }
        
        void Request(Uri request) {
            var bp = Path.GetFullPath(@"..\..\scripts");
            var rs = request.Scheme; //request.Split(new char[] { '/' });
            var path = Path.Combine(bp, rs+".js");
            if (!File.Exists(path)) return;

            var script = File.ReadAllText(path);
            en.SetParameter("request", request.AbsoluteUri);
            //en.SetFunction("deleteitem", new Action<int>((id) => {
            //    Console.WriteLine("deleteitem id = " + id.ToString());
            //}));
            object result = en.Run(script);
            int k = 0;
        }

        void ren() {
            var path = Path.GetFullPath(@"..\..\html\js\json2.js");
            //var path = Path.Combine(bp, rs + ".js");
            if (!File.Exists(path)) return;

            var script = File.ReadAllText(path);
            //en.SetParameter("request", request.AbsoluteUri);
            //en.SetFunction("deleteitem", new Action<int>((id) => {
            //    Console.WriteLine("deleteitem id = " + id.ToString());
            //}));
            object result = en.Run(script);
            int k = 0;
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
            var en = e;

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

        private void reBuild(List<Data> items) {
            foreach (var item in items) {
                //webBrowser1.Document.InvokeScript("js_BuildByID", new string[] { item.ID.ToString(), item.Text });
                InvokeScript("js_BuildByID", new string[] { item.ToJsonString() });
            }
        }

        private void reBuild(Data item) {
            //webBrowser1.Document.InvokeScript("js_BuildByID", new string[] { item.ID.ToString(), item.Text });
            InvokeScript("js_BuildByID", new string[] { item.ToJsonString() });
        }
        private void reBuild(long insertBefore,  Data item) {
            //webBrowser1.Document.InvokeScript("js_BuildByID", new string[] { item.ID.ToString(), item.Text });
            InvokeScript("js_BuildInsertByID", new string[] {insertBefore.ToString(), item.ToJsonString() });
        }

        private void webBrowser1_ContextMenuStripChanged(object sender, EventArgs e) {

        }

        private void timeToolStripMenuItem_Click(object sender, EventArgs e) {
            DateTimeForm f = new DateTimeForm();
            f.Time = DateTime.Now;
            var res = f.ShowDialog();
            var restime = f.Time;
        }

        private void toolStripButton1_Click(object sender, EventArgs e) {
            InvokeScript("jsview.jsmsg", new string[] { "jsview.jsmsg test" });
            //manager.Insert(new Data { ID = manager.GetNewID(), Text = "after", CreationTime = new DateTime(DateTime.Now.Ticks * 2) });
            //var p = Path.GetFullPath(@"..\..\html\wiki_parser.html");
            //webBrowser1.Navigate(p);
            //webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
        }
    }
}
