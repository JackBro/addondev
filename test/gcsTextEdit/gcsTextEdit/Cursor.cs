using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using System.Globalization;

namespace AsControls
{
    public class Caret {
        private IntPtr hwnd_;
        private bool created_;
        private int x, y;

        public bool isAlive() {
            return created_;
        }

        public IntPtr hwnd {
            get { return hwnd; }
        }

        public Caret(IntPtr hwnd) {
            hwnd_ = hwnd;
        }

        public void Show() {
            if( created_ ) Win32API.ShowCaret( hwnd_ ); 
        }
        public void Hide() {
            if (created_) Win32API.HideCaret(hwnd_);
        }
        public void Destroy() {
            if (created_) {
                Win32API.DestroyCaret();
                created_ = false;
            }
        }
        public void SetPos(int x, int y) {
            if (created_) {
                Win32API.SetCaretPos(x, y);
                this.x = x;
                this.y = y;
            }
        }

        public Point GetPos() {
            Point point = new Point();
            if (created_) {
                //Win32API.POINT p;
                //Win32API.GetCaretPos(out p);
                //point.X = p.x;
                //point.Y = p.y;
                point.X = this.x;
                point.Y = this.y;
            }
            return point;
        }

        public void Create( int H, int W){
            if( created_ ) Win32API.DestroyCaret();
			created_ = true;
            Win32API.CreateCaret(hwnd_, IntPtr.Zero, W, H);
			//ime().SetFont( hwnd_, lf );
			Show();
        }
    }

    //
    public class DPos {
        /// <summary>
        /// バッファ中のアドレス (0～ )
        /// </summary>
        public int ad { get; set; }

        /// <summary>
        /// 論理行番号 (0～ )
        /// </summary>
        public int tl { get; set; }

        public DPos() { }
        public DPos( int tl, int ad) {
            this.tl = tl;
            this.ad = ad;
        }
        public DPos(DPos dp) {
            this.tl = dp.tl;
            this.ad = dp.ad;
        }

        public static Boolean operator ==(DPos x, DPos y) {
            if (Object.ReferenceEquals(x, null) && Object.ReferenceEquals(y, null)) return true;
            if (Object.ReferenceEquals(x, null) || Object.ReferenceEquals(y, null)) return false; 

            return (x.tl == y.tl && x.ad == y.ad);
        }

        public static Boolean operator !=(DPos x, DPos y) {
            //return (x.tl != x.tl || x.ad != y.ad);

            return !(x == y);
        }

        public static Boolean operator <(DPos x, DPos y) {
            if (Object.ReferenceEquals(x, null) || Object.ReferenceEquals(y, null)) {
                throw new ArgumentNullException();
            }

            return (x.tl < y.tl || (x.tl == y.tl && x.ad < y.ad));
        }

        public static Boolean operator >(DPos x, DPos y) {
            //return (x.tl > y.tl || (x.tl == y.tl && x.ad > y.ad));

            return (y < x);
        }

        public static Boolean operator <=(DPos x, DPos y) {
            if (Object.ReferenceEquals(x, null) || Object.ReferenceEquals(y, null)) {
                throw new ArgumentNullException();
            }

            return (x.tl < y.tl || (x.tl == y.tl && x.ad <= y.ad));
        }

        public static Boolean operator >=(DPos x, DPos y) {
            //return (x.tl > y.tl || (x.tl == y.tl && x.ad >= y.ad));

            return (y <= x);
        }
    }

    //
    public class VPos : DPos {
        /// <summary>
        /// VLine-Index
        /// </summary>
        public int vl { get; set; }

        /// <summary>
        /// RLine-Index 表示行
        /// </summary>
        public int rl { get; set; } 

        /// <summary>
        /// スクロールを考慮しない仮想スクリーン上のx座標(pixel) 
        /// </summary>
        public int vx { get; set; }

        /// <summary>
        /// 文字の並びに左右されてないx座標(pixel)
        /// 長い行のしっぽから短い行に [↑] で移動してその後 [↓] で戻れるようなアレです。
        /// </summary>
        public int rx { get; set; }

        public void Copy(VPos src) {
            tl = src.tl;
            ad = src.ad;
            vl = src.vl;
            rl = src.rl;
            vx = src.vx;
            rx = src.rx;
        }

        //public void Copy(DPos src) {
        //    tl = src.tl;
        //    ad = src.ad;
        //}

        public VPos() {
            tl = 0; ad = 0; 
            vl = 0; rl = 0; vx = 0; rx = 0; 
        }

        public VPos(VPos vp) {
            tl = vp.tl; ad = vp.ad;
            vl = vp.vl; rl = vp.rl; vx = vp.vx; rx = vp.rx;
        }

        public VPos(DPos dp) {
            tl = dp.tl; ad = dp.ad; 
            vl = 0; rl = 0; vx = 0; rx = 0; 
        }
    }

    public enum SelectionType {
        Normal,
        Rectangle
    }

    public enum StateType {
        None,
        //MouseDown,
        TextSelect,
        TextGrab,
        TextMove
    }

    abstract class CursorEventAction {
        protected Cursor cur;
        public void MouseDown(MouseEventArgs e) { }
        public void MouseUp(MouseEventArgs e) { }
        public void MouseClick(MouseEventArgs e) { }
        public void MouseDoubleClick(MouseEventArgs e) { }
        public void MouseMove(MouseEventArgs e) { }
        public void DragOver(DragEventArgs e) { }
        public void DragDrop(DragEventArgs e) { }
    }

    //
    public class Cursor {

        private Document doc_; 
        private gcsTextEdit view_;
        private Caret caret_;

        /// <summary>
        /// カーソル位置
        /// </summary>
        private VPos cur_;

        /// <summary>
        /// 選択時の軸足位置
        /// </summary>
        private VPos sel_;

