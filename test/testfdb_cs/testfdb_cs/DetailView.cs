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
    public partial class DetailView : UserControl{
        public class MyEventArgs : EventArgs{
            public string guid;
            public string key;
            public string data;

            public MyEventArgs(string guid, string key, string data){
                this.guid = guid;
                this.key = key;
                this.data = data;
            }

        }

        public event EventHandler<MyEventArgs> OnChagedName;
        public event EventHandler<MyEventArgs> OnChagedComment;

        //private TableData _filedata;
        private string guid;
        private string name;
        private string comment;

        public DetailView(){
            InitializeComponent();

            NameTextBox.LostFocus += delegate{
                if (FileName != null && FileName != this.name && OnChagedName != null){
                    OnChagedName(this, new MyEventArgs(this.guid, "name", FileName));
                }
            };

            CommnetTextBox.LostFocus += delegate
            {
                if (Comment != null && Comment != this.comment && OnChagedComment != null)
                {
                    OnChagedComment(this, new MyEventArgs(this.guid, "comment", Comment));
                }              
            };
        }

        //public TableData filedata
        //{
        //    set {
        //       _filedata = value;
        //       NameTextBox.Text = _filedata.name;
        //       TagsTextBox.Text = _filedata.getTagsConcat();
        //       CommnetTextBox.Text = _filedata.comment;
        //    }
        //}
        public string Guid {
            get { return this.guid; }
            set {
                this.guid = value;
            }          
        }

        public string FileName{
            get { return NameTextBox.Text; }
            set {
                name = value;
                NameTextBox.Text = name;
            }
        }

        public string Tags{
            get { return TagsTextBox.Text; }
            set {
                TagsTextBox.Text = value;
            }
        }

        public string Comment
        {
            get { return CommnetTextBox.Text; }
            set {
                comment = value;
                CommnetTextBox.Text = comment;
            }
               
        }
    }
}
