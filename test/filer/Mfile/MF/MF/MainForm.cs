using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace MF {
    public partial class MainForm : Form {
        public MainForm() {
            InitializeComponent();

            this.ResizeEnd += (sender, e) => {
                ResizeWindow();
            };

            UserControl1 us = new UserControl1();
            us.Dock = DockStyle.Fill;
            panel1.Controls.Add(us);
            
        }

        private void ResizeWindow() {
            var w = this.Width / (panel1.Controls.Count) - 1;

            foreach (var item in panel1.Controls) {
                UserControl1 u = item as UserControl1;
                u.Width = w;
            }          
        }

        private void newToolStripMenuItem_Click(object sender, EventArgs e) {
            UserControl1 us = new UserControl1();
            us.Dock = DockStyle.Right;
            panel1.Controls.Add(us); 

            ResizeWindow();
        }
    }
}
