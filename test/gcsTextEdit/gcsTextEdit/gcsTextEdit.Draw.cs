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
        event DrawEventHandler DrawEventHandler;
       // private IntPtr hdc;
       // private IntPtr hfont;

        //private Brush cursorBrush = new SolidBrush(Color.Black);
        //private Pen spPen;
        //private Brush tabbrush = new SolidBrush(Color.Blue);
        private Brush ppp = new SolidBrush(Color.DarkGreen);
        Brush bb = new SolidBrush(Color.White);
        //private const Char ZSP = '　';
        //private const Char HSP = ' ';
        //private int ZSPW;
        //private int HSPW;

        //private int figureWidth;

        //private int tabNum =4;
        //private int lineHeight;

        //private void initDraw()
        //{
        //    hdc = Win32API.GetDC(Handle);
        //    hfont = this.Font.ToHfont();
        //    spPen = new Pen(Color.Gray);

        //    initTable();

        //    vRect.NumLineLeft = figureWidth*2;
        //}

        //private void deleteDraw()
        //{
        //    Win32API.ReleaseDC(Handle, hdc);
        //}

        //private void initTable()
        //{
        //    lineHeight = getLineHeight(this.Font);
        //    ZSPW = CalcCharWidth(ZSP);
        //    HSPW = CalcCharWidth(HSP);
        //    figureWidth = CalcCharWidth('0');
        //}

        //private int getLineHeight(Font font)
        //{
        //    // とりあえず四捨五入処理付きで一行の高さを取得
        //    return (int)(font.GetHeight() + 0.5f);
        //}

        //public Boolean CalcNumLineWidth()
        //{
        //    int prev = vRect.NumLineLeft;
        //    if (showNumLine)
        //    {
        //        int ln = doc.tlNum.ToString().Length;
        //        vRect.NumLineLeft = (1 + ln) * figureWidth;
        //    }
        //    else
        //    {
        //        vRect.NumLineLeft = 0;
        //    }
        //    return prev != vRect.NumLineLeft ? true : false;
        //}

        //private void drawText(Graphics g, string text, Font font, Color color, int X, int Y)
        //{
        //    TextRenderer.DrawText(g,
        //       text,
        //       font,
        //       new Point(X, Y),
        //       color,
        //       TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix);
        //       //TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.Left| TextFormatFlags.Internal | TextFormatFlags.NoPrefix); 
        //}

        //private void drawUnderLine(Graphics g, IBuffer text, Font font, Color color, int X, int Y)
        //{
        //    g.DrawLine(spPen, X, Y + font.Height, X + CalcStringWidth(text), Y + font.Height);
        //}

        //private void drawZen(Graphics g, int X, int Y, int len)
        //{
        //    for (int i = 0; i < len; i++)
        //    {
        //        g.DrawRectangle(spPen, X + i * ZSPW + 2, Y + 2, ZSPW - 4, lineHeight - 4);
        //    }
        //}

        //private void drawTab(Graphics g, int X, int Y, int len)
        //{
        //    for (int i = 0; i < len; i++)
        //    {
        //        g.DrawString(">", this.Font, tabbrush, X + i * tabNum * HSPW, Y);
        //    }
        //}

        //private void drawEOF(Graphics g, int X, int Y)
        //{
        //    //g.DrawString("↓", this.Font, tabbrush, X - 2, Y);
        //}



        //private void drawCaret(Graphics g, int X, int Y)
        //{
        //    g.FillRectangle(cursorBrush, X, Y, 2, lineHeight);
        //}


        //private void drawLNA(Graphics g, VDrawInfo v)
        //{
        //    if (v.rc.Top < v.YMAX)
        //    {
        //        Rectangle rc = new Rectangle(0, 0, v.NumLineLeft-2, v.YMAX);
        //        g.FillRectangle(bb, rc);
                
        //        // 境界線表示
        //        //int line = lna() - p.F() / 2;
        //        //p.DrawLine(line, v.rc.Top, line, v.YMAX);
        //        //p.SetColor(LN);
        //        g.DrawLine(spPen, v.NumLineLeft-2, v.rc.Top, v.NumLineLeft-2, v.YMAX);


        //        // 行番号表示
        //        int n = v.TLMIN + 1;
        //        int y = v.YMIN;

        //        //int edge = lna() - p.F() * 2;
        //        ///int edge = v.NumLineLeft;// - figureWidth * 2;
        //        int edge = v.NumLineLeft;// - figureWidth * 2;
        //        StringFormat sf = new StringFormat();
        //        sf.Alignment = StringAlignment.Far;  
        //        for (int i = v.TLMIN; y < v.YMAX; ++i, ++n)
        //        {
        //            //n.Output(p, edge, y);
        //            //sf.LineAlignment = StringAlignment.Center;
        //            g.DrawString(n.ToString(), this.Font, ppp, new Point(edge, y), sf);
        //            //drawText(g, n.ToString(), this.Font, Color.Red, edge, y);
        //            y += lineHeight * GetvlCnt(i);
        //        }
        //    }
        //}

        private void DrawLNA(Graphics g, VDrawInfo v, Painter p ){
            // 背面消去
            Rectangle rc = new Rectangle(v.rc.Left, v.rc.Top, lna(), v.rc.Bottom);
            //p.Fill(rc);
            g.FillRectangle(bb, rc);

            if (v.rc.Top < v.YMAX) {
                // 境界線表示
                int line = lna() - p.F() / 2;
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

        private void DrawTXT(Graphics g, VDrawInfo v, Painter p) {
            int ytop = v.YMIN;
            int ybuttom = ytop + v.YMAX;

            for (int i = v.TLMIN; ytop < v.YMAX; i++) {
                int lx = 0;
                IBuffer buf = doc_.tl(i);
                List<AttributeInfo> attributeInfoList = doc_.AttributeList(i);
                if (attributeInfoList.Count == 0) {
                    if (doc_.LastLine(i)) {
                    } else {
                        p.DrawReturn(g, lx + v.XBASE, ytop);
                    }
                    ytop +=p.H();
                    continue;
                }

                int wcs = 0;
                //int wcs = hScrollBar.Value/fnt().W(); //Math.Abs(v.XBASE) / p.H();
                int sindex = 0;

                int rYMAX = Math.Min(v.YMAX, ytop + rln(i) * p.H());

                for (int j = 0; ytop < rYMAX; j++, ytop += p.H()) {
                    int wc = 0;// doc.wrapInfo[i].wrap[j];
                    IBuffer text = null;
                    //wc = rlend(i,j);// wrapList[i].wrap[j];
                    wc = rlend(i, j);// wrapList[i].wrap[j];
                    text = buf.Substring(wcs, wc - wcs);

                    lx = 0; //折り返しでx位置リセット
                    IBuffer fs;
                    while (true) {
                        int len = attributeInfoList[sindex].len;
                        Color c = attributeInfoList[sindex].forecolor;

                        if (sindex + len > wc) {
                            fs = text.Substring(sindex - wcs, wc - sindex);
                            len = fs.Length;
                        } else {
                            fs = text.Substring(sindex - wcs, len);
                        }

                        switch (attributeInfoList[sindex].Token) {
                            case TokenType.TXT:
                                //drawText(g, fs.ToString(), Font, c, lx + v.XBASE, ytop-2);
                                p.DrawText(g, fs.ToString(), c, lx + v.XBASE, ytop);
                                break;
                            case TokenType.CLICKABLE:
                                //drawText(g, fs.ToString(), Font, c, lx + v.XBASE, ytop);
                                p.DrawText(g, fs.ToString(), c, lx + v.XBASE, ytop);
                                //drawUnderLine(g, fs, Font, c, lx + v.XBASE, ytop);
                                break;
                            case TokenType.TAB:
                                p.DrawTab(g, lx + v.XBASE, ytop, len);
                                break;
                            case TokenType.ZSP:
                                p.DrawZen(g, lx + v.XBASE, ytop, len);
                                break;
                        }

                        sindex += len;
                        lx += p.CalcStringWidth(fs.ToString()); //TODO
                        if (sindex >= wc) {
                            break;
                        }
                    }
                    wcs = wc;

                    // 選択範囲だったら反転
                    if (v.SYB <= ytop && ytop <= v.SYE && !isdropfile) {
                        //Inv(a.top, a.top == v.SYB ? v.SXB : (v.XBASE),
                        //            a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
                        //inline void ViewImpl::Inv( int y, int xb, int xe, Painter& p )
                        //{
                        //    RECT rc = {
                        //        Max( left(),  xb ), y,
                        //        Min( right(), xe ), y+p.H()-1
                        //    };
                        //    p.Invert( rc );
                        //}

                        //LONG left;
                        //LONG top;
                        //LONG right;
                        //LONG bottom;
                        int le = ytop == v.SYB ? v.SXB : v.XBASE;
                        int to = ytop;
                        //int ri = ytop == v.SYE ? v.SXE : (v.XBASE + p.CalcStringWidth(text));
                        int ri = ytop == v.SYE ? v.SXE : (v.XBASE + p.CalcStringWidth(text.ToString())); //TODO
                        int bo = ytop + p.H();
                        Rectangle rect = new Rectangle(le, to, ri, bo);
                        p.Invert(g, rect);
                    }
                }

                if (doc_.LastLine(i)) {

                } else {
                    //p.drawReturn(g, lx + vRect.XBASE, ytop - p.H());
                    p.DrawReturn(g, lx + v.XBASE, ytop - p.H());
                }
            }	        
        }

        private Bitmap image = new Bitmap("test.png");
        private void DrawTXT2(Graphics g, VDrawInfo v, Painter p) {
            //AutoScrollOffset= new Point(AutoScrollOffset.X,AutoScrollOffset.Y+ 10);

            //g.FillRectangle(bb, v.rc);
            // 定数１
        //	const int   TAB = p.T();
            int H = p.H();
            int TLM = doc_.tln()-1;

            // 作業用変数１
            Win32API.RECT a = new Win32API.RECT { left = 0, top = v.YMIN, right = 0, bottom=v.YMIN + p.H() };
            //Rectangle  a = new Rectangle( 0, v.YMIN, 0, v.YMIN+p.H() );
            //Rectangle a = new Rectangle(0, 0,,v.YMIN);
            int clr = -1;
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
                            g.DrawImage(image, x + v.XBASE, a.top);
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

        private char[] ch = { '\t', ' ', '　' };
        private void DrawTXT3(Graphics g, VDrawInfo v, Painter p) {
            //g.FillRectangle(bb, v.rc);
            // 定数１
            //	const int   TAB = p.T();
            int H = p.H();
            int TLM = doc_.tln() - 1;

            // 作業用変数１
            Win32API.RECT a = new Win32API.RECT { left = 0, top = v.YMIN, right = 0, bottom = v.YMIN + p.H() };
            //Rectangle  a = new Rectangle( 0, v.YMIN, 0, v.YMIN+p.H() );
            //Rectangle a = new Rectangle(0, 0,,v.YMIN);
            int clr = -1;
            int x, x2;
            int i, i2;

            Color color = Color.Black;
            Attribute attr;

            //int aTop = a.Top;
            //int aBottom = a.Bottom;
            // 論理行単位のLoop
            for (int tl = v.TLMIN; a.top < v.YMAX; ++tl) {




                // 定数２
                string str = doc_.tl(tl).ToString();
                //const uchar*   flg = doc_.pl(tl);
                int rYMAX = Math.Min(v.YMAX, a.top + rln(tl) * H);

                // 作業用変数２
                int stt = 0, end, t, n;


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
                    if (a.bottom <= v.YMIN)
                        continue;


                    // テキストデータ描画
                    //for (x2 = x = 0, i2 = i = stt; x <= v.XMAX && i < end; x = x2, i = i2) {
                    for (x = 0, i=stt; x <= v.XMAX && i < end; ) {


                        nextlen = end - (attrindex + attrlen);
                        //nextlen = end - attrindex;
                        if (nextlen >= 0) {

                            string s = str.Substring(attrindex, attrlen);


                            p.DrawText(g, s, color, x + v.XBASE, a.top);

                            x += p.CalcStringWidth(s);
                            i += s.Length;
                            stt = i;

                            if (nextlen > 0) {
                                attri++;
                                attrindex = ruls[attri].ad;
                                attrlen = ruls[attri].len;
                                color = ruls[attri].attr.color;
                            }
                        }
                        else {
                            //over
                            string s = str.Substring(attrindex, end - attrindex);
                            //string s = str.Substring(attrindex, end);
                            p.DrawText(g, s, color, x + v.XBASE, a.top);
                            //nextlen = attrlen - (end - attrindex);
                            x += p.CalcStringWidth(s);
                            i += s.Length;
                            stt = i;

                            attrlen -= s.Length; //(end - attrindex);
                            attrindex = end;

                            //attrlen = attrlen - end;

                        }


                    }


                    //// テキストデータ描画
                    //for (x2 = x = 0, i2 = i = stt; x <= v.XMAX && i < end; x = x2, i = i2) {
                    //    // n := 次のTokenの頭
                    //    //t = (flg[i]>>5);
                    //    t = 0;
                    //    n = i + t;
                    //    if (n >= end)
                    //        n = end;
                    //    else if (t == 7 || t == 0)
                    //        //while( n<end && (flg[n]>>5)==0 )
                    //        while (n < end)
                    //            ++n;

                    //    // x2, i2 := このTokenの右端
                    //    i2++;
                    //    x2 = (str[i] == '\t' ? p.nextTab(x2) : x2 + p.W(str[i]));
                    //    //	if( x2 <= v.XMIN )
                    //    //		x=x2, i=i2;
                    //    while (i2 < n && x2 <= v.XMAX)
                    //        x2 += p.W(str[i2++]);

                    //    // 再描画すべき範囲と重なっていない
                    //    if (x2 <= v.XMIN)
                    //        continue;

                    //    // x, i := このトークンの左端
                    //    if (x < v.XMIN) {
                    //        // tabの分が戻りすぎ？
                    //        x = x2; i = i2;
                    //        while (v.XMIN < x)
                    //            x -= p.W(str[--i]);
                    //    }

                    //    // 背景塗りつぶし
                    //    a.left = x + v.XBASE;
                    //    a.right = x2 + v.XBASE;
                    //    //p.Fill( a );
                    //    //g.FillRectangle(bb, a.left, a.top, a.right-a.left, a.bottom-a.top);

                    //    // 描画
                    //    //switch( str[i] )
                    //    //{
                    //    //case '\t':
                    //    //    if( p.sc(scTAB) )
                    //    //    {
                    //    //        p.SetColor( clr=CTL );
                    //    //        for( ; i<i2; ++i, x=p.nextTab(x) )
                    //    //            p.CharOut( L'>', x+v.XBASE, a.top );
                    //    //    }
                    //    //    break;
                    //    //case ' ':
                    //    //    if( p.sc(scHSP) )
                    //    //        p.DrawHSP( x+v.XBASE, a.top, i2-i );
                    //    //    break;
                    //    //case '　'://0x3000://L'　':
                    //    //    if( p.sc(scZSP) )
                    //    //        p.DrawZSP( x+v.XBASE, a.top, i2-i );
                    //    //    break;
                    //    //default:
                    //    //    if( clr != (flg[i]&3) )
                    //    //        p.SetColor( clr=(flg[i]&3) );
                    //    //    p.StringOut( str+i, i2-i, x+v.XBASE, a.top );
                    //    //    //p.StringOut( str+i, i2-i, x+v.XBASE, a.top );
                    //    //    // 何故だか２度描きしないとうまくいかん…
                    //    //    break;
                    //    //}
                    //    if (str.Contains("img")) {
                    //        g.DrawImage(image, x + v.XBASE, a.top);
                    //    }
                    //    p.DrawText(g, str.Substring(i, i2 - i), Color.Black, x + v.XBASE, a.top);

                    //}

                    // 選択範囲だったら反転
                    //if( v.SYB<=a.top && a.top<=v.SYE )
                    //    Inv( a.top, a.top==v.SYB?v.SXB:(v.XBASE),
                    //                a.top==v.SYE?v.SXE:(v.XBASE+x), p );
                    if (v.SYB <= a.top && a.top <= v.SYE)
                        Inv(g, a.top, a.top == v.SYB ? v.SXB : (v.XBASE),
                                    a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
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
            }

            // EOF後余白を背景色塗
            if (a.top < v.rc.Bottom) {
                a.left = v.rc.Left;
                a.right = v.rc.Right;
                a.bottom = v.rc.Bottom;
                //p.Fill( a );
                //g.FillRectangle(bb, a.left, a.top, a.right - a.left, a.bottom - a.top);
            }
        }
    }
}
