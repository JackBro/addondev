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

        private IText src;
        public IText Src {
            get { return src; }
            set {
                this.src = value;
                ch = 0;
                cnt = 0;
                unget_p = false;
            }
        }

        public LexerReader(IText src) {
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
        public LexerReader reader;

        private Rule resultRule;

        public bool isNextLine = false;

        private Dictionary<String, Rule> ruleDic = new Dictionary<String, Rule>();
        private Dictionary<String, Rule> keyWordRuleDic = new Dictionary<String, Rule>();

        private string ruleFirstKeys = string.Empty;


        public void AddRule(List<Rule> rules) {
            foreach (var item in rules) {
                this.AddRule(item);
            }
        }
        public void AddRule(Rule rule) {
            if (rule is KeywordRule) {
                keyWordRuleDic.Add(rule.start, rule);
            }
            else{
                ruleDic.Add(rule.start, rule);

                foreach (var item in ruleDic.Values) {
                    if (!ruleFirstKeys.Contains(item.start[0])) {
                        ruleFirstKeys += item.start[0];
                    }
                }
            }
        }

        public Rule getRule() {
            return resultRule;
        }

        public IText Src {
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

        public Lexer(IText src) {
            reader = new LexerReader(src);
        }

        public Lexer() {
            reader = new LexerReader();
        }

        public int Offset {
            get {
                return reader.offset();
            }
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
                    if (curblock.isLineHeadCmt == 0) { //0: 行頭がブロックコメントの内部ではない
                        if (Char.IsDigit((char)c)) {
                            reader.unread();
                            lexDigit();
                        }
                        else if (Util.isIdentifierPart((char)c)) {
                            reader.unread();
                            lexKeyWord();
                        }
                        else if (ruleFirstKeys.Contains((char)c)) {
                            //var r = ruleFirstKeys
                            char fc = (char)c;
                            foreach (var item in ruleDic) {
                                if (item.Key[0] == fc) {
                                    var len = item.Value.start.Length;
                                    if (Offset -1 + len <= Src.Length) {
                                        var text = Src.Substring(Offset-1, len);
                                        if (text.ToString() == item.Key) {
                                            reader.unread();
                                            lexSymbol(curblock);
                                            break;
                                        }
                                    }
                                }
                            }
                            //reader.unread();
                            //lexSymbol(curblock);
                        }
                        else {
                            tok = TokenType.TXT;
                        }
                    }
                    else { //1: 行頭がブロックコメントの内部
                        if (Src.IndexOf(preblock.elem.end, Offset - 1) >= 0) {

                            //var enelem = multiLineRuleDic[preblock.elem.start];
                            var enelem = ruleDic[preblock.elem.start];

                            int index = Src.IndexOf(preblock.elem.end, Offset - 1);
                            reader.setoffset(index + preblock.elem.end.Length);
                            enelem.startIndex = 0;
                            enelem.len = index + preblock.elem.end.Length;
                            tok = enelem.token;
                            resultRule = enelem;

                            curblock.elem = preblock.elem;

                            isNextLine = false;
                        }
                        else if (Offset - 1 == 0) {
                            //var enelem = multiLineRuleDic[preblock.elem.start];
                            var enelem = ruleDic[preblock.elem.start];

                            reader.setoffset(Src.Length);
                            enelem.startIndex = 0;
                            enelem.len = Src.Length;
                            tok = enelem.token;
                            resultRule = enelem;

                            curblock.elem = preblock.elem;
                            isNextLine = true;

                        }
                        else {
                            if (Char.IsDigit((char)c)) {
                                reader.unread();
                                lexDigit();
                            }
                            else if (Util.isIdentifierPart((char)c)) {
                                reader.unread();
                                lexKeyWord();
                            }
                            else if (ruleFirstKeys.Contains((char)c)) {
                                //reader.unread();
                                //lexSymbol();
                                char fc = (char)c;
                                foreach (var item in ruleDic) {
                                    if (item.Key[0] == fc) {
                                        var len = item.Value.start.Length;
                                        if (Offset - 1 + len <= Src.Length) {
                                            var text = Src.Substring(Offset - 1, len);
                                            if (text.ToString() == item.Key) {
                                                reader.unread();
                                                lexSymbol(curblock);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            else {
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
            tok = TokenType.Number;		
        }	

        private void lexKeyWord() {
            //offset = reader.offset();
            int offset = reader.offset()-1;

            StringBuilder buf = new StringBuilder();
            while (true) {
                int c = reader.read();
                if (c < 0) {
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
            var value = s;

            if (keyWordRuleDic.ContainsKey(s)) {
                //reader.setoffset(offset);
                //int offset = reader.offset() - value.Length;
                tok = TokenType.Keyword;
                var elem = keyWordRuleDic[s];
                elem.startIndex = offset;
                elem.len = value.Length;
                resultRule = elem;
            }
        }

        private void lexSymbol() {
            lexSymbol(null);
        }

        private void lexSymbol(Block curblock) {
            StringBuilder buf = new StringBuilder();
            int offset = reader.offset() - 1;

            while (true) {

                if (ruleDic.ContainsKey(buf.ToString())) {

                    Rule rule = ruleDic[buf.ToString()];
                    if (rule.token == TokenType.MultiLine) {
                        int index = rule.exer(this);
                        if (index < 0) {
                            rule.startIndex = offset;
                            rule.len = Src.Length - offset;
                            tok = rule.token;
                            resultRule = rule;

                            reader.setoffset(reader.Src.Length);
                            //reader.unread();
                            if (curblock != null) {
                                curblock.elem = rule as MultiLineRule;
                                isNextLine = true;
                            }
                            return;
                        }
                        //int offset = reader.offset() - value.Length;
                        var value = reader.Src.Substring(offset, index - offset);
                        rule.startIndex = offset;
                        rule.len = value.Length;
                        tok = rule.token;
                        resultRule = rule;
                        int offse = reader.offset();
                        reader.setoffset(index);
                        //reader.unread(' ');
                        if (curblock != null) {
                            curblock.elem = rule as MultiLineRule;
                            isNextLine = false;
                        }
                        return;
                    } else {

                        int index = rule.exer(this);
                        if (index < 0) {
                            reader.setoffset(reader.offset());
                            reader.unread();
                            return;
                        }
                        //int offset = reader.offset() - value.Length;
                        var value = reader.Src.Substring(offset, index - offset);
                        rule.startIndex = offset;
                        rule.len = value.Length;
                        tok = rule.token;
                        resultRule = rule;
                        int offse = reader.offset();
                        reader.setoffset(index);
                        //reader.unread(' ');

                        return;
                    }
                }

                int c = reader.read();
                if (c == -1) {
                    break;
                }
                buf.Append((char)c);
            }
        }
    }
}
