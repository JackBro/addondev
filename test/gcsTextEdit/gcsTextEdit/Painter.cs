using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls {
    class Painter {

        private IntPtr dc_;
        //private Font font_;
        
        private int height_;
        public int H { get { return height_; } }

        public Painter(IntPtr hdc, Config config) {

            height_ = (int)(config.Font.GetHeight() + 0.5f);
        }
    }
}
