using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Sgry.Azuki.WinForms;
using wiki.Properties;
using wiki.config;

namespace wiki {
    public partial class EditorConfig : UserControl {
        public Config config { get; set; }
        private AzukiControl editor;
        public EditorConfig(Config config) {
            this.config = config;

            InitializeComponent();
            editor = new AzukiControl();
            editor.Dock = DockStyle.Fill;
            editor.Text = Resources.EditorPreviewText;
            panel.Controls.Add(editor);

            FontSelectButton.Click += (sender, e) => {
                var res = EditorFontDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    var EditorFont = EditorFontDialog.Font;
                    FontSelectButton.Text = EditorFont.Name;
                }
            };

            FontColorButton.Click += (sender, e) => {
                var res = EditorColorDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    editor.ForeColor = EditorColorDialog.Color;
                }
            };

            BackColorButton.Click += (sender, e) => {
                var res = EditorColorDialog.ShowDialog();
                if (res == DialogResult.OK) {
                    editor.BackColor = EditorColorDialog.Color;
                }
            };

            ShowTabCheckBox.CheckedChanged += (s, e) => {
                editor.View.DrawsTab = ShowTabCheckBox.Checked;
            };
            ShowEolCheckBox.CheckedChanged += (s, e) => {
                editor.View.DrawsEolCode = ShowEolCheckBox.Checked;
            };
            ShowSpaceCheckBox.CheckedChanged += (s, e) => {
                editor.View.DrawsTab = ShowTabCheckBox.Checked;
            };
            ShowZenSpaceCheckBox.CheckedChanged += (s, e) => {
                editor.View.DrawsTab = ShowTabCheckBox.Checked;
            };

            //
            NewButton.Click += (s, e) => {
                var edit = new CompleEditForm();
                var res = edit.ShowDialog(this);
                if (res == DialogResult.OK) {
                    listBox1.Items.Add(edit.Code);
                }
            };
            EditButton.Click += (s, e) => {
                var edit = new CompleEditForm();
                edit.Code = listBox1.SelectedItem.ToString();
                var res = edit.ShowDialog(this);
                if (res == DialogResult.OK) {
                    listBox1.SelectedItem = edit.Code;
                }
            };
            DeleteButton.Click += (s, e) => {
                listBox1.Items.RemoveAt(listBox1.SelectedIndex);
            };

            this.Load += (s, e) => {
                editor.Font = config.EditorFont;
                editor.ForeColor = config.EditorFontColor;
                editor.BackColor = config.EditorBackColor;

                editor.DrawsTab = config.ShowTab;
                editor.DrawsEolCode = config.ShowEol;
                editor.DrawsSpace = config.ShowSpace;
                editor.DrawsFullWidthSpace = config.ShowZenSpace;

                ShowTabCheckBox.Checked = editor.DrawsTab;
                ShowEolCheckBox.Checked = editor.DrawsEolCode;
                ShowSpaceCheckBox.Checked = editor.DrawsSpace;
                ShowZenSpaceCheckBox.Checked = editor.DrawsFullWidthSpace;
            };
        }
    }
}
