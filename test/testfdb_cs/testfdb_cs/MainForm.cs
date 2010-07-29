﻿using System;
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

            tagdb.DBFileName = "file.db";
            tagdb.FileTableName = "filetable";
            tagdb.TagTableName = "tagtable";
            tagdb.Connection();
            tagdb.createTable();


            string[] tags = tagdb.getTags();
            TreeNode node = TagTreeView.Nodes["TagNode"];
            foreach (string tag in tags)
            {
                node.Nodes.Add(tag);
            }
        }


        private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            tagdb.Dispose();
        }

        private string getFileDbName()
        {
            return "file.db";
        }

  

        public void createFileTable(string filename)
        {
            if (!new FileInfo(filename).Exists)
            {
                using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + getFileDbName()))
                using (SQLiteCommand cmd = cnn.CreateCommand())
                {
                    cnn.Open();
                    cmd.CommandText = String.Format("CREATE TABLE {0} (ID INTEGER PRIMARY KEY AUTOINCREMENT, guid TEXT, name TEXT, comment TEXT)", getFileDbName());
                    cmd.ExecuteNonQuery();
                    cnn.Close();
                }
            }
        }

        public void insert(string[] fullpaths)
        {

            using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + getFileDbName()))
            using (SQLiteCommand cmd = cnn.CreateCommand())
            {
                cnn.Open();

                SQLiteTransaction transaction = cnn.BeginTransaction();
                foreach (string fullpath in fullpaths)
                {
                    String filename = Path.GetFileName(fullpath);
                    string guid = Win32.getObjectID(fullpath).ToString();
                    bool res = hasData(cmd, guid);
                    string strcmd = String.Format("INSERT INTO {0}(guid,name) VALUES('{1}', '{2}')",
                        getFileDbName(), guid, filename);
                    cmd.CommandText = strcmd;
                    //cmd.ExecuteNonQuery();
                }
                transaction.Commit();
                transaction.Dispose();
                transaction = null;

                cnn.Close();
            }
        }

        public List<ListViewItem> select(string key)
        {
            List<ListViewItem> co = new List<ListViewItem>();

            using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + getFileDbName()))
            using (SQLiteCommand cmd = cnn.CreateCommand())
            {
                cnn.Open();

                SQLiteTransaction transaction = cnn.BeginTransaction();

                string strcmd = String.Format("select * from {0} where name like '%{1}%'",getFileDbName(), key);
                cmd.CommandText = strcmd;
                //cmd.ExecuteNonQuery();
                using (SQLiteDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        
                        //Console.WriteLine(String.Format("ID = {0}, MyValue = {1}", reader[0], reader[1]));
                        
                        //string path = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(reader[0].ToString()));
                        //Console.WriteLine(String.Format("path = {0}", path));
                       // string[] item1 = { reader[0], reader[1] };
                        ListViewItem item = new ListViewItem(reader[1].ToString());
                        item.Tag = reader[0].ToString();
                        //listView1.Items.Add(item);
                        
                        co.Add(item);
                    }

                }
                transaction.Commit();
                transaction.Dispose();
                transaction = null;

                cnn.Close();
            }

            return co;
        }

        public bool hasData(SQLiteCommand cmd, string guid)
        {
            //bool res = false;

            //string strcmd = String.Format("SELECT * FROM name WHERE guid = '{0}' AND rowid <= 1", guid);
            string strcmd = String.Format("SELECT COUNT({0}) FROM {1} WHERE guid = '{0}'", guid, getFileDbName());
            cmd.CommandText = strcmd;
            return !cmd.ExecuteScalar().Equals(0);
            //using (SQLiteDataReader reader = cmd.ExecuteReader())
            //{
            //    res = reader.Read();
            //}

            //return res;

        }

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
                //createFileTable(getFileDbName());
                //foreach (string fileName in (string[])e.Data.GetData(DataFormats.FileDrop))
                //{
                string[] fullpaths = (string[])e.Data.GetData(DataFormats.FileDrop);
                //insert(files);
                //}

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
                string[] oldtags = tagdb.getTags();
                RegisterForm reg = new RegisterForm();
                reg.FileDatas = files;
                reg.SetAllTags(tagdb.getTags());
                reg.SetFileData();
                DialogResult res = reg.ShowDialog(this);
                if (res == DialogResult.OK)
                {
                    tagdb.insertFiles(reg.FileDatas, reg.Tags.ToList<string>());
                }

                //IEnumerable<string> addtags = oldtags.Intersect(tagdb.getTags());
                IEnumerable<string> addtags = tagdb.getTags().Except(oldtags);
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

                 List<ListViewItem> co = select(NameComboBox.Text);
                TabPage newtab = new TabPage(NameComboBox.Text);
                tabControl2.TabPages.Add(newtab);
                //ListVewUserControl listview = new ListVewUserControl();
                ListView listview = new ListView();
                listview.View = View.Details;
                ColumnHeader header1 = new ColumnHeader();
                header1.Text = "name";
                listview.Columns.Add(header1);
                listview.FullRowSelect = true;

                listview.DoubleClick += delegate
                {
                    ListViewItem selitem = listview.SelectedItems[0];
                    string guid = selitem.Tag.ToString();

                    string fullpath = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
                    MessageBox.Show(fullpath);
                };
               
                listview.Dock = DockStyle.Fill;
                newtab.Controls.Add(listview);
                tabControl2.SelectedTab = newtab;

                foreach (ListViewItem item in co)
                {
                    listview.Items.Add(item);
                }
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

        private void UpdateListView()
        {

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
  
            ListView listview = new ListView();
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

            TabPage newtab = new TabPage(NameComboBox.Text);
            tabControl2.TabPages.Add(newtab);
            newtab.Controls.Add(listview);
            tabControl2.SelectedTab = newtab;

            List<FileData> filedatas = tagdb.selectTags(new string[]{"test"});
            
            foreach (FileData file in filedatas)
            {
                ListViewItem item = new ListViewItem(new string[]{file.name, file.getTagsConcat(), file.comment});
                item.Tag = file.guid;
                listview.Items.Add(item);
            }
        }

        private void TagTreeView_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
        {

        }

    }


}
