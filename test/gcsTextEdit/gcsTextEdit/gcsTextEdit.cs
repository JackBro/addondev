using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Text.RegularExpressions;

namespace AsControls {
    public enum WrapType {
        Non,
        WindowWidth
    }

    public delegate Action<object, WrapType> WrapModeChangeEventHandler(object sender, WrapType wrapMode);

    public partial class gcsTextEdit : Control, ITextEditor {
        
        private class ViewRect {
            public Rectangle rc;
            public int XBASE;      // 一番左の文字のx座標
            public int XMIN;       // テキスト再描画範囲左端
            public int XMAX;       // テキスト再描画範囲右端
            public int YMIN;
            public int YMAX;
            public int TLMIN;
            public int SXB, SXE;   // 選択範囲のx座標
            public int SYB, SYE;   // 選択範囲のy座標

            public int NumLineLeft;

            public ViewRect() {
                rc = new Rectangle();
                YMIN = 0;
                YMAX = 0;
                TLMIN = 0;
                SXB = SXE = SYB = SYE = 0;

                NumLineLeft = 0;
            }
        }

        Document doc;

        ViewRect vRect = new ViewRect();

        VScrollBar vScrollBar;
        int vpage = 0;

        int udScr_tl_;  //一番上に表示される論理行のTLine_Index
        int udScr_vrl_; //一番上に表示される表示行のVRLine_Index

        HScrollBar hScrollBar;
        int hpage = 0;

        Panel scPanel = new Panel();

        ImeComposition imeComposition;

        WrapType wrapType;
        Boolean showNumLine = false;

        public KeyMap KeyBind { get; set; }

        public int ViewWidth {
            get {
                if (wrapType == WrapType.Non) {
                    return int.MaxValue;
                } else {
                    if (this.Width - (vRect.NumLineLeft + vScrollBar.Width) < 0)
                        return 0;

                    return this.Width - (vRect.NumLineLeft + vScrollBar.Width);
                }
            }
        }

        public int ViewHeight {
            get {
                if (this.Height - hScrollBar.Height < 0)
                    return 0;

                return this.Height - hScrollBar.Height;
            }
        }

        public int ViewLineHeight {
            get { return lineHeight; }
        }

        public new Font Font {
            get { return base.Font; }
            set {
                base.Font = value;
                initDraw();
            }
        }

        public WrapType Wrap {
            get { return wrapType; }
            set {
                wrapType = value;
                ReWrapAll();
            }
        }

        public new string Text {
            set {
                this.doc.Clear();
                //InitCaret();
                //initWrap();
                //udScr_tl_ = 0;
                //udScr_vrl_ = 0;
                //hpage = 0;
                //vpage = 0;
                Initialize();

                Input(value);
            }

            get {
                return this.doc.ToString();
            }
        }

        private Document Document {
            get { return this.doc; }
            set {
                if (doc != null) {
                    doc.TextUpdateEvent -= doc_TextUpdateEvent;
                }
                doc = value;
                doc.TextUpdateEvent += new TextUpdateEventHandler(doc_TextUpdateEvent);
                //initWrap();
                //ReWrapAll();
                //string sss = doc.ToString();
                //Input(sss);
                ResizeWrapList(doc.tlNum);
            }
        }

