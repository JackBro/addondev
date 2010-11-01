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
using YYS.Parser;

namespace YYS {

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

    //public partial class GCsTextEdit : Control, ITextEditor {
    public partial class GCsTextEdit : Control {

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

        public KeyMap KeyMap { get; set; }

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

        private bool showLineNumber = false;
        [DefaultValue(false)]
        public bool ShowLineNumber {
            get { return this.showLineNumber; } 
            set{
                this.showLineNumber = value;
                if (this.Visible && this.Width > 0 && this.Height > 0) {
                    cvs_.on_config_change(Wrap, ShowLineNumber);
                    DoConfigChange();
                } else {
                    cvs_.showLN = value;
                }
            } 
        }

        public WrapType Wrap {
            get { return cvs_.wrapType; }
            set { 
                if (this.Visible && this.Width>0 && this.Height>0) {
                    cvs_.on_config_change(value, ShowLineNumber);
                    DoConfigChange();
                } else {
                    cvs_.wrapType = value;
                }
            }
        }

        public new Font Font {
            get { return base.Font; }
            set {
                base.Font = value;
                fnt().Font = value;
                fnt().init();
                if (this.Visible && this.Width > 0 && this.Height > 0) {
                    cvs_.on_font_change();
                    cur_.on_setfocus();
                    CalcEveryLineWidth(); // 行幅再計算
                    DoConfigChange();
                }
            }
        }

        public new string Text {
            get { return this.doc_.ToString(); }
            set {
                //cur_.Input(value);
                doc_.Text = value;
            }
        }

        public bool RectSelect {
            get { return cur_.Selection == SelectionType.Rectangle; }
            set { cur_.Selection = value ? SelectionType.Rectangle : SelectionType.Normal; }
        }

        [DefaultValue(Keys.Alt)]
        public Keys MouseRectSelectKey { get; set; }

        [DefaultValue(Keys.Shift)]
        public Keys MouseNormalSelectKey { get; set; }

        private Canvas cvs_;

	    internal Win32API.RECT zone()  { return cvs_.zone(); }
        internal int left() { return cvs_.zone().left; }
        internal int right() { return cvs_.zone().right; }
        internal int bottom() { return cvs_.zone().bottom; }
        internal int lna() { return cvs_.zone().left; }
        internal int cx() { return cvs_.zone().right - cvs_.zone().left; }
        internal int cxAll() { return cvs_.zone().right; }
        internal int cy() { return cvs_.zone().bottom; }
        
        //
        private Document doc_;
        public Document Document {
            get { return this.doc_; }
        }

        //private IDocument doc_;
        //public IDocument Document {
        //    get { return this.doc_; }
        //}

        //
        internal Painter fnt() { return cvs_.getPainter(); }
        //
        private Cursor cur_;

        //public Cursor cursor {
        //    get { return cur_; }
        //}
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
        
        //public Tuple<DPos, DPos> GetSelect() {
        //    if (cur_.Cur > cur_.Sel) {
        //        DPos s = new DPos(cur_.Sel);
        //        DPos e = new DPos(cur_.Cur);
        //        return new Tuple<DPos, DPos> { t1 = s, t2 = e };
        //    }
        //    else {
        //        DPos s = new DPos(cur_.Cur);
        //        DPos e = new DPos(cur_.Sel);
        //        return new Tuple<DPos, DPos> { t1 = s, t2 = e };
        //    }
        //}

        public GCsTextEdit() {
            this.SetStyle(ControlStyles.OptimizedDoubleBuffer | ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint, true);

            //this.SetStyle(ControlStyles.DoubleBuffer, true);
            //this.SetStyle(ControlStyles.UserPaint, true);
            //this.SetStyle(ControlStyles.AllPaintingInWmPaint, true);

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

            KeyMap = new KeyMap();
            MouseNormalSelectKey = Keys.Shift;
            MouseRectSelectKey = Keys.Alt;

            doc_ = new Document();
            doc_.setHighlight(new Highlight(this.ForeColor));
            //doc_.DocumentChanged += (sender, e) => {
            //    if (e.type == DocumentEventType.Clear) {
            //        this.MoveCursor(new DPos(0, 0));
            //    }
            //};
            doc_.TextUpdateEvent += (s, e, e2, reparsed, nmlcmd) => {
                on_text_update(s, e, e2, reparsed, nmlcmd);
            };
            //
            cvs_ = new Canvas(this, this.Font, ShowLineNumber);
            fnt().LineNumberForeColor = this.ForeColor;
            fnt().LineNumberBackColor = this.BackColor;
            fnt().LineNumberLineColor = this.ForeColor;
            fnt().SpecialCharForeColor = Color.Gray;

            cur_ = new Cursor(this, new Caret(this.Handle));

            Initialize();
        }

