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
        MFTReader r;
        DateTime s;
        private void button1_Click(object sender, EventArgs e) {
            s = DateTime.Now;

            r = new MFTReader();
            r.CallBackEvent += new CallBackProc(r_CallBackEvent);
            //r.Read(new DriveInfo("c"));
            //var list = r.GetFile();
            backgroundWorker1.WorkerSupportsCancellation = true;
            backgroundWorker1.WorkerReportsProgress = true;
            backgroundWorker1.RunWorkerAsync();            
        }

        bool r_CallBackEvent(int per) {
            //throw new NotImplementedException();
            //this.Text = per.ToString();
            backgroundWorker1.ReportProgress(per);
            if (backgroundWorker1.CancellationPending) {
                return true;
            }
            return false;
        }

        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e) {
            r.Read(new DriveInfo("c"));
        }

        private void backgroundWorker1_ProgressChanged(object sender, ProgressChangedEventArgs e) {
            this.Text = e.ProgressPercentage.ToString();
        }

        private void backgroundWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e) {
            if (e.Cancelled) {
                this.Text = "キャンセルされました";
                return;
            }
            var tickgetrecode = DateTime.Now - s;
            var files = r.GetFile();
            var tickgetfiles = DateTime.Now - s;
            MessageBox.Show("read MFT is " + tickgetrecode.TotalMilliseconds.ToString() + "msec\n" + "make file is " + tickgetfiles.TotalMilliseconds.ToString() + "msec\n" + r.Count.ToString());
        }

        private void CancelButton_Click(object sender, EventArgs e) {
            backgroundWorker1.CancelAsync();
        }
    }
}
