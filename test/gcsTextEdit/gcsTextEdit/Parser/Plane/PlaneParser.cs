using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS.Parser.Plane {
    class PlaneParser : IParser {

        private Attribute defaultAttr;

        #region IParser メンバ

        public void SetHighlight(IHighlight highlight) {
            defaultAttr = highlight.getDefault();
        }

        public void AddHighlight(string partionID, IHighlight highlight) {}

        public bool Parse(List<Line> text, int ad, int s, int e) {
            for (int i = s; i <= e; ++i) {
                text[i].Tokens = new List<Token> { new Token { ad = 0, len = text[i].Length, attr = defaultAttr } };
            }
            return false;
        }

        #endregion
    }
}
