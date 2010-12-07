using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using YYS.Parser;

namespace test {

    class Highlight : IHighlight {
        private YYS.Parser.Attribute defaultAttr = new YYS.Parser.Attribute(Color.Black);

        #region IHighlight メンバ

        public YYS.Parser.Attribute getDefault() {
            return defaultAttr;
        }

        public List<Rule> getRules() {
            var rules = new List<Rule>();
            //rules.Add(new ScanRule("#START", "#END", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("[[", "]]", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            //rules.Add(new MultiLineRule("/'", "'/", new AsControls.Parser.Attribute(Color.LightBlue)));
            //rules.Add(new MultiLineRule("/*", "*/", new YYS.Parser.Attribute(Color.Green)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            //rules.Add(new EndLineRule("//", new AsControls.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
            rules.Add(new KeywordRule(new string[] { "if" }, new YYS.Parser.Attribute(Color.Blue, AttrType.Bold | AttrType.UnderLine)));
            rules.Add(new MultiLineRule("default.comment2", "/=", "=/", new YYS.Parser.Attribute(Color.DarkGreen)));
            //rules.Add(new MultiLineRule("default.comment3", "/-", "-/", new YYS.Parser.Attribute(Color.DarkGray)));
            return rules;
        }

        #endregion
    }

    class Highlight2 : IHighlight {
        private YYS.Parser.Attribute defaultAttr = new YYS.Parser.Attribute(Color.Red);

        #region IHighlight メンバ

        public YYS.Parser.Attribute getDefault() {
            return defaultAttr;
        }

        public List<Rule> getRules() {
            var rules = new List<Rule>();
            //rules.Add(new ScanRule("#START", "#END", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("[[", "]]", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            //rules.Add(new MultiLineRule("/'", "'/", new AsControls.Parser.Attribute(Color.LightBlue)));
            //rules.Add(new MultiLineRule("/*", "*/", new YYS.Parser.Attribute(Color.Green)));
            //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
            //rules.Add(new EndLineRule("//", new AsControls.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
            rules.Add(new MultiLineRule("default.comment", "/*", "*/", new YYS.Parser.Attribute(Color.Red)));


            return rules;
        }

        #endregion
    }

    class JSHighlight : IHighlight {
        private YYS.Parser.Attribute defaultAttr = new YYS.Parser.Attribute(Color.Black);
        private List<Rule> rules;

        #region IHighlight メンバ

        public YYS.Parser.Attribute getDefault() {
            return defaultAttr;
        }

        public List<Rule> getRules() {
            if(rules == null){
                rules = new List<Rule>();
                rules.Add(new KeywordRule(new string[] { "if", "function" }, new YYS.Parser.Attribute(Color.Blue, AttrType.Bold)));
                //rules.Add(new ScanRule("#START", "#END", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
                //rules.Add(new EncloseRule("[[", "]]", new AsControls.Parser.Attribute(Color.Red, AttrType.Image)));
                //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
                //rules.Add(new MultiLineRule("/'", "'/", new AsControls.Parser.Attribute(Color.LightBlue)));
                //rules.Add(new MultiLineRule("/*", "*/", new YYS.Parser.Attribute(Color.Green)));
                //rules.Add(new EncloseRule("\"", "\"", new AsControls.Parser.Attribute(Color.Brown), '\\'));
                rules.Add(new EndLineRule("//", new YYS.Parser.Attribute(Color.DarkGreen)));
                rules.Add(new MultiLineRule("default.comment", "/*", "*/", new YYS.Parser.Attribute(Color.DarkGreen)));
            }

            return rules;
        }

        #endregion
    }
}
