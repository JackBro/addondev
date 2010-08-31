using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Text.RegularExpressions;
using System.Threading;

namespace AsControls {

    public delegate Action<object, WrapType> WrapModeChangeEventHandler(object sender, WrapType wrapMode);

    public partial class gcsTextEdit : Control, ITextEditor {
        
        private class VDrawInfo {
            public Rectangle rc;
            public int XBASE;      // 一番左の文字のx座標
            public int XMIN;       // テキスト再描画範囲左端
            public int XMAX;       // テキスト再描画範囲右端
            public int YMIN;       // テキスト再描画範囲上端
            public int YMAX;       // テキスト再描画範囲下端
            public int TLMIN;      // テキスト再描画範囲上端論理行番号
            public int SXB, SXE;   // 選択範囲のx座標
            public int SYB, SYE;   // 選択範囲のy座標

            public VDrawInfo() {
                rc = new Rectangle();
                YMIN = 0;
                YMAX = 0;
                TLMIN = 0;
                SXB = SXE = SYB = SYE = 0;
            }
        }

        VDrawInfo vRect = new VDrawInfo();

        VGcsScrollBar vScrollBar;
        //int vpage = 0;
        HGcsScrollBar hScrollBar;
        //int hpage = 0;

        int udScr_tl_;  //一番上に表示される論理行のTLine_Index
        int udScr_vrl_; //一番上に表示される表示行のVRLine_Index


        //Panel EditorPanel = new Panel();

        ImeComposition imeComposition;

        //WrapType wrapType;
        //Boolean showNumLine = false;

        public KeyMap KeyBind { get; set; }

        //public int ViewWidth {
        //    get {
        //        if (wrapType == WrapType.Non) {
        //            return int.MaxValue;
        //        } else {
        //            if (this.Width - (vRect.NumLineLeft + vScrollBar.Width) < 0)
        //                return 0;
        //            return this.Width - (vRect.NumLineLeft + vScrollBar.Width);
        //        }
        //    }
        //}

        //public int ViewHeight {
        //    get {
        //        if (this.Height - hScrollBar.Height < 0)
        //            return 0;

        //        return this.Height - hScrollBar.Height;
        //    }
        //}

        //public int ViewLineHeight {
        //    get { return lineHeight; }
        //}

        //public new Font Font {
        //    get { return base.Font; }
        //    set {
        //        base.Font = value;
        //        initDraw();
        //    }
        //}

        //public WrapType Wrap {
        //    get { return wrapType; }
        //    set {
        //        wrapType = value;
        //        //ReWrapAll();
        //    }
        //}

        public new string Text {
            set {
                this.doc_.Clear();
                //InitCaret();
                //initWrap();
                //udScr_tl_ = 0;
                //udScr_vrl_ = 0;
                //hpage = 0;
                //vpage = 0;
                Initialize();
                cur_.Input(value, value.Length);
            }

            get {
                return this.doc_.ToString();
            }
        }

        //private Document Document {
        //    get { return this.doc; }
        //    set {
        //        if (doc != null) {
        //            doc.TextUpdateEvent -= doc_TextUpdateEvent;
        //        }
        //        doc = value;
        //        doc.TextUpdateEvent += new TextUpdateEventHandler(doc_TextUpdateEvent);
        //        //initWrap();
        //        //ReWrapAll();
        //        //string sss = doc.ToString();
        //        //Input(sss);
        //        ResizeWrapList(doc.tlNum);
        //    }
        //}

        //
        private Canvas cvs_;
	    public Rectangle zone()  { return cvs_.zone(); }
        //public int left() { return cvs_.zone().Left; }
        //public int right() { return cvs_.zone().Right - vScrollBar.Width; }
        //public int bottom() { return cvs_.zone().Bottom - hScrollBar.Height; }
        //public int lna() { return cvs_.zone().Left; }
        //public int cx() { return cvs_.zone().Right - cvs_.zone().Left - vScrollBar.Width; }
        //public int cxAll() { return cvs_.zone().Right; }
        //public int cy() { return cvs_.zone().Bottom - hScrollBar.Height; }