        public void Initialize() {
            wrap_ = new List<WLine>();

            // 適当に折り返し情報初期化
            InsertMulti(0, doc_.tln() - 1);

            udScr_tl_ = 0;
            udScr_vrl_ = 0;
            ReSetScrollInfo();
        }

        private string getLinkFromPositon(int x, int y) {
            string link = null;
            VPos vp = new VPos();
            GetVPos(x, y, ref vp, false);
            var rules = doc_.Rules(vp.tl);
            foreach (var rule in rules) {
                if (((rule.attr.type & AttrType.Link) == AttrType.Link) 
                    && (vp.ad >= rule.ad && vp.ad <= (rule.ad + rule.len))) {
                    link = doc_.tl(vp.tl).Substring(rule.ad, rule.len).ToString();
                    break;
                }
            }
            return link;
        }

        Win32API.RECT clientRect = new Win32API.RECT();
        internal Win32API.RECT getClientRect() {
            //return this.ClientRectangle;

            clientRect.left = this.ClientRectangle.Left;
            clientRect.top = this.ClientRectangle.Top;
            clientRect.right = this.ClientRectangle.Right - vScrollBar.Width;
            if (clientRect.right < 0) clientRect.right = 0;
            clientRect.bottom = this.ClientRectangle.Bottom - hScrollBar.Height;
            if (clientRect.bottom < 0) clientRect.bottom = 0;
            return clientRect;
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
            cvs_.Dispose();
            base.Dispose(disposing);
        }

        //~gcsTextEdit() {
        //    Dispose(false);
        //}

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
            base.OnSizeChanged(e);

            //int x = this.ClientSize.Width - vScrollBar.Width;
            //int y = this.ClientSize.Height - hScrollBar.Height;
            int cx = this.Location.X + this.Width - vScrollBar.Width;
            int cy = this.Bottom - hScrollBar.Height;
            if (cx < 0 || cy < 0) return;
            DoResize(cvs_.on_view_resize(cx, cy));
        }

        internal void GetOrigin(ref int x, ref int y) {
            x = left() - hScrollBar.Value;
            y = -vScrollBar.Value * cvs_.getPainter().H();
        }

