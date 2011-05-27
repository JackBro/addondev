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
using MouseGesture_Net;

namespace MF {
    public partial class MainForm : Form {
        Dictionary<Keys, Action<MainForm>> _KeyMap = new Dictionary<Keys, Action<MainForm>>();
        MouseGesture mg;

        private CommentMg cmg = new CommentMg();

        public MainForm() {
            InitializeComponent();

            mg = new MouseGesture();
            initKeyMap();
            initMouseGesture();

            this.ResizeEnd += (sender, e) => {
                ResizeWindow();
            };
            this.KeyPreview = true;
            this.KeyDown += (sender, e) => {
                if (_KeyMap.ContainsKey(e.KeyData)) {
                    _KeyMap[e.KeyData](this);
                    e.Handled = true;
                    e.SuppressKeyPress = true;
                }
            };
            this.FormClosing += (s, e) => {
                this.save();
            };


            flowLayoutPanel1.SizeChanged += (sender, e) => {
                ResizeWindow();
            };

            //var us = createView();
            //if (activeUs == null) {
            //    activeUs = us;
            //}
            load();

            RegDirListView.Items.Add(@"c:\");
            RegDirListView.Items.Add(@"d:\");

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
                        activeUs.Dir = t;
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

            ////foreach (var item in panel1.Controls) {
            //foreach (var item in flowLayoutPanel1.Controls) {
            //    UserControl1 u = item as UserControl1;
            //    u.Width = w;
            //    u.Height = h;
            //}
            
            for (int i = 0; i < cc*hc; i++) {
                var item = flowLayoutPanel1.Controls[i] as UserControl1;
                UserControl1 u = item as UserControl1;
                u.Width = w;
                u.Height = h;

                //u.resetColumSize();   
            }

            var resc = flowLayoutPanel1.Controls.Count % cc;
            if (resc != 0) {
                var resw = flowLayoutPanel1.Width / resc;
                for (int i = cc * hc; i < cc * hc + resc; i++) {
                    var item = flowLayoutPanel1.Controls[i] as UserControl1;
                    UserControl1 u = item as UserControl1;
                    u.Width = resw;
                    u.Height = h;

                    //u.resetColumSize();
                }
            }
        }

        private void newToolStripMenuItem_Click(object sender, EventArgs e) {
            UserControl1 us = new UserControl1();
            //us.Dock = DockStyle.Right;
            flowLayoutPanel1.Controls.Add(us); 

            ResizeWindow();
        }
        internal UserControl1 activeUs;
        private UserControl1 createView() {
            return createView(null);
        }

        string mglog = string.Empty;
        //bool mgstart = false;
        private UserControl1 createView(string path) {
            UserControl1 us = new UserControl1();
            us.listView.BackColor = Color.LightGray;
            us.Enter += (s, e) => {
                if (activeUs != null) {
                    activeUs.listView.BackColor = Color.LightGray;
                    if(mg !=null)mg.End();
                    mglog = string.Empty;
                    //mgstart = false;
                }
                activeUs = us;
                activeUs.listView.BackColor = Color.White;
            };

            //us.listView.DoubleClick += (sender, e) => {
            //    if (us.listView.SelectedIndices.Count == 1) {
            //        var items = us.ItemList; 
            //        var index = us.listView.SelectedIndices[0];
            //        //var fp = System.IO.Path.Combine(us.Path, items[index].Name);
            //        var fp = getFullPath(us.Dir, items[index].Name);
            //        //if (items[index].IsFile) {
            //        if (File.Exists(fp)) {
            //            HistoryListView.Items.Insert(0, fp);
            //            Process.Start(fp);
            //        }
            //        else if(Directory.Exists(fp)){
            //            us.Dir = fp;
            //        }
            //    }
            //};
            us.listView.DoubleClickEx += (sender, e) => {
                if (us.listView.SelectedIndices.Count == 1) {
                    var items = us.ItemList;
                    var index = us.listView.SelectedIndices[0];
                    //var fp = System.IO.Path.Combine(us.Path, items[index].Name);
                    var fp = getFullPath(us.Dir, items[index].Name);
                    //if (items[index].IsFile) {
                    if (File.Exists(fp)) {
                        HistoryListView.Items.Insert(0, fp);
                        Process.Start(fp);
                    }
                    else if (Directory.Exists(fp)) {
                        us.Dir = fp;
                    }

                }
                else if (us.listView.SelectedIndices.Count == 0) {
                    us.UpDir();
                }
            };

            us.listView.MouseDown += (s, e) => {
                if (e.Button == MouseButtons.Right) {
                    mglog = string.Empty;
                    //if (mg == null) mg = new MouseGesture();
                    
                    //mg.Start(us.listView, e.Location);
                    mg.Start(us.listView, us.listView.PointToScreen(new Point(e.X, e.Y)));
                    activeUs.listView.MultiSelect = false;
                }

                if (popupForm != null && popupForm.Visible) {
                    popupForm.Visible = false;
                }
            };

            us.listView.MouseMove += (s, e) => {
                if (e.Button == MouseButtons.Right) {

                    //Arrow arrow = mg.Test(new Point(e.X, e.Y));
                    Arrow arrow = mg.Test(us.listView.PointToScreen(new Point(e.X, e.Y)));
                    if (arrow != Arrow.none) {
                        switch (arrow) {
                            case Arrow.up:
                                //label1.Text += "↑";
                                mglog += "U";
                                break;
                            case Arrow.right:
                                //label1.Text += "→";
                                mglog += "R";
                                break;
                            case Arrow.down:
                                //label1.Text += "↓";
                                mglog += "D";
                                break;
                            case Arrow.left:
                                mglog += "L";
                                //label1.Text += "←";
                                break;
                        }
                    }
                }
            };
            us.listView.MouseUpEx += (s, e) => {
                if (e.Button == MouseButtons.Right) {
                    //Console.WriteLine("mglog=" + mglog);
                    if (MouseGestureMap.ContainsKey(mglog)) {
                        MouseGestureMap[mglog](this);
                    }
                    else if (mglog == string.Empty) {
                        //UserControl1 u = s as UserControl1;
                        var ctm = new ShellContextMenu();
                        var selfiles = us.SelectedItemList;
                        if (selfiles.Count == 0) {
                            //ctm.CreateFolderMenu(us.listView.PointToScreen(new Point(e.X, e.Y)), us.Dir);
                            DirectoryInfo[] dir = new DirectoryInfo[1];
                            dir[0] = new DirectoryInfo(us.Dir);
                            ctm.ShowContextMenu(dir, us.listView.PointToScreen(new Point(e.X, e.Y)));
                        }
                        else {
                            List<FileInfo> arrFI = new List<FileInfo>();
                            selfiles.ForEach(x => {
                                arrFI.Add(new FileInfo(System.IO.Path.Combine(us.Dir, x.Name)));
                            });
                            ctm.ShowContextMenu(arrFI.ToArray(), us.listView.PointToScreen(new Point(e.X, e.Y)));
                        }
                    }
                    if (mg != null) mg.End();
                    mglog = string.Empty;
                    activeUs.listView.MultiSelect = true;
                }
            };
            us.listView.MouseUp += (s, e) => {
                if (e.Button == MouseButtons.Middle) {
                    if (us.listView.SelectedIndices.Count > 0) {
                        var item = us.ItemList[us.listView.SelectedIndices[0]];
                        if (!item.IsFile) {
                            var p = Path.Combine(us.Dir, item.Name);
                            createView(p);
                        }
                    }
                }
                else if (e.Button == MouseButtons.Right && !activeUs.listView.MultiSelect) {
                    var ctm = new ShellContextMenu();
                    var selfiles = us.SelectedItemList;
                    //if (selfiles.Count == 0) {
                    //    DirectoryInfo[] dir = new DirectoryInfo[1];
                    //    dir[0] = new DirectoryInfo(us.Dir);
                    //    ctm.ShowContextMenu(dir, us.listView.PointToScreen(new Point(e.X, e.Y)));
                    //}
                    //else 
                    if (selfiles.Count > 0) {
                        List<FileInfo> arrFI = new List<FileInfo>();
                        selfiles.ForEach(x => {
                            arrFI.Add(new FileInfo(System.IO.Path.Combine(us.Dir, x.Name)));
                        });
                        ctm.ShowContextMenu(arrFI.ToArray(), us.listView.PointToScreen(new Point(e.X, e.Y)));
                    }
                    activeUs.listView.MultiSelect = true;
                }
            };
            us.listView.ItemMouseHover += (s, e) => {
                //e.Item.
                //var kk=0;
                
            };
            us.ChangePath += (s, e) => {
                HistoryListView.Items.Insert(0, e.path);
            };

            flowLayoutPanel1.Controls.Add(us);
            us.Dir = path;

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

        internal void initKeyMap() {
            _KeyMap.Clear();
            _KeyMap.Add(Keys.Control | Keys.X, Actions.Cut);
            _KeyMap.Add(Keys.Control | Keys.C, Actions.Copy);
            _KeyMap.Add(Keys.Control | Keys.V, Actions.Paste);
            _KeyMap.Add(Keys.Delete, Actions.Delete);

            _KeyMap.Add(Keys.F5, Actions.UpDateInfo);

            _KeyMap.Add(Keys.Control | Keys.Space, Actions.ShowPopupForm);
        }

        private Dictionary<string, Action<MainForm>> MouseGestureMap = new Dictionary<string, Action<MainForm>>();
        internal void initMouseGesture() {
            MouseGestureMap.Clear();
            MouseGestureMap.Add("L", Actions.UpDir);
        }

        internal void load() {
            var list = XMLSerializer.Deserialize<List<string>>("list", new List<string>() { Path.GetDirectoryName(Application.ExecutablePath) });
            foreach (var item in list) {
                if (Directory.Exists(item)) {
                    var us = createView(item);
                    if (activeUs == null) {
                        activeUs = us;
                    }
                }
            }
        }

        internal void save() {
            var list = new List<string>();
            foreach (var c in flowLayoutPanel1.Controls) {
                UserControl1 us = c as UserControl1;
                list.Add(us.Dir);
            }
            XMLSerializer.Serialize<List<string>>("list", list);
        }

        internal void ShowPopupForm() {
            if (activeUs == null) return;

            var lv = activeUs.listView;
            if (lv.SelectedIndices.Count > 0) {
                if (popupForm == null) {
                    popupForm = new PopupForm();
                }
                var item = lv.Items[lv.SelectedIndices[0]];
                var point = lv.PointToScreen(item.Bounds.Location);

                popupForm.Show(this, "test", point.X, point.Y + item.Bounds.Height);
            }
            
        }

        private PopupForm popupForm;
        private void toolStripButton1_Click(object sender, EventArgs e) {
            //activeUs.listView.MultiSelect = !activeUs.listView.MultiSelect;
            //Text = activeUs.listView.MultiSelect.ToString();
            //var f = new PopupForm();
            //f.Show("[[test]]\nok");
            //f.Show();
        }
    }
}