        //TODO
        public int left() { return cvs_.zone().Left; }
        public int right() { return cvs_.zone().Right; }
        public int bottom() { return cvs_.zone().Bottom; }
        public int lna() { return cvs_.zone().Left; }
        public int cx() { return cvs_.zone().Right - cvs_.zone().Left; }
        public int cxAll() { return cvs_.zone().Right; }
        public int cy() { return cvs_.zone().Bottom; }
        //
        private Document doc_;
        //
        public Painter fnt() { return cvs_.getPainter(); }
        //
        private Cursor cur_;
        //
        public enum ReDrawType {
            LNAREA, // 行番号ゾーンのみ
            LINE,   // 変更のあった一行のみ
            AFTER,  // 変更のあった行以下全部
            ALL     // 全画面
        }

        public gcsTextEdit() {
            this.SetStyle(ControlStyles.OptimizedDoubleBuffer | ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint, true);

            //EditorPanel = new Panel();
            ////EditorPanel.SetStyle(ControlStyles.OptimizedDoubleBuffer | ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint, true);
            //EditorPanel.BackColor = this.BackColor;
            ////EditorPanel.Size = new Size(hScrollBar.Height, vScrollBar.Width);
            //this.Controls.Add(EditorPanel);

            //wrapType = WrapType.WindowWidth;
            //wrapType = WrapType.Non;
            //showNumLine = true;



            //initDraw();
            //initWrap();

            //udScr_tl_ = 0;
            //udScr_vrl_ = 0;

            vScrollBar = new VGcsScrollBar();
            vScrollBar.Scroll += new ScrollEventHandler(ScrollBar_Scroll);
            //vScrollBar.Dock = DockStyle.Right;
            this.Controls.Add(vScrollBar);

            hScrollBar = new HGcsScrollBar();
            hScrollBar.Scroll += new ScrollEventHandler(ScrollBar_Scroll);
            //hScrollBar.Dock = DockStyle.Bottom;
            this.Controls.Add(hScrollBar);

            //scPanel.BackColor = this.BackColor;
            //scPanel.Size = new Size(hScrollBar.Height, vScrollBar.Width);
            //this.Controls.Add(scPanel);

            this.ImeMode = ImeMode.Off;
            imeComposition = new ImeComposition(this);
            imeComposition.ImeCompositedHira += new ImeComposition.ImeCompositionEventHandler(imeComposition_ImeCompositedHira);
            imeComposition.ImeCompositedKata += new ImeComposition.ImeCompositionEventHandler(imeComposition_ImeCompositedKata);

            this.HandleDestroyed += new EventHandler(AsTextEdit_HandleDestroyed);
            this.LostFocus += (sender, e) => {
                cur_.on_killfocus();
            };
            this.GotFocus += (sender, e) => {
                cur_.on_setfocus();
            };
            this.KeyDown += (sender, e) => {
                
            };
            this.VisibleChanged += new EventHandler(gcsTextEdit_VisibleChanged);

            KeyBind = new KeyMap();

            doc_ = new Document();
            doc_.TextUpdateEvent += new TextUpdateEventHandler(doc_TextUpdateEvent);
            //
            Config config = new Config();
            //config.setFontInfo(this.Font.Name, this.Font.Size);
            config.setFont(this.Font);
            cvs_ = new Canvas(this, config);
            cur_ = new Cursor(this, doc_, new Caret(this.Handle));
            base.AllowDrop = true;

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

            Initialize();


            Parser.Parser p = new Parser.Parser();
            //p.init();
            var res = p.parseLine("test//test");
            int i = 0;

        }

        void gcsTextEdit_VisibleChanged(object sender, EventArgs e) {
            if (this.Visible) {
                DoResize(cvs_.on_view_resize(this.ClientSize.Width - vScrollBar.Width, this.ClientSize.Height - hScrollBar.Height));
                this.VisibleChanged -= gcsTextEdit_VisibleChanged;
            }
        }

        public void Initialize() {
            //doc.Clear();
            //InitCaret();
            //initWrap();
            wrap_ = new List<WLine>();

            // 適当に折り返し情報初期化
            InsertMulti(0, doc_.tln() - 1);

            udScr_tl_ = 0;
            udScr_vrl_ = 0;
            ReSetScrollInfo();
        }

        //public new Rectangle ClientRectangle {
        //    get {
        //        int w = base.ClientRectangle.Width - vScrollBar.Width;
        //        int h = base.ClientRectangle.Height - hScrollBar.Height;

        //        Rectangle rec = new Rectangle(base.ClientRectangle.Location, new Size(w < 0 ? 0 : w, h < 0 ? 0 : h));
        //        //return base.ClientRectangle;
        //        return rec;
        //    }
        //}

