using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Globalization;
using AsControls.Parser;

namespace AsControls {

    public delegate Action<object> ChangeScrollEventHandler(object sender);

    public class Document : IDocument {

        //
        private List<Line> text_;

        Parser.Parser parser;


        //
        public int tln () {
            return text_.Count; //LineList.Count;
        }
        //
        public int len(int i) {
            return text_[i].Length;
        }
        //
        public IBuffer tl(int i) {
            return text_[i].Text;
        }

        public List<Rule> Rules(int i) {
            return text_[i].Rules;
        }

        public override string ToString() {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < text_.Count; i++) {
                str.AppendLine(text_[i].Text.ToString());
            }
            return str.ToString();
        }

        public void Clear() {
            text_.Clear();
            text_.Add(new Line(""));
            undoManager.Clear();
        }
        
        public Document() {
            //highlighter = new Highlighter();
            //highlighter.Add(@"//.*", TokenType.TXT, Color.Green);
            //highlighter.Add(@"\[\[.*\]\]", TokenType.CLICKABLE, Color.Red);
            //highlighter.Add(@">>\w*", TokenType.CLICKABLE, Color.Blue);
            //highlighter.Add(@"file:///\S*", TokenType.CLICKABLE, Color.Blue);

            parser = new AsControls.Parser.Parser();

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
                //TODO 行の途中で、１単語分戻る場合
                //const uchar* f = pl(dp.tl);
                //ulong s = dp.ad - 1;
                //while ((f[s] >> 5) == 0 && 0 <= s)
                //    --s;
                //return DPos(dp.tl, s);

                string f = tl(dp.tl).ToString();
                int s = dp.ad - 1;
                while ( 0 < s && f[s] != ' ')
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
                //TODO 行の途中で、普通に１単語進む場合
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
            //for (int i = startrl; i <= endrl; i++) {
            //    highlighter.Parse(text_[i].Text, text_[i].AttributeList);
            //}
            for (int i = startrl; i <= endrl; i++) {
                var rules = parser.parseLine(text_[i].Text.ToString());
                text_[i].Rules = rules;
            }
        }

        private void Insert(ref VPos s, ref VPos e, string text) {
            // 位置補正
            DPos cs = s as DPos;
            CorrectPos(ref cs);

            e.ad = s.ad;
            e.tl = s.tl;

            //if (s.Equals(e)) {
            //    int i = 0;
            //}

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
            // 位置補正
            CorrectPos(ref s);
            CorrectPos(ref e);
            CorrectPos(ref s, ref e);

            // 削除される量をカウント
            int undosiz = getRangeLength(s, e);

            // Undo操作用バッファ確保
            //undobuf = new unicode[undosiz + 1];
            //getText(undobuf, s, e);
            buff = getText(s, e);

            // 削除る
            if (s.tl == e.tl) {
                // 一行内削除
                //text_[s.tl].RemoveAt(s.ad, e.ad - s.ad);
                text_[s.tl].Text.Remove(s.ad, e.ad - s.ad);
            } else {
                // 先頭行の後ろを削除
                //text_[s.tl].RemoveToTail(s.ad);
                text_[s.tl].Text.Remove(s.ad);
                // 終了行の残り部分をくっつける
                //text_[s.tl].InsertToTail(tl(e.tl) + e.ad, len(e.tl) - e.ad);
                string val = tl(e.tl).Substring(e.ad, len(e.tl) - e.ad).ToString();
                text_[s.tl].Text.Append(val);
                // いらん行を削除
                //text_.RemoveAt(s.tl + 1, e.tl - s.tl);
                text_.RemoveRange(s.tl + 1, e.tl - s.tl);
            }

            // 再解析
            //highlighter.Parse(text_[s.tl].Text, text_[s.tl].AttributeList);
        }

        private void Replace(ref VPos s, ref VPos e, ref VPos e2, out string oldValue, string newValue) {

            Delete(ref s, ref e, out oldValue);
            Insert(ref s, ref e2, newValue);

            MultiParse(s.tl, e2.tl);
        }

        private void CorrectPos(ref VPos pos) {
            // 正常範囲に収まるように修正
            pos.tl = Math.Min(pos.tl, tln() - 1);
            pos.ad = Math.Min(pos.ad, len(pos.tl));
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

        private void CorrectPos(ref DPos s, ref DPos e) {
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

        //-------------------------------------------------------------------------
        // 挿入・削除等の作業用関数群
        //-------------------------------------------------------------------------

        int getRangeLength( DPos s, DPos e )
        {
	        // とりあえず全部足す
	        int ans=0, tl=s.tl, te=e.tl;
	        for( ; tl<=te; ++tl )
		        ans += len(tl);
	        // 先頭行の分を引く
	        ans -= s.ad;
	        // 最終行の分を引く
	        ans -= len(te) - e.ad;
	        // 改行コード(CRLF)の分を加える
	        ans += (e.tl-s.tl) * 2;
	        // おしまい
	        return ans;
        }

        string getText(DPos s, DPos e )
        {
            IBuffer buff;
	        if( s.tl == e.tl )
	        {
		        // 一行だけの場合
		        //text_[s.tl].CopyAt( s.ad, e.ad-s.ad, buf );
		        //buf[e.ad-s.ad] = L'\0';
                buff = text_[s.tl].Text.Substring( s.ad, e.ad-s.ad);
	        }
	        else
	        {
                buff = new LineBuffer("");
		        // 先頭行の後ろをコピー
		        //buf += text_[s.tl].CopyToTail( s.ad, buf );
		        //*buf++ = '\r', *buf++ = '\n';
                buff.Append(text_[s.tl].Text.Substring(s.ad).ToString());
                buff.Append("\r\n");
		        // 途中をコピー
		        for( int i=s.tl+1; i<e.tl; i++ )
		        {
			        //buf += text_[i].CopyToTail( 0, buf );
			        //*buf++ = '\r', *buf++ = '\n';
                    buff.Append(text_[i].Text.Substring(0).ToString());
                    buff.Append("\r\n");
		        }
		        // 終了行の先頭をコピー
		        //buf += text_[e.tl].CopyAt( 0, e.ad, buf );
		        //*buf = L'\0';
                buff.Append(text_[e.tl].Text.Substring(0, e.ad).ToString());
	        }

            return buff.ToString();
        }


        //TODO wordStartOf
        DPos wordStartOf(DPos dp )
        {
	        if( dp.ad == 0 )
	        {
		        // 行の先頭
		        return dp;
	        }
	        else
	        {
                
		        // 行の途中
		        const uchar* f = pl(dp.tl);
			          ulong  s = dp.ad;
		        while( (f[s]>>5)==0 && 0<=s )
			        --s;
		        return DPos( dp.tl, s );

                string f = text_[dp.tl].Text[dp.ad].ToString();
                
	        }
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
