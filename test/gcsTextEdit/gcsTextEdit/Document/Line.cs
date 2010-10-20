using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using YYS.Parser;

namespace YYS
{
    public class Line
    {
        private IText text;
        private List<Token> tokens;
        public Block Block { get; set; }

        public List<Token> Tokens {
            get { return tokens; }
            set { tokens = value; }
        }

        public IText Text
        {
            get { return text; }
        }

        public int Length
        {
            get { return text.Length; }
        }

        public Line(string text)
        {
            Block = new Block();
            this.text = new TextBuffer(text);
            tokens = new List<Token>();
        }

        public void SetText(string text)
        {
            this.text = new TextBuffer(text);
            tokens.Clear();
        }
    }
}
