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

    //public class Document : IDocument {
    public class Document {
        internal event TextUpdateEventHandler TextUpdate;

        private List<Line> text_;
        Parser.Parser parser;

        public void setHighlight(IHighlight highlight) {
            //parser.Highlight = highlight;
            parser.AddHighlight("default", highlight);
            parser.setd("default");
        }

        public UndoManager UndoManager { get; private set; }

        /// <summary>
        /// テキストの行数
        /// </summary>
        /// <returns></returns>
        public int tln () {
            return text_.Count; //LineList.Count;
        }
        /// <summary>
        /// i行目のテキストの長さ取得
        /// </summary>
        /// <param name="i"></param>
        /// <returns></returns>
        public int len(int i) {
            return text_[i].Length;
        }
        /// <summary>
        /// i行目のテキストの取得
        /// </summary>
        /// <param name="i"></param>
        /// <returns></returns>
        public IText tl(int i) {
            return text_[i].Text;
        }

        public Line line(int i) {
            return text_[i];
        }

        public List<Token> Rules(int i) {
            return text_[i].Tokens;
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
            UndoManager.Refresh();
        }
        
        public Document() {

            parser = new AsControls.Parser.Parser();

            UndoManager = new UndoManager(this);

            text_ = new List<Line>();
            text_.Add( new Line("") ); // 最初は一行だけ
        }

        internal void Fire_TEXTUPDATE(DPos s, DPos e, DPos e2, bool reparsed, bool nmlcmd) {
            TextUpdate(s, e, e2, reparsed, nmlcmd);
        }

        internal DPos leftOf(DPos dp, bool wide) {
            if (dp.ad == 0) {
                // 行の先頭だが、ファイルの先頭ではない場合
                // 一つ前の行の行末へ
                if (dp.tl > 0)
                    return new DPos(dp.tl - 1, len(dp.tl - 1));
                return dp;
            } else if (!wide) {
                // 行の途中で、普通に１文字戻る場合
                string l = tl(dp.tl).ToString();
                //if (dp.ad >= 2 && Util.isLowSurrogate(l[dp.ad - 1]) && Util.isHighSurrogate(l[dp.ad - 2]))
                //    return new DPos(dp.tl, dp.ad - 2);
                return new DPos(dp.tl, dp.ad - 1);
            } else {
                //TODO 行の途中で、１単語分戻る場合
                //const uchar* f = pl(dp.tl);
                //ulong s = dp.ad - 1;
                //while ((f[s] >> 5) == 0 && 0 <= s)
                //    --s;
                //return DPos(dp.tl, s);

                var f = text_[dp.tl].Text;
                var s = dp.ad-1;
                Util.CharType ctype = Util.getCharType(f[s]);
                while (0 < s && Util.getCharType(f[s]) == ctype)
                    --s;

                s = s != 0 ? ++s : s;
                return new DPos(dp.tl, s);
            }
        }
        internal DPos rightOf(DPos dp, bool wide) {
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
                //if (Util.isHighSurrogate(l[dp.ad]) && Util.isLowSurrogate(l[dp.ad + 1]))
                //    return new DPos(dp.tl, dp.ad + 2);
                return new DPos(dp.tl, dp.ad + 1);
            } else {
                //TODO 行の途中で、普通に１単語進む場合
                //const uchar* f = pl(dp.tl);
                //const ulong e = len(dp.tl);
                //ulong s = dp.ad;
                //const ulong t = (f[s] >> 5);
                //s += t;
                //if (s >= e)
                //    s = e;
                //else if (t == 7 || t == 0)
                //    while ((f[s] >> 5) == 0 && s < e)
                //        ++s;
                //return DPos(dp.tl, s);

                var f = text_[dp.tl].Text;
                int e = len(dp.tl);
                int s = dp.ad;
                Util.CharType ctype = Util.getCharType(f[s]);
                while (s < e && Util.getCharType(f[s]) == ctype )
                        ++s;

                //s = s != e ? --s : s;
                return new DPos(dp.tl, s);
            }
        }

        internal Boolean LastLine(int linenum) {
            return linenum == text_.Count - 1 ? true : false;
        }

        private bool ReParse(int s, int e) {
            int i;
            int cmt = text_[s].Block.isLineHeadCmt;
            int sccmt = text_[s].Block.scisLineHeadCmt;
            Block block = text_[s].Block;
            if (s > 0) {
                block = text_[s-1].Block;
            }

            // まずは変更範囲を再解析
            for (i = s; i <= e; ++i) {

                block = parser.Parse(text_[i], block, cmt, sccmt);
                cmt = parser.cmt;
                sccmt = parser.sccmt;
            }

            // コメントアウト状態に変化がなかったらここでお終い。
            //if (i == tln() || text_[i].Block.isLineHeadCmt == cmt)
            //if (i == tln() || (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.elem == block.elem))
            if (i == tln()
                || ( (text_[i].Block.isLineHeadCmt == cmt && text_[i].Block.elem == block.elem)
                     && (text_[i].Block.scisLineHeadCmt == sccmt)))
                return false;

            int pcmt = 0;
            Rule prule = null;

            int scpcmt = 0;

            // 例えば、/* が入力された場合などは、下の方の行まで
            // コメントアウト状態の変化を伝達する必要がある。
            do{
                Line line = text_[i++];
                pcmt = line.Block.isLineHeadCmt;
                prule = line.Block.elem;

                scpcmt = line.Block.scisLineHeadCmt;

                //block = parser.Parse(line, block, cmt);
                block = parser.Parse(line, block, cmt, sccmt);
                cmt = parser.cmt;
                sccmt = parser.sccmt;

                if (pcmt == cmt) {
                    if (prule != block.elem) {
                        pcmt--;
                    }
                }

                //if (scpcmt == sccmt) {
                //    if (prule != block.elem) {
                //        scpcmt--;
                //    }
                //}

            //}while (i < tln() && pcmt != cmt);
            } while (i < tln() && (pcmt != cmt || scpcmt != sccmt));

            return true;
        }

        //private int TransitCmt(Line line, int start) {
        //    return (line.Block.commentTransition >> start) & 1;
        //}

        StringInfo strinfo = new StringInfo();
        internal bool InsertingOperation(ref DPos s, string text, ref DPos e) {
            // 位置補正
            //DPos cs = s as DPos;
            CorrectPos(ref s);

            e.ad = s.ad;
            e.tl = s.tl;

            string[] lines = SplitLine(text);

            int lineLen = 0;
            // 一行目…
            text_[e.tl].Text.Insert(e.ad, lines[0]);
            //lineLen = lines[0].Length;
            strinfo.String = lines[0];
            lineLen = strinfo.LengthInTextElements;
            e.ad += lineLen;

            // 二行目～最終行
            if (lines.Count() > 1) {
                for (int i = 1; i < lines.Count(); i++) {
                    text_.Insert(++e.tl, new Line(lines[i]));
                    //lineLen = lines[i].Length;
                    strinfo.String = lines[i];
                    lineLen = strinfo.LengthInTextElements;
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

            return ReParse(s.tl, e.tl);
        }

        internal bool DeletingOperation(ref DPos s, ref DPos e, out string undobuf) {
            // 位置補正
            CorrectPos(ref s);
            CorrectPos(ref e);
            CorrectPos(ref s, ref e);

            // 削除される量をカウント
            int undosiz = getRangeLength(s, e);

            // Undo操作用バッファ確保
            //undobuf = new unicode[undosiz + 1];
            //getText(undobuf, s, e);
            undobuf = getText(s, e);

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
            return ReParse(s.tl, s.tl);
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

        internal int getRangeLength(DPos s, DPos e)
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

        internal string getText(DPos s, DPos e)
        {
            IText buff;
	        if( s.tl == e.tl )
	        {
		        // 一行だけの場合
		        //text_[s.tl].CopyAt( s.ad, e.ad-s.ad, buf );
		        //buf[e.ad-s.ad] = L'\0';
                buff = text_[s.tl].Text.Substring( s.ad, e.ad-s.ad);
	        }
	        else
	        {
                buff = new TextBuffer("");
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
        internal DPos wordStartOf(DPos dp)
        {
	        if( dp.ad == 0 )
	        {
		        // 行の先頭
		        return dp;
	        }
	        else
	        {
                
		        // 行の途中
                //const uchar* f = pl(dp.tl);
                //      ulong  s = dp.ad;
                //while( (f[s]>>5)==0 && 0<=s )
                //    --s;
                //return DPos( dp.tl, s );

                var f = text_[dp.tl].Text;
                int s = dp.ad;
                Util.CharType ctype = Util.getCharType(f[s]);
                while (0 < s && Util.getCharType(f[s]) == ctype) {
                    --s;
                }
                //s++;
                s = s != 0 ? ++s : s;
                return new DPos(dp.tl, s);
	        }
        }

        internal void Execute(ICommand cmd) {
            //bool b = urdo_.isModified();
            //urdo_.NewlyExec(cmd, doc_);
            //if (b != urdo_.isModified())
            //    Fire_MODIFYFLAGCHANGE();
            var c = cmd.Execute(this);
            UndoManager.Invoke(c);
        }

        internal void Execute(List<ICommand> cmds) {
            var list = new List<ICommand>();
            foreach (var cmd in cmds) {
                list.Add(cmd.Execute(this)); 
            }
            UndoManager.Invoke(list);
        }

        //#region IDocument メンバ
        //public event TextUpdateEventHandler TextUpdateEvent;
        //private UndoManager undoManager = new UndoManager();
        //public UndoManager UndoManager {
        //    get { return undoManager; }
        //}

        //public void Insert(VPos s, VPos e, string text) {
        //    Insert(ref s, ref e, text);

        //    if (UndoManager.AcceptChanges) {
        //        UndoManager.Push(new UndoInsert(this, s, e, text));
        //    }
        //    if (TextUpdateEvent != null) {
        //        TextUpdateEvent(new VPos(s), new VPos(s), e);
        //    }
        //}

        //public void Delete(VPos s, VPos e, out string deltext) {
        //    string buff;
        //    Delete(ref s, ref e, out buff);
        //    deltext = buff;

        //    if (UndoManager.AcceptChanges) {
        //        UndoManager.Push(new UndoDelete(this, s, s, buff));
        //    }
        //    if (TextUpdateEvent != null) {
        //        TextUpdateEvent(new VPos(s), e, new VPos(s));
        //    }
        //}

        //public void Replace(VPos s, VPos e, string newValue) {
        //    string oldvalue;
        //    VPos e2 = new VPos();
        //    Replace(ref s, ref e, ref e2, out oldvalue, newValue);

        //    if (UndoManager.AcceptChanges) {
        //        UndoManager.Push(new UndoReplace(this, s, e, e2, oldvalue, newValue));
        //    }
        //    if (TextUpdateEvent != null) {
        //        TextUpdateEvent(new VPos(s), e, new VPos(e2));
        //    }
        //}

        //#endregion
    }
}
