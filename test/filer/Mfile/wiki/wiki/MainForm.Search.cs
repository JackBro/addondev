using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using wiki.control;
using System.Windows.Forms;

namespace wiki {
    partial class MainForm{
        private SplitButton sb;
        private void initSearch() {
            sb = new SplitButton();
            sb.Menu = SearchContextMenuStrip;
            
            sb.ToggleButton.Click += new System.EventHandler(ToggleButton_Click);
            NormalToolStripMenuItem.Click += (sender, e) => {
                NormalToolStripMenuItem.Checked = true;
                RegexToolStripMenuItem.Checked = false;
                MigemoToolStripMenuItem.Checked = false;
                sb.ToggleButton.Text = "N";

            };
            RegexToolStripMenuItem.Click += (sender, e) => {
                RegexToolStripMenuItem.Checked = true;
                NormalToolStripMenuItem.Checked = false;
                MigemoToolStripMenuItem.Checked = false;
                sb.ToggleButton.Text = "R";
            };
            MigemoToolStripMenuItem.Click += (sender, e) => {
                MigemoToolStripMenuItem.Checked = true;
                NormalToolStripMenuItem.Checked = false;
                RegexToolStripMenuItem.Checked = false;
                sb.ToggleButton.Text = "M";
            };

            NormalToolStripMenuItem.Checked = true;
            sb.ToggleButton.Text = "N";

            sb.Dock = DockStyle.Fill;
            SearchButtonPanel.Controls.Add(sb);


            SearchComboBox.AutoCompleteMode = AutoCompleteMode.Suggest;
            SearchComboBox.AutoCompleteSource = AutoCompleteSource.CustomSource;
            var s = new AutoCompleteStringCollection();
            SearchComboBox.AutoCompleteCustomSource = s;

            SearchComboBox.KeyDown += (sender, e) => {
                if (e.KeyCode == Keys.Return && SearchComboBox.Text.Length > 0) {

                    //CreateListViewTabPage(SearchComboBox.Text, manager.Filter(x => {
                    //    return x.Text.Contains(SearchComboBox.Text); ;
                    //}));
                    var text = SearchComboBox.Text;
                    SearchMode mode = SearchMode.Normal;
                    if(RegexToolStripMenuItem.Checked){
                        mode = SearchMode.Regex;
                    }else if(MigemoToolStripMenuItem.Checked){
                        mode = SearchMode.Migemo;
                    }
                    var sobj = CreateSearchObj(text, mode);

                    var t = getTabControl(getSelectedCategory());
                    var p = CreateListViewTabPage(getSelectedCategory(), t, sobj);
                    if (!t.TabPages.Contains(p)) {
                        t.TabPages.Add(p);
                    }
                    t.BringToFront();
                    t.SelectedTab = p;
                    //ItemTabControl.SelectedTab = p;
                }
            };
        }

        void ToggleButton_Click(object sender, EventArgs e) {
            if (NormalToolStripMenuItem.Checked) {
                NormalToolStripMenuItem.Checked = false;
                RegexToolStripMenuItem.Checked = true;
                sb.ToggleButton.Text = "R";
            }
            else if (RegexToolStripMenuItem.Checked) {
                RegexToolStripMenuItem.Checked = false;
                MigemoToolStripMenuItem.Checked = true;
                sb.ToggleButton.Text = "M";
            }
            else if (MigemoToolStripMenuItem.Checked) {
                MigemoToolStripMenuItem.Checked = false;
                NormalToolStripMenuItem.Checked = true;
                sb.ToggleButton.Text = "N";
            }
        }

        Search CreateSearchObj(string pattern, SearchMode mode) {
            Search s = null;
            switch (mode) {
                case SearchMode.Normal:
                    s = new SearchNormal(pattern);
                    //s.Pattern = pattern;
                    break;
                case SearchMode.Regex:
                    s = new SearchRegex(pattern);
                    //s.Pattern = pattern;
                    break;
                case SearchMode.Migemo:
                    s = new SearchMigemo(getMigemo().Query(pattern));
                    //s.Pattern = getMigemo().Query(pattern);
                    break;
                default:
                    break;
            }
            return s;
        }
    }
}
