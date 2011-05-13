using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Diagnostics;
using Peter;

namespace MF {
    public partial class MainForm : Form {
        public MainForm() {
            InitializeComponent();

            this.ResizeEnd += (sender, e) => {
                ResizeWindow();
            };
            flowLayoutPanel1.SizeChanged += (sender, e) => {
                ResizeWindow();
            };

            //UserControl1 us = new UserControl1();
            //us.Dock = DockStyle.Fill;
            //panel1.Controls.Add(us);

            var us = createView();
            if (activeUs == null) {
                activeUs = us;
            }

            RegDirListView.Items.Add(@"c:\");
            RegDirListView.Items.Add(@"d:\");

            //var p = Path.GetFullPath(@"..\d:\src\ruby");
            Uri u1 = new Uri(@"d:\data\src\ruby\");
            Uri u2 = new Uri(u1, @"..\");

            //listView1.MouseClick += (s, e) => {
            //    if (listView1.SelectedItems.Count > 0) {
            //        var item = listView1.SelectedItems[0];
            //        var t = item.Text;
            //        if (e.Button == MouseButtons.Left && activeUs !=null) {
            //            activeUs.Path = t;
            //        }
            //        else if (e.Button == MouseButtons.Middle) {
            //            createView(t);
            //        }
            //    }
            //};
            RegDirListView.MouseDown += (s, e) => {
                var item = RegDirListView.GetItemAt(e.Location.X, e.Location.Y);
                if (item != null) {
                    item.Selected = true;
                }
            };
            RegDirListView.MouseUp += (s, e) => {
                if (RegDirListView.SelectedItems.Count > 0) {
                    var item = RegDirListView.SelectedItems[0];
                    var t = item.Text;
                    if (e.Button == MouseButtons.Left && activeUs != null) {
                        activeUs.Path = t;
                    }
                    else if (e.Button == MouseButtons.Middle) {
                        createView(t);
                    }
                }
            };

            HistoryListView.ShowItemToolTips = true;
            HistoryListView.OwnerDraw=true;
            HistoryListView.DrawItem += (s, e) => {
                Color brush;
                if (HistoryListView.SelectedIndices.Contains(e.ItemIndex)) {
                    e.Graphics.FillRectangle(SystemBrushes.Highlight, e.Bounds);
                    brush = SystemColors.HighlightText;
                }
                else {
                    brush = SystemColors.WindowText;
                }
                Rectangle r = new Rectangle(e.Bounds.Location, new Size(HistoryListView.Width, e.Bounds.Height));
                TextRenderer.DrawText(e.Graphics, e.Item.Text, e.Item.Font, r, brush, TextFormatFlags.PathEllipsis);
            };

            flowLayoutPanel1.ControlRemoved += (s, e) => {
                ResizeWindow();
            };
            
        }

        private void ResizeWindow() {
            var rmax = 3;
            var cc = flowLayoutPanel1.Controls.Count < rmax ? flowLayoutPanel1.Controls.Count : rmax;
            //var w = panel1.Width / (panel1.Controls.Count) - 1;
            var w = flowLayoutPanel1.Width / cc;
            var hc = flowLayoutPanel1.Controls.Count / cc;
            var h = 0;
            if (hc == 0) {
                h = flowLayoutPanel1.Height;
            }
            else {
                h = flowLayoutPanel1.Controls.Count % cc == 0 ? flowLayoutPanel1.Height / hc : flowLayoutPanel1.Height / (hc + 1);
            }

            //foreach (var item in panel1.Controls) {
            foreach (var item in flowLayoutPanel1.Controls) {
                UserControl1 u = item as UserControl1;
                u.Width = w;
                u.Height = h;
            }          
        }

        private void newToolStripMenuItem_Click(object sender, EventArgs e) {
            UserControl1 us = new UserControl1();
            //us.Dock = DockStyle.Right;
            flowLayoutPanel1.Controls.Add(us); 

            ResizeWindow();
        }
        UserControl1 activeUs;
        private UserControl1 createView() {
            return createView(null);
        }
        private UserControl1 createView(string path) {
            UserControl1 us = new UserControl1();
            us.listView.BackColor = Color.LightGray;
            us.Enter += (s, e) => {
                if (activeUs != null) {
                    activeUs.listView.BackColor = Color.LightGray;
                }
                activeUs = us;
                activeUs.listView.BackColor = Color.White;
            };

            us.listView.DoubleClick += (sender, e) => {
                if (us.listView.SelectedIndices.Count == 1) {
                    var items = us.ItemList; 
                    var index = us.listView.SelectedIndices[0];
                    //var fp = System.IO.Path.Combine(us.Path, items[index].Name);
                    var fp = getFullPath(us.Path, items[index].Name);
                    //if (items[index].IsFile) {
                    if (File.Exists(fp)) {
                        HistoryListView.Items.Insert(0, fp);
                        Process.Start(fp);
                    }
                    else if(Directory.Exists(fp)){
                        us.Path = fp;
                    }

                }
            };
            //us.listView.MouseDown += (s, e) => {
            //    if (e.Button == MouseButtons.Middle || e.Button == MouseButtons.Right) {
            //        var item = us.listView.GetItemAt(e.Location.X, e.Location.Y);
            //        if (item != null) {
            //            if ((e.Button == MouseButtons.Right && !us.listView.SelectedIndices.Contains(item.Index))
            //                || e.Button == MouseButtons.Middle) {
            //                var indexs = new int[us.listView.SelectedIndices.Count];
            //                us.listView.SelectedIndices.CopyTo(indexs, 0);
            //                for (int i = 0; i < indexs.Length; i++) {
            //                    us.listView.Items[indexs[i]].Selected = false;
            //                }
            //                item.Selected = true;
            //            }
            //        }
            //    }
            //};
            us.listView.MouseUp += (s, e) => {
                if (e.Button == MouseButtons.Middle) {
                    if (us.listView.SelectedIndices.Count > 0) {
                        var item = us.ItemList[us.listView.SelectedIndices[0]];
                        if (!item.IsFile) {
                            var p = Path.Combine(us.Path, item.Name);
                            createView(p);
                        }
                    }
                }
                //else if (e.Button == MouseButtons.Right) {
                //    var ctm = new ShellContextMenu();
                //    var selfiles = us.SelectedItemList;
                //    if (selfiles.Count == 0) {
                //        DirectoryInfo[] dir = new DirectoryInfo[1];
                //        dir[0] = new DirectoryInfo(us.Path);
                //        ctm.ShowContextMenu(dir, us.listView.PointToScreen(new Point(e.X, e.Y)));
                //    }
                //    else {
                //        List<FileInfo> arrFI = new List<FileInfo>();
                //        selfiles.ForEach(x => {
                //            arrFI.Add(new FileInfo(Path.Combine(us.Path, x.Name)));
                //        });
                //        ctm.ShowContextMenu(arrFI.ToArray(), us.listView.PointToScreen(new Point(e.X, e.Y)));
                //    } 
                //}
            };
            us.ChangePath += (s, e) => {
                HistoryListView.Items.Insert(0, e.path);
            };

            flowLayoutPanel1.Controls.Add(us);
            us.Path = path;

            ResizeWindow();

            return us;
        }

        private string getFullPath(string parent, string name) {
            if(name.EndsWith(".lnk", StringComparison.CurrentCultureIgnoreCase)){
                return Util.getShortcutPath(System.IO.Path.Combine(parent, name));
            }else{
                return System.IO.Path.Combine(parent, name);
            }
            
        }
    }
}
