using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS.Parser.Simple {


    public class SimpleParser : IParser {

        private Lexer lex;
        private TokenType tokentype;
        private List<Token> tokens;
        private Attribute defaultAttr;

        public SimpleParser() {
            lex = new Lexer();
        }

        #region IParser メンバ

        public bool Parse(List<Line> text, int ad, int s, int e) {
            //var id = getToken(text_[s], index);
            //Line.ID = id;
            //parser.SetPartition(id, false);
            int tln = text.Count;

            int i;
            Block block = text[s].Block;
            int cmt = text[s].Block.isLineHeadCmt;

            if (s > 0) {
                block = text[s - 1].Block;
            }

            // まずは変更範囲を再解析
            for (i = s; i <= e; ++i) {
                block = ParseLine(text[i], block, ref cmt, 0, text[i].Length);
                //cmt = parser.cmt;
            }

            if (i == tln || (text[i].Block.isLineHeadCmt == cmt && text[i].Block.mRule == block.mRule)) {
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

                block = ParseLine(line, block, ref cmt, 0, line.Length);
                //cmt = parser.cmt;

                if (pcmt == cmt) {
                    if (prule != block.mRule) {
                        pcmt--;
                    }
                }

            } while (i < tln && pcmt != cmt);

            return true;
        }

        #endregion

        public Block ParseLine(Line line, Block b, ref int _cmt, int start, int end) {

            tokentype = TokenType.TXT;
            tokens = new List<Token>();

            List<Tuple<int, int, bool>> cmstrulrs = new List<Tuple<int, int, bool>>();

            line.Block.isLineHeadCmt = _cmt;

            lex.Src = line.Text;

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

                            tokens.Add(new Token {ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3.attr });
                        }
                        break;

                    case TokenType.MultiLineStart: {
                            int off = lex.Offset;
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            int len = end - lex.OffsetLenAttr.t1;
                            lex.isNextLine = true;

                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                            tokens.Add(new Token {type = TokenType.MultiLine, mtype = MultiLineType.Start, ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3.attr });
                        }
                        break;

                    case TokenType.MultiLineAllLine: {
                            int off = lex.Offset;
                            //int len = line.Length - lex.OffsetLenAttr.t1;
                            int len = end - lex.OffsetLenAttr.t1;
                            lex.isNextLine = true;

                            cmstrulrs.Add(new Tuple<int, int, bool> { t1 = off, t2 = len, t3 = lex.isNextLine });
                            if (line.Length == 0) {
                                tokens.Add(new Token {type = TokenType.MultiLine, mtype = MultiLineType.All, ad = 0, len = 0, attr = lex.OffsetLenAttr.t3.attr });
                            }
                            else {
                                tokens.Add(new Token {type = TokenType.MultiLine, mtype = MultiLineType.All, ad = lex.OffsetLenAttr.t1, len = len, attr = lex.OffsetLenAttr.t3.attr });
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
                            if (tokens.Count > 0 && (tokens[tokens.Count - 1].mtype == MultiLineType.Start || tokens[tokens.Count - 1].mtype == MultiLineType.All)) {
                                int off = tokens[tokens.Count - 1].ad;
                                //tokens[tokens.Count - 1].len = off + lex.OffsetLenAttr.t2;
                                tokens[tokens.Count - 1].mtype = MultiLineType.Line;
                                tokens[tokens.Count - 1].len = lex.OffsetLenAttr.t2 - off;
                            }
                            else if (line.Block.isLineHeadCmt != 0) {
                                tokens.Add(new Token {type = TokenType.MultiLine, mtype = MultiLineType.End, ad = lex.OffsetLenAttr.t1, len = lex.OffsetLenAttr.t2, attr = lex.OffsetLenAttr.t3.attr });
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
                if (lastrule.ad + lastrule.len < end) {
                    tokens.Add(new Token { ad = lastrule.ad + lastrule.len, len = end - (lastrule.ad + lastrule.len), attr = defaultAttr });
                }

                List<Token> defaultRules = new List<Token>();
                int index = 0;
                for (int i = 0; i < tokens.Count; i++) {
                    if (tokens[i].ad - index > 0) {
                        defaultRules.Add(new Token {ad = index, len = tokens[i].ad - index, attr = defaultAttr });
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
                tokens.Add(new Token { ad = start, len = end, attr = defaultAttr });
            }

            line.Tokens = tokens;

            return line.Block;
        }

        #region IParser メンバ

        void IParser.SetHighlight(IHighlight highlight) {
            defaultAttr = highlight.getDefault();
            this.lex.AddRule(highlight.getRules());
            if (ReParseAll != null) {
                ReParseAll();
            }
        }

        void IParser.AddHighlight(string partionID, IHighlight highlight) {
            throw new NotImplementedException();
        }

        #endregion

        #region IParser メンバ

        public event ReParseAllEventHandler ReParseAll;

        #endregion
    }
}