        internal Rectangle getClientRect() {
            return this.ClientRectangle;
            //throw new NotImplementedException();
            //Rectangle crect = this.ClientRectangle;
            ////Rectangle rec = new Rectangle(crect.Location, new Size(crect.Width - vScrollBar.Width, crect.Height-hScrollBar.Height));
            //Rectangle rec = new Rectangle(crect.Location, new Size(crect.Width - vScrollBar.Width, crect.Height - hScrollBar.Height));
            //return EditorPanel.ClientRectangle;
        }

        void doc_TextUpdateEvent(VPos s, VPos e, VPos e2) {
            //UpDate(s, e, e2);
            on_text_update(s, e, e2, true, true);
            
            //base.Invalidate();
        }

        void AsTextEdit_HandleDestroyed(object sender, EventArgs e) {
            //deleteDraw();
        }

        void imeComposition_ImeCompositedKata(object sender, ImeComposition.ImeCompositionEventArgs e) {
            //Input(e.InputString);
        }

        void imeComposition_ImeCompositedHira(object sender, ImeComposition.ImeCompositionEventArgs e) {
            //Input(e.InputString);
            //base.Invalidate();
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
            cvs_.Dispose();
            base.Dispose(disposing);
        }

        ~gcsTextEdit() {
            Dispose(false);
        }

        protected override void WndProc(ref Message m) {
            //if (imeComposition != null)
            //    imeComposition.Ime(m, vPos.X, vPos.Y);
            
            base.WndProc(ref m);
        }

        protected override void OnMouseWheel(MouseEventArgs e) {
            base.OnMouseWheel(e);
            int dy = -e.Delta / 120;
            //ScUpDown(dy);
            //UpdateCaretPos();
            //base.Invalidate();
            UpDown(dy * 3, false);
        }

        protected override void OnSizeChanged(EventArgs e) {

            ////initWrap();
            //ReWrapAll();
            //UpdateTextCx();

            //vScrollBar.Left = this.Width - vScrollBar.Width;
            //vScrollBar.Top = 0;
            //vScrollBar.Height = this.Height - hScrollBar.Height;

            //hScrollBar.Left = 0;
            //hScrollBar.Top = this.Height - hScrollBar.Height;
            //hScrollBar.Width = this.Width - vScrollBar.Width;

            //scPanel.Left = vScrollBar.Left;
            //scPanel.Top = hScrollBar.Top;

            //ReSetScrollInfo();

            //ForceScrollTo(udScr_tl_);

            //ResetPos();

            base.OnSizeChanged(e);

            DoResize(cvs_.on_view_resize(this.ClientSize.Width-vScrollBar.Width, this.ClientSize.Height-hScrollBar.Height));
            //DoResize(cvs_.on_view_resize(this.ClientSize.Width, this.ClientSize.Height));
            //base.Invalidate();
        }

        //public new Size ClientSize {
        //    get {
        //        return new Size(base.ClientSize.Width - vScrollBar.Width, base.ClientSize.Height - hScrollBar.Height);
        //    }
        //}

        //int tl2vl(int tl) {
        //    if (vlNum_ == doc.tln)
        //        return tl;

        //    int vl = 0;
        //    for (int i = 0; i < tl; ++i)
        //        vl += GetvlCnt(i);
        //    return vl;
        //}

        //void ForceScrollTo(int tl) {
        //    vScrollBar.Value = tl2vl(udScr_tl_);
        //    udScr_tl_ = tl;
        //    udScr_vrl_ = 0;
        //}

        //void ScrollBar_Scroll(object sender, ScrollEventArgs e) {
        //    int d = e.NewValue - e.OldValue;
        //    if (d != 0) {
        //        if (e.ScrollOrientation == ScrollOrientation.HorizontalScroll) {
        //            ScrollView(d, 0, true);

        //            int x = 0;
        //            x = -(hScrollBar.Value + d);
        //            x += cur.vx;
        //            vPos.X = x;
        //        } else if (e.ScrollOrientation == ScrollOrientation.VerticalScroll) {
        //            ScUpDown(d);

        //            int x = vRect.NumLineLeft;
        //            int y = 0;
        //            //y = -(vScrollBar.Value + d) * lineHeight;
        //            y = -(vScrollBar.Value) * lineHeight;
        //            x += cur.vx;
        //            y += cur.vl * ViewLineHeight;
        //            vPos.X = x;
        //            vPos.Y = y;
        //        }
        //        base.Invalidate();
        //    }
        //}

