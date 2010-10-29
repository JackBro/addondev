using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace YYS
{
    public class KeyMap
    {
        private Action<GCsTextEdit> NullAction;
        private Dictionary<Keys, Action<GCsTextEdit>> EditorActionMap;

        public KeyMap()
        {
            EditorActionMap = new Dictionary<Keys, Action<GCsTextEdit>>();
            NullAction = (editor) =>{};
        }

        public void setAction(Keys key, Action<GCsTextEdit> action)
        {
            EditorActionMap.Add(key, action);
        }

        public Action<GCsTextEdit> getAction(Keys key)
        {
            if (EditorActionMap.ContainsKey(key))
            {
                return EditorActionMap[key];
            }
            return NullAction;
        }
    }
}