        public gcsTextEdit() {
            this.SetStyle(ControlStyles.OptimizedDoubleBuffer | ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint, true);

            wrapType = WrapType.WindowWidth;
            //wrapType = WrapType.Non;
            showNumLine = true;

            doc = new Document();
            doc.TextUpdateEvent += new TextUpdateEventHandler(doc_TextUpdateEvent);

            initDraw();
            //initWrap();

            //udScr_tl_ = 0;
            //udScr_vrl_ = 0;

            vScrollBar = new VScrollBar();
            vScrollBar.Scroll += new ScrollEventHandler(ScrollBar_Scroll);
            this.Controls.Add(vScrollBar);

            hScrollBar = new HScrollBar();
            hScrollBar.Scroll += new ScrollEventHandler(ScrollBar_Scroll);
            this.Controls.Add(hScrollBar);

            scPanel.BackColor = this.BackColor;
            scPanel.Size = new Size(hScrollBar.Height, vScrollBar.Width);
            this.Controls.Add(scPanel);

            this.ImeMode = ImeMode.Off;
            imeComposition = new ImeComposition(this);
            imeComposition.ImeCompositedHira += new ImeComposition.ImeCompositionEventHandler(imeComposition_ImeCompositedHira);
            imeComposition.ImeCompositedKata += new ImeComposition.ImeCompositionEventHandler(imeComposition_ImeCompositedKata);

            this.HandleDestroyed += new EventHandler(AsTextEdit_HandleDestroyed);

            KeyBind = new KeyMap();
            //KeyBind.SetAction("cmd_Undo", delegate(object obj)
            //{
            //    if (doc.UndoManager.CanUndo) {
            //        doc.UndoManager.Undo();
            //    }
            //});
            //keyMap.SetAction("cmd_Redo", delegate(object obj)
            //{
            //    if (doc.UndoManager.CanRedo) {
            //        doc.UndoManager.Redo();
            //    }
            //});
            //keyMap.SetAction("cmd_Paste", delegate(object obj)
            //{
            //    this.Paste();
            //});

            base.AllowDrop = true;

            Initialize();
        }

        public void Initialize() {
            doc.Clear();
            InitCaret();
            initWrap();
            udScr_tl_ = 0;
            udScr_vrl_ = 0;
        }

        void doc_TextUpdateEvent(CaretInfo s, CaretInfo e, CaretInfo e2) {
            UpDate(s, e, e2);
            on_text_update();
            base.Invalidate();
        }

        void AsTextEdit_HandleDestroyed(object sender, EventArgs e) {
            deleteDraw();
        }

        void imeComposition_ImeCompositedKata(object sender, ImeComposition.ImeCompositionEventArgs e) {
            Input(e.InputString);
        }

        void imeComposition_ImeCompositedHira(object sender, ImeComposition.ImeCompositionEventArgs e) {
            Input(e.InputString);
            base.Invalidate();
        }

        public new void Dispose() {
            Dispose(true);

            // システムにfinalize呼ばなくてもいいよ～って教える
            GC.SuppressFinalize(this);
        }

        protected override void Dispose(bool disposing) {
            if (disposing) {
                //Console.WriteLine("MyObject::Dispose マネージドなリソース開放");
            }
            //Console.WriteLine("MyObject::Dispose アンマネージドなリソース開放");
            //painter.Dispose();
            //deleteDraw();

            base.Dispose(disposing);
        }

        ~gcsTextEdit() {
            Dispose(false);
        }

        protected override void WndProc(ref Message m) {
            if (imeComposition != null)
                imeComposition.Ime(m, vPos.X, vPos.Y);

            base.WndProc(ref m);
        }

        protected override void OnMouseWheel(MouseEventArgs e) {
            base.OnMouseWheel(e);
            int dy = -e.Delta / 120;

            ScUpDown(dy);
            UpdateCaretPos();


            base.Invalidate();
        }

        protected override void OnSizeChanged(EventArgs e) {

            //initWrap();
            ReWrapAll();
            UpdateTextCx();

            vScrollBar.Left = this.Width - vScrollBar.Width;
            vScrollBar.Top = 0;
            vScrollBar.Height = this.Height - hScrollBar.Height;

            hScrollBar.Left = 0;
            hScrollBar.Top = this.Height - hScrollBar.Height;
            hScrollBar.Width = this.Width - vScrollBar.Width;

            scPanel.Left = vScrollBar.Left;
            scPanel.Top = hScrollBar.Top;

            ReSetScrollInfo();

            vScrollBar.Value = tl2vl(udScr_tl_);
            //udScr_tl_ = tl;
            udScr_vrl_ = 0;

            base.OnSizeChanged(e);
            base.Invalidate();
        }

