using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls
{
    public partial class gcsTextEdit
    {
        public class WLine : List<int> {
	        public int width{
                get{return this[0]; }
                set{this[0] = value;}
            }
            public int rln() { return this.Count - 1; }
            public void ForceSize(int size) {
                this.RemoveRange(size, this.Count - size);
            }

            public WLine() {
                //this.Add(0);
            }

            //public void Add(int item) {
            //    if (1000000 < item) {
            //        int deb = 0;
            //    }
            //    base.Add(item);
            //}
        }

        //public class WrapInfo
        //{
        //    public List<int> wrap;
        //    public int Width;

        //    public WrapInfo()
        //    {
        //        wrap = new List<int>();
        //        wrap.Add(0);
        //    }
        //}

        //public List<WrapInfo> wrapList;
        private int vlNum_;
        public int textCx_;

        //public int vlNum
        //{
        //    get { return vlNum_; }
        //}

        public List<WLine> wrap_ ;

        //@{ 全表示行数 //@}
        public int vln() { return vlNum_; }

        //@{ 一行の表示行数 //@}
        /// <summary>
        /// 一行の表示行数
        /// </summary>
        /// <param name="tl"></param>
        /// <returns></returns>
        public int rln(int tl) { return wrap_[tl].rln(); }
        
        //@{ 折り返し位置 //@}
        /// <summary>
        /// 折り返し位置
        /// </summary>
        /// <param name="tl"></param>
        /// <param name="rl"></param>
        /// <returns></returns>
        public int rlend(int tl, int rl) { 
            return wrap_[tl][rl + 1]; 
        }

	    //@{ 一個でも折り返しが存在するか否か //@}
	    bool wrapexists() { return doc_.tln() != vln(); }

        //private void initWrap()
        //{
        //    //wrapList = new List<WrapInfo>();
        //    //wrapList.Add(new WrapInfo());

        //    wrap_ = new List<WLine>();

        //    vlNum_ = 1;
        //    textCx_ = 0;
        //}

        public int InsertMulti( int ti_s, int ti_e )
        {
	        // 指定した分だけ新しく行情報を追加。
	        // ＆折り返し情報もきちんと計算
	        //
	        // 返値は
	        //   1: "折り返しあり" or "この行が横に一番長くなった"
	        //   0: "この行以外のどこかが最長"
	        // 詳しくは ReWrapSingle() を見よ。

	        int dy=0, cx=0;
	        for( int i=ti_s; i<=ti_e; ++i )
	        {
		        WLine pwl = new WLine();
                string ss = doc_.tl(i).ToString();
		        pwl.Add( CalcLineWidth( doc_.tl(i).ToString(), doc_.len(i) ) );

                int ww = cvs_.wrapWidth();
		        if( pwl.width < cvs_.wrapWidth() )
		        {
			        // 設定した折り返し幅より短い場合は一行で済む。
			        pwl.Add( doc_.len(i) );
			        dy++;
			        if( cx < pwl.width )
				        cx = pwl.width;
		        }
		        else
		        {
			        // 複数行になる場合
			        ModifyWrapInfo( doc_.tl(i).ToString(), doc_.len(i), ref pwl, 0 );
			        dy += pwl.rln();
		        }

		        //wrap_.InsertAt( i, pwl );
                wrap_.Insert(i, pwl);
	        }

	        // 表示行の総数を修正
	        vlNum_ += dy;

	        // 折り返しなしだと総横幅の更新が必要
	        if( cvs_.wrapType == WrapType.Non )
	        {
		        if( textCx_ <= cx )
		        {
			        textCx_ = cx;
			        return 1;
		        }
		        return 0;
	        }
	        return 1;
        }

        //
        public int DeleteMulti( int ti_s, int ti_e )
        {
	        // 指定した範囲の行情報を削除
	        //
	        // 返値は
	        //   1: "折り返しあり" or "この行以外のどこかが最長"
	        //   0: "さっきまでこの行は最長だったが短くなっちゃった"
	        // 詳しくは ReWrapSingle() を見よ。

	        bool  widthChanged = false;
	        int dy = 0;

	        // 情報収集しながら削除
	        for( int cx=textCx_, i=ti_s; i<=ti_e; ++i )
	        {
		        WLine wl = wrap_[i];
		        dy += wl.rln();
		        if( cx == wl.width )
			        widthChanged = true;
	        }
	        //wrap_.RemoveAt( ti_s, (ti_e-ti_s+1) );
            wrap_.RemoveRange(ti_s, (ti_e - ti_s + 1));

	        // 表示行の総数を修正
	        vlNum_ -= dy;

	        // 折り返しなしだと総横幅の更新が必要
	        return ( cvs_.wrapType==WrapType.Non && widthChanged ) ? 0 : 1;
        }


        //
        public void DoResize(bool wrapWidthChanged) {
            // 折り返し位置再計算
            if (wrapWidthChanged) {
                ReWrapAll();
                UpdateTextCx();
            }

            // スクロール情報変更
            ReSetScrollInfo();
            if (wrapWidthChanged)
                ForceScrollTo(udScr_tl_);

            // 再描画
            ReDraw(ReDrawType.ALL, null);
            cur_.ResetPos();
        }

        //public int GetvlCnt(int rl)
        //{
        //    return wrapList[rl].wrap.Count;
        //}

        //public void ResizeWrapList(int size)
        //{
        //    if (wrapList.Count > size)
        //    {
        //        wrapList.RemoveRange(wrapList.Count, wrapList.Count - size);
        //    }
        //    else if (wrapList.Count < size)
        //    {
        //        int cnt = size - wrapList.Count;
        //        for (int i = 0; i < cnt; i++)
        //        {
        //            wrapList.Add(new WrapInfo());
        //        }
        //    }
        //}


        //public void TextUpDate(CaretInfo s, CaretInfo e, CaretInfo e2)
        //{
        //    int r3 = 0, r2 = 1, r1 = ReWrapSingle(s);

        //    // 残りを調整
        //    if (s.tl != e.tl)
        //        r2 = DeleteMulti(s.tl + 1, e.tl);
        //    if (s.tl != e2.tl)
        //        r3 = InsertMulti(s.tl + 1, e2.tl);

        //    // この変更で横幅が…
        //    // if( "長くなったなてはいない" AND "短くなっちゃった可能性あり" )
        //    //     横幅再計算();
        //    if (!(r1 == 2 || r3 == 1) && (r1 == 0 || r2 == 0))
        //        UpdateTextCx();

        //    ReSetScrollInfo();

        //    Boolean doResize = false;

        //    // 行数に変化があって、行番号表示域の幅を変えなきゃならん時
        //    if (e.tl != e2.tl && CalcNumLineWidth())
        //    {
        //        doResize = true;
        //    }

        //    // 再描画
        //    if (doResize)
        //    {
        //        //DoResize(true);
        //        if (wrapType == WrapType.WindowWidth) {
        //            ReWrapAll();
        //        }
        //    }
        //    else
        //    {
        //        //if (e.tl != e2.tl) // 行番号領域再描画の必要があるとき
        //        //    ReDraw(LNAREA, 0);
        //        //ReDraw(t, &s);
        //    }
        //}
        //
        public void on_text_update
	        ( DPos s, DPos e, DPos e2, bool bAft, bool mCur )
        {
	        // まず、折り返し位置再計算

	        // 置換範囲の先頭行を調整
	        int r3 = 0, r2 = 1, r1 = ReWrapSingle( s );

	        // 残りを調整
	        if( s.tl != e.tl )
		        r2 = DeleteMulti( s.tl+1,  e.tl );
	        if( s.tl != e2.tl )
		        r3 = InsertMulti( s.tl+1, e2.tl );

	        // この変更で横幅が…
	        // if( "長くなったなてはいない" AND "短くなっちゃった可能性あり" )
	        //     横幅再計算();
	        if( !(r1==2 || r3==1) && (r1==0 || r2==0) )
		        UpdateTextCx();

	        // スクロールバー修正
	        ReDrawType t = TextUpdate_ScrollBar( s, e, e2 );
	        bool doResize = false;

	        // 行数に変化があって、行番号表示域の幅を変えなきゃならん時
	        if( e.tl!=e2.tl && cvs_.on_tln_change( doc_.tln() ) )
	        {
		        doResize = true;
	        } else if (bAft && t != ReDrawType.ALL)
	        {
                t = ReDrawType.AFTER;
	        }

	        // カーソル移動
	        cur_.on_text_update( s, e, e2, mCur );

	        // 再描画
	        if( doResize )
		        DoResize( true );
	        else
	        {
		        if( e.tl != e2.tl ) // 行番号領域再描画の必要があるとき
			        ReDraw( ReDrawType.LNAREA, null );
		        ReDraw( t, s );
	        }
        }

        private void UpdateTextCx()
        {
            //if (Wrap == WrapType.Non)
            //{
            //    // 折り返しなしなら、数えてみないと横幅はわからない
            //    int cx = 0;
            //    for (int i = 0, ie = doc.LineList.Count; i < ie; ++i)
            //        if (cx < wrapList[i].Width)
            //            cx = wrapList[i].Width;
            //    textCx_ = cx;
            //}
            //else
            //{
            //    // 折り返しありなら、横幅:=折り返し幅とする
            //    textCx_ = ViewWidth;
            //}
            if (cvs_.wrapType == WrapType.Non) {
                // 折り返しなしなら、数えてみないと横幅はわからない
                int cx = 0;
                for (int i = 0, ie = doc_.tln(); i < ie; ++i)
                    if (cx < wrap_[i].width)
                        cx = wrap_[i].width;
                textCx_ = cx;
            } else {
                // 折り返しありなら、横幅:=折り返し幅とする
                textCx_ = cvs_.wrapWidth();
            }
        }

        //int InsertMulti(int ti_s, int ti_e)
        //{
        //    // 指定した分だけ新しく行情報を追加。
        //    // ＆折り返し情報もきちんと計算
        //    //
        //    // 返値は
        //    //   1: "折り返しあり" or "この行が横に一番長くなった"
        //    //   0: "この行以外のどこかが最長"
        //    // 詳しくは ReWrapSingle() を見よ。

        //    int dy = 0, cx = 0;
        //    for (int i = ti_s; i <= ti_e; ++i)
        //    {
        //        WrapInfo pwl = new WrapInfo();
        //        pwl.Width = CalcStringWidth(doc.LineList[i].Text);

        //        if (pwl.Width < ViewWidth)
        //        {
        //            // 設定した折り返し幅より短い場合は一行で済む。
        //            pwl.wrap.Clear();
        //            pwl.wrap.Add(doc.LineList[i].Length);
        //            dy++;
        //            if (cx < pwl.Width)
        //                cx = pwl.Width;
        //        }
        //        else
        //        {
        //            pwl.wrap.Clear();
        //            // 複数行になる場合
        //            ModifyWrapInfo(i, ref pwl, 0);
        //            dy += pwl.wrap.Count;
        //        }

        //        wrapList.Insert(i, pwl);
        //    }

        //    // 表示行の総数を修正
        //    vlNum_ += dy;

        //    // 折り返しなしだと総横幅の更新が必要
        //    if (Wrap == WrapType.Non)
        //    {
        //        if (textCx_ <= cx)
        //        {
        //            textCx_ = cx;
        //            return 1;
        //        }
        //        return 0;
        //    }
        //    return 1;
        //}

        //int DeleteMulti(int ti_s, int ti_e)
        //{
        //    // 指定した範囲の行情報を削除
        //    //
        //    // 返値は
        //    //   1: "折り返しあり" or "この行以外のどこかが最長"
        //    //   0: "さっきまでこの行は最長だったが短くなっちゃった"
        //    // 詳しくは ReWrapSingle() を見よ。

        //    bool widthChanged = false;
        //    int dy = 0;

        //    // 情報収集しながら削除
        //    for (int cx = textCx_, i = ti_s; i <= ti_e; ++i)
        //    {
        //        WrapInfo wl = wrapList[i];
        //        dy += wl.wrap.Count;
        //        if (cx == wl.Width)
        //            widthChanged = true;
        //    }
        //    wrapList.RemoveRange(ti_s, (ti_e - ti_s + 1));

        //    // 表示行の総数を修正
        //    vlNum_ -= dy;

        //    // 折り返しなしだと総横幅の更新が必要
        //    return (Wrap == WrapType.Non && widthChanged) ? 0 : 1;
        //}

        //
        public void ReWrapAll()
        {
            // 折り返し幅に変更があった場合に、全ての行の
            // 折り返し位置情報を変更する。
            int ww = cvs_.wrapWidth();

            int vln = 0;
            for (int i = 0, ie = doc_.tln(); i < ie; ++i) {
                WLine wl = wrap_[i];
                wl.ForceSize(1);
                //wl.RemoveRange(1, wl.Count - 2);

                if (wl.width < ww) {
                    // 設定した折り返し幅より短い場合は一行で済む。
                    wl.Add(doc_.len(i));
                    ++vln;
                } else {
                    // 複数行になる場合
                    ModifyWrapInfo(doc_.tl(i).ToString(), doc_.len(i), ref wl, 0);
                    vln += wl.rln();
                }
            }
            vlNum_ = vln;
        }
        //
        //private int ReWrapSingle(CaretInfo s)
        //{
        //    // 指定した一行のみ折り返しを修正。
        //    //
        //    // 返値は
        //    //   2: "折り返しあり" or "この行が横に一番長くなった"
        //    //   1: "この行以外のどこかが最長"
        //    //   0: "さっきまでこの行は最長だったが短くなっちゃった"
        //    // で、上位ルーチンにm_TextCx修正の必要性を伝える。
        //    //
        //    // 昔は再描画範囲の計算のために、表示行数の変化を返していたが、
        //    // これは上位ルーチン側で vln() を比較すれば済むし、
        //    // むしろその方が効率的であるため廃止した。

        //    // 旧情報保存
        //    WrapInfo wl = wrapList[s.tl];
        //    int oldVRNum = wl.wrap.Count;
        //    int oldWidth = wl.Width;

        //    // 横幅更新
        //    wl.Width = CalcStringWidth(doc.LineList[s.tl].Text);

        //    if (wl.Width < ViewWidth)
        //    {
        //        // 設定した折り返し幅より短い場合は一行で済む。
        //        //wl[1] = doc_.len(s.tl);
        //        //wl.ForceSize(2);
        //        wrapList[s.tl].wrap.Clear();
        //        wrapList[s.tl].wrap.Add(doc.LineList[s.tl].Length);
        //    }
        //    else
        //    {
        //        //// 複数行になる場合
        //        //ulong vr = 1, stt = 0;
        //        //while (wl[vr] < s.ad) // while( vr行目は変更箇所より手前 )
        //        //    stt = wl[vr++];  // stt = 次の行の行頭のアドレス

        //        //// 変更箇所以降のみ修正
        //        //wl.ForceSize(vr);
        //        //ModifyWrapInfo(doc_.tl(s.tl), doc_.len(s.tl), wl, stt);

        //        int vr = 0, stt = 0;
        //        while (wl.wrap[vr] < s.ad) // while( vr行目は変更箇所より手前 )
        //            stt = wl.wrap[vr++];  // stt = 次の行の行頭のアドレス

        //        //wl.wrap.RemoveAt(vr);
        //        wl.wrap.RemoveRange(vr, wl.wrap.Count - vr);

        //        ModifyWrapInfo(s.tl, ref wl, stt);
        //    }

        //    // 表示行の総数を修正
        //    vlNum_ += (wl.wrap.Count - oldVRNum);

        //    // 折り返しなしだと総横幅の更新が必要
        //    if (Wrap == WrapType.Non)
        //        if (textCx_ <= wl.Width)
        //        {
        //            textCx_ = wl.Width;
        //            return 2;
        //        }
        //        else if (textCx_ == oldWidth)
        //        {
        //            return 0;
        //        }
        //        else
        //        {
        //            return 1;
        //        }
        //    return 2;

        //}

        //
        int ReWrapSingle( DPos s )
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
	        WLine wl            = wrap_[s.tl];
	        int oldVRNum = wl.rln();
	        int oldWidth = wl.width;

	        // 横幅更新
	        wl.width = CalcLineWidth( doc_.tl(s.tl).ToString(), doc_.len(s.tl) );

	        if( wl.width < cvs_.wrapWidth() )
	        {
		        // 設定した折り返し幅より短い場合は一行で済む。
		        wl[1] = doc_.len(s.tl);
		        wl.ForceSize( 2 );
	        }
	        else
	        {
		        // 複数行になる場合
		        int vr=1, stt=0;
		        while( wl[vr] < s.ad ) // while( vr行目は変更箇所より手前 )
			        stt = wl[ vr++ ];  // stt = 次の行の行頭のアドレス

		        // 変更箇所以降のみ修正
		        wl.ForceSize( vr );
		        ModifyWrapInfo( doc_.tl(s.tl).ToString(), doc_.len(s.tl), ref wl, stt );
	        }

	        // 表示行の総数を修正
	        vlNum_ += ( wl.rln() - oldVRNum );

	        // 折り返しなしだと総横幅の更新が必要
	        //if( cvs_.wrapType() == NOWRAP )
            if (cvs_.wrapType == WrapType.Non)
		        if( textCx_ <= wl.width )
		        {
			        textCx_ = wl.width;
			        return 2;
		        }
		        else if( textCx_ == oldWidth )
		        {
			        return 0;
		        }
		        else
		        {
			        return 1;
		        }
	        return 2;
        }

        //
        public void ModifyWrapInfo(string txt, int len, ref WLine wl, int stt)
        {
	        // 設定幅での折り返しを実行する。
	        // 行の途中からの変更の場合、sttが開始addressを指している
	        Painter p = cvs_.getPainter();
	        int   ww = cvs_.wrapWidth();

	        while( stt < len )
	        {
		        int i, w;
		        for( w=0,i=stt; i<len; ++i )
		        {
			        if( txt[i] == '\t' )
				        w = p.nextTab(w);
			        else
				        w += p.W( txt[i] );

			        if( w>ww )
				        break; // 幅が設定値を超えた所でおしまい
		        }
		        wl.Add( stt = (i==stt?i+1:i) );
	        }
        }

        //
        public void GetVPos(int x, int y, ref VPos vp, bool linemode) {
            // x座標補正
            x = x - lna() + hScrollBar.Value;
            
            // まず行番号計算
            int tl = udScr_tl_;
            int vl = vScrollBar.Value - udScr_vrl_;
            int rl = y / fnt().H() + udScr_vrl_;
            if (rl >= 0) { // View上端より下の場合、下方向を調べる
                while (tl < doc_.tln() && rln(tl) <= rl) {
                    vl += rln(tl);
                    rl -= rln(tl);
                    ++tl;
                }
            } else {           // View上端より上の場合、上方向を調べる
                while (0 <= tl && rl < 0) {
                    vl -= rln(tl);
                    rl += rln(tl);
                    --tl;
                }
            }

            if (tl == doc_.tln()) { // EOFより下に行ってしまう場合の補正
                --tl;
                vl -= rln(tl);
                rl = rln(tl) - 1;
            } else if (tl == -1) {// ファイル頭より上に行ってしまう場合の補正

                tl = vl = rl = 0;
            } else {
                if (linemode) // 行選択モードの場合
                {
                }
            }

            vp.tl = tl;
            vp.vl = vl + rl;
            vp.rl = rl;

            // 次に、横位置を計算
            if (rl < wrap_[tl].rln()) {
                IBuffer buf = doc_.tl(tl);
                int adend = rlend(tl, rl);
                int ad = (rl == 0 ? 0 : rlend(tl, rl - 1));
                int vx = (rl == 0 ? 0 : fnt().CalcStringWidth(buf.Substring(ad++, 1).ToString())); //TODO

                while (ad < adend) {

                    //int nvx = (str[ad]==L'\t'
                    //? fnt().nextTab(vx)
                    //:  vx + fnt().W(&str[ad])
                    //);

                    int nvx = vx + fnt().CalcStringWidth(buf.Substring(ad, 1).ToString()); //TODO
                    if (x + 2 < nvx)
                        break;
                    vx = nvx;
                    ++ad;
                }

                vp.ad = ad;
                vp.rx = vp.vx = vx;
            } else {
                vp.ad = vp.rx = vp.vx = 0;
            }
        }
    }
}
