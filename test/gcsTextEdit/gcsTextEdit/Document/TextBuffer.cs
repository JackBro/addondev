using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Globalization;

namespace AsControls
{
    public class LineBuffer : IBuffer
    {
        private StringInfo stringInfo;

        public int Length
        {
            get { return stringInfo.LengthInTextElements; }
        }

        public Boolean IsEmpty
        {
            get { return stringInfo.LengthInTextElements == 0 ? true : false; }
        }

        public LineBuffer(string text)
        {
            stringInfo = new StringInfo(text);
        }

        public override string ToString()
        {
            return stringInfo.String;
        }

        #region ILineBuffer メンバ

        public char this[int i]
        {
            get
            {
                return stringInfo.SubstringByTextElements(i, 1)[0];
            }
        }

        public void Append(string value)
        {
            stringInfo.String += value;
        }

        public void Insert(int startIndex, string value)
        {
            startIndex = (startIndex == 0 ? 0 : stringInfo.SubstringByTextElements(0, startIndex).Length);
            string s = stringInfo.String.Insert(startIndex, value);
            stringInfo.String = s;
        }

        public void Remove(int startIndex, int count)
        {
            string s1 = string.Empty;
            if (startIndex != 0)
                s1 = stringInfo.SubstringByTextElements(0, startIndex);

            string s2  = string.Empty;
            if(startIndex + count != stringInfo.LengthInTextElements)
                s2 = stringInfo.SubstringByTextElements(startIndex + count, stringInfo.LengthInTextElements - (startIndex + count));
            
            stringInfo.String = s1 + s2;
        }

        public void Remove(int startIndex)
        {
            this.Remove(startIndex, stringInfo.LengthInTextElements - startIndex);
        }

        public IBuffer Substring(int startIndex, int count)
        {
           
            return new LineBuffer(stringInfo.SubstringByTextElements(startIndex, count));
        }

        public IBuffer Substring(int startIndex)
        {
            if (stringInfo.LengthInTextElements == 0) return new LineBuffer("");
            return new LineBuffer(stringInfo.SubstringByTextElements(startIndex));
        }

        public void Replace(string oldValue, string newValue)
        {
            stringInfo.String = stringInfo.String.Replace(oldValue, newValue); 
        }

        #endregion
    }
}