        /// <summary>
        /// 挿入モード？
        /// </summary>
        private bool bIns_; 

        /// <summary>
        /// 行選択モード？
        /// </summary>
        private bool lineSelectMode_;


        public StateType State { get; set; }
        public SelectionType Selection {
            get;
            set;
        }

        public int dragX_, dragY_;

        public VPos Cur {
            get { return cur_; }
        }

        public VPos Sel {
            get { return sel_; }
        }

        public bool isSelectText() {
            return !(cur_ == sel_);
        }

        public Cursor(gcsTextEdit view_, Document doc_, Caret caret_) {
            this.view_ = view_;
            this.doc_ = doc_;
            this.caret_ = caret_;

            cur_ = new VPos();
            sel_ = new VPos();

            bIns_ = true;

            State = StateType.None;

            //TODO Rectangle
            Selection = SelectionType.Normal;

            //this.view_.MouseDown += (sender, e) => {
            //    state = State.MouseDown;
            //};
            //this.view_.MouseUp += (sender, e) => {
            //    state = State.None;
            //};
            //this.view_.MouseMove += (sender, e) => {
            //    if (State == CursorState.MouseDown) {             
            //        //if (!PtInRect(&view_.zone(), pt)) {
            //        //if ( view_.zone().Contains(e.X, e.Y)){
            //            MoveByMouse(e.X, e.Y);
            //        //}
            //    }
            //};
            //this.view_.MouseDoubleClick += (sender, e) => {
            //    // 行番号ゾーンの場合は特に何もしない
            //    if (view_.lna() - view_.fnt().F() < e.X) {
            //        // 行末の場合も特に何もしない
            //        if (cur_.ad != doc_.len(cur_.tl)) {
            //            VPos np = new VPos();
            //            view_.ConvDPosToVPos(doc_.wordStartOf(cur_), ref np, ref cur_);
            //            MoveTo(np, false);
            //            Right(true, true);
            //        }
            //    }
            //};
        }

        public void mouse_down(MouseEventArgs e) {
            switch (State) {
                case StateType.None:
                    if (e.Button == MouseButtons.Left){
                        if (ContainSelect(e.X, e.Y, Cur, Sel)) {
                            this.State = StateType.TextGrab;
                        }
                        else {

                        }
                    }
                    break;
                case StateType.TextSelect:
                    break;
                case StateType.TextGrab:
                    break;
                case StateType.TextMove:
                    break;
                default:
                    break;
            }
        }

        public void mouse_up(MouseEventArgs e) {
            switch (State) {
                case StateType.None:
                    break;
                case StateType.TextSelect:
                    break;
                case StateType.TextGrab:
                    
                    this.State = StateType.None;
                    on_lbutton_down(e.X, e.Y, false);
                    Selection = SelectionType.Normal;
                    State = StateType.None;
                    break;
                case StateType.TextMove:
                    break;
                default:
                    break;
            }
        }

        public void mouse_move(MouseEventArgs e) {
            switch (State) {
                case StateType.None:
                    if (e.Button == MouseButtons.Left) {
                        MoveByMouse(e.X, e.Y);
                    }
                    break;
                case StateType.TextSelect:
                    break;
                case StateType.TextGrab:
                    this.State = StateType.TextMove;
                    var text = getRangesText(Cur, Sel);
                    view_.DoDragDrop(text, DragDropEffects.Move);
                    break;
                case StateType.TextMove:
                    break;
                default:
                    break;
            }
        }


        public void DragOver(DragEventArgs e) {
            switch (State) {
                case StateType.None:
                    break;
                case StateType.TextSelect:
                    break;
                case StateType.TextGrab:
                    break;
                case StateType.TextMove:
                    Point p = view_.PointToClient(new Point(e.X, e.Y));
                    var vp = new VPos();
                    view_.GetVPos(p.X, p.Y, ref vp, false);
                    SetPos(vp);
                    break;
                default:
                    break;
            }
        }

        public void DragDrop(DragEventArgs e) {
            switch (State) {
                case StateType.None:
                    break;
                case StateType.TextSelect:
                    break;
                case StateType.TextGrab:
                    break;
                case StateType.TextMove:
                    Point p = view_.PointToClient(new Point(e.X, e.Y));
                    if (ContainSelect(p.X, p.Y, Cur, Sel)) {

                        //cur_.Cur.Copy(vpcur);                  
                        on_lbutton_down(p.X, p.Y, false);
                        on_button_up();
                        return;
                    }
                    if (Selection == SelectionType.Normal) {
                        var vp = new VPos();
                        view_.GetVPos(p.X, p.Y, ref vp, false);
                        //cur_.Cur.Copy(vp);
                        MoveText(vp, Cur, Sel);

                        //cur_.on_lbutton_down(p.X, p.Y, false);
                        //cur_.on_button_up(); //State = CursorState.None;

                    }
                    else if (Selection == SelectionType.Rectangle) {
                        //var vp1 = new VPos();
                        ///GetVPos(p.X, p.Y, ref vp1, false);
                        //cur_.Cur.Copy(vp1);

                        //VPos vp = new VPos(cur_.Cur);
                        string text = e.Data.GetData(DataFormats.Text) as string;
                        //var text = getRangesText(Cur, Sel);
                        DelRectangle(Cur, Sel);
                        List<string> lines = text.Split(new string[] { "\r\n" }, StringSplitOptions.None).ToList<string>();

                        var vp = new VPos();
                        view_.GetVPos(p.X, p.Y, ref vp, false);
                        Cur.Copy(vp);
                        RectangleInsert(Cur, Cur, lines);
                    }
                    Selection = SelectionType.Normal;
                    State = StateType.None;
                    break;
                default:
                    break;
            }
        }

        public void MoveCur(DPos dp, bool select) {
            VPos vp = new VPos();
            view_.ConvDPosToVPos(dp, ref vp);
            MoveTo(vp, select);
        }

