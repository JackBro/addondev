using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace YYS
{
    public class KeyMap
    {
        private Action<ITextEditor> NullAction;
        private Dictionary<Keys, Action<ITextEditor>> EditorActionMap;

        public KeyMap()
        {
            EditorActionMap = new Dictionary<Keys, Action<ITextEditor>>();
            NullAction = (editor) =>{};
        }

        public void setAction(Keys key, Action<ITextEditor> action)
        {
            EditorActionMap.Add(key, action);
        }

        public Action<ITextEditor> getAction(Keys key)
        {
            if (EditorActionMap.ContainsKey(key))
            {
                return EditorActionMap[key];
            }
            return NullAction;
        }
    }
}
