using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace AsControls
{
    class Ime
    {
        [DllImport("Imm32.dll")]
        private static extern int ImmGetContext(IntPtr hWnd);

        [DllImport("Imm32.dll")]
        private static extern int ImmGetCompositionString(
            int hIMC, int dwIndex, StringBuilder lpBuf, int dwBufLen);

        [DllImport("Imm32.dll")]
        private static extern bool ImmReleaseContext(IntPtr hWnd, int hIMC);

        [StructLayout(LayoutKind.Sequential)]
        public struct Point {
            public int x;
            public int y;
        }
        [StructLayout(LayoutKind.Explicit)]
        public struct Rect {
            [FieldOffset(0)]
            public int left;
            [FieldOffset(4)]
            public int top;
            [FieldOffset(8)]
            public int right;
            [FieldOffset(12)]
            public int bottom;
        }
        [StructLayout(LayoutKind.Sequential)]
        public struct COMPOSITIONFORM {
            public int dwStyle;
            public Point ptCurrentPos;
            public Rect rcArea;
        }

        [DllImport("imm32.dll")]
        public static extern int ImmSetCompositionWindow(int hIMC, ref COMPOSITIONFORM lpCompositionForm);

        public class ImeCompositionEventArgs : System.EventArgs
        {
            private string str;

            public ImeCompositionEventArgs()
            {
            }
            public ImeCompositionEventArgs(string str)
            {
                this.str = str;
            }

            public string InputString
            {
                set { this.str = value; }
                get { return this.str; }
            }
        }

        public delegate void ImeEventHandler(object sender, ImeCompositionEventArgs e);

        public event ImeEventHandler ImeCompositionHira = null;
        public event ImeEventHandler ImeCompositionKata = null;
        public event ImeEventHandler ImeCompositedHira = null;
        public event ImeEventHandler ImeCompositedKata = null;

        // IMEでキーが押されたかのフラグ
        private const int WM_IME_COMPOSITION = 0x010F;
        // 変換確定後文字取得に使用する値(ひらがな)
        private const int GCS_RESULTSTR = 0x0800;
        // 変換確定後文字取得に使用する値(1バイトカタカナ)
        private const int GCS_RESULTREADSTR = 0x0200;
        // IME入力中文字取得に使用する値(ひらがな)
        private const int GCS_COMPSTR = 0x0008;
        // IME入力中文字取得に使用する値(1バイトカタカナ)
        private const int GCS_COMPREADSTR = 0x0001;

        private const int CFS_POINT = 0x0002;
        private const int WM_IME_STARTCOMPOSITION = 0x010D;

        private Control control;

        public Ime(Control control)
        {
            this.control = control;
        }
        public bool isImeComposition(Message m) {
            return (ImeCompositedHira != null && m.Msg == WM_IME_COMPOSITION);
        }

        public void ImeComposition(Message m) {
            if (((int)m.LParam & GCS_RESULTSTR) > 0) {
                int hIMC = ImmGetContext(this.control.Handle);
                int strLen = ImmGetCompositionString(hIMC, GCS_RESULTSTR, null, 0);
                StringBuilder str = new StringBuilder(strLen);

                ImmGetCompositionString(hIMC, GCS_RESULTSTR, str, str.Capacity);
                ImmReleaseContext(this.control.Handle, hIMC);

                //if (ImeCompositedHira != null)
                //{
                // 環境によって文字コードが違うので、それにあわせる
                byte[] tmp1 = System.Text.Encoding.Default.GetBytes(str.ToString());
                byte[] tmp2 = new byte[strLen];
                Array.Copy(tmp1, 0, tmp2, 0, strLen);
                ImeCompositedHira(this,
                    new ImeCompositionEventArgs(System.Text.Encoding.Default.GetString(tmp2)));
                //}
            }
        }
        public bool isStartcomposition(Message m) {
            return (ImeCompositedHira != null && m.Msg == WM_IME_STARTCOMPOSITION);
        }
        public void ImeStartcomposition(Message m, int x, int y) {
            int hIMC = ImmGetContext(this.control.Handle);
            COMPOSITIONFORM cf = new COMPOSITIONFORM();
            cf.dwStyle = CFS_POINT;
            cf.ptCurrentPos = new Point();
            cf.ptCurrentPos.x = x;
            cf.ptCurrentPos.y = y;
            int res = ImmSetCompositionWindow(hIMC, ref cf);

            ImmReleaseContext(this.control.Handle, hIMC);
        }

        //public void Ime(Message m, int x, int y)
        //{
        //    //// 変換確定(1バイトカタカナ)
        //    //if (ImeCompositedKata != null && m.Msg == WM_IME_COMPOSITION)
        //    //{
        //    //    if (((int)m.LParam & GCS_RESULTREADSTR) > 0)
        //    //    {
        //    //        int hIMC = ImmGetContext(this.handle);
        //    //        int strLen = ImmGetCompositionString(hIMC, GCS_RESULTREADSTR, null, 0);
        //    //        StringBuilder str = new StringBuilder(strLen);

        //    //        ImmGetCompositionString(hIMC, GCS_RESULTREADSTR, str, str.Capacity);
        //    //        ImmReleaseContext(this.handle, hIMC);

        //    //        if (ImeCompositedKata != null)
        //    //        {
        //    //            // 環境によって文字コードが違うので、それにあわせる
        //    //            byte[] tmp1 = System.Text.Encoding.Default.GetBytes(str.ToString());
        //    //            byte[] tmp2 = new byte[strLen];
        //    //            Array.Copy(tmp1, 0, tmp2, 0, strLen);
        //    //            ImeCompositedKata(this,
        //    //                new ImeCompositionEventArgs(System.Text.Encoding.Default.GetString(tmp2)));
        //    //        }
        //    //    }
        //    //}

        //    // 変換確定(ひらがな)
        //    if (ImeCompositedHira != null && m.Msg == WM_IME_COMPOSITION)
        //    {
        //        if (((int)m.LParam & GCS_RESULTSTR) > 0)
        //        {
        //            int hIMC = ImmGetContext(this.control.Handle);
        //            int strLen = ImmGetCompositionString(hIMC, GCS_RESULTSTR, null, 0);
        //            StringBuilder str = new StringBuilder(strLen);

        //            ImmGetCompositionString(hIMC, GCS_RESULTSTR, str, str.Capacity);
        //            ImmReleaseContext(this.control.Handle, hIMC);

        //            //if (ImeCompositedHira != null)
        //            //{
        //                // 環境によって文字コードが違うので、それにあわせる
        //                byte[] tmp1 = System.Text.Encoding.Default.GetBytes(str.ToString());
        //                byte[] tmp2 = new byte[strLen];
        //                Array.Copy(tmp1, 0, tmp2, 0, strLen);
        //                ImeCompositedHira(this,
        //                    new ImeCompositionEventArgs(System.Text.Encoding.Default.GetString(tmp2)));
        //            //}
        //        }
        //    } else if (ImeCompositedHira != null && m.Msg == WM_IME_STARTCOMPOSITION) {
        //        int hIMC = ImmGetContext(this.control.Handle);
        //        COMPOSITIONFORM cf = new COMPOSITIONFORM();
        //        cf.dwStyle = CFS_POINT;
        //        cf.ptCurrentPos = new Point();
        //        cf.ptCurrentPos.x = x;
        //        cf.ptCurrentPos.y = y;
        //        int res = ImmSetCompositionWindow(hIMC, ref cf);

        //        ImmReleaseContext(this.control.Handle, hIMC);
        //    }
        //}

    }
}