        //private void ScUpDown(int dy) {
        //    // １．udScr_.nPos + dy が正常範囲に収まるように補正
        //    if (vScrollBar.Value + dy < 0)
        //        dy = -vScrollBar.Value;
        //    else if (vScrollBar.Maximum + 1 - vpage < vScrollBar.Value + dy)
        //        dy = vScrollBar.Maximum + 1 - vpage - vScrollBar.Value;
        //    if (dy == 0)
        //        return;

        //    int rl = dy + udScr_vrl_;
        //    int tl = udScr_tl_;

        //    if (dy < 0) // 上へ戻る場合
        //    {
        //        // ジャンプ先論理行の行頭へDash!
        //        while (rl < 0)
        //            rl += GetvlCnt(--tl);
        //    } else if (dy > 0) {
        //        // ジャンプ先論理行の行頭へDash!
        //        while (rl > 0)
        //            rl -= GetvlCnt(tl++);
        //        if (rl < 0)
        //            rl += GetvlCnt(--tl); //行き過ぎ修正
        //    }
        //    udScr_tl_ = tl;
        //    udScr_vrl_ = rl;

        //    int y = -udScr_vrl_;
        //    tl = udScr_tl_;
        //    //int top = v.rc.top / H;
        //    //while (y + (signed)rln(tl) <= top)
        //    //    y += rln(tl++);

        //    // 縦座標
        //    vRect.YMIN = y * lineHeight;
        //    vRect.YMAX = this.Height;
        //    vRect.TLMIN = tl;

        //    vScrollBar.Value += dy;
        //}

        //
        public void GetOrigin(ref int x, ref int y) {
            //x = vRect.NumLineLeft - hScrollBar.Value;
            //y = -vScrollBar.Value * lineHeight;
            x = left() - hScrollBar.Value;
            y = -vScrollBar.Value * cvs_.getPainter().H();
        }

        //public void on_text_update() {
        //    //ReSetScrollInfo();
        //    //ScrollTo(cur);
        //    //UpdateCaretPos();
        //    //cur.CopyTo(ref sel);
        //}

        //public void ReSetScrollInfo() {
        //    int cx = this.Width - (vRect.NumLineLeft + vScrollBar.Width);
        //    if (cx < 0) return;
        //    hpage = cx + 1;

        //    //rlScr_.nPage = cx + 1;
        //    //rlScr_.nMax = Max(textCx_ + 3, cx);
        //    //rlScr_.nPos = Min<int>(rlScr_.nPos, rlScr_.nMax - rlScr_.nPage + 1);

        //    hScrollBar.SmallChange = 1;
        //    hScrollBar.LargeChange = hpage;
        //    hScrollBar.Maximum = Math.Max(textCx_ + 3, cx);
        //    hScrollBar.Value = Math.Min(hScrollBar.Value, hScrollBar.Maximum - hpage + 1);

        //    int cy = ViewHeight;
        //    vpage = cy / lineHeight + 1;
        //    vScrollBar.SmallChange = 1;
        //    vScrollBar.LargeChange = vpage;
        //    vScrollBar.Maximum = vlNum_ + vpage - 2;
        //}



        //public void ScrollTo(CaretInfo vp) {
        //    // 横フォーカス
        //    int dx = 0;
        //    if (vp.vx < hScrollBar.Value) {
        //        dx = vp.vx - hScrollBar.Value;
        //    } else {
        //        //const int W = cvs_.getPainter().W();
        //        //if (rlScr_.nPos + (signed)(rlScr_.nPage - W) <= vp.vx)
        //        //    dx = vp.vx - (rlScr_.nPos + rlScr_.nPage) + W;

        //        //tmp
        //        const int W = 12;
        //        if (hScrollBar.Value + (hpage - W) <= vp.vx)
        //            dx = vp.vx - (hScrollBar.Value + hpage) + W;
        //    }

        //    // 縦フォーカス
        //    int dy = 0;
        //    if (vp.vl < vScrollBar.Value)
        //        dy = vp.vl - vScrollBar.Value;
        //    else if (vScrollBar.Value + (vpage - 1) <= vp.vl)
        //        dy = vp.vl - (vScrollBar.Value + vpage) + 2;


        //    if (dx != 0) ScrollView(dx, 0, true);
        //    //if (dy != 0) UpDown(dy, dx == 0);
        //    if (dy != 0) UpDown(dy, true);
        //}