        int tl2vl(int tl) {
            if (vlNum_ == doc.tln)
                return tl;

            int vl = 0;
            for (int i = 0; i < tl; ++i)
                vl += GetvlCnt(i);
            return vl;
        }

        void ScrollBar_Scroll(object sender, ScrollEventArgs e) {
            int d = e.NewValue - e.OldValue;
            if (d != 0) {
                if (e.ScrollOrientation == ScrollOrientation.HorizontalScroll) {
                    ScrollView(d, 0, true);

                    int x = 0;
                    x = -(hScrollBar.Value + d);
                    x += cur.vx;
                    vPos.X = x;
                } else if (e.ScrollOrientation == ScrollOrientation.VerticalScroll) {
                    ScUpDown(d);

                    int x = vRect.NumLineLeft;
                    int y = 0;
                    //y = -(vScrollBar.Value + d) * lineHeight;
                    y = -(vScrollBar.Value) * lineHeight;
                    x += cur.vx;
                    y += cur.vl * ViewLineHeight;
                    vPos.X = x;
                    vPos.Y = y;
                }
                base.Invalidate();
            }
        }

        private void ScUpDown(int dy) {
            // １．udScr_.nPos + dy が正常範囲に収まるように補正
            if (vScrollBar.Value + dy < 0)
                dy = -vScrollBar.Value;
            else if (vScrollBar.Maximum + 1 - vpage < vScrollBar.Value + dy)
                dy = vScrollBar.Maximum + 1 - vpage - vScrollBar.Value;
            if (dy == 0)
                return;

            int rl = dy + udScr_vrl_;
            int tl = udScr_tl_;

            if (dy < 0) // 上へ戻る場合
            {
                // ジャンプ先論理行の行頭へDash!
                while (rl < 0)
                    rl += GetvlCnt(--tl);
            } else if (dy > 0) {
                // ジャンプ先論理行の行頭へDash!
                while (rl > 0)
                    rl -= GetvlCnt(tl++);
                if (rl < 0)
                    rl += GetvlCnt(--tl); //行き過ぎ修正
            }
            udScr_tl_ = tl;
            udScr_vrl_ = rl;

            int y = -udScr_vrl_;
            tl = udScr_tl_;
            //int top = v.rc.top / H;
            //while (y + (signed)rln(tl) <= top)
            //    y += rln(tl++);

            // 縦座標
            vRect.YMIN = y * lineHeight;
            vRect.YMAX = this.Height;
            vRect.TLMIN = tl;

            vScrollBar.Value += dy;
        }

        public void GetOrigin(ref int x, ref int y) {
            x = vRect.NumLineLeft - hScrollBar.Value;
            y = -vScrollBar.Value * lineHeight;
        }

        private void GetDrawPosInfo(ref ViewRect v) {
            int H = lineHeight;

            int most_under = (vlNum_ - vScrollBar.Value) * H;
            if (most_under <= v.rc.Top) {
                v.YMIN = v.rc.Top;
                v.YMAX = most_under;
            } else {
                int y = -udScr_vrl_;
                int tl = udScr_tl_;
                int top = v.rc.Top / H;

                while (y + wrapList[tl].wrap.Count <= top)
                    y += wrapList[tl++].wrap.Count;


                // 縦座標
                v.YMIN = y * H;
                //v.YMAX = Math.Min(v.rc.Bottom, most_under);
                v.YMAX = Math.Min(ViewHeight, most_under);
                v.TLMIN = tl;

                // 横座標                
                v.XBASE = v.NumLineLeft - hScrollBar.Value;
                v.XMIN = v.rc.Left - v.XBASE;
                v.XMAX = v.rc.Right - v.XBASE;
            }

            // 選択範囲
            //v.SXB = v.SXE = v.SYB = v.SYE = 0x7fffffff;
            //const VPos *bg, *ed;
            //if( cur_.getCurPos( &bg, &ed ) )
            //{
            //    v.SXB = bg->vx - rlScr_.nPos + left();
            //    v.SXE = ed->vx - rlScr_.nPos + left();
            //    v.SYB = (bg->vl - udScr_.nPos) * H;
            //    v.SYE = (ed->vl - udScr_.nPos) * H;
            //}

            v.SXB = v.SXE = v.SYB = v.SYE = int.MaxValue;
            CaretInfo bg, ed;
            if (dostart) {
                v.SXB = sels.vx - hScrollBar.Value + v.NumLineLeft;
                v.SXE = sele.vx - hScrollBar.Value + v.NumLineLeft;
                v.SYB = (sels.vl - vScrollBar.Value) * H;
                v.SYE = (sele.vl - vScrollBar.Value) * H;
            } else if (getCurPos(out bg, out ed)) {
                //v.SXB = bg.vx - rlScr_.nPos + left();
                //v.SXE = ed.vx - rlScr_.nPos + left();
                //v.SXB = bg.vx;
                //v.SXE = ed.vx;

                v.SXB = bg.vx - hScrollBar.Value + v.NumLineLeft;
                v.SXE = ed.vx - hScrollBar.Value + v.NumLineLeft;
                v.SYB = (bg.vl - vScrollBar.Value) * H;
                v.SYE = (ed.vl - vScrollBar.Value) * H;
            }

        }

