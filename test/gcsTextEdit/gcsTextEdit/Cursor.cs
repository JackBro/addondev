﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;

namespace AsControls
{
    public class Caret {
        private IntPtr hwnd_;
        private bool created_;

        public bool isAlive() {
            return created_;
        }

        public IntPtr hwnd {
            get { return hwnd; }
        }

        public Caret(IntPtr hwnd) {
            hwnd_ = hwnd;
        }

        public void Show() {
            if( created_ ) Win32API.ShowCaret( hwnd_ ); 
        }
        public void Hide() {
            if (created_) Win32API.HideCaret(hwnd_);
        }
        public void Destroy() {
            if (created_) {
                Win32API.DestroyCaret();
                created_ = false;
            }
        }
        public void SetPos(int x, int y) {
            if (created_) {
                Win32API.SetCaretPos(x, y);
                //
            }
        }
        public void Create( int H, int W){
            if( created_ ) Win32API.DestroyCaret();
			created_ = true;
            Win32API.CreateCaret(hwnd_, IntPtr.Zero, W, H);
			//ime().SetFont( hwnd_, lf );
			Show();
        }
    }

    //
    public class DPos {
        public int ad { get; set; } //@{ バッファ中のアドレス (0～ ) //@}
        public int tl { get; set; } //@{ 論理行番号 (0～ ) //@}

        public DPos() { }
        public DPos( int tl, int ad) {
            this.ad = ad;
            this.tl = tl;
        }

        public override bool Equals(object obj) {
            return base.Equals(obj);
        }

        public static Boolean operator ==(DPos x, DPos y) {
            //if (x == null && y == null) return true;

            return (x.tl == y.tl && x.ad == y.ad);
        }

        public static Boolean operator !=(DPos x, DPos y) {
            return (x.tl != x.tl || x.ad != y.ad);
        }

        public static Boolean operator <(DPos x, DPos y) {
            return (x.tl < y.tl || (x.tl == y.tl && x.ad < y.ad));
        }

        public static Boolean operator >(DPos x, DPos y) {
            return (x.tl > y.tl || (x.tl == y.tl && x.ad > y.ad));
        }

        public static Boolean operator <=(DPos x, DPos y) {
            return (x.tl < y.tl || (x.tl == y.tl && x.ad <= y.ad));
        }

        public static Boolean operator >=(DPos x, DPos y) {
            return (x.tl > y.tl || (x.tl == y.tl && x.ad >= y.ad));
        }
    }

    //
    public class VPos : DPos {
        public int vl { get; set; } // VLine-Index
        public int rl { get; set; } // RLine-Index
        public int vx { get; set; } // スクロールを考慮しない仮想スクリーン上のx座標(pixel) 
        public int rx { get; set; } // 文字の並びに左右されてないx座標(pixel)
                                    //   == 長い行のしっぽから短い行に [↑] で移動して
                                    //   == その後 [↓] で戻れるようなアレです。

        public void Copy(VPos src) {
            tl = src.tl;
            ad = src.ad;
            vl = src.vl;
            rl = src.rl;
            vx = src.vx;
            rx = src.rx;
        }
        
        public VPos() {
            ad = 0; tl = 0;
            vl = 0; rl = 0; vx = 0; rx = 0; 
        }

        public VPos(VPos vp) {
            ad = vp.ad; tl = vp.tl;
            vl = vp.vl; rl = vp.rl; vx = vp.vx; rx = vp.rx;
        }
    }

    //
    public class Cursor {
        private enum State {
            none,
            mouse_down
        }

        private State state;

        private Document doc_; 
        private gcsTextEdit view_;
        private Caret caret_;

        private VPos cur_;  // カーソル位置
        private VPos sel_;  // 選択時の軸足位置
        private bool bIns_; // 挿入モード？

        private bool lineSelectMode_; // 行選択モード？
        private int dragX_, dragY_;

        public bool isSelectText() {
            return !(cur_ == sel_);
        }

