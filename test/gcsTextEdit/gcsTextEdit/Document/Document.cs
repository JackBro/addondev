using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Globalization;

namespace AsControls {

    public delegate Action<object> ChangeScrollEventHandler(object sender);

    public class Document : IDocument {
        //public event TextUpdateEventHandler TextUpdateEvent = null;

        //private AsTextEdit EditView;
        public List<Line> LineList;

        Highlighter highlighter;

        public Highlighter Highlighter {
            get { return highlighter; }
            set {
                highlighter = value;
            }
        }

        public int tlNum {
            get {
                return LineList.Count;
            }
        }

        public override string ToString() {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < LineList.Count; i++) {
                str.AppendLine(LineList[i].Text.ToString());
            }
            return str.ToString();
        }

        public void Clear() {
            LineList.Clear();
            LineList.Add(new Line(string.Empty));
            undoManager.Clear();
        }
        
        public Document() {
            highlighter = new Highlighter();
            //highlighter.Add(@"//.*", TokenType.TXT, Color.Green);
            highlighter.Add(@"\[\[.*\]\]", TokenType.CLICKABLE, Color.Red);
            //highlighter.Add(@">>\w*", TokenType.CLICKABLE, Color.Blue);
            //highlighter.Add(@"file:///\S*", TokenType.CLICKABLE, Color.Blue);

            LineList = new List<Line>();
            LineList.Add(new Line(string.Empty));
        }

        public Boolean LastLine(int linenum) {
            return linenum == LineList.Count - 1 ? true : false;
        }

        private void MultiParse(int startrl, int endrl) {
            for (int i = startrl; i <= endrl; i++) {
                highlighter.Parse(LineList[i].Text, LineList[i].AttributeList);
            }
        }

        private void Insert(CaretInfo s, ref CaretInfo e, string text) {
            e.ad = s.ad;
            e.tl = s.tl;

            int textlen = 0;

            string[] test = text.Split(new string[] { "\r\n" }, StringSplitOptions.None);
            LineList[s.tl].Text.Insert(s.ad, test[0]);
            StringInfo sinfo = new StringInfo(test[0]);
            e.ad += sinfo.LengthInTextElements;

            if (test.Length > 1) {
                for (int i = 1; i < test.Length; i++) {
                    e.tl++;
                    LineList.Insert(e.tl, new Line(test[i]));
                }
                StringInfo sinfo2 = new StringInfo(test[test.Length - 1]);
                textlen = sinfo2.LengthInTextElements;

                if (LineList[s.tl].Length - e.ad > 0) {
                    int slen = LineList[s.tl].Length;
                    LineList[e.tl].Text.Append(LineList[s.tl].Text.Substring(e.ad, slen - e.ad).ToString());
                    LineList[s.tl].Text.Remove(e.ad);
                }
                e.ad = textlen;
            }

            MultiParse(s.tl, e.tl);

            //upd();
        }

        private void Delete(ref CaretInfo s, ref CaretInfo e, out string buff) {
            CorrectPos(ref s, ref e);
            buff = string.Empty;
            if (s.tl == e.tl) {
                Line linfo = LineList[s.tl];
                buff = linfo.Text.Substring(s.ad, e.ad - s.ad).ToString();
                linfo.Text.Remove(s.ad, e.ad - s.ad);
            } else {
                //// 先頭行の後ろを削除
                //text_[s.tl].RemoveToTail(s.ad);
                //// 終了行の残り部分をくっつける
                //text_[s.tl].InsertToTail(tl(e.tl) + e.ad, len(e.tl) - e.ad);
                //// いらん行を削除
                //text_.RemoveAt(s.tl + 1, e.tl - s.tl);

                string remtext = LineList[s.tl].Text.ToString();
                if (s.ad < LineList[s.tl].Text.Length) {
                    buff = LineList[s.tl].Text.Substring(s.ad).ToString();
                    remtext = LineList[s.tl].Text.Substring(0, s.ad).ToString();

                }
                for (int i = s.tl + 1; i < e.tl - s.tl; i++) {
                    buff += "\r\n" + LineList[i].Text.ToString();
                }
                string mm = (LineList[e.tl].Text.IsEmpty ? string.Empty : LineList[e.tl].Text.Substring(0, e.ad).ToString());
                buff += "\r\n" + mm;

                //LineList[s.tl].SetText(remtext + mm);
                int len = LineList[e.tl].Text.Length;
                //string mm2=LineList[e.tl].Text.Substring(e.ad, len-e.ad).ToString();
                string mm2 = (LineList[e.tl].Text.IsEmpty ? string.Empty : LineList[e.tl].Text.Substring(0, len - e.ad).ToString());
                LineList[s.tl].SetText(remtext + mm2);
                LineList.RemoveRange(s.tl + 1, e.tl - s.tl);
            }

            highlighter.Parse(LineList[s.tl].Text, LineList[s.tl].AttributeList);
        }

