using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using wiki.control;
using System.Drawing;
using System.IO;

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

            _editor.Font = config.EditorFont;
            _editor.ForeColor = config.EditorFontColor;
            _editor.BackColor = config.EditorBackColor;
            _editor.DrawsTab = config.ShowTab;
            _editor.DrawsSpace = config.ShowSpace;
            _editor.DrawsFullWidthSpace = config.ShowZenSpace;
            _editor.DrawsEolCode = config.ShowEol;
            _editor.ShowsLineNumber = true;

            _editor.ColorScheme.SetColor(Sgry.Azuki.CharClass.Heading6 + 1, Color.Red, _editor.BackColor);
            _editor.Highlighter = new EditorHighlighter();

            EditorWrapToolStripButton.Checked = config.EdiorWrap;
            CloseEditor();

            _editor.ImeOnOffEvent += (sender, e) => {
                if (_editor.Document.AnchorIndex > 0) {
                    _editor.SetSelection(_editor.Document.AnchorIndex, _editor.Document.AnchorIndex - 1);
                    _editor.Delete();
                }
            };

            _editor.KeyDown += (sender, e) => {
                if (_editor.IsReadOnly) {
                    _editor.IsReadOnly = false;
                }
                if(_EditorKeyMap.ContainsKey(e.KeyData)){
                    _EditorKeyMap[e.KeyData](this);
                    e.Handled = true;
                    e.SuppressKeyPress = true;
                }
            };

            _editor.TextChanged += (s, e) => {
                initEditorToolStripButton();
            };

            _editor.Document.SelectionChanged += (s, e) => {
                initEditorToolStripButton();
            };

            //_editor.DragDrop

            CloseEditorToolStripButton.Click += (sender, e) => {
                //ViewEditorSplitContainer.Panel2Collapsed = true;
                CloseEditor();
            };

            EditorSearchToolStripButton.CheckedChanged += (sender, e) => {
                if (EditorSearchToolStripButton.Checked) {
                    if (_editorSearchControl == null) {
                        _editorSearchControl = new SearchControl();
                        _editorSearchControl.Dock = DockStyle.Bottom;
                        _editorSearchControl.CloseButton.Click += (ss, ee) => {
                            EditorSearchToolStripButton.Checked = false;
                        };
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

            EditorWrapToolStripButton.CheckedChanged += (sender, e) => {
                if (EditorWrapToolStripButton.Checked) {
                    Editor.ViewType = Sgry.Azuki.ViewType.WrappedProportional;
                    Editor.ViewWidth = Editor.ClientSize.Width - Editor.View.HRulerUnitWidth * 2;
                }
                else {
                    Editor.ViewType = Sgry.Azuki.ViewType.Proportional;
                }
            };

            EditorDateToolStripButton.Click += (s, e) => {
                EditDateTime();
            };

            EditorPinToolStripButton.CheckedChanged += (s, e) => {

            };

        }

        void initEditorToolStripButton() {
            UndoToolStripButto.Enabled = _editor.CanUndo;
            RedoToolStripButton.Enabled = _editor.CanRedo;
            CopyToolStripButton.Enabled = _editor.CanCopy;
            CutToolStripButton.Enabled = _editor.CanCut;
            PasteToolStripButton.Enabled = _editor.CanPaste;
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

        internal void EditDateTime() {
            //var manager = category.getManger(getSelectedCategory());
            //if (manager.EditingData == null) return;

            var item = category.GetItem(category.EditingID);
            if (item == null) return;

            var tform = new DateTimeForm();
            tform.Time = item.CreationTime;
            tform.StartPosition = FormStartPosition.CenterParent;
            var res = tform.ShowDialog(this);
            if (res == DialogResult.OK) {
                var mtime = tform.Time;
                if (mtime != item.CreationTime) {
                    //var data = manager.EditingData;
                    //manager.Remove(data.ID);
                    //data.CreationTime = mtime;
                    //manager.Insert(data);
                    category.UpDateCreationTime(category.EditingID, mtime);
                }
            }
            tform.Close();
        }

        internal void CloseEditor() {
            ViewEditorSplitContainer.Panel2Collapsed = true;
        }

        internal void OpenEditor() {
            ViewEditorSplitContainer.Panel2Collapsed = false;
        }
    }
}