        //public void ScrollTo(VPos vp) {
        //    // 横フォーカス
        //    int dx = 0;
        //    if (vp.vx < hScrollBar.Value) {
        //        dx = vp.vx - hScrollBar.Value;
        //    }
        //    else {
        //        int W = cvs_.getPainter().W();
        //        if (hScrollBar.Value + (hScrollBar.nPage - W) <= vp.vx)
        //            dx = vp.vx - (hScrollBar.Value + hScrollBar.nPage) + W;

        //        ////tmp
        //        //const int W = 12;
        //        //if (hScrollBar.Value + (hpage - W) <= vp.vx)
        //        //    dx = vp.vx - (hScrollBar.Value + hpage) + W;
        //    }

        //    // 縦フォーカス
        //    int dy = 0;
        //    //ulong vnPos = (ulong)vScrollBar.Value;
        //    if (vp.vl < vScrollBar.Value)
        //        dy = (int)(vp.vl - vScrollBar.Value);
        //    else if (vScrollBar.Value + (vpage - 1) <= vp.vl)
        //        dy = vp.vl - (vScrollBar.Value + vpage) + 2;


        //    if (dx != 0) ScrollView(dx, 0, true);
        //    //if (dy != 0) UpDown(dy, dx == 0);
        //    if (dy != 0) UpDown(dy, true);
        //}

        //public void UpDown(int dy, bool thumb) {
        //    // １．udScr_.nPos + dy が正常範囲に収まるように補正
        //    if (vScrollBar.Value + dy < 0)
        //        dy = -vScrollBar.Value;
        //    else if (vScrollBar.Maximum + 1 - vpage < vScrollBar.Value + dy)
        //        dy = vScrollBar.Maximum + 1 - vpage - vScrollBar.Value;
        //    if (dy == 0)
        //        return;

        //    if (true) {
        //        int rl = dy + udScr_vrl_;
        //        int tl = udScr_tl_;

        //        if (dy < 0) // 上へ戻る場合
        //        {
        //            // ジャンプ先論理行の行頭へDash!
        //            while (rl < 0)
        //                rl += GetvlCnt(--tl);
        //        } else if (dy > 0) // 下へ進む場合
        //        {
        //            // ジャンプ先論理行の行頭へDash!
        //            while (rl > 0)
        //                rl -= GetvlCnt(tl++);
        //            if (rl < 0)
        //                rl += GetvlCnt(--tl); //行き過ぎ修正
        //        }
        //        udScr_tl_ = tl;
        //        udScr_vrl_ = rl;
        //    }

        //    ScrollView(0, dy, true);
        //}

        //
        VPos dummyVPos = new VPos();
        public void ConvDPosToVPos(DPos dp, ref VPos vp) {
            dummyVPos.ad = -1;
            ConvDPosToVPos(dp, ref vp, ref dummyVPos);
        }
        //
        public void ConvDPosToVPos(DPos dp, ref VPos vp, ref VPos basevp) {

            // 補正
            dp.tl = Math.Min(dp.tl, doc_.tln() - 1);
            dp.ad = Math.Min(dp.ad, doc_.len(dp.tl));

            VPos topPos = new VPos();
            //if (basevp == null) {
            if (basevp.ad <0) {
                basevp = topPos;
            }

            // とりあえずbase行頭の値を入れておく
            int vl = basevp.vl - basevp.rl;
            int rl = 0;
            int vx;

            // 違う行だった場合
            // vlを合わせる
            int tl = basevp.tl;
            if (tl > dp.tl) {      // 目的地が基準より上にある場合
                do {
                    vl -= rln(--tl);
                } while (tl > dp.tl);
            }
            else if (tl < dp.tl) { // 目的地が基準より下にある場合
                do {
                    vl += rln(tl++);// wrapList[tl++].wrap.Count;
                } while (tl < dp.tl);
            }

            // rlを合わせる
            int stt = 0;
            //while (wrapList[tl].wrap[rl] < dp.ad)
            //    stt = wrapList[tl].wrap[rl++];
            while (wrap_[tl][rl + 1] < dp.ad)
                stt = wrap_[tl][++rl];
            vl += rl;

            // x座標計算
            ///vx = CalcStringWidth(doc.LineList[tl].Text, stt, dp.ad - stt);

            //vx = fnt().CalcStringWidth(doc_.tl(tl), stt, (dp.ad - stt));
            vx = stt==(dp.ad - stt)?0:fnt().CalcStringWidth(doc_.tl(tl).Substring(stt, (dp.ad - stt)).ToString()); //TODO
            //vx = CalcLineWidth(doc_.tl(tl), stt, dp.ad - stt);

            vp.tl = dp.tl;
            vp.ad = dp.ad;
            vp.vl = vl;
            vp.rl = rl;
            vp.rx = vp.vx = vx;
        }
        //
        public int GetLastWidth(int tl) {
            if (rln(tl) == 1)
                return wrap_[tl][0];

            int beg = rlend(tl, rln(tl) - 2);
            string text = doc_.tl(tl).Substring(beg).ToString();
            return CalcLineWidth(text, doc_.len(tl) - beg);
        }

