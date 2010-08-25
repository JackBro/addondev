using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AsControls
{
    public class KeyMap
    {
        private Action<ITextEditor> NullAction;
        private Dictionary<Keys, Action<ITextEditor>> EditorActionMap;
        //private Dictionary<EditorAction, Action<object>> funcNameToFunctinDic;

        public KeyMap()
        {
            EditorActionMap = new Dictionary<Keys, Action<ITextEditor>>();
            NullAction = (editor) =>{};
            //funcNameToFunctinDic = new Dictionary<EditorAction, Action<object>>();
        }

        //public void SetFunc(EditorAction action, Action<object> func)
        //{
        //    funcNameToFunctinDic.Add(action, func);
        //}


        public void setAction(Keys key, Action<ITextEditor> action)
        {
            EditorActionMap.Add(key, action);
        }

        //public void SetKey(Keys key, EditorAction action)
        //{
        //    keyToFuncNameDic.Add(key, action);
        //}

        //public Boolean ContainsFunction(Keys key)
        //{
        //    return keyToFuncNameDic.ContainsKey(key);
        //}

        public Action<ITextEditor> getAction(Keys key)
        {
            if (EditorActionMap.ContainsKey(key))
            {
                //EditorAction editaction = keyToFuncNameDic[key];
                return EditorActionMap[key];
            }
            return NullAction;
        }
    }
}
