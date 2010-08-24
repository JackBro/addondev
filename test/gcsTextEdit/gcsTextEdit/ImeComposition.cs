using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace AsControls
{
    class ImeComposition
    {
        [DllImport("Imm32.dll")]
        private static extern int ImmGetContext(IntPtr hWnd);

        [DllImport("Imm32.dll")]
        private static extern int ImmGetCompositionString(
            int hIMC, int dwIndex, StringBuilder lpBuf, int dwBufLen);

        [DllImport("Imm32.dll")]
        private static extern bool ImmReleaseContext(IntPtr hWnd, int hIMC);

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

        public delegate void ImeCompositionEventHandler(object sender, ImeCompositionEventArgs e);

        public event ImeCompositionEventHandler ImeCompositionHira = null;
        public event ImeCompositionEventHandler ImeCompositionKata = null;
        public event ImeCompositionEventHandler ImeCompositedHira = null;
        public event ImeCompositionEventHandler ImeCompositedKata = null;

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

        private Control control;

        public ImeComposition(Control control)
        {
            this.control = control;
        }

        public void Ime(Message m)
        {
            //// 変換確定(1バイトカタカナ)
            //if (ImeCompositedKata != null && m.Msg == WM_IME_COMPOSITION)
            //{
            //    if (((int)m.LParam & GCS_RESULTREADSTR) > 0)
            //    {
            //        int hIMC = ImmGetContext(this.handle);
            //        int strLen = ImmGetCompositionString(hIMC, GCS_RESULTREADSTR, null, 0);
            //        StringBuilder str = new StringBuilder(strLen);

            //        ImmGetCompositionString(hIMC, GCS_RESULTREADSTR, str, str.Capacity);
            //        ImmReleaseContext(this.handle, hIMC);

            //        if (ImeCompositedKata != null)
            //        {
            //            // 環境によって文字コードが違うので、それにあわせる
            //            byte[] tmp1 = System.Text.Encoding.Default.GetBytes(str.ToString());
            //            byte[] tmp2 = new byte[strLen];
            //            Array.Copy(tmp1, 0, tmp2, 0, strLen);
            //            ImeCompositedKata(this,
            //                new ImeCompositionEventArgs(System.Text.Encoding.Default.GetString(tmp2)));
            //        }
            //    }
            //}

            // 変換確定(ひらがな)
            if (ImeCompositedHira != null && m.Msg == WM_IME_COMPOSITION)
            {
                if (((int)m.LParam & GCS_RESULTSTR) > 0)
                {
                    int hIMC = ImmGetContext(this.control.Handle);
                    int strLen = ImmGetCompositionString(hIMC, GCS_RESULTSTR, null, 0);
                    StringBuilder str = new StringBuilder(strLen);

                    ImmGetCompositionString(hIMC, GCS_RESULTSTR, str, str.Capacity);
                    ImmReleaseContext(this.control.Handle, hIMC);

                    if (ImeCompositedHira != null)
                    {
                        // 環境によって文字コードが違うので、それにあわせる
                        byte[] tmp1 = System.Text.Encoding.Default.GetBytes(str.ToString());
                        byte[] tmp2 = new byte[strLen];
                        Array.Copy(tmp1, 0, tmp2, 0, strLen);
                        ImeCompositedHira(this,
                            new ImeCompositionEventArgs(System.Text.Encoding.Default.GetString(tmp2)));
                    }
                }
            }
        }

    }
}