        public Cursor(gcsTextEdit view_, Document doc_, Caret caret_) {
            this.view_ = view_;
            this.doc_ = doc_;
            this.caret_ = caret_;

            cur_ = new VPos();
            sel_ = new VPos();

            bIns_ = true;

            state = State.none;

            this.doc_.TextUpdateEvent += (s, e, e2) => {
                this.on_text_update(s, e, e2, true);
            };

            this.view_.PreviewKeyDown += (sender, e) => {

            };
            this.view_.MouseDown += (sender, e) => {
                state = State.mouse_down;
            };
            this.view_.MouseUp += (sender, e) => {
                state = State.none;
            };
            this.view_.MouseMove += (sender, e) => {
                if (state == State.mouse_down) {
                   
                    //if (!PtInRect(&view_.zone(), pt)) {
                    if ( view_.zone().Contains(e.X, e.Y)){
                        MoveByMouse(e.X, e.Y);
                    }
                }
            };
            this.view_.MouseCaptureChanged += (sender, e) => {
                //state = State.none;
            };
            this.view_.MouseLeave += (sender, e) => {
                
            };
        }

        public void MoveCur(DPos dp, bool select) {
            VPos vp = new VPos();
            view_.ConvDPosToVPos(dp, ref vp);
            MoveTo(vp, select);
        }

        private void MoveTo(VPos vp, bool sel) {
            if (sel) {
                // 選択状態が変わる範囲を再描画
                Redraw(vp, cur_);
            }
            else {
                // 選択解除される範囲を再描画
                if (cur_ != sel_)
                    Redraw(cur_, sel_);
                sel_.Copy(cur_);
            }

            //TODO
            //if (cur_.Equals(vp)) {
            //    int i = 0; 
            //}

            cur_.Copy(vp);
            UpdateCaretPos();
            view_.ScrollTo(cur_);
        }

        private void Redraw(VPos s, VPos e ){
	        int x=0, y=0; // 原点
	        view_.GetOrigin( ref x, ref y );

            Point sp = new Point(x + s.vx, y + s.vl * view_.fnt().H());
            Point ep = new Point(x + e.vx, y + e.vl * view_.fnt().H());
	        if( s > e ){ // Swap
		        //sp.X^=ep.X; ep.X^=sp.X; sp.X^=ep.X;
		        //sp.X^=ep.Y; ep.Y^=sp.Y; sp.Y^=ep.Y;

                int tmp = ep.X;
                ep.X = sp.X;
                sp.X = tmp;
                
                tmp = ep.Y;
                ep.Y = sp.Y;
                sp.Y = tmp;               
            }
	        //ep.X+=2;

	        //// 手抜き16bitチェック入り…
	        int LFT = view_.left();
            int RHT = view_.right();
            int TOP = 0;
	        int  BTM = view_.bottom();

	        if( sp.Y == ep.Y )
	        {
		        //Rectangle rc = new Rectangle(Math.Max(LFT,sp.X), sp.Y, Math.Min(RHT,ep.X), sp.Y+view_.fnt().H());
                Rectangle rc = new Rectangle(Math.Max(LFT, sp.X), sp.Y, Math.Min(RHT, ep.X - sp.X), view_.fnt().H());
                view_.Invalidate(rc, false);
		        //::InvalidateRect( caret_->hwnd(), &rc, FALSE );
	        }
	        else
	        {
                //view_.Invalidate();

                //Rectangle rc = new Rectangle(Math.Max(LFT, sp.X), Math.Max(TOP, sp.Y), RHT, Math.Min(BTM, sp.Y + view_.fnt().H()));
                Rectangle rc = new Rectangle(LFT, Math.Max(TOP, sp.Y), RHT, Math.Min(BTM, sp.Y + view_.fnt().H()));
                //::InvalidateRect( caret_->hwnd(), &rc, FALSE );
                view_.Invalidate(rc, false);
                Rectangle re = new Rectangle(LFT, Math.Max(TOP, ep.Y), Math.Min(RHT, ep.X), Math.Min(BTM, ep.Y + view_.fnt().H()));
                //::InvalidateRect( caret_->hwnd(), &re, FALSE );
                view_.Invalidate(re, false);
                Rectangle rd = new Rectangle(LFT, Math.Max(TOP, rc.Bottom), RHT, Math.Min(BTM, re.Top));
                //::InvalidateRect( caret_->hwnd(), &rd, FALSE );
                view_.Invalidate(rd, false);
	        }
        }

