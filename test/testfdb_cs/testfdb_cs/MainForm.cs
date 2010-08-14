using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.IO;
using System.Data.SQLite;

namespace testfdb_cs
{
    public partial class MainForm : Form
    {
        private DetailView detailview;

        private TagDB tagdb = new TagDB();
      
        private Dictionary<TreeNode, string> nodemap = new Dictionary<TreeNode, string>();
        private Dictionary<TabPage, int> tabItemWidth = new Dictionary<TabPage, int>();
        private Dictionary<TabPage, ListView> tabListviewMap = new Dictionary<TabPage, ListView>();

        public MainForm()
        {
            InitializeComponent();

            setDetailView();

            AddTagMenuItem.Click += (sender, e) =>
            {    
                InputForm input = new InputForm();
                DialogResult res = input.ShowDialog(this);
                if(res == DialogResult.OK){
                    string tag = input.input;
                    if (tag.Length > 0) {
                        //tagdb.insertTag(new string[] { input.input });
                        IEnumerable<string> newtags = insertTags(new string[] { input.input });

                        foreach (string newtag in newtags)
                        {
                            TreeNode tagnode = TagTreeView.Nodes["TagNode"].Nodes.Add(newtag);
                            tagnode.Tag = newtag;
                        }
                    }
                }
            };

            tagdb.Connection();
            tagdb.createTable();

            sqlitewrap.Connection();

            //string[] tags = tagdb.getAllTags();
            IEnumerable<string> tags = getAllTags();
            TreeNode node = TagTreeView.Nodes["TagNode"];
            foreach (string tag in tags)
            {
                TreeNode tagnode = node.Nodes.Add(tag);
                tagnode.Tag = tag;
            }

            getTabControl().Selecting += (sender, e) =>
            {
                if(tabListviewMap.ContainsKey(e.TabPage))
                {
                    ListViewPanel.Controls.Remove(tabListviewMap[e.TabPage]);
                }
            };

            getTabControl().Selected += (sender, e) =>
            {
                var control = ListViewPanel.Controls[0];
                if (control !=null) ListViewPanel.Controls.Remove(control);

                if (tabListviewMap.ContainsKey(e.TabPage))
                {
                    ListViewPanel.Controls.Add(tabListviewMap[e.TabPage]);
                }                
            };

            SearchComboBox.KeyPress += (sender, e) =>
            {
                if (e.KeyChar == '\r')
                {
                    e.Handled = true;
                    string text = SearchComboBox.Text;
                    if (text.IndexOf(",") == -1)
                    {

                    }
                    else
                    {
                        string[] strs = text.Split(',');

                    }
                    //OpenNewTab();
                }
            };

        }

        private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            tagdb.Dispose();
        }

        private TabControl getTabControl()
        {
            return this.tabControl2;
        }

        private Size resizeTabBarSize(TabControl tabControl)
        {
            int tabH = tabControl.ItemSize.Height;
            int controlW = tabControl.Width;
            int newH = tabH;
            int sumW = 0;
            foreach (TabPage tabpage in tabControl.TabPages)
            {
                sumW += tabItemWidth[tabpage];
                if (sumW > controlW)
                {
                    newH += tabH;
                    sumW = 0;
                }
            }

            return new Size(controlW, newH);
        }

        private void setDetailView()
        {
            var panel = ViewSplitContainer.Panel2;
            detailview = new DetailView();
            detailview.OnChagedName += (sender, args) =>
            {
                var listview = getActiveListView();
                if (listview == null) return;

                tagdb.updateFileDataName(args.filedata.guid, args.filedata.name);
                updataListviewItem(getActiveListView(), args.filedata);
            };
            detailview.OnChagedComment += (sender, args) =>
            {
                var listview = getActiveListView();
                if (listview == null) return;

                tagdb.updateFileDataComment(args.filedata.guid, args.filedata.comment);
                updataListviewItem(getActiveListView(), args.filedata);
            };
            detailview.Dock = DockStyle.Fill;
            panel.Controls.Add(detailview);
        }