        private void MoveTo(VPos vp, bool sel) {
            if (sel) {
                // 選択状態が変わる範囲を再描画
                Redraw(vp, cur_);
            }
            else {
                // 選択解除される範囲を再描画
                if (cur_ != sel_)
                    Redraw(cur_, sel_);
                sel_.Copy(vp);
            }

            cur_.Copy(vp);
            UpdateCaretPos();
            view_.ScrollTo(cur_);
        }

        private void Redraw(VPos s, VPos e ){
	        int x=0, y=0; // 原点
	        view_.GetOrigin( ref x, ref y );

            Point sp = new Point(x + s.vx, y + s.vl * view_.fnt().H());
            Point ep = new Point(x + e.vx, y + e.vl * view_.fnt().H());
	        if( s > e ){ // Swap
		        //sp.X^=ep.X; ep.X^=sp.X; sp.X^=ep.X;
		        //sp.X^=ep.Y; ep.Y^=sp.Y; sp.Y^=ep.Y;

                int tmp = ep.X;
                ep.X = sp.X;
                sp.X = tmp;
                
                tmp = ep.Y;
                ep.Y = sp.Y;
                sp.Y = tmp;               
            }
	        ep.X+=2;

	        //// 手抜き16bitチェック入り…
	        int LFT = view_.left();
            int RHT = view_.right();
            int TOP = 0;
	        int BTM = view_.bottom();

	        if( sp.Y == ep.Y )
	        {
                if (Selection == SelectionType.Rectangle) {
                    
                    //Console.WriteLine("sp.Y = " + sp.Y);
                    //int ex = 0;
                    //if (cur_ > sel_) {
                    //    ex = cur_.vx;
                    //}
                    //else {
                    //    ex = sel_.vx;
                    //}
                    var v = view_.VRect;
                    var pos = view_.PointToClient(System.Windows.Forms.Cursor.Position);
                    Rectangle rc = new Rectangle(LFT, Math.Max(TOP, 0), pos.X, ep.Y + view_.fnt().H());
                    view_.Invalidate( false);
                }
                else {
                    //Rectangle rc = new Rectangle(Math.Max(LFT,sp.X), sp.Y, Math.Min(RHT,ep.X), sp.Y+view_.fnt().H());
                    Rectangle rc = new Rectangle(Math.Max(LFT, sp.X), sp.Y, Math.Min(RHT, ep.X - sp.X), view_.fnt().H());
                    view_.Invalidate(rc, false);
                    //::InvalidateRect( caret_->hwnd(), &rc, FALSE );
                    //Console.WriteLine("Rectangle sp.Y == ep.Y");
                }
	        }
	        else
	        {
                //TODO Rectangle
                if (Selection == SelectionType.Rectangle) {
                    //Rectangle rc = new Rectangle(LFT, Math.Max(TOP, sp.Y), ep.X, ep.Y + view_.fnt().H());
                    Rectangle rc = new Rectangle(LFT, Math.Max(TOP, sp.Y), ep.X, ep.Y + view_.fnt().H());
                    view_.Invalidate(rc, false);
                } else {
                    //RECT rc = { Max(LFT,sp.x), Max(TOP,sp.y), RHT, Min<int>(BTM,sp.y+view_.fnt().H()) };
                    //::InvalidateRect( caret_->hwnd(), &rc, FALSE );
                    //RECT re = { LFT, Max(TOP,ep.y), Min(RHT,ep.x), Min<int>(BTM,ep.y+view_.fnt().H()) };
                    //::InvalidateRect( caret_->hwnd(), &re, FALSE );
                    //RECT rd = { LFT, Max(TOP,rc.bottom), RHT, Min<int>((long)BTM,re.top) };
                    //::InvalidateRect( caret_->hwnd(), &rd, FALSE );
         
                    //Rectangle rc = new Rectangle(LFT, Math.Max(TOP, sp.Y), RHT, Math.Min(BTM, sp.Y + view_.fnt().H()));
                    //view_.Invalidate(rc, false);
                    //Rectangle re = new Rectangle(LFT, Math.Max(TOP, ep.Y), Math.Min(RHT, ep.X), Math.Min(BTM, ep.Y + view_.fnt().H()));
                    //view_.Invalidate(re, false);
                    //Rectangle rd = new Rectangle(LFT, Math.Max(TOP, rc.Bottom), RHT, Math.Min(BTM, re.Top));
                    //view_.Invalidate(rd, false);

                    Rectangle rc = new Rectangle(Math.Max(LFT, sp.X), Math.Max(TOP, sp.Y), RHT - Math.Max(LFT, sp.X), Math.Min(BTM, view_.fnt().H()));
                    view_.Invalidate(rc, false);
                    Rectangle re = new Rectangle(LFT, Math.Max(TOP, ep.Y), Math.Min(RHT, ep.X) - LFT, Math.Min(BTM, view_.fnt().H()));
                    view_.Invalidate(re, false);
                    Rectangle rd = new Rectangle(LFT, Math.Max(TOP, rc.Bottom), RHT - LFT, Math.Min(BTM, re.Top));
                    view_.Invalidate(rd, false);
                }
	        }
        }

        public bool getCurPos( out VPos start, out VPos end) {
            //*start = *end = &cur_;
            start = new VPos();
            end = new VPos();
            start.Copy(cur_);
            end.Copy(cur_);

            if (cur_ == sel_)//|| !caret_->isAlive() )
                return false;
            if (cur_ < sel_) {
                //*end = &sel_;
                end.Copy(sel_);
            } else {
                //*start = &sel_;
                start.Copy(sel_);
            }
            return true;
        }

