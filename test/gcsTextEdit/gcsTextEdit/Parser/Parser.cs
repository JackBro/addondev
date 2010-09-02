using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {

    enum AttributeType {

    }

    public class Rule {
        public int ad;
        public int len;
        public Attribute attr;
    }

    class Parser {

        private Lexer lex;
        private TokenType token;//=TokenType.NONE;

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
            token = TokenType.TXT;
            Attribute attr = new Attribute(Color.Red, false, false, false);
            lex.AddElement(new EncloseElement("[[", "]]", attr));
        }

        public List<Rule> parseLine(string line) {
            lex.reader.Src = line;
            //lex = new Lexer(line);
            //init();

            List<Rule> rules = new List<Rule>();
            //lex = new Lexer(line);
		    //getToken();
		    while (token != TokenType.EOS) {
                int offset = lex.Offset;
				getToken();
                switch(token){
                    case TokenType.Enclose:
                        rules.Add(new Rule { ad = offset, len=lex.Value.Length, attr=lex.getAttribute().attr });
                        break;

                    case TokenType.EndLine:
                        break;

                    case TokenType.Line:
                        break;

                    case TokenType.Image:
                        break;
                }
                //if (token == TokenType.ATTR) {
                //    attrs.Add(lex.getAttribute());
                //}
		    }
            return rules;
	    }
    }
}
