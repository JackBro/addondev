using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls {
    public class Config {
        private Font font;

        public Font Font { get { return font; } }

        public void setFontInfo(string fontname, float fontsize) {
            if (font != null) {
                font.Dispose();
                font = null;
            }
            font = new Font(fontname, fontsize);
        }
        public void setFont(Font font) {
            if (this.font != null) {
                this.font.Dispose();
                this.font = null;
            }
            this.font = font;
        }

        public Config() {

        }
    }
}
