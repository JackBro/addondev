using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Drawing;

namespace YYS
{
    class Win32API
    {
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
        public struct SIZE
        {
            public Int32 width, height;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct RECT {
            public Int32 left;
            public Int32 top;
            public Int32 right;
            public Int32 bottom;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct POINT {
            public int x;
            public int y;
        } 

        //[DllImport("gdi32", CharSet = CharSet.Unicode)]
        //public unsafe static extern Int32 GetTextExtentExPointW(IntPtr hdc, string text, int textLen, int maxWidth, int* out_fitLength, int* out_x, SIZE* out_size);


        //[DllImport("gdi32", CharSet = CharSet.Unicode)]
        //public unsafe static extern Int32 GetTextExtentPoint32W(IntPtr hdc, string text, int textLen, ref SIZE out_size);

        [DllImport("gdi32.dll", CharSet = CharSet.Unicode)]
        public static extern bool GetTextExtentExPointW(IntPtr hdc, [MarshalAs(UnmanagedType.LPWStr)] string lpszStr,
           int cchString, int nMaxExtent, out int lpnFit, int[] alpDx, out SIZE lpSize);


        [DllImport("gdi32", CharSet = CharSet.Unicode)]
        public unsafe static extern Int32 GetCharWidthW(IntPtr hdc, int iFirstChar, int iLastChar, ref int w);


        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool InvertRect(IntPtr hDC, ref System.Drawing.Rectangle lprc);

        //caret
        [DllImport("user32.dll")]
        public static extern int CreateCaret(IntPtr hwnd, IntPtr hbm, int cx, int cy);
        [DllImport("user32.dll")]
        public static extern int DestroyCaret();
        [DllImport("user32.dll")]
        public static extern int SetCaretPos(int x, int y);
        [DllImport("user32.dll")]
        public static extern bool GetCaretPos(out POINT lpPoint);
        [DllImport("user32.dll")]
        public static extern int ShowCaret(IntPtr hwnd);
        [DllImport("user32.dll")]
        public static extern int HideCaret(IntPtr hwnd);

        //
        [DllImport("gdi32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern int GetCharWidth32(IntPtr hDC, int first, int last, IntPtr widthsBuffer);


        [Serializable, StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct TEXTMETRIC {
            public int tmHeight;
            public int tmAscent;
            public int tmDescent;
            public int tmInternalLeading;
            public int tmExternalLeading;
            public int tmAveCharWidth;
            public int tmMaxCharWidth;
            public int tmWeight;
            public int tmOverhang;
            public int tmDigitizedAspectX;
            public int tmDigitizedAspectY;
            public char tmFirstChar;
            public char tmLastChar;
            public char tmDefaultChar;
            public char tmBreakChar;
            public byte tmItalic;
            public byte tmUnderlined;
            public byte tmStruckOut;
            public byte tmPitchAndFamily;
            public byte tmCharSet;
        }

        //
        [DllImport("gdi32.dll", CharSet = CharSet.Unicode)]
        public static extern bool GetTextMetrics(IntPtr hdc, out TEXTMETRIC lptm);

        [DllImport("gdi32")]
        public static extern int IntersectClipRect(IntPtr hDC, int X1, int Y1, int X2, int Y2);

        [DllImport("gdi32.dll")]
        public static extern int SelectClipRgn(IntPtr hdc, IntPtr hrgn);

        [DllImport("user32.dll")]
        public static extern bool ValidateRect(IntPtr hWnd, ref RECT lpRect);

        [DllImport("user32.dll")]
        public static extern bool InvalidateRect(IntPtr hWnd, ref RECT lpRect, bool bErase);

        [DllImport("user32.dll")]
        public static extern bool UpdateWindow(IntPtr hWnd);
        
        //[DllImport("user32.dll")]
        //public static extern int ScrollWindowEx(IntPtr hWnd, int dx, int dy, IntPtr prcScroll,
        //    [In] ref RECT prcClip, IntPtr hrgnUpdate, IntPtr prcUpdate, uint flags);

        //[DllImport("user32.dll")]
        //public static extern int ScrollWindowEx(IntPtr hWnd, int dx, int dy, ref RECT prcScroll,
        //    [In] ref RECT prcClip, IntPtr hrgnUpdate, IntPtr prcUpdate, uint flags);

        const int SW_INVALIDATE = 0x0002;

        [DllImport("user32.dll")]
        public static unsafe extern Int32 ScrollWindowEx(IntPtr window, int x, int y, RECT* scroll, RECT* clip, IntPtr updateRegion, RECT* update, UInt32 flags);

        public static void ScrollWindow(IntPtr window, int x, int y, RECT clip) {
            unsafe {
                ScrollWindowEx(window, x, y, null, &clip, IntPtr.Zero, null, SW_INVALIDATE);
            }
        }

        public static void ScrollWindow(IntPtr window, int x, int y) {
            unsafe {
                ScrollWindowEx(window, x, y, null, null, IntPtr.Zero, null, SW_INVALIDATE);
            }
        }
    }
}