        public void on_text_update() {
            ReSetScrollInfo();
            ScrollTo(cur);
            UpdateCaretPos();
            cur.CopyTo(ref sel);
        }

        public void ReSetScrollInfo() {
            int cx = this.Width - (vRect.NumLineLeft + vScrollBar.Width);
            if (cx < 0) return;
            hpage = cx + 1;

            //rlScr_.nPage = cx + 1;
            //rlScr_.nMax = Max(textCx_ + 3, cx);
            //rlScr_.nPos = Min<int>(rlScr_.nPos, rlScr_.nMax - rlScr_.nPage + 1);

            hScrollBar.SmallChange = 1;
            hScrollBar.LargeChange = hpage;
            hScrollBar.Maximum = Math.Max(textCx_ + 3, cx);
            hScrollBar.Value = Math.Min(hScrollBar.Value, hScrollBar.Maximum - hpage + 1);

            int cy = ViewHeight;
            vpage = cy / lineHeight + 1;
            vScrollBar.SmallChange = 1;
            vScrollBar.LargeChange = vpage;
            vScrollBar.Maximum = vlNum_ + vpage - 2;
        }

        public void ScrollView(int dx, int dy, bool update) {
            if (dx != 0) {
                // 範囲チェック
                if (hScrollBar.Value + dx < 0)
                    dx = -hScrollBar.Value;
                else if (hScrollBar.Maximum - hpage < hScrollBar.Value + dx)
                    dx = hScrollBar.Maximum - hpage - hScrollBar.Value + 1;

                //rlScr_.nPos += dx;
                //::SetScrollInfo( hwnd_, SB_HORZ, &rlScr_, TRUE );
                hScrollBar.Value += dx;
                dx = -dx;
                base.Invalidate();
            }

            if (dy != 0) {
                // 範囲チェック…は前処理で終わってる。

                vScrollBar.Value += dy;
                //::SetScrollInfo( hwnd_, SB_VERT, &udScr_, TRUE );
                //dy *= -H;
                base.Invalidate();
            }
        }

        public void ScrollTo(CaretInfo vp) {
            // 横フォーカス
            int dx = 0;
            if (vp.vx < hScrollBar.Value) {
                dx = vp.vx - hScrollBar.Value;
            } else {
                //const int W = cvs_.getPainter().W();
                //if (rlScr_.nPos + (signed)(rlScr_.nPage - W) <= vp.vx)
                //    dx = vp.vx - (rlScr_.nPos + rlScr_.nPage) + W;

                //tmp
                const int W = 12;
                if (hScrollBar.Value + (hpage - W) <= vp.vx)
                    dx = vp.vx - (hScrollBar.Value + hpage) + W;
            }

            // 縦フォーカス
            int dy = 0;
            if (vp.vl < vScrollBar.Value)
                dy = vp.vl - vScrollBar.Value;
            else if (vScrollBar.Value + (vpage - 1) <= vp.vl)
                dy = vp.vl - (vScrollBar.Value + vpage) + 2;


            if (dx != 0) ScrollView(dx, 0, true);
            //if (dy != 0) UpDown(dy, dx == 0);
            if (dy != 0) UpDown(dy, true);
        }