        public void SetPos(VPos vp) {
            int x = 0, y = 0;
            view_.GetOrigin(ref x, ref y);
            x += vp.vx;
            y += vp.vl * view_.fnt().H();

            // 行番号ゾーンにCaretがあっても困るので左に追いやる
            if (0 < x && x < view_.left())
                x = -view_.left();

            // セット
            caret_.SetPos(x, y);
        }

        public void UpdateCaretPos() {
            int x=0, y=0;
            view_.GetOrigin(ref x, ref y);
            x += cur_.vx;
            y += cur_.vl * view_.fnt().H();

            // 行番号ゾーンにCaretがあっても困るので左に追いやる
            if (0 < x && x < view_.left())
                x = -view_.left();

            // セット
            caret_.SetPos(x, y);
            //pEvHan_->on_move(cur_, sel_);
        }

        public void Ud(int dy, Boolean select) {
            // はみ出す場合は、先頭行/終端行で止まるように制限
            VPos np = new VPos(cur_);
            //int viewvln = vlNum_;
            if (np.vl + dy < 0)
                dy = -np.vl;
            else if (np.vl + dy >= view_.vln())
                dy = view_.vln() - np.vl - 1;

            np.vl += dy;
            np.rl += dy;

            if (dy < 0) { // 上へ戻る場合
                // ジャンプ先論理行の行頭へDash!
                while (np.rl < 0)
                    np.rl += view_.rln(--np.tl);

            } else if (dy > 0) {  // 下へ進む場合
                // ジャンプ先論理行の行頭へDash!
                while (np.rl > 0)
                    np.rl -= view_.rln(np.tl++);
                if (np.rl < 0)
                    np.rl += view_.rln(--np.tl); //行き過ぎ修正
            }

            // x座標決定にかかる
            IText str = doc_.tl(np.tl);

            // 右寄せになってる。不自然？
            np.ad = (np.rl == 0 ? 0 : view_.rlend(np.tl, np.rl - 1) + 1);

            np.vx = (np.rl == 0 ? 0 : view_.fnt().CalcStringWidth(str.Substring(np.ad - 1, 1).ToString())); //TODO

            //int wrapindex = wrapList[np.tl].wrap[np.rl];

            while (np.vx < np.rx && np.ad < view_.rlend(np.tl, np.rl)) {
                //int newvx = np.vx + view_.CalcStringWidth(doc.LineList[np.tl].Text, np.ad, 1);
                //if (newvx > np.rx)
                //    break;
                //np.vx = newvx;
                //++np.ad;

                // 左寄せにしてみた。
		        int newvx;
		        //if( str[np.ad] == '\t')
			    //    newvx = view_.fnt().nextTab(np.vx);
		        //else
			    //    newvx = np.vx + view_.fnt().W(&str[np.ad]);
                if (str[np.ad] == '\t')
                    newvx = view_.fnt().nextTab(np.vx);
                else
                    newvx = np.vx + view_.fnt().CalcStringWidth(str.Substring(np.ad, 1).ToString());

                // newvx = np.vx + view_.fnt().CalcStringWidth(str.Substring(np.ad, 1).ToString());//TODO
		        if(newvx > np.rx)
			        break;
		        np.vx = newvx;
		        ++np.ad;
            }

            MoveTo(np, select);
        }

        public void Home(bool wide, bool select) {
            VPos np = new VPos();
	        np.ad = np.vx = np.rx = np.rl = 0;
	        if( wide ) // 文書の頭へ
		        np.tl = np.vl = 0;
	        else // 行の頭へ
	        {
		        // 1.07.4 --> 1.08 :: Virtual Home
		        // np.tl = cur_.tl, np.vl = cur_.vl-cur_.rl;

		        if( cur_.rl == 0 ){
                    np.tl = cur_.tl;
                    np.vl = cur_.vl - cur_.rl;
                } else{
			        //view_.ConvDPosToVPos( doc_.rightOf(DPos(cur_.tl, view_.rlend(cur_.tl,cur_.rl-1))), &np, &cur_ );
                    view_.ConvDPosToVPos(doc_.rightOf(new DPos(cur_.tl, view_.rlend(cur_.tl, cur_.rl - 1)), false), ref np, ref cur_);
                }
	        }
	        MoveTo( np, select );
        }

        public void End(bool wide, bool select) {
            VPos np = new VPos();
            if (wide) { // 文書の末尾へ
                np.tl = doc_.tln() - 1;
                np.vl = view_.vln() - 1;
            } else { // 行の末尾へ
	
                // 1.07.4 --> 1.08 :: Virtual End
                // np.tl = cur_.tl;
                // np.vl = cur_.vl + view_.rln(np.tl) - 1 - cur_.rl;

                view_.ConvDPosToVPos(new DPos(cur_.tl, view_.rlend(cur_.tl, cur_.rl)), ref np, ref cur_);
                MoveTo(np, select);
                return;
            }
            np.ad = doc_.len(np.tl);
            np.rl = view_.rln(np.tl) - 1;
            np.rx = np.vx = view_.GetLastWidth(np.tl);

            MoveTo(np, select);
        }

        public void Up(bool wide, bool select) {
            Ud(wide ? -3 : -1, select);
        }

        public void Down( bool wide, bool select )
        {
	        Ud( wide?3:1, select );
        }

        public void PageUp( bool select )
        {
	        Ud( -view_.cy()/view_.fnt().H(), select );
        }

        public void PageDown( bool select )
        {
	        Ud( view_.cy()/view_.fnt().H(), select );
        }

        public void Left( bool wide, bool select )
        {
            VPos np = new VPos();
	        if( cur_!=sel_ && !select ){
                //np = Math.Min(cur_, sel_);
                //np = cur_ > sel_ ? sel_ : cur_;
                np.Copy(Util.Min(cur_, sel_));
                np.rx = np.vx;
	        }else{
		        view_.ConvDPosToVPos( doc_.leftOf(cur_,wide), ref np, ref cur_ );
            }
	        MoveTo( np, select );
        }

