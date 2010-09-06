using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AsControls {
    internal class HGcsScrollBar : HScrollBar {
        public int nPage {
            get;
            set;
        }
    }

    internal class VGcsScrollBar : VScrollBar {
        public int nPage {
            get;
            set;
        }
    }
}
