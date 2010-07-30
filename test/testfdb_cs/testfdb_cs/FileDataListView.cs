using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace testfdb_cs
{
    class FileDataListView : ListView
    {
        public void InitializeComponent()
        {
            this.Dock = System.Windows.Forms.DockStyle.Fill;
            this.FullRowSelect = true;
            this.HideSelection = false;
            this.UseCompatibleStateImageBehavior = false;
            this.View = System.Windows.Forms.View.Details;
        }

        public void SetFileData(List<FileData> filedatas)
        {
        }
    }
}
