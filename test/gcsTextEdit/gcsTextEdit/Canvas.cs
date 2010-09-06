using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls {

    public enum WrapType {
        Non,
        Window
    }

    internal class Canvas : IDisposable {

        private Rectangle txtZone_;
        private Painter font_; // 描画用オブジェクト
        private int wrapWidth_; // 折り返し幅(pixel)

        //@{ [行番号を表示するか否か] //@}
	    public bool showLN { get; set; }
        public WrapType wrapType { get; set; }

        //@{ 表示領域の位置(pixel) //@}
        public Rectangle zone() { return txtZone_; }

        //@{ 折り返し幅(pixel) //@}
	    public int wrapWidth() { return wrapWidth_; }

        int figNum_; // 行番号の桁数
        private gcsTextEdit view;
        public Canvas(gcsTextEdit view, Config config) {
            txtZone_ = view.getClientRect();
            //txtZone_ = new Rectangle();
            showLN = true;
            wrapType = WrapType.Non;
            wrapType = WrapType.Window;
            //txtZone_ = view.ClientRectangle;
            font_ = new Painter(view.Handle, config);
            wrapWidth_ = 0xfffffff;
            //wrapWidth_ = view.cx() - 3; //TODO
            //wrapWidth_ = 100;
            this.view = view;
            //TODO wrap
            //view.SizeChanged += (sender, e) => {
            //};

            //view.VisibleChanged += new EventHandler(view_VisibleChanged);
        }

        //void view_VisibleChanged(object sender, EventArgs e) {
        //    if (view.Visible) {
        //        txtZone_ = view.getClientRect();
        //        view.VisibleChanged -= view_VisibleChanged;
        //    }
        //}

        public Painter getPainter() {
            return font_;
        }

        public bool on_tln_change( int tln )
        {
	        figNum_ = Log10( tln ); // 桁数計算

	        if( CalcLNAreaWidth() )
	        {
                if (wrapType == WrapType.Window)
			        CalcWrapWidth();
		        return true;
	        }
	        return false;
        }

        bool CalcLNAreaWidth()
        {
	        int prev = txtZone_.Left;
            if (showLN)
	        {
                //txtZone_.Left = (1 + figNum_) * font_.F();
                //if (txtZone_.Left + font_.W() >= txtZone_.Right)
                //    txtZone_.Left = 0; // 行番号ゾーンがデカすぎるときは表示しない
                if (figNum_ == 0) figNum_ = 2; //TODO
               int left = (1 + figNum_) * font_.F();
               if (left + font_.W() >= txtZone_.Right) {
                   left = 0; // 行番号ゾーンがデカすぎるときは表示しない
                   //txtZone_ = new Rectangle(new Point(left, txtZone_.Top), new Size(view.cx(), txtZone_.Size.Height));
               }

               if (left != txtZone_.Left)
                   txtZone_ = new Rectangle(new Point(left, txtZone_.Top), txtZone_.Size);
                   //TODO wrap
                   //txtZone_ = new Rectangle(new Point(left, txtZone_.Top), new Size(view.cx() - 3 , txtZone_.Size.Height));
	        }
	        else
	        {
                //txtZone_.Left = 0;
                txtZone_ = new Rectangle(new Point(0, txtZone_.Top), txtZone_.Size);
	        }

            return (prev != txtZone_.Left);
        }

        void CalcWrapWidth()
        {
	        switch( wrapType )
	        {
	        case WrapType.Non:
                    wrapWidth_ = 0xfffffff;
		        break;
            case WrapType.Window:
                wrapWidth_ = txtZone_.Right - txtZone_.Left - 3; //TODO wrap
                //wrapWidth_ = view.cx()-3; //TODO
		        break; //Caretの分-3補正
            default:
            //    wrapWidth_ = wrapType_ * font_->W();
                break;
	        }
        }
        
        static int Log10( int n )
	    {
		    int[] power_of_ten =
			    { 1, 10, 100, 1000, 10000, 100000, 1000000,
			      10000000, 100000000, 1000000000 }; // 10^0 ～ 10^9
		    int c=3;
		    if( power_of_ten[9] <= n )
			    c=10;
		    else
			    while( power_of_ten[c] <= n )
				    c++;
		    return c; // 3<=c<=10 s.t. 10^(c-1) <= n < 10^c
	    }

        public bool on_view_resize( int cx, int cy )
        {
            //TODO wrap
	        //txtZone_.right  = cx;
	        //txtZone_.bottom = cy;
            txtZone_ = new Rectangle(txtZone_.Left, txtZone_.Top, cx - txtZone_.Left, cy - txtZone_.Top);
            //txtZone_ = new Rectangle(txtZone_.Left, txtZone_.Top, cx - 18, cy - 18);

	        CalcLNAreaWidth();
	        if( wrapType == WrapType.Window )
	        {
		        CalcWrapWidth();
		        return true;
	        }
	        return false;
        }

        #region IDisposable メンバ

        public void Dispose() {
            if (font_ != null) {
                font_.Dispose();
            }
        }

        #endregion
    }
}
