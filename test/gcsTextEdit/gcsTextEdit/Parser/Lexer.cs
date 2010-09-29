using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {

    public  class LexerReader {
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

        public void unread() {
            unget_p = true;
        }

        public int offset() {
            return cnt;
        }

        public void setoffset(int offset) {
            cnt = offset;
        }
    }


    public class Lexer {

        private TokenType tok;
        private String value;
        public LexerReader reader;

        private Rule resultElement;

        public bool isNextLine = false;

        private Dictionary<String, Rule> elemDic = new Dictionary<String, Rule>();
        private Dictionary<String, Rule> keyWordElemDic = new Dictionary<String, Rule>();

        private Dictionary<String, MultiLineRule> encelemDic = new Dictionary<String, MultiLineRule>();

        private Dictionary<String, MultiLineRule> encendelemDic = new Dictionary<String, MultiLineRule>();

        private string fkeys = string.Empty;

        private string fCncEndkeys = string.Empty;

        //public void AddElement(Element elem) {
        //    if (elem is KeywordElement) {
        //        keyWordElemDic.Add(elem.start, elem);
        //    } else {
        //        elemDic.Add(elem.start, elem);
        //    }

        //    string key=string.Empty;
        //    foreach (var item in elemDic.Values) {
        //        key += item.start[0];
        //    }
        //    fkeys = key;
        //}

        public void AddElement(Rule elem) {
            if (elem is KeywordRule) {
                keyWordElemDic.Add(elem.start, elem);
            }
            else if (elem is MultiLineRule) {
                encelemDic.Add(elem.start, (MultiLineRule)elem);
                encendelemDic.Add(((MultiLineRule)elem).end, (MultiLineRule)elem);

                foreach (var item in encendelemDic.Values) {
                    if (!fCncEndkeys.Contains(item.end[0])) {
                        fCncEndkeys += item.end[0];
                    }
                }
                foreach (var item in encelemDic.Values) {
                    if (!fkeys.Contains(item.start[0])) {
                        fkeys += item.start[0];
                    }
                }

            }else{
                elemDic.Add(elem.start, elem);

                foreach (var item in elemDic.Values) {
                    if (!fkeys.Contains(item.start[0])) {
                        fkeys += item.start[0];
                    }
                }
            }

            //fCncEndkeys = string.Empty;
            //foreach (var item in encendelemDic.Values) {
            //    if (!fCncEndkeys.Contains(item.end[0])) {
            //        fCncEndkeys += item.end[0];
            //    }
            //}
            //fkeys = string.Empty;
            //foreach (var item in encelemDic.Values) {
            //    if (!fkeys.Contains(item.start[0])) {
            //        fkeys += item.start[0];
            //    }
            //}
        }

        //public void AddDefaultElement(Rule elem) {
        //    defaultElement = elem;
        //}

        public Rule getElement() {
            return resultElement;
        }

        public string Src {
            get { return reader.Src; }
            set {
                if (reader == null) {
                    reader = new LexerReader(value);
                }
                else {
                    reader.Src = value;
                    tok = TokenType.TXT;
                    value = null;
                    isNextLine = false;
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
                    //if( (c>=33 && c<=47)
                    //    || (c >= 58 && c <= 74)
                    //    || (c >= 91 && c <= 96)
                    //    || (c >= 123 && c <= 126)){

                    ////if (Char.IsSymbol((char)c)) {
                    //    reader.unread(c);
                    //    //lexSymbol(((char)c).ToString());
                    //    lexSymbol();
                    //} else if (Char.IsDigit((char)c)) {
                    //    reader.unread(c);
                    //    lexDigit();

                    //} else {
                    //    reader.unread(c);
                    //    lexKeyWord();
                    //}
                    if (Char.IsDigit((char)c)) {
                        reader.unread();
                        lexDigit();
                    } else if(Util.isIdentifierPart((char)c)){
                        reader.unread();
                        lexKeyWord();
                    } else if(fkeys.Contains((char)c)){
                        //if (Char.IsSymbol((char)c)) {
                        reader.unread();
                        //lexSymbol(((char)c).ToString());
                        lexSymbol();
                    }
                    break;

            }
            return true;
        }

        public bool advance(Block preblock, Block curblock) {
            //skipWhiteSpace();
            //resultAttr = null;
            tok = TokenType.TXT;

            if (Src.Length == 0 && curblock.isLineHeadCmt == 1) {
                curblock.elem = preblock.elem;
                isNextLine = true;
            }

            int c = reader.read();
            if (c == -1) {
                tok = TokenType.EOS;
                return false;
            }
            switch (c) {
                case ' ':
                case 0x3000:
                case '\t':

                default:
                    //if (block.state == BlockState.all || block.state == BlockState.start || block.state == BlockState.end) {
                    if (curblock.isLineHeadCmt == 0) { //0: 行頭がブロックコメントの内部ではない
                        if (Char.IsDigit((char)c)) {
                            reader.unread();
                            lexDigit();
                            //Block = new Block();
                        }
                        else if (Util.isIdentifierPart((char)c)) {
                            reader.unread();
                            lexKeyWord();
                            //Block = new Block();
                        }
                        else if (fkeys.Contains((char)c)) {
                            //if (Char.IsSymbol((char)c)) {
                            reader.unread();
                            //lexSymbol(((char)c).ToString());
                            lexSymbol(curblock);
                        }
                        else {
                            //Block = new Block();
                            //resultElement = defa
                            tok = TokenType.TXT;
                        }
                    }
                    else { //1: 行頭がブロックコメントの内部
                        //if (fCncEndkeys.Contains((char)c)) {
                        if (Src.IndexOf(preblock.elem.end, Offset - 1) >= 0) {
                        //if (Src.IndexOf(preblock.elem.end, Offset) >= 0) {

                            var enelem = encelemDic[preblock.elem.start];

                            //if (Char.IsSymbol((char)c)) {
                            int index = Src.IndexOf(preblock.elem.end, Offset - 1);
                            //int index = Src.IndexOf(preblock.elem.end, Offset);
                            //reader.unread();
                            reader.setoffset(index + preblock.elem.end.Length);
                            //lexSymbol(((char)c).ToString());
                            //Block = EncEndlexSymbol(block);
                            enelem.startIndex = 0;
                            enelem.len = index + preblock.elem.end.Length;
                            tok = enelem.token;
                            resultElement = enelem;

                            curblock.elem = preblock.elem;

                            isNextLine = false;
                            //if(){
                            //    Block = new Block { elem = enelem, state= BlockState.all };
                            //}else{
                            //Block = new Block { elem = enelem, state = BlockState.end };
                            //}
                        }
                        else if (Offset - 1 == 0) {
                        //else if (Offset - 1 == 0) {
                            var enelem = encelemDic[preblock.elem.start];

                            //Block = new Block { elem = preblock.elem, state = BlockState.all };
                            reader.setoffset(Src.Length);
                            enelem.startIndex = 0;
                            enelem.len = Src.Length;
                            tok = enelem.token;
                            resultElement = enelem;

                            //Block = new Block { elem = enelem, state = BlockState.all };
                            curblock.elem = preblock.elem;

                            isNextLine = true;

                        }
                        else {
                            if (Char.IsDigit((char)c)) {
                                reader.unread();
                                lexDigit();
                                //Block = new Block();
                            }
                            else if (Util.isIdentifierPart((char)c)) {
                                reader.unread();
                                lexKeyWord();
                                //Block = new Block();
                            }
                            else if (fkeys.Contains((char)c)) {
                                //if (Char.IsSymbol((char)c)) {
                                reader.unread();
                                //lexSymbol(((char)c).ToString());
                                lexSymbol();
                            }
                            else {
                                //Block = new Block();
                                tok = TokenType.TXT;
                            }
                        }

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
            reader.unread();
        }

        private void lexDigit() {
            int num = 0;
            while (true) {
                int c = reader.read();
                if (c < 0) {
                    break;
                }
                if (!Char.IsDigit((char)c)) {
                    reader.unread();
                    break;
                }
                num = (num * 10) + (c - '0');
            }
            value = num.ToString();
            tok = TokenType.Number;		
        }	

        private void lexKeyWord() {
            //offset = reader.offset();
            int offset = reader.offset()-1;

            //tok = TokenType.Keyword;
            StringBuilder buf = new StringBuilder();
            while (true) {
                int c = reader.read();
                if (c < 0) {
                    // throw new Exception("ファイルの終わりに到達しました。");
                    //tok = TokenType.EOS;
                    break;
                }
                if (!Util.isIdentifierPart((char)c)) {
                    reader.unread();
                    break;
                }
                buf.Append((char)c);
            }
            String s = buf.ToString();
            value = s;

            if (keyWordElemDic.ContainsKey(s)) {
                //reader.setoffset(offset);
                //int offset = reader.offset() - value.Length;
                tok = TokenType.Keyword;
                var elem = keyWordElemDic[s];
                elem.startIndex = offset;
                elem.len = value.Length;
                resultElement = elem;
            }
        }

        private void lexSymbol() {
            StringBuilder buf = new StringBuilder();
            //if (initstr != null) buf.Append(initstr);
            int offset = reader.offset()-1;
            while (true) {

                if (elemDic.ContainsKey(buf.ToString())) {
                    
                    Rule elem = elemDic[buf.ToString()];
                    //int offset = reader.offset()-elem.start.Length;
                    //Block = new Block { elem = null, state = BlockState.no };

                    int index = elem.exer(this);
                    if (index < 0) {
                        //int ii = reader.offset();
                        reader.setoffset(reader.offset());
                        reader.unread();
                        return;
                    }
                    //int offset = reader.offset() - value.Length;
                    value = reader.Src.Substring(offset, index - offset);
                    elem.startIndex = offset;
                    elem.len = value.Length;
                    tok = elem.token;
                    resultElement = elem;
                    int offse = reader.offset();
                    reader.setoffset(index);
                    //reader.unread(' ');
                    
                    return;
                }
                else if (encelemDic.ContainsKey(buf.ToString())) {
                    var elem = encelemDic[buf.ToString()];

                    int index = elem.exer(this);
                    if (index < 0) {
                        //int ii = reader.offset();
                        //reader.setoffset(reader.offset() + 1);
                        //reader.unread();

                        //Block = new Block { elem = elem, state = BlockState.start };

                        elem.startIndex = offset;
                        elem.len = Src.Length-offset;
                        tok = elem.token;
                        resultElement = elem;

                        reader.setoffset(reader.Src.Length);
                        //reader.unread();
                        
                        return;
                    }
                    //int offset = reader.offset() - value.Length;
                    value = reader.Src.Substring(offset, index - offset);
                    elem.startIndex = offset;
                    elem.len = value.Length;
                    tok = elem.token;
                    resultElement = elem;
                    int offse = reader.offset();
                    reader.setoffset(index);
                    //reader.unread(' ');
                   
                    //Block = new Block { elem = elem, state = BlockState.start_end };

                    return;
                }

                int c = reader.read();
                if (c == -1) {
                    //tok = TokenType.EOS;
                    //Block = new Block { elem = null, state = BlockState.no };
                    break;
                }
                buf.Append((char)c);
            }
        }

        private void lexSymbol(Block curblock) {
            StringBuilder buf = new StringBuilder();
            //if (initstr != null) buf.Append(initstr);
            int offset = reader.offset() - 1;

            while (true) {

                if (elemDic.ContainsKey(buf.ToString())) {

                    Rule elem = elemDic[buf.ToString()];
                    //int offset = reader.offset()-elem.start.Length;
                    //Block = new Block { elem = null, state = BlockState.no };

                    int index = elem.exer(this);
                    if (index < 0) {
                        //int ii = reader.offset();
                        reader.setoffset(reader.offset());
                        reader.unread();
                        return;
                    }
                    //int offset = reader.offset() - value.Length;
                    value = reader.Src.Substring(offset, index - offset);
                    elem.startIndex = offset;
                    elem.len = value.Length;
                    tok = elem.token;
                    resultElement = elem;
                    int offse = reader.offset();
                    reader.setoffset(index);
                    //reader.unread(' ');

                    return;
                }
                else if (encelemDic.ContainsKey(buf.ToString())) {
                    var elem = encelemDic[buf.ToString()];

                    int index = elem.exer(this);
                    if (index < 0) {
                        //int ii = reader.offset();
                        //reader.setoffset(reader.offset() + 1);
                        //reader.unread();

                        //Block = new Block { elem = elem, state = BlockState.start };

                        elem.startIndex = offset;
                        elem.len = Src.Length - offset;
                        tok = elem.token;
                        resultElement = elem;

                        reader.setoffset(reader.Src.Length);
                        //reader.unread();
                        curblock.elem = elem;
                        isNextLine = true;
                        return;
                    }
                    //int offset = reader.offset() - value.Length;
                    value = reader.Src.Substring(offset, index - offset);
                    elem.startIndex = offset;
                    elem.len = value.Length;
                    tok = elem.token;
                    resultElement = elem;
                    int offse = reader.offset();
                    reader.setoffset(index);
                    //reader.unread(' ');
                    curblock.elem = elem;
                    isNextLine = false;
                    //Block = new Block { elem = elem, state = BlockState.start_end };

                    return;
                }

                int c = reader.read();
                if (c == -1) {
                    //tok = TokenType.EOS;
                    //Block = new Block { elem = null, state = BlockState.no };
                    break;
                }
                buf.Append((char)c);
            }
        }
    }
}