        public bool getCurPos( out VPos start, out VPos end) {
            //*start = *end = &cur_;
            start = new VPos();
            end = new VPos();
            start.Copy(cur_);
            end.Copy(cur_);

            if (cur_ == sel_)//|| !caret_->isAlive() )
                return false;
            if (cur_ < sel_) {
                //*end = &sel_;
                end.Copy(sel_);
            } else {
                //*start = &sel_;
                start.Copy(sel_);
            }
            return true;
        }

        public void UpdateCaretPos() {
            int x=0, y=0;
            view_.GetOrigin(ref x, ref y);
            x += cur_.vx;
            y += cur_.vl * view_.fnt().H();

            // 行番号ゾーンにCaretがあっても困るので左に追いやる
            if (0 < x && x < view_.left())
                x = -view_.left();

            // セット
            caret_.SetPos(x, y);
            //pEvHan_->on_move(cur_, sel_);
        }

        public void Ud(int dy, Boolean select) {
            // はみ出す場合は、先頭行/終端行で止まるように制限
            VPos np = new VPos(cur_);
            //int viewvln = vlNum_;
            if (np.vl + dy < 0)
                dy = -np.vl;
            else if (np.vl + dy >= view_.vln())
                dy = view_.vln() - np.vl - 1;

            np.vl += dy;
            np.rl += dy;

            if (dy < 0) { // 上へ戻る場合
                // ジャンプ先論理行の行頭へDash!
                while (np.rl < 0)
                    np.rl += view_.rln(--np.tl);

            } else if (dy > 0) {  // 下へ進む場合
                // ジャンプ先論理行の行頭へDash!
                while (np.rl > 0)
                    np.rl -= view_.rln(np.tl++);
                if (np.rl < 0)
                    np.rl += view_.rln(--np.tl); //行き過ぎ修正
            }

            // x座標決定にかかる
            IBuffer str = doc_.tl(np.tl);

            // 右寄せになってる。不自然？
            np.ad = (np.rl == 0 ? 0 : view_.rlend(np.tl, np.rl - 1) + 1);

            np.vx = (np.rl == 0 ? 0 : view_.fnt().CalcStringWidth(str.Substring(np.ad - 1, 1).ToString())); //TODO

            //int wrapindex = wrapList[np.tl].wrap[np.rl];

            while (np.vx < np.rx && np.ad < view_.rlend(np.tl, np.rl)) {
                //int newvx = np.vx + view_.CalcStringWidth(doc.LineList[np.tl].Text, np.ad, 1);
                //if (newvx > np.rx)
                //    break;
                //np.vx = newvx;
                //++np.ad;

                // 左寄せにしてみた。
		        int newvx;
		        //if( str[np.ad] == '\t')
			    //    newvx = view_.fnt().nextTab(np.vx);
		        //else
			    //    newvx = np.vx + view_.fnt().W(&str[np.ad]);
                 newvx = np.vx + view_.fnt().CalcStringWidth(str.Substring(np.ad, 1).ToString());//TODO
		        if(newvx > np.rx)
			        break;
		        np.vx = newvx;
		        ++np.ad;
            }

            MoveTo(np, select);
        }

