using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;

namespace wiki.control {
    public partial class CompleForm : Form {
        private MainForm form;
        private int startOffset;

        public CompleForm(MainForm form) {
            InitializeComponent();
            this.FormBorderStyle = FormBorderStyle.None;
            this.Owner = form; 

            this.form = form;

            startOffset = form.Editor.Document.AnchorIndex;

            form.Editor.TextChanged += new System.EventHandler(Editor_TextChanged);
            form.Editor.MouseDown += new MouseEventHandler(Editor_MouseDown);
            form.Editor.PreKeyDown += new KeyEventHandler(Editor_PreKeyDown);
                
            this.Shown += (sender, e) => {
                form.Editor.Focus();
            };
            this.FormClosing += (sender, e) => {
                form.Editor.TextChanged -= Editor_TextChanged;
                form.Editor.MouseDown -= Editor_MouseDown;
                form.Editor.PreKeyDown -= Editor_PreKeyDown;
            };
            listBox.DoubleClick += (sender, e) => {

            };
            var h = listBox.ItemHeight * (listBox.Items.Count)-5;
            this.Height = h == 0 ? listBox.ItemHeight : h;

            var pos = form.Editor.PointToScreen(form.Editor.GetPositionFromIndex(form.Editor.Document.AnchorIndex));
            pos.Y += (int)form.Editor.Font.GetHeight();
            this.StartPosition = FormStartPosition.Manual;
            this.Left = pos.X;
            this.Top = pos.Y;
        }

        void Editor_PreKeyDown(object sender, KeyEventArgs e) {
            e.Handled = true;
            switch (e.KeyCode) {
                case Keys.Up:
                    listBox.SelectedIndex = listBox.SelectedIndex == 0 ? 0 : listBox.SelectedIndex - 1;
                    break;
                case Keys.Down:
                    listBox.SelectedIndex = listBox.SelectedIndex == listBox.Items.Count - 1 ? listBox.SelectedIndex : listBox.SelectedIndex + 1;
                    break;
                case Keys.PageUp: {
                        var count = listBox.Height / listBox.ItemHeight;
                        listBox.SelectedIndex = listBox.SelectedIndex - count < 0 ? 0 : listBox.SelectedIndex -count;
                    }
                    break;
                case Keys.PageDown: {
                        var count = listBox.Height / listBox.ItemHeight;
                        listBox.SelectedIndex = listBox.SelectedIndex + count == listBox.Items.Count - 1 ? listBox.SelectedIndex : listBox.SelectedIndex + count;
                    }
                    break;
                case Keys.Home:
                    listBox.SelectedIndex = 0;
                    break;
                case Keys.End:
                    listBox.SelectedIndex = listBox.Items.Count - 1;
                    break;
                case Keys.Left:           
                case Keys.Right:
                case Keys.Escape:
                    e.Handled = false;
                    Close();                   
                    break;
                default:
                    e.Handled = false;
                    break;
            }
        }

        void Editor_MouseDown(object sender, MouseEventArgs e) {
            Close();
        }

        void Editor_TextChanged(object sender, EventArgs e) {
            //throw new NotImplementedException();
        }


        [Flags]
        private enum BORDER : uint {
            NONE = 0x0000,
            TOP = 0x0001,
            BOTTOM = 0x0002,
            LEFT = 0x0004,
            RIGHT = 0x0008,
        }

        [DllImport("user32.dll", SetLastError = true)]
        private static extern bool SendMessage(IntPtr HWnd, uint Msg, IntPtr WParam, IntPtr LParam);
        protected override void WndProc(ref Message m) {
            switch ((uint)m.Msg) {
                // リサイズの処理
                case Windows.WM_NCHITTEST:
                    base.WndProc(ref m);

                    switch ((int)m.Result) {
                        case Windows.HTCLIENT:

                            const int BORDER_WIDTH = 3;
                            BORDER Border = BORDER.NONE;

                            var X = (int)Windows.LOWORD((uint)m.LParam);
                            var Y = (int)Windows.HIWORD((uint)m.LParam);
                            var L = this.Left;
                            var R = this.Right;
                            var T = this.Top;
                            var B = this.Bottom;

                            if (Y - T < BORDER_WIDTH) Border |= BORDER.TOP;
                            if (B - Y < BORDER_WIDTH) Border |= BORDER.BOTTOM;
                            if (X - L < BORDER_WIDTH) Border |= BORDER.LEFT;
                            if (R - X < BORDER_WIDTH) Border |= BORDER.RIGHT;

                            switch (Border) {
                                case BORDER.TOP:
                                    m.Result = (IntPtr)Windows.HTTOP;
                                    break;
                                case BORDER.BOTTOM:
                                    m.Result = (IntPtr)Windows.HTBOTTOM;
                                    break;
                                case BORDER.LEFT:
                                    m.Result = (IntPtr)Windows.HTLEFT;
                                    break;
                                case BORDER.RIGHT:
                                    m.Result = (IntPtr)Windows.HTRIGHT;
                                    break;
                                case BORDER.TOP | BORDER.LEFT:
                                    m.Result = (IntPtr)Windows.HTTOPLEFT;
                                    break;
                                case BORDER.TOP | BORDER.RIGHT:
                                    m.Result = (IntPtr)Windows.HTTOPRIGHT;
                                    break;
                                case BORDER.BOTTOM | BORDER.LEFT:
                                    m.Result = (IntPtr)Windows.HTBOTTOMLEFT;
                                    break;
                                case BORDER.BOTTOM | BORDER.RIGHT:
                                    m.Result = (IntPtr)Windows.HTBOTTOMRIGHT;
                                    break;
                            }
                            break;
                    }
                    break;

                // その他
                default:
                    base.WndProc(ref m);
                    break;
            }
        }
    }


    public static class Windows {
        public const uint WM_GETTEXT = 0x000d;
        public const uint WM_GETTEXTLENGTH = 0x000e;

        public const uint WM_NCHITTEST = 0x0084;    // マウスポインタの位置を問い合わせる
        public const uint WM_NCLBUTTONDOWN = 0x00A1;    // 領域外でマウスの左が押下された
        public const uint WM_LBUTTONDOWN = 0x0201;    // 領域内でマウスの左が押下された
        public const uint WM_LBUTTONUP = 0x0202;    // 領域内でマウスの左が解放された
        public const uint WM_LBUTTONDBLCLK = 0x0203;    // 領域内でマウスの左がダブルクリックされた

        public const int HTTRANSPARENT = -1;        // マウスポインタが別のウィンドウの下にある
        public const int HTCLIENT = 1;         // マウスポインタがクライアント領域内にある
        public const int HTCAPTION = 2;         // マウスポインタがキャプションバー上にある
        public const int HTLEFT = 10;        // マウスポインタが左端にある
        public const int HTRIGHT = 11;        // マウスポインタが右端にある
        public const int HTTOP = 12;        // マウスポインタが上端にある
        public const int HTTOPLEFT = 13;        // マウスポインタが上左にある
        public const int HTTOPRIGHT = 14;        // マウスポインタが上右にある
        public const int HTBOTTOM = 15;        // マウスポインタが下端にある
        public const int HTBOTTOMLEFT = 16;        // マウスポインタが下左にある
        public const int HTBOTTOMRIGHT = 17;        // マウスポインタが下右にある

        public static uint HIWORD(uint i) { return i >> 16 & 0xFFFF; }
        public static uint LOWORD(uint i) { return i >> 0 & 0xFFFF; }
    }
}
