using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace YYS
{
    public interface IText
    {
        int Length
        {
            get;
        }

        Boolean IsEmpty
        {
            get;
        }

        char this[int i]
        {
            get;
        }

        void Append(string value);
        void Insert(int startIndex, string value);
        void Remove(int startIndex, int count);
        void Remove(int startIndex);
        int IndexOf(string value, int startIndex);
        IText Substring(int startIndex, int count);
        IText Substring(int startIndex);
        void Replace(string oldValue, string newValue);
    }
}