        public void Right( bool wide, bool select )
        {
            VPos np = new VPos();
	        if( cur_!=sel_ && !select ){
                //np = Max(cur_, sel_);
                //np = cur_ < sel_ ? sel_ : cur_;
                np.Copy(Util.Max(cur_, sel_));
                np.rx = np.vx;
	        }else{
                //np = new VPos();
		        view_.ConvDPosToVPos( doc_.rightOf(cur_,wide), ref np, ref cur_ );
            }
	        MoveTo( np, select );
        }

        public void InputChar(char ch) {
            // 「上書モード ＆ 選択状態でない ＆ 行末でない」なら右一文字選択
            if (!bIns_ && cur_ == sel_ && doc_.len(cur_.tl) != cur_.ad)
            //if (cur_ == sel_ && doc_.len(cur_.tl) != cur_.ad)
                Right(false, true);

            // 入力
            Input(ch);
        }
        public void Input( string str )
        {
            if (cur_ == sel_)
                //doc_.Execute( Insert( cur_, str, len ) );
                //doc_.Insert(cur_, sel_, str);
                doc_.Execute(new Insert(cur_,str));
            else
                //doc_.Execute(Replace(cur_, sel_, str, len));
                //doc_.Replace(cur_, sel_, str);
                doc_.Execute(new Replace(cur_, sel_, str));
        }

        public void Input( char chr )
        {
            //unicode* ustr = new unicode[ len*4 ];
            //len = ::MultiByteToWideChar( CP_ACP, 0, str, len, ustr, len*4 );
            //Input( ustr, len );
            //delete [] ustr;
            Input(chr.ToString());
        }

        public void DelBack()
        {
	        // 選択状態なら BackSpace == Delete
	        // でなければ、 BackSpace == Left + Delete (手抜き
	        if( cur_ == sel_ )
	        {
		        if( cur_.tl==0 && cur_.ad==0 )
			        return;
		        Left( false, false );
	        }
	        Del();
        }

        public void Del()
        {
	        // 選択状態なら cur_ ～ sel_ を削除
	        // でなければ、 cur_ ～ rightOf(cur_) を削除
	        DPos dp = (cur_==sel_ ? doc_.rightOf(cur_, false) : (DPos)sel_ );
	        if( cur_ != dp ){
                //string del;
		        //doc_.Execute( Delete( cur_, dp ) );
                //doc_.Delete(new DPos(cur_.tl, cur_.ad), new DPos(dp.tl, dp.ad), out del);

                if (Selection == SelectionType.Rectangle) {
                    List<Tuple<DPos, DPos>> list =null;
                    if (cur_ < sel_) {
                        list = getRectangleDpos(cur_, sel_);
                    }
                    else {
                        list = getRectangleDpos(sel_, cur_);
                    }
                    list.Sort((x, y) => {
                        if (x.t1.ad == y.t1.ad) return 0;
                        return x.t1.ad < y.t1.ad ? 1 : -1;
                    });

                    var cmds = new List<ICommand>();
                    foreach (var item in list) {
                        //doc_.Execute(new Delete(item.t1, item.t2));
                        cmds.Add(new Delete(item.t1, item.t2));
                    }
                    doc_.Execute(cmds);
                }
                else {

                    //VPos vp = new VPos();
                    //vp.ad = dp.ad;
                    //vp.tl = dp.tl;
                    //doc_.Delete(cur_, vp, out del);
                    doc_.Execute(new Delete(cur_, dp));
                }
            }
        }
        public void DelRectangle(VPos cur, VPos sel) {
            DPos dp = (cur == sel ? doc_.rightOf(cur, false) : (DPos)sel);
            if (cur != dp) {
                List<Tuple<DPos, DPos>> list = null;
                if (cur < sel) {
                    list = getRectangleDpos(cur, sel);
                } else {
                    list = getRectangleDpos(sel, cur);
                }
                list.Sort((x, y) => {
                    if (x.t1.ad == y.t1.ad) return 0;
                    return x.t1.ad < y.t1.ad ? 1 : -1;
                });

                var cmds = new List<ICommand>();
                foreach (var item in list) {
                    cmds.Add(new Delete(item.t1, item.t2));
                }
                doc_.Execute(cmds);
            }
        }

        public void MoveText(VPos to, DPos s, DPos e) {
            DPos s1, e1;
            if (s < e) {
                s1 = s; e1 = e;
            } else {
                s1 = e; e1 = s;
            }
            var text = doc_.getText(s1, e1);
            var cmds = new List<ICommand>();
            if (to < s1) {
                cmds.Add(new Delete(s1, e1));
                cmds.Add(new Insert(to, text));
                doc_.Execute(cmds);
            } else if (to > e1) {
                cmds.Add(new Insert(to, text));
                cmds.Add(new Delete(s1, e1));
                doc_.Execute(cmds);
                if (s1.tl == e1.tl) {
                    to.ad += text.Length;
                }
                else {

                }
                Cur.Copy(to);
                Sel.Copy(Cur);             
                UpdateCaretPos();
            }  
        }

        //-------------------------------------------------------------------------
        // Viewからの指令を処理
        //-------------------------------------------------------------------------

        internal void on_setfocus()
        {
            //TODO caret
            caret_.Create(view_.fnt().H(),
                //(bIns_ ? 2 : view_.fnt().W()), view_.fnt().LogFont() );
                2);
	        UpdateCaretPos();
        }

        internal void on_killfocus()
        {
	        caret_.Destroy();
	        Redraw( cur_, sel_ );
        }

        internal void on_scroll_begin()
        {
	        caret_.Hide();
        }

        internal void on_scroll_end()
        {
	        UpdateCaretPos();
	        caret_.Show();
        }

