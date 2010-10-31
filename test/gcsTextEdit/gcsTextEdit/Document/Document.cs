using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Globalization;
using YYS.Parser;
using System.Collections;

namespace YYS {

    public enum lbcode {
        CR,
        LF,
        CRLF
    }

    public enum DocumentEventType {
        None,
        Clear,
        Insert,
        Delete,
        Replace
    }

    public class DocumentEventArgs : EventArgs {
        public Document document;
        public DocumentEventType type;
        public DPos s;
        public int len;
        public string value;

        public DocumentEventArgs(Document document, DocumentEventType type, DPos s, int len, string value ){
            this.document = document;
            this.type = type;
            this.s = s;
            this.len = len;
            this.value = value;
        }
    }

    public delegate void DocumentEventHandler(object sender, DocumentEventArgs e);
    
    public class Document : IEnumerable<string> {
    //public class Document {

        public static string DEFAULT_ID = "default";

        public event DocumentEventHandler DocumentChanged;

        internal event TextUpdateEventHandler TextUpdateEvent;

        private List<Line> text_;

        Parser.Parser parser;

        public void setHighlight(IHighlight highlight) {
            parser.AddHighlight(Document.DEFAULT_ID, highlight);
            parser.setd(Document.DEFAULT_ID);
        }

        public void setHighlight(string id, IHighlight highlight) {
            parser.AddHighlight(id, highlight);
        }

        public void AddPartition(PartRule rule) {
            parser.AddPartition(rule);
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

        public string Text {
            get {
                return ToString();
            }
            set { 
                //Clear();
                ClearAll();
                //this.Insert(new DPos(0, 0), value);
                DPos s = new DPos(0,0);
                DPos e = new DPos(0,0);
                InsertingOperation(ref s, value, ref e); 
                //Fire_TEXTUPDATE(DPos s, DPos e, DPos e2, bool reparsed, bool nmlcmd);
                Fire_TEXTUPDATE(new DPos(0, 0), new DPos(0, 0), e, true, false);
            }
        }

        public override string ToString() {
            StringBuilder str = new StringBuilder();
            int i = 0;
            for (i = 0; i < text_.Count-1; i++) {
                str.AppendLine(text_[i].Text.ToString());
                str.Append(Environment.NewLine);
            }
            str.AppendLine(text_[i].Text.ToString());
            return str.ToString();
        }
         
        public string GetText(lbcode code) {

            string lb = Environment.NewLine;
            switch (code) {
                case lbcode.CR:
                    lb = "\r";
                    break;
                case lbcode.LF:
                    lb = "\n";
                    break;
                case lbcode.CRLF:
                    lb = "\r\n";
                    break;
                default:
                    break;
            }

            StringBuilder str = new StringBuilder();
            int i = 0;
            for (i = 0; i < text_.Count - 1; i++) {
                str.AppendLine(text_[i].Text.ToString());
                str.Append(lb);
            }
            str.AppendLine(text_[i].Text.ToString());
            return str.ToString();
        }

        public void ClearAll() {
            //text_.Clear();
            //text_.Add(new Line(""));
            //if (tln() - 1 >= 0 && tl(tln() - 1).Length >= 0) {
                this.Delete(new DPos(0, 0), new DPos(tln() - 1, tl(tln() - 1).Length));
            //}
            UndoManager.Clear();
        }
        
        public Document() {
            parser = new YYS.Parser.Parser();

            UndoManager = new UndoManager(this);

            text_ = new List<Line>();
            text_.Add( new Line("") ); // 最初は一行だけ
        }

        internal void Fire_TEXTUPDATE(DPos s, DPos e, DPos e2, bool reparsed, bool nmlcmd) {
            TextUpdateEvent(s, e, e2, reparsed, nmlcmd);
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
            undobuf = GetText(s, e);

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

        private string[] separater= new string[] { "\r\n", "\r", "\n" };
        //private string[] separater = new string[] { "\r\n" };
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

        public string GetText(DPos s, DPos e)
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

        public void Execute(ICommand command) {
            var c = command.Execute(this);
            UndoManager.Invoke(c);
        }

        public void Execute(List<ICommand> commands) {
            var list = new List<ICommand>();
            foreach (var cmd in commands) {
                list.Add(cmd.Execute(this)); 
            }
            UndoManager.Invoke(list);
        }

        public int LineCount {
            get { return tln(); }
        }

        public void Insert(DPos s, string value) {
            this.Execute(new Insert(s, value));
        }

        public void Delete(DPos s, DPos e) {
            this.Execute(new Delete(s, e));
        }

        public void Replace(DPos s, DPos e, string newValue) {
            this.Execute(new Replace(s, e, newValue));
        }

        public void Undo() {
            this.UndoManager.Undo();
        }

        public void Redo() {
            this.UndoManager.Redo();
        }

        public bool CanUndo() {
            return this.UndoManager.CanUndo;
        }

        public bool CanRedo() {
            return this.UndoManager.CanRedo;
        }

        public string GetText(int line) {
            return tl(line).ToString();        
        }

        public int GetLength(int line) {
            return tl(line).Length;
        }

        #region IEnumerable<string> メンバ

        IEnumerator<string> IEnumerable<string>.GetEnumerator() {
            for (int i = 0; i < tln(); i++) {
                yield return this.tl(i).ToString();
            }
        }

        #endregion

        #region IEnumerable メンバ

        IEnumerator IEnumerable.GetEnumerator() {
            for (int i = 0; i < tln(); i++) {
                yield return this.tl(i).ToString();
            }
        }

        #endregion
    }
}
