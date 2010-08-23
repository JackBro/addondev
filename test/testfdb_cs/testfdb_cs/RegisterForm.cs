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
                var checkedtag = new List<string>();
                for(int i=0;i<TagCheckedListBox.Items.Count; i++)
                {
                    if (TagCheckedListBox.GetItemChecked(i))
                    {
                        checkedtag.Add((string)TagCheckedListBox.Items[i]);
                    }
                }
                return checkedtag.ToArray<string>();
            }

            set
            {
                foreach (string tag in value)
                {
                    TagCheckedListBox.Items.Add(tag);
                }
            }
        }

        //private List<FileData> filedatas;
        public List<TableData> FileDatas {get;set;}
        

        public RegisterForm()
        {
            InitializeComponent();
        }

        public void SetFileData()
        {
            foreach (TableData filedata in FileDatas)
            {
                ListViewItem item = new ListViewItem(new string[]{filedata.name, filedata.TagsToString(), filedata.comment});
                FileListView.Items.Add(item);
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
