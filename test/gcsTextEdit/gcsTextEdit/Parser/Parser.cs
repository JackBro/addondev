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

        //private Dictionary<string, PartRule> scruledic = new Dictionary<string, PartRule>();
        //private Dictionary<string, IHighlight> highlightDic = new Dictionary<string, IHighlight>();
        private Dictionary<string, Partition> partitionDic = new Dictionary<string, Partition>();

        //public void AddPartition(PartRule rule) {
        //    scruledic.Add(rule.id, rule);
        //    lex.AddPartRule(rule);
        //}

        public void AddHighlight(string id, IHighlight highlight) {
            if (partitionDic.ContainsKey(id)) {
                var part = partitionDic[id];
                //part.Highlight = highlight;
                var rr = highlight.getRules();
                part.Highlight.getRules().AddRange(highlight.getRules());

                var rules = part.Highlight.getRules();
                int i = 0;

                foreach (var rule in rules) {
                    if (rule is PartRule) {
                        PartRule prule = rule as PartRule;
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
                    if (rule is PartRule) {
                        PartRule prule = rule as PartRule;
                        var partid = prule.id;
                        var newpart = new Partition(prule, new DefaultHighlight(prule.attr));
                        part.AddPartition(newpart);
                        partitionDic.Add(partid, newpart);
                    }
                }
            }
        }

        //public void AddHighlight(IHighlight highlight) {
        //}

        //private string curID = string.Empty;
        //public void setd(string id) {
        //    if (curID != id) {
        //        IHighlight highlight = highlightDic[id];
        //        defaultAttr = highlight.getDefault();
        //        lex.ClearRule();
        //        this.lex.AddRule(highlight.getRules());
        //        curID = id;
        //    }
        //}

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
                    this.lex.AddPartRule(item.rule);
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
                            this.lex.AddPartRule(item.rule);
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
        private Partition getPartition(string ID) {
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

        //public int cmt;
        public int sccmt;

        public Parser() {
            lex = new Lexer();
        }

        public Block Parse(Line line, Block b, int _sccmt) {

            tokentype = TokenType.TXT;
            tokens = new List<Token>();

            //List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();

            //line.Block.isLineHeadCmt = _cmt;

            bool? isscnext = null;
            line.Block.isLineHeadPart = _sccmt;

            if (line.Block.isLineHeadPart == 0) {
                //line.Block.PartID = Document.DEFAULT_ID;
                //setd(Document.DEFAULT_ID);
                if (this.partition.Parent != null) {
                    line.Block.PartID = this.partition.Parent.ID;
                    SetPartition(this.partition.Parent);
                }
            }
            else {
                //line.Block.PartID = b.PartID;
                //setd(b.PartID);

                line.Block.PartID = b.PartID;
                SetPartition(getPartition(line.Block.PartID));
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

                            {
                                //int off = lex.Offset;
                                //int len = line.Length - lex.OffsetLenAttr.t1;
                                //lex.isNextLine = true;

                                //cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                                //tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3 });
                                if (tokens.Count > 0) {
                                    int off = tokens[tokens.Count - 1].ad;
                                    int len = tokens[tokens.Count - 1].len;
                                    //tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                                    //tokens[tokens.Count - 1].len = lex.OffsetLenAttr.t2 - off;

                                    tokens.Add(new Token { ad = off + len, len = lex.Offset - off + len, attr = defaultAttr });
                                }
                                else {
                                    if (lex.Offset - lex.Value.Length - 1 > 0)
                                        tokens.Add(new Token { ad = 0, len = lex.Offset - lex.Value.Length - 1, attr = defaultAttr });
                                }
                            }


                            tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3 });
                        }
                        break;

                    //case TokenType.MultiLineStart: {
                    //        int off = lex.Offset;
                    //        int len = line.Length - lex.OffsetLenAttr.t1;
                    //        lex.isNextLine = true;

                    //        cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                    //        tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3 });
                    //    }
                    //    break;
                    //case TokenType.MultiLineEnd: {
                    //        int len = line.Length - lex.OffsetLenAttr.t1;
                    //        bool isnext = false;// lex.isNextLine;
                    //        lex.isNextLine = false;

                    //        if (cmstrulrs.Count > 0) {
                    //            cmstrulrs[cmstrulrs.Count - 1].t3 = isnext;
                    //        }
                    //        else {
                    //            int off = lex.Offset;
                    //            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = isnext });
                    //        }
                    //        if (tokens.Count > 0) {
                    //            int off = tokens[tokens.Count - 1].ad;
                    //            //tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                    //            tokens[tokens.Count - 1].len = lex.OffsetLenAttr.t2 - off;
                    //        }
                    //        else if (line.Block.isLineHeadCmt != 0) {
                    //            tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3 });
                    //        }
                    //    }
                    //    break;

                    case TokenType.PartitionStart:


                        {
                            //int off = lex.Offset;
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            //lex.isNextLine = true;

                            //cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                            //tokens.Add(new Token { ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3 });
                            if (tokens.Count > 0) {
                                int off = tokens[tokens.Count - 1].ad;
                                int len = tokens[tokens.Count - 1].len;
                                //tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                                //tokens[tokens.Count - 1].len = lex.OffsetLenAttr.t2 - off;

                                tokens.Add(new Token { ad = off + len, len = lex.Offset - off + len, attr = defaultAttr });
                            }
                            else{
                                if (lex.Offset - lex.Value.Length - 1 > 0)
                                tokens.Add(new Token { ad = 0, len = lex.Offset- lex.Value.Length-1, attr = defaultAttr });
                            }
                        }

                        isscnext = lex.scisNextLine;
                        //if (line.Block.PartID != Document.DEFAULT_ID) {
                        //    setd(line.Block.PartID);
                        //}

                        if (this.partition.Parent==null || (this.partition.Parent!=null && line.Block.PartID != this.partition.Parent.ID)) {
                            SetPartition(getPartition(line.Block.PartID));
                        }
                        break;
                    case TokenType.Partition:
                        isscnext = lex.scisNextLine;
                        //setd(line.Block.PartID);
                        break;
                    case TokenType.PartitionEnd:

                        {
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            //bool isnext = false;// lex.isNextLine;
                            //lex.isNextLine = false;

                            //if (cmstrulrs.Count > 0) {
                            //    cmstrulrs[cmstrulrs.Count - 1].t3 = isnext;
                            //}
                            //else {
                            //    int off = lex.Offset;
                            //    cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = isnext });
                            //}
                            if (tokens.Count > 0) {
                                int off = tokens[tokens.Count - 1].ad;
                                int len = tokens[tokens.Count - 1].len ;
                                //tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                                //tokens[tokens.Count - 1].len = lex.OffsetLenAttr.t2 - off;

                                tokens.Add(new Token { ad = off + len, len = lex.Offset - (off + len), attr = defaultAttr });
                            }
                            else if (line.Block.isLineHeadPart != 0) {
                                tokens.Add(new Token { ad = 0, len = lex.Offset , attr = defaultAttr });
                            }
                            else if (lex.scisNextLine) {
                                tokens.Add(new Token { ad = 0, len = lex.Offset , attr = defaultAttr });
                            }
                            lex.scisNextLine = false;
                        }

                        isscnext = lex.scisNextLine; 
                        //setd(Document.DEFAULT_ID);

                        SetPartition(partition.Parent);
                        break;
                    default:
                        break;
                }

            }

            //if (cmstrulrs.Count == 0) {
            //    line.Block.commentTransition = 2;
            //}
            //else {
            //    bool next = cmstrulrs[cmstrulrs.Count - 1].t3;
            //    if (next) {
            //        line.Block.commentTransition = 3;
            //    }
            //    else {
            //        line.Block.commentTransition = 0;
            //    }
            //}
            //cmt = (line.Block.commentTransition >> _cmt) & 1;


            if (isscnext == null) {
                line.Block.partTransition = 2;
            }
            else {
                if ((bool)isscnext) {
                    line.Block.partTransition = 3;
                }
                else {
                    line.Block.partTransition = 0;
                }
            }
            sccmt = (line.Block.partTransition >> _sccmt) & 1;

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
