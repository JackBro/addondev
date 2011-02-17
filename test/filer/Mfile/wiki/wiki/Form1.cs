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

namespace wiki
{
    public partial class Form1 : Form
    {
        private ItemManager manager;
        //private Dictionary<int, string> testmap = new Dictionary<int, string>();
        //List<Data> datas = new List<Data>();
        //TabPage ItemAllTabPage;

        public Form1()
        {
            InitializeComponent();


            
            
            //webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
//            testmap[0] = @"!WikiParser TestTTTTTTT";
//            testmap[1] = @"!! List Test
//
//* ListItem 1
//* ListItem 2
//* ListItem 3
//** ListItem 3-1
//** ListItem 3-2
//* ListItem 4
//** ListItem 4-1
//** ListItem 4-2
//*** ListItem 4-2-1
//*** ListItem 4-2-2";
//            testmap[2] = @"!! Link Test
//
//* http://www.google.co.jp/
//* http://www.goo.ne.jp/img/logo/goo_top.gif
//* [[ぐーぐる|http://www.google.co.jp/]]
//* [[ぐー|http://www.goo.ne.jp/img/logo/goo_top.gif]]
//* [[ページ名]]";

//            foreach (var item in testmap) {
//                datas.Add(new Data { Text = item.Value });
//            }
            //textBox1.Text = "!WikiParser TestTTTTTTT";


            //listView1.SelectedIndexChanged += new EventHandler(listView1_SelectedIndexChanged);
            //listView1.VirtualListSize = datas.Count;

            this.Load += (sender, e) => {

            };
            this.FormClosing += (sender, e) => {
                if (manager.IsDirty) {
                    manager.Save();
                }
            };

            this.SizeChanged += (sender, e) => {

                for (int i = 0; i < ItemTabControl.TabPages.Count; i++) {
                    var page = ItemTabControl.TabPages[i];
                    var listview = GetTabControl(page);
                    listview.Columns["title"].Width = -2;
                }
            };

            textBox1.TextChanged += (sender, e) => {
                
                if (dirty) {
                    //var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                    //var items = listview.GetActive();
                    //if (items.Count == 1) {
                    //    items[0].Text = textBox1.Text;
                    //    manager.UpDate(items[0]);
                    //}
                    var item = manager.EditingData;
                    if (item != null) {
                        item.Text = textBox1.Text;
                        manager.UpDate(item);
                    }
                }
                dirty = true;
                //SetText(textBox1.Text);
            };
            textBox1.LostFocus += (sender, e) => {
                
            };

            ItemTabControl.Selecting += (sender, e) => {
            };

            //ItemAllTabPage = new TabPage("All");
            //ItemTabControl.TabPages.Add(ItemAllTabPage);

            ItemTabControl.TabIndexChanged += (sender, e) => {
                
            };

            NewItemToolStripButton.Click += (sender, e) => {
                CreateItem();
            };

            manager = new ItemManager();
            manager.DataPath = "data.bin";
            manager.Load();
            manager.eventHandler += (sender, e) => {
                //if (e.type == ChangeType.Add) {
                //    ItemTabControl.SelectedIndex = 0;
                //    var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                //    listview.AddItem(e.Item);
                //}
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
        }
        void f() {
            webBrowser1.DocumentCompleted -= new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);


            var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
           // reBuild(listview.DataItems);      
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
            manager.Insert(new Data { Text="!new" });

            //webBrowser1.Document.InvokeScript("test", new String[] { "called from client code" });
        }

        private void CreateItemView(List<Data> items) {
            var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;

        }

        private ListViewEx GetTabControl(TabPage page) {
            return page.Controls[0] as ListViewEx; 
        }

        //private Dictionary<TabPage, Data> dic;
        private bool dirty = false;
        private ListViewEx CreateListViewTabPage(string name, List<Data> items) {

            //ListView listview = new ListView();
            //listview.View = View.Details;
            //ColumnHeader headerName = new ColumnHeader();
            //headerName.Name = "name";
            //headerName.Text = "name";
            //listview.Columns.Add(headerName);

            //listview.FullRowSelect = true;
            //listview.VirtualMode = true;
            //listview.VirtualListSize = items.Count;

            //listview.RetrieveVirtualItem += (sender, e) => {
            //    if (e.ItemIndex < items.Count) {
            //        var data = items[e.ItemIndex];
            //        var item = new ListViewItem();
            //        item.Text = data.Title;
            //        e.Item = item;
            //    }       
            //};
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

        //void SetText(string text) {
        //    //if (listView1.SelectedIndices.Count == 1) {
        //    //    var index = listView1.SelectedIndices[0];
        //    //    var item = datas[index];
        //    //    item.Text = text;
        //        //reBuild(textBox1.Text);
        //    textBox1.Text = text;
        //        reBuild(text);
        //    //} 
        //}



        void Document_Click(object sender, HtmlElementEventArgs e) {
            //throw new NotImplementedException();
            var en = e;
        }

        void Document_ContextMenuShowing(object sender, HtmlElementEventArgs e) {
            var ae = webBrowser1.Document.ActiveElement;
            var en = e;
        }

        //void listView1_SelectedIndexChanged(object sender, EventArgs e)
        //{
        //        //throw new NotImplementedException();
        //    if (listView1.SelectedItems.Count > 0)
        //    {
        //        var item = listView1.SelectedItems[0];
        //        var text = testmap[item.Index];
        //        textBox1.Text = text;
        //        reBuild(text);
        //    }
        //}

        void webBrowser1_Navigating(object sender, WebBrowserNavigatingEventArgs e)
        {
            //throw new NotImplementedException();
            var l = e.Url;
            e.Cancel = true;
            EditItem(1);
        }

        private void EditItem(long id) {
            var item = manager.GetItem(id);
            if (item == null) return;
            manager.EditingData = item;
            dirty = false;
            textBox1.Text = item.Text; 
        }

        //private void reBuild()
        //{
        //    IHTMLDocument3 h3 = webBrowser1.Document.DomDocument as IHTMLDocument3;
        //    var em = h3.getElementById("txtSource");
        //    var te = em.innerText;
        //    em.innerText = textBox1.Text;
        //    webBrowser1.Document.InvokeScript("testrebuild");
        //}

        //private void reBuild(string text) {
        //    IHTMLDocument3 h3 = webBrowser1.Document.DomDocument as IHTMLDocument3;
        //    var em = h3.getElementById("txtSource");
        //    em.innerText = text;
        //    webBrowser1.Document.InvokeScript("testrebuild");
        //}

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

        //private void listView1_RetrieveVirtualItem(object sender, RetrieveVirtualItemEventArgs e) {
        //    if (e.ItemIndex < datas.Count) {
        //        var data = datas[e.ItemIndex];
        //        var item = new ListViewItem();
        //        item.Text = data.Title;
        //        e.Item = item;
        //    }
        //}

        //private void listView1_ItemSelectionChanged(object sender, ListViewItemSelectionChangedEventArgs e) {
        //    var s = e.ItemIndex;
        //    if (e.IsSelected) {
        //        var text = datas[s].Text;
        //        textBox1.Text = text;
        //        reBuild(text);
        //    }
        //}
    }
}
