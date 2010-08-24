using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace CsEdit
{
    class Painter : IDisposable
    {
        private IntPtr hdc;
        private IntPtr hfont;
        private IntPtr oldFont;
        private Graphics g;
        private int fHeight;
        private Font font;

        private const string ZSP = "　";
        private const string HSP = " ";
        private int ZSPW;
        private int HSPW;

        private Pen pen;

        private int TabNum;

        public int LineHeight
        {
            get { return fHeight; }
        }

        public Painter(Graphics g, Font font)
        {
            this.g = g;
            hdc = g.GetHdc();
            this.font = font;
            hfont = this.font.ToHfont();
            oldFont = Win32API.SelectObject(hdc, hfont);
            fHeight = getLineHeight(this.font);

            pen = new Pen(Color.Brown);

            ZSPW = CalcStringWidth(ZSP);
            HSPW = CalcStringWidth(HSP);
        }

        private int getLineHeight(Font font)
        {
            // とりあえず四捨五入処理付きで一行の高さを取得
            return (int)(font.GetHeight() + 0.5f);
        }

        public int CalcStringWidth(string text)
        {
            int w = 0;
            for (int i = 0; i < text.Length; i++)
            {
                if (text[i] == '\t')
                {

                }
                else
                {
                    w += getStringWidth(text[i].ToString());
                }
            }
            return w;
        }

        public int getStringWidth(string text)
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

        private void DrawZen(int X, int Y, int len)
        {
            int zenw = getStringWidth("　");
            for (int i = 0; i < len; i++)
            {
                g.DrawRectangle(pen, X + i * zenw + 2, Y + fHeight + 2, zenw - 2, fHeight-2);
            }
        }

         Brush tabbrush = new SolidBrush(Color.Blue);
         private void DrawTab(Graphics g, int X, int Y, int len)
         {
             for (int i = 0; i < len; i++)
             {
                 g.DrawString(">", this.font, tabbrush, X + i * 24, Y);
             }
         }

        #region IDisposable メンバ

        public void Dispose()
        {
            Win32API.SelectObject(hdc, oldFont);
            g.ReleaseHdc(hdc);
        }

        #endregion
    }
}