        private void updataListviewItem(ListView listview, FileData filedata)
        {         
            foreach (int index in selectedIndexQueue)
            {
                if (listview.SelectedItems.Count > index)
                {
                    ListViewItem selecteditem = listview.SelectedItems[index];
                    FileData selectedfiledata = selecteditem.Tag as FileData;
                    if (selectedfiledata != null && filedata.guid == selectedfiledata.guid)
                    {
                        setItemData(selecteditem, filedata);
                        break;
                    }
                }
            }
        }

        private void setItemData(ListViewItem item, FileData filedata)
        {
            item.SubItems[0].Text = filedata.name;
            item.SubItems[1].Text = filedata.getTagsConcat();
            item.SubItems[2].Text = filedata.comment;
        }

        private void DoIt(string dir, Action<string> func)
        {
            Queue<string> q = new Queue<string>();
            q.Enqueue(dir);

            while (q.Count > 0)
            {
                string d = q.Dequeue();

                string[] files = Directory.GetFiles(d);
                foreach (string s in files)
                {
                    func(s);
                }

                string[] dirs = Directory.GetDirectories(d);
                foreach (string s in dirs)
                {
                    func(s);
                    q.Enqueue(s);
                }
            }
        }

        private List<FileData> getFileData(string[] fullpaths)
        {
            List<FileData> files = new List<FileData>();

            Action<string> func = x =>
            {
                String name = Path.GetFileName(x);
                string guid = Win32.getObjectID(x).ToString();
                FileData filedata = tagdb.selectFileData(guid);
                if (filedata == null)
                {
                    filedata = new FileData(guid, name, new List<string>(), "");
                }
                files.Add(filedata);
            };

            foreach (string fullpath in fullpaths)
            {
                if (File.GetAttributes(fullpath) == FileAttributes.Directory)
                {
                    DoIt(fullpath, func);
                }
                else
                {
                    String filename = Path.GetFileName(fullpath);
                    string guid = Win32.getObjectID(fullpath).ToString();
                    files.Add(new FileData(guid, filename, new List<string>(), ""));
                }

            }
            return files;
        }

        private void RegisterTagsComent(List<FileData> files)
        {
            RegisterForm reg = new RegisterForm();
            reg.FileDatas = files;
            reg.Tags = tagdb.getAllTags();
            reg.SetFileData();
            DialogResult res = reg.ShowDialog(this);
            if (res == DialogResult.OK)
            {
                tagdb.insertFileData(reg.FileDatas, reg.Tags.ToList<string>());
            }
        }

        private void TagTreeView_DragEnter(object sender, DragEventArgs e)
        {
            e.Effect = DragDropEffects.All;
        }