        public void on_lbutton_down(int x, int y, bool shift)
        {
            //State = StateType.MouseDown;
	        if( !shift )
	        {
		        // これまでの選択範囲をクリア
                //if(cur_ != sel_)
		            Redraw( cur_, sel_ );

		        // 行番号ゾーンのクリックだったら、行選択モードに
		        lineSelectMode_ = ( x < view_.lna()-view_.fnt().F() );

		        // 選択開始位置を調整
		        view_.GetVPos( x, y, ref sel_ , false);
		        if( lineSelectMode_ )
			        view_.ConvDPosToVPos( new DPos(sel_.tl,0), ref sel_, ref sel_ );
		        //cur_ = sel_;
                cur_.Copy(sel_);
	        }

	        // 移動！
            dragX_ = x;
            dragY_ = y;
	        MoveByMouse( x, y );

	        //// マウス位置の追跡開始
	        //timerID_ = ::SetTimer( caret_->hwnd(), 178116, keyRepTime_, NULL );
	        //::SetCapture( caret_->hwnd() );
        }

        internal void on_button_up() {
            //if (SelectMode == SelectType.TextSelect) {
             //   SelectMode = SelectType.Normal;
            //}
            State = StateType.None;
        }

        internal void on_mouse_db_click(int x, int y) {
            // 行番号ゾーンの場合は特に何もしない
            if (view_.lna() - view_.fnt().F() < x) {
                // 行末の場合も特に何もしない
                if (cur_.ad != doc_.len(cur_.tl)) {
                    VPos np = new VPos();
                    view_.ConvDPosToVPos(doc_.wordStartOf(cur_), ref np, ref cur_);
                    MoveTo(np, false);
                    Right(true, true);
                }
            }
        }

        internal void MoveByMouse(int x, int y, bool sel) {
            dragX_ = x;
            dragY_ = y;

            VPos vp = new VPos();
            view_.GetVPos(x, y, ref vp, lineSelectMode_);
            MoveTo(vp, sel);
        }

        internal void MoveByMouse( int x, int y )
        {
            dragX_ = x;
            dragY_ = y;

	        VPos vp = new VPos();
	        view_.GetVPos( x, y, ref vp, lineSelectMode_ );
	        MoveTo( vp, true );
        }

        public void ResetCaret() {
            caret_.Create(view_.fnt().H(),
                //(bIns_ ? 2 : view_.fnt().W()), view_.fnt().LogFont() );
                2);
        }

        public void ResetPos()
        {
	        // 設定変更などに対応
	        view_.ConvDPosToVPos( cur_, ref cur_ );
	        view_.ConvDPosToVPos( sel_, ref sel_ );
	        UpdateCaretPos();
	        if( caret_.isAlive())
		        view_.ScrollTo( cur_ );
        }

        public bool ContainSelect(int x, int y, VPos cur, VPos sel) {
            //VPos vp = new VPos();
            //view_.GetVPos(x, y, ref vp, false);
            //DPos c = vp as DPos;
            //var curs = Cursor.Sort(this.Cur, this.Sel);
            //return (curs.t1 < c && c < curs.t2);
            return this.ContainSelect(x, y, cur as DPos, sel as DPos);
        }

        public bool ContainSelect(int x, int y, DPos cur, DPos sel) {
            if (Selection == SelectionType.Rectangle) {
                if (view_.VRect.SXB < x && x < view_.VRect.SXE
                    && view_.VRect.SYB < y && y < view_.VRect.SYE + view_.fnt().H()) {
                    //VPos vp=new VPos();
                    //view_.GetVPos(x,y,ref vp,false);
                    //if (view_.VRect.SXB < x && x < vp.vx + view_.VRect.XBASE) {
                    return true;
                    //}
                }
            } else {
                VPos vp = new VPos();
                view_.GetVPos(x, y, ref vp, false);
                DPos c = vp as DPos;
                var curs = Cursor.Sort(cur, sel);
                return (curs.t1 < c && c < curs.t2);
            }

            return false;
        }

        public static Tuple<DPos, DPos> Sort(DPos x, DPos y) {
            Tuple<DPos, DPos> res = new Tuple<DPos, DPos>();
            if (x < y) {
                res.t1 = x;
                res.t2 = y;
            } else {
                res.t1 = y;
                res.t2 = x;
            }
            return res;
        }

        public void on_text_update(DPos s, DPos e, DPos e2, bool mCur) {
            VPos search_base  = new VPos();
            search_base.ad = -1;

	        if( mCur && s==cur_ && e==sel_ )
	        {
		        //search_base = new VPos(cur_);
                search_base = cur_;

	        }
	        else if( mCur && s==sel_ && e==cur_ )
	        {
		        //search_base =  new VPos(sel_);
                search_base = sel_;
	        }
	        else
	        {
		        Redraw( cur_, sel_ );
		        if( mCur && caret_.isAlive() )
		        {
			        if( cur_ <= s ){
				        //search_base = &cur_;
                        search_base.Copy(cur_);
                    }
		        }
		        else
		        {
			        if( s < cur_ )
			        {
                        if (cur_ <= e) {
                            //cur_ = e2 as VPos;
                            VPos ve2 = e2 as VPos;
                            cur_.Copy(ve2);
                        } else if (cur_.tl == e.tl) {
                            cur_.tl = e2.tl;
                            cur_.ad = e2.ad + cur_.ad - e.ad;
                        } else
                            cur_.tl = e2.tl - e.tl;
				        view_.ConvDPosToVPos( cur_, ref cur_ );
			        }
                    if (s < sel_)
                        //sel_ = cur_;
                        sel_.Copy(cur_);
		        }
	        }

	        if( mCur )
	        {
		        view_.ConvDPosToVPos( e2, ref cur_, ref search_base );
		        //sel_ = cur_;
                sel_.Copy(cur_);
		        if( caret_.isAlive() )
			        view_.ScrollTo( cur_ );
	        }
	        UpdateCaretPos();
        }