        //
        private int CalcLineWidth(string txt, int len) {
            // 行を折り返さずに書いたときの横幅を計算する
            // ほとんどの行が折り返し無しで表示されるテキストの場合、
            // この値を計算しておくことで、処理の高速化が可能。
            Painter p = cvs_.getPainter();
            int w=0;
            for( int i=0; i<len; ++i )
                if( txt[i] == '\t' )
                    w = p.nextTab(w);
                else
                    w += p.W( txt[i] );
            return w;
        }

        protected override void OnKeyPress(KeyPressEventArgs e) {
            if (IsInputChar(e.KeyChar) && this.ImeMode == ImeMode.Off) {
                //Input(e.KeyChar.ToString());
                cur_.InputChar(e.KeyChar);
                e.Handled = true;
            }else if(e.KeyChar == '\r' ){
                cur_.Input("\r\n", 2);
                e.Handled = true;
            }
        }

        protected override void OnPreviewKeyDown(PreviewKeyDownEventArgs e) {
            //switch (e.KeyCode) {
            //    case Keys.Enter:
            //        //string ss = e.KeyValue.;
            //        Input("\r\n");
            //        //base.Invalidate();
            //        break;
            //    case Keys.Tab:
            //        Input("\t");
            //        //base.Invalidate();
            //        break;
            //    case Keys.Back:
            //        DelBack();
            //        //base.Invalidate();
            //        break;
            //    case Keys.Delete:
            //        Del();
            //        break;
            //    case Keys.Right:
            //        ConvDPosToVPos(CaretRight(1), ref cur);
            //        MoveTo(e.Shift);
            //        base.Invalidate();
            //        break;
            //    case Keys.Left:
            //        ConvDPosToVPos(CaretLeft(1), ref cur);
            //        MoveTo(e.Shift);
            //        base.Invalidate();
            //        break;
            //    case Keys.Down:
            //        Ud(1, e.Shift);
            //        MoveTo(e.Shift);
            //        base.Invalidate();
            //        break;
            //    case Keys.Up:
            //        Ud(-1, e.Shift);
            //        MoveTo(e.Shift);
            //        base.Invalidate();
            //        break;
            //    case Keys.Home:
            //        Home();
            //        base.Invalidate();
            //        break;
            //    //default:
            //    //    return base.IsInputKey(keyData);
            //}

            KeyBind.getAction(e.Modifiers | e.KeyCode)(this);
            base.OnPreviewKeyDown(e);
        }

        protected override void OnKeyUp(KeyEventArgs e) {
            base.OnKeyUp(e);
        }

        protected override bool ProcessDialogKey(Keys keyData) {
            return false;
        }

        //
        protected override void OnPaint(PaintEventArgs e) {
            base.OnPaint(e);

            //GetDrawPosInfo(ref vRect);

            //drawDoc(e.Graphics, vRect);
            //drawCaret(e.Graphics, vPos.X, vPos.Y);
            //if (showNumLine)
            //    drawLNA(e.Graphics, vRect);
            
            Painter p = cvs_.getPainter();
            vRect.rc = e.ClipRectangle;
            GetDrawPosInfo(ref vRect);

            if (e.ClipRectangle.Right <= lna()) {
                // case A: 行番号表示域のみ更新
                DrawLNA(e.Graphics, vRect, p);
            } else if (lna() <= e.ClipRectangle.Left) {
                // case B: テキスト表示域のみ更新
                DrawTXT2(e.Graphics, vRect, p);
            } else {
                // case C: 両方更新
                DrawLNA(e.Graphics, vRect, p);
                p.SetClip(cvs_.zone());
                DrawTXT2(e.Graphics, vRect, p);
                p.ClearClip();

                
            }

        }

