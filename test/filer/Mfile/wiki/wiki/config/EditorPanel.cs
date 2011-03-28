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
                    FontNameLabel.Text = EditorFont.Name;
                }
            };

            FontColorButton.Click += (sender, e) => {
                var res = EditorColorDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    FonColorLabel.BackColor = EditorColorDialog.Color;
                }
            };

            BackColorButton.Click += (sender, e) => {
                var res = EditorColorDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    BackColorLabel.BackColor = EditorColorDialog.Color;
                }
            };
        }
    }
}
