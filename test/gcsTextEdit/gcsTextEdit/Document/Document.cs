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
        //public event TextUpdateEventHandler TextUpdateEvent = null

        //private AsTextEdit EditView;
        //public List<Line> LineList;

        //
        private List<Line> text_;

        Highlighter highlighter;

        public Highlighter Highlighter {
            get { return highlighter; }
            set {
                highlighter = value;
            }
        }

        //public int tlNum {
        //    get {
        //        return LineList.Count;
        //    }
        //}

        //
        public int tln () {
            return text_.Count; //LineList.Count;
        }
        //
        public int len(int i) {
            return text_[i].Length;
        }
        //
        //public string tl(int i) {
        //    return text_[i].Text.ToString();
        //}
        public IBuffer tl(int i) {
            return text_[i].Text;
        }

        public List<AttributeInfo> AttributeList(int i) {
            return text_[i].AttributeList;
        }

        public override string ToString() {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < text_.Count; i++) {
                str.AppendLine(text_[i].Text.ToString());
            }
            return str.ToString();
        }

        public void Clear() {
            //LineList.Clear();
            text_.Clear();
            text_.Add(new Line(""));
            //LineList.Add(new Line(string.Empty));
            undoManager.Clear();
        }
        
        public Document() {
            highlighter = new Highlighter();
            highlighter.Add(@"//.*", TokenType.TXT, Color.Green);
            highlighter.Add(@"\[\[.*\]\]", TokenType.CLICKABLE, Color.Red);
            //highlighter.Add(@">>\w*", TokenType.CLICKABLE, Color.Blue);
            //highlighter.Add(@"file:///\S*", TokenType.CLICKABLE, Color.Blue);

            //LineList = new List<Line>();
            //LineList.Add(new Line(string.Empty));
            text_ = new List<Line>();
            text_.Add( new Line("") ); // 最初は一行だけ
        }

        public DPos leftOf(DPos dp, bool wide) {
            if (dp.ad == 0) {
                // 行の先頭だが、ファイルの先頭ではない場合
                // 一つ前の行の行末へ
                if (dp.tl > 0)
                    return new DPos(dp.tl - 1, len(dp.tl - 1));
                return dp;
            } else if (!wide) {
                // 行の途中で、普通に１文字戻る場合
                string l = tl(dp.tl).ToString();
                if (dp.ad >= 2 && Util.isLowSurrogate(l[dp.ad - 1]) && Util.isHighSurrogate(l[dp.ad - 2]))
                    return new DPos(dp.tl, dp.ad - 2);
                return new DPos(dp.tl, dp.ad - 1);
            } else {
                // 行の途中で、１単語分戻る場合
                string f = tl(dp.tl).ToString();
                int s = dp.ad - 1;
                while ((f[s] >> 5) == 0 && 0 <= s)
                    --s;
                return new DPos(dp.tl, s);
            }
        }
        public DPos rightOf(DPos dp, bool wide) {
            if (dp.ad == len(dp.tl)) {
                // 行末だが、ファイルの終わりではない場合
                // 一つ後の行の先頭へ
                if (dp.tl < tln() - 1)
                    return new DPos(dp.tl + 1, 0);
                return dp;
            } else if (!wide) {
                // 行の途中で、普通に１文字進む場合
                string l = tl(dp.tl).ToString();
                // 番兵 0x007f が l の末尾にいるので長さチェックは不要
                if (Util.isHighSurrogate(l[dp.ad]) && Util.isLowSurrogate(l[dp.ad + 1]))
                    return new DPos(dp.tl, dp.ad + 2);
                return new DPos(dp.tl, dp.ad + 1);
            } else {
                // 行の途中で、普通に１単語進む場合
                string f = tl(dp.tl).ToString();
                int e = len(dp.tl);
                int s = dp.ad;
                int t = (f[s] >> 5);
                s += t;
                if (s >= e)
                    s = e;
                else if (t == 7 || t == 0)
                    while ((f[s] >> 5) == 0 && s < e)
                        ++s;
                return new DPos(dp.tl, s);
            }
        }

        public Boolean LastLine(int linenum) {
            return linenum == text_.Count - 1 ? true : false;
        }

        private void MultiParse(int startrl, int endrl) {
            for (int i = startrl; i <= endrl; i++) {
                highlighter.Parse(text_[i].Text, text_[i].AttributeList);
            }
        }

        


        private void Insert(ref VPos s, ref VPos e, string text) {
            //// 位置補正
            //DPos cs = s as DPos;
            //CorrectPos(ref cs);
            
            //e.ad = s.ad;
            //e.tl = s.tl;

            //int textlen = 0;

            //string[] test = text.Split(new string[] { "\r\n" }, StringSplitOptions.None);
            //text_[s.tl].Text.Insert(s.ad, test[0]);
            //StringInfo sinfo = new StringInfo(test[0]);
            //e.ad += sinfo.LengthInTextElements;

            //if (test.Length > 1) {
            //    for (int i = 1; i < test.Length; i++) {
            //        e.tl++;
            //        text_.Insert(e.tl, new Line(test[i]));
            //    }
            //    StringInfo sinfo2 = new StringInfo(test[test.Length - 1]);
            //    textlen = sinfo2.LengthInTextElements;

            //    if (text_[s.tl].Length - e.ad > 0) {
            //        int slen = text_[s.tl].Length;
            //        text_[e.tl].Text.Append(text_[s.tl].Text.Substring(e.ad, slen - e.ad).ToString());
            //        text_[s.tl].Text.Remove(e.ad);
            //    }
            //    e.ad = textlen;
            //}

            //MultiParse(s.tl, e.tl);

            // 位置補正
            DPos cs = s as DPos;
            CorrectPos(ref cs);

            e.ad = s.ad;
            e.tl = s.tl;

            if (s.Equals(e)) {
                int i = 0;
            }

            int lineLen = 0;

            // 一行目…
            string[] lines = SplitLine(text);
            text_[e.tl].Text.Insert(e.ad, lines[0]);
            lineLen = lines[0].Length;
            e.ad += lineLen;

            // 二行目～最終行
            if (lines.Count() > 1) {
                for (int i = 1; i < lines.Count(); i++) {
                    text_.Insert(++e.tl, new Line(lines[i]));
                    lineLen = lines[i].Length;
                }
                // 一行目の最後尾に残ってた文字列を最終行へ
                Line fl = text_[s.tl];
                Line ed = text_[e.tl];
                int ln = fl.Length - e.ad;
                if (ln>0) {
                    //ed.InsertToTail(fl.str() + e.ad, ln);
                    //fl.RemoveToTail(e.ad);
                    ed.Text.Append(fl.Text.Substring(e.ad, fl.Length - e.ad).ToString());
                    fl.Text.Remove(e.ad);
                }

                // 終了位置記録
                e.ad = lineLen;
            }

            MultiParse(s.tl, e.tl);
        }

        private void Delete(ref VPos s, ref VPos e, out string buff) {
            CorrectPos(ref s, ref e);
            buff = string.Empty;
            if (s.tl == e.tl) {
                Line linfo = text_[s.tl];
                buff = linfo.Text.Substring(s.ad, e.ad - s.ad).ToString();
                linfo.Text.Remove(s.ad, e.ad - s.ad);
            } else {
                //// 先頭行の後ろを削除
                //text_[s.tl].RemoveToTail(s.ad);
                //// 終了行の残り部分をくっつける
                //text_[s.tl].InsertToTail(tl(e.tl) + e.ad, len(e.tl) - e.ad);
                //// いらん行を削除
                //text_.RemoveAt(s.tl + 1, e.tl - s.tl);

                string remtext = text_[s.tl].Text.ToString();
                if (s.ad < text_[s.tl].Text.Length) {
                    buff = text_[s.tl].Text.Substring(s.ad).ToString();
                    remtext = text_[s.tl].Text.Substring(0, s.ad).ToString();

                }
                for (int i = s.tl + 1; i < e.tl - s.tl; i++) {
                    buff += "\r\n" + text_[i].Text.ToString();
                }
                string mm = (text_[e.tl].Text.IsEmpty ? string.Empty : text_[e.tl].Text.Substring(0, e.ad).ToString());
                buff += "\r\n" + mm;

                //LineList[s.tl].SetText(remtext + mm);
                int len = text_[e.tl].Text.Length;
                //string mm2=LineList[e.tl].Text.Substring(e.ad, len-e.ad).ToString();
                string mm2 = (text_[e.tl].Text.IsEmpty ? string.Empty : text_[e.tl].Text.Substring(0, len - e.ad).ToString());
                text_[s.tl].SetText(remtext + mm2);
                text_.RemoveRange(s.tl + 1, e.tl - s.tl);
            }

            highlighter.Parse(text_[s.tl].Text, text_[s.tl].AttributeList);
        }

        private void Replace(ref VPos s, ref VPos e, ref VPos e2, out string oldValue, string newValue) {

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
            Insert(ref s, ref e2, newValue);

            //MultiParse(s.tl, e2.tl);
        }

        private void CorrectPos(ref DPos pos) {
            // 正常範囲に収まるように修正
            pos.tl = Math.Min(pos.tl, tln() - 1);
            pos.ad = Math.Min(pos.ad, len(pos.tl));
        }

        private void CorrectPos(ref VPos s, ref VPos e) {
            // 必ずs<=eになるように修正
            if (s > e) {
                int tmp;
                tmp = s.ad; s.ad = e.ad; e.ad = tmp;
                tmp = s.tl; s.tl = e.tl; e.tl = tmp;
            }
        }

        private string[] separater= new string[] { "\r\n", "\r" };
        private string[] SplitLine(string line) {
            //string[] res = line.Split(separater, StringSplitOptions.RemoveEmptyEntries);
            //if (res.Count() == 0) {
            //    res = new string[] { "" };
            //}
            //return res;
            return line.Split(separater, StringSplitOptions.None);
        }

        #region IDocument メンバ
        public event TextUpdateEventHandler TextUpdateEvent;
        private UndoManager undoManager = new UndoManager();
        public UndoManager UndoManager {
            get { return undoManager; }
        }

        public void Insert(VPos s, VPos e, string text) {
            Insert(ref s, ref e, text);

            if (UndoManager.AcceptChanges) {
                UndoManager.Push(new UndoInsert(this, s, e, text));
            }
            if (TextUpdateEvent != null) {
                TextUpdateEvent(new VPos(s), new VPos(s), e);
            }
        }

        public void Delete(VPos s, VPos e, out string deltext) {
            string buff;
            Delete(ref s, ref e, out buff);
            deltext = buff;

            if (UndoManager.AcceptChanges) {
                UndoManager.Push(new UndoDelete(this, s, s, buff));
            }
            if (TextUpdateEvent != null) {
                TextUpdateEvent(new VPos(s), e, new VPos(s));
            }
        }

        public void Replace(VPos s, VPos e, string newValue) {
            string oldvalue;
            VPos e2 = new VPos();
            Replace(ref s, ref e, ref e2, out oldvalue, newValue);

            if (UndoManager.AcceptChanges) {
                UndoManager.Push(new UndoReplace(this, s, e, e2, oldvalue, newValue));
            }
            if (TextUpdateEvent != null) {
                TextUpdateEvent(new VPos(s), e, new VPos(e2));
            }
        }

        #endregion
    }
}
