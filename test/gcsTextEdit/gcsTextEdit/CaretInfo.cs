using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;

namespace AsControls
{
    //
    public class DPos {
        public int ad { get; set; } //@{ バッファ中のアドレス (0～ ) //@}
        public int tl { get; set; } //@{ 論理行番号 (0～ ) //@}

        public DPos() { }
        public DPos(int ad, int tl) {
            this.ad = ad;
            this.tl = tl;
        }

        public static Boolean operator ==(DPos x, DPos y) {
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
        }
        
        public VPos() {
            ad = 0; tl = 0;
            vl = 0; rl = 0; vx = 0; rx = 0; 
        }
    }

    //
    public class Cursor {
        private VPos cur_;  // カーソル位置
        private VPos sel_;  // 選択時の軸足位置

        private Document doc_; 
        private gcsTextEdit view_;


        public void MoveCur(DPos dp, bool select) {
            VPos vp = new VPos();
            view_.ConvDPosToVPos(dp, ref vp);
            MoveTo(vp, select);
        }

        private void MoveTo(VPos vp, bool sel) {
            if (sel) {
                int te = sel_.tl;
            }
            else {
                sel_.Copy(cur_);
            }
            
            ScrollTo(cur);
            UpdateCaretPos();
        }

        private void Redraw(VPos s, VPos e ){
	        int x=0, y=0; // 原点
	        view_.GetOrigin( ref x, ref y );

            Point sp = new Point(x + s.vx, y + s.vl * view_.fnt.H);
            Point ep = new Point(x + e.vx, y + e.vl * view_.fnt.H);
	        if( s > e ){ // Swap
		        sp.X^=ep.X; ep.X^=sp.X; sp.X^=ep.X;
		        sp.X^=ep.Y; ep.Y^=sp.Y; sp.Y^=ep.Y;
            }
	        ep.X+=2;

	        //// 手抜き16bitチェック入り…
	        int LFT = view_.left();
            int RHT = view_.right();
            int TOP = 0;
	        int  BTM = view_.bottom();

	        if( sp.Y == ep.Y )
	        {
		        Rectangle rc = new Rectangle(Math.Max(LFT,sp.X), sp.Y, Math.Min(RHT,ep.X), sp.Y+view_.fnt.H);
                view_.Invalidate(rc, false);
		        //::InvalidateRect( caret_->hwnd(), &rc, FALSE );
	        }
	        else
	        {
                Rectangle rc = new Rectangle(Math.Max(LFT, sp.X), Math.Max(TOP, sp.Y), RHT, Math.Min(BTM, sp.Y + view_.fnt.H));
		        //::InvalidateRect( caret_->hwnd(), &rc, FALSE );
                view_.Invalidate(rc, false);
                Rectangle re = new Rectangle(LFT, Math.Max(TOP, ep.Y), Math.Min(RHT, ep.X), Math.Min(BTM, ep.Y + view_.fnt.H));
		        //::InvalidateRect( caret_->hwnd(), &re, FALSE );
                view_.Invalidate(re, false);
		        Rectangle rd = new Rectangle( LFT, Math.Max(TOP,rc.Bottom), RHT, Math.Min(BTM,re.Top) );
		        //::InvalidateRect( caret_->hwnd(), &rd, FALSE );
                view_.Invalidate(rd, false);
	        }
        }

        public void UpdateCaretPos() {
            int x, y;
            view_.GetOrigin(ref x, ref y);
            x += cur_.vx;
            y += cur_.vl * view_.fnt.H;

            // 行番号ゾーンにCaretがあっても困るので左に追いやる
            if (0 < x && x < view_.left())
                x = -view_.left();

            // セット
            caret_->SetPos(x, y);
            pEvHan_->on_move(cur_, sel_);
        }
    }

    public class CaretInfo
    {
        public int tl;
        public int ad;

        public int vl;
        public int rl;

        public int vx;
        public int rx;

        public CaretInfo()
        {
        }

        public CaretInfo(CaretInfo curpos)
        {
            tl = curpos.tl;
            ad = curpos.ad;
            vl = curpos.vl;
            rl = curpos.rl;
            vx = curpos.vx;
            rx = curpos.rx;
        }

        public void CopyTo(ref CaretInfo dist)
        {
            dist.tl=tl;  
            dist.ad=ad;  
            dist.vl=vl;  
            dist.rl=rl;  
            dist.vx=vx;  
            dist.rx=rx; 
        }

        public static Boolean operator ==(CaretInfo s, CaretInfo e)
        {
            return (s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl && s.vx == e.vx && s.rx == e.rx);
            //return (s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl);
        }

        public static Boolean operator !=(CaretInfo s, CaretInfo e)
        {
            return !(s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl && s.vx == e.vx && s.rx == e.rx);
            //return !(s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl);
        }

        public static Boolean operator <(CaretInfo s, CaretInfo e)
        {
            return (s.tl < e.tl || (s.tl == e.tl && s.ad < e.ad));
        }

        public static Boolean operator >(CaretInfo s, CaretInfo e)
        {
            return (s.tl > e.tl || (s.tl == e.tl && s.ad > e.ad));
        }

        public static Boolean operator <=(CaretInfo s, CaretInfo e)
        {
            return (s.tl < e.tl || (s.tl == e.tl && s.ad <= e.ad));
        }

        public static Boolean operator >=(CaretInfo s, CaretInfo e)
        {
            return (s.tl > e.tl || (s.tl == e.tl && s.ad >= e.ad));
        }

        //public override bool Equals(object obj) {
        //    CaretInfo caret = obj as CaretInfo;
        //    if (caret == null) return false;

        //    return (caret.ad == ad && caret.rl == rl && caret.rx == rx && caret.tl == tl && caret.vl == vl && caret.vx == vx);
        //}
    }
}