        private void TagTreeView_DragDrop(object sender, DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop)) {
                var pt = ((TreeView)sender).PointToClient(new Point(e.X, e.Y));
                TreeNode DestinationNode = ((TreeView)sender).GetNodeAt(pt);

                if (DestinationNode != null) {
                    string tasg = DestinationNode.Tag as string;
                    if (tasg != null){// && nodemap.ContainsKey(DestinationNode)) {
                        string[] fullpaths = (string[])e.Data.GetData(DataFormats.FileDrop);

                        List<FileData> files = getFileData(fullpaths);

                        tagdb.insertFileData(files, new List<string> { tasg });
                    }
                }
            }
        }

        private void TagTreeView_DragOver(object sender, DragEventArgs e)
        {
            var pt = ((TreeView)sender).PointToClient(new Point(e.X, e.Y));
            TreeNode DestinationNode = ((TreeView)sender).GetNodeAt(pt);

            if (DestinationNode != null) {
                ((TreeView)sender).SelectedNode = DestinationNode;
            } 
        }

        private Queue<int> selectedIndexQueue = new Queue<int>(2);

        private ListView CreateListView()
        {
            ListView listview = new ListView();
            listview.SelectedIndexChanged += (object sender, EventArgs e) =>
            {
                if (listview.SelectedIndices.Count > 0)
                {
                    var index = listview.SelectedIndices[0];
                    selectedIndexQueue.Enqueue(index);

                    ListViewItem item = listview.SelectedItems[0];
                    FileData filedata = item.Tag as FileData;
                    detailview.filedata = filedata;
                }
            };

            listview.HideSelection = false;
            listview.Name = "listview";

            listview.View = View.Details;
            ColumnHeader header1 = new ColumnHeader();
            header1.Text = "name";
            listview.Columns.Add(header1);

            ColumnHeader header2 = new ColumnHeader();
            header2.Text = "tags";
            listview.Columns.Add(header2);

            ColumnHeader header3 = new ColumnHeader();
            header3.Text = "comment";
            listview.Columns.Add(header3);

            listview.FullRowSelect = true;

            listview.Dock = DockStyle.Fill;

            return listview;
        }

        private ListViewItem CreateItem(FileData filedata)
        {
            var item = new ListViewItem(new string[] { filedata.name, filedata.getTagsConcat(), filedata.comment });
            item.Tag = filedata;// filedata.guid;

            return item;
        }

        private void UpdateListView(ListView listview, List<FileData> filedatas)
        {
            filedatas.ForEach(file =>
            {
                ListViewItem item = CreateItem(file);
                listview.Items.Add(item);
            });
        }

        private void UpdateListView(ListView listview, List<FileData> filedatas, Func<FileData, bool> func)
        {
            var filte = filedatas.FindAll(x => func(x));//x => x.Length == 5);
            filte.ForEach(file =>
            {
                ListViewItem item = CreateItem(file);
                listview.Items.Add(item);
            });
        }

        private TabPage OpenNewTab(List<FileData> filedatas, string text)
        {
            var listview = CreateListView();
            listview.Tag = filedatas;

            filedatas.ForEach(file =>
            {
                ListViewItem item = CreateItem(file);
                listview.Items.Add(item);
            });

            var tabcontrol = getTabControl();
            var newtabpage = addTagPage(tabcontrol, text);
            newtabpage.Text = text;

            tabListviewMap.Add(newtabpage, listview);
            ListViewPanel.Controls.Add(listview);

            return newtabpage;
        }

        private ListView getActiveListView()
        {
            if (getTabControl().TabPages.Count == 0)
            {
                return null;
            }
            else
            {
                var tabpage = getTabControl().SelectedTab;
                return tabListviewMap[tabpage];
            }
        }

        private void TagTreeView_MouseDown(object sender, MouseEventArgs e)
        {
            var pt = new Point(e.X, e.Y);
            TreeNode DestinationNode = ((TreeView)sender).GetNodeAt(pt);
            ((TreeView)sender).SelectedNode = DestinationNode;

            TreeNode selnode = ((TreeView)sender).SelectedNode;
            if (selnode != null && selnode.Tag != null)
            {
                if (e.Button == MouseButtons.Left)
                {
                    var listview = getActiveListView();
                    if (listview == null)
                    {
                        OpenNewTab(tagdb.selectFileData(new string[] { (string)selnode.Tag }), (string)selnode.Tag);
                    }
                    else
                    {
                        getTabControl().SelectedTab.Text = (string)selnode.Tag;
                        listview.Items.Clear();
                        UpdateListView(listview, tagdb.selectFileData(new string[] { (string)selnode.Tag }));
                    }
                }
                else if (e.Button == MouseButtons.Middle)
                {
                    OpenNewTab(tagdb.selectFileData(new string[] { (string)selnode.Tag }), (string)selnode.Tag);
                }
            } 
        }

        private TabPage addTagPage(TabControl tabcontrol, string text)
        {
            var newtabpage = new TabPage(text);
            tabcontrol.TabPages.Add(newtabpage);

            tabItemWidth.Add(newtabpage, CalcStringWidth(newtabpage.Text));
            var newsize = resizeTabBarSize(tabcontrol);
            tabcontrol.Height = newsize.Height;

            return newtabpage;
        }

        private TabPage removeTabPage(TabControl tabcontrol, TabPage tabpage)
        {
            tabItemWidth.Remove(tabpage);
            tabcontrol.TabPages.Remove(tabpage);
            
            var newsize = resizeTabBarSize(tabcontrol);
            tabcontrol.Height = newsize.Height;

            return tabpage;
        }

        private int CalcStringWidth(string text)
        {
            if (getTabControl().SizeMode == TabSizeMode.Fixed)
            {
                return getTabControl().ItemSize.Width;
            }
            return getTabControl().ItemSize.Width;
        }
    }
}
