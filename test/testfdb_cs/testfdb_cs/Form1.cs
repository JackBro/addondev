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

namespace testfdb_cs
{
    public partial class Form1 : Form
    {
        [StructLayout(LayoutKind.Sequential)]
        public struct Fuga
        {
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 16)] 
            public Byte[] id;
        }

        [DllImport("fgutil.dll", EntryPoint = "getObjectID", CharSet = CharSet.Unicode)]
        public static extern Boolean getObjectID(string msg, ref Fuga fuga);

        [DllImport("fgutil.dll", EntryPoint = "getFullPathByObjectID", CharSet = CharSet.Unicode)]
        public static extern Boolean getFullPathByObjectID(Fuga fuga, [MarshalAs(UnmanagedType.BStr)]ref string msg);

        public Form1()
        {
            InitializeComponent();
        }
        Fuga fuga;
        private void button1_Click(object sender, EventArgs e)
        {
            fuga = new Fuga();
            fuga.id = new Byte[16];

            string msg = @"D:\data\src\PDE\xt2howm.rb";
            Boolean rc = getObjectID(msg, ref fuga);
            if (rc)
            {
                MessageBox.Show(msg.ToString());
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
             //string path="";
             //Boolean rc = getFullPathByObjectID(fuga, ref path);
             //if (rc)
             //{
             //    MessageBox.Show(path.ToString());
             //}
            DoIt(@"D:\data\src\PDE\workrepository\plugins\test\testfdb_cs");
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
    }
}
