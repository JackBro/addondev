using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Globalization;

namespace AsControls
{
    public delegate void DrawEventHandler(Graphics g, string line, int x,int y);
    public partial class gcsTextEdit
    {
        public event DrawEventHandler DrawEventHandler;

        /// <summary>
        /// pixel
        /// </summary>
        public int BackDrawSize { get; set; }

        private void DrawLNA(Graphics g, VDrawInfo v, Painter p ){
            // 背面消去
            Rectangle rc = new Rectangle(v.rc.Left, v.rc.Top, lna(), v.rc.Bottom);
            //p.Fill(rc);
            //g.FillRectangle(bb, rc);
            p.DrawLineNumBack(g, rc);

            if (v.rc.Top < v.YMAX) {
                // 境界線表示
                int line = lna() - p.F() / 2;
                //int line = lna() - p.F();
                p.DrawLine(g, line, v.rc.Top, line, v.YMAX);
                //p.SetColor(LN);

                //StringFormat sf = new StringFormat();
                //sf.Alignment = StringAlignment.Far;

                // 行番号表示
                int n = v.TLMIN + 1;
                int y = v.YMIN;
                int edge = lna() - p.F() * 2;

                for (int i = v.TLMIN; y < v.YMAX; ++i, ++n) {
                    //n.Output(p, edge, y);
                    //y += p.H() * rln(i);
                    //g.DrawString(n.ToString(), this.Font, ppp, edge, y, sf);
                    //g.DrawString(n.ToString(), this.Font, ppp, edge, y, sf);
                    p.DrawLineNum(g, n.ToString(), Color.Black, edge, y);
                    y += p.H() * rln(i);
                }
            }
        }

