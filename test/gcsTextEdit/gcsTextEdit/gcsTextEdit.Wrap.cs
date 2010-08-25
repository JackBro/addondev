using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls
{
    public partial class gcsTextEdit
    {
        public class WrapInfo
        {
            public List<int> wrap;
            public int Width;

            public WrapInfo()
            {
                wrap = new List<int>();
                wrap.Add(0);
            }
        }

        public List<WrapInfo> wrapList;
        private int vlNum_;
        public int textCx_;

        public int vlNum
        {
            get { return vlNum_; }
        }

        private void initWrap()
        {
            wrapList = new List<WrapInfo>();
            wrapList.Add(new WrapInfo());

            vlNum_ = 1;
            textCx_ = 0;
        }

        public int GetvlCnt(int rl)
        {
            return wrapList[rl].wrap.Count;
        }

        public void ResizeWrapList(int size)
        {
            if (wrapList.Count > size)
            {
                wrapList.RemoveRange(wrapList.Count, wrapList.Count - size);
            }
            else if (wrapList.Count < size)
            {
                int cnt = size - wrapList.Count;
                for (int i = 0; i < cnt; i++)
                {
                    wrapList.Add(new WrapInfo());
                }
            }
        }


        public void TextUpDate(CaretInfo s, CaretInfo e, CaretInfo e2)
        {
            int r3 = 0, r2 = 1, r1 = ReWrapSingle(s);

            // 残りを調整
            if (s.tl != e.tl)
                r2 = DeleteMulti(s.tl + 1, e.tl);
            if (s.tl != e2.tl)
                r3 = InsertMulti(s.tl + 1, e2.tl);

            // この変更で横幅が…
            // if( "長くなったなてはいない" AND "短くなっちゃった可能性あり" )
            //     横幅再計算();
            if (!(r1 == 2 || r3 == 1) && (r1 == 0 || r2 == 0))
                UpdateTextCx();

            ReSetScrollInfo();

            Boolean doResize = false;

            // 行数に変化があって、行番号表示域の幅を変えなきゃならん時
            if (e.tl != e2.tl && CalcNumLineWidth())
            {
                doResize = true;
            }

            // 再描画
            if (doResize)
            {
                //DoResize(true);
                if (wrapType == WrapType.WindowWidth) {
                    ReWrapAll();
                }
            }
            else
            {
                //if (e.tl != e2.tl) // 行番号領域再描画の必要があるとき
                //    ReDraw(LNAREA, 0);
                //ReDraw(t, &s);
            }
        }

        private void UpdateTextCx()
        {
            if (Wrap == WrapType.Non)
            {
                // 折り返しなしなら、数えてみないと横幅はわからない
                int cx = 0;
                for (int i = 0, ie = doc.LineList.Count; i < ie; ++i)
                    if (cx < wrapList[i].Width)
                        cx = wrapList[i].Width;
                textCx_ = cx;
            }
            else
            {
                // 折り返しありなら、横幅:=折り返し幅とする
                textCx_ = ViewWidth;
            }
        }

        int InsertMulti(int ti_s, int ti_e)
        {
            // 指定した分だけ新しく行情報を追加。
            // ＆折り返し情報もきちんと計算
            //
            // 返値は
            //   1: "折り返しあり" or "この行が横に一番長くなった"
            //   0: "この行以外のどこかが最長"
            // 詳しくは ReWrapSingle() を見よ。

            int dy = 0, cx = 0;
            for (int i = ti_s; i <= ti_e; ++i)
            {
                WrapInfo pwl = new WrapInfo();
                pwl.Width = CalcStringWidth(doc.LineList[i].Text);

                if (pwl.Width < ViewWidth)
                {
                    // 設定した折り返し幅より短い場合は一行で済む。
                    pwl.wrap.Clear();
                    pwl.wrap.Add(doc.LineList[i].Length);
                    dy++;
                    if (cx < pwl.Width)
                        cx = pwl.Width;
                }
                else
                {
                    pwl.wrap.Clear();
                    // 複数行になる場合
                    ModifyWrapInfo(i, ref pwl, 0);
                    dy += pwl.wrap.Count;
                }

                wrapList.Insert(i, pwl);
            }

            // 表示行の総数を修正
            vlNum_ += dy;

            // 折り返しなしだと総横幅の更新が必要
            if (Wrap == WrapType.Non)
            {
                if (textCx_ <= cx)
                {
                    textCx_ = cx;
                    return 1;
                }
                return 0;
            }
            return 1;
        }

        int DeleteMulti(int ti_s, int ti_e)
        {
            // 指定した範囲の行情報を削除
            //
            // 返値は
            //   1: "折り返しあり" or "この行以外のどこかが最長"
            //   0: "さっきまでこの行は最長だったが短くなっちゃった"
            // 詳しくは ReWrapSingle() を見よ。

            bool widthChanged = false;
            int dy = 0;

            // 情報収集しながら削除
            for (int cx = textCx_, i = ti_s; i <= ti_e; ++i)
            {
                WrapInfo wl = wrapList[i];
                dy += wl.wrap.Count;
                if (cx == wl.Width)
                    widthChanged = true;
            }
            wrapList.RemoveRange(ti_s, (ti_e - ti_s + 1));

            // 表示行の総数を修正
            vlNum_ -= dy;

            // 折り返しなしだと総横幅の更新が必要
            return (Wrap == WrapType.Non && widthChanged) ? 0 : 1;
        }

        public void ReWrapAll()
        {
            // 折り返し幅に変更があった場合に、全ての行の
            // 折り返し位置情報を変更する。
            int ww = ViewWidth;

            int vln = 0;
            for (int i = 0, ie = doc.LineList.Count; i < ie; ++i)
            {
                //WLine & wl = wrap_[i];
                //wl.ForceSize(1);
                WrapInfo winfo = wrapList[i];
                winfo.wrap.Clear();

                if (winfo.Width < ww)
                {
                    // 設定した折り返し幅より短い場合は一行で済む。
                    winfo.wrap.Add(doc.LineList[i].Text.Length);
                    ++vln;
                }
                else
                {
                    // 複数行になる場合
                    ModifyWrapInfo(i, ref winfo, 0);
                    vln += winfo.wrap.Count;
                }
            }
            vlNum_ = vln;
        }

        private int ReWrapSingle(CaretInfo s)
        {
            // 指定した一行のみ折り返しを修正。
            //
            // 返値は
            //   2: "折り返しあり" or "この行が横に一番長くなった"
            //   1: "この行以外のどこかが最長"
            //   0: "さっきまでこの行は最長だったが短くなっちゃった"
            // で、上位ルーチンにm_TextCx修正の必要性を伝える。
            //
            // 昔は再描画範囲の計算のために、表示行数の変化を返していたが、
            // これは上位ルーチン側で vln() を比較すれば済むし、
            // むしろその方が効率的であるため廃止した。

            // 旧情報保存
            WrapInfo wl = wrapList[s.tl];
            int oldVRNum = wl.wrap.Count;
            int oldWidth = wl.Width;

            // 横幅更新
            wl.Width = CalcStringWidth(doc.LineList[s.tl].Text);

            if (wl.Width < ViewWidth)
            {
                // 設定した折り返し幅より短い場合は一行で済む。
                //wl[1] = doc_.len(s.tl);
                //wl.ForceSize(2);
                wrapList[s.tl].wrap.Clear();
                wrapList[s.tl].wrap.Add(doc.LineList[s.tl].Length);
            }
            else
            {
                //// 複数行になる場合
                //ulong vr = 1, stt = 0;
                //while (wl[vr] < s.ad) // while( vr行目は変更箇所より手前 )
                //    stt = wl[vr++];  // stt = 次の行の行頭のアドレス

                //// 変更箇所以降のみ修正
                //wl.ForceSize(vr);
                //ModifyWrapInfo(doc_.tl(s.tl), doc_.len(s.tl), wl, stt);

                int vr = 0, stt = 0;
                while (wl.wrap[vr] < s.ad) // while( vr行目は変更箇所より手前 )
                    stt = wl.wrap[vr++];  // stt = 次の行の行頭のアドレス

                //wl.wrap.RemoveAt(vr);
                wl.wrap.RemoveRange(vr, wl.wrap.Count - vr);

                ModifyWrapInfo(s.tl, ref wl, stt);
            }

            // 表示行の総数を修正
            vlNum_ += (wl.wrap.Count - oldVRNum);

            // 折り返しなしだと総横幅の更新が必要
            if (Wrap == WrapType.Non)
                if (textCx_ <= wl.Width)
                {
                    textCx_ = wl.Width;
                    return 2;
                }
                else if (textCx_ == oldWidth)
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            return 2;

        }

        public void ModifyWrapInfo(int tl, ref WrapInfo winfo, int stt)
        {
            // 設定幅での折り返しを実行する。
            // 行の途中からの変更の場合、sttが開始addressを指している
            //const Painter& p = cvs_.getPainter();
            int ww = ViewWidth;
            int len = doc.LineList[tl].Text.Length;

            while (stt < len)
            {
                int i, w;
                for (w = 0, i = stt; i < len; ++i)
                {
                    //if( txt[i] == '\t' )
                    //    w = p.nextTab(w);
                    //else
                    //    w += p.W( &txt[i] );
                    //w += EditView.CalcStringWidth(txt[i].ToString());
                    //int kkk = CalcStringWidth(doc.LineList[tl].Text, i, 1);
                    w += CalcStringWidth(doc.LineList[tl].Text, i, 1);

                    if (w > ww)
                        break; // 幅が設定値を超えた所でおしまい
                }
                int ccc = wrapList.Count;
                winfo.wrap.Add(stt = (i == stt ? i + 1 : i));
            }
        }
    }
}
