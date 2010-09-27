using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Text.RegularExpressions;
using System.Threading;
using System.ComponentModel;

namespace AsControls {

    /// <summary>
    /// テキスト内容が変更されたときに発生
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="s">変更範囲の先頭</param>
    /// <param name="e">変更範囲の終端(前)</param>
    /// <param name="e2">変更範囲の終端(後)</param>
    /// <param name="reparsed">e2より後ろのコメントアウト状態が変化していたらtrue</param>
    /// <param name="nmlcmd">挿入/削除/置換ならtrue、ファイル開き/全置換ならfalse</param>
    internal delegate void TextUpdateEventHandler(DPos s, DPos e, DPos e2, bool reparsed, bool nmlcmd);

    public class ClickableLinkEventArgs : EventArgs {
        private Point location;
        public string Link { get; private set; }
        public MouseButtons Button { get; private set; }
        public int Clicks { get; private set; }
        public int X { get { return Location.X; } private set { location.X = value; } }
        public int Y { get { return Location.Y; } private set { location.Y = value; } }
        public Point Location { get { return location; } private set { location = value; } }
        public int Delta { get; private set; }

        public ClickableLinkEventArgs(MouseEventArgs e, string link)
        {
            Button = e.Button;
            Clicks = e.Clicks;
            Location = e.Location;
            Delta = e.Delta;
            Link = link;
        }
    }

    public partial class gcsTextEdit : Control, ITextEditor {

        public event EventHandler<ClickableLinkEventArgs> MouseLinkClick;
        public event EventHandler<ClickableLinkEventArgs> MouseLinkDoubleClick;
        public event EventHandler<ClickableLinkEventArgs> MouseLinkDown;

        internal class VDrawInfo {
            public Rectangle rc;

            /// <summary>
            /// 一番左の文字のx座標
            /// </summary>
            public int XBASE;

            /// <summary>
            /// テキスト再描画範囲左端
            /// </summary>
            public int XMIN;

            /// <summary>
            /// テキスト再描画範囲右端
            /// </summary>
            public int XMAX;

            /// <summary>
            /// テキスト再描画範囲上端
            /// </summary>
            public int YMIN;

            /// <summary>
            /// テキスト再描画範囲下端
            /// </summary>
            public int YMAX;

            /// <summary>
            /// テキスト再描画範囲上端論理行番号
            /// </summary>
            public int TLMIN;

            /// <summary>
            /// 選択範囲のx座標
            /// </summary>
            public int SXB;
            /// <summary>
            /// 選択範囲のx座標
            /// </summary>
            public int SXE;

            /// <summary>
            /// 選択範囲のy座標
            /// </summary>
            public int SYB;
            /// <summary>
            /// 選択範囲のy座標
            /// </summary>
            public int SYE;

            public VDrawInfo() {
                rc = new Rectangle();
                YMIN = 0;
                YMAX = 0;
                TLMIN = 0;
                SXB = SXE = SYB = SYE = 0;
            }
        }

        private VDrawInfo vRect = new VDrawInfo();
        internal VDrawInfo VRect {
            get { return vRect; }
        }

        private VGcsScrollBar vScrollBar;
        private HGcsScrollBar hScrollBar;

        /// <summary>
        /// 一番上に表示される論理行のTLine_Index
        /// </summary>
        private int udScr_tl_;
        /// <summary>
        /// 一番上に表示される表示行のVRLine_Index
        /// </summary>
        private int udScr_vrl_;


        private Ime imeComposition;

        public KeyMap KeyBind { get; set; }

        public SelectType SelectMode {
            get { return cur_.SelectMode; }
            set { cur_.SelectMode = value; }
        }

        public new Font Font {
            get { return base.Font; }
            set {
                base.Font = value;
                fnt().Font = value;
                fnt().init(); //TODO fnt().init();
            }
        }

        public int TabWidth {
            get { return fnt().TabWidth; }
            set { fnt().TabWidth = value; }
        }

        public Color LineNumberForeColor {
            get { return fnt().LineNumberForeColor; }
            set { fnt().LineNumberForeColor = value; }
        }

        public Color LineNumberBackColor {
            get { return fnt().LineNumberBackColor; }
            set { fnt().LineNumberBackColor = value; }
        }

