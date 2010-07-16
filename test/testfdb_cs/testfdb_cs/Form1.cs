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
    public partial class Form1 : Form
    {
        //[StructLayout(LayoutKind.Sequential)]
        //public struct Fuga
        //{
        //    [MarshalAs(UnmanagedType.ByValArray, SizeConst = 16)] 
        //    public Byte[] id;

        //    public override string ToString()
        //    {
        //        string s = "";
        //        foreach(Byte b in id)
        //        {
        //           s+= b.ToString();
        //        }
            
        //        return s;//base.ToString();
        //    }
        //}

        [StructLayout(LayoutKind.Sequential)]
        public struct FILEGUID
        {
            public ulong  Data1;
            public ushort Data2;
            public ushort Data3;

            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 4)] 
            public Byte[] Data4;

            public override string ToString()
            {
                return String.Format("{0}-{1}-{2}-{3}-{4}-{5}-{6}",
                    Data1.ToString(),
                    Data2.ToString(),
                    Data3.ToString(),
                    Data4[0].ToString(),
                    Data4[1].ToString(),
                    Data4[2].ToString(),
                    Data4[3].ToString());
            }

            public static FILEGUID parse(string guid)
            {
                FILEGUID fileguid = new FILEGUID();
                fileguid.Data4 = new Byte[4];

                string[] g = guid.Split('-');
                fileguid.Data1 = ulong.Parse(g[0]);
                fileguid.Data2 = ushort.Parse(g[1]);
                fileguid.Data3 = ushort.Parse(g[2]);
                fileguid.Data4[0] = Byte.Parse(g[3]);
                fileguid.Data4[1] = Byte.Parse(g[4]);
                fileguid.Data4[2] = Byte.Parse(g[5]);
                fileguid.Data4[3] = Byte.Parse(g[6]);

                return fileguid;
            }
        }

        [DllImport("fgutil.dll", EntryPoint = "getObjectID", CharSet = CharSet.Unicode)]
        public static extern Boolean getObjectID(string msg, ref FILEGUID guid);

        [DllImport("fgutil.dll", EntryPoint = "getFullPathByObjectID", CharSet = CharSet.Unicode)]
        public static extern Boolean getFullPathByObjectID(FILEGUID guid, [MarshalAs(UnmanagedType.BStr)]ref string msg);

        public Form1()
        {
            InitializeComponent();
        }
        FILEGUID fuga;
        private void button1_Click(object sender, EventArgs e)
        {
            fuga = new FILEGUID();
            fuga.Data4 = new Byte[4];
            
            string msg = @"D:\data\src\PDE\xt2howm.rb";
            Boolean rc = getObjectID(msg, ref fuga);
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
            FILEGUID guid = FILEGUID.parse("1287905504378253229-60810-3072-118-29-23-147");
            
            Boolean rc = getFullPathByObjectID(guid, ref path);
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
        public void createTable(string filename)
        {
            using (SQLiteConnection cnn = new SQLiteConnection("Data Source=" + filename))
            using (SQLiteCommand cmd = cnn.CreateCommand())
            {
                cnn.Open();
                cmd.CommandText = "CREATE TABLE FOO (ID INTEGER PRIMARY KEY, guid TEXT))";
                cmd.ExecuteNonQuery();
            }
        }
    }


}