        public void UpDown(int dy, bool thumb) {
            // １．udScr_.nPos + dy が正常範囲に収まるように補正
            if (vScrollBar.Value + dy < 0)
                dy = -vScrollBar.Value;
            else if (vScrollBar.Maximum + 1 - vpage < vScrollBar.Value + dy)
                dy = vScrollBar.Maximum + 1 - vpage - vScrollBar.Value;
            if (dy == 0)
                return;

            if (true) {
                int rl = dy + udScr_vrl_;
                int tl = udScr_tl_;

                if (dy < 0) // 上へ戻る場合
                {
                    // ジャンプ先論理行の行頭へDash!
                    while (rl < 0)
                        rl += GetvlCnt(--tl);
                } else if (dy > 0) // 下へ進む場合
                {
                    // ジャンプ先論理行の行頭へDash!
                    while (rl > 0)
                        rl -= GetvlCnt(tl++);
                    if (rl < 0)
                        rl += GetvlCnt(--tl); //行き過ぎ修正
                }
                udScr_tl_ = tl;
                udScr_vrl_ = rl;
            }

            ScrollView(0, dy, true);
        }

        public void ConvDPosToVPos(CaretInfo dp, ref CaretInfo vp) {
            CaretInfo dummyPos = null;
            this.ConvDPosToVPos(dp, ref vp, ref dummyPos);
        }

        public void ConvDPosToVPos(CaretInfo dp, ref CaretInfo vp, ref CaretInfo basePos) {
            if (object.Equals(basePos, null)) {
                basePos = new CaretInfo();
            }

            // とりあえずbase行頭の値を入れておく
            int vl = basePos.vl - basePos.rl;
            int rl = 0;
            int vx;

            // vlを合わせる
            int tl = basePos.tl;
            if (tl > dp.tl)      // 目的地が基準より上にある場合
            {
                do
                    vl -= wrapList[--tl].wrap.Count;
                while (tl > dp.tl);
            } else if (tl < dp.tl) // 目的地が基準より下にある場合
            {
                do
                    vl += wrapList[tl++].wrap.Count;
                while (tl < dp.tl);
            }

            // rlを合わせる
            int stt = 0;
            while (wrapList[tl].wrap[rl] < dp.ad)
                stt = wrapList[tl].wrap[rl++];
            vl += rl;

            // x座標計算
            vx = CalcStringWidth(doc.LineList[tl].Text, stt, dp.ad - stt);

            vp.tl = dp.tl;
            vp.ad = dp.ad;
            vp.vl = vl;
            vp.rl = rl;
            vp.rx = vp.vx = vx;
        }

        protected override void OnKeyPress(KeyPressEventArgs e) {
            if (IsInputChar(e.KeyChar) && this.ImeMode == ImeMode.Off) {
                Input(e.KeyChar.ToString());
                e.Handled = true;
            }
        }