        protected override bool IsInputChar(char charCode) {
            //return (!char.IsControl(charCode)) || (charCode == '\t');
            if (charCode == '\t') return true;
            return !char.IsControl(charCode);
        }

        private Boolean ldowm = false;
        //private CaretInfo sels, sele;
        protected override void OnMouseDown(MouseEventArgs e) {
            Focus();
            if (e.Button == MouseButtons.Left) {
                //if (cur_.isSelectText()) {
                //
                //} else {
                    cur_.on_lbutton_down(e.X, e.Y, (Control.ModifierKeys & Keys.Shift) == Keys.Shift);
                //}
                //if (cur != sel) {
                //    CaretInfo c = new CaretInfo();
                //    GetVPos(e.X, e.Y, ref c);

                //    if (Insel(c)) {
                //        ldowm = true;
                //        dostart = true;

                //        sels = new CaretInfo(cur);
                //        sele = new CaretInfo(sel);
                //        CorrectPos(ref sels, ref sele);

                //    } else {
                //        GetVPos(e.X, e.Y, ref cur);

                //        int ad = cur.ad;
                //        int tl = cur.tl;

                //        ScrollTo(cur);
                //        UpdateCaretPos();

                //        cur.CopyTo(ref sel);

                //        base.Invalidate();

                //        ldowm = true;
                //        dostart = false;
                //    }
                //} else {
                //    GetVPos(e.X, e.Y, ref cur);

                //    //int ad = cur.ad;
                //    //int tl = cur.tl;

                //    ScrollTo(cur);
                //    UpdateCaretPos();

                //    cur.CopyTo(ref sel);

                //    base.Invalidate();

                //    ldowm = true;
                //    dostart = false;
                //}
            }
            base.OnMouseDown(e);
        }

        Boolean dostart = false;
        protected override void OnMouseMove(MouseEventArgs e) {
            base.OnMouseMove(e);
            //if (ldowm) {
            //    //if (cur == sel)
            //    //{
            //    //    Point p = this.PointToClient(MousePosition);
            //    //    GetVPos(p.X, p.Y, ref cur);
            //    //    CaretMove(true);
            //    //    this.Invalidate();
            //    //}
            //    if (dostart) {
            //        //dostart = true;
            //        DoDragDrop("test", DragDropEffects.Move);
            //    } else {
            //        Point p = this.PointToClient(MousePosition);
            //        GetVPos(p.X, p.Y, ref cur);
            //        MoveTo(true);
            //        this.Invalidate();
            //    }
            //}
        }

        protected override void OnMouseUp(MouseEventArgs e) {
            //if (dostart) {
            //    GetVPos(e.X, e.Y, ref cur);
            //    UpdateCaretPos();
            //    cur.CopyTo(ref sel);

            //    base.Invalidate();
            //}
            //ldowm = false;
            //dostart = false;

            base.OnMouseUp(e);
        }
 

        protected override void OnMouseEnter(EventArgs e) {
            //Cursor = Cursors.IBeam;
            base.OnMouseEnter(e);
        }

        protected override void OnMouseLeave(EventArgs e) {
            //Cursor = Cursors.Default;
            base.OnMouseLeave(e);
        }

        //private void CorrectPos(ref CaretInfo s, ref CaretInfo e) {
        //    // 必ずs<=eになるように修正
        //    if (s > e) {
        //        int tmp;
        //        tmp = s.ad; s.ad = e.ad; e.ad = tmp;
        //        tmp = s.tl; s.tl = e.tl; e.tl = tmp;
        //    }
        //}

        //private Boolean Insel(CaretInfo caret) {
        //    if (cur < sel) {
        //        if (cur <= caret && caret <= sel) {
        //            return true;
        //        }
        //    } else if (cur > sel) {
        //        if (sel <= caret && caret <= cur) {
        //            return true;
        //        }
        //    }
        //    return false;
        //}

        //private Boolean Insel(CaretInfo caret, CaretInfo selStart, CaretInfo selEnd) {
        //    if (selStart < selEnd) {
        //        if (selStart <= caret && caret <= selEnd) {
        //            return true;
        //        }
        //    } else if (selStart > selEnd) {
        //        if (selEnd <= caret && caret <= selStart) {
        //            return true;
        //        }
        //    }
        //    return false;
        //}

        

