using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace testfdb_cs
{
    public partial class DetailView : UserControl
    {
        public class MyEventArgs : EventArgs
        {
            public FileData filedata;

            public MyEventArgs(FileData filedata)
            {
                this.filedata = filedata;
            }

        }

        public event EventHandler<MyEventArgs> OnChagedName;
        public event EventHandler<MyEventArgs> OnChagedComment;

        private FileData _filedata;

        public DetailView()
        {
            InitializeComponent();

            NameTextBox.LostFocus += delegate
            {
                if (_filedata != null && FileName != _filedata.name && OnChagedName != null)
                {
                    _filedata.name = FileName;
                    OnChagedName(this, new MyEventArgs(_filedata));
                }
            };

            CommnetTextBox.LostFocus += delegate
            {
                if (_filedata !=null && Comment != _filedata.comment && OnChagedComment != null)
                {
                    _filedata.comment = Comment;
                    OnChagedComment(this, new MyEventArgs(_filedata));
                }              
            };
        }

        public FileData filedata
        {
            set {
               _filedata = value;
               NameTextBox.Text = _filedata.name;
               TagsTextBox.Text = _filedata.getTagsConcat();
               CommnetTextBox.Text = _filedata.comment;
            }
        }

        public string FileName
        {
            get { return NameTextBox.Text; }
        }

        public string Tags
        {
            get { return TagsTextBox.Text; }
        }

        public string Comment
        {
            get { return CommnetTextBox.Text; }
        }
    }
}
