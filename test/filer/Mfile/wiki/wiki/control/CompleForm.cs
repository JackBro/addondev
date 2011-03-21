using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki.control {
    public partial class CompleForm : Form {
        private MainForm form;
        private int startOffset;

        public CompleForm(MainForm form) {
            InitializeComponent();
            this.FormBorderStyle = FormBorderStyle.None;
            this.Owner = form; 

            this.form = form;

            startOffset = form.Editor.Document.AnchorIndex;

            form.Editor.TextChanged += new System.EventHandler(Editor_TextChanged);
            form.Editor.MouseDown += new MouseEventHandler(Editor_MouseDown);

                
            this.Shown += (sender, e) => {
                //form.Editor.Focus();
            };
            this.FormClosing += (sender, e) => {
                form.Editor.TextChanged -= Editor_TextChanged;
                form.Editor.MouseDown -= Editor_MouseDown;
            };
            this.KeyDown += (sender, e) => {
                
            };
            listBox.DoubleClick += (sender, e) => {

            };
            var h = listBox.ItemHeight * (listBox.Items.Count);
            this.Height = h == 0 ? listBox.ItemHeight : h;

            var pos = form.Editor.PointToScreen(form.Editor.GetPositionFromIndex(form.Editor.Document.AnchorIndex));
            pos.Y += (int)form.Editor.Font.GetHeight();
            this.StartPosition = FormStartPosition.Manual;
            this.Left = pos.X;
            this.Top = pos.Y;
        }

        void Editor_MouseDown(object sender, MouseEventArgs e) {
            Close();
        }

        void Editor_TextChanged(object sender, EventArgs e) {
            //throw new NotImplementedException();
        }
    }
}
