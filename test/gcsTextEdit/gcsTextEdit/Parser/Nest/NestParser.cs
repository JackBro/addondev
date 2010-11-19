using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using YYS.Parser;

namespace YYS.Parser.Nest {

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

    class NestParser :IParser{

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

        //public int cmt;

        public NestParser() {
            lex = new Lexer();
        }

        public Block ParseLine(string id, Line line, Block b, ref int _cmt) {
            return this.ParseLine(id, line, b, ref _cmt, 0, line.Length, false);
        }

        public Block ParseLine(string id, Line line, Block b, ref int _cmt, int start, int end, bool ispart) {

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
            _cmt = (line.Block.commentTransition >> _cmt) & 1;


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

        private string getToken(Line line, int ad) {
            var tokens = line.Tokens;

            string id = null;

            if (tokens.Count > 0) {
                var last = tokens[tokens.Count - 1];
                if (ad == (last.ad + last.len)) {
                    if (last.type == TokenType.MultiLine) {
                        //if (last.mtype == MultiLineType.End) {
                        if (line.GetBlock(getPartition(last.id).Parent.ID).commentTransition == 0) {
                            var parent = getPartition(last.id);
                            if (parent == null) {
                                id = Document.DEFAULT_ID;
                            }
                            else {
                                id = parent.ID;
                            }
                        }
                        else {
                            id = last.id;
                        }
                    }
                    else {
                        id = last.id;
                    }
                }
            }

            if (id == null) {

                foreach (var token in tokens) {
                    if (token.ad <= ad && ad < (token.ad + token.len)) {
                        id = token.id;
                        break;
                    }
                }
            }

            if (id == null) {
                id = Document.DEFAULT_ID;
            }
            else if (id != Document.DEFAULT_ID) {
                var pa = getPartition(id).Parent;
                if (pa != null) {
                    id = pa.ID;
                }
                if (id == null) {
                    id = Document.DEFAULT_ID;
                }
            }

            return id;
        }

        //private List<List<Tuple<int, Token>>> lines(int s, int e) {
        //    var lists = new List<List<Tuple<int, Token>>>();
        //    Action<int, List<Token>> func = (linenum, tokens) => {
        //        var list = new List<Token>(); 
        //        foreach (var item in tokens) {
        //            if (item.type == TokenType.MultiLine) {
        //                switch (item.mtype) {
        //                    case MultiLineType.Line:
        //                        lists.Add(new List<Tuple<int, Token>>{ new Tuple<int, Token>(linenum, item) });
        //                        break;
        //                    case MultiLineType.Start:
        //                        lists.Add(new List<Tuple<int, Token>> { new Tuple<int, Token>(linenum, item) });
        //                        break;
        //                    case MultiLineType.All:
        //                        if (lists.Count > 0) {
        //                            lists[lists.Count - 1].Add(new Tuple<int, Token>(linenum, item));
        //                        } else {
        //                            lists.Add(new List<Tuple<int, Token>> { new Tuple<int, Token>(linenum, item) });
        //                        }
        //                        break;
        //                    case MultiLineType.End:
        //                        if (lists.Count > 0) {
        //                            lists[lists.Count - 1].Add(new Tuple<int, Token>(linenum, item));
        //                        } else {
        //                            lists.Add(new List<Tuple<int, Token>> { new Tuple<int, Token>(linenum, item) });
        //                        }
        //                        break;
        //                    default:
        //                        break;
        //                }
        //            }
        //        }
        //    };

        //    for (int i = s; i <= e; ++i) {
        //        var ts = text_[i].Tokens;
        //        func(i, ts);
        //    }

        //    return lists;
        //}

        private List<List<Tuple<int, Token>>> lines(List<Line> text, int s, int e) {
            var lists = new List<List<Tuple<int, Token>>>();
            Action<int, List<Token>> func = (linenum, tokens) => {
                var list = new List<Token>();
                foreach (var item in tokens) {
                    if (item.type == TokenType.MultiLine) {
                        switch (item.mtype) {
                            case MultiLineType.Line:
                            case MultiLineType.Start:
                                lists.Add(new List<Tuple<int, Token>> { new Tuple<int, Token>(linenum, item) });
                                break;
                            case MultiLineType.All:
                            case MultiLineType.End:
                                if (lists.Count > 0) {
                                    lists[lists.Count - 1].Add(new Tuple<int, Token>(linenum, item));
                                }
                                else {
                                    lists.Add(new List<Tuple<int, Token>> { new Tuple<int, Token>(linenum, item) });
                                }
                                break;
                            default:
                                //lists.Add(new List<Tuple<int, Token>> { new Tuple<int, Token>(linenum, item) });
                                break;
                        }
                    }
                }
            };

            for (int i = s; i < e; ++i) {
                var ts = text[i].Tokens;
                func(i, ts);
            }

            return lists;
        }

        private int las = 0;
        private int lines(List<Line> text, int ad, int s, int e) {
            //string id = null;
            //Token ttoken = null;
            //var ts = text_[s].Tokens;
            //foreach (var item in ts) {
            //    if (item.ad <= ad && ad < (item.ad + item.len)) {
            //        id = item.id;
            //       // break;
            //    }
            //    if (id != null && item.id != id) {
            //        ttoken = item;
            //    }
            //}

            string id = getToken(text[s], ad);
            var pa = getPartition(id);
            string paid = null;
            if (pa != null) {
                paid = pa.ID;
            }
            else {
                paid = Document.DEFAULT_ID;
            }

            int tln = text.Count;
            for (int i = s + 1; i < tln; i++) {
                var tss = text[i].Tokens;
                //if (tss[0].id == paid) {
                //    return i;
                //}
                foreach (var item in tss) {
                    if (item.id == paid) {
                        if (item == tss[0]) {
                            var ltoken = text[i - 1].Tokens[text[i - 1].Tokens.Count - 1];
                            las = ltoken.ad + ltoken.len;
                            return i - 1;
                        }
                        int index = tss.IndexOf(item);
                        var tt = tss[index - 1];
                        las = tt.ad + tt.len;
                        return i;
                    }
                }
            }

            throw new Exception();
        }

        //private bool ParsePart(string id, int s, int e) {
        private bool ParsePart(List<Line> text, List<Tuple<int, Token>> list) {
            string id = list[0].t2.id;
            //string id = parser.getPartition(list[0].t2.id).Parent.ID;
            int s = list[0].t1;
            int e = list[list.Count - 1].t1;
            //int tln = 0;

            Line.ID = id;
            SetPartition(id, false);

            int i;
            int cmt = text[s].Block.isLineHeadCmt;
            Block block = text[s].Block;
            //if (s > 0) {
            //    block = text_[s - 1].Block;
            //}

            // まずは変更範囲を再解析
            for (i = s; i <= e; ++i) {
                //block = parser.Parse(text_[i], block, cmt);
                var token = list[i - s].t2;
                block = ParseLine(id, text[i], block, ref cmt, token.ad, token.ad + token.len, true);
                //cmt = parser.cmt;
            }

            // コメントアウト状態に変化がなかったらここでお終い。
            //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.mRule == block.mRule)) {
            //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.mRule == block.mRule)) {
            //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt)) {
            if (i == e+1 || (text[i].Block.isLineHeadCmt == cmt)) {

                return false;
            }

            int pcmt = 0;
            Rule prule = null;

            // 例えば、/* が入力された場合などは、下の方の行まで
            // コメントアウト状態の変化を伝達する必要がある。
            do {
                Line line = text[i++];
                pcmt = line.Block.isLineHeadCmt;
                prule = line.Block.mRule;

                block = ParseLine(id, line, block, ref cmt);
                //cmt = parser.cmt;

                if (pcmt == cmt) {
                    if (prule != block.mRule) {
                        pcmt--;
                    }
                }

            //} while (i < tln() && pcmt != cmt);
            } while (i < e+1 && pcmt != cmt);

            return true;
        }

        #region IParser メンバ

        public bool Parse(List<Line> text, int ad, int s, int e) {
            var id = getToken(text[s], ad);

            Line.ID = id;
            SetPartition(id, false);

            int i;
            Block block = text[s].Block;
            int cmt = text[s].Block.isLineHeadCmt;

            if (s > 0) {
                block = text[s - 1].Block; //TODO
            }

            if (id != Document.DEFAULT_ID) {
                var tmpid = id;
                var idlist = new List<string>();
                while (true) {
                    idlist.Add(tmpid);
                    var parent = getPartition(tmpid).Parent;
                    if (parent == null) {
                        idlist.Add(Document.DEFAULT_ID);
                        break;
                    }
                    else if (parent.ID == Document.DEFAULT_ID) {
                        idlist.Add(Document.DEFAULT_ID);
                        break;
                    }

                    tmpid = parent.ID;
                }
                //var pid = parser.getPartition(id).Parent.ID;
                for (int j = s; j <= e; ++j) {
                    if (text[j].GetBlockLength() < idlist.Count) {
                        foreach (var item in idlist) {
                            var pb = text[j].GetBlock(item);
                            if (pb == null) {
                                Block nb = new Block();
                                nb.isLineHeadCmt = 1;
                                nb.commentTransition = 3;
                                if (j > 0) {
                                    nb.mRule = text[j - 1].GetBlock(item).mRule;
                                }
                                text[j].SetBlock(item, nb);
                            }
                        }
                    }
                }
            }

            // まずは変更範囲を再解析
            for (i = s; i <= e; ++i) {
                block = ParseLine(id, text[i], block, ref cmt);
                //cmt = parser.cmt;
            }

            //if (text_[i - 1].Block.mRule != null && id != text_[i - 1].Block.mRule.id) {
            //    return false;
            //}
            // コメントアウト状態に変化がなかったらここでお終い。
            //if (i == tln() || text_[i].Block.isLineHeadCmt == cmt)
            //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.mRule == block.mRule)) {
            //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.mRule == block.mRule)
            //    || (text_[i].Block.mRule != null && id != text_[i].Block.mRule.id)) {

            int tln = text.Count();
            if (id != Document.DEFAULT_ID) {
                tln = lines(text, ad, s, e);
                //ll++;
                //int ii = 0;
            }
            if (i == tln || (text[i].Block.isLineHeadCmt == cmt && text[i].Block.mRule == block.mRule)) {
                //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.mRule == block.mRule)) {

                //var lists = lines(s, tln()==ll?e:ll);
                var lists = lines(text, s, tln);
                foreach (var list in lists) {
                    ParsePart(text, list);
                }

                return false;
            }

            int pcmt = 0;
            Rule prule = null;

            // 例えば、/* が入力された場合などは、下の方の行まで
            // コメントアウト状態の変化を伝達する必要がある。
            do {
                Line line = text[i++];
                pcmt = line.Block.isLineHeadCmt;
                prule = line.Block.mRule;

                if (i == tln) {
                    block = ParseLine(id, line, block, ref cmt, 0, las, true);
                }
                else {
                    block = ParseLine(id, line, block, ref cmt);
                }
                //cmt = parser.cmt;

                if (pcmt == cmt) {
                    if (prule != block.mRule) {
                        pcmt--;
                    }
                }

                //} while (i < tln() && pcmt != cmt);
            } while (i < tln && pcmt != cmt);

            int ss = s;
            int se = i;


            return true;
        }

        #endregion
    }
}
