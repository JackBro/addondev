using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki.config {
    public partial class SnippetEditForm : Form {
        public string Code {
            get { return TextBox.Text; }
            set { TextBox.Text = value; }
        }
        public SnippetEditForm() {
            InitializeComponent();

            OKbutton.Click += (s, e) => {
                this.DialogResult = DialogResult.OK;
                Close();
            };
            Cancelbutton.Click += (s, e) => {
                this.DialogResult = DialogResult.Cancel;
                Close();
            };
        }
    }
}
