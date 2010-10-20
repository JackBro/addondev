using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS.Parser {
    class Highlight : IHighlight {
        private Color textcolor;
        public Highlight(Color textcolor) {
            this.textcolor = textcolor;
        }

        #region IHighlight メンバ

        public Attribute getDefault() {
            return new Attribute(textcolor);
        }

        public List<Rule> getRules() {
            var rules = new List<Rule>();
            //rules.Add(new EndLineRule("//", new YYS.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
            //rules.Add(new MultiLineRule("/*", "*/", new YYS.Parser.Attribute(Color.Red)));
            return rules;
        }

        #endregion
    }
}
