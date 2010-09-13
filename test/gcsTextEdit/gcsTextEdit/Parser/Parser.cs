using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {

    public class Rule {
        public int ad;
        public int len;
        public Attribute attr;
    }


    enum BlockState {
        start,
        end,
        start_end,
        all,
        no
    }

    class Block {
        public BlockState state;
        public EncloseElement elem;
    }

    public class LexerMap{
        public string start;
        public string end;
        public Lexer lexer;
    }

    class Parser {

        private Lexer lex;
        private TokenType token;

        List<Rule> rules;

        private Dictionary<string, Lexer> Lexers;
        private Dictionary<string, string> EndLexers;

        Attribute defaultAttr;

        private void getToken() {
            if (lex.advance()) {
                token = lex.token;
            }
            else {
                token = TokenType.EOS;
            }
        }

        public Parser() {
            lex = new Lexer();
            init();
        }

        public void init() {
            
            lex.AddElement(new KeywordElement("test", new Attribute(Color.DarkGray, false, false, false, false)));

            lex.AddElement(new EncloseElement("/*", "*/", new Attribute(Color.Red, true, false, false, false)));

            //lex.AddElement(new EndLineElement("//", new Attribute(Color.DarkGreen, false, false, false, false)));

            lex.AddElement(new ImageElement("[[", "]]", new Attribute(Color.Red, false, true, false, false)));

            defaultAttr = new Attribute(Color.Black, false, false, false, false);
            lex.AddDefaultElement(new TextElement(defaultAttr));

            //lex.tokenEventHandler += (sender, start, len, e) => {
            //    rules.Add(new Rule { ad = start, len = len, attr = e.attr });
            //};
        }

        public void Parse(int s, int e, List<Line> lines) {

            int i;
            Block block = lines[s].Block;

            for (i = s; i < e; i++) {
                block = Parse(lines[i], block);
                //if(Lexers.ContainsKey(lines[i].Text.ToString())){

                //} else if (EndLexers.ContainsKey(lines[i].Text.ToString())) {

                //} else {
                //    lines[s].p = string.Empty;
                //}
            }

        }

        public Block Parse(Line line, Block b) {
            token = TokenType.TXT;

            lex.Src = line.ToString();

            //List<Rule> rules = new List<Rule>();
            rules = new List<Rule>();
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
                        rules.Add(new Rule { ad = elem.startIndex, len = elem.len, attr = elem.attr });
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
                    rules.Add(new Rule { ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
                }

                List<Rule> defaultRules = new List<Rule>();
                int index = 0;
                for (int i = 0; i < rules.Count; i++) {

                    if (rules[i].ad - index > 0) {
                        defaultRules.Add(new Rule { ad = index, len = rules[i].ad - index, attr = defaultAttr });
                    }
                    index = rules[i].ad + rules[i].len;
                }

                if (defaultRules.Count > 0) {
                    rules.AddRange(defaultRules);
                    rules.Sort((x, y) => {
                        return x.ad < y.ad ? -1 : 1;
                    });
                }
            } else {
                rules.Add(new Rule { ad = 0, len = line.Length, attr = defaultAttr });
            }

            line.Rules = rules; 
            return rules;
        }

        public List<Rule> Parse(string line) {

            token = TokenType.TXT;
            //int lexoffset = 0;

            //lex.reader.Src = line;
            lex.Src = line;
            //lex = new Lexer(line);
            //init();

            //List<Rule> rules = new List<Rule>();
            rules = new List<Rule>();
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
                        rules.Add(new Rule { ad = elem.startIndex, len = elem.len, attr = elem.attr });
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
                    rules.Add(new Rule { ad = lastrule.ad + lastrule.len, len = line.Length - (lastrule.ad + lastrule.len), attr = defaultAttr });
                }

                List<Rule> defaultRules =new List<Rule>();
                int index = 0;
                for (int i = 0; i < rules.Count; i++) {

                    if (rules[i].ad - index > 0) {
                        defaultRules.Add(new Rule { ad = index, len = rules[i].ad - index, attr = defaultAttr });
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
                rules.Add(new Rule { ad = 0, len = line.Length, attr = defaultAttr });
            }

            return rules;
	    }
    }
}
