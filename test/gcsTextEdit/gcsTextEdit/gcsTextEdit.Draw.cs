using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Globalization;
using AsControls.Parser;

namespace AsControls
{

    //
    // 文字列のまま足し算を行うルーチン
    //
    class strint {
        char[] digit = new char[11];
        public strint(char[] digit) {
            this.digit = digit; 
        }
        public strint(int num) {
            int i = 11;
            while (num > 0) {
                digit[--i] = (char)('0' + (num % 10));
                num /= 10;
            }
            while (i > 0) {
                digit[--i] = ' ';
            }
        }
        //void operator++() {
        public static strint operator ++(strint s) {
            int i = 10;
            do
                if (s.digit[i] == '9')
                    s.digit[i] = '0';
                else { ++s.digit[i]; return new strint(s.digit); }
            while (s.digit[--i] != ' ');
            s.digit[i] = '1';

            strint tmp = new strint(s.digit);
            return tmp;
        }
        public void Output(Graphics g, Painter f, int x, int y) {
            //for( unicode* p=digit+10; *p!=L' '; --p,x-=f.F() )
            //    f.CharOut( *p, x, y );
            for (int p = 10; digit[p] != ' '; --p, x -= f.F()) {
                string n = digit[p].ToString();
                f.DrawLineNum(g, n, Color.Black, x, y);
            }
        }

    }

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
            p.DrawLineNumBack(g, rc);

