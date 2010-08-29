using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace AsControls
{
    partial class gcsTextEdit {
        //public CaretInfo cur;// = new CaretInfo();
        //public CaretInfo sel;// = new CaretInfo();
        //public Point vPos;// = new Point();

        //public void InitCaret() {
        //    cur = new CaretInfo();
        //    sel = new CaretInfo();
        //    vPos = new Point();
        //}

        //public int offset {
        //    get {
        //        int pos = 0;
        //        for (int i = 0; i < cur.tl; i++) {
        //            pos += doc.LineList[i].Length+1;
        //        }
        //        return pos + cur.ad;
        //    }
        //}

        //public void ResetPos() {
        //    // 設定変更などに対応
        //    ConvDPosToVPos(cur, ref cur);
        //    ConvDPosToVPos(sel, ref sel);
        //    UpdateCaretPos();
        //    //if (caret_->isAlive())
        //    //    view_.ScrollTo(cur_);
        //    ScrollTo(cur);
        //}

        //public void UpdateCaretPos() {
        //    int x = 0;
        //    int y = 0;
        //    GetOrigin(ref x, ref y);
        //    x += cur.vx;
        //    y += cur.vl * ViewLineHeight;

        //    this.vPos.X = x;
        //    this.vPos.Y = y;
        //}

        //public void MoveTo(Boolean select) {
        //    if (select) {
        //        int te = sel.tl;
        //    }
        //    else {
        //        cur.CopyTo(ref sel);
        //    }
        //    ScrollTo(cur);
        //    UpdateCaretPos();
        //}

        //public CaretInfo CaretLeft(int value) {
        //    CaretInfo caret = new CaretInfo(cur);
        //    //for (int i = 0; i < value; i++) {
        //    //    caret = CaretLeft(caret);
        //    //}
        //    //return caret;
        //    for (int i = 0; i < value; i++) {
        //      if (caret.ad == 0){
        //            if (caret.tl > 0) {
        //                caret.tl--;
        //                int len = doc.LineList[caret.tl].Length;
        //                caret.ad = len;
        //            }
        //        }
        //        else {
        //            caret.ad--;
        //        }
        //    }
        //    return caret;
        //}

        //public CaretInfo CaretLeft(CaretInfo caret) {
        //    CaretInfo nPos = new CaretInfo(caret);
        //    if (nPos.ad == 0)//&& lineManager.LineInfoList.Count - 1 > rPos.Y)
        //    {
        //        if (nPos.tl > 0) {
        //            nPos.tl--;
        //            int len = doc.LineList[nPos.tl].Length;
        //            nPos.ad = len;
        //        }
        //    }
        //    else {
        //        nPos.ad--;
        //    }
        //    return nPos;
        //}

        //public CaretInfo CaretRight(int value) {
        //    CaretInfo caret = new CaretInfo(cur);
        //    //for (int i = 0; i < value; i++) {
        //    //    caret = CaretRight(caret);
        //    //}
        //    //return caret;
        //    for (int i = 0; i < value; i++) {
        //        int len = doc.LineList[caret.tl].Length;
        //        if (caret.ad == len){
        //            if (caret.tl < doc.LineList.Count - 1) {
        //                caret.tl++;
        //                caret.ad = 0;
        //            }
        //        }
        //        else {
        //            caret.ad++;
        //        }
        //    }
        //    return caret;
        //}

        //public CaretInfo CaretRight(CaretInfo caret) {
        //    CaretInfo nPos = new CaretInfo(caret);
        //    int len = doc.LineList[nPos.tl].Length;
        //    if (nPos.ad == len)//&& lineManager.LineInfoList.Count - 1 > rPos.Y)
        //    {
        //        if (nPos.tl < doc.LineList.Count - 1)
        //        //if (nPos.tl < doc.LineList.Count)
        //        {
        //            nPos.tl++;
        //            nPos.ad = 0;
        //        }
        //    }
        //    else {
        //        nPos.ad++;
        //    }
        //    return nPos;
        //}

        //public void Ud(int dy, Boolean select) {
        //    // はみ出す場合は、先頭行/終端行で止まるように制限
        //    CaretInfo np = new CaretInfo(cur);
        //    int viewvln = vlNum_;
        //    if (np.vl + dy < 0)
        //        dy = -np.vl;
        //    else if (np.vl + dy >= viewvln)
        //        dy = viewvln - np.vl - 1;

        //    np.vl += dy;
        //    np.rl += dy;

        //    if (dy < 0) {
        //        // ジャンプ先論理行の行頭へDash!
        //        while (np.rl < 0)
        //            np.rl += GetvlCnt(--np.tl);

        //    }
        //    else if (dy > 0) {
        //        // ジャンプ先論理行の行頭へDash!
        //        while (np.rl > 0)
        //            np.rl -= GetvlCnt(np.tl++);
        //        if (np.rl < 0)
        //            np.rl += GetvlCnt(--np.tl); //行き過ぎ修正
        //    }

        //    np.ad = (np.rl == 0 ? 0 : wrapList[np.tl].wrap[np.rl - 1] + 1);
        //    np.vx = (np.rl == 0 ? 0 : CalcStringWidth(doc.LineList[np.tl].Text, np.ad - 1, 1));

        //    int wrapindex = wrapList[np.tl].wrap[np.rl];

        //    while (np.vx < np.rx && np.ad < wrapindex) {
        //        int newvx = np.vx + CalcStringWidth(doc.LineList[np.tl].Text, np.ad, 1);
        //        if (newvx > np.rx)
        //            break;
        //        np.vx = newvx;
        //        ++np.ad;
        //    }

        //    np.CopyTo(ref cur);
        //    UpdateCaretPos();
        //    MoveTo(select);
        //}

        //public Boolean getCurPos(out CaretInfo start, out CaretInfo end) {
        //    start = end = cur;
        //    if (cur == sel)
        //        return false;
        //    if (cur < sel)
        //        end = sel;
        //    else
        //        start = sel;

        //    return true;
        //}

        //public void UpDate(CaretInfo s, CaretInfo e, CaretInfo e2) {
        //    TextUpDate(s, e, e2);

        //    CaretInfo search_base = null;

        //    if (s == cur && e == sel) {
        //        search_base = cur;
        //    }
        //    else if (s == sel && e == cur) {
        //        search_base = sel;
        //    }
        //    else {

        //    }

        //    ConvDPosToVPos(e2, ref cur, ref search_base);
        //    cur.CopyTo(ref sel);
        //}

        //public void Redraw(VPos s, VPos e) {

        //}

        //public void Insert(int start, int end, string text) {
        //    int _offset = offset;
        //    int diff = start - _offset;
        //    int enddiff = end - _offset;

        //    CaretInfo startcaret = new CaretInfo(cur);
        //    CaretInfo endcaret = new CaretInfo(cur);

        //    if (diff > 0) {
        //        ConvDPosToVPos(CaretRight(diff), ref startcaret);
        //    }
        //    else if (diff < 0) {
        //        ConvDPosToVPos(CaretLeft(Math.Abs(diff)), ref startcaret);
        //    }
        //    if (enddiff > 0) {
        //        ConvDPosToVPos(CaretRight(enddiff), ref endcaret);
        //    }
        //    else if (enddiff < 0) {
        //        ConvDPosToVPos(CaretLeft(Math.Abs(enddiff)), ref endcaret);
        //    }

        //    Input(startcaret, endcaret, text);
        //}

        //public void Input(CaretInfo s, CaretInfo e, string text) {
        //    if (s == e) {
        //        CaretInfo ce = new CaretInfo();
        //        doc.Insert(cur, ce, text);
        //    }
        //    else {
        //        doc.Replace(s, e, text);
        //    }
        //}

        //public void Input(string text) {
        //    Input(cur, sel, text);
        //}

        //public void DelBack() {
        //    // 選択状態なら BackSpace == Delete
        //    // でなければ、 BackSpace == Left + Delete (手抜き
        //    if (cur == sel) {
        //        if (cur.tl == 0 && cur.ad == 0)
        //            return;

        //        //Left(false, false);
        //        ConvDPosToVPos(CaretLeft(1), ref cur);
        //        MoveTo(false);
        //    }
        //    Del();
        //}

        //public void Del(CaretInfo c, CaretInfo s) {
        //    //// 選択状態なら cur_ ～ sel_ を削除
        //    //// でなければ、 cur_ ～ rightOf(cur_) を削除
        //    //DPos dp = (cur == sel ? doc.rightOf(cur_) : (DPos)sel_);
        //    //if (cur_ != dp)
        //    //    doc_.Execute(Delete(cur_, dp));
        //    CaretInfo dp = (c == s ? CaretRight(1) : new CaretInfo(s));
        //    if (c != dp) {
        //        string buff;
        //        doc.Delete(c, dp, out buff);
        //    }
        //}

        //public void Del() {
        //    Del(cur, sel);
        //}

        //public IBuffer GetText(CaretInfo s, CaretInfo e) {
        //    if (s.tl == e.tl) {
        //        // 一行だけの場合
        //        //text_[s.tl].CopyAt( s.ad, e.ad-s.ad, buf );
        //        //buf[e.ad-s.ad] = L'\0';
        //        return doc.LineList[s.tl].Text.Substring(s.ad, e.ad - s.ad);
        //    }
        //    else {
        //        // 先頭行の後ろをコピー
        //        //buf += text_[s.tl].CopyToTail( s.ad, buf );
        //        //*buf++ = L'\r', *buf++ = L'\n';
        //        IBuffer buf = doc.LineList[s.tl].Text.Substring(s.ad);
        //        buf.Append("\r\n");

        //        // 途中をコピー
        //        //for( ulong i=s.tl+1; i<e.tl; i++ )
        //        //{
        //        //    buf += text_[i].CopyToTail( 0, buf );
        //        //    *buf++ = L'\r', *buf++ = L'\n';
        //        //}
        //        for (int i = s.tl + 1; i < e.tl; i++) {
        //            buf.Append(doc.LineList[i].Text.ToString());
        //            buf.Append("\r\n");
        //        }

        //        // 終了行の先頭をコピー
        //        //buf += text_[e.tl].CopyAt( 0, e.ad, buf );
        //        //*buf = L'\0';
        //        buf.Append(doc.LineList[e.tl].Text.Substring(0, e.ad).ToString());

        //        return buf;
        //    }
        //}
    }
}
