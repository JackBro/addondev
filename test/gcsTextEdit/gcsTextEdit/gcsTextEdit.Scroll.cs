using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;

namespace AsControls {
    partial class gcsTextEdit {

        //
        //-------------------------------------------------------------------------
        // スクロールバー計算ルーチン
        //-------------------------------------------------------------------------
        // rl (横スクロール情報)
        // max:  view.txt.txtwidth()
        // page: view.cx()
        // pos:  0～max-page

        // ud (縦スクロール情報)
        // max:   view.txt.vln() + page - 1
        // page:  view.cy() / view.fnt.H()
        // delta: 0～view.fnt.H()
        // pos:   0～max-page (topの行番号)

        public bool ReSetScrollInfo()
        {
	        int prevRlPos = hScrollBar.Value;
            //Rectangle rec = cvs_.zone();
	        int cx = cvs_.zone().Right - cvs_.zone().Left;
	        int cy = cvs_.zone().Bottom;

	        // 横は変な値にならないよう補正するだけでよい
        //	rlScr_.nPage = cx + 1;
        //	rlScr_.nMax  = Max( textCx_, cx );
        //	rlScr_.nPos  = Min<int>( rlScr_.nPos, rlScr_.nMax-rlScr_.nPage+1 );
            hScrollBar.Left = 0;
            hScrollBar.Top = this.Height - hScrollBar.Height;
            hScrollBar.Width = this.Width - vScrollBar.Width;
            hScrollBar.nPage = cx + 1;
            //hScrollBar.Maximum = Math.Max(textCx_ + 3, cx);
            hScrollBar.Maximum = Math.Max(textCx_, cx);
            hScrollBar.Value = Math.Min(hScrollBar.Value, hScrollBar.Maximum - hScrollBar.nPage + 1);
            hScrollBar.SmallChange = 1;
            hScrollBar.SmallChange = fnt().W()+1; //TODO
            hScrollBar.LargeChange = hScrollBar.nPage;

	        // 縦はnPageとnMaxはとりあえず補正
	        // nPosは場合によって直し方が異なるので別ルーチンにて
            vScrollBar.Left = this.Width - vScrollBar.Width;
            vScrollBar.Top = 0;
            vScrollBar.Height = this.Height - hScrollBar.Height;
            vScrollBar.nPage = cy / cvs_.getPainter().H() + 1;
            vScrollBar.Maximum = vln() + vScrollBar.nPage - 2;
            vScrollBar.SmallChange = 1;
            //vScrollBar.SmallChange = cvs_.getPainter().H();
            vScrollBar.LargeChange = vScrollBar.nPage;

            //EditorPanel.Top = 0;
            //EditorPanel.Left = 0;
            //EditorPanel.Width = this.Width - vScrollBar.Width;
            //EditorPanel.Height = this.Height - hScrollBar.Height;
	        
            // 横スクロールが起きたらtrue
            return (prevRlPos != hScrollBar.Value);
        }
        //
        int tl2vl(int tl) {
            if (vln() == doc_.tln())
                return tl;

            int vl = 0;
            for (int i = 0; i < tl; ++i)
                vl += rln(i);
            return vl;
        }
        //
        void ForceScrollTo(int tl) {
            vScrollBar.Value = tl2vl(tl);
            udScr_tl_ = tl;
            udScr_vrl_ = 0;
        }

        //
        void ScrollBar_Scroll(object sender, ScrollEventArgs e) {
            int d = e.NewValue - e.OldValue;
            if (d != 0) {
                if (e.ScrollOrientation == ScrollOrientation.HorizontalScroll) {
                    ScrollView(d, 0, true);

                    //int x = 0;
                    //x = -(hScrollBar.Value + d);
                    //x += cur.vx;
                    //vPos.X = x;
                } else if (e.ScrollOrientation == ScrollOrientation.VerticalScroll) {
                    //ScUpDown(d);
                    bool thumb = e.Type == ScrollEventType.LargeDecrement || e.Type == ScrollEventType.LargeIncrement;
                    //UpDown(d, thumb);
                    UpDown(d, false); //TODO scroll

                    //int x = vRect.NumLineLeft;
                    //int y = 0;
                    //y = -(vScrollBar.Value + d) * lineHeight;
                    //y = -(vScrollBar.Value) * lineHeight;
                    //x += cur.vx;
                    //y += cur.vl * ViewLineHeight;
                    //vPos.X = x;
                    //vPos.Y = y;
                }
                //base.Invalidate();
            }
        }

