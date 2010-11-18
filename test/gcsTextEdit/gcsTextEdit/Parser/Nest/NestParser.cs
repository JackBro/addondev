using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using YYS.Parser;

namespace YYS.Parser.NestParser {

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

    class Parser :IParser{

        private Lexer lex;
        private TokenType tokentype;
        private List<Token> tokens;
        private Attribute defaultAttr;

        private Dictionary<string, Partition> partitionDic = new Dictionary<string, Partition>();

        public void AddHighlight(string id, IHighlight highlight) {
            if (partitionDic.ContainsKey(id)) {
                var part = partitionDic[id];
                //part.Highlight = highlight;
                part.Highlight.getRules().AddRange(highlight.getRules());

                var rules = part.Highlight.getRules();
   

                foreach (var rule in rules) {
                    if (rule is MultiLineRule) {
                        MultiLineRule prule = rule as MultiLineRule;
                        var partid = prule.id;
                        var newpart = new Partition(prule, new DefaultHighlight(prule.attr));
                        part.AddPartition(newpart);
                        partitionDic.Add(partid, newpart);
                    }
                }

            } else {
                var part = new Partition(id, highlight);
                partitionDic.Add(id, part);

                var rules = highlight.getRules();
                foreach (var rule in rules) {
                    if (rule is MultiLineRule) {
                        MultiLineRule prule = rule as MultiLineRule;
                        var partid = prule.id;
                        var newpart = new Partition(prule, new DefaultHighlight(prule.attr));
                        part.AddPartition(newpart);
                        partitionDic.Add(partid, newpart);
                    }
                }
            }
        }

        private Partition partition;
        public void SetPartition(Partition partition) {
            if (this.partition != partition) {
                this.partition = partition;

                var highlight = this.partition.Highlight;
                defaultAttr = highlight.getDefault();

                lex.ClearRule();

                var ch = this.partition.Children;
                //var parent = this.partition.Parent;
                //var ch = parent.Children;
                foreach (var item in ch) {
                    //this.lex.AddPartRule(item.GetPartRule());
                    //this.lex.AddPartRule(item.rule);
                }
                this.lex.AddRule(highlight.getRules());
            }
        }
        public void SetPartition(string ID, bool force) {

                if (partitionDic.ContainsKey(ID)) {
                    if (force) {
                        this.partition = partitionDic[ID];

                        var highlight = this.partition.Highlight;
                        defaultAttr = highlight.getDefault();

                        lex.ClearRule();

                        var ch = this.partition.Children;
                        foreach (var item in ch) {
                            //this.lex.AddPartRule(item.GetPartRule());
                            //this.lex.AddPartRule(item.rule);
                        }
                        this.lex.AddRule(highlight.getRules());
                    }
                    else {
                        SetPartition(partitionDic[ID]);
                    }
            }
        }

        public void SetPartition(string ID) {
            SetPartition(ID, false);
        }
        public Partition getPartition(string ID) {
            //var ch = this.partition.Children;
            //foreach (var item in ch) {
            //    if (item.ID == ID) {
            //        return item;
            //    }
            //}
            //return null;

            //return this.partition.GetChildren(ID);
            return partitionDic[ID];
        }

        public int cmt;

        public Parser() {
            lex = new Lexer();
        }

        public Block Parse(string id, Line line, Block b, int _cmt) {
            return this.Parse(id, line, b, _cmt, 0, line.Length, false);
        }

