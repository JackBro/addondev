using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;

namespace MouseGesture_Net
{
    public partial class SettingControl : UserControl
    {
        private ColumnHeader ActioncolumnHeader= new ColumnHeader();
        private ColumnHeader EnablecolumnHeader= new ColumnHeader();
        private ColumnHeader GesturecolumnHeader= new ColumnHeader();
        public SettingControl()
        {
            InitializeComponent();
            
            Dock = DockStyle.Fill;
            ActioncolumnHeader.Text = "Action";
            EnablecolumnHeader.Text = "Enable";
            GesturecolumnHeader.Text = "Gesture";
            this.Load += new EventHandler(SettingControl_Load);
            this.GesturelistView.Click += new EventHandler(GesturelistView_Click);
            this.GeaturetextBox.KeyDown += new KeyEventHandler(GeaturetextBox_KeyDown);
        }

        void GeaturetextBox_KeyDown(object sender, KeyEventArgs e)
        {
            switch(e.KeyCode)
            {
                case Keys.Up:
                    GeaturetextBox.Text += "Å™";
                    break;
                case Keys.Down:
                    GeaturetextBox.Text += "Å´";
                    break;
                case Keys.Left:
                    GeaturetextBox.Text += "Å©";
                    break;
                case Keys.Right:
                    GeaturetextBox.Text += "Å®";
                    break;
                default:
                    break;
            }
        }

        void GesturelistView_Click(object sender, EventArgs e)
        {
            if (GesturelistView.SelectedItems.Count == 1)
            {
                GeaturetextBox.Text = GesturelistView.SelectedItems[0].SubItems[2].Text;
                //GeaturetextBox.Focus();
                //GeaturetextBox.SelectAll();
            }
        }

        void SettingControl_Load(object sender, EventArgs e)
        {
            if (this.GesturelistView.Columns.Count != 0) return;
            this.GesturelistView.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.ActioncolumnHeader,
            this.EnablecolumnHeader,
            this.GesturecolumnHeader});
        }

        public List<MouseGestureCommand> list
        {
            get 
            {
                List<MouseGestureCommand> mgclist = new List<MouseGestureCommand>();
                foreach (ListViewItem item in GesturelistView.Items)
                {
                    mgclist.Add(new MouseGestureCommand(item.SubItems[2].Text, item.Text));
                }
                return mgclist;
            }
            set 
            {
                GesturelistView.Clear();
                foreach (MouseGestureCommand cmd in value)
                {
                    ListViewItem item = GesturelistView.Items.Add(cmd.CommandName);
                    item.SubItems.Add("true");
                    item.SubItems.Add(cmd.Gesture);
                }
                ActioncolumnHeader.AutoResize(ColumnHeaderAutoResizeStyle.ColumnContent);
                EnablecolumnHeader.AutoResize(ColumnHeaderAutoResizeStyle.ColumnContent);
                GesturecolumnHeader.AutoResize(ColumnHeaderAutoResizeStyle.ColumnContent);
            }
        }

        private void OKbutton_Click(object sender, EventArgs e)
        {
            if (GesturelistView.SelectedItems.Count == 1)
            {
                ListViewItem item = GesturelistView.SelectedItems[0];
                item.SubItems[2].Text = GeaturetextBox.Text;
            }
        }

        private void Clearbutton_Click(object sender, EventArgs e)
        {
            GeaturetextBox.Clear();
        }
    }
}