        //
        private VPos dummyVPos = new VPos();
        internal void ConvDPosToVPos(DPos dp, ref VPos vp) {
            dummyVPos.ad = -1;
            ConvDPosToVPos(dp, ref vp, ref dummyVPos);
        }
        //
        internal void ConvDPosToVPos(DPos dp, ref VPos vp, ref VPos basevp) {

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

        protected override void OnKeyPress(KeyPressEventArgs e) {
            base.OnKeyPress(e);
            if (e.Handled) {
                return;
            }

            if (IsInputChar(e.KeyChar)) {
                cur_.InputChar(e.KeyChar);
                e.Handled = true;
            }
        }

        protected override bool IsInputChar(char charCode) {
            if (charCode == '\t') return true;
            if (charCode == '\r') return true;
            return !char.IsControl(charCode);
        }

        protected override void OnPreviewKeyDown(PreviewKeyDownEventArgs e) {
            base.OnPreviewKeyDown(e);
            if (e.Modifiers == Keys.None) {

            }
            KeyMap.getAction(e.Modifiers | e.KeyCode)(this);
        }

        protected override bool ProcessDialogKey(Keys keyData) {
            //base.ProcessDialogKey;
            return false;
        }

        protected override void OnPaint(PaintEventArgs e) {
            base.OnPaint(e);
       
            Painter p = cvs_.getPainter();
            //vRect.rc = e.ClipRectangle;
            if (this.Height <= e.ClipRectangle.Height) {
                Size s = new Size {
                    Height = e.ClipRectangle.Height - hScrollBar.Height,
                    Width = e.ClipRectangle.Width - vScrollBar.Width
                };
                vRect.rc = new Rectangle(e.ClipRectangle.Location, s);
            } else {
                vRect.rc = e.ClipRectangle;
            }

            GetDrawPosInfo(ref vRect);

            if (e.ClipRectangle.Right <= lna()) {
                // case A: 行番号表示域のみ更新
                DrawLNA(e.Graphics, vRect, p);
            } else if (lna() <= e.ClipRectangle.Left) {
                // case B: テキスト表示域のみ更新
                DrawTXT3(e.Graphics, vRect, p);
            } else {
                // case C: 両方更新
                DrawLNA(e.Graphics, vRect, p);
                p.SetClip(cvs_.zone());
                DrawTXT3(e.Graphics, vRect, p);
                p.ClearClip();

                //DrawTXT3(e.Graphics, vRect, p);
                //DrawLNA(e.Graphics, vRect, p);
            }
        }

        protected override void OnMouseEnter(EventArgs e) {
            base.OnMouseEnter(e);
            this.Cursor = Cursors.IBeam;
        }

        protected override void OnMouseLeave(EventArgs e) {
            base.OnMouseLeave(e);
            this.Cursor = Cursors.Default;
        }

        protected override void OnMouseClick(MouseEventArgs e) {
            base.OnMouseClick(e);

            if (!Focused) Focus();

            if (MouseLinkClick != null) {
                string link = getLinkFromPositon(e.X, e.Y);
                if (link != null) {
                    MouseLinkClick(this, new ClickableLinkEventArgs(e, link));
                }
            }
        }

        protected override void OnMouseDoubleClick(MouseEventArgs e) {
            base.OnMouseDoubleClick(e);
            
            if (!Focused) Focus();

            cur_.mouse_double_click(e);
            if (MouseLinkDoubleClick != null) {
                string link = getLinkFromPositon(e.X, e.Y);
                if (link != null) {
                    MouseLinkDoubleClick(this, new ClickableLinkEventArgs(e, link));
                }
            }
        }

        protected override void OnMouseUp(MouseEventArgs e) {
            base.OnMouseUp(e);

            cur_.mouse_up(e);
        }

        protected override void OnMouseDown(MouseEventArgs e) {
            base.OnMouseDown(e);

            if (!Focused) Focus();

            cur_.mouse_down(e);
            if (MouseLinkDown != null) {
                string link = getLinkFromPositon(e.X, e.Y);
                if (link != null) {
                    MouseLinkDown(this, new ClickableLinkEventArgs(e, link));
                }
            }
        }

        protected override void OnMouseMove(MouseEventArgs e) {
            base.OnMouseMove(e);
            cur_.mouse_move(e);
        }

        protected override void OnDragOver(DragEventArgs e) {
            base.OnDragOver(e);
            if (!Focused)
                Focus();
            cur_.DragOver(e);
        }

        protected override void OnDragDrop(DragEventArgs e) {
            base.OnDragDrop(e);
            cur_.DragDrop(e);
        }

        protected override void OnDragEnter(DragEventArgs e) {
            base.OnDragEnter(e);

            if (e.Data.GetDataPresent(DataFormats.UnicodeText)
                || e.Data.GetDataPresent(DataFormats.Text)) {
                e.Effect = DragDropEffects.Move;
            } else {
                e.Effect = DragDropEffects.None;
            }
            
        }

        //
        //-------------------------------------------------------------------------
        // 再描画したい範囲を Invalidate する。
        //-------------------------------------------------------------------------
        private void ReDraw( ReDrawType r, DPos s )
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
			        DPos st = ( s.ad==0 ? s : doc_.leftOf(s,true) );
                    InvalidateView(st, r == ReDrawType.AFTER);
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

        #endregion

        #region ITextEditor メンバ

        public void SetSelction(DPos s, DPos e) {
            cur_.MoveCur(s, false);
            cur_.MoveCur(e, true);
        }

        public void GetSelction(out DPos s, out DPos e) {
            if (cur_.Cur <= cur_.Sel) {
                s = cur_.Cur;
                e = cur_.Sel;
            }
            else {
                s = cur_.Sel;
                e = cur_.Cur;
            }
        }

        #endregion

        #region ITextEditor メンバ

        public void MoveCursor(DPos dp) {
            cur_.MoveCur(dp, false);
        }

        public void SelectAll() {
            this.Home(true, false);
            this.End(true, true);
        }

        public Point GetPointFromDPos(DPos dp) {
            int x,y;
            if (dp == this.cur_.Cur) {
                x = this.cur_.Cur.rx;
                y = this.cur_.Cur.vl * fnt().H();
            }
            else {
                VPos vp = new VPos();
                this.ConvDPosToVPos(dp, ref vp);
                x = vp.rx;
                y = vp.vl * fnt().H();
            }
            return new Point(x, y);
        }

        #endregion
    }
}
