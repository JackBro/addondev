using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    public partial class ConfigForm : Form {
        public Font EditorFont { get; private set; }

        public ConfigForm() {
            InitializeComponent();

            FontSelectButton.Click += (sender, e) => {
                var res = EditorFontDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    EditorFont = EditorFontDialog.Font;
                    FontTextBox.Text = EditorFont.Name;
                }
            };
        }
    }
}