        public void Home(bool wide, bool select) {
            VPos np = new VPos();
	        np.ad = np.vx = np.rx = np.rl = 0;
	        if( wide ) // 文書の頭へ
		        np.tl = np.vl = 0;
	        else // 行の頭へ
	        {
		        // 1.07.4 --> 1.08 :: Virtual Home
		        // np.tl = cur_.tl, np.vl = cur_.vl-cur_.rl;

		        if( cur_.rl == 0 ){
                    np.tl = cur_.tl;
                    np.vl = cur_.vl - cur_.rl;
                } else{
			        //view_.ConvDPosToVPos( doc_.rightOf(DPos(cur_.tl, view_.rlend(cur_.tl,cur_.rl-1))), &np, &cur_ );
                    view_.ConvDPosToVPos(doc_.rightOf(new DPos(cur_.tl, view_.rlend(cur_.tl, cur_.rl - 1)), false), ref np, ref cur_);
                }
	        }
	        MoveTo( np, select );
        }

        public void End(bool wide, bool select) {
            VPos np = new VPos();
            if (wide) { // 文書の末尾へ
                np.tl = doc_.tln() - 1;
                np.vl = view_.vln() - 1;
            } else { // 行の末尾へ
	
                // 1.07.4 --> 1.08 :: Virtual End
                // np.tl = cur_.tl;
                // np.vl = cur_.vl + view_.rln(np.tl) - 1 - cur_.rl;

                view_.ConvDPosToVPos(new DPos(cur_.tl, view_.rlend(cur_.tl, cur_.rl)), ref np, ref cur_);
                MoveTo(np, select);
                return;
            }
            np.ad = doc_.len(np.tl);
            np.rl = view_.rln(np.tl) - 1;
            np.rx = np.vx = view_.GetLastWidth(np.tl);

            MoveTo(np, select);
        }

        public void Up(bool wide, bool select) {
            Ud(wide ? -3 : -1, select);
        }

        public void Down( bool wide, bool select )
        {
	        Ud( wide?3:1, select );
        }

        public void PageUp( bool select )
        {
	        Ud( -view_.cy()/view_.fnt().H(), select );
        }

        public void PageDown( bool select )
        {
	        Ud( view_.cy()/view_.fnt().H(), select );
        }

        public void Left( bool wide, bool select )
        {
            VPos np = new VPos();
	        if( cur_!=sel_ && !select ){
                //np = Math.Min(cur_, sel_);
                //np = cur_ > sel_ ? sel_ : cur_;
                np.Copy(Util.Min(cur_, sel_));
                np.rx = np.vx;
	        }else{
		        view_.ConvDPosToVPos( doc_.leftOf(cur_,wide), ref np, ref cur_ );
            }
	        MoveTo( np, select );
        }

        public void Right( bool wide, bool select )
        {
            VPos np = new VPos();
	        if( cur_!=sel_ && !select ){
                //np = Max(cur_, sel_);
                //np = cur_ < sel_ ? sel_ : cur_;
                np.Copy(Util.Max(cur_, sel_));
                np.rx = np.vx;
	        }else{
                //np = new VPos();
		        view_.ConvDPosToVPos( doc_.rightOf(cur_,wide), ref np, ref cur_ );
            }
	        MoveTo( np, select );
        }

        public void InputChar(char ch) {
            // 「上書モード ＆ 選択状態でない ＆ 行末でない」なら右一文字選択
            if (!bIns_ && cur_ == sel_ && doc_.len(cur_.tl) != cur_.ad)
            //if (cur_ == sel_ && doc_.len(cur_.tl) != cur_.ad)
                Right(false, true);

            // 入力
            Input(ch, 1);
        }
        public void Input( string str, int len )
        {
            if (cur_ == sel_)
                //doc_.Execute( Insert( cur_, str, len ) );
                doc_.Insert(cur_, sel_, str);
            else
                //doc_.Execute(Replace(cur_, sel_, str, len));
                doc_.Replace(cur_, sel_, str);
        }

        public void Input( char str, int len )
        {
            //unicode* ustr = new unicode[ len*4 ];
            //len = ::MultiByteToWideChar( CP_ACP, 0, str, len, ustr, len*4 );
            //Input( ustr, len );
            //delete [] ustr;
            Input(str.ToString(), 1);
        }



