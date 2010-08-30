using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;
using System.ComponentModel;
using System.Windows.Forms.VisualStyles;
using System.Windows.Forms;

namespace AsControls {
    public class Painter : IDisposable{

        private IntPtr dc_;
        private IntPtr hwnd_;
        //private Font font_;
        private Config config_;

        private Pen numPen;
        private Brush tabbrush;
        private Brush TextBrush = new SolidBrush(Color.Black);

        private IntPtr fint_;
        private int[] widthTable_;
        //private Dictionary<char, int> widthTableMap_;
        
        public int figWidth_;

        private int height_;
        public int H() { return height_; }

        //@{ 標準文字幅(pixel) //@}
        public int W() { return widthTable_[(int)'x']; }

        //@{ 数字幅(pixel) //@}
	    public int F() { return figWidth_; }

        //@{ 次のタブ揃え位置を計算 //@}
        private int T() { return widthTable_[ '\t' ]; }
	    public int nextTab(int x) { int t=T(); return ((x+4)/t+1)*t; }


        public int W(char ch ) // 1.08 サロゲートペア回避
		{
			//unicode ch = *pch;
			if( widthTable_[ ch ] == -1 )
			{
                if (Util.isHighSurrogate(ch))
				{
                    //Win32API.SIZE sz = new Win32API.SIZE();
                    //if (Win32API.GetTextExtentPoint32W(dc_, ch.ToString(), 2, ref sz)!=0)
                    //    return sz.width;
                    //int w = 0;
                    //Win32API.GetCharWidthW( dc_, ch, ch, ref w );
                    //return w;
                    return CalcStringWidth(ch.ToString());
				}
                //int w2 = 0;
                //Win32API.GetCharWidthW(dc_, ch, ch, ref w2);
                //widthTable_[ch] = w2;

                int w2 = CalcStringWidth(ch.ToString());
                widthTable_[ch] = w2;
			}
			return widthTable_[ ch ];
		}

        public Painter(IntPtr hwnd, Config config) {
            hwnd_ = hwnd;
            dc_ = Win32API.GetDC(hwnd_);
            //Win32API.ReleaseDC
            //widthTableMap_ = new Dictionary<char, int>();
            config_ = config;
            fint_ = config_.Font.ToHfont();

            widthTable_ = new int[65536];
            int s = (int)' ';
            int e = (int)'~';
            int w = 0;
            for(int i=s; i<=e; i++){
                //int w2 = getStringWidth(((char)i).ToString());
                //int w3 = CalcStringWidth(new LineBuffer(((char)i).ToString()));
                //Win32API.GetCharWidthW(dc_, (char)i, (char)i, ref w);
                w = CalcStringWidth(((char)i).ToString());
                widthTable_[i] = w;
            }
            widthTable_['\t'] = W() * Math.Max(1, 4);//vc.tabstep);

            // 数字の最大幅を計算
            figWidth_ = 0;
            for (int ch = '0'; ch <= '9'; ++ch) {
                if (figWidth_ < widthTable_[ch])
                    figWidth_ = widthTable_[ch];
            }

            height_ = (int)(config.Font.GetHeight() + 0.5f);

            //Win32API.TEXTMETRIC tm;
            //////using (Graphics g = this.CreateGraphics()) {
            //Win32API.GetTextMetrics(dc_, out tm);
            //////}
            //height_ = tm.tmHeight; //This is what you want.
            //int kk = 0;
            //widthPtr = Marshal.AllocHGlobal(65536*sizeof(int));
            //int ret = Win32API.GetCharWidth32(dc_, ' ', '~', widthPtr + ' ');
            //if (ret != 0) {
            //    if (widthPtr != IntPtr.Zero) {
            //        Marshal.Copy(widthPtr, widthTable_, 0, 65535);
            //        //int start = (int)' ';
            //        //int end = (int)'~';
            //        //for(int i=start;i < end; i++ ){

            //        //}

            //        widthTable_['\t'] = W() * Math.Max(1, 4);//vc.tabstep);

            //        // 数字の最大幅を計算
            //        figWidth_ = 0;
            //        for (int ch = '0'; ch <= '9'; ++ch) {
            //            if (figWidth_ < widthTable_[ch])
            //                figWidth_ = widthTable_[ch];
            //        }

            //        height_ = (int)(config.Font.GetHeight() + 0.5f);
            //    }
            //} else {
            //    Dispose();
            //    throw new Win32Exception(Marshal.GetLastWin32Error());
            //}

            numPen = new Pen(Color.Gray);
            tabbrush = new SolidBrush(Color.Blue);
        }

