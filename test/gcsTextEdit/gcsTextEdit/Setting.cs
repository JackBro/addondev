using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AsControls
{
    public enum EditorAction {
        Copy,
        Paste,
        Delete,
        BackSpace,
        Undo,
        Redo,
        Up,
        Down,
        Right,
        Left,
        Home,
        PageUp,
        PageDown
    }

    public class KeyMap
    {
        private Dictionary<Keys, EditorAction> keyToFuncNameDic;
        private Dictionary<EditorAction, Action<object>> funcNameToFunctinDic;

        public KeyMap()
        {
            keyToFuncNameDic = new Dictionary<Keys, EditorAction>();
            funcNameToFunctinDic = new Dictionary<EditorAction, Action<object>>();
        }

        public void SetFunc(EditorAction action, Action<object> func)
        {
            funcNameToFunctinDic.Add(action, func);
        }

        public void SetKey(Keys key, EditorAction action)
        {
            keyToFuncNameDic.Add(key, action);
        }

        public Boolean ContainsFunction(Keys key)
        {
            return keyToFuncNameDic.ContainsKey(key);
        }

        public Action<object> getFunction(Keys key)
        {
            if (keyToFuncNameDic.ContainsKey(key))
            {
                EditorAction editaction = keyToFuncNameDic[key];
                return funcNameToFunctinDic[editaction];
            }
            return null;
        }
    }
}
