using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {

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
    //class TestHighlight : IHighlight {
    //    public TestHighlight() {
    //    }

    //    #region IHighlight メンバ

    //    public Attribute getDefault() {
    //        return new Attribute(Color.Red);
    //    }

    //    public List<Rule> getRules() {
    //        var rules = new List<Rule>();
    //        //rules.Add(new ScanRule("#START", "#END", "start",new AsControls.Parser.Attribute(Color.Red)));

    //        rules.Add(new EndLineRule("//", new AsControls.Parser.Attribute(Color.Pink, AttrType.UnderLine | AttrType.Strike)));
    //        return rules;
    //    }

    //    #endregion
    //}

    public class Token {
        public int ad;
        public int len;
        public Attribute attr;
    }

    public class Block {
        //TODO test
        public string id;
        public int scisLineHeadCmt = 0;
        public int sccommentTransition = 0;

        public MultiLineRule elem;
        public int isLineHeadCmt = 0;
        public int commentTransition = 0;

        public Block() {
            id = Document.DEFAULT_ID;
        }
    }

    class Parser {

        private Lexer lex;
        private TokenType tokentype;
        private List<Token> tokens;
        private Attribute defaultAttr;

        //private IHighlight tmp=null;
        //private IHighlight highlight;
        //public IHighlight Highlight {
        //    get { return highlight; }
        //    set{
        //        //TODO test
        //        if(tmp==null){
        //            tmp = value;
        //            //this.lex.AddScanRule();
        //            //if (value.getRules().Count>0)
        //            //lex.AddScanRule((ScanRule)value.getRules()[0]);
        //            //lex.AddScanRule(new ScanRule("#START", "#END", "start", new AsControls.Parser.Attribute(Color.Red)));
        //        }
        //        highlight = value;

        //        defaultAttr = highlight.getDefault();
        //        lex.ClearRule();
        //        this.lex.AddRule(highlight.getRules());     
        //    }
        //}

        //TODO test
        //private Dictionary<string, Tuple<IHighlight, ScanRule>> scruledic=new Dictionary<string,Tuple<IHighlight,ScanRule>>();
        private Dictionary<string, ScanRule> scruledic = new Dictionary<string, ScanRule>();
        private Dictionary<string, IHighlight> highlightDic = new Dictionary<string, IHighlight>();
        public void AddPartition(ScanRule rule) {
            scruledic.Add(rule.id, rule);
            lex.AddScanRule(rule);
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

        //public void AddHighlight(string name, IHighlight highlight, ScanRule rule) {
        //    scruledic.Add(name, new Tuple<IHighlight, ScanRule>(highlight, rule));
        //}

        //private void getToken() {
        //    if (lex.advance()) {
        //        tokentype = lex.token;
        //    }
        //    else {
        //        tokentype = TokenType.EOS;
        //    }
        //}
        private ScanRule scr;

        public int cmt;

        public int sccmt;

        public Parser() {
            lex = new Lexer();
            //lex.AddScanRule(new ScanRule("#START", "#END", "start", new AsControls.Parser.Attribute(Color.Red)));
        }

        public Block Parse(Line line, Block b, int _cmt, int _sccmt) {

            tokentype = TokenType.TXT;
            tokens = new List<Token>();

            List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();
            line.Block.isLineHeadCmt = _cmt;

            bool? isscnext = null;
            line.Block.scisLineHeadCmt = _sccmt;

            //if (line.Block.pa != "default") {
            //    if (!(this.Highlight is TestHighlight))
            //        this.Highlight = new TestHighlight();               
            //}
            //else {
            //    if(this.Highlight != tmp)
            //        this.Highlight = tmp;
            //}
            if (b.id != Document.DEFAULT_ID) {
                //if (!(this.Highlight is TestHighlight))
                //    this.Highlight = new TestHighlight();
                setd(b.id);
            } else {
                //if (this.Highlight != tmp)
                //    this.Highlight = tmp;
                setd(b.id);
            }

            //lex.Src = line.Text.ToString();
            lex.Src = line.Text;

            while (tokentype != TokenType.EOS) {

                //getToken(b);
                //if (lex.advance(b, line.Block)) {
                if (lex.advance2(b, line.Block)) {
                    tokentype = lex.token;
                }
                else {
                    tokentype = TokenType.EOS;
                }
                switch (tokentype) {
                    case TokenType.MultiLine:
                        //var melem = lex.getElement();
                        ////if (melem.startIndex == 0 && melem.len == line.Text.ToString().Length) {
                        ////    line.Block.commentTransition = 2;
                        ////}
                        //cmstrulrs.Add(new Token { ad = melem.startIndex, len = melem.len, attr = null });

                    case TokenType.Enclose:
                    case TokenType.EndLine:
                    case TokenType.Line:
                    case TokenType.Keyword:
                        if (tokentype == TokenType.MultiLine) {
                            var melem = lex.getRule();
                            var isnext = lex.isNextLine;
                            //cmstrulrs.Add(new Token { ad = melem.startIndex, len = melem.len, attr = null });
                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = melem.startIndex, t2 = melem.len, t3 = isnext });
                        }
                        var elem = lex.getRule();
                        tokens.Add(new Token { ad = elem.startIndex, len = elem.len, attr = elem.attr });
                        //if (lex.Block.state == BlockState.end) {
                        //    //lex.Block.state = BlockState.no;
                        //}
                        //b.state = lex.Block.state;
                        //b.elem = lex.Block.elem;
                        break;


                    case TokenType.PartitionStart: //TODO test
                        //var val = lex.Value;
                        //if (val == "#START") {
                        //    isscnext = true;
                        //    this.Highlight = new TestHighlight();
                        //    line.Block.pa = "START";
                        //    line.Block.schi = this.Highlight;
                        //} else if (val == "#END") {
                        //    isscnext = false;
                        //    this.Highlight = tmp;
                        //} 
                        isscnext = lex.scisNextLine;
                        if (line.Block.id != Document.DEFAULT_ID) {
                            //this.Highlight = new TestHighlight();
                            //if (scr == null) {
                            //    scr = new ScanRule("#START", "#END", "start", new AsControls.Parser.Attribute(Color.Red));
                            //    lex.AddScanRule(scr);
                            //}
                            setd(line.Block.id);
                        }
                        break;
                    case TokenType.Partition:
                        isscnext = lex.scisNextLine;
                        //this.Highlight = new TestHighlight();
                        break;
                    case TokenType.PartitionEnd:
                        isscnext = lex.scisNextLine;
                        line.Block.id = Document.DEFAULT_ID; //TODO
                        //setd(line.Block.pa);
                        setd(Document.DEFAULT_ID);
                        break;
                    //case TokenType.Enclose:
                    //    rules.Add(new Rule { ad = lex.Offset - lex.Value.Length, len = lex.Value.Length, attr = lex.getElement().attr });
                    //    break;

                    //case TokenType.EndLine:
                    //    rules.Add(new Rule { ad = lex.Offset - lex.Value.Length, len = lex.Value.Length, attr = lex.getElement().attr });
                    //    break;

                    //case TokenType.Line:
                    //    break;

                    //case TokenType.Image:
                    //    break;

                    //case TokenType.Keyword:
                    //    rules.Add(new Rule { ad = lex.Offset - lex.Value.Length, len = lex.Value.Length, attr = lex.getElement().attr });
                    //    break;
                }
                //if (sccmt == 1) {
                //    isscnext = true;
                //}
            }

            if (cmstrulrs.Count == 0) {
                line.Block.commentTransition = 2;
            } else {
                bool next = cmstrulrs[cmstrulrs.Count-1].t3;
                if (next) {
                    line.Block.commentTransition = 3;
                } else {
                    line.Block.commentTransition = 0;
                }
            }
            cmt = (line.Block.commentTransition >> _cmt) & 1;


            if (isscnext == null) {
                line.Block.sccommentTransition = 2;
            } else {
                if ((bool)isscnext) {
                    line.Block.sccommentTransition = 3;
                } else {
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