        protected override void OnPreviewKeyDown(PreviewKeyDownEventArgs e) {
            switch (e.KeyCode) {
                case Keys.Enter:
                    //string ss = e.KeyValue.;
                    Input("\r\n");
                    //base.Invalidate();
                    break;
                case Keys.Tab:
                    Input("\t");
                    //base.Invalidate();
                    break;
                case Keys.Back:
                    DelBack();
                    //base.Invalidate();
                    break;
                case Keys.Delete:
                    Del();
                    break;
                case Keys.Right:
                    ConvDPosToVPos(CaretRight(1), ref cur);
                    CaretMove(e.Shift);
                    base.Invalidate();
                    break;
                case Keys.Left:
                    ConvDPosToVPos(CaretLeft(1), ref cur);
                    CaretMove(e.Shift);
                    base.Invalidate();
                    break;
                case Keys.Down:
                    Ud(1, e.Shift);
                    CaretMove(e.Shift);
                    base.Invalidate();
                    break;
                case Keys.Up:
                    Ud(-1, e.Shift);
                    CaretMove(e.Shift);
                    base.Invalidate();
                    break;
                case Keys.Home:
                    Home();
                    base.Invalidate();
                    break;
                //default:
                //    return base.IsInputKey(keyData);
            }

            KeyBind.getAction(e.Modifiers | e.KeyCode)(this);

            //if (e.Control && e.KeyCode == Keys.V)
            //{
            //    string vv = e.Modifiers.ToString() + "+" + e.KeyCode.ToString();

            //    string t = Clipboard.GetText();
            //    //doc.InsertString(cursorInfo.curPos, t, ref cursorInfo.curPos);
            //    //ConvPos(cursorInfo.curPos, ref cursorInfo.curPos);
            //    //cursorInfo.UpdateCaretPos();

            //    //CursorPos baseCurPos = new CursorPos(cursorInfo.cur);
            //    //doc.Insert(cursorInfo.cur, ref baseCurPos, t);
            //    //ConvDPosToVPos(baseCurPos, ref cursorInfo.cur);
            //    //on_text_update();
            //    Input(t);
            //    base.Invalidate();
            //}
            //else if (e.Control && e.KeyCode == Keys.Z)
            //string k = e.Modifiers.ToString() + "+" + e.KeyCode.ToString();

            //if (e.Modifiers != Keys.None)
            //{
            //    string k = e.Modifiers.ToString() + "+" + e.KeyCode.ToString();
            //    if (keyMap.ContainsFunction(k))
            //    {
            //        keyMap.getFunction(k)(this);
            //    }
            //}
            base.OnPreviewKeyDown(e);
        }

        protected override void OnKeyUp(KeyEventArgs e) {
            base.OnKeyUp(e);
        }

        protected override bool ProcessDialogKey(Keys keyData) {
            return false;
        }

        protected override void OnPaint(PaintEventArgs e) {
            //base.OnPaint(e);

            GetDrawPosInfo(ref vRect);

            drawDoc(e.Graphics, vRect);
            drawCaret(e.Graphics, vPos.X, vPos.Y);
            if (showNumLine)
                drawLNA(e.Graphics, vRect);
        }

        protected override bool IsInputChar(char charCode) {
            //return (!char.IsControl(charCode)) || (charCode == '\t');
            return !char.IsControl(charCode);
        }

        private Boolean ldowm = false;
        private CaretInfo sels, sele;
        protected override void OnMouseDown(MouseEventArgs e) {
            Focus();

            if (e.Button == MouseButtons.Left) {
                if (cur != sel) {
                    CaretInfo c = new CaretInfo();
                    GetVPos(e.X, e.Y, ref c);

                    if (Insel(c)) {
                        ldowm = true;
                        dostart = true;

                        sels = new CaretInfo(cur);
                        sele = new CaretInfo(sel);
                        CorrectPos(ref sels, ref sele);

                    } else {
                        GetVPos(e.X, e.Y, ref cur);

                        int ad = cur.ad;
                        int tl = cur.tl;

                        ScrollTo(cur);
                        UpdateCaretPos();

                        cur.CopyTo(ref sel);

                        base.Invalidate();

                        ldowm = true;
                        dostart = false;
                    }
                } else {
                    GetVPos(e.X, e.Y, ref cur);

                    //int ad = cur.ad;
                    //int tl = cur.tl;

                    ScrollTo(cur);
                    UpdateCaretPos();

                    cur.CopyTo(ref sel);

                    base.Invalidate();

                    ldowm = true;
                    dostart = false;
                }
            }
            base.OnMouseDown(e);
        }

        private void CorrectPos(ref CaretInfo s, ref CaretInfo e) {
            // 必ずs<=eになるように修正
            if (s > e) {
                int tmp;
                tmp = s.ad; s.ad = e.ad; e.ad = tmp;
                tmp = s.tl; s.tl = e.tl; e.tl = tmp;
            }
        }

