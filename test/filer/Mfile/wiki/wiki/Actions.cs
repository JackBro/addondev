using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Drawing.Imaging;
using wiki.control;

namespace wiki {
    
    class Actions {
        public static Action<MainForm> NewItem = (form) => {
            form.CreateItem();
        };
        public static Action<MainForm> Cut = (form) => {
            form.Editor.Cut();
        };
        public static Action<MainForm> Copy = (form) => {
            form.Editor.Copy();
        };
        public static Action<MainForm> Paste = (form) => {
            if (Clipboard.ContainsImage()) {
                var image = Clipboard.GetImage();
                image.Save("0.png", ImageFormat.Png);
            } else if (Clipboard.ContainsText()) {
                form.Editor.Paste();
            }
        };

        public static Action<MainForm> Undo = (form) => {
            form.Editor.Undo();
        };
        public static Action<MainForm> Redo = (form) => {
            form.Editor.Redo();
        };

        public static Action<MainForm> Comple = (form) => {
            var f = new CompleForm(form);
            //var f = new CompleWindow(form);
            f.Show();
        };
    }
}
