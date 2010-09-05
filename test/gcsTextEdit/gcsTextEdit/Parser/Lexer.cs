using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {


    //enum TokenType {
    //    EOS,
    //    TAB, // Tab
    //    WSP, // 半角スペース
    //    ZSP, // 全角スペース
    //    TXT, // 普通の字
    //    EncloseAttr,
    //    EndLineAttr,
    //    LineAttr,
    //    ImageAttr
    //}

    public class Attribute {
        public readonly Color color;
        public readonly bool islink;
        public readonly bool isbold;
        public readonly bool isunderline;

        public Attribute(Color color, bool islink, bool isbold, bool isunderline) {
            this.color = color;
            this.islink = islink;
            this.isbold = isbold;
            this.isunderline = isunderline;
        }
    }

    enum TokenType {
        EOS,
        TXT, // 普通の字
        Enclose,
        EndLine,
        Line,
        Image,
        Keyword
    }

    abstract class Element {
        public Attribute attr;
        public TokenType token;
        public string start { get; set; }
        public abstract int exer(Lexer lex);
    }

    class TextElement : Element {
        public TextElement(Attribute attr) {
            this.attr = attr;
            this.token = TokenType.TXT;
        }

        public override int exer(Lexer lex) {
            //throw new NotImplementedException();
            return 0;
        }
    }

    //[[..]]end
    class EncloseElement :Element{
        public string end;
        public EncloseElement() {
            token = TokenType.Enclose;
        }
        public EncloseElement(string start, string end, Attribute attr) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            token = TokenType.Enclose;
        }

        public override int exer(Lexer lex) {
            //throw new NotImplementedException();
            int offset = lex.reader.offset();
            int index = lex.reader.Src.IndexOf(end, offset);
            
            if (index < 0) return index;

            int endindex = lex.reader.Src.IndexOf(end, offset) + this.end.Length;
            return endindex;
        }
    }

    // //. ..\nend
    class EndLineElement :Element{

        public EndLineElement(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.EndLine;
        }
        public override int exer(Lexer lex) {
            //throw new NotImplementedException();
            int offset = lex.reader.offset();
            return lex.reader.Src.Length;
        }
    }

    // >>.. end
    class LineElement : Element {
        private Char[] c = new char[3];

        public LineElement(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.Line;
            c[0] = '\t';
            c[1] = ' ';
            c[2] = '　';
        }
        public override int exer(Lexer lex) {
            //throw new NotImplementedException();
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

    class KeywordElement : Element {
        public KeywordElement(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.Keyword;
        }

        public override int exer(Lexer lex) {
            throw new NotImplementedException();
        }
    }


    //##name
    class ImageElement : EncloseElement {

        public Size size;

        public ImageElement(string start, string end, Attribute attr) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            token = TokenType.Image;
        }
    }


    class LexerReader {
        private bool unget_p = false;
        private int ch;
        private int cnt;
        private string src;
        public string Src {
            get { return src; }
            set{
                this.src = value;
                ch = 0;
                cnt = 0;
                unget_p = false;
            } 
        }

        public LexerReader(string src) {
            this.src = src;
            cnt = 0;
        }

        public LexerReader() {
            cnt = 0;
        }

        public int read() {
            if (unget_p) {
                unget_p = false;
            }
            else {
                if (cnt > src.Length - 1) {
                    ch = -1;
                }
                else {
                    ch = src[cnt];
                    cnt++;
                }
            }
            return ch;
        }

        public void unread(char c) {
            unget_p = true;
        }

        public int offset() {
            return cnt;
        }

        public void setoffset(int offset) {
            cnt = offset;
        }
    }

    //delegate void TokenEventHandler(object sender, int start, int len, Element e);

    class Lexer {

        //public event TokenEventHandler tokenEventHandler = null;


        private TokenType tok;
        private String value;
        public LexerReader reader;
        //private int offset;

        private Element resultElement;

        //private int preoffset=0;

        private Element defaultElement;
        private Dictionary<String, Element> elemDic = new Dictionary<String, Element>();
        //private Dictionary<String, SingleLineAttribute> SingleLineattrDic = new Dictionary<String, SingleLineAttribute>();
        //private Dictionary<String, KeywordAttribute> keywordattrDic = new Dictionary<String, KeywordAttribute>();

        public void AddElement(Element elem) {
            elemDic.Add(elem.start, elem);
        }

        public void AddDefaultElement(Element elem) {
            defaultElement = elem;
        }

        public Element getElement() {
            return resultElement;
        }

        public string Src {
            get { return reader.Src; }
            set {
                //preoffset = 0;
                if (reader == null) {
                    reader = new LexerReader(value);
                }
                else {
                    reader.Src = value;
                    //reader.setoffset(0);
                    tok = TokenType.TXT;
                    value = null;
                }
            }
        }

        public TokenType token {
            get { return tok; }
        }

        public Lexer(string src) {

            reader = new LexerReader(src);
        }

        public Lexer() {
            reader = new LexerReader();
        }

        public string Value {
            get { return value; }
        }

        public int Offset {
            get {
                return reader.offset();
            }
        }

        public bool advance() {
            //skipWhiteSpace();
            //resultAttr = null;
            tok = TokenType.TXT;

            int c = reader.read();
            if (c == -1) {
                tok = TokenType.EOS;
                return false;
            }
            switch (c) {
                case ' ':
                    break;

                //case '　':
                case 0x3000:
                    break;

                case '\t':
                    break;

                default:
                    if( (c>=33 && c<=47)
                        || (c >= 58 && c <= 74)
                        || (c >= 91 && c <= 96)
                        || (c >= 123 && c <= 126)){

                    //if (Char.IsSymbol((char)c)) {
                        lexSymbol(((char)c).ToString());
                    } else {
                        
                    }
                    break;

            }
            return true;
        }

        private void skipWhiteSpace() {
            int c = reader.read();
            char ch = (char)c;

            while ((c != -1)
                    && (Char.IsWhiteSpace(ch) || (ch == '\t') || (ch == '\n') || (ch == '\r'))) {
                c = reader.read();
                ch = (char)c;
            }
            reader.unread((char)c);
        }

        //private void lexText(string initstr) {
        //    StringBuilder buf = new StringBuilder();
        //    if (initstr != null) buf.Append(initstr);

        //    while (true) {
        //        if (elemDic.ContainsKey(buf.ToString())) {

        //        }

        //        int c = reader.read();
        //        if (c == -1) {
        //            tok = TokenType.EOS;
        //            break;
        //        }
        //        buf.Append((char)c);
        //    }
        //}

        private void lexSymbol(string initstr) {
            //offset = reader.offset();

            ////tok = TokenType.SYMBOL;
            StringBuilder buf = new StringBuilder();
            if (initstr != null) buf.Append(initstr);

            while (true) {

                if (elemDic.ContainsKey(buf.ToString())) {
                    
                    Element elem = elemDic[buf.ToString()];
                    int offset = reader.offset()-elem.start.Length;

                    int index = elem.exer(this);
                    if (index < 0) {
                        reader.setoffset(reader.offset() + 1);
                        return;
                    }

                    //if (preoffset != offset) {
                    //    tok = TokenType.TXT;
                    //    value = reader.Src.Substring(preoffset, offset - preoffset);
                    //    if (tokenEventHandler != null) {
                    //        tokenEventHandler(this, preoffset, offset - preoffset, defaultElement);
                    //    }
                    //}

                    value = reader.Src.Substring(offset, index - offset);
                    tok = elem.token;
                    resultElement = elem;
                    //if (tokenEventHandler != null) {
                    //    tokenEventHandler(this, offset, index - offset, elem);
                    //}
                    reader.setoffset(index);

                    //preoffset = offset;
                    return;
                }

                int c = reader.read();
                if (c == -1) {
                    tok = TokenType.EOS;
                    break;
                }
                buf.Append((char)c);
            }
        }

    }
}
