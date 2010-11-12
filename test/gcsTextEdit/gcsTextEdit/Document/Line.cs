using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using YYS.Parser;

namespace YYS
{

    public class Line
    {
        private static string id = Document.DEFAULT_ID;
        public static string ID {
            get { return Line.id; }
            set { 
                Line.id = value; 
            }
        }
        private IText text;
        private List<Token> tokens;

        private Dictionary<string, Block> blockmap = new Dictionary<string, Block>();
        public Block Block {
            get {
                if (!blockmap.ContainsKey(Line.id)) {
                    blockmap.Add(Line.id, new Block());
                }
                return blockmap[Line.id]; 
            }
            set {
                if (blockmap.ContainsKey(Line.id)) {
                    blockmap[Line.id] = value;
                }
                else {
                    blockmap.Add(Line.id, value);
                }
            }
        }

        public List<Token> Tokens {
            get { return tokens; }
            set { tokens = value; }
        }

        public IText Text
        {
            get { return text; }
        }

        public int Length
        {
            get { return text.Length; }
        }

        public Line(string text)
        {
            //Block = new Block();
            this.text = new TextBuffer(text);
            tokens = new List<Token>();

        }

        public void SetText(string text)
        {
            this.text = new TextBuffer(text);
            tokens.Clear();
        }
    }
}
