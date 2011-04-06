using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using wiki.control;
using System.Windows.Forms;

namespace wiki {
    partial class MainForm {
        private SearchControl _browserSearchControl;
        private void initBrowser() {
            BrowserSearchToolStripButton.CheckedChanged += (sender, e) => {
                string pat = string.Empty;
                if (BrowserSearchToolStripButton.Checked) {
                    if (_browserSearchControl == null) {
                        _browserSearchControl = new SearchControl();
                        _browserSearchControl.Dock = DockStyle.Bottom;
                        _browserSearchControl.SearchComboBox.TextChanged += (ss, se) => {
                            var cmbbox = ss as ComboBox;
                            pat = cmbbox.Text;
                            if (_browserSearchControl.MigemoCheckBox.Checked) {
                                pat = getMigemo().Query(cmbbox.Text);
                            }
                            if (pat.Length > 0) {
                                InvokeScript("js_incrementalSearch", pat, _browserSearchControl.MigemoCheckBox.Checked.ToString());
                            }
                        };
                        _browserSearchControl.NextButton.Click += (ss, se) => {
                            InvokeScript("js_searchNext", pat, _browserSearchControl.MigemoCheckBox.Checked.ToString());
                        };
                        _browserSearchControl.PrevButton.Click += (ss, se) => {
                            InvokeScript("js_searchPrev", pat, _browserSearchControl.MigemoCheckBox.Checked.ToString());
                        };

                        _browserSearchControl.CloseButton.Click += (ss, se) => {
                            BrowserSearchToolStripButton.Checked = false;
                        };
                    }
                    ViewEditorSplitContainer.Panel1.Controls.Add(_browserSearchControl);
                }
                else {
                    if (_browserSearchControl != null) {
                        ViewEditorSplitContainer.Panel1.Controls.Remove(_browserSearchControl);
                    }
                }
            };
        }
    }
}