        public Color LineNumberLineColor {
            get { return fnt().LineNumberLineColor; }
            set { fnt().LineNumberLineColor = value; }

        }

        public Color SpecialCharForeColor {
            get { return fnt().SpecialCharForeColor; }
            set { fnt().SpecialCharForeColor = value; }
        }

        [DefaultValue(false)]
        public bool ShowReturn { get; set; }

        [DefaultValue(false)]
        public bool ShowTab { get; set; }

        [DefaultValue(false)]
        public bool ShowWhiteSpace { get; set; }

        [DefaultValue(false)]
        public bool ShowZenWhiteSpace { get; set; }

        public WrapType Wrap {
            get { return cvs_.wrapType; }
            set { cvs_.wrapType = value; }
        }

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
        internal int left() { return cvs_.zone().Left; }
        internal int right() { return cvs_.zone().Right; }
        internal int bottom() { return cvs_.zone().Bottom; }
        internal int lna() { return cvs_.zone().Left; }
        internal int cx() { return cvs_.zone().Right - cvs_.zone().Left; }
        internal int cxAll() { return cvs_.zone().Right; }
        internal int cy() { return cvs_.zone().Bottom; }
        //
        private Document doc_;
        public Document Document {
            get { return this.doc_; }
        }
        //
        internal Painter fnt() { return cvs_.getPainter(); }
        //
        private Cursor cur_;

        public Cursor cursor {
            get { return cur_; }
        }
        //
        internal enum ReDrawType {
            /// <summary>
            /// 行番号ゾーンのみ
            /// </summary>
            LNAREA,
            /// <summary>
            /// 変更のあった一行のみ
            /// </summary>
            LINE,
            /// <summary>
            /// 変更のあった行以下全部
            /// </summary>
            AFTER,
            /// <summary>
            /// 全画面
            /// </summary>
            ALL 
        }

        public void SetSelect(CursorPos s, CursorPos e) {
           
        }
        
        public Tuple<CursorPos, CursorPos> GetSelect() {
            if (cur_.Cur > cur_.Sel) {
                CursorPos s = new CursorPos { line = cur_.Sel.tl, index = cur_.Sel.ad };
                CursorPos e = new CursorPos { line = cur_.Cur.tl, index = cur_.Cur.ad };
                return new Tuple<CursorPos, CursorPos> { t1 = s, t2 = e };
            }
            else {
                CursorPos s = new CursorPos { line = cur_.Cur.tl, index = cur_.Cur.ad };
                CursorPos e = new CursorPos { line = cur_.Sel.tl, index = cur_.Sel.ad };
                return new Tuple<CursorPos, CursorPos> { t1 = s, t2 = e };
            }
        }

        public Search Sr() { 
                Search s = new Search(this);
                //s.Searcher = new NormalSearch(word);
                return s;
        }

