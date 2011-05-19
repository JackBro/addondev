using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;

namespace MF {

    //public class ColumnEventArgs : EventArgs {
    //    public int index { get; set; }
    //}
    //public delegate void ColumnEventHandler(object sender, ColumnEventArgs e);

    public class ListViewEx : ListView {
        private const int WM_MOUSEMOVE = 0x0200;
        private const int WM_LBUTTONDOWN = 0x0201;
        private const int WM_LBUTTONUP = 0x0202;
        private const int WM_LBUTTONDBLCLK = 0x0203;
        private const int WM_RBUTTONDOWN = 0x204;
        private const int WM_RBUTTONUP = 0x205;
        private const int WM_MBUTTONDOWN = 0x0207;
        private const int WM_MBUTTONUP = 0x0208;

        public event MouseEventHandler MouseUpEx;
        public event MouseEventHandler DoubleClickEx;
        //public event ColumnEventHandler ColumnDoubleClick;
        public ListViewEx() {
            this.DoubleBuffered = true;
        }

        protected override void WndProc(ref Message m) {
            switch (m.Msg) {
                case WM_RBUTTONUP:
                    if (MouseUpEx != null) {
                        Point p = new Point(m.LParam.ToInt32());
                        MouseUpEx(this, new MouseEventArgs(MouseButtons.Right, 1, p.X, p.Y, 0));
                    }
                    break;
                case WM_LBUTTONDBLCLK:
                    if (DoubleClickEx != null) {
                        Point p = new Point(m.LParam.ToInt32());
                        DoubleClickEx(this, new MouseEventArgs(MouseButtons.Left, 1, p.X, p.Y, 0));
                    }
                    break;
                default:
                    break;
            }
            //if (m.Msg == WM_RBUTTONUP){
            //    if (MouseUpEx != null) {
            //        Point p = new Point(m.LParam.ToInt32());
            //        MouseUpEx(this, new MouseEventArgs(MouseButtons.Right, 1, p.X, p.Y, 0));
            //    }
            //}
            base.WndProc(ref m);
        }
    }
}
