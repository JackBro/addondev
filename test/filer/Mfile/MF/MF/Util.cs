using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using IWshRuntimeLibrary;
using System.Windows.Forms;

namespace MF {
    class Util {
        public static string getShortcutPath(string pathlink) {
            IWshShell shell = new WshShell();
            IWshShortcut shortcut = (IWshShortcut)shell.CreateShortcut(pathlink);
            return (shortcut.TargetPath);
        }

        public static void ShellFileCopy(IntPtr hwnd, string[] files, string toDir) {
            ShellOperation(hwnd, files, toDir, Win32API.FOFunc.FO_COPY);
        }

        public static void ShellFileMove(IntPtr hwnd, string[] files, string toDir) {
            ShellOperation(hwnd, files, toDir, Win32API.FOFunc.FO_MOVE);
        }

        public static void ShellFileDelete(IntPtr hwnd, string[] files) {
            MF.Win32API.SHFILEOPSTRUCT shellOpStruct = new MF.Win32API.SHFILEOPSTRUCT();
            shellOpStruct.hwnd = hwnd;
            shellOpStruct.wFunc = Win32API.FOFunc.FO_DELETE;
            //var form = string.Join(@"\0", files) + '\0' + '\0';
            var form = string.Empty;
            foreach (var f in files) {
                form +=  f + '\0';
            }
            form += '\0';
            shellOpStruct.pFrom = form;
            shellOpStruct.pTo = null;
            shellOpStruct.fFlags = Win32API.FOFlags.FOF_ALLOWUNDO;
            shellOpStruct.fAnyOperationsAborted = false;
            shellOpStruct.hNameMappings = IntPtr.Zero;
            MF.Win32API.SHFileOperation(ref shellOpStruct);
        }

        static void ShellOperation(IntPtr hwnd, string[] files, string toDir, MF.Win32API.FOFunc func) {
            MF.Win32API.SHFILEOPSTRUCT shellOpStruct = new MF.Win32API.SHFILEOPSTRUCT();
            shellOpStruct.hwnd = hwnd;
            shellOpStruct.wFunc = func;
            //var form = string.Join(@"\0", files) + @"\0" + @"\0";
            var form = string.Empty;
            foreach (var f in files) {
                form += f + '\0';
            }
            form += '\0';
            shellOpStruct.pFrom = form;
            shellOpStruct.pTo = toDir;
            MF.Win32API.SHFileOperation(ref shellOpStruct);
        } 

        /// <summary>
        /// クリップボードの"Preferred DropEffect"を調べる
        /// </summary>
        public static DragDropEffects GetPreferredDropEffect(IDataObject data) {
            DragDropEffects dde = DragDropEffects.None;

            if (data != null) {
                //Preferred DropEffect形式のデータを取得する
                System.IO.MemoryStream ms =
                    (System.IO.MemoryStream)data.GetData("Preferred DropEffect");
                if (ms != null) {
                    //先頭のバイトからDragDropEffectsを取得する
                    dde = (DragDropEffects)ms.ReadByte();

                    if (dde == (DragDropEffects.Copy | DragDropEffects.Link)) {
                    }
                    else if (dde == DragDropEffects.Move) {
                    }
                }
            }

            return dde;
        }

        public static Win32API.SIZE GetTextExtend(Control c, string str) {
            IntPtr hwnd_ = c.Handle;
            IntPtr hfont_ = c.Font.ToHfont();
            IntPtr dc_ = Win32API.GetDC(hwnd_);
            int fit = 0;
            IntPtr OldFont = Win32API.SelectObject(dc_, hfont_);
            Win32API.SIZE size = new Win32API.SIZE();
            Win32API.GetTextExtentExPointW(dc_, str, str.Length, int.MaxValue, out fit, null, out size);
            Win32API.SelectObject(dc_, OldFont);

            Win32API.ReleaseDC(hwnd_, dc_);
            Win32API.DeleteObject(hfont_);

            return size;
        }
    }
}
