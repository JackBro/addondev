using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls
{
    public enum TokenType
    {
        TAB, // Tab
        WSP, // 半角スペース
        ZSP, // 全角スペース
        TXT, // 普通の字
        CLICKABLE //クリッカブル
    }

    //Attribute
    public class AttributeInfo
    {
        public int len;
        public Color forecolor;
        public Color backcolor;
        public TokenType Token;
        public AttributeInfo()
        {
            len = 0;
            forecolor = Color.Black;
            Token = TokenType.TXT;
        }

        public void Clear()
        {
        }
    }

    public class HighlightInfo
    {
        public Color FontColor;
        public Boolean Bold;
        public Boolean UnderLine;
        public TokenType type;
    }

    public class MatchEx
    {
        int index;
        int length;
        bool success;

        public int Index
        {
            get { return index; }
        }

        public int Length
        {
            get { return length; }
        }

        public Boolean Success
        {
            get { return success; }
        }

        public MatchEx(int index, int length, Boolean success)
        {
            this.index = index;
            this.length = length;
            this.success = success;
        }
    }

    interface IHighlighter
    {
        void Parse(IBuffer buffer, List<AttributeInfo> attributeList);
    }
}
