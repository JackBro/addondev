using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace MF {

    public partial class UserControl1 : UserControl, IDisposable {
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

        private int MaxNameWidth;
        private int DateWidth;
        private string DateFormat;

        public UserControl1() {
            InitializeComponent();

            this.DoubleBuffered = true;

            hwnd_ = listView1.Handle;
            dc_ = Win32API.GetDC(hwnd_);
            hfont_ = listView1.Font.ToHfont();

            listView1.OwnerDraw = true;
            listView1.VirtualMode = true;
            listView1.VirtualListSize = 0;
            listView1.FullRowSelect = true;
            listView1.View = View.Details;

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
                    listviewitem.SubItems.Add(item.LastWriteTime.ToString("yyyy/MM/dd HH:mm:ss"));
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
                //} else {
                //    drawFormat.Alignment = StringAlignment.Near;
                //}

                //System.Drawing.Brush brush;

                //// Forcus = 0x0010,Selected = 0x0001 として ItemStateにForcusとSelectedがセットされていた場合、
                //// 2bitで書くと Selected = 0001 0000 ItemState = 0000 0001で
                //// この二つ論理積を取ると0000 0001(0x0001 = Selected)となる。
                ////if ((e.ItemState & ListViewItemStates.Selected) == ListViewItemStates.Selected) {
                //if (listView1.SelectedIndices.Contains(e.ItemIndex)) {
                //    // Hightlightで範囲を塗りつぶす
                //    e.Graphics.FillRectangle(SystemBrushes.Highlight, e.Bounds);
                //    // 上でセルを塗りつぶしているので、表示する文字を反転する
                //    brush = SystemBrushes.HighlightText;
                //} else {
                //    //if (!Items[e.ItemIndex].IsFile) {
                //    //    brush = SystemBrushes.HighlightText;
                //    //} else {
                //        // 塗りつぶされていない通常のセルはWindowsTextに設定する
                //        brush = SystemBrushes.WindowText;
                //    //}
                //}

                //// 上で設定した,brushとdrawFormatを利用して文字を描画する
                //e.Graphics.DrawString(e.SubItem.Text, e.Item.Font, brush, e.Bounds, drawFormat);

                //// drawFormatを開放する
                //drawFormat.Dispose();

                TextFormatFlags flg;
                if (e.ColumnIndex == 0) {
                    flg = TextFormatFlags.EndEllipsis;
                }else if (e.ColumnIndex == 2) {
                    flg = TextFormatFlags.Right;
                }
                else {
                    flg = TextFormatFlags.Left;
                }
                Color brush;
                if (listView1.SelectedIndices.Contains(e.ItemIndex)) {
                    e.Graphics.FillRectangle(SystemBrushes.Highlight, e.Bounds);
                    brush = SystemColors.HighlightText;
                }
                else {
                    brush = SystemColors.WindowText;
                }
                Rectangle r = new Rectangle(e.Bounds.Location, new Size(listView1.Columns[e.ColumnIndex].Width, e.Bounds.Height));
                TextRenderer.DrawText(e.Graphics, e.SubItem.Text, e.Item.Font, r, brush, flg);
            };

            listView1.DrawColumnHeader += (sender, e) => {
                e.DrawDefault = true;
            };


            textBox1.KeyDown +=(sender, e)=>{
                if (e.KeyCode == Keys.Return) {
                    Path = textBox1.Text;
                }
            };

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

        private string _path;
        public string Path{
            get { return this._path; }
            set {
                if (value != null) {
                    this._path = value;
                    textBox1.Text = this._path;
                    if (ChangePath != null) {
                        ChangePath(this, new ChangePathEventArgs { path = this._path });
                    }
                    this.LoadDir(this._path);
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

            MaxNameWidth = 0;
            {
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
                }
            }

            toolStripStatusLabel1.Text = (DateTime.Now - s).TotalMilliseconds.ToString();
            listView1.VirtualListSize = Items.Count;
        }

        private string getFileSizeFormat(long size) {
            string KBSize = string.Format("{0} KB", size / 1024);
            return KBSize;
        }

        internal void MoveUp() {
            var u = UpDirPath(this.Path);
            if (!u.Equals(this.Path)) {
                this.Path = u;
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
            MoveUp();
        }
    }
}