        public void ScrollView(int dx, int dy, bool update) {
            
            // スクロール開始通知
	        cur_.on_scroll_begin();

	        Rectangle clip = (dy==0 ? cvs_.zone() : new Rectangle() /*NULL*/);
	        int H = cvs_.getPainter().H();

	        // スクロールバー更新
	        if( dx != 0 ){
		        // 範囲チェック
		        if( hScrollBar.Value+dx < 0 )
			        dx = -hScrollBar.Value;
		        else if( hScrollBar.Maximum-hScrollBar.nPage < hScrollBar.Value+dx ) 
			        dx = hScrollBar.Maximum-hScrollBar.nPage-hScrollBar.Value+1;

		        //hScrollBar.Value += dx;
		        //::SetScrollInfo( hwnd_, SB_HORZ, &rlScr_, TRUE );
		        //dx = -dx;
	        }
	        if( dy != 0 ){
		        // 範囲チェック…は前処理で終わってる。

		        //vScrollBar.Value += dy;
		        //::SetScrollInfo( hwnd_, SB_VERT, &udScr_, TRUE );
		        //dy *= -H;
                //dy *= H;
	        }

	        if( dx!=0 || dy!=0 ){
		        if( -dx>=right() || dx>=right()
		         || -dy>=bottom() || dy>=bottom() ){
			        // 全画面再描画
			        // ちょうど65536の倍数くらいスクロールしたときに、
			        // ScrollWindowEx on Win9x だと再描画が変なのを回避。
			        //::InvalidateRect( hwnd_, NULL, FALSE );
                    this.Invalidate(false);
		        }else{
			        // 再描画の不要な領域をスクロール
			        //::ScrollWindowEx( hwnd_, dx, dy, NULL, 
					//        clip, NULL, NULL, SW_INVALIDATE );
                    hScrollBar.Value += dx;
                    vScrollBar.Value += dy;

                    //this.AutoScrollPosition = new Point(this.AutoScrollPosition.X + dy, this.AutoScrollPosition.Y + dy);
			        // 即時再描画？
			        if( update ){
				        // 縦スクロールは高速化したいので一工夫
				        if( dy != 0 ){
                            //// 再描画の必要な領域を自分で計算
                            //Rectangle rc = new Rectangle (0,0,right(),bottom());
                            ////dy *= H;
                            //if( dy < 0 ){
                            //    //rc.Top  = rc.Bottom + dy;
                            //    rc = new Rectangle(rc.Left, rc.Bottom + dy, rc.Width, rc.Height);
                            //}else{
                            //    //rc.Bottom = dy;
                            //    rc = new Rectangle(rc.Left, rc.Top, rc.Width, dy);
                            //}
					        // インテリマウスの中ボタンクリックによる
					        // オートスクロール用カーソルの下の部分を先に描く
					        // ２回に分けることで、小さな矩形部分二つで済むので高速
					        //::ValidateRect( hwnd_, &rc );
					        //::UpdateWindow( hwnd_ );
					        //::InvalidateRect( hwnd_, &rc, FALSE );
                            
                            //this.Invalidate(rc, false);
                            this.Invalidate(false);
                            
				        }
				        //::UpdateWindow( hwnd_ );       
                        this.Update(); //TODO scroll
                        
			        }
		        }
	        }

	        // スクロール終了通知
	        cur_.on_scroll_end();
        }

