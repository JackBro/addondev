using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AsControls.Parser;

namespace AsControls
{
    public static class isLineHeadCommented {
        public static int firstout = 0;
        public static int firstin = 1;
    }

    public static class commentTransition {
        public static int lastout=0;
        public static int firstlastrev=1;
        public static int firstlastsame=2;
        public static int lastin = 3;
    }

    public class Line
    {
        private IText text;
        private List<Token> rules;
        //public bool IsBlockOnly { get; set; }
        //public bool IsImageExist { get; set; }
        //public string p;
        public Block Block { get; set; }

        public List<Token> Rules {
            get { return rules; }
            set { rules = value; }
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
            //p = string.Empty;
            Block = new Block();
            this.text = new LineBuffer(text);
            rules = new List<Token>();
        }

        public void SetText(string text)
        {
            this.text = new LineBuffer(text);
            rules.Clear();
        }
    }
}
