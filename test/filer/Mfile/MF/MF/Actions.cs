using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MF {
    class Actions {
        public static Action<MainForm> Copy = (form) => {
            if (form.activeUs != null) {
                form.activeUs.Copy();
            }
        };

        public static Action<MainForm> Cut = (form) => {
            if (form.activeUs != null) {
                form.activeUs.Cut();
            }
        };

        public static Action<MainForm> Paste = (form) => {
            if (form.activeUs != null) {
                form.activeUs.Paste();
            }
        };

        public static Action<MainForm> Delete = (form) => {
            if (form.activeUs != null) {
                form.activeUs.Delete();
            }
        };

        public static Action<MainForm> UpDir = (form) => {
            if (form.activeUs != null) {
                form.activeUs.UpDir();
            }
        };

        public static Action<MainForm> Reload = (form) => {
            if (form.activeUs != null) {
                form.activeUs.Reload();
            }
        };
    }
}
