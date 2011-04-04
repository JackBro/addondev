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
            if (Clipboard.ContainsFileDropList()) {
                var text = string.Empty;
                var files = Clipboard.GetFileDropList();
                foreach (string fileName in files) {
                    text += fileName + Environment.NewLine;
                }
                form.Editor.Document.Replace(text);
            }else if (Clipboard.ContainsImage()) {
                //var image = Clipboard.GetImage();
                //image.Save("0.png", ImageFormat.Png);
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
            f.Show();
        };

        //cat
        public static Action<MainForm> DeleteFile = (form) => {
            var items = form.CategoryListView.SelectedItems;
            for (int i = 0; i < items.Count; i++) {
                if (items[i].Name != Category.Trust) {
                    form.DeleteFile(items[i].Name);
                }
            }
        };
    }
}
