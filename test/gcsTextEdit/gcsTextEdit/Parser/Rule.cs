using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {

    [Flags]
    public enum AttrType {
        Normal=1,
        Bold=2,
        UnderLine=4,
        Strike=8,
        Link=16,
        Image=32
    }

    public class Attribute {
        public readonly Color color;
        public AttrType type;

        public Attribute(Color color, AttrType type) {
            this.color = color;
            this.type = type;
        }
        public Attribute(Color color) {
            this.type = AttrType.Normal;
        }
    }

    public enum TokenType {
        EOS,
        TXT, // 普通の字
        MultiLine,
        Enclose,
        EndLine,
        Line,
        Image,
        Keyword,
        Element,
        Number
    }

    public abstract class Rule {
        public Attribute attr;
        public TokenType token;
        public string start { get; set; }
        public abstract int exer(Lexer lex);
        public int startIndex;
        public int len;
    }

    public class TextRule : Rule {
        public TextRule(Attribute attr) {
            this.attr = attr;
            this.token = TokenType.TXT;
        }

        public override int exer(Lexer lex) {
            return 0;
        }
    }

    // /*..*/end
    public class MultiLineRule : Rule {
        public string end;
        public MultiLineRule() {
            token = TokenType.Enclose;
        }
        public MultiLineRule(string start, string end, Attribute attr) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            token = TokenType.MultiLine;
        }

        public override int exer(Lexer lex) {
            int offset = lex.reader.offset();
            int index = lex.reader.Src.IndexOf(end, offset);

            if (index < 0) return index;

            int endindex = lex.reader.Src.IndexOf(end, offset) + this.end.Length;
            return endindex;
        }
    }

    //[[..]]end
    public class EncloseRule : Rule {
        public string end;
        public EncloseRule() {
            token = TokenType.Enclose;
        }
        public EncloseRule(string start, string end, Attribute attr) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            token = TokenType.Enclose;
        }

        public override int exer(Lexer lex) {
            int offset = lex.reader.offset();
            int index = lex.reader.Src.IndexOf(end, offset);

            if (index < 0) return index;

            int endindex = lex.reader.Src.IndexOf(end, offset) + this.end.Length;
            return endindex;
        }
    }

    // //. ..\nend
    class EndLineRule : Rule {

        public EndLineRule(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.EndLine;
        }
        public override int exer(Lexer lex) {
            int offset = lex.reader.offset();
            return lex.reader.Src.Length;
        }
    }

    // >>.. end
    class LineRule : Rule {
        private Char[] c = new char[3];

        public LineRule(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.Line;
            c[0] = '\t';
            c[1] = ' ';
            c[2] = '\x3000';
        }
        public override int exer(Lexer lex) {
            int offset = lex.reader.offset();
            string src = lex.reader.Src;
            int endindex = src.IndexOfAny(c, offset);
            //int index = lex.reader.src.Length - offset;
            if (endindex < 0) {
                endindex = src.Length;
            }
            else {
                endindex = src.Length - endindex;
            }
            return endindex;
        }
    }

    //##name
    class ImageRule : MultiLineRule {

        public ImageRule(string start, string end, Attribute attr) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            token = TokenType.Image;
        }
    }

    class KeywordRule : Rule {
        public KeywordRule(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.Keyword;
        }

        public override int exer(Lexer lex) {
            return lex.reader.offset();
        }
    }
}
