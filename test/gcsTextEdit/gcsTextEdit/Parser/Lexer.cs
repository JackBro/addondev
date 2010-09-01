using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {


    enum TokenType {
        EOS,
        TAB, // Tab
        WSP, // 半角スペース
        ZSP, // 全角スペース
        TXT, // 普通の字
    }

    class WordInfo
    {
        public Color color;
        public Boolean Bold;
        public Boolean UnderLine;
    }

    abstract class Attribute {
        public int ad;
        public int len;
        public Color forecolor;
        public Boolean bold;
        public Boolean clickable;
        public Boolean underline;

        public abstract string getWord();
    }

    //[[..]]end
    class EncloseAttribute :Attribute{
        public string start;
        public string end;

        public override string getWord() {
            return start;
        }

        public EncloseAttribute(Color forecolor, bool bold, bool underline) {
            this.forecolor = forecolor;
            this.bold = bold;
            this.underline = underline;
        }

        public EncloseAttribute(EncloseAttribute attr) {
            start = attr.start;
            end = attr.end;
            forecolor = attr.forecolor;
            bold = attr.bold;
            underline = attr.underline;
        }
    }

    // //. ..\nend
    class EndLineAttribute :Attribute{
        public string start;
        public int len;
        public override string getWord() {
            return start;
        }

        public EndLineAttribute(string start, Color forecolor, bool bold, bool underline) {
            this.start = start;
            this.forecolor = forecolor;
            this.bold = bold;
            this.underline = underline;
        }

        public EndLineAttribute(EndLineAttribute attr) {
            start = attr.start;
            forecolor = attr.forecolor;
            bold = attr.bold;
            underline = attr.underline;
        }
    }

    // >>.. end
    class LineAttribute : Attribute {
        public string start;
        public int len;
        public override string getWord() {
            return start;
        }

        public LineAttribute(string start, Color forecolor, bool bold, bool underline) {
            this.start = start;
            this.forecolor = forecolor;
            this.bold = bold;
            this.underline = underline;
        }

        public LineAttribute(LineAttribute attr) {
            start = attr.start;
            forecolor = attr.forecolor;
            bold = attr.bold;
            underline = attr.underline;
        }
    }

    class Highlight {
        public int ad;
        public int len;
        public Color forecolor;
        public WordInfo info;
    }


    class LexerReader {
        private bool unget_p = false;
        private Char? ch;
        private int cnt;
        public string src { get; set; }

        public LexerReader(string src) {
            this.src = src;
            cnt = 0;
        }

        public char? read() {
            if (unget_p) {
                unget_p = false;
            }
            else {
                if (cnt > src.Length - 1) {
                    ch = null;
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
        private String val;
        public LexerReader reader;
        private int offset;

        private Attribute resultAttr;

        private Dictionary<String, LineAttribute> LineattrDic = new Dictionary<String, LineAttribute>();
        //private Dictionary<String, SingleLineAttribute> SingleLineattrDic = new Dictionary<String, SingleLineAttribute>();
        //private Dictionary<String, KeywordAttribute> keywordattrDic = new Dictionary<String, KeywordAttribute>();

        public void AddAttribute(Attribute attr) {
            //keywordattrDic.Add(attr.getWord(), attr as KeywordAttribute);
        }

        public Attribute getAttribute() {
            return resultAttr;
        }

        public TokenType token() {
            return tok;
        }

        public Lexer(string src) {
            reader = new LexerReader(src);
        }

        public string value() {
            return val;
        }

        public bool advance() {
            //skipWhiteSpace();
            //tok = TokenType.NONE;
            char? c = reader.read();
            if (c == null) {
                tok = TokenType.EOS;
                return false;
            }
            switch (c) {
                case ' ':
                    break;

                //case '　':
                case (char)0x3000:
                    break;

                case '\t':
                    break;
                    
                default:
                    if (Char.IsSymbol((char)c)) {

                    } else {
                        
                    }
                    break;

            }
            lexSymbol(c.ToString());
            
            return true;
        }

        private void skipWhiteSpace() {
            char? c = reader.read();
            char ch = (char)c;

            while ((c != null)
                    && (Char.IsWhiteSpace(ch) || (ch == '\t') || (ch == '\n') || (ch == '\r'))) {
                c = reader.read();
                ch = (char)c;
            }
            reader.unread((char)c);
        }

        private void lexSymbol(string initstr) {
            //offset = reader.offset();

            ////tok = TokenType.SYMBOL;
            //StringBuilder buf = new StringBuilder();
            //if (initstr != null) buf.Append(initstr);

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

            //while (true) {
            //    char? c = reader.read();
            //    if (c==null) {
            //        // throw new Exception("ファイルの終わりに到達しました。");
            //        tok = TokenType.EOS;
            //        break;
            //    }
            //    buf.Append((char)c);

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
            //}
        }

    }
}