        public void DrawLine(Graphics g, int x1, int y1, int x2, int y2) {
            g.DrawLine(numPen, x1, y1, x2, x2);
        }

        public void DrawZen(Graphics g, int X, int Y, int len) {
            for (int i = 0; i < len; i++) {
                g.DrawRectangle(numPen, X + i * widthTable_[' '] + 2, Y + 2, widthTable_[' '] - 4, H() - 4);
            }
        }

        public void DrawTab(Graphics g, int X, int Y, int len) {
            for (int i = 0; i < len; i++) {
                g.DrawString(">", config_.Font, tabbrush, X + i * T(), Y);
            }
        }

        public void DrawText(Graphics g, string text, Color c, int X, int Y) {
            //g.DrawString(text, config_.Font, TextBrush, new Point(X, Y));
            TextRenderer.DrawText(g,
               text,
               config_.Font,
               new Point(X, Y),
               c,
                TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix);
                //TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.Left | TextFormatFlags.Top | TextFormatFlags.Internal | TextFormatFlags.NoPrefix); 

        }

        public void DrawReturn(Graphics g, int X, int Y) {
            g.DrawString("↓", config_.Font, tabbrush, X - 2, Y);
        }

        public void Invert(Graphics g, Rectangle rect) {
            Win32API.InvertRect(g.GetHdc(), ref rect);
            g.ReleaseHdc();
        }

        ////
        //public int CalcStringWidth(IBuffer text, int startIndex, int count) {
        //    IntPtr oldFont = Win32API.SelectObject(dc_, config_.Font.ToHfont());

        //    int w = 0;
        //    int len = startIndex + count;
        //    for (int i = startIndex; i < len; i++) {
        //        if (text[i] == "\t") {
        //            w += T();
        //        } else {
        //            w += getStringWidth(text[i]);
        //        }
        //    }

        //    Win32API.SelectObject(dc_, oldFont);
        //    return w;
        //}

        public int CalcStringWidth(string text) {
            //////return this.CalcStringWidth(text, 0, text.Length);
            //int w = 0;
            ////string strtext = text.ToString();
            //for (int i = 0; i < text.Length; i++) {
            //    w += this.W(text[i]);
            //}
            //return w;
            int fit;
            Win32API.SIZE size = GetTextExtend(text, int.MaxValue, out fit);
            return size.width;
        }

        //public int CalcCharWidth(Char c) {
        //    IntPtr oldFont = Win32API.SelectObject(dc_, config_.Font.ToHfont());

        //    int w = 0;
        //    if (c == '\t') {
        //        w = T();
        //    } else {
        //        w = getStringWidth(c.ToString());
        //    }

        //    Win32API.SelectObject(dc_, oldFont);
        //    return w;
        //}

        //private int getStringWidth(string text) {
        //    int drawableLength;
        //    int[] extents = new int[text.Length];
        //    return GetTextExtent(dc_, text, text.Length, int.MaxValue, out drawableLength, out extents).Width;
        //}

        //public Size GetTextExtent(IntPtr hdc, string text, int textLen, int maxWidth, out int fitLength, out int[] extents) {
        //    Int32 bOk;
        //    Win32API.SIZE size;
        //    extents = new int[text.Length];

        //    unsafe {
        //        fixed (int* pExtents = extents)
        //        fixed (int* pFitLength = &fitLength)
        //            bOk = Win32API.GetTextExtentExPointW(hdc, text, textLen, maxWidth, pFitLength, pExtents, &size);
        //        return new Size(size.width, size.height);
        //    }
        //}
        private Win32API.SIZE GetTextExtend(string str, int maxwidth, out int fit) {
            IntPtr OldFont = Win32API.SelectObject(dc_, fint_); //TODO
            //IntPtr OldFont = Win32API.SelectObject(dc_, config_.Font.ToHfont());
            Win32API.SIZE size = new Win32API.SIZE();
            Win32API.GetTextExtentExPointW(dc_, str, str.Length, maxwidth, out fit, null, out size);
            Win32API.SelectObject(dc_, OldFont);
            return size;
        }


        #region IDisposable メンバ

        public void Dispose() {
            numPen.Dispose();
            tabbrush.Dispose();
            //Marshal.FreeHGlobal(widthPtr);
            Win32API.ReleaseDC(hwnd_, dc_);
        }

        #endregion
    }
}
