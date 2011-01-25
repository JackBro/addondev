using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using MFTReaderWrap;
using System.IO;

namespace MFTReaderTest {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e) {
            MFTReader r = new MFTReader();
            r.CallBackEvent += new CallBackProc(r_CallBackEvent);
            r.Read(new DriveInfo("c"));
            var list = r.GetFile();
            
        }

        bool r_CallBackEvent(int per) {
            //throw new NotImplementedException();
            this.Text = per.ToString();
            return false;
        }
    }
}
