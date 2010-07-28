using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace testfdb_cs
{
    public partial class RegisterForm : Form
    {
        public string Comment
        {
            get
            {
                return CommentTextBox.Text;
            }
        }

        public string[] Tags
        {
            get
            {
                string tags = TagTextBox.Text;
                return tags.Split(',');
            }
        }

        private List<TagDB.FileData> filedatas;
        public List<TagDB.FileData> FileDatas
        {
            get
            {
                return filedatas;
            }
            set
            {
                filedatas = value;
            }
        }
        

        public RegisterForm()
        {
            InitializeComponent();
        }

        public void SetFileData()
        {
            foreach(TagDB.FileData filedata in filedatas)
            {
                ListViewItem item = new ListViewItem(new string[]{filedata.name, filedata.getTagsConcat(), filedata.comment});
                FileListView.Items.Add();
            }
        }

        private void OKButton_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
            Close();
        }

        private void CancelButton_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
            Close();
        }
    }
}
