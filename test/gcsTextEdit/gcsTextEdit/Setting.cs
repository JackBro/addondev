using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls
{
    public class KeyMap
    {
        private Dictionary<string, string> keyToFuncNameDic;
        private Dictionary<string, Action<object>> funcNameToFunctinDic;

        public KeyMap()
        {
            keyToFuncNameDic = new Dictionary<string, string>();
            funcNameToFunctinDic = new Dictionary<string, Action<object>>();
        }

        public void SetFunc(string funcName, Action<object> func)
        {
            funcNameToFunctinDic.Add(funcName, func);
        }

        public void SetKey(string key, string funcName)
        {
            keyToFuncNameDic.Add(key, funcName);
        }

        public Boolean ContainsFunction(string key)
        {
            return keyToFuncNameDic.ContainsKey(key);
        }

        public Action<object> getFunction(string key)
        {
            if (keyToFuncNameDic.ContainsKey(key))
            {
                string funcname = keyToFuncNameDic[key];
                return funcNameToFunctinDic[funcname];
            }
            return null;
        }
    }
}
