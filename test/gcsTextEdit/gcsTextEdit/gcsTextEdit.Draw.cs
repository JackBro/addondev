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
        private IntPtr hdc;
        private IntPtr hfont;

        private Brush cursorBrush = new SolidBrush(Color.Black);
        private Pen spPen;
        private Brush tabbrush = new SolidBrush(Color.Blue);
        private Brush ppp = new SolidBrush(Color.DarkGreen);

        private const Char ZSP = '　';
        private const Char HSP = ' ';
        private int ZSPW;
        private int HSPW;

        private int figureWidth;

        private int tabNum =4;
        private int lineHeight;

        private void initDraw()
        {
            hdc = Win32API.GetDC(Handle);
            hfont = this.Font.ToHfont();
            spPen = new Pen(Color.Gray);

            initTable();

            vRect.NumLineLeft = figureWidth*2;
        }

        private void deleteDraw()
        {
            Win32API.ReleaseDC(Handle, hdc);
        }

        private void initTable()
        {
            lineHeight = getLineHeight(this.Font);
            ZSPW = CalcCharWidth(ZSP);
            HSPW = CalcCharWidth(HSP);
            figureWidth = CalcCharWidth('0');
        }

        private int getLineHeight(Font font)
        {
            // とりあえず四捨五入処理付きで一行の高さを取得
            return (int)(font.GetHeight() + 0.5f);
        }

        public int CalcStringWidth(IBuffer text, int startIndex, int count)
        {
            IntPtr oldFont = Win32API.SelectObject(hdc, hfont);

            int w = 0;
            int len = startIndex + count;
            for (int i = startIndex; i < len; i++)
            {
                if (text[i] == "\t")
                {
                    w += tabNum * HSPW;
                }
                else
                {
                    w += getStringWidth(text[i]);
                }
            }

            Win32API.SelectObject(hdc, oldFont);
            return w;
        }

        public int CalcStringWidth(IBuffer text)
        {
            return this.CalcStringWidth(text, 0, text.Length);
        }

        public int CalcCharWidth(Char c)
        {
            IntPtr oldFont = Win32API.SelectObject(hdc, hfont);

            int w = 0;
            if (c == '\t')
            {
                w = tabNum * HSPW;
            }
            else
            {
                w = getStringWidth(c.ToString());
            }

            Win32API.SelectObject(hdc, oldFont);
            return w;        
        }

        private int getStringWidth(string text)
        {
            int drawableLength;
            int[] extents = new int[text.Length];
            return GetTextExtent(hdc, text, text.Length, int.MaxValue, out drawableLength, out extents).Width;
        }

        public Size GetTextExtent(IntPtr hdc, string text, int textLen, int maxWidth, out int fitLength, out int[] extents)
        {
            Int32 bOk;
            Win32API.SIZE size;
            extents = new int[text.Length];

            unsafe
            {
                fixed (int* pExtents = extents)
                fixed (int* pFitLength = &fitLength)
                    bOk = Win32API.GetTextExtentExPointW(hdc, text, textLen, maxWidth, pFitLength, pExtents, &size);
                return new Size(size.width, size.height);
            }
        }

        public Boolean CalcNumLineWidth()
        {
            int prev = vRect.NumLineLeft;
            if (showNumLine)
            {
                int ln = doc.tlNum.ToString().Length;
                vRect.NumLineLeft = (1 + ln) * figureWidth;
            }
            else
            {
                vRect.NumLineLeft = 0;
            }

            return prev != vRect.NumLineLeft ? true : false;
        }

        private void drawText(Graphics g, string text, Font font, Color color, int X, int Y)
        {
            
            TextRenderer.DrawText(g,
               text,
               font,
               new Point(X, Y),
               color,
               TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix);
        }

        private void drawUnderLine(Graphics g, IBuffer text, Font font, Color color, int X, int Y)
        {
            g.DrawLine(spPen, X, Y + font.Height, X + CalcStringWidth(text), Y + font.Height);
        }

        private void drawZen(Graphics g, int X, int Y, int len)
        {
            for (int i = 0; i < len; i++)
            {
                g.DrawRectangle(spPen, X + i * ZSPW + 2, Y + 2, ZSPW - 4, lineHeight - 4);
            }
        }

        private void drawTab(Graphics g, int X, int Y, int len)
        {
            for (int i = 0; i < len; i++)
            {
                g.DrawString(">", this.Font, tabbrush, X + i * tabNum * HSPW, Y);
            }
        }

        private void drawReturn(Graphics g, int X, int Y)
        {
            g.DrawString("↓", this.Font, tabbrush, X-2, Y);
        }

        private void drawEOF(Graphics g, int X, int Y)
        {
            //g.DrawString("↓", this.Font, tabbrush, X - 2, Y);
        }

        private void Invert(Graphics g, Rectangle rect)
        {
            Win32API.InvertRect(g.GetHdc(), ref rect);
            g.ReleaseHdc();
        }

        private void drawCaret(Graphics g, int X, int Y)
        {
            g.FillRectangle(cursorBrush, X, Y, 2, lineHeight);
        }

        Brush bb = new SolidBrush(Color.White);
        private void drawLNA(Graphics g, ViewRect v)
        {


            if (v.rc.Top < v.YMAX)
            {
                Rectangle rc = new Rectangle(0, 0, v.NumLineLeft-2, v.YMAX);
                g.FillRectangle(bb, rc);
                
                // 境界線表示
                //int line = lna() - p.F() / 2;
                //p.DrawLine(line, v.rc.Top, line, v.YMAX);
                //p.SetColor(LN);
                g.DrawLine(spPen, v.NumLineLeft-2, v.rc.Top, v.NumLineLeft-2, v.YMAX);


                // 行番号表示
                int n = v.TLMIN + 1;
                int y = v.YMIN;

                //int edge = lna() - p.F() * 2;
                ///int edge = v.NumLineLeft;// - figureWidth * 2;
                int edge = v.NumLineLeft;// - figureWidth * 2;
                StringFormat sf = new StringFormat();
                sf.Alignment = StringAlignment.Far;  
                for (int i = v.TLMIN; y < v.YMAX; ++i, ++n)
                {
                    //n.Output(p, edge, y);
                    //sf.LineAlignment = StringAlignment.Center;
                    g.DrawString(n.ToString(), this.Font, ppp, new Point(edge, y), sf);
                    //drawText(g, n.ToString(), this.Font, Color.Red, edge, y);
                    y += lineHeight * GetvlCnt(i);
                }
            }
        }

        private void drawDoc(Graphics g, ViewRect v)
        {
            int ytop = v.YMIN;
            int ybuttom = ytop + ViewHeight;

            for (int i = v.TLMIN; ytop < v.YMAX; i++)
            {
                int lx = 0;
                IBuffer buf = doc.LineList[i].Text;
                List<AttributeInfo> attributeInfoList = doc.LineList[i].AttributeList;
                if (attributeInfoList.Count == 0)
                {
                    if (doc.LastLine(i))
                    {
                    }
                    else
                    {
                        drawReturn(g, lx + vRect.XBASE, ytop);
                    }
                    ytop += lineHeight;
                    continue;
                }

                int wcs = 0;
                int sindex = 0;

                int rYMAX = Math.Min(v.YMAX, ytop + GetvlCnt(i) * lineHeight);

                for (int j = 0; ytop < rYMAX; j++, ytop += lineHeight)
                {
                    int wc = 0;// doc.wrapInfo[i].wrap[j];
                    IBuffer text = null;
                    wc = wrapList[i].wrap[j];
                    text = buf.Substring(wcs, wc - wcs);

                    lx = 0;
                    IBuffer fs;
                    while (true)
                    {
                        int len = attributeInfoList[sindex].len;
                        Color c = attributeInfoList[sindex].forecolor;

                        if (sindex + len > wc)
                        {
                            fs = text.Substring(sindex - wcs, wc - sindex);
                            len = fs.Length;
                        }
                        else
                        {
                            fs = text.Substring(sindex - wcs, len);
                        }

                        switch (attributeInfoList[sindex].Token)
                        {
                            case TokenType.TXT:
                                drawText(g, fs.ToString(), Font, c, lx + v.XBASE, ytop);
                                break;
                            case TokenType.CLICKABLE:
                                drawText(g, fs.ToString(), Font, c, lx + v.XBASE, ytop);
                                drawUnderLine(g, fs, Font, c, lx + v.XBASE, ytop);
                                break;
                            case TokenType.TAB:
                                drawTab(g, lx + v.XBASE, ytop, len);
                                break;
                            case TokenType.ZSP:
                                drawZen(g, lx + v.XBASE, ytop, len);
                                break;
                        }

                        sindex += len;
                        lx += CalcStringWidth(fs);
                        if (sindex >= wc)
                        {
                            break;
                        }
                    }
                    wcs = wc;

                    // 選択範囲だったら反転
                    if (v.SYB <= ytop && ytop <= v.SYE && !isdropfile)
                    {
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
                        int ri = ytop == v.SYE ? v.SXE : (v.XBASE + CalcStringWidth(text));
                        int bo = ytop + lineHeight;
                        Rectangle rect = new Rectangle(le, to, ri, bo);
                        Invert(g, rect);
                    }
                }

                if (doc.LastLine(i))
                {

                }
                else
                {
                    drawReturn(g, lx + vRect.XBASE, ytop - lineHeight);
                }
            }
        }
    }
}