        public gcsTextEdit() {
            this.SetStyle(ControlStyles.OptimizedDoubleBuffer | ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint, true);

            vScrollBar = new VGcsScrollBar();
            vScrollBar.Scroll += new ScrollEventHandler(ScrollBar_Scroll);
            this.Controls.Add(vScrollBar);

            hScrollBar = new HGcsScrollBar();
            hScrollBar.Scroll += new ScrollEventHandler(ScrollBar_Scroll);
            this.Controls.Add(hScrollBar);

            this.ImeMode = ImeMode.Off;
            imeComposition = new Ime(this);
            //imeComposition.ImeCompositedHira += new Ime.ImeEventHandler(imeComposition_ImeCompositedHira);
            //imeComposition.ImeCompositedKata += new Ime.ImeEventHandler(imeComposition_ImeCompositedKata);

            //this.HandleDestroyed += new EventHandler(AsTextEdit_HandleDestroyed);
            this.LostFocus += (sender, e) => {
                cur_.on_killfocus();
            };
            this.GotFocus += (sender, e) => {
                cur_.on_setfocus();
            };

            this.VisibleChanged += new EventHandler(gcsTextEdit_VisibleChanged);

            this.MouseClick += (sender, e) => {
                if (MouseLinkClick != null) {
                    VPos vs, ve;
                    cur_.getCurPos(out vs, out ve);
                    var rules = doc_.Rules(vs.tl);
                    foreach (var rule in rules) {
                        if (rule.attr.islink && (vs.ad >= rule.ad && vs.ad <= (rule.ad + rule.len))) {
                            string link = doc_.tl(vs.tl).Substring(rule.ad, rule.len).ToString();
                            MouseLinkClick(this, new ClickableLinkEventArgs(e, link));
                            break;
                        }
                    }
                }
            };

            KeyBind = new KeyMap();

            doc_ = new Document();
            doc_.TextUpdate += (s, e, e2, reparsed, nmlcmd) => {
                on_text_update(s, e, e2, reparsed, nmlcmd);
            };
            //
            cvs_ = new Canvas(this, this.Font);
            fnt().LineNumberForeColor = this.ForeColor;
            fnt().LineNumberBackColor = this.BackColor;
            fnt().LineNumberLineColor = this.ForeColor;
            fnt().SpecialCharForeColor = Color.Gray;

            cur_ = new Cursor(this, doc_, new Caret(this.Handle));
            //base.AllowDrop = true;

            Initialize();
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

        private string getLinkFromCursor() {
            string link = string.Empty;
            VPos vs, ve;
            cur_.getCurPos(out vs, out ve);
            var rules = doc_.Rules(vs.tl);
            foreach (var rule in rules) {
                if (rule.attr.islink && (vs.ad >= rule.ad && vs.ad <= (rule.ad + rule.len))) {
                    link = doc_.tl(vs.tl).Substring(rule.ad, rule.len).ToString();
                    break;
                }
            }
            return link;
        }

        internal Rectangle getClientRect() {
            return this.ClientRectangle;
            ////throw new NotImplementedException();
            //Rectangle crect = this.ClientRectangle;
            //////Rectangle rec = new Rectangle(crect.Location, new Size(crect.Width - vScrollBar.Width, crect.Height-hScrollBar.Height));
            //int w = crect.Width - vScrollBar.Width;
            //int h = crect.Height - hScrollBar.Height;
            //w = w > 0 ? w : 0;
            //h = h > 0 ? h : 0;
            //Size size = new Size(w, h);
            //Rectangle rec = new Rectangle(crect.Location, size);
            //return rec;
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
            if (imeComposition != null) {
                //imeComposition.ImeComposition(m, 0, 0);
                if (imeComposition.isImeComposition(m)) {
                    imeComposition.ImeComposition(m);
                }
                else if (imeComposition.isStartcomposition(m)) {
                    // メンバ変数の値を元に、実際にCaretを動かす処理
                    int x = 0;
                    int y = 0; ;
                    GetOrigin(ref x, ref y);
                    x += cur_.Cur.vx;
                    y += cur_.Cur.vl * fnt().H();

                    // 行番号ゾーンにCaretがあっても困るので左に追いやる
                    if (0 < x && x < left())
                        x = -left();

                    imeComposition.ImeStartcomposition(m, x, y);
                }
            }
            base.WndProc(ref m);
        }

        protected override void OnMouseWheel(MouseEventArgs e) {
            base.OnMouseWheel(e);
            int dy = -e.Delta / 120;
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

            int w = this.ClientSize.Width - vScrollBar.Width;
            int h = this.ClientSize.Height - hScrollBar.Height;
            if (w <= 0 || h <= 0) return;
            DoResize(cvs_.on_view_resize(w, h));
            //DoResize(cvs_.on_view_resize(this.ClientSize.Width, this.ClientSize.Height));
            //base.Invalidate();
        }

        //
        public void GetOrigin(ref int x, ref int y) {
            //x = vRect.NumLineLeft - hScrollBar.Value;
            //y = -vScrollBar.Value * lineHeight;
            x = left() - hScrollBar.Value;
            y = -vScrollBar.Value * cvs_.getPainter().H();
        }

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
            int w = 0;
            for (int i = 0; i < len; ++i)
                if (txt[i] == '\t')
                    w = p.nextTab(w);
                else
                    w += p.W(txt[i]);
            return w;

            //Painter p = cvs_.getPainter();
            //return p.CalcStringWidth(txt);
        }

        protected override void OnKeyPress(KeyPressEventArgs e) {
            //if (IsInputChar(e.KeyChar) && this.ImeMode == ImeMode.Off) {
            if (IsInputChar(e.KeyChar)) {
                cur_.InputChar(e.KeyChar);
                e.Handled = true;
            }else if(e.KeyChar == '\r' ){
                cur_.Input("\r\n", 2);
                e.Handled = true;
            }
            //base.OnKeyPress(e);
        }

