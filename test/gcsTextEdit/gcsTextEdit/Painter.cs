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
        //private Config config_;

        //private Brush tabbrush;
        //private Brush TextBrush = new SolidBrush(Color.Black);

        private Brush lineNumberBrush;
        private Color lineNumberColor;
        public Color LineNumberForeColor {
            get { return this.lineNumberColor; }
            set {
                this.lineNumberColor = value;
                DeleteObj(this.lineNumberBrush);
                this.lineNumberBrush = new SolidBrush(value);
            }
        }

        private Pen lineNumberLinePen;
        public Color LineNumberLineColor {
            get { return lineNumberLinePen.Color; }
            set {
                DeleteObj(this.lineNumberLinePen);
                this.lineNumberLinePen = new Pen(value);
            }
        }

        public Color LineNumberBackColor {
            get;
            set;
        }

        private Pen specialCharPen;
        public Color SpecialCharForeColor {
            get { return specialCharPen.Color; }
            set {
                DeleteObj(this.specialCharPen);
                this.specialCharPen = new Pen(value);
            }
        }

        //private int[] widthTable_;
        private Dictionary<char, int> widthMap = new Dictionary<char, int>();

        private int figWidth_;

        private StringFormat sf = new StringFormat();

        private int height_;
        public int H() { return height_; }

        /// <summary>
        /// 標準文字幅(pixel)
        /// </summary>
        /// <returns></returns>
        //public int W() { return widthTable_['x']; }
        public int W() { return this.widthMap['x']; }

        /// <summary>
        /// 数字幅(pixel)
        /// </summary>
        /// <returns></returns>
	    public int F() { return figWidth_; }

        /// <summary>
        /// 次のタブ揃え位置を計算
        /// </summary>
        /// <returns></returns>
        //public int T() { return widthTable_['\t']; }
        public int T() { return widthMap['\t']; }
	    public int nextTab(int x) { int t=T(); return ((x+4)/t+1)*t; }
        
        //
        //public int Wc( char ch )
        //{
        //    if( widthTable_[ ch ] == -1 )
        //        ::GetCharWidthW( dc_, ch, ch, widthTable_+ch );
        //    return widthTable_[ ch ];
        //}

        private int tabWidth;

        [DefaultValue(4)]
        public int TabWidth {
            get { return tabWidth; }
            set {
                this.tabWidth = value;      
            }
        }

        private IntPtr hfont_;
        private Font font;
        public Font Font {
            get { return this.font; }
            set {
                this.font = value;
                DeleteObject(hfont_);
                this.hfont_= value.ToHfont();
            }
        }

        //public Painter(IntPtr hwnd, Config config) {
        public Painter(IntPtr hwnd, Font font) {
            hwnd_ = hwnd;
            dc_ = Win32API.GetDC(hwnd_);

            this.Font = font;

            sf.Alignment = StringAlignment.Center;

            TabWidth = 4;

            init();

            //config_ = config;
            //hfont_ = config_.Font.ToHfont();

            //widthTable_ = new int[65536];
            //int s = (int)' ';
            //int e = (int)'~';
            //int w = 0;
            //for(int i=s; i<=e; i++){
            //    //int w2 = getStringWidth(((char)i).ToString());
            //    //int w3 = CalcStringWidth(new LineBuffer(((char)i).ToString()));
            //    //Win32API.GetCharWidthW(dc_, (char)i, (char)i, ref w);
            //    w = CalcStringWidth(((char)i));
            //    widthTable_[i] = w;
            //}

            //widthTable_['\t'] = W() * Math.Max(1, 4);//vc.tabstep);
            //widthTable_['\x3000'] = CalcStringWidth('\x3000');

            //// 数字の最大幅を計算
            //figWidth_ = 0;
            //for (int ch = '0'; ch <= '9'; ++ch) {
            //    if (figWidth_ < widthTable_[ch])
            //        figWidth_ = widthTable_[ch];
            //}

            //height_ = (int)(config.Font.GetHeight() + 0.5f);
            //sf.Alignment = StringAlignment.Center;

            //widthMap = new Dictionary<char, int>();
            //widthMap.Add('x', CalcStringWidth('x'));
            //widthMap.Add('\t', W() * Math.Max(1, TabWidth));
            //widthMap.Add('\x3000', CalcStringWidth('\x3000'));

            //// 数字の最大幅を計算
            //figWidth_ = 0;
            //for (int i = 0; i <= 9; i++) {
            //    //int w2 = getStringWidth(((char)i).ToString());
            //    //int w3 = CalcStringWidth(new LineBuffer(((char)i).ToString()));
            //    //Win32API.GetCharWidthW(dc_, (char)i, (char)i, ref w);
            //    char num = (char)i;
            //    int numw = CalcStringWidth(num);
            //    widthMap.Add(num, numw);
            //    if (figWidth_ < numw) {
            //        figWidth_ = numw;
            //    }
            //}

            //numPen = new Pen(Color.Gray);
            //tabbrush = new SolidBrush(Color.Blue);


        }

        public void init() {

            height_ = (int)(this.Font.GetHeight() + 0.5f);
            
            widthMap.Clear();
            widthMap.Add('x', CalcStringWidth('x'));
            widthMap.Add('\t', W() * Math.Max(1, TabWidth));
            widthMap.Add('\x3000', CalcStringWidth('\x3000'));

            // 数字の最大幅を計算
            figWidth_ = 0;
            for (char ch = '0'; ch <= '9'; ++ch) {
                //char num = (char)i;
                int numw = CalcStringWidth(ch);
                widthMap.Add(ch, numw);
                if (figWidth_ < numw) {
                    figWidth_ = numw;
                }
            }
        }

        #region IDisposable メンバ

        public void Dispose() {
            DeleteObj(lineNumberBrush);
            DeleteObj(lineNumberLinePen);
            DeleteObj(specialCharPen);
            //Marshal.FreeHGlobal(widthPtr);
            Win32API.ReleaseDC(hwnd_, dc_);

            DeleteObject(hfont_);
        }

        #endregion

        private void DeleteObject(IntPtr ptr) {
            if (ptr != IntPtr.Zero) {
                Win32API.DeleteObject(ptr);
            }
        }

        private void DeleteObj(IDisposable obj) {
            if (obj != null) {
                obj.Dispose();
            }
        }

        public int W(char ch) // 1.08 サロゲートペア回避
        {
            ////unicode ch = *pch;
            ////if( widthTable_[ ch ] == -1 )
            //if (widthTable_[ch] == 0) {
            //    if (Char.IsHighSurrogate(ch)) {
            //        //Win32API.SIZE sz = new Win32API.SIZE();
            //        //if (Win32API.GetTextExtentPoint32W(dc_, ch.ToString(), 2, ref sz)!=0)
            //        //    return sz.width;
            //        //int w = 0;
            //        //Win32API.GetCharWidthW( dc_, ch, ch, ref w );
            //        //return w;
            //        return CalcStringWidth(ch.ToString());
            //    }
            //    //int w2 = 0;
            //    //Win32API.GetCharWidthW(dc_, ch, ch, ref w2);
            //    //widthTable_[ch] = w2;

            //    int w2 = CalcStringWidth(ch.ToString());
            //    widthTable_[ch] = w2;
            //}
            //return widthTable_[ch];

            if (!widthMap.ContainsKey(ch)) {
                if (Char.IsHighSurrogate(ch)) {
                    return CalcStringWidth(ch);
                }
                widthMap.Add(ch, CalcStringWidth(ch));
            }
            return widthMap[ch];
        }

        public void DrawLine(Graphics g, Pen pen, int x1, int y1, int x2, int y2) {
            g.DrawLine(pen, x1, y1, x2, y2);
        }

        public void DrawLine(Graphics g, int x1, int y1, int x2, int y2) {
            g.DrawLine(lineNumberLinePen, x1, y1, x2, y2);
        }

        public void DrawHSP(Graphics g, int x, int y, int times )
        {
            // 半角スペース記号(ホチキスの芯型)を描く
            int w=W(' ');
            int h=H();
            Point[] pt = {
                new Point( x    , y+h-4 ),
                new Point( x    , y+h-2 ),
                new Point( x+w-3, y+h-2 ),
                new Point( x+w-3, y+h-5 )
            };
            while( times-->0 )
            {
                if( 0 <= pt[3].X )
                    g.DrawPolygon(specialCharPen, pt);
                pt[0].X += w;
                pt[1].X += w;
                pt[2].X += w;
                pt[3].X += w;
            }
        }

        public void DrawZen(Graphics g, int X, int Y, int times) {
            for (int i = 0; i < times; i++) {
                //g.DrawRectangle(numPen, X + i * widthTable_[' '] + 2, Y + 2, widthTable_[' '] - 4, H() - 4);
                g.DrawRectangle(specialCharPen, X + i * W('\x3000') + 2, Y + 2, W('\x3000') - 4, H() - 4);
                //int w = W(' ')*2 - 4;
                //int h = H() - 4;
                //g.DrawRectangle(numPen, 30, 30, 30,30);
            }
        }

        public void DrawTab(Graphics g, int X, int Y, int len) {
            for (int i = 0; i < len; i++) {
                //g.DrawString(">", config_.Font, tabbrush, X + i * T(), Y);
            }
        }

        private Point[] returnPt1 = { new Point(), new Point() };
        private Point[] returnPt2 = { new Point(), new Point(), new Point() };
        public void DrawReturn(Graphics g, int X, int Y) {
            //g.DrawString("↓", config_.Font, tabbrush, X - 2, Y);
            returnPt1[0].X = X+4;
            returnPt1[0].Y = Y + 2;
            returnPt1[1].X = X+4 ;
            returnPt1[1].Y = Y + H() -2;
            g.DrawPolygon(specialCharPen, returnPt1);

            returnPt2[0].X = X + 2;
            returnPt2[0].Y = Y + H() - 4;
            returnPt2[1].X = X + 4;
            returnPt2[1].Y = Y + H()-2;
            returnPt2[2].X = X + 6;
            returnPt2[2].Y = Y + H() - 4;
            g.DrawLines(specialCharPen, returnPt2);
        }

        public void DrawLineNum(Graphics g, string text, Color c, int X, int Y) {
            g.DrawString(text, this.Font, lineNumberBrush, new Point(X, Y), sf);
        }
        public void DrawText(Graphics g, string text, Color c, int X, int Y) {
            //g.DrawString(text, config_.Font, TextBrush, new Point(X, Y));
            TextRenderer.DrawText(g,
               text,
               this.Font,
               new Point(X, Y),
               c,
                TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix);
                //TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.Left | TextFormatFlags.Top | TextFormatFlags.Internal | TextFormatFlags.NoPrefix); 

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

        public int CalcStringWidth(char c) {
            int fit;
            int w = 0;

            switch (c) {
                case ' ':
                case '\x3000': //0x3000://L'　':
                    w = GetTextExtend(c.ToString(), int.MaxValue, out fit).width;
                    break;
                case '\t':
                    w = T();
                    break;
                default:
                    w = GetTextExtend(c.ToString(), int.MaxValue, out fit).width;
                    break;
            }
            return w;
        }

        private static char[] cs = { '\t' };
        public int CalcStringWidth(string text) {
            //////return this.CalcStringWidth(text, 0, text.Length);
            //int w = 0;
            ////string strtext = text.ToString();
            //for (int i = 0; i < text.Length; i++) {
            //    w += this.W(text[i]);
            //}
            //return w;

            //int fit;
            //Win32API.SIZE size = GetTextExtend(text, int.MaxValue, out fit);
            //return size.width;

            int fit;
            int w = 0;

            if(text.LastIndexOfAny(cs)==-1){
                w = GetTextExtend(text, int.MaxValue, out fit).width;
                return w;
            }

            foreach (var s in parse(text, cs)) {
                switch (s[0]) {
                    //case ' ':
                    //    w += W(' ')*s.Length;
                    //    break;
                    //case '\x3000': //0x3000://L'　':
                    //    w += W('\x3000') * s.Length;
                    //    break;
                    case '\t':
                        w += T() * s.Length;//p.CalcStringWidth(s[ci].ToString());
                        break;
                    default:
                        w += GetTextExtend(s, int.MaxValue, out fit).width;
                        break;
                }                
            }

            return w;
        }

       
        public static IEnumerable<string> parse(string src, char[] sc) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < src.Length; i++) {

                if (sc.Contains(src[i])) {
                    if (sb.Length > 0) {
                        yield return sb.ToString();
                        sb.Remove(0, sb.Length);
                    }

                    char c = src[i];
                    while (i < src.Length && c == src[i]) {
                        sb.Append(src[i]);
                        i++;
                    }
                    if (i < src.Length) {
                        i--;
                    }
                    yield return sb.ToString();
                    sb.Remove(0, sb.Length);

                } else {
                    sb.Append(src[i]);
                }
            }
            if (sb.Length > 0) {
                yield return sb.ToString();
            }
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
            IntPtr OldFont = Win32API.SelectObject(dc_, hfont_); //TODO
            //IntPtr OldFont = Win32API.SelectObject(dc_, config_.Font.ToHfont());
            Win32API.SIZE size = new Win32API.SIZE();
            Win32API.GetTextExtentExPointW(dc_, str, str.Length, maxwidth, out fit, null, out size);
            Win32API.SelectObject(dc_, OldFont);
            return size;
        }

        public void SetClip(Rectangle rc ){
            Win32API.IntersectClipRect(dc_, rc.Left, rc.Top, rc.Right, rc.Bottom);
        }

        public void ClearClip()
        {
            Win32API.SelectClipRgn(dc_, IntPtr.Zero);
        }
    }
}
