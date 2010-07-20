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
    //http://www.adamrocker.com/blog/195/practical_way_of_autocompletetextview_with_sqlite.html
    //http://webcache.googleusercontent.com/search?q=cache:NSovgXJuaKMJ:blog.livedoor.jp/maru_tak/archives/cat_10012124.html+sqlite+ROWNUM&cd=1&hl=ja&ct=clnk&gl=jp&lr=lang_ja&client=firefox-a
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();

        }

        Win32.FILEGUID fuga;
        private void button1_Click(object sender, EventArgs e)
        {
            fuga = new Win32.FILEGUID();
            fuga.Data4 = new Byte[4];
            
            string msg = @"D:\data\src\PDE\xt2howm.rb";
            Boolean rc = Win32.getObjectID(msg, ref fuga);
            if (rc)
            {
                MessageBox.Show(fuga.ToString());
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string path = "";
            //Fuga f = new Fuga();
            //f.id = new Byte[16];
            //Int64 ii = 1287905504378253229;
            //f.id = BitConverter.GetBytes(ii);
            Win32.FILEGUID guid = Win32.FILEGUID.parse("1287905504378253229-60810-3072-118-29-23-147");

            Boolean rc = Win32.getFullPathByObjectID(guid, ref path);
            if (rc)
            {
                MessageBox.Show(path.ToString());
            }
            //DoIt(@"D:\data\src\PDE\workrepository\plugins\test\testfdb_cs");
        }


        private List<string> DoIt(string dir)
        {
            List<string> list = new List<string>();

            Queue<string> q = new Queue<string>();
            q.Enqueue(dir);

            while (q.Count > 0)
            {
                string d = q.Dequeue();

                string[] files = Directory.GetFiles(d);
                foreach (string s in files)
                {
                    list.Add(s);
                    Console.WriteLine(s);
                }

                string[] dirs = Directory.GetDirectories(d);
                foreach (string s in dirs)
                {
                    list.Add(s);
                    Console.WriteLine(s);
                    q.Enqueue(s);
                }
            }

            return list;
        }

        //http://techbank.jp/Community/blogs/poohkid/archive/2009/11/14/22590.aspx
        //http://sites.google.com/site/gsfzero1/
        public void createNameTable(string filename)
        {
            if (!new FileInfo(filename).Exists)
            {
                using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + filename))
                using (SQLiteCommand cmd = cnn.CreateCommand())
                {
                    cnn.Open();
                    cmd.CommandText = "CREATE TABLE name (ID INTEGER PRIMARY KEY AUTOINCREMENT, guid TEXT, name TEXT)";
                    cmd.ExecuteNonQuery();
                    cnn.Close();
                }
            }
        }

        public void insert(string[] fullpaths)
        {
            
            using (SQLiteConnection cnn = new SQLiteConnection("Data Source=name.db"))
            using (SQLiteCommand cmd = cnn.CreateCommand())
            {
                cnn.Open();

                SQLiteTransaction transaction = cnn.BeginTransaction();
                foreach (string fullpath in fullpaths)
                {
                    String filename = Path.GetFileName(fullpath);
                    string guid = Win32.getObjectID(fullpath).ToString();
                    bool res = hasData(cmd, guid);
                    string strcmd = String.Format("INSERT INTO name(guid,name) VALUES('{0}', '{1}')",
                        guid, filename);
                    cmd.CommandText = strcmd;
                    //cmd.ExecuteNonQuery();
                }
                transaction.Commit();
                transaction.Dispose();
                transaction = null;

                cnn.Close();
            }
        }

        public void select(string key)
        {
            using (SQLiteConnection cnn = new SQLiteConnection("Data Source=name.db"))
            using (SQLiteCommand cmd = cnn.CreateCommand())
            {
                cnn.Open();

                SQLiteTransaction transaction = cnn.BeginTransaction();

                string strcmd = String.Format("select * from name where name like '%{0}%'", key);
                cmd.CommandText = strcmd;
                //cmd.ExecuteNonQuery();
                using (SQLiteDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        
                        Console.WriteLine(String.Format("ID = {0}, MyValue = {1}", reader[0], reader[1]));
                        
                        //string path = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(reader[0].ToString()));
                        //Console.WriteLine(String.Format("path = {0}", path));
                       // string[] item1 = { reader[0], reader[1] };
                        ListViewItem item = new ListViewItem(reader[2].ToString());
                        item.Tag = reader[1].ToString();
                        listView1.Items.Add(item);
                    }

                }
                transaction.Commit();
                transaction.Dispose();
                transaction = null;

                cnn.Close();
            }
        }

        public bool hasData(SQLiteCommand cmd, string guid)
        {
            bool res = false;

            //string strcmd = String.Format("SELECT * FROM name WHERE guid = '{0}' AND rowid <= 1", guid);
            string strcmd = String.Format("SELECT * FROM name WHERE guid = '{0}'", guid);
            cmd.CommandText = strcmd;
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                res = reader.Read();
            }

            return res;

        }

        private void button3_Click(object sender, EventArgs e)
        {
            createNameTable("name.db");
        }

        private void Form1_DragDrop(object sender, DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop))
            {
                createNameTable("name.db");
                //foreach (string fileName in (string[])e.Data.GetData(DataFormats.FileDrop))
                //{
                string[] files = (string[])e.Data.GetData(DataFormats.FileDrop);
                insert(files);
                //}
            }
        }

        private void Form1_DragEnter(object sender, DragEventArgs e)
        {
            e.Effect = DragDropEffects.All;
        }

        private void comboBox1_KeyDown(object sender, KeyEventArgs e)
        {

        }

        private void comboBox1_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == '\r')
            {
                e.Handled = true;
                select(comboBox1.Text);
            }
        }

        private void listView1_DoubleClick(object sender, EventArgs e)
        {
            if (listView1.SelectedItems.Count == 1)
            {
                ListViewItem selitem = listView1.SelectedItems[0];
                string guid = selitem.Tag.ToString();

                string fullpath = Win32.getFullPathByObjectID(Win32.FILEGUID.parse(guid));
                 MessageBox.Show(fullpath);
            }
        }
    }


}