        public Block Parse(string id, Line line, Block b, int _cmt, int start, int end, bool ispart) {

            tokentype = TokenType.TXT;
            tokens = new List<Token>();

            List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();

            line.Block.isLineHeadCmt = _cmt;

            lex.Src = line.Text;
            if (ispart) {
                lex.SetRange(start, end);
            }

            //while (tokentype != TokenType.EOS) {
            while (lex.advance(b, line.Block)) {
                tokentype = lex.token;

                //if (lex.advance(b, line.Block)) {
                //    tokentype = lex.token;
                //}
                //else {
                //    tokentype = TokenType.EOS;
                //}

                switch (tokentype) {
                    case TokenType.EndLine:
                    case TokenType.Line:
                    case TokenType.Enclose:
                    case TokenType.Keyword: {

                            tokens.Add(new Token { id = id, ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3.attr });
                        }
                        break;

                    case TokenType.MultiLineStart: {
                            int off = lex.Offset;
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            int len = end - lex.OffsetLenAttr.t1;
                            lex.isNextLine = true;

                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                            var parid = ((MultiLineRule)(lex.OffsetLenAttr.t3)).id;
                            tokens.Add(new Token { id = parid, type = TokenType.MultiLine, mtype= MultiLineType.Start, ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3.attr });
                        }
                        break;

                    case TokenType.MultiLineAllLine: {
                            int off = lex.Offset;
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            int len = end - lex.OffsetLenAttr.t1;
                            lex.isNextLine = true;

                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                            var parid = ((MultiLineRule)(lex.OffsetLenAttr.t3)).id;
                            if (line.Length == 0) {
                                tokens.Add(new Token { id = parid, type = TokenType.MultiLine, mtype = MultiLineType.All, ad = 0, len = 0, attr = lex.OffsetLenAttr.t3.attr });
                            }
                            else {
                                tokens.Add(new Token { id = parid, type = TokenType.MultiLine, mtype = MultiLineType.All, ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3.attr });
                            }
                        }
                        break;

                    case TokenType.MultiLineEnd: {
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            int len = end - lex.OffsetLenAttr.t1;
                            bool isnext = false;// lex.isNextLine;
                            lex.isNextLine = false;

                            if (cmstrulrs.Count > 0) {
                                cmstrulrs[cmstrulrs.Count - 1].t3 = isnext;
                            }
                            else {
                                int off = lex.Offset;
                                cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = isnext });
                            }
                            if (tokens.Count > 0 && (tokens[tokens.Count - 1].mtype== MultiLineType.Start || tokens[tokens.Count - 1].mtype== MultiLineType.All)) {
                                int off = tokens[tokens.Count - 1].ad;
                                //tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                                tokens[tokens.Count - 1].mtype = MultiLineType.Line;
                                tokens[tokens.Count - 1].len = lex.OffsetLenAttr.t2 - off;
                            }
                            else if (line.Block.isLineHeadCmt != 0) {
                                var parid = ((MultiLineRule)(lex.OffsetLenAttr.t3)).id;
                                tokens.Add(new Token { id = parid, type = TokenType.MultiLine, mtype = MultiLineType.End, ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3.attr });
                            }
                        }
                        break;

                    
                    default:
                        break;
                }

                if (line.Length == 0) {
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

            //if (!ispart) {

                if (tokens.Count > 0) {

                    var lastrule = tokens[tokens.Count - 1];
                    //if (lastrule.ad + lastrule.len < line.Length) {
                    //    tokens.Add(new Token { id = id, ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
                    //}
                    if (lastrule.ad + lastrule.len < end) {
                        tokens.Add(new Token { id = id, ad = lastrule.ad + lastrule.len, len = end - (lastrule.ad + lastrule.len), attr = defaultAttr });
                    }

                    List<Token> defaultRules = new List<Token>();
                    int index = 0;
                    for (int i = 0; i < tokens.Count; i++) {
                        if (tokens[i].ad - index > 0) {
                            defaultRules.Add(new Token { id = id, ad = index, len = tokens[i].ad - index, attr = defaultAttr });
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
                    if (ispart) {
                        tokens.Add(new Token { id = id, ad = start, len = end - start, attr = defaultAttr });
                    }
                    else {
                        //tokens.Add(new Token { id = id, ad = 0, len = line.Length, attr = defaultAttr });
                        tokens.Add(new Token { id = id, ad = 0, len = end, attr = defaultAttr });
                    }
                }
            //}

            if (ispart) {
                foreach (var token in line.Tokens) {
                    if (token.ad == start && (token.ad + token.len) == end) {
                        int index = line.Tokens.IndexOf(token);
                        line.Tokens.Remove(token);
                        tokens[0].type = token.type;
                        line.Tokens.InsertRange(index, tokens);
                        break;
                    }
                }
                
            } else {
                line.Tokens = tokens;
            }

            return line.Block;
        }
    }
}
