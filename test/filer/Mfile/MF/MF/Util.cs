using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using IWshRuntimeLibrary;

namespace MF {
    class Util {
        public static string getShortcutPath(string pathlink) {
            IWshShell shell = new WshShell();
            IWshShortcut shortcut = (IWshShortcut)shell.CreateShortcut(pathlink);
            return (shortcut.TargetPath);
        }
    }
}
