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


            webBrowser1.IsWebBrowserContextMenuEnabled = false;
            
            webBrowser1.DocumentCompleted += new WebBrowserDocumentCompletedEventHandler(webBrowser1_DocumentCompleted);
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
            var p = Path.GetFullPath(@"..\..\html\wiki_parser.html");
            webBrowser1.Navigate(p);

            webBrowser1.Navigating += new WebBrowserNavigatingEventHandler(webBrowser1_Navigating);

            //listView1.SelectedIndexChanged += new EventHandler(listView1_SelectedIndexChanged);
            //listView1.VirtualListSize = datas.Count;

            this.Load += (sender, e) => {

            };
            this.FormClosing += (sender, e) => {
                if (manager.IsDirty) {
                    manager.Save();
                }
            };

            textBox1.TextChanged += (sender, e) => {
                
                if (dirty) {
                    var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                    var items = listview.GetActive();
                    if (items.Count == 1) {
                        items[0].Text = textBox1.Text;
                        manager.UpDate();
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
                if (e.type == ChangeType.Add) {
                    ItemTabControl.SelectedIndex = 0;
                    var listview = ItemTabControl.SelectedTab.Controls[0] as ListViewEx;
                    listview.AddItem(e.Item);
                }
            };

            CreateListViewTabPage(manager.Filter(x => { return true; }));
        }

        private void CreateItem() {
            manager.Insert(new Data { Text="new" });
        }

        //private Dictionary<TabPage, Data> dic;
        private bool dirty = false;
        private void CreateListViewTabPage(List<Data> items){

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
                        SetText(text);

                    } else if (MouseButtons == MouseButtons.Middle) {
                    
                    }
                    //reBuild(text);
                }              
            };
            listview.Dock = DockStyle.Fill;

            var t = new TabPage();
            t.Controls.Add(listview);
            ItemTabControl.TabPages.Add(t);
        }

        void SetText(string text) {
            //if (listView1.SelectedIndices.Count == 1) {
            //    var index = listView1.SelectedIndices[0];
            //    var item = datas[index];
            //    item.Text = text;
                //reBuild(textBox1.Text);
            textBox1.Text = text;
                reBuild(text);
            //} 
        }

        void webBrowser1_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e) {
            webBrowser1.Document.ContextMenuShowing += new HtmlElementEventHandler(Document_ContextMenuShowing);
            webBrowser1.Document.Click += new HtmlElementEventHandler(Document_Click);
        }

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
            
        }

        private void reBuild()
        {
            IHTMLDocument3 h3 = webBrowser1.Document.DomDocument as IHTMLDocument3;
            var em = h3.getElementById("txtSource");
            var te = em.innerText;
            em.innerText = textBox1.Text;
            webBrowser1.Document.InvokeScript("testrebuild");
        }

        private void reBuild(string text) {
            IHTMLDocument3 h3 = webBrowser1.Document.DomDocument as IHTMLDocument3;
            var em = h3.getElementById("txtSource");
            em.innerText = text;
            webBrowser1.Document.InvokeScript("testrebuild");
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