        //
        void InvalidateView( DPos dp, bool afterall ){
	        int H = cvs_.getPainter().H();

	        // 表示域より上での更新
	        if( dp.tl < udScr_tl_ )
	        {
		        if( afterall )
			        //::InvalidateRect( hwnd_, NULL, FALSE );
                    this.Invalidate(false);
		        return;
	        }

	        // 開始y座標計算
	        int r=0, yb=-udScr_vrl_;
	        for( int t=udScr_tl_, ybe=cy()/H; t<dp.tl; yb+=rln(t++) )
		        if( yb >= ybe )
			        return;
	        for( ; dp.ad>rlend(dp.tl,r); ++r,++yb );
	        yb = H * Math.Max( yb, -100 ); // 上にはみ出し過ぎないよう調整
	        if( yb >= cy() )
		        return;

	        // １行目を再描画
	        int rb = (r==0 ? 0 : rlend(dp.tl,r-1));
            string text = doc_.tl(dp.tl).Substring(rb).ToString();
	        //int xb = left() + Math.Max( 0,
		    //    //CalcLineWidth(doc_.tl(dp.tl)+rb, dp.ad-rb) -rlScr_.nPos );
            //    CalcLineWidth(text, dp.ad - rb) - hScrollBar.Value);
            int xb = left();
	        if( xb < right() )
	        {
                //TODO scroll
		        //Rectangle rc=new Rectangle(xb,yb,right(),yb+H);
                Rectangle rc = new Rectangle(xb, yb, right(), H);
                //Rectangle rc = new Rectangle(xb, 0, right(), yb + H);
		        //::InvalidateRect( hwnd_, &rc, FALSE );
                this.Invalidate(rc, false);
                //this.Invalidate(false);
	        }

	        // 残り
	        int ye;
	        yb += H;
	        if( afterall ){
		        xb=0;
                ye=cy();
            }else{
		        xb=left();
                ye=Math.Min(cy(),yb+(int)(H*(rln(dp.tl)-r-1)));
            }
	        if( yb < ye )
	        {
                Rectangle rc = new Rectangle( xb, yb, right(), ye );
		        //::InvalidateRect( hwnd_, &rc, FALSE );
                this.Invalidate(rc, false);
	        }
        }

        public void ScrollTo( VPos vp )
        {
	        // 横フォーカス
	        int dx=0;
	        if( vp.vx < hScrollBar.Value )
	        {
                dx = vp.vx - hScrollBar.Value;
	        }
	        else
	        {
		        int W = cvs_.getPainter().W();
                if (hScrollBar.Value + (hScrollBar.nPage - W) <= vp.vx)
                    dx = vp.vx - (hScrollBar.Value + hScrollBar.nPage) + W;
	        }

	        // 縦フォーカス
	        int dy=0;
            if (vp.vl < vScrollBar.Value)
                dy = vp.vl - vScrollBar.Value;
            else if (vScrollBar.Value + (vScrollBar.nPage - 1) <= vp.vl)
                dy = vp.vl - (vScrollBar.Value + vScrollBar.nPage) + 2;

	        // スクロール
	        if( dy!=0 )	
                //UpDown( dy, dx==0 ); //TODO scroll
                UpDown(dy, false);
	        if( dx!=0 )	ScrollView( dx, 0, true );
        }

        private void UpDown(int dy, bool thumb ){
          // １．udScr_.nPos + dy が正常範囲に収まるように補正
            if (vScrollBar.Value + dy < 0)
                dy = -vScrollBar.Value;
            else if (vScrollBar.Maximum + 1 - vScrollBar.nPage < vScrollBar.Value + dy)
                dy = vScrollBar.Maximum + 1 - vScrollBar.nPage - vScrollBar.Value;
	        if( dy==0 )
		        return;

          // ２－１．折り返し無しの場合は一気にジャンプ出来る
	        if( !wrapexists() )
	        {
                udScr_tl_ = vScrollBar.Value + dy;
	        }

          // ２－２．でなけりゃ、現在位置からの相対サーチ
          // ScrollBarを連続的にドラッグせず一度に一気に飛んだ場合は
          // 1行目や最終行からの相対サーチの方が有効な可能性があるが、
          // その場合は多少速度が遅くなっても描画が引っかかることはないのでＯＫ
	        else
	        {
		        int   rl = dy + udScr_vrl_;
		        int tl = udScr_tl_;

		        if( dy<0 ) // 上へ戻る場合
		        {
			        // ジャンプ先論理行の行頭へDash!
			        while( rl < 0 )
				        rl += rln(--tl);
		        }
		        else if( dy>0 ) // 下へ進む場合
		        {
			        // ジャンプ先論理行の行頭へDash!
			        while( rl > 0 )
				        rl -= rln(tl++);
			        if( rl < 0 )
				        rl += rln(--tl); //行き過ぎ修正
		        }
		        udScr_tl_ = tl;
		        udScr_vrl_= rl;
	        }

          // ４．画面をスクロール
	        ScrollView( 0, dy, !thumb );
        }

