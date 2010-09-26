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

    public enum isLineHeadCommented {
        firstout = 0,
        firstin
    }

    public enum commentTransition {
        lastout = 0,
        firstlastrev,
        firstlastsame,
        lastin
    }

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
        private TokenType token;

        List<Token> rules;

        //private Dictionary<string, Lexer> Lexers;
        //private Dictionary<string, string> EndLexers;

        Attribute defaultAttr;

        private void getToken() {
            if (lex.advance()) {
                token = lex.token;
            }
            else {
                token = TokenType.EOS;
            }
        }

        //private void getToken(Block block) {
        //    if (lex.advance(block)) {
        //        token = lex.token;
        //    } else {
        //        token = TokenType.EOS;
        //    }
        //}

        //private void getToken(int cmst) {
        //    if (lex.advance(cmst)) {
        //        token = lex.token;
        //    }
        //    else {
        //        token = TokenType.EOS;
        //    }
        //}

        public Parser() {
            lex = new Lexer();
            init();
        }

        public void init() {
            
            lex.AddElement(new KeywordRule("test", new Attribute(Color.DarkGray, false, false, false, false)));

            lex.AddElement(new MultiLineRule("/*", "*/", new Attribute(Color.Red, true, false, false, false)));

            lex.AddElement(new MultiLineRule("'", "'", new Attribute(Color.Brown, false, false, false, false)));

            lex.AddElement(new EndLineRule("//", new Attribute(Color.LightBlue, false, false, false, false)));

            //lex.AddElement(new ImageElement("[[", "]]", new Attribute(Color.Red, false, true, false, false)));

            defaultAttr = new Attribute(Color.Black);
            //lex.AddDefaultElement(new TextElement(defaultAttr));
        }

        //public 

        //public abstract List<Rule> createRules();

        //public Block Parse(Line line, Block b) {
        //    line.Block.state = b.state;

        //    token = TokenType.TXT;

        //    lex.Src = line.Text.ToString();

        //    rules = new List<Token>();
        //    //lex = new Lexer(line);
        //    //getToken();
        //    while (token != TokenType.EOS) {
        //        //offset = lex.Offset;
        //        getToken(b);
        //        switch (token) {
        //            case TokenType.Enclose:
        //            case TokenType.EndLine:
        //            case TokenType.Line:
        //            case TokenType.Image:
        //            case TokenType.Keyword:

        //                var elem = lex.getElement();
        //                rules.Add(new Token { ad = elem.startIndex, len = elem.len, attr = elem.attr });
        //                //if (lex.Block.state == BlockState.end) {
        //                //    //lex.Block.state = BlockState.no;
        //                //}
        //                //b.state = lex.Block.state;
        //                //b.elem = lex.Block.elem;
        //                break;
        //            //case TokenType.Enclose:
        //            //    rules.Add(new Rule { ad = lex.Offset - lex.Value.Length, len = lex.Value.Length, attr = lex.getElement().attr });
        //            //    break;

        //            //case TokenType.EndLine:
        //            //    rules.Add(new Rule { ad = lex.Offset - lex.Value.Length, len = lex.Value.Length, attr = lex.getElement().attr });
        //            //    break;

        //            //case TokenType.Line:
        //            //    break;

        //            //case TokenType.Image:
        //            //    break;

        //            //case TokenType.Keyword:
        //            //    rules.Add(new Rule { ad = lex.Offset - lex.Value.Length, len = lex.Value.Length, attr = lex.getElement().attr });
        //            //    break;
        //        }
        //    }

        //    if (rules.Count > 0) {

        //        var lastrule = rules[rules.Count - 1];
        //        if (lastrule.ad + lastrule.len < line.Length) {
        //            rules.Add(new Token { ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
        //        }

        //        List<Token> defaultRules = new List<Token>();
        //        int index = 0;
        //        for (int i = 0; i < rules.Count; i++) {

        //            if (rules[i].ad - index > 0) {
        //                defaultRules.Add(new Token { ad = index, len = rules[i].ad - index, attr = defaultAttr });
        //            }
        //            index = rules[i].ad + rules[i].len;
        //        }

        //        if (defaultRules.Count > 0) {
        //            rules.AddRange(defaultRules);
        //            rules.Sort((x, y) => {
        //                return x.ad < y.ad ? -1 : 1;
        //            });
        //        }
        //    } else {
        //        rules.Add(new Token { ad = 0, len = line.Length, attr = defaultAttr });
        //    }

        //    line.Rules = rules; 

        //    Block next = new Block();

        //    if (lex.Block.elem != null) {
        //        line.Block.elem = lex.Block.elem;
        //        next.elem = lex.Block.elem;
        //    }
        //    //line.Block.state = lex.Block.state;

        //    if (lex.Block.state == BlockState.end || lex.Block.state == BlockState.start_end) {
        //        next.state = BlockState.no;
        //        //next.elem.attr = defaultAttr;
        //    }
        //    else if(lex.Block.state == BlockState.start){
        //        next.state = BlockState.all;
        //    }
        //    else {
        //        next.state = lex.Block.state;
        //    }
            
        //    return next;//lex.Block;
        //}

        public Block Parse(Line line, Block b, int _cmt) {
            //line.Block.state = b.state;


            rules = new List<Token>();
            token = TokenType.TXT;

            //List<Token> cmstrulrs = new List<Token>();
            List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();
            //int cmst = (b.commentTransition >> b.isLineHeadCmt) & 1;
            //line.Block.isLineHeadCmt = cmst;
            line.Block.isLineHeadCmt = _cmt;


            lex.Src = line.Text.ToString();

            while (token != TokenType.EOS) {

                //getToken(b);
                if (lex.advance(b, line.Block)) {
                    token = lex.token;
                }
                else {
                    token = TokenType.EOS;
                }
                switch (token) {
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
                        if (token == TokenType.MultiLine) {
                            var melem = lex.getElement();
                            var isnext = lex.isNextLine;
                            //cmstrulrs.Add(new Token { ad = melem.startIndex, len = melem.len, attr = null });
                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = melem.startIndex, t2 = melem.len, t3 = isnext });
                        }
                        var elem = lex.getElement();
                        rules.Add(new Token { ad = elem.startIndex, len = elem.len, attr = elem.attr });
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
            
            if (rules.Count > 0) {

                var lastrule = rules[rules.Count - 1];
                if (lastrule.ad + lastrule.len < line.Length) {
                    rules.Add(new Token { ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
                }

                List<Token> defaultRules = new List<Token>();
                int index = 0;
                for (int i = 0; i < rules.Count; i++) {
                    //if(rules[t].attr)
                    if (rules[i].ad - index > 0) {
                        defaultRules.Add(new Token { ad = index, len = rules[i].ad - index, attr = defaultAttr });
                    }
                    index = rules[i].ad + rules[i].len;
                }

                if (defaultRules.Count > 0) {
                    rules.AddRange(defaultRules);
                    rules.Sort((x, y) => {
                        return x.ad < y.ad ? -1 : 1;
                    });
                }
            }
            else {
                rules.Add(new Token { ad = 0, len = line.Length, attr = defaultAttr });
            }

            line.Rules = rules;
            
            return line.Block;
        }

        public int cmt;

        public List<Token> Parse(string line) {

            token = TokenType.TXT;
            //int lexoffset = 0;

            //lex.reader.Src = line;
            lex.Src = line;
            //lex = new Lexer(line);
            //init();

            //List<Rule> rules = new List<Rule>();
            rules = new List<Token>();
            //lex = new Lexer(line);
		    //getToken();
		    while (token != TokenType.EOS) {
                //offset = lex.Offset;
				getToken();
                switch (token) {
                    case TokenType.Enclose:
                    case TokenType.EndLine:
                    case TokenType.Line:
                    case TokenType.Image:
                    case TokenType.Keyword:
                        var elem = lex.getElement();
                        rules.Add(new Token { ad = elem.startIndex, len = elem.len, attr = elem.attr });
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

            if (rules.Count > 0) {

                var lastrule = rules[rules.Count - 1];
                if (lastrule.ad + lastrule.len < line.Length) {
                    rules.Add(new Token { ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
                }

                List<Token> defaultRules =new List<Token>();
                int index = 0;
                for (int i = 0; i < rules.Count; i++) {

                    if (rules[i].ad - index > 0) {
                        defaultRules.Add(new Token { ad = index, len = rules[i].ad - index, attr = defaultAttr });
                    }
                    index = rules[i].ad + rules[i].len;
                }

                if (defaultRules.Count > 0) {
                    rules.AddRange(defaultRules);
                    rules.Sort((x, y) => {
                        return x.ad < y.ad ? -1 : 1;
                    });
                }
            }
            else {
                rules.Add(new Token { ad = 0, len = line.Length, attr = defaultAttr });
            }

            return rules;
	    }
    }
}
