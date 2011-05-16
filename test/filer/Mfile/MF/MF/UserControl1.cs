using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Runtime.InteropServices;
using Peter;
using System.Collections.Specialized;

namespace MF {

    public partial class UserControl1 : UserControl, IDisposable {
        private const int LVM_FIRST = 0x1000;
        private const int LVM_SETEXTENDEDLISTVIEWSTYLE = (LVM_FIRST + 54);
        private const int LVM_GETEXTENDEDLISTVIEWSTYLE = (LVM_FIRST + 55);
        private const int LVS_EX_DOUBLEBUFFER = 0x00010000;

        private IntPtr dc_;
        private IntPtr hfont_;
        private IntPtr hwnd_;

        private int fit;
        //private Win32API.SIZE GetTextExtend(string str, out int fit) {
        private Win32API.SIZE GetTextExtend(string str) {
            IntPtr OldFont = Win32API.SelectObject(dc_, hfont_);
            Win32API.SIZE size = new Win32API.SIZE();
            Win32API.GetTextExtentExPointW(dc_, str, str.Length, int.MaxValue, out fit, null, out size);
            Win32API.SelectObject(dc_, OldFont);
            return size;
        }

        public ListView listView {
            get { return listView1; }
        }

        public event EventHandler Closed;
        public event EventHandler Closing;

        private int MaxNameWidth;
        private int DateWidth;
        private string DateFormat;
        private TextFormatFlags flg;
        private DateTime mdtime = DateTime.Now;

