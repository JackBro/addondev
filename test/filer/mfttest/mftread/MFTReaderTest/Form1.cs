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
using System.Runtime.Serialization.Formatters.Binary;

namespace MFTReaderTest {
    public partial class Form1 : Form {
        
        private List<CheckBox> drivelist = new List<CheckBox>();
        private List<MFTFile> MFTFileList;
        //private List<MFTFile> ResultMFTFileList = new List<MFTFile>();

        private MFT.MFT_FILE_INFO[] mftfiles;
        private List<MFT.MFT_FILE_INFO> ResultMFTFileList = new List<MFT.MFT_FILE_INFO>();
        
        public Form1() {
            InitializeComponent();

            ColumnHeader headerName = new ColumnHeader();
            headerName.Name = "name";
            headerName.Text = "name";
            listView1.Columns.Add(headerName);
            
            ColumnHeader headerPath = new ColumnHeader();
            headerPath.Name = "path";
            headerPath.Text = "path";
            listView1.Columns.Add(headerPath);

            ColumnHeader headerSize = new ColumnHeader();
            headerSize.Name = "size";
            headerSize.Text = "size";
            listView1.Columns.Add(headerSize);

            ColumnHeader headerCreationTime = new ColumnHeader();
            headerCreationTime.Name = "creationtime";
            headerCreationTime.Text = "creationtime";
            listView1.Columns.Add(headerCreationTime);

            ColumnHeader headerLastWriteTime = new ColumnHeader();
            headerLastWriteTime.Name = "lastwritetime";
            headerLastWriteTime.Text = "lastwritetime";
            listView1.Columns.Add(headerLastWriteTime);
            
            var ds = DriveInfo.GetDrives();
            var drives = ds.Reverse();
            foreach (var drive in drives) {
                if (drive.IsReady && drive.DriveFormat == "NTFS"
                    && (drive.DriveType == DriveType.Fixed || drive.DriveType == DriveType.Removable)) {
                    var ch = new CheckBox();
                    ch.Text = drive.Name;
                    ch.Tag = drive;
                    ch.Width = 50;
                    drivelist.Add(ch);
                    ch.Dock = DockStyle.Left;
                    panel2.Controls.Add(ch);
                }
	        }
            //panel1.AutoSize = true;

            //MFT.MFT_FILE_INFO[] files;
            //BinaryFormatter bf = new BinaryFormatter();
            //using (FileStream fs = new FileStream("temp.bin", FileMode.Open)) {
            //    files = (MFT.MFT_FILE_INFO[])bf.Deserialize(fs);
            //}
            //GC.Collect();
            //GC.WaitForPendingFinalizers();
            //GC.Collect();

        }
        MFTReader r;
        DateTime s;
        private void button1_Click(object sender, EventArgs e) {
            s = DateTime.Now;
            if (DLLradioButton.Checked) {
                r = new MFTReader();
                r.CallBackEvent += new CallBackProc(r_CallBackEvent);
                backgroundWorker1.WorkerSupportsCancellation = true;
                backgroundWorker1.WorkerReportsProgress = true;
                backgroundWorker1.RunWorkerAsync();
            } else if(ClassradioButton.Checked) {
                MFT.MFTReader mr = new MFT.MFTReader();

                mftfiles = mr.read(new DriveInfo("c"));
                //mftfiles = mr.read(new DriveInfo("d"));
                var tickgetrecode = DateTime.Now - s;
                MessageBox.Show("MFT.MFTReader read() is " + tickgetrecode.TotalMilliseconds.ToString() + "msec\n" + mftfiles.Count().ToString());

                //mftfiles = null;
                //BinaryFormatter bf = new BinaryFormatter();
                //using (FileStream fs = new FileStream("temp.bin", FileMode.Create)) {
                //    bf.Serialize(fs, mftfiles); 
                //}
            }
            //GC.Collect();
            //GC.WaitForPendingFinalizers();
            //GC.Collect();
        }

        bool r_CallBackEvent(int per) {
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
            //MFTFileList = r.GetFile();
            var tickgetfiles = DateTime.Now - s;
            MessageBox.Show("read MFT is " + tickgetrecode.TotalMilliseconds.ToString() + "msec\n" + "make file is " + tickgetfiles.TotalMilliseconds.ToString() + "msec\n" + r.Count.ToString());
        }

        private void CancelButton_Click(object sender, EventArgs e) {
            backgroundWorker1.CancelAsync();
        }

        private void listView1_RetrieveVirtualItem(object sender, RetrieveVirtualItemEventArgs e) {
            
            //e.Item.Text = item.Name;
            if (e.ItemIndex < ResultMFTFileList.Count){ // e.Item != null) {
                var item = ResultMFTFileList[e.ItemIndex];
                var listviewitem = new ListViewItem();
                listviewitem.Text = item.Name;
                listviewitem.SubItems.Add("");
                listviewitem.SubItems.Add(item.Size.ToString());
                listviewitem.SubItems.Add(item.CreationTime.ToLongDateString());
                listviewitem.SubItems.Add(item.LastWriteTime.ToLongDateString());
                //e.Item.SubItems["name"].Text = item.Name;
                //e.Item.SubItems["path"].Text = item.Path;
                //e.Item.SubItems["size"].Text = item.Size.ToString();
                //e.Item.SubItems["creationtime"].Text = item.CreationTime.ToLongDateString();
                //e.Item.SubItems["lastwritetime"].Text = item.LastWriteTime.ToLongDateString();
                e.Item = listviewitem;
            }

        }

        private void textBox1_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyData == Keys.Return) {
                DateTime ss = DateTime.Now;
                ResultMFTFileList.Clear();
                //foreach (var item in MFTFileList) {
                foreach (var item in mftfiles) {
                    if (item.Name !=null && item.Name.Contains(textBox1.Text)) {
                        ResultMFTFileList.Add(item);
                        //listView1.VirtualListSize = ResultMFTFileList.Count;
                    }
                }
                var tickgetfiles = DateTime.Now - ss;
                listView1.VirtualListSize = ResultMFTFileList.Count;
            }
        }
    }
}
