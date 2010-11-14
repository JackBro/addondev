using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS.Parser {
    public class LexerReader {
        private bool unget_p = false;
        private int ch;
        private int cnt;

        private IText src;
        public IText Src {
            get { return src; }
            set {
                this.src = value;
                ch = 0;
                cnt = 0;
                unget_p = false;

                ispart = false;
            }
        }

        public LexerReader(IText src) {
            this.src = src;
            cnt = 0;
        }

        public LexerReader() {
            cnt = 0;
        }

        public int read() {
            if (unget_p) {
                unget_p = false;
            }
            else {
                if (ispart && cnt > end) {
                    ch = -1;
                }
                else if (cnt > src.Length - 1) {
                    ch = -1;
                }
                else {
                    ch = src[cnt];
                    cnt++;
                }
            }
            return ch;
        }

        public void unread() {
            unget_p = true;
        }

        public int offset() {
            return cnt;
        }

        public void setoffset(int offset) {
            cnt = offset;
        }

        private int start;
        private int end;
        private bool ispart;
        public void SetRange(int start, int end) {
            this.start = start;
            this.end = end;

            this.setoffset(start);
            ispart = true;

        }
    }
}
