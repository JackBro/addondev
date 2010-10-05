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

    public class Token {
        public int ad;
        public int len;
        public Attribute attr;
    }

    public class Block {
        public MultiLineRule elem;
        public int isLineHeadCmt = 0;
        public int commentTransition = 0;

        public Block() {
        }
    }

    //public class LexerMap{
    //    public string start;
    //    public string end;
    //    public Lexer lexer;
    //}

    class Parser {

        private Lexer lex;
        private TokenType tokentype;
        private List<Token> tokens;
        private Attribute defaultAttr;

        //public Attribute DefaultAttr {
        //    get;
        //    set;
        //}
        private IHighlight highlight;
        public IHighlight Highlight {
            get { return highlight; }
            set{
                highlight = value;
                defaultAttr = highlight.getDefault();
                this.lex.AddRule(highlight.getRules());
            }
        }

        //private void getToken() {
        //    if (lex.advance()) {
        //        tokentype = lex.token;
        //    }
        //    else {
        //        tokentype = TokenType.EOS;
        //    }
        //}

        public int cmt;

        public Parser() {
            lex = new Lexer();
            //init();
            //lex.AddRule(new EncloseRule("\"", "\"", new Attribute(Color.Red, AttrType.Normal), '\\'));
        }

        //public void init() {
            
        //    //lex.AddElement(new KeywordRule("test", new Attribute(Color.DarkGray, false, false, false, false)));

        //    //lex.AddElement(new MultiLineRule("/*", "*/", new Attribute(Color.Red, true, false, false, false)));
        //    lex.AddElement(new EncloseRule("\"", "\"", new Attribute(Color.Red, AttrType.Normal), '\\'));

        //    //lex.AddElement(new MultiLineRule("/'", "'/", new Attribute(Color.Brown, false, false, false, false)));
        //    lex.AddElement(new MultiLineRule("/'", "'/", new Attribute(Color.Brown, AttrType.Normal)));

        //    lex.AddElement(new EndLineRule("//", new Attribute(Color.DarkGreen, AttrType.UnderLine| AttrType.Strike)));

        //    //lex.AddElement(new ImageElement("[[", "]]", new Attribute(Color.Red, false, true, false, false)));

        //    defaultAttr = new Attribute(Color.Black);
        //    //lex.AddDefaultElement(new TextElement(defaultAttr));
        //}
        public Block Parse(Line line, Block b, int _cmt) {

            tokentype = TokenType.TXT;
            tokens = new List<Token>();

            List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();
            line.Block.isLineHeadCmt = _cmt;

            //lex.Src = line.Text.ToString();
            lex.Src = line.Text;

            while (tokentype != TokenType.EOS) {

                //getToken(b);
                if (lex.advance(b, line.Block)) {
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
                    case TokenType.Image:
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
