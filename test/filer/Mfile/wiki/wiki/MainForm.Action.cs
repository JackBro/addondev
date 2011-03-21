using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    partial class MainForm {
        Dictionary<Keys, Action<MainForm>> _KeyMap = new Dictionary<Keys, Action<MainForm>>();
        
        internal void initKeyMap() {
            _KeyMap.Clear();
            _KeyMap.Add(Keys.Control | Keys.S, Actions.Cut);
            _KeyMap.Add(Keys.Control | Keys.C, Actions.Copy);
            _KeyMap.Add(Keys.Control | Keys.V, Actions.Paste);
            _KeyMap.Add(Keys.Control | Keys.Z, Actions.Undo);
            _KeyMap.Add(Keys.Control | Keys.Y, Actions.Redo);
            _KeyMap.Add(Keys.Control | Keys.Space, Actions.Comple);
        }
    }
}
