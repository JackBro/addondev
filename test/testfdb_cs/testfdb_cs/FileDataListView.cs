using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace testfdb_cs
{
    class FileDataListView : ListView
    {
        public FileDataListView()
        {
            InitializeComponent();
        }

        public void InitializeComponent()
        {
            this.Dock = System.Windows.Forms.DockStyle.Fill;
            this.FullRowSelect = true;
            this.HideSelection = false;
            this.UseCompatibleStateImageBehavior = false;
            this.View = System.Windows.Forms.View.Details;

            ColumnHeader header1 = new ColumnHeader();
            header1.Text = "name";
            this.Columns.Add(header1);

            ColumnHeader header2 = new ColumnHeader();
            header2.Text = "tags";
            this.Columns.Add(header2);

            ColumnHeader header3 = new ColumnHeader();
            header3.Text = "comment";
            this.Columns.Add(header3);
        }

        public List<FileData> FileData{ get; set; }
    }
}
