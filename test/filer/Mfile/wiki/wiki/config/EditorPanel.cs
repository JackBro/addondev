using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    public partial class EditorPanel : UserControl {
        public EditorPanel() {
            InitializeComponent();

            FontSelectButton.Click += (sender, e) => {
                var res = EditorFontDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    var EditorFont = EditorFontDialog.Font;
                    FontTextBox.Text = EditorFont.Name;
                }
            };
        }
    }
}
