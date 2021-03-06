﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS.Parser {

    public class Token {
        public string id;
        public TokenType type;
        public MultiLineType mtype;
        public int ad;
        public int len;
        public Attribute attr;
    }

    public class Block {
        //private Dictionary<string, Tuple<int, int>> st = new Dictionary<string, Tuple<int, int>>();
        //public void setDic(string id, int partTransition, int isLineHeadPart) {
        //    if (id == Document.DEFAULT_ID) return;
        //    if (st.ContainsKey(id)) {
        //        st[id] = new Tuple<int, int>(partTransition, isLineHeadPart);
        //    }
        //    else {
        //        st.Add(id, new Tuple<int, int>(partTransition, isLineHeadPart));
        //    }
        //}
        //public Tuple<int, int> getDic(string id) {
        //    if(st.ContainsKey(id))
        //        return st[id];

        //    return null;
        //}
        //public string PartID;
        //public int isLineHeadPart = 0;
        //public int partTransition = 0;

        public MultiLineRule mRule;
        public int isLineHeadCmt = 0;
        public int commentTransition = 0;

        public Block() {
            //PartID = Document.DEFAULT_ID;
        }
    }

    [Flags]
    public enum AttrType {
        Normal=1,
        Bold=2,
        UnderLine=4,
        Strike=8,
        Link=16,
        Image=32
    }

    public class Attribute {
        public readonly Color color;
        public AttrType type;

        public Attribute(Color color, AttrType type) {
            this.color = color;
            this.type = type;
        }
        public Attribute(Color color) {
            this.color = color;
            this.type = AttrType.Normal;
        }
    }

    public enum TokenType {
        EOS,
        TXT, // 普通の字
        MultiLine,
        MultiLineOneLine,
        MultiLineAllLine,
        MultiLineStart,
        MultiLineEnd,
        Enclose,
        EndLine,
        Line,
        Keyword,
        Number,
        PartitionStart,
        Partition,
        PartitionEnd
    }

    public enum MultiLineType {
        None,
        Line,
        Start,
        All,
        End
    }

    public abstract class Rule {
        public Attribute attr;
        public TokenType token;
        public string start { get; set; }
        public int startIndex;
        public int len;

        public Func<string, LexerReader, bool> detected;

        public virtual bool Detected(string sequence, LexerReader reader) {
            if (detected == null) {
                if (sequence == start) {
                    return true;
                }
                return false;
            }
            else {
                return detected(sequence, reader);
            }
        }

        public virtual int getLen(string sequence, LexerReader reader) {
            return this.start.Length;
        }
    }

    // /*..*/end
    public class MultiLineRule : Rule {
        public string id;
        public string end;
        public MultiLineRule() {
            token = TokenType.MultiLine;
        }
        public MultiLineRule(string id, string start, string end, Attribute attr) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.attr = attr;
            token = TokenType.MultiLine;
        }

        public override bool Detected(string sequence, LexerReader reader) {
            if (sequence == start) {
                token = TokenType.MultiLineStart;
                return true;
            }
            if (sequence == end) {
                token = TokenType.MultiLineEnd;
                return true;
            }

            return false;
        }

        public override int getLen(string sequence, LexerReader reader) {
            if (sequence == this.start) {
                return base.getLen(sequence, reader);
            } else {
                return reader.offset();
            }
        }
    }

    //public class PartRule : Rule {
    //    public string id;
    //    public string end;
    //    public PartRule() {
    //        token = TokenType.TXT;
    //    }
    //    public PartRule(string id, string start, string end, Attribute attr) {
    //        this.id = id;
    //        this.start = start;
    //        this.end = end;
    //        this.attr = attr;
    //        token = TokenType.TXT;
    //    }
    //}

    //[[..]]end
    public class EncloseRule : Rule {
        public string end;
        private char? escape;
        public EncloseRule() {
            token = TokenType.Enclose;
        }
        public EncloseRule(string start, string end, Attribute attr, char escape) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            this.escape = escape;
            token = TokenType.Enclose;
        }
        public EncloseRule(string start, string end, Attribute attr) {
            this.start = start;
            this.end = end;
            this.attr = attr;
            this.escape = null;
            token = TokenType.Enclose;
        }

        public override int getLen(string sequence, LexerReader reader) {
            int off = reader.offset();
            int index = reader.Src.IndexOf(this.end, off);
            if (index < 0) {
                return reader.Src.Length - (off-this.start.Length);
            } else {
                return index - (off - this.start.Length) + this.end.Length;
            }
        }
    }

    // //. ..\nend
    public class EndLineRule : Rule {

        public EndLineRule(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.EndLine;
        }

        public override int getLen(string sequence, LexerReader reader) {
            return reader.Src.Length - reader.offset() + this.start.Length;
        }
    }

    // >>.. end
    public class LineRule : Rule {
        private Char[] c = new char[3];

        public LineRule(string start, Attribute attr) {
            this.start = start;
            this.attr = attr;
            token = TokenType.Line;
            c[0] = '\t';
            c[1] = ' ';
            c[2] = '\x3000';
        }

        public override int getLen(string sequence, LexerReader reader) {
            int off = reader.offset();
            int index = reader.Src.ToString().IndexOfAny(c, off);
            if (index < 0) {
                return reader.Src.Length - off;
            } else {
                return index - off;
            }
        }
    }

    public class KeywordRule : Rule {
        private string[] words; 

        public KeywordRule(string[] words, Attribute attr) {
            this.words = words;
            this.attr = attr;
            token = TokenType.Keyword;
        }

        public override bool Detected(string sequence, LexerReader reader) {
            return words.Contains(sequence);
        }

        public override int getLen(string sequence, LexerReader reader) {
            return sequence.Length;
        }
    }
}
