using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls.Parser {
    class Parser {

        private Lexer lex;
        private TokenType token;//=TokenType.NONE;

        private void getToken() {
            if (lex.advance()) {
                token = lex.token();
            }
            else {
                token = TokenType.EOS;
            }
        }

        public Parser() {

        }

        public void init() {
            //lex.AddAttribute(new SingleLineAttribute("//", Color.Red, false, false));
        }

        public List<Attribute> parseLine(string line){
            lex = new Lexer(line);
            init();

            List<Attribute> attrs = new List<Attribute>();
            //lex = new Lexer(line);
		    //getToken();
		    while (token != TokenType.EOS) {
				getToken();
                //if (token == TokenType.ATTR) {
                //    attrs.Add(lex.getAttribute());
                //}
		    }
            return attrs;
	    }
    }
}