        private void DrawTXT2(Graphics g, VDrawInfo v, Painter p) {

            // 定数１
            //	const int   TAB = p.T();
            int H = p.H();
            int TLM = doc_.tln()-1;

            // 作業用変数１
            Win32API.RECT a = new Win32API.RECT { left = 0, top = v.YMIN, right = 0, bottom=v.YMIN + p.H() };
            //Rectangle  a = new Rectangle( 0, v.YMIN, 0, v.YMIN+p.H() );
            //Rectangle a = new Rectangle(0, 0,,v.YMIN);
            //int clr = -1;
            int   x, x2;
            int i, i2;

            //int aTop = a.Top;
            //int aBottom = a.Bottom;
            // 論理行単位のLoop
            for( int tl=v.TLMIN; a.top<v.YMAX; ++tl )
            {
                // 定数２
                string str = doc_.tl(tl).ToString();
                //const uchar*   flg = doc_.pl(tl);
                int rYMAX = Math.Min(v.YMAX, a.top + rln(tl) * H);

                // 作業用変数２
                int stt=0, end, t, n;

                // 表示行単位のLoop
                for (int rl = 0; a.top < rYMAX; ++rl, a.top += H, a.bottom += H, stt = end)
                //for (int rl = 0; a.Top < rYMAX; rl++, aTop += H, aBottom += H, stt = end)
                {
                    // 作業用変数３
                    end = rlend(tl,rl);
                    if( a.bottom<=v.YMIN )
                        continue;

                    // テキストデータ描画
                    for( x2=x=0,i2=i=stt; x<=v.XMAX && i<end; x=x2,i=i2 )
                    {
                        // n := 次のTokenの頭
                        //t = (flg[i]>>5);
                        t = 0;
                        n = i + t;
                        if( n >= end )
                            n = end;
                        else if( t==7 || t==0 )
                            //while( n<end && (flg[n]>>5)==0 )
                            while( n<end)
                                ++n;

                        // x2, i2 := このTokenの右端
                        i2 ++;
                        x2 = (str[i]=='\t' ? p.nextTab(x2) : x2+p.W(str[i]));
                    //	if( x2 <= v.XMIN )
                    //		x=x2, i=i2;
                        while( i2<n && x2<=v.XMAX )
                            x2 += p.W( str[i2++] );

                        // 再描画すべき範囲と重なっていない
                        if( x2<=v.XMIN )
                            continue;

                        // x, i := このトークンの左端
                        if( x<v.XMIN )
                        {
                            // tabの分が戻りすぎ？
                            x = x2; i = i2;
                            while( v.XMIN<x )
                                x -= p.W( str[--i] );
                        }
 
                        // 背景塗りつぶし
                        a.left  = x + v.XBASE;
                        a.right = x2 + v.XBASE;
                        //p.Fill( a );
                        //g.FillRectangle(bb, a.left, a.top, a.right-a.left, a.bottom-a.top);

                        // 描画
                        //switch( str[i] )
                        //{
                        //case '\t':
                        //    if( p.sc(scTAB) )
                        //    {
                        //        p.SetColor( clr=CTL );
                        //        for( ; i<i2; ++i, x=p.nextTab(x) )
                        //            p.CharOut( L'>', x+v.XBASE, a.top );
                        //    }
                        //    break;
                        //case ' ':
                        //    if( p.sc(scHSP) )
                        //        p.DrawHSP( x+v.XBASE, a.top, i2-i );
                        //    break;
                        //    case '　'://0x3000://L'　':
                        //    if( p.sc(scZSP) )
                        //        p.DrawZSP( x+v.XBASE, a.top, i2-i );
                        //    break;
                        //default:
                        //    if( clr != (flg[i]&3) )
                        //        p.SetColor( clr=(flg[i]&3) );
                        //    p.StringOut( str+i, i2-i, x+v.XBASE, a.top );
                        //    //p.StringOut( str+i, i2-i, x+v.XBASE, a.top );
                        //    // 何故だか２度描きしないとうまくいかん…
                        //    break;
                        //}
                        if (str.Contains("img")) {
                            //g.DrawImage(image, x + v.XBASE, a.top);
                        }
                        p.DrawText(g, str.Substring(i, i2 - i), Color.Black, x + v.XBASE, a.top);

                    }

                    // 選択範囲だったら反転
                    //if( v.SYB<=a.top && a.top<=v.SYE )
                    //    Inv( a.top, a.top==v.SYB?v.SXB:(v.XBASE),
                    //                a.top==v.SYE?v.SXE:(v.XBASE+x), p );
                    if (v.SYB <= a.top && a.top <= v.SYE)
                        Inv(g, a.top, a.top == v.SYB ? v.SXB : (v.XBASE),
                                    a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
                    // 行末より後ろの余白を背景色塗
                    if( x<v.XMAX )
                    {
                        a.left = v.XBASE + Math.Max( v.XMIN, x );
                        a.right= v.XBASE + v.XMAX;
                        //p.Fill( a );
                        //g.FillRectangle(bb, a.left, a.top, a.right - a.left, a.bottom - a.top);
                    }
                }

                //// 行末記号描画反転
                //SpecialChars sc = (tl==TLM ? scEOF : scEOL);
                //if( i==doc_.len(tl) && -32768<x+v.XBASE )
                //{
                //    if( p.sc(sc) )
                //    {
                //        static const unicode* const sstr[] = { L"[EOF]", L"/" };
                //        static const int slen[] = { 5, 1 };
                //        p.SetColor( clr=CTL );
                //        p.StringOut( sstr[sc], slen[sc], x+v.XBASE, a.top-H );
                //    }
                //    if( v.SYB<a.top && a.top<=v.SYE && sc==scEOL )
                //        Inv( a.top-H, x+v.XBASE, x+v.XBASE+p.Wc('/'), p );
                //}
            }

            // EOF後余白を背景色塗
            if (a.top < v.rc.Bottom)
            {
                a.left   = v.rc.Left;
                a.right  = v.rc.Right;
                a.bottom = v.rc.Bottom;
                //p.Fill( a );
                //g.FillRectangle(bb, a.left, a.top, a.right - a.left, a.bottom - a.top);
            }
        }

        private void Inv(Graphics g, int y, int xb, int xe, Painter p )
        {
            var rc = new Rectangle(
                Math.Max(left(), xb), y,
                Math.Min(right(), xe), y + p.H() - 1);
	        p.Invert(g, rc );
        }

        private Tuple<int, int> l(int utl, int vrl) {
            int upsize=500;
            Painter p = cvs_.getPainter();
            int H=p.H();
            int rn=0;
            //int start = vrl > 0 ? utl - 1 : utl;
            //int start = vrl > 0 ? utl : utl-1;
            int start = utl - 1;
            //start = start < 0 ? 0 : start;
            //for (; start > 0; start--) {
            for (; start >= 0; start--) {

                rn += rln(start);
                if (upsize - rn * H <= 0) {
                    break;
                }
            }
            start = start < 0 ? 0 : start;
            return new Tuple<int, int>(start, rn );
        }

        private static char[] cs = { '\t', ' ', '\x3000' };

        private void DrawTXT3(Graphics g, VDrawInfo v, Painter p) {
            //g.FillRectangle(bb, v.rc);
            // 定数１
            //	const int   TAB = p.T();
            int H = p.H();
            int TLM = doc_.tln() - 1;

            int tmpYMIN = 0;
            int tmpTLMIN = 0;
            if (DrawEventHandler == null) {
                tmpYMIN = v.YMIN;
                tmpTLMIN = v.TLMIN;
            }
            else {
                var tuple = l(udScr_tl_, udScr_vrl_);
                tmpYMIN = -(tuple.t2 + udScr_vrl_) * H;
                tmpTLMIN = tuple.t1;
            }
            //TODO tmp scroll
            //int tmpYMIN = v.YMIN;
            //int tmpTLMIN = v.TLMIN;
            //v.YMIN = -(v.TLMIN) * H;
            //v.TLMIN = 0;//doc_.tln()

            //var tuple = l(udScr_tl_, udScr_vrl_);
            //int tmpYMIN = -(tuple.t2 + udScr_vrl_) * H;
            //int tmpTLMIN = tuple.t1;
            //int testYMIN = -(tuple.t2 + udScr_vrl_) * H;
            //int testTLMIN = tuple.t1;


            // 作業用変数１
            //Win32API.RECT a = new Win32API.RECT { left = 0, top = v.YMIN, right = 0, bottom = v.YMIN + p.H() };
            Win32API.RECT a = new Win32API.RECT { left = 0, top = tmpYMIN, right = 0, bottom = tmpYMIN + p.H() };
            //Rectangle  a = new Rectangle( 0, v.YMIN, 0, v.YMIN+p.H() );
            //Rectangle a = new Rectangle(0, 0,,v.YMIN);
            //int clr = -1;
            int x=0;//, x2;
            int i=0;//, i2;

            Color color = Color.Black;
            //Attribute attr;

            //int aTop = a.Top;
            //int aBottom = a.Bottom;
            // 論理行単位のLoop
            for (int tl = tmpTLMIN; a.top < v.YMAX; ++tl) {

                // 定数２
                string str = doc_.tl(tl).ToString();

                //if (str.Length == 0) break;

                //const uchar*   flg = doc_.pl(tl);
                int rYMAX = Math.Min(v.YMAX, a.top + rln(tl) * H);

                // 作業用変数２
                int stt = 0, end;//, t, n;
                //int sss = 0;

                int attri = 0;
                int nextlen = 0;

                var ruls = doc_.Rules(tl);
                int attrindex = ruls[attri].ad;
                int attrlen = ruls[attri].len;
                color = ruls[attri].attr.color;

                // 表示行単位のLoop
                for (int rl = 0; a.top < rYMAX; ++rl, a.top += H, a.bottom += H, stt = end)
                {
                    //if(ruls[rl].ad

                    // 作業用変数３
                    end = rlend(tl, rl);
                    if (a.bottom <= tmpYMIN)
                        continue;


                    // テキストデータ描画
                    //for (x2 = x = 0, i2 = i = stt; x <= v.XMAX && i < end; x = x2, i = i2) {
                    for (x = 0, i=stt; x <= v.XMAX && i < end; ) {

                        nextlen = end - (attrindex + attrlen);
                        //nextlen = end - attrindex;
                        if (nextlen >= 0) {

                            string s = str.Substring(attrindex, attrlen);

                            int ci = s.IndexOfAny(cs, 0);
                            if (ci < 0) {
                                p.DrawText(g, s, color, x + v.XBASE, a.top);
                                if (ruls[attri].attr.isimage && i==ruls[attri].ad && DrawEventHandler != null) {
                                    DrawEventHandler(g, str.Substring(ruls[attri].ad, ruls[attri].len), x + v.XBASE, a.top + H);
                                }
                                x += p.CalcStringWidth(s);
                                i += s.Length;
                                stt = i;
                            } else {
                                foreach (var ps in Painter.parse(s, cs)) {
                                    switch (ps[0]) {
                                        case ' ':
                                            if(ShowWhiteSpace) p.DrawHSP(g, x + v.XBASE, a.top, ps.Length);
                                            x += p.CalcStringWidth(ps);
                                            break;
                                        case '\x3000': //0x3000://L'　':
                                            if(ShowZenWhiteSpace) p.DrawZen(g, x + v.XBASE, a.top, ps.Length);
                                            x += p.CalcStringWidth(ps);
                                            break;
                                        case '\t':
                                            //TODO tab
                                            //if(ShowTab) p.DrawTab(g, x + v.XBASE, a.top, ps.Length);
                                            //x += p.T() * ps.Length;//p.CalcStringWidth(s[ci].ToString());
                                            if (ShowTab) {
                                                //for (; i < i2; ++i, x = p.nextTab(x))
                                                //	p.CharOut( L'>', x+v.XBASE, a.top );
                                                for (int i2 = 0; i2 < ps.Length; ++i2){//, x = p.nextTab(x)) {
                                                    //p.DrawTab(g, x + v.XBASE, a.top);
                                                    int ntx = p.nextTab(x);
                                                    p.DrawTab(g, x + v.XBASE, a.top, ntx - x);
                                                    x = ntx;
                                                }
                                                //p.DrawTab(g, x + v.XBASE, a.top, ps.Length);
                                            }
                                            
                                            break;
                                        default:
                                            //string s2 = s.Substring(i2, ci - i2);
                                            p.DrawText(g, ps, color, x + v.XBASE, a.top);
                                            if (ruls[attri].attr.isimage && i == ruls[attri].ad && DrawEventHandler != null) {
                                                DrawEventHandler(g, str.Substring(ruls[attri].ad, ruls[attri].len), x + v.XBASE, a.top + H);
                                            }
                                            x += p.CalcStringWidth(ps);
                                            break;
                                    }
                                    i += ps.Length;
                                }
                            }

                            stt = i;

                            if (nextlen > 0) {
                                attri++;
                                attrindex = ruls[attri].ad;
                                attrlen = ruls[attri].len;
                                color = ruls[attri].attr.color;
                            }
                            if (rln(tl) > 0 && nextlen == 0 && i == end && attri < ruls.Count-1) {
                                attri++;
                                attrindex = ruls[attri].ad;
                                attrlen = ruls[attri].len;
                                color = ruls[attri].attr.color;
                            }

                        } else {
                            //over
                            string s = str.Substring(attrindex, end - attrindex);
                            //string s = str.Substring(attrindex, end);
                            p.DrawText(g, s, color, x + v.XBASE, a.top);
                            if (ruls[attri].attr.isimage && i == ruls[attri].ad && DrawEventHandler != null) {
                                DrawEventHandler(g, str.Substring(ruls[attri].ad, ruls[attri].len), x + v.XBASE, a.top + H);
                            }
                            //nextlen = attrlen - (end - attrindex);
                            x += p.CalcStringWidth(s);
                            i += s.Length;
                            stt = i;

                            attrlen -= s.Length; //(end - attrindex);
                            attrindex = end;

                            //attrlen = attrlen - end;
                        }
                    }

                    // 選択範囲だったら反転
                    //if( v.SYB<=a.top && a.top<=v.SYE )
                    //    Inv( a.top, a.top==v.SYB?v.SXB:(v.XBASE),
                    //                a.top==v.SYE?v.SXE:(v.XBASE+x), p );
                    if (v.SYB <= a.top && a.top <= v.SYE) {
                        //TODO Rectangle
                        if (cur_.SelectMode == SelectType.Rectangle) {
                            if (cur_.Cur.tl == tl && rln(tl) - 1 == rl) {
                                //Inv(g, a.top, v.SXB ,
                                //    a.top == v.SYE ? v.SXE : (v.XBASE + x), p);

                                if (v.SXB == v.XBASE) {
                                    Inv(g, a.top, v.XBASE, a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
                                }
                                else {
                                    VPos vpb = new VPos();
                                    GetVPos(v.SXB, a.top, ref vpb, false);
                                    Inv(g, a.top, v.XBASE + vpb.vx, a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
                                }
                            } else {
                                //TODO Rectangle
                                VPos vpe = new VPos();
                                GetVPos(v.SXE, a.top, ref vpe, false);
                                if (v.SXB == v.XBASE) {
                                    Inv(g, a.top, v.XBASE, v.XBASE + vpe.vx, p);
                                } else {
                                    VPos vpb = new VPos();
                                    GetVPos(v.SXB, a.top, ref vpb, false);
                                    Inv(g, a.top, v.XBASE + vpb.vx, v.XBASE + vpe.vx, p);
                                }
                            }
                        } else {
                            Inv(g, a.top, a.top == v.SYB ? v.SXB : (v.XBASE),
                                        a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
                        }
                    }

                    //sss = end;
                    
                    // 行末より後ろの余白を背景色塗
                    if (x < v.XMAX) {
                        a.left = v.XBASE + Math.Max(v.XMIN, x);
                        a.right = v.XBASE + v.XMAX;
                        //p.Fill( a );
                        //g.FillRectangle(bb, a.left, a.top, a.right - a.left, a.bottom - a.top);
                    }
                }

                //// 行末記号描画反転
                //SpecialChars sc = (tl==TLM ? scEOF : scEOL);
                //if( i==doc_.len(tl) && -32768<x+v.XBASE )
                //{
                //    if( p.sc(sc) )
                //    {
                //        static const unicode* const sstr[] = { L"[EOF]", L"/" };
                //        static const int slen[] = { 5, 1 };
                //        p.SetColor( clr=CTL );
                //        p.StringOut( sstr[sc], slen[sc], x+v.XBASE, a.top-H );
                //    }
                //    if( v.SYB<a.top && a.top<=v.SYE && sc==scEOL )
                //        Inv( a.top-H, x+v.XBASE, x+v.XBASE+p.Wc('/'), p );
                //}
                if( i==doc_.len(tl) && -32768<x+v.XBASE ){
                    if (ShowReturn && tl != TLM) {
                        //static const unicode* const sstr[] = { L"[EOF]", L"/" };
                        //static const int slen[] = { 5, 1 };
                        //p.SetColor( clr=CTL );
                        //p.StringOut( sstr[sc], slen[sc], x+v.XBASE, a.top-H );
                        p.DrawReturn(g, x + v.XBASE, a.top - H);
                    }
                    if (v.SYB < a.top && a.top <= v.SYE && ShowReturn)
                        Inv(g, a.top - H, x + v.XBASE, x + v.XBASE + p.W(), p);
                }
            }

            // EOF後余白を背景色塗
            if (a.top < v.rc.Bottom) {
                a.left = v.rc.Left;
                a.right = v.rc.Right;
                a.bottom = v.rc.Bottom;
                //p.Fill( a );
                //g.FillRectangle(bb, a.left, a.top, a.right - a.left, a.bottom - a.top);
            }

            //v.YMIN = tmpYMIN;
            //v.TLMIN = tmpTLMIN;
        }
    }
}