        //-------------------------------------------------------------------------
        // Viewからの指令を処理
        //-------------------------------------------------------------------------

        public void on_setfocus()
        {
            caret_.Create(view_.fnt().H(),
                //(bIns_ ? 2 : view_.fnt().W()), view_.fnt().LogFont() );
                2);
	        UpdateCaretPos();
        }

        public void on_killfocus()
        {
	        caret_.Destroy();
	        Redraw( cur_, sel_ );
        }

        public void on_scroll_begin()
        {
	        caret_.Hide();
        }

        public void on_scroll_end()
        {
	        UpdateCaretPos();
	        caret_.Show();
        }

        public void on_lbutton_down( int x, int y, bool shift )
        {
	        if( !shift )
	        {
		        // これまでの選択範囲をクリア
                //if(cur_ != sel_)
		            Redraw( cur_, sel_ );

		        // 行番号ゾーンのクリックだったら、行選択モードに
		        lineSelectMode_ = ( x < view_.lna()-view_.fnt().F() );

		        // 選択開始位置を調整
		        view_.GetVPos( x, y, ref sel_ , false);
		        if( lineSelectMode_ )
			        view_.ConvDPosToVPos( new DPos(sel_.tl,0), ref sel_, ref sel_ );
		        //cur_ = sel_;
                cur_.Copy(sel_);
	        }

	        // 移動！
            dragX_ = x;
            dragY_ = y;
	        MoveByMouse( x, y );

	        //// マウス位置の追跡開始
	        //timerID_ = ::SetTimer( caret_->hwnd(), 178116, keyRepTime_, NULL );
	        //::SetCapture( caret_->hwnd() );
        }

        private void MoveByMouse( int x, int y )
        {
	        VPos vp = new VPos();
	        view_.GetVPos( x, y, ref vp, lineSelectMode_ );
	        MoveTo( vp, true );
        }

        public void ResetPos()
        {
	        // 設定変更などに対応
	        view_.ConvDPosToVPos( cur_, ref cur_ );
	        view_.ConvDPosToVPos( sel_, ref sel_ );
	        UpdateCaretPos();
	        if( caret_.isAlive())
		        view_.ScrollTo( cur_ );
        }

        public void on_text_update(DPos s, DPos e, DPos e2, bool mCur) {
            VPos search_base  = new VPos();
            search_base.ad = -1;

	        if( mCur && s==cur_ && e==sel_ )
	        {
		        //search_base = new VPos(cur_);
                search_base = cur_;

	        }
	        else if( mCur && s==sel_ && e==cur_ )
	        {
		        //search_base =  new VPos(sel_);
                search_base = sel_;
	        }
	        else
	        {
		        Redraw( cur_, sel_ );
		        if( mCur && caret_.isAlive() )
		        {
			        if( cur_ <= s ){
				        //search_base = &cur_;
                        search_base.Copy(cur_);
                    }
		        }
		        else
		        {
			        if( s < cur_ )
			        {
                        if (cur_ <= e) {
                            //cur_ = e2 as VPos;
                            VPos ve2 = e2 as VPos;
                            cur_.Copy(ve2);
                        } else if (cur_.tl == e.tl) {
                            cur_.tl = e2.tl;
                            cur_.ad = e2.ad + cur_.ad - e.ad;
                        } else
                            cur_.tl = e2.tl - e.tl;
				        view_.ConvDPosToVPos( cur_, ref cur_ );
			        }
                    if (s < sel_)
                        //sel_ = cur_;
                        sel_.Copy(cur_);
		        }
	        }

	        if( mCur )
	        {
		        view_.ConvDPosToVPos( e2, ref cur_, ref search_base );
		        //sel_ = cur_;
                sel_.Copy(cur_);
		        if( caret_.isAlive() )
			        view_.ScrollTo( cur_ );
	        }
	        UpdateCaretPos();
        }

        //

    }


}