        private Boolean Insel(CaretInfo caret) {
            if (cur < sel) {
                if (cur <= caret && caret <= sel) {
                    return true;
                }
            } else if (cur > sel) {
                if (sel <= caret && caret <= cur) {
                    return true;
                }
            }
            return false;
        }

        private Boolean Insel(CaretInfo caret, CaretInfo selStart, CaretInfo selEnd) {
            if (selStart < selEnd) {
                if (selStart <= caret && caret <= selEnd) {
                    return true;
                }
            } else if (selStart > selEnd) {
                if (selEnd <= caret && caret <= selStart) {
                    return true;
                }
            }
            return false;
        }

        protected override void OnMouseUp(MouseEventArgs e) {
            if (dostart) {
                GetVPos(e.X, e.Y, ref cur);
                UpdateCaretPos();
                cur.CopyTo(ref sel);

                base.Invalidate();
            }
            ldowm = false;
            dostart = false;

            base.OnMouseUp(e);
        }
        Boolean dostart = false;
        protected override void OnMouseMove(MouseEventArgs e) {
            base.OnMouseMove(e);
            if (ldowm) {
                //if (cur == sel)
                //{
                //    Point p = this.PointToClient(MousePosition);
                //    GetVPos(p.X, p.Y, ref cur);
                //    CaretMove(true);
                //    this.Invalidate();
                //}
                if (dostart) {
                    //dostart = true;
                    DoDragDrop("test", DragDropEffects.Move);
                } else {
                    Point p = this.PointToClient(MousePosition);
                    GetVPos(p.X, p.Y, ref cur);
                    CaretMove(true);
                    this.Invalidate();
                }
            }
        }

        protected override void OnMouseEnter(EventArgs e) {
            Cursor = Cursors.IBeam;
            base.OnMouseEnter(e);
        }

        protected override void OnMouseLeave(EventArgs e) {
            Cursor = Cursors.Default;
            base.OnMouseLeave(e);
        }

        public void GetVPos(int x, int y, ref CaretInfo curPos) {
            // x座標補正
            x = x - vRect.NumLineLeft + hScrollBar.Value;

            int tl = udScr_tl_;
            int vl = vScrollBar.Value - udScr_vrl_;
            int rl = y / lineHeight + udScr_vrl_;
            if (rl >= 0) // View上端より下の場合、下方向を調べる
                while (tl < doc.LineList.Count && GetvlCnt(tl) <= rl) {
                    vl += GetvlCnt(tl);
                    rl -= GetvlCnt(tl);
                    ++tl;
                } else           // View上端より上の場合、上方向を調べる
                while (0 <= tl && rl < 0) {
                    vl -= GetvlCnt(tl);
                    rl += GetvlCnt(tl);
                    --tl;
                }

            if (tl == doc.LineList.Count) // EOFより下に行ってしまう場合の補正
	        {
                --tl;
                vl -= GetvlCnt(tl);
                rl = GetvlCnt(tl) - 1;
            } else if (tl == -1) // ファイル頭より上に行ってしまう場合の補正
	        {
                tl = vl = rl = 0;
            }

            curPos.tl = tl;
            curPos.vl = vl + rl;
            curPos.rl = rl;

            if (rl < GetvlCnt(tl)) {
                IBuffer buf = doc.LineList[tl].Text;
                int adend = wrapList[tl].wrap[rl];
                int ad = (rl == 0 ? 0 : wrapList[tl].wrap[rl - 1]);
                int vx = (rl == 0 ? 0 : CalcStringWidth(buf, ad++, 1));

                while (ad < adend) {
                    int nvx = vx + CalcStringWidth(buf, ad, 1);
                    if (x + 2 < nvx)
                        break;
                    vx = nvx;
                    ++ad;
                }

                curPos.ad = ad;
                curPos.rx = curPos.vx = vx;
            } else {
                curPos.ad = curPos.rx = curPos.vx = 0;
            }
        }

