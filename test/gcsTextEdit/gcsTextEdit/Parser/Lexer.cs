using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using YYS;
using YYS.Parser;

namespace YYS.Parser {

    public class Lexer {

        //public string Value { get; set; }

        public Tuple<int, int, Rule> OffsetLenAttr;

        private TokenType tok;
        public LexerReader reader;

        //private Rule resultRule;

        public bool isNextLine = false;

        private Dictionary<String, Rule> ruleDic = new Dictionary<String, Rule>();

        private Dictionary<String, MultiLineRule> multiRuleDic = new Dictionary<String, MultiLineRule>();
        private Dictionary<String, MultiLineRule> multiRuleEndDic = new Dictionary<String, MultiLineRule>();

        private List<KeywordRule> keyWordRules = new List<KeywordRule>();

        public void ClearRule() {
            ruleDic.Clear();
            multiRuleDic.Clear();
            multiRuleEndDic.Clear();
            keyWordRules.Clear();
        }

        public void AddRule(List<Rule> rules) {
            foreach (var item in rules) {
                this.AddRule(item);
            }
        }
        public void AddRule(Rule rule) {
            if (rule is KeywordRule) {
                keyWordRules.Add(rule as KeywordRule);
            }
            else {
                if (rule is MultiLineRule) {
                    multiRuleDic.Add(((MultiLineRule)rule).start, (MultiLineRule)rule);
                    multiRuleEndDic.Add(((MultiLineRule)rule).end, (MultiLineRule)rule);
                }

                {
                    ruleDic.Add(rule.start, rule);
                }
            }
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
                    isNextLine = false;
                    //Value = null;
                }
            }
        }

        public TokenType token {
            get { return tok; }
        }

        public Lexer(IText src) {
            reader = new LexerReader(src);
        }

        public Lexer(LexerReader reader) {
            this.reader = reader;
        }

        public Lexer() {
            reader = new LexerReader();
        }

        public int Offset {
            get {
                return reader.offset();
            }
        }

        private int start;
        private int end;
        public void SetRange(int start, int end) {
            this.start = start;
            this.end = end;

            reader.setoffset(start);
        }

        public bool advance(Block preblock, Block curblock) {
            tok = TokenType.TXT;

            if (Src.Length == 0 && curblock.isLineHeadCmt == 1) {
                curblock.mRule = preblock.mRule;
                tok = TokenType.MultiLineAllLine;
                isNextLine = true;
                return true;
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
                    break;

                default:
                    if (curblock.isLineHeadCmt == 0) { //0: 行頭がブロックコメントの内部ではない
                        if (Char.IsDigit((char)c)) {
                            reader.unread();
                            lexDigit();
                            break;
                        }
                        else if (Util.isIdentifierPart((char)c)) {
                            reader.unread();
                            lexKeyWord();
                            break;
                        }
                        else {
                            reader.unread();
                            lexSymbol(curblock);
                            break;
                        }
                    }
                    else { //1: 行頭がブロックコメントの内部

                        if (Offset - 1 == 0) {
                            int index;
                            if ((index = Src.IndexOf(preblock.mRule.end, 0)) >= 0) {
                                string end = preblock.mRule.end;
                                var rule = multiRuleEndDic[end];
                                if (rule.Detected(end, reader)) {
                                    curblock.mRule = rule;
                                    tok = TokenType.MultiLineEnd;
                                    int len = rule.getLen(end, reader);
                                    reader.setoffset(len);
                                    OffsetLenAttr = new Tuple<int, int, Rule>(0, len, rule);
                                    return true;
                                }

                            }
                            else if(multiRuleDic.ContainsKey(preblock.mRule.start)){
                                var rule = multiRuleDic[preblock.mRule.start];
                                curblock.mRule = preblock.mRule;
                                tok = TokenType.MultiLineAllLine;
                                OffsetLenAttr = new Tuple<int, int, Rule>(0, Src.Length, rule);
                                reader.setoffset(Src.Length);
                                return true;
                            }

                            //{
                            //    reader.unread();
                            //    StringBuilder buf = new StringBuilder();
                            //    while (true) {
                            //        c = reader.read();
                            //        if (c == -1) {
                            //            break;
                            //        }
                            //        buf.Append((char)c);

                            //        string s = buf.ToString();
                            //        string end = preblock.mRule.end;

                            //        if (s.EndsWith(end)) {
                            //            if (multiRuleEndDic.ContainsKey(end)) {
                            //                var rule = multiRuleEndDic[end];
                            //                if (rule.Detected(end, reader)) {
                            //                    curblock.mRule = rule;
                            //                    //isNextLine = false;
                            //                    //tok = rule.token;
                            //                    tok = TokenType.MultiLineEnd;
                            //                    int len = rule.getLen(end, reader);
                            //                    reader.setoffset(len);
                            //                    OffsetLenAttr = new Tuple<int, int, Rule>(0, len, rule);
                            //                    //break;
                            //                    return true;
                            //                }
                            //            }
                            //        }
                            //    }

                            //    if (c == -1 && preblock.mRule != null && multiRuleDic.ContainsKey(preblock.mRule.start)) {
                            //        var enelem = multiRuleDic[preblock.mRule.start];
                            //        curblock.mRule = preblock.mRule;
                            //        //isNextLine = true;
                            //        //tok = TokenType.MultiLineStart;
                            //        tok = TokenType.MultiLineAllLine;
                            //        OffsetLenAttr = new Tuple<int, int, Rule>(0, Src.Length, enelem);
                            //        reader.setoffset(Src.Length);
                            //    }
                            //    break;
                            //}
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
                            else {
                                reader.unread();
                                lexSymbol(curblock);
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

            while ((c != -1) && (Char.IsWhiteSpace(ch) || (ch == '\t') || (ch == '\n') || (ch == '\r'))) {
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
            int offset = reader.offset() - 1;

            StringBuilder buf = new StringBuilder();
            while (true) {
                int c = reader.read();
                if (c < 0) {
                    break;
                }
                if (!Util.isIdentifierPart((char)c)) {
                    reader.unread();
                    break;
                }
                buf.Append((char)c);
            }
            String s = buf.ToString();

            foreach (var rule in keyWordRules) {
                if (rule.Detected(s, reader)) {
                    tok = rule.token;
                    int len = rule.getLen(s, reader);
                    reader.setoffset(offset + len);
                    OffsetLenAttr = new Tuple<int, int, Rule>(offset, len, rule);
                    break;
                }
            }
        }

        private void lexSymbol(Block curblock) {
            StringBuilder buf = new StringBuilder();
            int offset = reader.offset() - 1;

            while (true) {
                int c = reader.read();
                if (c == -1) {
                    return;
                }
                buf.Append((char)c);

                string s = buf.ToString();
                if (ruleDic.ContainsKey(s)) {
                    var rule = ruleDic[s];
                    if (rule.Detected(s, reader)) {
                        tok = rule.token;
                        int len = rule.getLen(s, reader);
                        OffsetLenAttr = new Tuple<int, int, Rule>(offset, len, rule);
                        reader.setoffset(offset + len);

                        if (rule is MultiLineRule) {
                            if (curblock != null) {
                                curblock.mRule = rule as MultiLineRule;
                                tok = TokenType.MultiLineStart;
                                //isNextLine = true;
                            }
                        }
                        break;
                    }
                }
                else if (multiRuleEndDic.ContainsKey(s)) {
                    var rule = multiRuleEndDic[s];
                    if (rule.Detected(s, reader)) {
                        //tok = rule.token;
                        tok = TokenType.MultiLineEnd;
                        int len = rule.getLen(s, reader);
                        OffsetLenAttr = new Tuple<int, int, Rule>(offset, len, rule);
                        reader.setoffset(offset + len);

                        if (curblock != null) {
                            curblock.mRule = rule as MultiLineRule;
                        }

                        break;
                    }
                }
            }
        }
    }
}
