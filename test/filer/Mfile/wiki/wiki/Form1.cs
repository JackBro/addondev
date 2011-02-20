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

namespace wiki
{
    public partial class Form1 : Form
    {
        private ItemManager manager;
        private long baseticks;
        JintEngine en = new JintEngine();
        public Form1(){
            InitializeComponent();
            TimeSpan utcOffset = System.TimeZoneInfo.Local.BaseUtcOffset;
            baseticks = new DateTime(1601, 01, 01).Ticks + utcOffset.Ticks;

            this.Load += (sender, e) => {

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
                webBrowser1.Document.InvokeScript("testClearAll");
                reBuild(listview.DataItems);
            };

            en = new JintEngine();
            en.DisableSecurity();
            en.AllowClr = true;
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
                    case ChangeType.Add:
                        {
                            ItemTabControl.SelectedIndex = 0;
                            var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                            listview.AddItem(e.Item);
                            reBuild(e.Item);
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

            var newlistview = CreateListViewTabPage("All", manager.Filter(x => { return true; }));
            //reBuild(newlistview.DataItems);

            webBrowser1.IsWebBrowserContextMenuEnabled = false;
            var p = Path.GetFullPath(@"..\..\html\wiki_parser.html");
            webBrowser1.Navigate(p);
            webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
            webBrowser1.Navigating += new WebBrowserNavigatingEventHandler(webBrowser1_Navigating);

            comboBox1.AutoCompleteMode = AutoCompleteMode.Suggest;
            comboBox1.AutoCompleteSource = AutoCompleteSource.CustomSource;
            var s = new AutoCompleteStringCollection();
            comboBox1.AutoCompleteCustomSource = s;

            comboBox1.KeyDown += (sender, e) => {
                if (e.KeyCode == Keys.Return && comboBox1.Text.Length>0) {
                    CreateListViewTabPage(comboBox1.Text, manager.Filter(x => 
                    {
                        return x.Text.Contains(comboBox1.Text);
                        //return true; 
                    }));
                }
            };
        }

        void webBrowser1_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e) {
            webBrowser1.DocumentCompleted -= webBrowser1_DocumentCompleted;
            Console.WriteLine("webBrowser1_DocumentCompleted : " + e.Url);
            //webBrowser1.Document.ContextMenuShowing += new HtmlElementEventHandler(Document_ContextMenuShowing);
            //webBrowser1.Document.Click += new HtmlElementEventHandler(Document_Click);
            var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
            reBuild(listview.DataItems);

        }

        private void CreateItem() {
            manager.Insert(new Data { Text = "!new", CreationTime = new DateTime(DateTime.Now.Ticks+baseticks) });

            //webBrowser1.Document.InvokeScript("test", new String[] { "called from client code" });
        }
        private void DeleteItem(long id) {
            manager.Delete(id);
            webBrowser1.Document.InvokeScript("cs_Remove", new string[] { id.ToString() });
            
        }
        private void EditItem(long id) {
            var item = manager.GetItem(id);
            if (item == null) return;
            manager.EditingData = item;
            dirty = false;
            textBox1.Text = item.Text;
        }


        //private void CreateItemView(List<Data> items) {
        //    var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
        //}

        private ListViewEx GetTabControl(TabPage page) {
            return page.Controls[0] as ListViewEx; 
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
            var script = File.ReadAllText(path);
            en.SetParameter("request", request.AbsoluteUri);
            //en.SetFunction("deleteitem", new Action<int>((id) => {
            //    Console.WriteLine("deleteitem id = " + id.ToString());
            //}));
            object result = en.Run(script);
            int k = 0;
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
            e.Cancel = true;
            this.Request(e.Url);
            //EditItem(1);
        }


        private void reBuild(List<Data> items) {
            foreach (var item in items) {
                webBrowser1.Document.InvokeScript("testrebuildbyid", new string[] { item.ID.ToString(), item.Text });
            }
        }

        private void reBuild(Data item) {
            webBrowser1.Document.InvokeScript("testrebuildbyid", new string[] { item.ID.ToString(), item.Text });
        }

        private void webBrowser1_ContextMenuStripChanged(object sender, EventArgs e) {

        }

        private void timeToolStripMenuItem_Click(object sender, EventArgs e) {
            DateTimeForm f = new DateTimeForm();
            f.Time = DateTime.Now;
            var res = f.ShowDialog();
            var restime = f.Time;
        }
    }
}
