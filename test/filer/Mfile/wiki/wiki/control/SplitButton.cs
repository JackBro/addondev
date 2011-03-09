using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki.control {
    public partial class SplitButton : UserControl {
        private ContextMenuStrip menu;
        public ContextMenuStrip Menu {
            get { return this.menu; }
            set {
                this.menu = value;
            }
        }
        public SplitButton() {
            InitializeComponent();
           
 
            ToggleButton.Click += (sender, e) => {

            };

            DropDownButton.Click += (sender, e) => {
                if (menu == null) return;
                
                //var ch = sender as Button;
                if (!menu.Visible) {
                    menu.Show((Control)sender,
                    ClientRectangle.Right - 100 - menu.GripMargin.Left,
                    ClientRectangle.Bottom + 1 - menu.GripMargin.Top);
                } else {
                    menu.Close();
                }
            };
        }
    }
}
