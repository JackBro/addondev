using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using wiki.control;

namespace wiki {
    partial class MainForm {
        private AzukiControlEx _editor;
        private SearchControl _editorSearchControl;
        internal AzukiControlEx Editor {
            get { return _editor; }
        }
        private void initEditor() {

            _editor = new AzukiControlEx();
            _editor.Dock = DockStyle.Fill;
            EditorPanel.Controls.Add(_editor);

            
            _editor.ShowsLineNumber = true;

            _editor.ImeOnOffEvent += (sender, e) => {
                if (_editor.Document.AnchorIndex > 0) {
                    _editor.SetSelection(_editor.Document.AnchorIndex, _editor.Document.AnchorIndex - 1);
                    _editor.Delete();
                }
            };

            _editor.KeyDown += (sender, e) => {
                if(_KeyMap.ContainsKey(e.KeyData)){
                    _KeyMap[e.KeyData](this);
                    e.Handled = true;
                    e.SuppressKeyPress = true;
                }
            };

            CloseEditorToolStripButton.Click += (sender, e) => {
                ViewEditorSplitContainer.Panel2Collapsed = true;
            };

            EditorSearchToolStripButton.CheckedChanged += (sender, e) => {
                if (EditorSearchToolStripButton.Checked) {
                    if (_editorSearchControl == null) {
                        _editorSearchControl = new SearchControl();
                        _editorSearchControl.Dock = DockStyle.Bottom;
                        _editorSearchControl.SearchComboBox.TextChanged += new System.EventHandler(SearchComboBox_TextChanged);
                        _editorSearchControl.NextButton.Click += (ss, se) => {
                            FindNext();
                        };
                        _editorSearchControl.PrevButton.Click += (ss, se) => {
                            FindPrev();
                        };
                    }
                    EditorPanel.Controls.Add(_editorSearchControl);
                }
                else {
                    if (_editorSearchControl != null) {
                        EditorPanel.Controls.Remove(_editorSearchControl);
                    }
                }
            };
        }

        void SearchComboBox_TextChanged(object sender, EventArgs e) {
            FindNext();
        }

        void FindNext() {
            var pattern = _editorSearchControl.SearchComboBox.Text;
            var res = _editor.Document.FindNext(pattern, _editor.Document.AnchorIndex, false);
            if (res != null) {
                _editor.Document.SetSelection(res.End, res.Begin);
                _editor.ScrollToCaret();
            }
        }
        void FindPrev() {
            var pattern = _editorSearchControl.SearchComboBox.Text;
            var res = _editor.Document.FindPrev(pattern, _editor.Document.AnchorIndex, false);
            if (res != null) {
                _editor.Document.SetSelection(res.Begin, res.End);
                _editor.ScrollToCaret();
            }
        }
    }
}