            if (v.rc.Top < v.YMAX) {
                // 境界線表示
                int line = lna() - p.F() / 2;
                p.DrawLine(g, line, v.rc.Top, line, v.YMAX);

                // 行番号表示
                //int n = v.TLMIN + 1;
                strint n = new strint(v.TLMIN + 1);
                int y = v.YMIN;
                int edge = lna() - p.F() * 2;

                for (int i = v.TLMIN; y < v.YMAX; ++i, ++n) {
                    n.Output(g, p, edge, y);
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
            int x, x2;
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

        internal void Inv(Graphics g, int y, int xb, int xe, Painter p )
        {
            var rc = new Rectangle(
                Math.Max(left(), xb), y,
                //Math.Min(right(), xe), y + p.H() - 1);
                Math.Min(right(), xe), y + p.H());
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
        //private Point pp = new Point();
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
            int x=0;
            int xbk = 0;
            int i=0;

            Color color = Color.Empty;
            Token token = null;

            Action<IText> draw = (itext) => {
                string s = itext.ToString();

                int ci = s.IndexOfAny(cs, 0);
                if (ci < 0) {
                    p.DrawText(g, s, color, x + v.XBASE, a.top);
                    if (((token.attr.type & AttrType.Image) == AttrType.Image)
                        && i == token.ad && DrawEventHandler != null) {
                        //DrawEventHandler(g, s.Substring(token.ad, token.len), x + v.XBASE, a.top + H);
                        DrawEventHandler(g, s, x + v.XBASE, a.top + H);
                    }
                    x += p.CalcStringWidth(s);
                    i += itext.Length;
                } else {
                    foreach (var ps in Painter.parse(s, cs)) {
                        switch (ps[0]) {
                            case ' ':
                                if (ShowWhiteSpace) p.DrawHSP(g, x + v.XBASE, a.top, ps.Length);
                                x += p.CalcStringWidth(ps);
                                break;
                            case '\x3000': //'　':
                                if (ShowZenWhiteSpace) p.DrawZen(g, x + v.XBASE, a.top, ps.Length);
                                x += p.CalcStringWidth(ps);
                                break;
                            case '\t':
                                if (ShowTab) {
                                    for (int i2 = 0; i2 < ps.Length; ++i2) {
                                        int ntx = p.nextTab(x);
                                        p.DrawTab(g, x + v.XBASE, a.top, ntx - x);
                                        x = ntx;
                                    }
                                }

                                break;
                            default:
                                p.DrawText(g, ps, color, x + v.XBASE, a.top);
                                if (((token.attr.type & AttrType.Image) == AttrType.Image)
                                    && i == token.ad && DrawEventHandler != null) {
                                    DrawEventHandler(g, ps.Substring(token.ad, token.len), x + v.XBASE, a.top + H);
                                }
                                x += p.CalcStringWidth(ps);
                                break;
                        }
                        i += ps.Length;
                    }
                }

                p.DrawAttribute(g, token.attr, v.XBASE + xbk, a.top - 1, v.XBASE + x, a.top - 1);
            };


            //int aTop = a.Top;
            //int aBottom = a.Bottom;
            // 論理行単位のLoop
            //for (int tl = tmpTLMIN; a.top < v.YMAX && tl < doc_.tln(); ++tl) {
            for (int tl = tmpTLMIN; a.top < v.YMAX; ++tl) {
            //for (int tl = tmpTLMIN; a.top < v.YMAX; ++tl) {

                // 定数２
                //string str = doc_.tl(tl).ToString();
                IText str = doc_.tl(tl); 

                //if (str.Length == 0) break;

                //const uchar*   flg = doc_.pl(tl);
                int rYMAX = Math.Min(v.YMAX, a.top + rln(tl) * H);

                // 作業用変数２
                int stt = 0, end;

                int index = 0;
                int nextlen = 0;

                var rules = doc_.Rules(tl);
                if (rules.Count == 0) return;

                token = rules[index];

                int tokenad = token.ad;
                int tokenlen = token.len;
                color = token.attr.color;

                // 表示行単位のLoop
                for (int rl = 0; a.top < rYMAX; ++rl, a.top += H, a.bottom += H, stt = end)
                {
                    // 作業用変数３
                    end = rlend(tl, rl);
                    if (a.bottom <= tmpYMIN)
                        continue;

                    // テキストデータ描画
                    for (x = 0, i=stt; x <= v.XMAX && i < end; ) {
                        xbk = x;
                        nextlen = end - (tokenad + tokenlen);
                        //nextlen = end - attrindex;
                        if (nextlen >= 0) {
                            var text = str.Substring(tokenad, tokenlen);
                            //draw(str, tokenad, tokenlen);
                            draw(text);
                            stt = i;

                            if (nextlen > 0) {
                                index++;
                                token = rules[index];
                                tokenad = token.ad;
                                tokenlen = token.len;
                                color = token.attr.color;
                            }
                            if (rln(tl) > 0 && nextlen == 0 && i == end && index < rules.Count-1) {
                                index++;
                                token = rules[index];
                                tokenad = token.ad;
                                tokenlen = token.len;
                                color = token.attr.color;
                            }

                        } else {
                            //over
                            var text = str.Substring(tokenad, end - tokenad);
                            draw(text);
                            //draw(str, tokenad, end - tokenad);
                            stt = i;

                            tokenlen -= text.Length; //(end - attrindex);
                            tokenad = end;
                        }
                    }

                    // 選択範囲だったら反転
                    //if( v.SYB<=a.top && a.top<=v.SYE )
                    //    Inv( a.top, a.top==v.SYB?v.SXB:(v.XBASE),
                    //                a.top==v.SYE?v.SXE:(v.XBASE+x), p );
                    if (v.SYB <= a.top && a.top <= v.SYE) {
                        //TODO Rectangle
                        if (cur_.Selection == SelectionType.Rectangle) {
                            int cx = cur_.caret.GetPos().X;
                            if (cur_.Cur.tl == tl && cur_.Cur.rl == rl) {
                                for (int selY = v.SYB; selY <= v.SYE; selY += H) {

                                    //VPos vpb = new VPos();
                                    //GetVPos(v.SXB, selY, ref vpb, false);
                                    VPos vpe = new VPos();
                                    GetVPos(cx, selY, ref vpe, false);
                                    //int w = CalcLineWidth(doc_.tl(vpe.tl).ToString(), doc_.tl(vpe.tl).Length);
                                    if (CalcLineWidth(doc_.tl(vpe.tl).ToString(), doc_.tl(vpe.tl).Length) + v.XBASE < cx) {
                                        Inv(g, selY, Math.Min(v.XBASE + cur_.Sel.vx, cx),
                                                Math.Max(v.XBASE + cur_.Sel.vx, cx), p);
                                    } else {
                                        Inv(g, selY, Math.Min(v.XBASE + cur_.Sel.vx, v.XBASE + vpe.vx),
                                                Math.Max(v.XBASE + cur_.Sel.vx, v.XBASE + vpe.vx), p);
                                    }

                                    //Inv(g, selY, Math.Min(v.XBASE + cur_.Sel.vx, cx),
                                    //        Math.Max(v.XBASE + cur_.Sel.vx, cx), p);    
                                }
                            } 
                        } else {
                            Inv(g, a.top, a.top == v.SYB ? v.SXB : (v.XBASE),
                                        a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
                        }
                    }
                    
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
                        p.DrawReturn(g, x + v.XBASE, a.top - H);
                    }
                    if (cur_.Selection == SelectionType.Normal && v.SYB < a.top && a.top <= v.SYE && ShowReturn)
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
