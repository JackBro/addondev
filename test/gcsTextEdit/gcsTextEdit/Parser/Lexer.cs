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
        Image
    }


    //class WordInfo
    //{
    //    public Color color;
    //    public Boolean Bold;
    //    public Boolean UnderLine;
    //}

    //abstract class Attribute {
    //    public int ad;
    //    public int len;
    //    public Color forecolor;
    //    public Boolean bold;
    //    public Boolean clickable;
    //    public Boolean underline;

    //    public abstract string getWord();
    //    public abstract int exer(Lexer lex);
    //}


    abstract class Element {
        public Attribute attr;
        public TokenType token;
        public string start { get; set; }
        public abstract int exer(Lexer lex);
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
            int endindex = lex.reader.Src.IndexOf(end, offset);
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

    //class Highlight {
    //    public int ad;
    //    public int len;
    //    public Color forecolor;
    //    public WordInfo info;
    //}


    class LexerReader {
        private bool unget_p = false;
        private int ch;
        private int cnt;
        private string src;
        public string Src {
            get { return src; }
            set{
                this.src = value;
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

    class Lexer {

        private TokenType tok;
        private String value;
        public LexerReader reader;
        //private int offset;

        private Element resultAttr;

        private Dictionary<String, Element> elemDic = new Dictionary<String, Element>();
        //private Dictionary<String, SingleLineAttribute> SingleLineattrDic = new Dictionary<String, SingleLineAttribute>();
        //private Dictionary<String, KeywordAttribute> keywordattrDic = new Dictionary<String, KeywordAttribute>();

        public void AddElement(Element elem) {
            elemDic.Add(elem.start, elem);
        }

        public Element getAttribute() {
            return resultAttr;
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
            resultAttr = null;
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

        private void lexSymbol(string initstr) {
            //offset = reader.offset();

            ////tok = TokenType.SYMBOL;
            StringBuilder buf = new StringBuilder();
            if (initstr != null) buf.Append(initstr);

            //if(reserved.ContainsKey(buf.ToString())){
            //    var attr = reserved[buf.ToString()];
            //    if(attr is LineAttribute){
            //        string end = (attr as LineAttribute).end;
            //        string src = reader.src;
            //        int index = src.IndexOf(end, offset);
            //        if (index >= 0) {
            //            attr.ad = offset;
            //            attr.len = index - offset + end.Length;
            //            reader.setoffset(offset + index - offset + end.Length);
            //            tok = TokenType.ATTR;

            //            resultAttr = new LineAttribute(attr as LineAttribute);

            //        }
            //    }else{
            //        attr.ad = offset;
                    

            //        tok = TokenType.ATTR;
            //        if (attr is SingleLineAttribute) {
            //            resultAttr = new SingleLineAttribute(attr as SingleLineAttribute);
            //            attr.len = reader.src.Length - offset;
            //        }
            //        else {
            //            resultAttr = new KeywordAttribute(attr as KeywordAttribute);
            //            attr.len = attr.getWord().Length;
            //        }

            //        reader.setoffset(offset + attr.len + 1);
            //    }
            //    return;
            //}

            while (true) {

                if (elemDic.ContainsKey(buf.ToString())) {
                    int offset = reader.offset();
                    Element elem = elemDic[buf.ToString()];
                    int index = elem.exer(this);
                    if (index < 0) {
                        reader.setoffset(reader.offset() + elem.start.Length + 1);
                        return;
                    }

                    resultAttr = elem;
                    value = reader.Src.Substring(offset, index - offset);
                    tok = elem.token;

                    reader.setoffset(index);

                    return;
                }

                int c = reader.read();
                if (c == -1) {
                    // throw new Exception("ファイルの終わりに到達しました。");
                    tok = TokenType.EOS;
                    break;
                }
                buf.Append((char)c);

            //    if (reserved.ContainsKey(buf.ToString())) {
            //        var attr = reserved[buf.ToString()];
            //        if (attr is LineAttribute) {
            //            string end = (attr as LineAttribute).end;
            //            string src = reader.src;
            //            int index = src.IndexOf(end, offset);
            //            if (index >= 0) {
            //                attr.ad = offset;
            //                attr.len = index - offset + end.Length;
            //                reader.setoffset(offset + index - offset + end.Length);
            //                tok = TokenType.ATTR;

            //                resultAttr = new LineAttribute(attr as LineAttribute);
            //            }
            //        }
            //        else {
            //            attr.ad = offset;


            //            tok = TokenType.ATTR;
            //            if (attr is SingleLineAttribute) {
            //                resultAttr = new SingleLineAttribute(attr as SingleLineAttribute);
            //                attr.len = reader.src.Length - offset;
            //            }
            //            else {
            //                resultAttr = new KeywordAttribute(attr as KeywordAttribute);
            //                attr.len = attr.getWord().Length;
            //            }

            //            reader.setoffset(offset + attr.len + 1);
            //        }
            //        return;
            //    }
            }
        }

    }
}
