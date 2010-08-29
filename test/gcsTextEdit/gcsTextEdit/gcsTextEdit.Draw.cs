using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Globalization;

namespace AsControls
{
    public partial class gcsTextEdit
    {
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
                    p.DrawText(g, n.ToString(), Color.Black, edge, y);
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
                        p.DrawReturn(g, lx + vRect.XBASE, ytop);
                    }
                    ytop +=p.H();
                    continue;
                }

                int wcs = 0;
                int sindex = 0;

                int rYMAX = Math.Min(v.YMAX, ytop + rln(i) * p.H());

                for (int j = 0; ytop < rYMAX; j++, ytop += p.H()) {
                    int wc = 0;// doc.wrapInfo[i].wrap[j];
                    IBuffer text = null;
                    wc = rlend(i,j);// wrapList[i].wrap[j];
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

        //private void drawDoc(Graphics g, VDrawInfo v)
        //{
        //    int ytop = v.YMIN;
        //    int ybuttom = ytop + ViewHeight;

        //    for (int i = v.TLMIN; ytop < v.YMAX; i++)
        //    {
        //        int lx = 0;
        //        IBuffer buf = doc.LineList[i].Text;
        //        List<AttributeInfo> attributeInfoList = doc.LineList[i].AttributeList;
        //        if (attributeInfoList.Count == 0)
        //        {
        //            if (doc.LastLine(i))
        //            {
        //            }
        //            else
        //            {
        //                drawReturn(g, lx + vRect.XBASE, ytop);
        //            }
        //            ytop += lineHeight;
        //            continue;
        //        }

        //        int wcs = 0;
        //        int sindex = 0;

        //        int rYMAX = Math.Min(v.YMAX, ytop + GetvlCnt(i) * lineHeight);

        //        for (int j = 0; ytop < rYMAX; j++, ytop += lineHeight)
        //        {
        //            int wc = 0;// doc.wrapInfo[i].wrap[j];
        //            IBuffer text = null;
        //            wc = wrapList[i].wrap[j];
        //            text = buf.Substring(wcs, wc - wcs);

        //            lx = 0;
        //            IBuffer fs;
        //            while (true)
        //            {
        //                int len = attributeInfoList[sindex].len;
        //                Color c = attributeInfoList[sindex].forecolor;

        //                if (sindex + len > wc)
        //                {
        //                    fs = text.Substring(sindex - wcs, wc - sindex);
        //                    len = fs.Length;
        //                }
        //                else
        //                {
        //                    fs = text.Substring(sindex - wcs, len);
        //                }

        //                switch (attributeInfoList[sindex].Token)
        //                {
        //                    case TokenType.TXT:
        //                        drawText(g, fs.ToString(), Font, c, lx + v.XBASE, ytop);
        //                        break;
        //                    case TokenType.CLICKABLE:
        //                        drawText(g, fs.ToString(), Font, c, lx + v.XBASE, ytop);
        //                        drawUnderLine(g, fs, Font, c, lx + v.XBASE, ytop);
        //                        break;
        //                    case TokenType.TAB:
        //                        drawTab(g, lx + v.XBASE, ytop, len);
        //                        break;
        //                    case TokenType.ZSP:
        //                        drawZen(g, lx + v.XBASE, ytop, len);
        //                        break;
        //                }

        //                sindex += len;
        //                lx += CalcStringWidth(fs);
        //                if (sindex >= wc)
        //                {
        //                    break;
        //                }
        //            }
        //            wcs = wc;

        //            // 選択範囲だったら反転
        //            if (v.SYB <= ytop && ytop <= v.SYE && !isdropfile)
        //            {
        //                //Inv(a.top, a.top == v.SYB ? v.SXB : (v.XBASE),
        //                //            a.top == v.SYE ? v.SXE : (v.XBASE + x), p);
        //                //inline void ViewImpl::Inv( int y, int xb, int xe, Painter& p )
        //                //{
        //                //    RECT rc = {
        //                //        Max( left(),  xb ), y,
        //                //        Min( right(), xe ), y+p.H()-1
        //                //    };
        //                //    p.Invert( rc );
        //                //}

        //                //LONG left;
        //                //LONG top;
        //                //LONG right;
        //                //LONG bottom;
        //                int le = ytop == v.SYB ? v.SXB : v.XBASE;
        //                int to = ytop;
        //                int ri = ytop == v.SYE ? v.SXE : (v.XBASE + CalcStringWidth(text));
        //                int bo = ytop + lineHeight;
        //                Rectangle rect = new Rectangle(le, to, ri, bo);
        //                Invert(g, rect);
        //            }
        //        }

        //        if (doc_.LastLine(i))
        //        {

        //        }
        //        else
        //        {
        //            drawReturn(g, lx + vRect.XBASE, ytop - lineHeight);
        //        }
        //    }
        //}
    }
}