        protected override void OnDragDrop(DragEventArgs drgevent) {
            isdropfile = false;
            ldowm = false;
            cur.CopyTo(ref sel);

            if (drgevent.Data.GetDataPresent(DataFormats.FileDrop)) {

                string[] files = (string[])drgevent.Data.GetData(DataFormats.FileDrop, false);

            } else if (drgevent.Data.GetDataPresent(typeof(string))) {
                dostart = false;
                if (!Insel(cur, sels, sele)) {
                    string ss = (string)drgevent.Data.GetData(DataFormats.UnicodeText);
                    ss = GetText(sels, sele).ToString();

                    if (cur < sels) {
                        Input(ss);
                        CaretInfo s = new CaretInfo(cur);
                        Del(sels, sele);
                        s.CopyTo(ref cur);
                        cur.CopyTo(ref sel);
                    } else if (cur > sele) {
                        Input(ss);
                        CaretInfo s = new CaretInfo(cur);
                        int nn = vlNum_;
                        int t = sels.tl - sele.tl;
                        int dad = sele.ad - sels.ad;
                        Del(sels, sele);

                        if (s.tl == cur.tl) {
                            s.ad -= dad;
                        }
                        s.vl += vlNum_ - nn;
                        s.tl += t;

                        ConvDPosToVPos(s, ref cur, ref s);

                        //s.CopyTo(ref cur);
                        cur.CopyTo(ref sel);


                    }
                    UpdateCaretPos();
                }
            }
            //base.OnDragDrop(drgevent);

        }

        protected override void OnDragOver(DragEventArgs drgevent) {
            if (!Focused)
                Focus();

            //base.OnDragOver(drgevent);
            Point p = this.PointToClient(new Point(drgevent.X, drgevent.Y));
            GetVPos(p.X, p.Y, ref cur);
            ScrollTo(cur);
            UpdateCaretPos();
            base.Invalidate();
        }

        private bool isdropfile = false;
        protected override void OnDragEnter(DragEventArgs drgevent) {
            if (drgevent.Data.GetDataPresent(DataFormats.FileDrop)) {
                drgevent.Effect = DragDropEffects.Copy;
                isdropfile = true;
            } else if (drgevent.Data.GetDataPresent(DataFormats.UnicodeText)
                || drgevent.Data.GetDataPresent(DataFormats.Text)) {
                drgevent.Effect = DragDropEffects.Move;
            } else {
                drgevent.Effect = DragDropEffects.None;
            }
            base.OnDragEnter(drgevent);
        }

        public bool SearchText(string sh) {
            Search search = new Search(this.doc);
            search.searchstr = sh;
            CaretInfo bgn = new CaretInfo(cur);
            CaretInfo end = new CaretInfo(cur);

            if (search.FindNext(cur, ref bgn, ref end)) {
                ConvDPosToVPos(bgn, ref cur);
                ConvDPosToVPos(end, ref sel);
                CaretMove(true);
                base.Invalidate();
                return true;
            }

            return false;
        }

        public void Home() {
            CaretInfo np = new CaretInfo();
            if (cur.rl == 0) {
                np.tl = cur.tl;
                np.vl = cur.vl - cur.rl;
            } else {
                CaretInfo np2 = new CaretInfo();
                np2.tl = cur.tl;
                np2.ad = wrapList[cur.tl].wrap[cur.rl - 1];
                ConvDPosToVPos(np2, ref np, ref cur);
            }

            ConvDPosToVPos(np, ref cur);
            CaretMove(true);
        }

        public void End() {

        }

        public void Clear() {

        }

        public void Copy() {

        }

        public void Cut() {
            string buff;
            doc.Delete(cur, sel, out buff);
            Clipboard.SetText(buff);
        }

        //public void Paste()
        //{
        //    string t = Clipboard.GetText();
        //    Input(t);
        //    base.Invalidate();
        //}

        #region ITextEditor メンバ

        public void Paste() {
            string t = Clipboard.GetText();
            Input(t);
            base.Invalidate();
        }

        #endregion
    }
}
