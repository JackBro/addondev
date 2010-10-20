using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS.Parser {

    //public static class isLineHeadCommented {
    //    public static int firstout = 0;
    //    public static int firstin = 1;
    //}

    //public static class commentTransition {
    //    public static int lastout=0;
    //    public static int firstlastrev=1;
    //    public static int firstlastsame=2;
    //    public static int lastin = 3;
    //}

    //public enum isLineHeadCommented {
    //    firstout = 0,
    //    firstin
    //}

    //public enum commentTransition {
    //    lastout = 0,
    //    firstlastrev,
    //    firstlastsame,
    //    lastin
    //}

    class Parser {

        private Lexer lex;
        private TokenType tokentype;
        private List<Token> tokens;
        private Attribute defaultAttr;

        //TODO test
        private Dictionary<string, PartRule> scruledic = new Dictionary<string, PartRule>();
        private Dictionary<string, IHighlight> highlightDic = new Dictionary<string, IHighlight>();

        public void AddPartition(PartRule rule) {
            scruledic.Add(rule.id, rule);
            lex.AddPartRule(rule);
        }

        public void AddHighlight(string id, IHighlight highlight) {
            if (!highlightDic.ContainsKey(id)) {
                highlightDic.Add(id, highlight);
            }
        }
        private string curID = string.Empty;
        public void setd(string id) {
            if (curID != id) {
                IHighlight highlight = highlightDic[id];
                defaultAttr = highlight.getDefault();
                lex.ClearRule();
                this.lex.AddRule(highlight.getRules());
                curID = id;
            }
        }

        public int cmt;
        public int sccmt;

        public Parser() {
            lex = new Lexer();
        }

        public Block Parse(Line line, Block b, int _cmt, int _sccmt) {

            tokentype = TokenType.TXT;
            tokens = new List<Token>();

            List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();

            line.Block.isLineHeadCmt = _cmt;

            bool? isscnext = null;
            line.Block.scisLineHeadCmt = _sccmt;

            if (line.Block.scisLineHeadCmt == 0) {
                line.Block.id = Document.DEFAULT_ID;
                setd(b.id);
            }
            else {
                line.Block.id = b.id;
                setd(b.id);
            }

            lex.Src = line.Text;

            while (tokentype != TokenType.EOS) {

                if (lex.advance(b, line.Block)) {
                    tokentype = lex.token;
                }
                else {
                    tokentype = TokenType.EOS;
                }

                switch (tokentype) {
                    case TokenType.EndLine:
                    case TokenType.Line:
                    case TokenType.Enclose:
                    case TokenType.Keyword: {
                            tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3 });
                        }
                        break;

                    case TokenType.MultiLineStart: {
                            int off = lex.Offset;
                            int len = line.Length - lex.OffsetLenAttr.t1;
                            lex.isNextLine = true;
                        
                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                            tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3 });
                        }
                        break;
                    case TokenType.MultiLineEnd: {
                            int len = line.Length - lex.OffsetLenAttr.t1;
                            bool isnext = false;// lex.isNextLine;
                            lex.isNextLine = false;

                            if (cmstrulrs.Count > 0) {
                                cmstrulrs[cmstrulrs.Count - 1].t3 = isnext;
                            } else {
                                int off = lex.Offset;
                                cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = isnext });
                            }
                            if (tokens.Count > 0) {
                                int off = tokens[tokens.Count - 1].ad;
                                tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                            } else if (line.Block.isLineHeadCmt!=0) {
                                tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3 });
                            } 
                        }
                        break;

                    case TokenType.PartitionStart: //TODO test

                        isscnext = lex.scisNextLine;
                        if (line.Block.id != Document.DEFAULT_ID) {

                            setd(line.Block.id);
                        }
                        break;
                    case TokenType.Partition:
                        isscnext = lex.scisNextLine;
                        setd(line.Block.id);
                        break;
                    case TokenType.PartitionEnd:
                        isscnext = lex.scisNextLine;
                        setd(Document.DEFAULT_ID);
                        break;
                    default:
                        break;
                }

            }

            if (cmstrulrs.Count == 0) {
                line.Block.commentTransition = 2;
            }
            else {
                bool next = cmstrulrs[cmstrulrs.Count - 1].t3;
                if (next) {
                    line.Block.commentTransition = 3;
                }
                else {
                    line.Block.commentTransition = 0;
                }
            }
            cmt = (line.Block.commentTransition >> _cmt) & 1;


            if (isscnext == null) {
                line.Block.sccommentTransition = 2;
            }
            else {
                if ((bool)isscnext) {
                    line.Block.sccommentTransition = 3;
                }
                else {
                    line.Block.sccommentTransition = 0;
                }
            }
            sccmt = (line.Block.sccommentTransition >> _sccmt) & 1;

            if (tokens.Count > 0) {

                var lastrule = tokens[tokens.Count - 1];
                if (lastrule.ad + lastrule.len < line.Length) {
                    tokens.Add(new Token { ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
                }

                List<Token> defaultRules = new List<Token>();
                int index = 0;
                for (int i = 0; i < tokens.Count; i++) {
                    if (tokens[i].ad - index > 0) {
                        defaultRules.Add(new Token { ad = index, len = tokens[i].ad - index, attr = defaultAttr });
                    }
                    index = tokens[i].ad + tokens[i].len;
                }

                if (defaultRules.Count > 0) {
                    tokens.AddRange(defaultRules);
                    tokens.Sort((x, y) => {
                        return x.ad < y.ad ? -1 : 1;
                    });
                }
            }
            else {
                tokens.Add(new Token { ad = 0, len = line.Length, attr = defaultAttr });
            }

            line.Tokens = tokens;

            return line.Block;
        }
    }
}
