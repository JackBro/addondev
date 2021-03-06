﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace MF {
    class Win32API {
        [DllImport("user32")]
        public static extern IntPtr GetDC(IntPtr hWnd);

        [DllImport("user32")]
        public static extern Int32 ReleaseDC(IntPtr hWnd, IntPtr dc);

        [DllImport("GDI32.dll")]
        public static extern bool DeleteObject(IntPtr objectHandle);

        [DllImport("gdi32.dll", EntryPoint = "SelectObject")]
        public static extern IntPtr SelectObject(
            IntPtr hdc,
            IntPtr hgdiobj);

        [StructLayout(LayoutKind.Sequential)]
        public struct SIZE {
            public Int32 width, height;
        }

        [DllImport("gdi32.dll", CharSet = CharSet.Unicode)]
        public static extern bool GetTextExtentExPointW(IntPtr hdc, [MarshalAs(UnmanagedType.LPWStr)] string lpszStr,
           int cchString, int nMaxExtent, out int lpnFit, int[] alpDx, out SIZE lpSize);

        [ComVisibleAttribute(false)]
        internal class NativeMethods { [DllImport("user32.dll")] public static extern int SendMessage(IntPtr hWnd, int msg, int wParam, IntPtr lParam); }


        public enum FOFunc : uint {
            FO_MOVE = 0x0001,
            FO_COPY = 0x0002,
            FO_DELETE = 0x0003,
            FO_RENAME = 0x0004
        }

        public enum FOFlags : ushort {
            FOF_MULTIDESTFILES = 0x0001,
            FOF_CONFIRMMOUSE = 0x0002,
            FOF_SILENT = 0x0004,  // don't create progress/report
            FOF_RENAMEONCOLLISION = 0x0008,
            FOF_NOCONFIRMATION = 0x0010,  // Don't prompt the user.
            FOF_WANTMAPPINGHANDLE = 0x0020,  // Fill in SHFILEOPSTRUCT.hNameMappings
            // Must be freed using SHFreeNameMappings
            FOF_ALLOWUNDO = 0x0040,
            FOF_FILESONLY = 0x0080,  // on *.*, do only files
            FOF_SIMPLEPROGRESS = 0x0100,  // means don't show names of files
            FOF_NOCONFIRMMKDIR = 0x0200,  // don't confirm making any needed dirs
            FOF_NOERRORUI = 0x0400,  // don't put up error UI
            FOF_NOCOPYSECURITYATTRIBS = 0x0800,  // dont copy NT file Security Attributes
            FOF_NORECURSION = 0x1000,  // don't recurse into directories.
            FOF_NO_CONNECTED_ELEMENTS = 0x2000,  // don't operate on connected elements.
            FOF_WANTNUKEWARNING = 0x4000,  // during delete operation, warn if nuking instead of recycling (partially overrides FOF_NOCONFIRMATION)
            FOF_NORECURSEREPARSE = 0x8000  // treat reparse points as objects, not containers
        }
        // typedef struct _SHFILEOPSTRUCT 
        //              {
        //                      HWND hwnd;
        //                      UINT wFunc;
        //                      LPCTSTR pFrom;
        //                      LPCTSTR pTo;
        //                      FILEOP_FLAGS fFlags;
        //                      BOOL fAnyOperationsAborted;
        //                      LPVOID hNameMappings;
        //                      LPCTSTR lpszProgressTitle;
        //              } SHFILEOPSTRUCT, *LPSHFILEOPSTRUCT;
        [StructLayout(LayoutKind.Sequential)]
        public struct SHFILEOPSTRUCT {
            public IntPtr hwnd;
            public FOFunc wFunc;
            [MarshalAs(UnmanagedType.LPWStr)]
            public string pFrom;
            [MarshalAs(UnmanagedType.LPWStr)]
            public string pTo;
            public FOFlags fFlags;
            public bool fAnyOperationsAborted;
            public IntPtr hNameMappings;
            [MarshalAs(UnmanagedType.LPWStr)]
            public string lpszProgressTitle;
        }
        // int SHFileOperation(          LPSHFILEOPSTRUCT lpFileOp
        //      );
        // Unicode を明示的に指定する必要があります。
        [DllImport("Shell32.dll", CharSet = CharSet.Unicode)]
        public static extern int SHFileOperation(
                ref SHFILEOPSTRUCT lpFileOp
                );


        

    }

    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
    public struct SHFILEINFO {
        public IntPtr hIcon;
        public int iIcon;
        public uint dwAttributes;
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 260)]
        public string szDisplayName;
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 80)]
        public string szTypeName;
    }
    [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
    public struct LVITEM {
        public uint mask;
        public int iItem;
        public int iSubItem;
        public int state;
        public int stateMask;
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 80)]
        public string pszText;
        public int cchTextMax;
        public int iImage;
        public uint lParam;
        public int iIndent;
    }
    /// <summary>
    /// NativeMethods の概要の説明です。
    /// </summary>
    public class NativeMethods {
        public const int SHGFI_LARGEICON = 0x00000000;
        public const int SHGFI_SMALLICON = 0x00000001;
        public const int SHGFI_USEFILEATTRIBUTES = 0x00000010;
        public const int SHGFI_OVERLAYINDEX = 0x00000040;
        public const int SHGFI_ICON = 0x00000100;
        public const int SHGFI_SYSICONINDEX = 0x00004000;
        public const int LVSIL_NORMAL = 0;
        public const int LVSIL_SMALL = 1;
        public const int LVIS_OVERLAYMASK = 0x0F00;
        public const int LVM_SETIMAGELIST = 0x1003;
        public const int LVM_SETITEMSTATE = 0x102B;
        [DllImport("shell32.dll", CharSet = CharSet.Auto)]
        public static extern IntPtr SHGetFileInfo(string pszPath, uint dwFileAttributes,
            out SHFILEINFO psfi, uint cbFileInfo, uint uFlags);
        [DllImport("user32.dll", CharSet = CharSet.Auto)]
        public static extern IntPtr SendMessage(IntPtr hWnd, uint Msg, IntPtr wParam, IntPtr lParam);
        [DllImport("user32.dll", CharSet = CharSet.Auto)]
        public static extern IntPtr SendMessage(IntPtr hWnd, uint Msg, int wParam, ref LVITEM lParam);
        [DllImport("user32.dll", SetLastError = true)]
        public static extern bool DestroyIcon(IntPtr hIcon);
        public NativeMethods() {
        }
    }
}