        //-------------------------------------------------------------------------
        // クリップボード処理
        //-------------------------------------------------------------------------

        public void Cut()
        {
	        if( cur_ != sel_ )
	        {
		        // コピーして削除
		        Copy();
		        Del();
	        }
        }

        public void Copy()
        {
	        if( cur_==sel_ )
		        return;

            if(Selection == SelectionType.Rectangle){
                string lines =string.Empty;
                if (cur_ > sel_) {
                    lines = getRangesText(sel_, cur_);
                }
                else {
                    lines = getRangesText(cur_, sel_); 
                }
                //Clipboard.SetData("Rectangle", data);
                DataObject data = new DataObject();
                data.SetData(lines);
                data.SetData(SelectionType.Rectangle);
                Clipboard.SetDataObject(data, true);
            }else{

                DPos dm = new DPos(cur_.tl, cur_.ad);
                DPos dM = new DPos(sel_.tl, sel_.ad);
                if (cur_ > sel_) {
                    Clipboard.SetText(doc_.getText(dM, dm));
                }
                else {
                    Clipboard.SetText(doc_.getText(dm, dM));
                }
            }
        }

        public void Paste()
        {
            IDataObject data = Clipboard.GetDataObject();
            if(data.GetDataPresent(typeof(SelectionType))){
            //if (Clipboard.GetData("Rectangle") != null) {
            //if (Clipboard.GetData(DataFormats.Text) != null) {

                //var text = Clipboard.GetData("Rectangle") as string;
                //var text = Clipboard.GetData(DataFormats.Text) as string;
                var text = Clipboard.GetText();
                if (text != null) {
                    List<string> lines = text.Split(new string[]{"\r\n"}, StringSplitOptions.None).ToList<string>();

                    if (cur_ > sel_)
                        RectangleInsert(sel_, cur_, lines);       
                    else
                        RectangleInsert(cur_, sel_, lines);
                }
            } else {
                string text = Clipboard.GetText();
                if (text != null) {
                    //doc_.Replace(cur_, sel_, text);
                    doc_.Execute(new Replace(cur_, sel_, text));
                }
            }
        }

        internal void RectangleInsert(VPos s, VPos e, List<string> texts) {

            Func<int, int, int, Tuple<int, int, int>> func = (stl, srl, cnt) => {
                if (cnt - (view_.rln(stl) - srl) > 0) {
                    cnt -= (view_.rln(stl) - srl);
                    stl++;
                }
                else {
                    srl = srl + cnt;
                }

                while (cnt > 0 && stl < doc_.tln()) {
                    if (cnt > view_.rln(stl)) {
                        cnt -= view_.rln(stl);
                    }
                    else if (cnt == view_.rln(stl)) {
                        cnt -= view_.rln(stl);
                        srl = view_.rln(stl);
                        break;
                    }
                    else {
                        cnt = 0;
                        srl = view_.rln(stl) + (cnt - view_.rln(stl));
                        break;
                    }
                    if (stl + 1 >= doc_.tln()) {
                        break;
                    }
                    stl++;
                }

                return new Tuple<int, int, int>(stl, srl, cnt);
            };

            if (s == e) {

                var cmds = new List<ICommand>();

                int cnt = texts.Count;
                var etlerl = func(s.tl, s.rl, cnt);
                int etl = etlerl.t1;
                int erl = etlerl.t2;
                int rescnt = etlerl.t3;
                var list = getRectangleDpos(s, e, etl, erl);

                //add
                if (rescnt > 0) {
                    
                    int wsw = view_.fnt().W(' ');
                    int wscnt = s.vx / wsw;
                    string ws = string.Empty;
                    for (int j = 0; j < wscnt; j++) {
                        ws += " ";
                    }

                    for (int i = 0; i < rescnt; i++) {
                        DPos dp = new DPos(doc_.tln() - 1, doc_.tl(doc_.tln() - 1).Length);

                        //doc_.Execute(new Insert(dp, "\r\n" + ws + texts[rescnt - i]));
                        cmds.Add(new Insert(dp, "\r\n" + ws + texts[rescnt - i]));
                    }
                    texts.RemoveRange(texts.Count-rescnt, rescnt);
                }

                for (int i = list.Count-1; i >= 0; i--)
			    {
                    DPos dp = list[i].t1;
                    String ws= string.Empty;
                    for (int j = 0; j < list[i].t2; j++) {
                        ws += " ";
                    }
                    if (ws.Length > 0) {
                        //doc_.Execute(new Insert(dp, ws));
                        cmds.Add(new Insert(dp, ws));
                    }
                    dp.ad += ws.Length;
                    //doc_.Execute(new Insert(dp, texts[i]));
                    cmds.Add(new Insert(dp, texts[i]));
			    }
                doc_.Execute(cmds);

            } else {
                if (Selection == SelectionType.Rectangle) {

                    var cmds = new List<ICommand>();

                    var dposlist = getRectangleDpos(s, e);
                    if (dposlist.Count >= texts.Count) {
                        int c = dposlist.Count - texts.Count;
                        for (int i = 0; i < c; i++) {
                            texts.Add("");
                        }
                    }
                    else {
                        int cnt = texts.Count;// -dposlist.Count;

                        var etlerl = func(s.tl, s.rl, cnt);
                        int etl = etlerl.t1;
                        int erl = etlerl.t2;
                        int rescnt = etlerl.t3;
                        var list = getRectangleDpos(s, e, etl, erl);

                        //add
                        if (rescnt > 0) {

                            int wsw = view_.fnt().W(' ');
                            int wscnt = s.vx / wsw;
                            string ws = string.Empty;
                            for (int j = 0; j < wscnt; j++) {
                                ws += " ";
                            }

                            for (int i = 0; i < rescnt; i++) {
                                DPos dp = new DPos(doc_.tln() - 1, doc_.tl(doc_.tln() - 1).Length);

                                //doc_.Execute(new Insert(dp, "\r\n" + ws + texts[rescnt - i]));
                                cmds.Add(new Insert(dp, "\r\n" + ws + texts[rescnt - i]));
                            }
                            texts.RemoveRange(texts.Count - rescnt, rescnt);
                        }


                        for (int i = list.Count - 1 ; i >= dposlist.Count; i--) {
                            DPos dp = list[i].t1;
                            String ws = string.Empty;
                            for (int j = 0; j < list[i].t2; j++) {
                                ws += " ";
                            }
                            if (ws.Length > 0) {
                                //doc_.Execute(new Insert(dp, ws));
                                cmds.Add(new Insert(dp, ws));
                            }
                            dp.ad += ws.Length;
                            //doc_.Execute(new Insert(dp, texts[i]));
                            cmds.Add(new Insert(dp, texts[i]));
                        }
                    }

                    for (int i = dposlist.Count - 1; i >= 0; i--) {
                        string text = texts[i];
                        DPos dps = dposlist[i].t1;
                        DPos dpe = dposlist[i].t2;
                        //doc_.Execute(new Replace(dps, dpe, text));
                        cmds.Add(new Replace(dps, dpe, text));
                    }

                    doc_.Execute(cmds);
                }
                else {
                }
            }

        }

