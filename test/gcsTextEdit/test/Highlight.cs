using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using AsControls.Parser;

namespace test {
    class Highlight : IHighlight {
        #region IHighlight メンバ

        public AsControls.Parser.Attribute getDefault() {
            return new AsControls.Parser.Attribute(Color.Black);
        }

        public List<Rule> getRules() {
            var rules = new List<Rule>();
            rules.Add(new EncloseRule("[[", "]]", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            rules.Add(new MultiLineRule("/'", "'/", new AsControls.Parser.Attribute(Color.LightBlue)));
            rules.Add(new MultiLineRule("/*", "*/", new AsControls.Parser.Attribute(Color.Green)));
            rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            rules.Add(new EndLineRule("//", new AsControls.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
            return rules;
        }

        #endregion
    }
}
