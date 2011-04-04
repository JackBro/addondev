using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    partial class MainForm {
        Dictionary<Keys, Action<MainForm>> _EditorKeyMap = new Dictionary<Keys, Action<MainForm>>();
        Dictionary<Keys, Action<MainForm>> _CategoryLsitViewKeyMap = new Dictionary<Keys, Action<MainForm>>();
        
        internal void initKeyMap() {
            _EditorKeyMap.Clear();
            _EditorKeyMap.Add(Keys.Control | Keys.S, Actions.Cut);
            _EditorKeyMap.Add(Keys.Control | Keys.C, Actions.Copy);
            _EditorKeyMap.Add(Keys.Control | Keys.V, Actions.Paste);
            _EditorKeyMap.Add(Keys.Control | Keys.Z, Actions.Undo);
            _EditorKeyMap.Add(Keys.Control | Keys.Y, Actions.Redo);
            _EditorKeyMap.Add(Keys.Control | Keys.Space, Actions.Comple);

            _CategoryLsitViewKeyMap.Add(Keys.Delete, Actions.DeleteFile);
        }
    }
}
