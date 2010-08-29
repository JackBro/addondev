using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AsControls {
    public class HGcsScrollBar : HScrollBar {
        public int nPage {
            get;
            set;
        }
    }

    public class VGcsScrollBar : VScrollBar {
        public int nPage {
            get;
            set;
        }
    }
}