        private void Replace(ref CaretInfo s, ref CaretInfo e, ref CaretInfo e2, out string oldValue, string newValue) {

            // string buff;
            // CaretInfo dp = new CaretInfo(sel);
            // doc.Delete(ref cur, ref dp, out buff);

            // if (undoManager.AcceptChanges)
            // {
            //     undoManager.Push(new UndoDelete(this, cur, cur, buff));
            // }

            // CaretInfo e2 = new CaretInfo();
            // doc.Insert(cur, ref e2, text);

            // if (undoManager.AcceptChanges)
            // {
            //     undoManager.Push(new UndoInsert(this, cur, e2, text));
            // }
            //// UpDate(ref cur, ref dp, ref e2);
            //// on_text_update();
            // doc.upd(new CaretInfo(cur), dp, e2);

            Delete(ref s, ref e, out oldValue);

            //CaretInfo e2 = new CaretInfo();
            Insert(s, ref e2, newValue);

            //MultiParse(s.tl, e2.tl);
        }

        private void CorrectPos(ref CaretInfo s, ref CaretInfo e) {
            // 必ずs<=eになるように修正
            if (s > e) {
                int tmp;
                tmp = s.ad; s.ad = e.ad; e.ad = tmp;
                tmp = s.tl; s.tl = e.tl; e.tl = tmp;
            }
        }

        //public 

        #region IDocument メンバ
        public event TextUpdateEventHandler TextUpdateEvent;
        private UndoManager undoManager = new UndoManager();
        public UndoManager UndoManager {
            get { return undoManager; }
        }

        public void Insert(CaretInfo s, CaretInfo e, string text) {
            Insert(s, ref e, text);

            if (UndoManager.AcceptChanges) {
                UndoManager.Push(new UndoInsert(this, s, e, text));
            }
            if (TextUpdateEvent != null) {
                TextUpdateEvent(new CaretInfo(s), new CaretInfo(s), e);
            }
        }

        public void Delete(CaretInfo s, CaretInfo e, out string deltext) {
            string buff;
            Delete(ref s, ref e, out buff);
            deltext = buff;

            if (UndoManager.AcceptChanges) {
                UndoManager.Push(new UndoDelete(this, s, s, buff));
            }
            if (TextUpdateEvent != null) {
                TextUpdateEvent(new CaretInfo(s), e, new CaretInfo(s));
            }
        }

        public void Replace(CaretInfo s, CaretInfo e, string newValue) {
            string oldvalue;
            CaretInfo e2 = new CaretInfo();
            Replace(ref s, ref e, ref e2, out oldvalue, newValue);

            if (UndoManager.AcceptChanges) {
                UndoManager.Push(new UndoReplace(this, s, e, e2, oldvalue, newValue));
            }
            if (TextUpdateEvent != null) {
                TextUpdateEvent(new CaretInfo(s), e, new CaretInfo(e2));
            }
        }

        #endregion
    }
}
