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
        private List<AttributeInfo> attributeInfoList;
        private List<Rule> rules;

        public List<Rule> Rules {
            get { return rules; }
            set { rules = value; }
        }

        public List<AttributeInfo> AttributeList
        {
            get { return this.attributeInfoList; }
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

            attributeInfoList = new List<AttributeInfo>();
            for (int i = 0; i < this.text.Length; i++)
            {
                attributeInfoList.Add(new AttributeInfo());
            }

            rules = new List<Rule>();
        }

        public void SetText(string text)
        {
            this.text = new LineBuffer(text);

            attributeInfoList.Clear();
            for (int i = 0; i < this.text.Length; i++)
            {
                attributeInfoList.Add(new AttributeInfo());
            }

            rules.Clear();
        }
    }
}