        protected override void OnDragDrop(DragEventArgs drgevent) {
            //isdropfile = false;
            //ldowm = false;
            //cur.CopyTo(ref sel);

            //if (drgevent.Data.GetDataPresent(DataFormats.FileDrop)) {

            //    string[] files = (string[])drgevent.Data.GetData(DataFormats.FileDrop, false);

            //} else if (drgevent.Data.GetDataPresent(typeof(string))) {
            //    dostart = false;
            //    if (!Insel(cur, sels, sele)) {
            //        string ss = (string)drgevent.Data.GetData(DataFormats.UnicodeText);
            //        ss = GetText(sels, sele).ToString();

            //        if (cur < sels) {
            //            Input(ss);
            //            CaretInfo s = new CaretInfo(cur);
            //            Del(sels, sele);
            //            s.CopyTo(ref cur);
            //            cur.CopyTo(ref sel);
            //        } else if (cur > sele) {
            //            Input(ss);
            //            CaretInfo s = new CaretInfo(cur);
            //            int nn = vlNum_;
            //            int t = sels.tl - sele.tl;
            //            int dad = sele.ad - sels.ad;
            //            Del(sels, sele);

            //            if (s.tl == cur.tl) {
            //                s.ad -= dad;
            //            }
            //            s.vl += vlNum_ - nn;
            //            s.tl += t;

            //            ConvDPosToVPos(s, ref cur, ref s);

            //            //s.CopyTo(ref cur);
            //            cur.CopyTo(ref sel);


            //        }
            //        UpdateCaretPos();
            //    }
            //}
            base.OnDragDrop(drgevent);

        }

        protected override void OnDragOver(DragEventArgs drgevent) {
            if (!Focused)
                Focus();

            ////base.OnDragOver(drgevent);
            //Point p = this.PointToClient(new Point(drgevent.X, drgevent.Y));
            //GetVPos(p.X, p.Y, ref cur);
            //ScrollTo(cur);
            //UpdateCaretPos();
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

        //public bool SearchText(string sh) {
        //    Search search = new Search(this.doc);
        //    search.searchstr = sh;
        //    CaretInfo bgn = new CaretInfo(cur);
        //    CaretInfo end = new CaretInfo(cur);

        //    if (search.FindNext(cur, ref bgn, ref end)) {
        //        ConvDPosToVPos(bgn, ref cur);
        //        ConvDPosToVPos(end, ref sel);
        //        MoveTo(true);
        //        base.Invalidate();
        //        return true;
        //    }
        //    return false;
        //}

        //public void Cut() {
        //    string buff;
        //    doc.Delete(cur, sel, out buff);
        //    Clipboard.SetText(buff);
        //}

        //public void Paste()
        //{
        //    string t = Clipboard.GetText();
        //    Input(t);
        //    base.Invalidate();
        //}


        //
        //-------------------------------------------------------------------------
        // 再描画したい範囲を Invalidate する。
        //-------------------------------------------------------------------------
        void ReDraw( ReDrawType r, DPos s )
        {
	        // まずスクロールバーを更新
	        //UpdateScrollBar();

	        switch( r ){
                case ReDrawType.ALL: // 全画面
		        //::InvalidateRect( hwnd_, NULL, FALSE );
                this.Invalidate(false);
                break;

                case ReDrawType.LNAREA: // 行番号表示域のみ
		        if( lna() > 0 )
		        {
			        Rectangle rc = new Rectangle(0, 0, lna(), bottom());
			        //::InvalidateRect( hwnd_, &rc, FALSE );
                    this.Invalidate(rc, false);
		        }
		        break;

                case ReDrawType.LINE: // 指定した行の後半
                case ReDrawType.AFTER: // 指定した行以下全部
		        //{
			        DPos st = ( s.ad==0 ? s : doc_.leftOf(s,true) );
                    InvalidateView(st, r == ReDrawType.AFTER);
		        //}
                break;
	        }
        }
        //


        #region ITextEditor メンバ

        public void Copy() {
            throw new NotImplementedException();
        }

        public void Cut() {
            throw new NotImplementedException();
        }

        public void Paste() {
            //string t = Clipboard.GetText();
            //Input(t);
            //base.Invalidate();
        }

        public void BackSpace() {
            throw new NotImplementedException();
        }

        public void Delete() {
            throw new NotImplementedException();
        }

        public void Up() {
            throw new NotImplementedException();
        }

        public void Down() {
            throw new NotImplementedException();
        }

        public new void Left() {
            throw new NotImplementedException();
        }

        public new void Right() {
            throw new NotImplementedException();
        }

        #endregion
    }
}