        private List<Tuple<DPos, int>> getRectangleDpos(VPos s, VPos e, int etl, int erl) {
            List<Tuple<DPos, int>> list = new List<Tuple<DPos,int>>();
            
            int H = view_.fnt().H();

            int sxb = s.vx+view_.lna();
            int syb = 0;
            if (s == e)
                syb = caret_.GetPos().Y;// + H;
            else {
                syb = view_.VRect.SYB;
            }
 
            int y = syb + H / 2;

            int wsw= view_.fnt().W(' ');

            Func<int, VPos> func = (rl) => {
                VPos vpb = new VPos();
                view_.GetVPos(sxb, y, ref vpb, false);
                y += H;

                if (vpb.ad > 0 && rl > 0 && sxb == view_.VRect.XBASE)
                    vpb.ad--;

                return vpb;
            };
            int wcnt = 0;
            if (s.tl == etl) {
                wcnt=0;
                for (int i = s.rl; i < erl; i++) {
                    VPos vp = func(i);

                    //if (i == erl-1) {
                    if (i == view_.rln(s.tl)- 1) {
                        if (s.vx > vp.vx) {
                            wcnt = (s.vx - vp.vx) / wsw; 
                        }
                    }
                    list.Add(new Tuple<DPos, int>(vp, wcnt));
                }
            } else {
                wcnt = 0;
                for (int i = s.rl; i < view_.rln(s.tl); i++) {
                    VPos vp = func(i);

                    if (i == view_.rln(s.tl)- 1) {
                        if (s.vx > vp.vx) {
                            wcnt = (s.vx - vp.vx) / wsw;
                        }
                    }
                    list.Add(new Tuple<DPos, int>(vp, wcnt));
                }

                for (int i = s.tl + 1; i < etl; i++) {
                    wcnt = 0;
                    for (int j = 0; j < view_.rln(i); j++) {
                        VPos vp = func(j);
                        //if (i == erl-1) {
                        if (j == view_.rln(i) - 1) {
                            if (s.vx > vp.vx) {
                                wcnt = (s.vx - vp.vx) / wsw;
                            }
                        }
                        list.Add(new Tuple<DPos, int>(vp, wcnt));
                    }
                }

                wcnt = 0;
                for (int i = 0; i < erl; i++) {
                    VPos vp = func(etl);
                    //if (i == erl-1) {
                    if (i == view_.rln(etl) - 1) {
                        if (s.vx > vp.vx) {
                            wcnt = (s.vx - vp.vx) / wsw;
                        }
                    }
                    list.Add(new Tuple<DPos, int>(vp, wcnt));
                }
            }
            return list;
        }

        private List<Tuple<DPos, DPos>> getRectangleDpos(VPos s, VPos e) {

            List<Tuple<DPos, DPos>> list = new List<Tuple<DPos, DPos>>();

            int sxb = view_.VRect.SXB;
            int sxe = view_.VRect.SXE;
            int syb = view_.VRect.SYB;

            int H = view_.fnt().H();
            int y = syb+H/2;

            Action<int> action = (rl) => {
                VPos vpb = new VPos();
                VPos vpe = new VPos();
                
                view_.GetVPos(sxb, y , ref vpb, false);
                view_.GetVPos(sxe, y , ref vpe, false);
                y += H;

                if (rl > 0 && sxb == view_.VRect.XBASE)
                    vpb.ad--;

                list.Add(new Tuple<DPos, DPos>(vpb, vpe));
            };

            if (s.tl == e.tl) {
                for (int i = s.rl; i <= e.rl; i++) {
                    action(i);
                }
            }
            else {
                
                for (int i = s.rl; i < view_.rln(s.tl); i++) {
                    action(i);
                }

                for (int i = s.tl + 1; i < e.tl; i++) {
                    for (int j = 0; j < view_.rln(i); j++) {
                        action(j);
                    }
                }

                for (int i = 0; i <= e.rl; i++) {
                    action(i);
                }
            }
            return list;
        }

        //TODO Rectangle
        internal string getRangesText(VPos s, VPos e) {

            List<string> texts = new List<string>();
            var list = getRectangleDpos(s, e);
            foreach (var item in list) {
                IText text = doc_.tl(item.t1.tl).Substring(item.t1.ad, item.t2.ad - item.t1.ad);
                //buff.Append(text.ToString());
                texts.Add(text.ToString());
            }

            //return buff.ToString();
            return String.Join("\r\n", texts.ToArray<string>());
        }
    }


}
