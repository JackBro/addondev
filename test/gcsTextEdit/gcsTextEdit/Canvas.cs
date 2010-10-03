using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls {

    public enum WrapType {
        NonWrap,
        WindowWidth
    }

    internal class Canvas : IDisposable {

        //private Rectangle txtZone_;
        private Win32API.RECT txtZone_;

        /// <summary>
        /// 描画用オブジェクト
        /// </summary>
        private Painter font_;
 
        /// <summary>
        /// 折り返し幅(pixel)
        /// </summary>
        private int wrapWidth_;

        /// <summary>
        /// 行番号を表示するか否か
        /// </summary>
	    public bool showLN { get; set; }
        public WrapType wrapType { get; set; }

        /// <summary>
        /// 表示領域の位置(pixel)
        /// </summary>
        /// <returns></returns>
        //public Rectangle zone() { return txtZone_; }
        public Win32API.RECT zone() { return txtZone_; }

        /// <summary>
        /// 折り返し幅(pixel) 
        /// </summary>
        /// <returns></returns>
	    public int wrapWidth() { return wrapWidth_; }

        /// <summary>
        /// 行番号の桁数
        /// </summary>
        int figNum_;

        public Canvas(gcsTextEdit view, Font font, bool showLN) {
            txtZone_ = view.getClientRect();
            font_ = new Painter(view.Handle, font);
            this.showLN = showLN;

            figNum_ = 3;
            wrapWidth_ = 0xfffffff;
            wrapType = WrapType.NonWrap;
        }

        public Painter getPainter() {
            return font_;
        }

        public bool on_tln_change( int tln )
        {
	        figNum_ = Log10( tln ); // 桁数計算

	        if( CalcLNAreaWidth() )
	        {
                if (wrapType == WrapType.WindowWidth)
			        CalcWrapWidth();
		        return true;
	        }
	        return false;
        }

        bool CalcLNAreaWidth(){
            int prev = txtZone_.left;
            if (showLN){
                txtZone_.left = (1 + figNum_) * font_.F();
                if (txtZone_.left + font_.W() >= txtZone_.right) {
                    txtZone_.left = 0; // 行番号ゾーンがデカすぎるときは表示しない
               }
	        }else{
                txtZone_.left = 0;
	        }

            return (prev != txtZone_.left);
        }

        void CalcWrapWidth(){
	        switch( wrapType ){
	        case WrapType.NonWrap:
                wrapWidth_ = 0xfffffff;
		        break;
            case WrapType.WindowWidth:
                wrapWidth_ = txtZone_.right - txtZone_.left - 3;
		        break; //Caretの分-3補正
            default:
            //    wrapWidth_ = wrapType_ * font_->W();
                break;
	        }
        }
        
        static int Log10( int n ){
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

        public bool on_view_resize( int cx, int cy ){
            //TODO wrap
	        txtZone_.right  = cx;
	        txtZone_.bottom = cy;
            //txtZone_ = new Rectangle(txtZone_.Left, txtZone_.Top, cx - txtZone_.Left, cy - txtZone_.Top);
            //txtZone_ = new Rectangle(txtZone_.Left, txtZone_.Top, cx - 18, cy - 18);

	        CalcLNAreaWidth();
	        if( wrapType == WrapType.WindowWidth )
	        {
		        CalcWrapWidth();
		        return true;
	        }
	        return false;
        }

        //public void on_font_change( const VConfig& vc ){
        //    HDC dc = font_->getDC();
        //    font_ = NULL; // 先にデストラクタを呼ばねばならない…
        //                  // ってうわー格好悪ぃーーー(T_T)
        //    font_ = new Painter( dc, vc );

        //    CalcLNAreaWidth();
        //    CalcWrapWidth();
        //}

        public void on_config_change(WrapType wrap, bool showln) {
	        showLN = showln;
            wrapType = wrap;

	        CalcLNAreaWidth();
	        CalcWrapWidth();
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
