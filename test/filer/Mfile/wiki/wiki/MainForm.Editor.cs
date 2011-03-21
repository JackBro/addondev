using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    partial class MainForm {
        private AzukiControlEx _editor;
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
        }
    }
}