        public ReDrawType TextUpdate_ScrollBar( DPos s, DPos e, DPos e2 ){
            int prevUdMax = vScrollBar.Maximum;
	        bool rlScrolled = ReSetScrollInfo();
            int vl_dif = (vScrollBar.Maximum - prevUdMax);
	        ReDrawType ans =
                (vl_dif != 0 || s.tl != e2.tl ? ReDrawType.AFTER : ReDrawType.LINE);

	        if( udScr_tl_ < s.tl )
	        {
		        // パターン１：現在の画面上端より下で更新された場合
		        // スクロールしない
	        }
	        else if( udScr_tl_ == s.tl )
	        {
		        // パターン２：現在の画面上端と同じ行で更新された場合
		        // 出来るだけ同じ位置を表示し続けようと試みる。

		        if( vScrollBar.Value >= vln() )
		        {
			        // パターン2-1：しかしそこはすでにEOFよりも下だ！
			        // しゃーないので一番下の行を表示
                    vScrollBar.Value = vln() - 1;
			        udScr_tl_   = doc_.tln()-1;
			        udScr_vrl_  = rln(udScr_tl_)-1;
			        ans = ReDrawType.ALL;
		        }
		        else
		        {
			        // パターン2-2：
			        // スクロール無し
			        while( udScr_vrl_ >= rln(udScr_tl_) )
			        {
				        udScr_vrl_ -= rln(udScr_tl_);
				        udScr_tl_++;
			        }
		        }
	        }
	        else
	        {
		        // パターン３：現在の画面上端より上で更新された場合
		        // 表示内容を変えないように頑張る

		        if( e.tl < udScr_tl_ )
		        {
			        // パターン3-1：変更範囲の終端も、現在行より上の場合
			        // 行番号は変わるが表示内容は変わらないで済む
                    vScrollBar.Value += vl_dif;
			        udScr_tl_   += (e2.tl - e.tl);
                    ans = ReDrawType.LNAREA;
		        }
		        else
		        {
			        // パターン3-2：
			        // どうしよーもないので適当な位置にスクロール
			        ForceScrollTo( e2.tl );
                    ans = ReDrawType.ALL;
		        }
	        }

	        // どんな再描画をすればよいか返す
            return (rlScrolled ? ReDrawType.ALL : ans);
        }

        //
        private void GetDrawPosInfo(ref VDrawInfo v) {
            int H = cvs_.getPainter().H();

            int most_under = (vln() - vScrollBar.Value) * H;
            if (most_under <= v.rc.Top) {
                v.YMIN = v.rc.Top;
                v.YMAX = most_under;
            } else {
                int y = -udScr_vrl_;
                int tl = udScr_tl_;
                int top = v.rc.Top / H;
                while (y + rln(tl) <= top)
                    y += rln(tl++);

                // 縦座標
                v.YMIN = y * H;
                v.YMAX = Math.Min(v.rc.Bottom, most_under);
                v.TLMIN = tl;

                // 横座標
                v.XBASE = left() - hScrollBar.Value;
                //v.XBASE = left() - hScrollBar.Value/fnt().W();
                v.XMIN = v.rc.Left - v.XBASE;
                v.XMAX = v.rc.Right - v.XBASE; //TODO
                //v.XMAX = v.rc.Right - v.XBASE -vScrollBar.Width;

                // 選択範囲
                v.SXB = v.SXE = v.SYB = v.SYE = 0x7fffffff;

                VPos bg, ed;
                if (cur_.getCurPos(out bg, out ed)) {
                    v.SXB = bg.vx - hScrollBar.Value + left();
                    v.SXE = ed.vx - hScrollBar.Value + left();
                    v.SYB = (bg.vl - vScrollBar.Value) * H;
                    v.SYE = (ed.vl - vScrollBar.Value) * H;
                }
            }
        }
    }
}
