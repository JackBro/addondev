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

    public class Canvas {

        private Rectangle txtZone_;
        private Painter font_; // 描画用オブジェクト

        //@{ [行番号を表示するか否か] //@}
	    public bool showLN { get; set; }
        public WrapType wrapType { get; set; }

        //@{ 表示領域の位置(pixel) //@}
        public Rectangle zone { get { return txtZone_; } }



        public Canvas(gcsTextEdit view, Config config) {
            txtZone_ = view.ClientRectangle;
            showLN = true;
            wrapType = WrapType.Non;
            txtZone_ = view.ClientRectangle;
            font_ = new Painter(Win32API.GetDC(view.Handle), config);
        }

        public Painter getPainter() {
            return font_;
        }

        
    }
}