        public UserControl1() {
            InitializeComponent();

            this.DoubleBuffered = true;
            this.Margin = new Padding(0);

            hwnd_ = listView1.Handle;
            dc_ = Win32API.GetDC(hwnd_);
            hfont_ = listView1.Font.ToHfont();

            listView1.OwnerDraw = true;
            listView1.VirtualMode = true;
            listView1.VirtualListSize = 0;
            //listView1.FullRowSelect = true;
            listView1.View = View.Details;
            
            int styles = (int)MF.Win32API.NativeMethods.SendMessage(listView1.Handle, (int)LVM_GETEXTENDEDLISTVIEWSTYLE, 0, (IntPtr)0);
            styles |= LVS_EX_DOUBLEBUFFER;
            MF.Win32API.NativeMethods.SendMessage(listView1.Handle, (int)LVM_SETEXTENDEDLISTVIEWSTYLE, 0, (IntPtr)styles);


            DateWidth = GetTextExtend(DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss")).width+10;

            ColumnHeader headerName = new ColumnHeader();
            headerName.Name = "name";
            headerName.Text = "name";
            listView1.Columns.Add(headerName);

            ColumnHeader headerType = new ColumnHeader();
            headerType.Name = "type";
            headerType.Text = "type";
            listView1.Columns.Add(headerType);

            ColumnHeader headerSize = new ColumnHeader();
            headerSize.Name = "size";
            headerSize.Text = "size";
            listView1.Columns.Add(headerSize);

            ColumnHeader headerLastWriteTime = new ColumnHeader();
            headerLastWriteTime.Name = "lastwritetime";
            headerLastWriteTime.Text = "lastwritetime";
            headerLastWriteTime.Width = DateWidth;

            //headerLastWriteTime.AutoResize(ColumnHeaderAutoResizeStyle.ColumnContent);
            listView1.Columns.Add(headerLastWriteTime);
            //listView1.ColumnClick

            listView1.ShowItemToolTips = true;

            listView1.SizeChanged += (sender, e) => {
                //var w = listView1.Width;
                //w -= headerType.Width;
                //w -= headerSize.Width;
                //w -= headerLastWriteTime.Width + 10;
                //if (w > 0) {
                //    headerName.Width = w;
                //}
            };

            //listView1.DoubleClick += (sender, e) => {
            //    if (listView1.SelectedIndices.Count == 1) {
            //        var index = listView1.SelectedIndices[0];
            //        if (!Items[index].IsFile) {
            //            var name = Items[index].Name;
            //            this.Path = System.IO.Path.Combine(this.Path, name);
            //        }
            //    }
            //    else {
            //    }
            //};

            listView1.RetrieveVirtualItem += (sender, e) => {
                if (e.ItemIndex < Items.Count) {
                    var item = Items[e.ItemIndex];
                    var listviewitem = new ListViewItem();

                    listviewitem.Text = item.Name;
                    //if (item.IsFile) {
                        //listviewitem.ForeColor = Color.Yellow;
                        listviewitem.SubItems.Add(item.type);
                    //}
                    //else {
                    //    listviewitem.SubItems.Add("Dir");
                    //}
                    
                    listviewitem.SubItems.Add(getFileSizeFormat(item.Size));
                    if (item.LastWriteTime!=null) listviewitem.SubItems.Add(item.LastWriteTime.ToString("yyyy/MM/dd HH:mm:ss"));
                    //listviewitem.SubItems.Add(item.LastWriteTime.ToLongDateString());
                    e.Item = listviewitem;
                }
            };


            listView1.DrawSubItem += (sender, e) => {
                //// 描画するSubItemが2列目(ColumnIndexが1)の時は、StringAligment.Farに設定して、右寄せにする
                //// それ以外は、Nearにして、標準の左寄せ
                //StringFormat drawFormat = new StringFormat();
                //if (e.ColumnIndex == 2) {
                //    drawFormat.Alignment = StringAlignment.Far;
                //}
                //else {
                //    drawFormat.Alignment = StringAlignment.Near;
                //}

                //System.Drawing.Brush brush;

                //// Forcus = 0x0010,Selected = 0x0001 として ItemStateにForcusとSelectedがセットされていた場合、
                //// 2bitで書くと Selected = 0001 0000 ItemState = 0000 0001で
                //// この二つ論理積を取ると0000 0001(0x0001 = Selected)となる。
                ////if ((e.ItemState & ListViewItemStates.Selected) == ListViewItemStates.Selected) {
                //if (listView1.SelectedIndices.Contains(e.ItemIndex)) {
                //    // Hightlightで範囲を塗りつぶす
                //    if (e.ColumnIndex == 0) {

                //        e.Graphics.FillRectangle(SystemBrushes.Highlight, e.Bounds);

                //    // 上でセルを塗りつぶしているので、表示する文字を反転する
                //    brush = SystemBrushes.HighlightText;
                //    }else{
                //        brush = SystemBrushes.WindowText;
                //    }
                //}
                //else {
                //    //if (!Items[e.ItemIndex].IsFile) {
                //    //    brush = SystemBrushes.HighlightText;
                //    //} else {
                //    // 塗りつぶされていない通常のセルはWindowsTextに設定する
                //    brush = SystemBrushes.WindowText;
                //    //}
                //}

                //// 上で設定した,brushとdrawFormatを利用して文字を描画する
                //e.Graphics.DrawString(e.SubItem.Text, e.Item.Font, brush, e.Bounds, drawFormat);

                //// drawFormatを開放する
                //drawFormat.Dispose();

                //TextFormatFlags flg;
                if (e.ColumnIndex == 0) {
                    flg = TextFormatFlags.EndEllipsis;
                }
                else if (e.ColumnIndex == 2) {
                    flg = TextFormatFlags.Right;
                }
                else {
                    flg = TextFormatFlags.Left;
                }
                Color brush;
                if (e.ColumnIndex == 0 && listView1.SelectedIndices.Contains(e.ItemIndex)) {
                    //if (e.ColumnIndex == 0) {

                    e.Graphics.FillRectangle(SystemBrushes.Highlight, e.Bounds);

                    //}
                    //Rectangle hr = new Rectangle(e.Bounds.Location, new Size(listView1.Width, e.Bounds.Height));
                    //e.Graphics.FillRectangle(SystemBrushes.Highlight, hr);
                    brush = SystemColors.HighlightText;
                }
                else {
                    if (Items[e.ItemIndex].IsFile) {
                        brush = SystemColors.WindowText;
                    }
                    else {
                        brush = Color.Blue;
                    }
                }
                Rectangle r = new Rectangle(e.Bounds.Location, new Size(listView1.Columns[e.ColumnIndex].Width, e.Bounds.Height));
                TextRenderer.DrawText(e.Graphics, e.SubItem.Text, e.Item.Font, r, brush, flg);
            };

            listView1.DrawColumnHeader += (sender, e) => {
                e.DrawDefault = true;
            };
            bool res = false;
            listView1.MouseDown += (s, e) => {
                var lv = s as ListView;
                //if (e.Button == MouseButtons.Left) {
                //    if (res) {
                //        res = false;
                //        return;
                //    }
                //    var mdinv = DateTime.Now - mdtime;
                //    if (!res && mdinv.Milliseconds <= SystemInformation.DoubleClickTime/2) {
                //        if (lv.SelectedIndices.Count == 0) {
                //            MoveUp();
                //            res = true;
                //        }
                //    }
                //    else {
                //    mdtime = DateTime.Now;
                //    }
                //}else 
                if (e.Button == MouseButtons.Middle || e.Button == MouseButtons.Right) {
                    
                    var item = lv.GetItemAt(e.Location.X, e.Location.Y);
                    if (item != null) {
                        if ((e.Button == MouseButtons.Right && !lv.SelectedIndices.Contains(item.Index))
                            || e.Button == MouseButtons.Middle) {
                            var indexs = new int[lv.SelectedIndices.Count];
                            lv.SelectedIndices.CopyTo(indexs, 0);
                            for (int i = 0; i < indexs.Length; i++) {
                                listView1.Items[indexs[i]].Selected = false;
                            }
                            item.Selected = true;
                        }
                    }
                }
            };
            listView1.MouseUp += (s, e) => {
                var lv = s as ListView;
                if (e.Button == MouseButtons.Right) {
                    var ctm = new ShellContextMenu();
                    var selfiles = SelectedItemList;
                    if (selfiles.Count == 0) {
                        DirectoryInfo[] dir = new DirectoryInfo[1];
                        dir[0] = new DirectoryInfo(this.Dir);
                        ctm.ShowContextMenu(dir, lv.PointToScreen(new Point(e.X, e.Y)));
                    }
                    else {
                        List<FileInfo> arrFI = new List<FileInfo>();
                        selfiles.ForEach(x => {
                            arrFI.Add(new FileInfo(System.IO.Path.Combine(this.Dir, x.Name)));
                        });
                        ctm.ShowContextMenu(arrFI.ToArray(), lv.PointToScreen(new Point(e.X, e.Y)));
                    }
                }
            };

            textBox1.KeyDown +=(sender, e)=>{
                if (e.KeyCode == Keys.Return) {
                    Dir = textBox1.Text;
                }
            };

            CloseLabel.Click += (s, e) => {
                Close();
            };

            fileSystemWatcher1.IncludeSubdirectories = false;
            //fileSystemWatcher1.NotifyFilter = (NotifyFilters.DirectoryName | NotifyFilters.FileName | NotifyFilters.Size);
            fileSystemWatcher1.SynchronizingObject = this;
            fileSystemWatcher1.Created += new FileSystemEventHandler(fileSystemWatcher1_Changed);
            fileSystemWatcher1.Deleted += new FileSystemEventHandler(fileSystemWatcher1_Changed);
            fileSystemWatcher1.Changed += new FileSystemEventHandler(fileSystemWatcher1_Changed);
            fileSystemWatcher1.Renamed += (s, e) => {
                //var index = e.OldFullPath.LastIndexOf("\\");
                //var oldname = e.OldFullPath.Substring(index + 1, e.OldFullPath.Length - (index + 1));
                var old = Items.First(x => {
                    return x.Name.Equals(e.OldName);
                });
                if (old != null) {
                    old.Name = e.Name;
                    var index = Items.IndexOf(old);
                    listView1.RedrawItems(index, index, false);
                }
            };

        }

        internal void Copy() {
            StringCollection files = new StringCollection();
            foreach (var item in SelectedItemList) {
                files.Add(System.IO.Path.Combine(this.Dir, item.Name));
            }
            Clipboard.SetFileDropList(files);
        }

        internal void Cut() {
            var files = new List<string>();
            var selfiles = SelectedItemList;
            foreach (var item in selfiles) {
                files.Add(System.IO.Path.Combine(this.Dir, item.Name));
            }
            string[] fileNames = files.ToArray();
            IDataObject data = new DataObject(DataFormats.FileDrop, fileNames);
            byte[] bs = new byte[] { (byte)DragDropEffects.Move, 0, 0, 0 };
            System.IO.MemoryStream ms = new System.IO.MemoryStream(bs);
            data.SetData("Preferred DropEffect", ms);
            Clipboard.SetDataObject(data);
        }

        internal void Paste() {
            try {
                var dde = Util.GetPreferredDropEffect(Clipboard.GetDataObject());
                if (Clipboard.ContainsFileDropList()) {
                    var files = Clipboard.GetFileDropList();
                    string[] fary = new string[files.Count];
                    files.CopyTo(fary, 0);
                    if (dde == (DragDropEffects.Copy | DragDropEffects.Link)) {
                        Util.ShellFileCopy(this.Handle, fary, this.Dir);
                    }
                    else if (dde == DragDropEffects.Move) {
                        Util.ShellFileMove(this.Handle, fary, this.Dir);
                    }
                }
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }

        internal void Delete() {
            try {
                var files = new List<string>();
                var selfiles = SelectedItemList;
                foreach (var item in selfiles) {
                    files.Add(System.IO.Path.Combine(this.Dir, item.Name));
                }
                Util.ShellFileDelete(this.Handle, files.ToArray());
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }

        internal void Reload() {
            LoadDir(this.Dir);
        }

        FileItem lFileItem = null;

        void fileSystemWatcher1_Changed(object sender, FileSystemEventArgs e) {
            switch (e.ChangeType) {
                case System.IO.WatcherChangeTypes.Changed:
                    Console.WriteLine("ファイル 「" + e.FullPath + "」が変更されました。");
                    //if (File.Exists(e.FullPath)) {
                    //    FileItem fi = null;
                    //    var info = new FileInfo(e.FullPath);
                    //    if (lFileItem != null && lFileItem.Name.Equals(e.Name)) {
                    //        fi = lFileItem;
                    //    }
                    //    else if ((listView1.SelectedIndices.Count > 0 && Items[listView1.SelectedIndices[0]].Name.Equals(e.Name))) {
                    //        fi = Items[listView1.SelectedIndices[0]];
                    //    }else{
                    //        fi = Items.First(x => {
                    //            return x.Name.Equals(e.Name);
                    //        });
                    //    }
                    //    if (fi != null) {
                    //        fi.Size = info.Length;
                    //        fi.LastWriteTime = info.LastWriteTime; 
                    //        var index = Items.IndexOf(fi);
                    //        listView1.RedrawItems(index, index, false);
                    //    }
                    //    lFileItem = fi;
                    //}
                    break;
                case System.IO.WatcherChangeTypes.Created:
                    var fitem = new FileItem();
                    fitem.Name = e.Name;
                    if (File.Exists(e.FullPath)) {
                        //var info = new FileInfo(e.FullPath);
                        fitem.IsFile = true;
                        fitem.type = e.Name.Substring(e.Name.IndexOf("."));
                        //fitem.Size = info.Length;
                        //fitem.LastWriteTime = info.LastWriteTime;
                    }
                    else {
                        //var info = new DirectoryInfo(e.FullPath);
                        fitem.IsFile = false;
                        fitem.type = "/";
                        //fitem.LastWriteTime = info.LastWriteTime;
                    }
                    lFileItem = fitem;
                    Items.Add(fitem);
                    listView1.VirtualListSize = Items.Count;
                    Console.WriteLine("ファイル 「" + e.FullPath + "」がCreatedされました。");
                    break;
                case System.IO.WatcherChangeTypes.Deleted:
                    Console.WriteLine("ファイル 「" + e.FullPath + "」が削除されました。");
                    Items.Remove(Items.First(x=>{
                        return x.Name.Equals(e.Name);
                    }));
                    listView1.VirtualListSize = Items.Count;
                    break;
            }
        }

        internal void Close() {
            if (Closing != null) {
                Closing(this, new EventArgs());
            }
            this.Parent.Controls.Remove(this);
            if (Closed != null) {
                Closed(this, new EventArgs());
            }
        }

        #region IDisposable メンバ

        void IDisposable.Dispose() {
            Win32API.ReleaseDC(hwnd_, dc_);
            Win32API.DeleteObject(hfont_);
        }

        #endregion

        internal class ChangePathEventArgs : EventArgs {
            public string path;
        }
        internal delegate void ChangePathEventHandler(object sender, ChangePathEventArgs e);

        internal event ChangePathEventHandler ChangePath;

        private string _dir;
        public string Dir{
            get { return this._dir; }
            set {
                if (value != null) {
                    if (this._dir == null) {
                        var w = listView1.Width;
                        w -= listView1.Columns["type"].Width;// headerType.Width;
                        w -= listView1.Columns["size"].Width;//headerSize.Width;
                        w -= listView1.Columns["lastwritetime"].Width + 10;//headerLastWriteTime.Width + 10;
                        if (w > 0) {
                            listView1.Columns["name"].Width = w;// headerName.Width = w;
                        }
                    }
                    this._dir = value;
                    textBox1.Text = this._dir;
                    if (ChangePath != null) {
                        ChangePath(this, new ChangePathEventArgs { path = this._dir });
                    }
                    fileSystemWatcher1.EnableRaisingEvents = false;
                    this.LoadDir(this._dir);

                    fileSystemWatcher1.Path = this._dir;
                    fileSystemWatcher1.NotifyFilter = (NotifyFilters.DirectoryName | NotifyFilters.FileName | NotifyFilters.Size);
                    fileSystemWatcher1.EnableRaisingEvents = true;
                    
                }
            }
        }

        internal List<FileItem> SelectedItemList {
            get {
                var ret = new List<FileItem>();
                var indexs = listView1.SelectedIndices;
                for (int i = 0; i < indexs.Count; i++) {
                    ret.Add(Items[indexs[i]]);
                }
                return ret;
            }
        }

        internal List<FileItem> ItemList {
            get { return Items; }
        }
        private List<FileItem> Items = new List<FileItem>();
        //private List<FileItem> viewItems = new List<FileItem>();

        private void LoadDir(string path) {

            listView1.VirtualListSize = 0;
            Items.Clear();

            DateTime s = DateTime.Now;
            //{
            //    var dirs = Directory.GetDirectories(path);
            //    var files = Directory.GetFiles(path);
                
            //    Items.Capacity = dirs.Length + files.Length;
            //    foreach (var f in dirs) {
            //        FileItem item = new FileItem();
            //        FileInfo fi = new FileInfo(System.IO.Path.Combine(path, f));
            //        item.IsFile = false;
            //        item.Name = fi.Name;
            //        Items.Add(item);
            //    }
            //    foreach (var f in files) {
            //        FileItem item = new FileItem();
            //        FileInfo fi = new FileInfo(System.IO.Path.Combine(path, f));
            //        item.IsFile = true;
            //        item.Name = fi.Name;
            //        item.Size = fi.Length;
            //        item.LastWriteTime = fi.LastWriteTime;
            //        Items.Add(item);
            //    }
            //}
            long sum = 0;        
            MaxNameWidth = 0;
            var dirinfo = new DirectoryInfo(path);
            foreach (var item in dirinfo.GetDirectories()) {
                FileItem fitem = new FileItem();
                fitem.IsFile = false;
                fitem.Name = item.Name;
                fitem.type = "/";
                //fitem.NameWidth = GetTextExtend(fitem.Name).width;
                //if (MaxNameWidth < fitem.NameWidth) {
                //    MaxNameWidth = fitem.NameWidth;
                //}
                fitem.LastWriteTime = item.LastWriteTime;
                Items.Add(fitem);
                
            }
            int dircnt = Items.Count;

            foreach (var item in dirinfo.GetFiles()) {
                FileItem fitem = new FileItem();
                fitem.IsFile = true;
                fitem.Name = item.Name;
                fitem.type = item.Extension;
                //fitem.NameWidth = GetTextExtend(fitem.Name).width;
                //if (MaxNameWidth < fitem.NameWidth) {
                //    MaxNameWidth = fitem.NameWidth;
                //}
                fitem.Size = item.Length;
                fitem.LastWriteTime = item.LastWriteTime;
                Items.Add(fitem);
                sum += fitem.Size; 
            }
            int filecnt = Items.Count-dircnt;

            //toolStripStatusLabel1.Text = (DateTime.Now - s).TotalMilliseconds.ToString();
            toolStripStatusLabel1.Text = string.Format("Objects {0}(Dir {1}, Files {2})", Items.Count.ToString(), dircnt, filecnt); ;
            toolStripStatusLabel2.Text = getFileSizeFormat(sum);
            listView1.VirtualListSize = Items.Count;
        }

        private string getFileSizeFormat(long size) {
            string KBSize = string.Format("{0:N0} KB", size / 1024);
            return KBSize;
        }

        internal void UpDir() {
            var u = UpDirPath(this.Dir);
            if (!u.Equals(this.Dir)) {
                this.Dir = u;
            }
        }

        private string UpDirPath(string path) {
            if (!path.EndsWith(@"\")) {
                path += @"\";
            }
            Uri u1 = new Uri(path);
            Uri u2 = new Uri(u1, @"..\");

            return u2.LocalPath;
        }

        private void toolStripButton1_Click(object sender, EventArgs e) {
            UpDir();
        }
    }
}
