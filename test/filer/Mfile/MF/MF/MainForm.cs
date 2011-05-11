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
            panel1.SizeChanged += (sender, e) => {
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
            
        }

        private void ResizeWindow() {
            var w = panel1.Width / (panel1.Controls.Count) - 1;

            foreach (var item in panel1.Controls) {
                UserControl1 u = item as UserControl1;
                u.Width = w;
            }          
        }

        private void newToolStripMenuItem_Click(object sender, EventArgs e) {
            UserControl1 us = new UserControl1();
            us.Dock = DockStyle.Right;
            panel1.Controls.Add(us); 

            ResizeWindow();
        }
        UserControl1 activeUs;
        private UserControl1 createView() {
            return createView(null);
        }
        private UserControl1 createView(string path) {
            UserControl1 us = new UserControl1();
            us.Enter += (s, e) => {
                activeUs = us;
            };
            us.listView.DoubleClick += (sender, e) => {
                if (us.listView.SelectedIndices.Count == 1) {
                    var items = us.ItemList; 
                    var index = us.listView.SelectedIndices[0];
                    var fp = System.IO.Path.Combine(us.Path, items[index].Name);
                    if (items[index].IsFile) {
                        Process.Start(fp);
                    }
                    else {
                        us.Path = fp;
                    }

                }
            };
            us.listView.MouseDown += (s, e) => {
                if (e.Button == MouseButtons.Middle) {
                    var item = us.listView.GetItemAt(e.Location.X, e.Location.Y);
                    if (item != null) {
                        var indexs = us.listView.SelectedIndices;
                        if (indexs.Count > 0) {
                            for (int i = 0; i < indexs.Count; i++) {
                                us.listView.Items[indexs[i]].Selected = false;
                            }
                        }
                        item.Selected = true;
                    }
                } else if (e.Button == MouseButtons.Right) {
                    var ctm = new ShellContextMenu();
                    var selfiles = us.SelectedItemList;
                    if (selfiles.Count == 0) {
                        DirectoryInfo[] dir = new DirectoryInfo[1];
                        dir[0] = new DirectoryInfo(us.Path);
                        ctm.ShowContextMenu(dir, us.listView.PointToScreen(new Point(e.X, e.Y)));
                    } else {
                        List<FileInfo> arrFI = new List<FileInfo>();
                        selfiles.ForEach(x => {
                            arrFI.Add(new FileInfo(Path.Combine(us.Path, x.Name)));
                        });
                        ctm.ShowContextMenu(arrFI.ToArray(), us.listView.PointToScreen(new Point(e.X, e.Y)));
                    }    
                }
            };
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
            };
            us.ChangePath += (s, e) => {
                //string tt=e.path;
                //TextRenderer.DrawText
                //TextRenderer.MeasureText(tt, us.listView.Font, new Size(100, 20), TextFormatFlags.ModifyString); 
                HistoryListView.Items.Insert(0, e.path);
            };

            us.Dock = DockStyle.Right;

            panel1.Controls.Add(us);
            us.Path = path;

            ResizeWindow();

            return us;
        }
    }
}