        protected override void OnPreviewKeyDown(PreviewKeyDownEventArgs e) {
            KeyBind.getAction(e.Modifiers | e.KeyCode)(this);
            base.OnPreviewKeyDown(e);
        }

        protected override bool ProcessDialogKey(Keys keyData) {
            return false;
        }

        //
        protected override void OnPaint(PaintEventArgs e) {
            base.OnPaint(e);
            
            Painter p = cvs_.getPainter();
            vRect.rc = e.ClipRectangle;
            
            //Size size = new Size(e.ClipRectangle.Width-16,e.ClipRectangle.Height );
            //vRect.rc = new Rectangle(e.ClipRectangle.Location, size);
            //e.Graphics.SetClip(new Rectangle(new Point(0,0), size));

            GetDrawPosInfo(ref vRect);

            if (e.ClipRectangle.Right <= lna()) {
                // case A: 行番号表示域のみ更新
                DrawLNA(e.Graphics, vRect, p);
            } else if (lna() <= e.ClipRectangle.Left) {
                // case B: テキスト表示域のみ更新
                DrawTXT3(e.Graphics, vRect, p);
            } else {
                // case C: 両方更新
                //DrawLNA(e.Graphics, vRect, p);
                ////p.SetClip(cvs_.zone());
                //DrawTXT3(e.Graphics, vRect, p);
                ////p.ClearClip();

                DrawTXT3(e.Graphics, vRect, p);
                DrawLNA(e.Graphics, vRect, p);
            }
        }

        protected override bool IsInputChar(char charCode) {
            //return (!char.IsControl(charCode)) || (charCode == '\t');
            if (charCode == '\t') return true;
            return !char.IsControl(charCode);
        }

        private Boolean ldowm = false;
        protected override void OnMouseDown(MouseEventArgs e) {
            Focus();
            if (e.Button == MouseButtons.Left) {
                //if (cur_.isSelectText()) {
                //    //DoDragDrop("", DragDropEffects.Move);
                //}
                //else {
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

        protected override void OnMouseEnter(EventArgs e) {
            this.Cursor = Cursors.IBeam;
            base.OnMouseEnter(e);
        }

        protected override void OnMouseLeave(EventArgs e) {
            this.Cursor = Cursors.Default;
            base.OnMouseLeave(e);
        }        

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

        protected override void OnDragOver(DragEventArgs e) {
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
        protected override void OnDragEnter(DragEventArgs e) {
            if (e.Data.GetDataPresent(DataFormats.FileDrop)) {
                e.Effect = DragDropEffects.Copy;
                isdropfile = true;
            } else if (e.Data.GetDataPresent(DataFormats.UnicodeText)
                || e.Data.GetDataPresent(DataFormats.Text)) {
                e.Effect = DragDropEffects.Move;
            } else {
                e.Effect = DragDropEffects.None;
            }
            base.OnDragEnter(e);
        }

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


        #region ITextEditor メンバ

        public void Copy() {
            cur_.Copy();
        }

        public void Cut() {
            cur_.Cut();
        }

        public void Paste() {
            cur_.Paste();
        }

        public void BackSpace() {
            cur_.DelBack();
        }

        public void Delete() {
            cur_.Del();
        }

        public void Up(bool wide, bool select) {
            cur_.Up(wide, select);
        }

        public void Down(bool wide, bool select) {
            cur_.Down(wide, select);
        }

        public new void Left(bool wide, bool select) {
            cur_.Left(wide, select);
        }

        public new void Right(bool wide, bool select) {
            cur_.Right(wide, select);
        }

        public void Home(bool wide, bool select) {
            cur_.Home(wide, select);
        }

        public void End(bool wide, bool select) {
            cur_.End(wide, select);
        }

        public bool CanUndo() {
            throw new NotImplementedException();
        }

        public bool CanRedo() {
            throw new NotImplementedException();
        }

        public void Undo() {
            doc_.UndoManager.Undo();
        }

        public void Redo() {
            doc_.UndoManager.Redo();
        }

        #endregion
    }
}
