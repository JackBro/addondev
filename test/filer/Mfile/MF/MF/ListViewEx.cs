using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace MF {

    public class ColumnEventArgs : EventArgs {
        public int index { get; set; }
    }
    public delegate void ColumnEventHandler(object sender, ColumnEventArgs e);

    public class ListViewEx : ListView {
        public event ColumnEventHandler ColumnDoubleClick;

        public int[] FixedColumns {
            get { return this.fixedColumns; }
            set { this.fixedColumns = (value == null) ? new int[0] : value; }
        }
        private int[] fixedColumns = new int[0];
        private struct NotifyMessageHeader {
            public IntPtr Window;
            public int Id;
            public int NotifyCode;
            public int ItemIndex;
            public MouseButton MouseButton;
            public IntPtr ItemInfo;
            // 単に警告を黙らせるためだけなので無くて良いコンストラクタ
            public NotifyMessageHeader(int dummy) {
                this.Window = this.ItemInfo = IntPtr.Zero;
                this.Id = this.NotifyCode = this.ItemIndex = 0;
                this.MouseButton = MouseButton.Left;
            }
        }
        private enum MouseButton {
            Left = 0,
            Right = 1,
            Middle = 2,
        }
        protected virtual bool ProcessNotifyMessage(ref Message m) {
            object lParam = m.GetLParam(typeof(NotifyMessageHeader));
            NotifyMessageHeader header = (NotifyMessageHeader)lParam;
            //const int BeginTrackW = -326; //HDN_BEGINTRACKW
            //const int BeginTrackA = -306; //HDN_BEGINTRACKA
            const int DividerDoubleClickW = -325; //HDCN_DIVIDERDBLCLICKW
            const int DividerDoubleClickA = -305; //HDCN_DIVIDERDBLCLICKA
            switch (header.NotifyCode) {
                //A が来るはずなのに W が来たりなどあるようなのでいっそ全部
                //case BeginTrackW:
                case DividerDoubleClickW:
                //case BeginTrackA:
                case DividerDoubleClickA:
                    if (this.fixedColumns == null)
                        break;
                    if (Array.IndexOf(this.fixedColumns, header.ItemIndex) != -1) {
                        m.Result = new IntPtr(1);
                        if (ColumnDoubleClick != null) {
                            ColumnDoubleClick(this, new ColumnEventArgs { index = header.ItemIndex});
                        }
                        return true;
                    }
                    break;
            }
            return false;
        }

        protected override void WndProc(ref Message m) {
            if (m.Msg == 0x4E) { //WM_NOTIFY
                if (ProcessNotifyMessage(ref m))
                    return;
            }
            base.WndProc(ref m);
        }
    }
}
