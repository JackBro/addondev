using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;

namespace testfdb_cs
{
    public partial class Form1 : Form
    {
        [DllImport("fgutil.dll")]
        public static extern int getObjectID([MarshalAs(UnmanagedType.BStr)]ref string msg);

        [DllImport("fgutil.dll")]
        public static extern int getFullPathByObjectID([MarshalAs(UnmanagedType.BStr)]ref string msg);

        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string msg = "";
            int rc = GetMsgBSTR(ref msg);
            if (rc == 0)
            {
                MessageBox.Show(msg.ToString());
            }
        }
    }
}
