using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS.Parser {
    class DefaultHighlight : IHighlight {
        //private Color textcolor;
        private Attribute defaultAttr;
        private List<Rule> rules;
        public DefaultHighlight(Color textcolor) {
            //this.textcolor = textcolor;
            defaultAttr = new Attribute(textcolor);
        }

        #region IHighlight メンバ

        public Attribute getDefault() {
            return defaultAttr;
        }

        public List<Rule> getRules() {
            if (rules == null) {
                rules = new List<Rule>();
                //rules.Add(new EncloseRule("[[", "]]", new YYS.Parser.Attribute(Color.Red, AttrType.Image)));
                //rules.Add(new EndLineRule("//", new YYS.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
                //rules.Add(new MultiLineRule("/*", "*/", new YYS.Parser.Attribute(Color.Red)));
            }
            return rules;
        }

        #endregion
    }
}
