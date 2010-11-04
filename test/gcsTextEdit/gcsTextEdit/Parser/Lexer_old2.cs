using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS.Parser {

    public class Lexer {

        public Tuple<int, int, YYS.Parser.Attribute> OffsetLenAttr;

        public LexerReader reader;

        public bool isNextLine = false;
        public bool scisNextLine = false;

        private Dictionary<String, Rule> ruleDic = new Dictionary<String, Rule>();

        private Dictionary<String, MultiLineRule> multiRuleDic = new Dictionary<String, MultiLineRule>();
        private Dictionary<String, MultiLineRule> multiRuleEndDic = new Dictionary<String, MultiLineRule>();

        private List<KeywordRule> keyWordRules = new List<KeywordRule>();

        private Dictionary<String, PartRule> partRuleDic = new Dictionary<String, PartRule>();
        private Dictionary<String, PartRule> partRuleEndDic = new Dictionary<String, PartRule>();
        private string paruleStartKeys = string.Empty;
        private string paruleEndKeys = string.Empty;

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
            else 
            {
                if (rule is MultiLineRule) {
                    multiRuleDic.Add(((MultiLineRule)rule).start, (MultiLineRule)rule);
                    multiRuleEndDic.Add(((MultiLineRule)rule).end, (MultiLineRule)rule);
                }

                ruleDic.Add(rule.start, rule);
            }
        }

        public void AddPartRule(PartRule rule) {
            partRuleDic.Add(rule.start, rule);
            partRuleEndDic.Add(rule.end, rule);

            foreach (var item in partRuleDic.Values) {
                if (!paruleStartKeys.Contains(item.start[0])) {
                    paruleStartKeys += item.start[0];
                }
                if (!paruleEndKeys.Contains(item.end[0])) {
                    paruleEndKeys += item.end[0];
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
                    value = null;
                    isNextLine = false;
                }
            }
        }

        private TokenType tok;
        public TokenType token {
            get { return tok; }
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
            tok = TokenType.TXT;

            if (Src.Length == 0 && curblock.isLineHeadCmt == 1) {
                curblock.mRule = preblock.mRule;
                isNextLine = true;
            }

            if (Src.Length == 0 && curblock.isLineHeadPart == 1) {
                curblock.PartID = preblock.PartID;
                scisNextLine = true;
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
                    //TODO test
                    if (curblock.isLineHeadCmt == 0) {
                        if (curblock.isLineHeadPart == 0) {
                            if (paruleStartKeys.Contains((char)c)) {
                                char fc = (char)c;
                                foreach (var item in partRuleDic) {
                                    if (item.Key[0] == fc) {
                                        var len = item.Value.start.Length;
                                        if (Offset - 1 + len <= Src.Length) {
                                            var text = Src.Substring(Offset - 1, len);
                                            if (text.ToString() == item.Key) {
                                                tok = TokenType.PartitionStart;
                                                if (curblock != null) {
                                                    curblock.PartID = item.Value.id;
                                                    scisNextLine = true;
                                                }
                                                reader.setoffset(Offset + text.Length);
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }else {
                            if (Offset - 1 == 0) {
                                while (c != -1) {
                                    if (paruleStartKeys.Contains((char)c)) {
                                        StringBuilder buf = new StringBuilder();
                                        while (c != -1) {
                                            buf.Append((char)c);

                                            if (partRuleEndDic.ContainsKey(buf.ToString())) {
                                                if (preblock.PartID == partRuleEndDic[buf.ToString()].id) {
                                                    var Eenelem = partRuleEndDic[buf.ToString()];
                                                    tok = TokenType.PartitionEnd;

                                                    curblock.PartID = Eenelem.id;
                                                    scisNextLine = false;

                                                    reader.setoffset(Offset + buf.ToString().Length);

                                                    return true;
                                                }
                                            }
                                            c = reader.read();
                                        }
                                    }
                                    c = reader.read();
                                }

                            //Finish:

                                if (c == -1) {
                                    tok = TokenType.Partition;
                                    curblock.PartID = preblock.PartID;
                                    scisNextLine = true;
                                }
                                reader.setoffset(0);
                                c = reader.read();
                            }
                            else if (paruleStartKeys.Contains((char)c)) {
                                char fc = (char)c;
                                foreach (var item in partRuleDic) {
                                    if (item.Key[0] == fc) {
                                        var len = item.Value.start.Length;
                                        if (Offset - 1 + len <= Src.Length) {
                                            var text = Src.Substring(Offset - 1, len);
                                            if (text.ToString() == item.Key) {
                                                tok = TokenType.PartitionStart;
                                                if (curblock != null) {
                                                    curblock.PartID = item.Key;
                                                    scisNextLine = true;
                                                }
                                                reader.setoffset(Offset + text.Length);
                                                //break;
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

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
                            reader.unread();
                            StringBuilder buf = new StringBuilder();
                            while (true) {
                                c = reader.read();
                                if (c == -1) {
                                    break;
                                }
                                buf.Append((char)c);

                                string s = buf.ToString();
                                string end = preblock.mRule.end;

                                if (s.EndsWith(end)) {
                                    if (multiRuleEndDic.ContainsKey(end)) {
                                        var rule = multiRuleEndDic[end];
                                        if (rule.Detected(end, reader)) {
                                            curblock.mRule = rule;
                                            //isNextLine = false;

                                            tok = rule.token;
                                            int len = rule.getLen(end, reader);
                                            reader.setoffset(len);
                                            OffsetLenAttr = new Tuple<int, int, Attribute>(0, len, rule.attr);
                                            //break;
                                            return true;
                                        }
                                    }
                                }
                            }

                            if (c == -1 && preblock.mRule != null && multiRuleDic.ContainsKey(preblock.mRule.start)) {
                                var enelem = multiRuleDic[preblock.mRule.start];
                                curblock.mRule = preblock.mRule;

                                tok = TokenType.MultiLineStart;
                                OffsetLenAttr = new Tuple<int, int, Attribute>(0, Src.Length, enelem.attr);

                                reader.setoffset(Src.Length);
                            }
                            break;
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
                    OffsetLenAttr = new Tuple<int, int, Attribute>(offset, len, rule.attr);
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
                        OffsetLenAttr = new Tuple<int, int, Attribute>(offset, len, rule.attr);
                        reader.setoffset(offset + len);

                        if (rule is MultiLineRule) {
                            if (curblock != null) {
                                curblock.mRule = rule as MultiLineRule;
                                //isNextLine = true;
                            }
                        }
                        break;
                    }                   
                }
                else if (multiRuleEndDic.ContainsKey(s)) {
                    var rule = multiRuleEndDic[s];
                    if (rule.Detected(s, reader)) {
                        tok = rule.token;
                        int len = rule.getLen(s, reader);
                        OffsetLenAttr = new Tuple<int, int, Attribute>(offset, len, rule.attr);
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
