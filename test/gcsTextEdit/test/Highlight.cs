using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using YYS.Parser;

namespace test {
    class Highlight : IHighlight {
        #region IHighlight メンバ
        private YYS.Parser.Attribute defaultAttr = new YYS.Parser.Attribute(Color.Black);
        public YYS.Parser.Attribute getDefault() {
            return defaultAttr;
        }

        public List<Rule> getRules() {
            var rules = new List<Rule>();
            //rules.Add(new ScanRule("#START", "#END", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("[[", "]]", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            //rules.Add(new MultiLineRule("/'", "'/", new AsControls.Parser.Attribute(Color.LightBlue)));
            rules.Add(new MultiLineRule("/*", "*/", new YYS.Parser.Attribute(Color.Green)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            //rules.Add(new EndLineRule("//", new AsControls.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
            rules.Add(new KeywordRule(new string[] { "if" }, new YYS.Parser.Attribute(Color.Blue, AttrType.Bold | AttrType.UnderLine)));
            return rules;
        }

        #endregion
    }
}
