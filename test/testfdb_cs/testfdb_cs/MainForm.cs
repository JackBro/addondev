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
using System.Diagnostics;
using System.Collections;
using System.Linq.Expressions;

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
                        //IEnumerable<string> newtags = insertTags(new string[] { input.input });
                        string[] newtags = tag.Split(' '); 
                        foreach (string newtag in newtags)
                        {
                            TreeNode tagnode = TagTreeView.Nodes["TagNode"].Nodes.Add(newtag);
                            tagnode.Tag = newtag;
                        }
                    }
                }
            };

            ToolStripMenuItem openfolder = new ToolStripMenuItem("open folder");
            openfolder.Click += (sender, e) => {
                var listview = getActiveListView() as FileListView<TableData>;
                var datas = listview.getSelectItemData();
                if (datas.Count() > 0) {
                    string guid = datas.ElementAt(0).guid;
                    string path = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
                    
                    //ProcessStartInfo psi = new ProcessStartInfo();
                    
                    //FileInfo info = new FileInfo(path);
                    //psi.FileName = info.Directory.FullName;
                    //psi.Verb = "open";
                    //System.Diagnostics.Process.Start(psi);
                    System.Diagnostics.Process.Start("EXPLORER.EXE", @"/select," + path);

                }
            };
            ListViewContextMenu.Items.Add(openfolder);

            ToolStripMenuItem openitem = new ToolStripMenuItem("open");
            openitem.Click += (sender, e) =>
            {
                var listview = getActiveListView() as FileListView<TableData>;
                var datas = listview.getSelectItemData();
                if (datas.Count() > 0) {
                    string guid = datas.ElementAt(0).guid;
                    string path = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
                    System.Diagnostics.Process.Start("Notepad", path);
                }
            };
            ListViewContextMenu.Items.Add(openitem);

            //tagdb.Connection();
            //tagdb.createTable();

            //sqlitewrap.Connection();
            createTable();

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

                    var tabpage = OpenNewTab(text);
                    var listview = tabListviewMap[tabpage];
                    if (text.IndexOf(",") == -1)
                    {
                        var values = text.Split(' ', '　');

                        var datas = new List<TableData>();

                        using (FileDataModelContainer db = new FileDataModelContainer()) {
                            //db.FileTable.Where(c => c.ext == "ok").Select(s => s);
                            //var query = from c in db.FileTable
                            //            where c.comment.Contains(text) || c.name.Contains(text) || c.TagTable.Any(t => t.tag.Contains(text))
                            //            select c;
                            //foreach (FileTable f in query) {
                            //    string stag = String.Empty;

                            //    var tagquery = from c in db.TagTable
                            //                   where c.FileTable.filetableid == f.filetableid
                            //                   select c.tag;

                            //    datas.Add(new TableData(f.guid, f.name, f.size, f.ext, tagquery.ToList<string>(), f.comment, f.creationtime, f.lastwritetime));
                            //}
                            string[] targets = {"name","comment","ext"};
                            var query = from c in db.FileTable
                                        select c;
                            var contains = typeof(string).GetMethod("Contains");
                            var paramExpr = Expression.Parameter(typeof(FileTable), "c");
                            Expression mainExpr = null;
                            
                            foreach (string target in targets)
                            {
                                Expression bodyExpr = null;
                                foreach (var o in values)
                                {
                                    if (o.Length == 0) continue;

                                    if (bodyExpr == null)
                                    {
                                        // d.FileName.Contains("値")のコードと等価
                                        bodyExpr = Expression.Call(
                                            Expression.Property(paramExpr, target), contains, Expression.Constant(o)
                                        );

                                    }
                                    else
                                    {
                                        // 既に式があればOR演算する
                                        bodyExpr = Expression.AndAlso(
                                            bodyExpr,
                                            Expression.Call(
                                                Expression.Property(paramExpr, target), contains, Expression.Constant(o)
                                            )
                                        );
                                    }
                                }
                                if (mainExpr == null)
                                {
                                    mainExpr = bodyExpr;
                                }
                                else
                                {
                                    mainExpr = Expression.OrElse(mainExpr, bodyExpr);
                                }
                            }

                            if(values.Length != 0){

                                var res = query.Where(Expression.Lambda<Func<FileTable, bool>>(mainExpr, paramExpr));

                                foreach (FileTable f in res)
                                {
                                    string stag = String.Empty;

                                    var tagquery = from c in db.TagTable
                                                   where c.FileTable.filetableid == f.filetableid
                                                   select c.tag;

                                    datas.Add(new TableData(f.guid, f.name, f.size, f.ext, tagquery.ToList<string>(), f.comment, f.creationtime, f.lastwritetime));
                                }

                            }

                        }

                        FileListView<TableData> filelistview = listview as FileListView<TableData>;
                        filelistview.inputData(datas);
                        filelistview.setitem();
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
                updataListviewItem(getActiveListView(), args.guid, args.key, args.data);

                using (FileDataModelContainer db = new FileDataModelContainer())
                {
                    var query = from c in db.FileTable
                                where c.guid == args.guid
                                select c;
                    foreach (FileTable ftable in query)
                    {
                        ftable.name = args.data;
                    }

                    db.SaveChanges();
                }
            };
            detailview.OnChagedComment += (sender, args) =>
            {
                updataListviewItem(getActiveListView(), args.guid, args.key, args.data);

                using (FileDataModelContainer db = new FileDataModelContainer())
                {
                    var query = from c in db.FileTable
                                where c.guid == args.guid
                                select c;
                    foreach (FileTable ftable in query)
                    {
                        ftable.comment = args.data;
                    }

                    db.SaveChanges();
                }
            };
            detailview.Dock = DockStyle.Fill;
            panel.Controls.Add(detailview);
        }

        private void updataListviewItem(ListView listview, string guid, string key, string data)
        {         
            foreach (int index in selectedIndexQueue)
            {
                if (listview.Items.Count > index)
                {
                    ListViewItem selecteditem = listview.Items[index];
                    if (guid == ((TableData)selecteditem.Tag).guid) {
                        selecteditem.SubItems[listview.Columns[key].Index].Text = data;
                        break;
                    }
                }
            }
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

        //private List<TableData> getFileData(string[] fullpaths)
        //{
        //    List<TableData> files = new List<TableData>();

        //    Action<string> func = x =>
        //    {
        //        String name = Path.GetFileName(x);
        //        String ext = Path.GetExtension(x);
                 
        //        //FileInfo fileinfo = new FileInfo(x);
        //        //fileinfo.CreationTime;
        //        string guid = Win32.getObjectID(x).ToString();
        //        TableData filedata = tagdb.selectFileData(guid);
        //        if (filedata == null)
        //        {
        //            filedata = new TableData(guid, name, new List<string>(), "");
        //        }
        //        files.Add(filedata);
        //    };

        //    foreach (string fullpath in fullpaths)
        //    {
        //        if (File.GetAttributes(fullpath) == FileAttributes.Directory)
        //        {
        //            DoIt(fullpath, func);
        //        }
        //        else
        //        {
        //            String filename = Path.GetFileName(fullpath);
        //            string guid = Win32.getObjectID(fullpath).ToString();
        //            files.Add(new TableData(guid, filename, new List<string>(), ""));
        //        }

        //    }
        //    return files;
        //}

        private void RegisterTagsComent(List<TableData> files)
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

                        insertFileData(fullpaths, new List<string> { tasg });
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


        private FileListView<TableData> CreateListView()
        {
            var listview = new FileListView<TableData>();
            listview.Name = "listview";
            listview.HideSelection = false;
            listview.View = View.Details;
            listview.FullRowSelect = true;
            listview.ContextMenuStrip = ListViewContextMenu;
            listview.Dock = DockStyle.Fill;

            ColumnHeader headerName = new ColumnHeader();
            headerName.Name = "name";
            headerName.Text = "name";
            listview.Columns.Add(headerName);

            ColumnHeader headerSize = new ColumnHeader();
            headerSize.Name = "size";
            headerSize.Text = "size";
            listview.Columns.Add(headerSize);

            ColumnHeader headerExt = new ColumnHeader();
            headerExt.Name = "ext";
            headerExt.Text = "ext";
            listview.Columns.Add(headerExt);

            ColumnHeader headerTags = new ColumnHeader();
            headerTags.Name = "tags";
            headerTags.Text = "tags";
            listview.Columns.Add(headerTags);

            ColumnHeader headerComment = new ColumnHeader();
            headerComment.Name = "comment";
            headerComment.Text = "comment";
            listview.Columns.Add(headerComment);

            ColumnHeader headerCreationTime = new ColumnHeader();
            headerCreationTime.Name = "creationtime";
            headerCreationTime.Text = "creationtime";
            listview.Columns.Add(headerCreationTime);

            ColumnHeader headerLastWriteTime = new ColumnHeader();
            headerLastWriteTime.Name = "lastwritetime";
            headerLastWriteTime.Text = "lastwritetime";
            listview.Columns.Add(headerLastWriteTime);

            listview.LabelFunc = (data, name) => {
                string ret = string.Empty;
                switch(name){
                    case "name":
                        ret = data.name;
                        break;
                    case "ext":
                        ret = data.ext;
                        break;
                    case "size":
                        ret = data.size.ToString();
                        break;
                    case "tags":
                        ret = data.TagsToString();
                        break;
                    case "comment":
                        ret = data.comment;
                        break;
                    case "creationtime":
                        ret = data.creationtime.ToString("yyyy/MM/dd HH:mm:ss");
                        break;
                    case "lastwritetime":
                        ret = data.lastwritetime.ToString("yyyy/MM/dd HH:mm:ss");
                        break;
                }
                return ret;
            };

            listview.ItemSelectionChanged += (sender, e) =>
            {
               
                if (listview.mySel.Count > 0)
                {
                    selectedIndexQueue.Enqueue(listview.mySel[0]);
                    if (selectedIndexQueue.Count > 2)
                    {
                        selectedIndexQueue.Dequeue();
                    }

                    ListViewItem item = listview.myCol[listview.mySel[0]];
                    detailview.Data = listview.getData(item);
                }
            };

            listview.listViewItemComparer.SortFunc = (x, y, columindex) => {
                int result = 0;
                string columname = listview.Columns[columindex].Name;
                switch (columname) {
                    case "name":
                        result = string.Compare(x.name, y.name);
                        break;
                    case "size":
                        result = x.size-y.size>0?1:-1;
                        break;
                    case "ext":
                        result = string.Compare(x.ext, y.ext);
                        break;
                    case "comment":
                        result = string.Compare(x.comment, y.comment);
                        break;
                    //case "tags":
                    //    result = string.Compare(x.tags, y.tags);
                    //    break;
                    case "creationtime":
                        result = DateTime.Compare(x.creationtime, y.creationtime);
                        break;
                    case "lastwritetime":
                        result = DateTime.Compare(x.lastwritetime, y.lastwritetime);
                        break;
                }
                return result;
            };



            return listview;
        }

        private TabPage OpenNewTab(string text)
        {
            var listview = CreateListView();

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
                    var selecttag = (string)selnode.Tag;
                    var listview = getActiveListView();
                    if (listview == null)
                    {    
                        var tabpage = OpenNewTab(selecttag);
                        listview = tabListviewMap[tabpage];
 
                        //FileListView<TableData> filelistview = listview as FileListView<TableData>;
                        //filelistview.inputData(datas);
                        //filelistview.setitem();
                        //OpenNewTab(tagdb.selectFileData(new string[] { (string)selnode.Tag }), (string)selnode.Tag);
                    }
                    else
                    {
                        getTabControl().SelectedTab.Text = selecttag;

                        //getTabControl().SelectedTab.Text = (string)selnode.Tag;
                        //listview.Items.Clear();
                        //UpdateListView(listview, tagdb.selectFileData(new string[] { (string)selnode.Tag }));
                    }

                    var datas = new List<TableData>();
                    using (FileDataModelContainer db = new FileDataModelContainer()) {
                        //db.FileTable.Where(c => c.ext == "ok").Select(s => s);
                        var query = from c in db.FileTable
                                    where c.TagTable.Any(t => t.tag.Contains(selecttag))
                                    select c;
                        foreach (FileTable f in query) {
                            string stag = String.Empty;

                            var tagquery = from c in db.TagTable
                                           where c.FileTable.filetableid == f.filetableid
                                           select c.tag;
                            
                            datas.Add(new TableData(f.guid, f.name, f.size, f.ext, tagquery.ToList<string>(), f.comment, f.creationtime, f.lastwritetime));
                        }
                    }

                    FileListView<TableData> filelistview = listview as FileListView<TableData>;
                    filelistview.inputData(datas);
                    filelistview.setitem();
                }
                else if (e.Button == MouseButtons.Middle)
                {

                    //OpenNewTab(tagdb.selectFileData(new string[] { (string)selnode.Tag }), (string)selnode.Tag);
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
