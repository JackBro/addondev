﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Sgry.Azuki.Highlighter;

namespace wiki.control {
    class EditorHighlighter : KeywordHighlighter {
        public List<string> keyword1;

        public EditorHighlighter() {
            //this.
            AddEnclosure("[[", "]]", Sgry.Azuki.CharClass.Heading6 + 1);
            AddLineHighlight("//", Sgry.Azuki.CharClass.Comment);
        }
    }
}
