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
        private TagDB tagdb = new TagDB();

        public MainForm()
        {
            InitializeComponent();

            AddTagMenuItem.Click += (sender, e) =>
            {
                
                MessageBox.Show(
                tagdb.insertTag

            };

            tagdb.DBFileName = "file.db";
            tagdb.FileTable = "filetable";
            tagdb.TaggedFileTable = "taggedfiletable";
            tagdb.TagTable = "tagtable";
            tagdb.Connection();
            tagdb.createTable();


            string[] tags = tagdb.getAllTags();
            TreeNode node = TagTreeView.Nodes["TagNode"];
            foreach (string tag in tags)
            {
                TreeNode tagnode = node.Nodes.Add(tag);
                tagnode.Tag = tag;
            }
        }


        private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            tagdb.Dispose();
        }

        private TabControl getTabControl()
        {
            return this.tabControl2;
        }

        //private string getFileDbName()
        //{
        //    return "file.db";
        //}

  

        //public void createFileTable(string filename)
        //{
        //    if (!new FileInfo(filename).Exists)
        //    {
        //        using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + getFileDbName()))
        //        using (SQLiteCommand cmd = cnn.CreateCommand())
        //        {
        //            cnn.Open();
        //            cmd.CommandText = String.Format("CREATE TABLE {0} (ID INTEGER PRIMARY KEY AUTOINCREMENT, guid TEXT, name TEXT, comment TEXT)", getFileDbName());
        //            cmd.ExecuteNonQuery();
        //            cnn.Close();
        //        }
        //    }
        //}

        //public void insert(string[] fullpaths)
        //{

        //    using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + getFileDbName()))
        //    using (SQLiteCommand cmd = cnn.CreateCommand())
        //    {
        //        cnn.Open();

        //        SQLiteTransaction transaction = cnn.BeginTransaction();
        //        foreach (string fullpath in fullpaths)
        //        {
        //            String filename = Path.GetFileName(fullpath);
        //            string guid = Win32.getObjectID(fullpath).ToString();
        //            bool res = hasData(cmd, guid);
        //            string strcmd = String.Format("INSERT INTO {0}(guid,name) VALUES('{1}', '{2}')",
        //                getFileDbName(), guid, filename);
        //            cmd.CommandText = strcmd;
        //            //cmd.ExecuteNonQuery();
        //        }
        //        transaction.Commit();
        //        transaction.Dispose();
        //        transaction = null;

        //        cnn.Close();
        //    }
        //}

        //public List<ListViewItem> select(string key)
        //{
        //    List<ListViewItem> co = new List<ListViewItem>();

        //    using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + getFileDbName()))
        //    using (SQLiteCommand cmd = cnn.CreateCommand())
        //    {
        //        cnn.Open();

        //        SQLiteTransaction transaction = cnn.BeginTransaction();

        //        string strcmd = String.Format("select * from {0} where name like '%{1}%'",getFileDbName(), key);
        //        cmd.CommandText = strcmd;
        //        //cmd.ExecuteNonQuery();
        //        using (SQLiteDataReader reader = cmd.ExecuteReader())
        //        {
        //            while (reader.Read())
        //            {
                        
        //                //Console.WriteLine(String.Format("ID = {0}, MyValue = {1}", reader[0], reader[1]));
                        
        //                //string path = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(reader[0].ToString()));
        //                //Console.WriteLine(String.Format("path = {0}", path));
        //               // string[] item1 = { reader[0], reader[1] };
        //                ListViewItem item = new ListViewItem(reader[1].ToString());
        //                item.Tag = reader[0].ToString();
        //                //listView1.Items.Add(item);
                        
        //                co.Add(item);
        //            }

        //        }
        //        transaction.Commit();
        //        transaction.Dispose();
        //        transaction = null;

        //        cnn.Close();
        //    }

        //    return co;
        //}

        //public bool hasData(SQLiteCommand cmd, string guid)
        //{
        //    //bool res = false;

        //    //string strcmd = String.Format("SELECT * FROM name WHERE guid = '{0}' AND rowid <= 1", guid);
        //    string strcmd = String.Format("SELECT COUNT({0}) FROM {1} WHERE guid = '{0}'", guid, getFileDbName());
        //    cmd.CommandText = strcmd;
        //    return !cmd.ExecuteScalar().Equals(0);
        //    //using (SQLiteDataReader reader = cmd.ExecuteReader())
        //    //{
        //    //    res = reader.Read();
        //    //}

        //    //return res;

        //}

        //private List<string> DoIt(string dir, Action<string> func)
        private void DoIt(string dir, Action<string> func)
        {
            //List<string> list = new List<string>();

            Queue<string> q = new Queue<string>();
            q.Enqueue(dir);

            while (q.Count > 0)
            {
                string d = q.Dequeue();

                string[] files = Directory.GetFiles(d);
                foreach (string s in files)
                {
                    //list.Add(s);
                    //Console.WriteLine(s);
                    func(s);
                }

                string[] dirs = Directory.GetDirectories(d);
                foreach (string s in dirs)
                {
                    //list.Add(s);
                    func(s);
                    //Console.WriteLine(s);
                    q.Enqueue(s);
                }
            }

            //return list;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            //createFileTable(getFileDbName());
        }

        private void Form1_DragDrop(object sender, DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop))
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

                string[] fullpaths = (string[])e.Data.GetData(DataFormats.FileDrop);
                foreach (string fullpath in fullpaths)
                {

                    //string strcmd = String.Format("INSERT INTO {0}(guid,name) VALUES('{1}', '{2}')",
                    //    getFileDbName(), guid, filename);
                    if (File.GetAttributes(fullpath) == FileAttributes.Directory)
                    {
                        DoIt(fullpath, func);
                    }
                    else
                    {
                        String filename = Path.GetFileName(fullpath);
                        string guid = Win32.getObjectID(fullpath).ToString();
                        //files.Add(fullpath);
                        files.Add(new FileData(guid, filename, new List<string>(), ""));
                    }
                    
                }
                string[] oldtags = tagdb.getAllTags();
                RegisterForm reg = new RegisterForm();
                reg.FileDatas = files;
                reg.Tags = tagdb.getAllTags();
                reg.SetFileData();
                DialogResult res = reg.ShowDialog(this);
                if (res == DialogResult.OK)
                {
                    tagdb.insertFileData(reg.FileDatas, reg.Tags.ToList<string>());
                }

                //IEnumerable<string> addtags = oldtags.Intersect(tagdb.getTags());
                IEnumerable<string> addtags = tagdb.getAllTags().Except(oldtags);
                foreach (string tag in addtags)
                {
                    TreeNode node = TagTreeView.Nodes["TagNode"];
                    node.Nodes.Add(tag);
                }

            }
        }

        private void Form1_DragEnter(object sender, DragEventArgs e)
        {
            e.Effect = DragDropEffects.All;
        }

        private void comboBox1_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == '\r')
            {
                e.Handled = true;

                // List<ListViewItem> co = select(NameComboBox.Text);
                //TabPage newtab = new TabPage(NameComboBox.Text);
                //tabControl2.TabPages.Add(newtab);
                ////ListVewUserControl listview = new ListVewUserControl();
                //ListView listview = new ListView();
                //listview.View = View.Details;
                //ColumnHeader header1 = new ColumnHeader();
                //header1.Text = "name";
                //listview.Columns.Add(header1);
                //listview.FullRowSelect = true;

                //listview.DoubleClick += delegate
                //{
                //    ListViewItem selitem = listview.SelectedItems[0];
                //    string guid = selitem.Tag.ToString();

                //    string fullpath = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
                //    MessageBox.Show(fullpath);
                //};
               
                //listview.Dock = DockStyle.Fill;
                //newtab.Controls.Add(listview);
                //tabControl2.SelectedTab = newtab;

                //foreach (ListViewItem item in co)
                //{
                //    listview.Items.Add(item);
                //}
            }
        }

        private void listView1_DoubleClick(object sender, EventArgs e)
        {
            //if (listView1.SelectedItems.Count == 1)
            //{
            //    ListViewItem selitem = listView1.SelectedItems[0];
            //    string guid = selitem.Tag.ToString();

            //    string fullpath = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
            //     MessageBox.Show(fullpath);
            //}
        }

 

        private void TagTreeView_DragEnter(object sender, DragEventArgs e)
        {

        }

        private void TagTreeView_DragDrop(object sender, DragEventArgs e)
        {

        }

        private void TagTreeView_DragOver(object sender, DragEventArgs e)
        {
            
        }

        private void TagTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
        {
            if (e.Node.Tag != null)
            {
                if (e.Button == MouseButtons.Left)
                {
                    var listview = getActiveListView();
                    listview.Items.Clear();
                    UpdateListView(listview, tagdb.selectFileData(new string[] { (string)e.Node.Tag }));
                }
                else if (e.Button == MouseButtons.Middle)
                {
                    OpenNewTab(tagdb.selectFileData(new string[] { (string)e.Node.Tag }));
                }
            }
        }

        private void TagTreeView_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
        {

        }

        private ListView CreateListView()
        {
            ListView listview = new ListView();
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

            listview.DoubleClick += delegate
            {
                ListViewItem selitem = listview.SelectedItems[0];
                string guid = selitem.Tag.ToString();

                string fullpath = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
                MessageBox.Show(fullpath);
            };

            listview.Dock = DockStyle.Fill;

            return listview;
        }

        private ListViewItem CreateItem(FileData filedata)
        {
            var item = new ListViewItem(new string[] { filedata.name, filedata.getTagsConcat(), filedata.comment });
            item.Tag = filedata.guid;

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

        private TabPage OpenNewTab(List<FileData> filedatas)
        {
            var listview = CreateListView();
            listview.Tag = filedatas;

            filedatas.ForEach(file =>
            {
                ListViewItem item = CreateItem(file);
                listview.Items.Add(item);
            });


            var newtabpage = new TabPage(NameComboBox.Text);
            getTabControl().TabPages.Add(newtabpage);
            newtabpage.Controls.Add(listview);
            getTabControl().SelectedTab = newtabpage;

            return newtabpage;
        }

        private ListView getActiveListView()
        {
            if (getTabControl().TabPages.Count == 0)
            {
                return CreateListView();
            }
            else
            {
                return (ListView)getTabControl().TabPages[getTabControl().SelectedIndex].Controls["listview"];
            }
        }
    }


}
