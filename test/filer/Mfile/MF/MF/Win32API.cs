using System;
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
    }
}
