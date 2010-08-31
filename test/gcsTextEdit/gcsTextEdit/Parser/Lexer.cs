using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {


    enum TokenType {
        EOS, // 普通の字
        NONE,
        ATTR //クリッカブル
    }

    enum AttrType {
        TAB, // Tab
        WSP, // 半角スペース
        ZSP, // 全角スペース
        TXT, // 普通の字
        CLICKABLE //クリッカブル
    }

    class WordInfo
    {
        public Color color;
        public Boolean Bold;
        public Boolean UnderLine;
        public AttrType type;
    }

    abstract class Attribute {
        public int ad;
        public int len;
        public Color forecolor;
        public Boolean bold;
        public Boolean underline;
        public AttrType type;

        public abstract string getWord();
    }

    class LineAttribute :Attribute{
        public string start;
        public string end;

        public override string getWord() {
            return start;
        }

        public LineAttribute(Color forecolor, bool bold, bool underline) {
            this.forecolor = forecolor;
            this.bold = bold;
            this.underline = underline;
        }

        public LineAttribute(LineAttribute attr) {
            start = attr.start;
            end = attr.end;
            forecolor = attr.forecolor;
            bold = attr.bold;
            underline = attr.underline;
            type = attr.type;
        }
    }

    class SingleLineAttribute :Attribute{
        public string start;
        public override string getWord() {
            return start;
        }

        public SingleLineAttribute(string start, Color forecolor, bool bold, bool underline) {
            this.start = start;
            this.forecolor = forecolor;
            this.bold = bold;
            this.underline = underline;
        }

        public SingleLineAttribute(SingleLineAttribute attr) {
            start = attr.start;
            forecolor = attr.forecolor;
            bold = attr.bold;
            underline = attr.underline;
            type = attr.type;
        }
    }

    class WordAttribute :Attribute{
        public string word;
        public override string getWord() {
            return word;
        }

        public WordAttribute(Color forecolor, bool bold, bool underline) {
            this.forecolor = forecolor;
            this.bold = bold;
            this.underline = underline;
        }

        public WordAttribute(WordAttribute attr) {
            word = attr.word;
            forecolor = attr.forecolor;
            bold = attr.bold;
            underline = attr.underline;
            type = attr.type;
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

        private Dictionary<String, Attribute> reserved = new Dictionary<String, Attribute>();

        public void AddAttribute(Attribute attr) {
            reserved.Add(attr.getWord(), attr);
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
            tok = TokenType.NONE;
            char? c = reader.read();
            if (c == null) {
                tok = TokenType.EOS;
                return false;
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
            offset = reader.offset();

            //tok = TokenType.SYMBOL;
            StringBuilder buf = new StringBuilder();
            if (initstr != null) buf.Append(initstr);

            if(reserved.ContainsKey(buf.ToString())){
                var attr = reserved[buf.ToString()];
                if(attr is LineAttribute){
                    string end = (attr as LineAttribute).end;
                    string src = reader.src;
                    int index = src.IndexOf(end, offset);
                    if (index >= 0) {
                        attr.ad = offset;
                        attr.len = index - offset + end.Length;
                        reader.setoffset(offset + index - offset + end.Length);
                        tok = TokenType.ATTR;

                        resultAttr = new LineAttribute(attr as LineAttribute);

                    }
                }else{
                    attr.ad = offset;
                    

                    tok = TokenType.ATTR;
                    if (attr is SingleLineAttribute) {
                        resultAttr = new SingleLineAttribute(attr as SingleLineAttribute);
                        attr.len = reader.src.Length - offset;
                    }
                    else {
                        resultAttr = new WordAttribute(attr as WordAttribute);
                        attr.len = attr.getWord().Length;
                    }

                    reader.setoffset(offset + attr.len + 1);
                }
                return;
            }

            while (true) {
                char? c = reader.read();
                if (c==null) {
                    // throw new Exception("ファイルの終わりに到達しました。");
                    tok = TokenType.EOS;
                    break;
                }
                buf.Append((char)c);

                if (reserved.ContainsKey(buf.ToString())) {
                    var attr = reserved[buf.ToString()];
                    if (attr is LineAttribute) {
                        string end = (attr as LineAttribute).end;
                        string src = reader.src;
                        int index = src.IndexOf(end, offset);
                        if (index >= 0) {
                            attr.ad = offset;
                            attr.len = index - offset + end.Length;
                            reader.setoffset(offset + index - offset + end.Length);
                            tok = TokenType.ATTR;

                            resultAttr = new LineAttribute(attr as LineAttribute);
                        }
                    }
                    else {
                        attr.ad = offset;


                        tok = TokenType.ATTR;
                        if (attr is SingleLineAttribute) {
                            resultAttr = new SingleLineAttribute(attr as SingleLineAttribute);
                            attr.len = reader.src.Length - offset;
                        }
                        else {
                            resultAttr = new WordAttribute(attr as WordAttribute);
                            attr.len = attr.getWord().Length;
                        }

                        reader.setoffset(offset + attr.len + 1);
                    }
                    return;
                }
            }
        }

    }
}
