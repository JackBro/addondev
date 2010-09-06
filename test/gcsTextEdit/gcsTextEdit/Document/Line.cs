using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AsControls.Parser;

namespace AsControls
{
    public class Line
    {
        private IBuffer text;
        private List<Rule> rules;

        public List<Rule> Rules {
            get { return rules; }
            set { rules = value; }
        }

        public IBuffer Text
        {
            get { return text; }
        }

        public int Length
        {
            get { return text.Length; }
        }

        public Line(string text)
        {
            this.text = new LineBuffer(text);
            rules = new List<Rule>();
        }

        public void SetText(string text)
        {
            this.text = new LineBuffer(text);
            rules.Clear();
        }
    }
}
