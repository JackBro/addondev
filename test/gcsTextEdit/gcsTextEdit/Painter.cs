using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.ComponentModel;
using System.Windows.Forms;
using YYS.Parser;

namespace YYS {
    public class Painter : IDisposable{

        private IntPtr dc_;
        private IntPtr hwnd_;

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

        private Brush lineNumberBackBrush;
        private Color lineNumberBackColor;
        public Color LineNumberBackColor {
            get { return lineNumberBackColor; }
            set{
                this.lineNumberBackColor = value;
                DeleteObj(this.lineNumberBackBrush);
                this.lineNumberBackBrush = new SolidBrush(value);
            }
        }

        private Pen specialCharPen;
        public Color SpecialCharForeColor {
            get { return specialCharPen.Color; }
            set {
                DeleteObj(this.specialCharPen);
                this.specialCharPen = new Pen(value);
            }
        }

        private Pen AttributeLinePen;
        private float lineWeight;
        public float LineWeight {
            get { return lineWeight; }
            set {
                lineWeight = value;
                DeleteObj(this.AttributeLinePen);
                this.AttributeLinePen = new Pen(Color.Black, value);
            }
        }

        private Dictionary<char, int> widthMap = new Dictionary<char, int>();

        private int figWidth_;

        private int height_;
        public int H() { return height_; }

        /// <summary>
        /// 標準文字幅(pixel)
        /// </summary>
        /// <returns></returns>
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
        public int T() { return widthMap['\t']; }
	    public int nextTab(int x) { int t=T(); return ((x+4)/t+1)*t; }

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

                LineWeight = (float)(this.font.Size/10.0);

                //DeleteObj(boldFont);
                //boldFont = null;
            }
        }

        //private Font boldFont;
        //private Font BoldFont {
        //    get {
        //        if (boldFont == null) {
        //            var ff = this.Font.FontFamily;
        //            var size = this.Font.Size;
        //            boldFont = new Font(ff, size, FontStyle.Bold);                    
        //        }
        //        return boldFont;
        //    }
        //}

        internal Canvas canvas;

        public Painter(IntPtr hwnd, Font font) {
            hwnd_ = hwnd;
            dc_ = Win32API.GetDC(hwnd_);
            this.Font = font;
            TabWidth = 4;
            AttributeLinePen = new Pen(Color.Black);

            init();
        }

        public void init() {

            //height_ = (int)(this.Font.GetHeight() + 0.5f);
            height_ = (int)(this.Font.GetHeight());
            
            widthMap.Clear();
            widthMap.Add('x', CalcStringWidth('x'));
            widthMap.Add('\t', W() * Math.Max(1, TabWidth));
            widthMap.Add('\x3000', CalcStringWidth('\x3000'));

            // 数字の最大幅を計算
            figWidth_ = 0;
            for (char ch = '0'; ch <= '9'; ++ch) {
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
            DeleteObj(lineNumberBackBrush);
            DeleteObj(specialCharPen);
            DeleteObj(AttributeLinePen);
            //Marshal.FreeHGlobal(widthPtr);
            Win32API.ReleaseDC(hwnd_, dc_);

            //DeleteObj(this.boldFont);
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
                obj = null;
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

        //TODO test
        Brush b = new SolidBrush(Color.LightGray);
        public void DrawFill(Graphics g,  int left, int top, int w, int h) {
            g.FillRectangle(b, left, top, w, h);
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
                    g.DrawLines(specialCharPen, pt);
                pt[0].X += w;
                pt[1].X += w;
                pt[2].X += w;
                pt[3].X += w;
            }
        }

        public void DrawZen(Graphics g, int X, int Y, int times) {
            int w = W('\x3000');
            for (int i = 0; i < times; i++) {
                g.DrawRectangle(specialCharPen, X + i * w + 2, Y + 2, w - 4, H() - 4);
            }
        }

        private Point[] tabPt = { new Point(), new Point(), new Point(), new Point() };
        public void DrawTab(Graphics g, int X, int Y, int w) {
            int h = H();
            int fh = h / 2;
            tabPt[0].X = X + 2;
            tabPt[0].Y = Y + h - 4;

            tabPt[1].X = X + 2;
            tabPt[1].Y = Y + fh;

            tabPt[2].X = X + w - 4;
            tabPt[2].Y = Y + fh;

            tabPt[3].X = X + w - 4;
            tabPt[3].Y = Y + h - 4; 

            g.DrawLines(specialCharPen, tabPt);
        }

        private Point[] returnPt1 = { new Point(), new Point() };
        private Point[] returnPt2 = { new Point(), new Point(), new Point() };
        public void DrawReturn(Graphics g, int X, int Y) {
            returnPt1[0].X = X+4;
            returnPt1[0].Y = Y + 2;
            returnPt1[1].X = X+4 ;
            returnPt1[1].Y = Y + H() -2;
            g.DrawLines(specialCharPen, returnPt1);

            returnPt2[0].X = X + 2;
            returnPt2[0].Y = Y + H() - 4;
            returnPt2[1].X = X + 4;
            returnPt2[1].Y = Y + H()-2;
            returnPt2[2].X = X + 6;
            returnPt2[2].Y = Y + H() - 4;
            g.DrawLines(specialCharPen, returnPt2);
        }

        public void DrawLineNum(Graphics g, string text, Color c, int X, int Y) {
            g.DrawString(text, this.Font, lineNumberBrush, new Point(X, Y));//, sf);
        }

        public void DrawLineNumBack(Graphics g, Rectangle rect) {
            g.FillRectangle(lineNumberBackBrush, rect);
        }

        //public void DrawText(Graphics g, string text, Color color, int X, int Y) {
        //    //g.DrawString(text, this.Font, TextBrush, new Point(X, Y));
        //    TextRenderer.DrawText(g,
        //       text,
        //       this.Font,
        //       new Point(X, Y),
        //       color,
        //       TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix);
        //       //TextFormatFlags.NoPadding | TextFormatFlags.NoClipping 
        //       //     | TextFormatFlags.Left | TextFormatFlags.Top | TextFormatFlags.Internal | TextFormatFlags.NoPrefix); 
        //}
        private TextFormatFlags textFlags = TextFormatFlags.Left | TextFormatFlags.Top | TextFormatFlags.Internal | TextFormatFlags.NoPadding | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix;
        //private TextFormatFlags textFlags = TextFormatFlags.Left | TextFormatFlags.WordBreak | TextFormatFlags.Internal | TextFormatFlags.NoClipping | TextFormatFlags.NoPrefix;
        public void DrawText(Graphics g, string text, YYS.Parser.Attribute attr, int X, int Y) {
            if ((attr.type & AttrType.Bold) == AttrType.Bold) {

                //TextRenderer.DrawText(g,
                //   text,
                //   this.BoldFont,
                //   new Point(X, Y),
                //   attr.color,
                //   textFlags);
                
                //StringFormat sf = new StringFormat(StringFormat.GenericTypographic);
                //sf.FormatFlags = StringFormatFlags.NoWrap;

                ////var s = BoldFont.SizeInPoints * (96 / 72) * 0.17;
                ////var pf = new PointF((float)(X), (float)Y);
                //g.DrawString(text, this.Font, lineNumberBrush, new PointF(X, Y), sf);
                //g.DrawString(text, this.Font, lineNumberBrush, new PointF((float)(X + 1), (float)(Y)), sf);

                TextRenderer.DrawText(g,
                   text,
                   this.Font,
                   new Point(X, Y),
                   attr.color,
                   textFlags);
                TextRenderer.DrawText(g,
                   text,
                   this.Font,
                   new Point(X + 1, Y),
                   attr.color,
                   textFlags);
            }
            else {
                TextRenderer.DrawText(g,
                   text,
                   this.Font,
                   new Point(X, Y),
                   attr.color,
                   textFlags);
            }
        }

        public void DrawAttribute(Graphics g, YYS.Parser.Attribute attr, int x1, int y1, int x2, int y2) {

            if ((attr.type & AttrType.UnderLine) == AttrType.UnderLine
                || (attr.type & AttrType.Link) == AttrType.Link) {
                AttributeLinePen.Color = attr.color;
                g.DrawLine(AttributeLinePen, x1, y1 + H(), x2, y2 + H());
            }

            if ((attr.type & AttrType.Strike) == AttrType.Strike) {
                AttributeLinePen.Color = attr.color;
                g.DrawLine(AttributeLinePen, x1, y1 + H() * 2/3, x2, y2 + H() *2/ 3);
            }
        }

        public void Invert(Graphics g, Rectangle rect) {
            Win32API.InvertRect(g.GetHdc(), ref rect);
            g.ReleaseHdc();
        }

        public int CalcStringWidth(char c) {
            int fit;
            int w = 0;

            switch (c) {
                case ' ':
                case '\x3000': //0x3000://L'　':
                    w = GetTextExtend(c.ToString(), int.MaxValue, out fit).width;
                    break;
                case '\t':
                    //TODO tab
                    w = T();
                    //w = nextTab(w);
                    break;
                default:
                    w = GetTextExtend(c.ToString(), int.MaxValue, out fit).width;
                    break;
            }
            return w;
        }

        private static char[] cs = { '\t' };
        public int CalcStringWidth(string text) {

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
                        //TODO tab
                        //w += T() * s.Length;//p.CalcStringWidth(s[ci].ToString());
                        for (int i2 = 0; i2 < s.Length; ++i2) {
                            w = nextTab(w);
                        }
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

        private Win32API.SIZE GetTextExtend(string str, int maxwidth, out int fit) {
            IntPtr OldFont = Win32API.SelectObject(dc_, hfont_);
            Win32API.SIZE size = new Win32API.SIZE();
            Win32API.GetTextExtentExPointW(dc_, str, str.Length, maxwidth, out fit, null, out size);
            Win32API.SelectObject(dc_, OldFont);
            return size;
        }

        internal void SetClip(Win32API.RECT rc) {
            Win32API.IntersectClipRect(dc_, rc.left, rc.top, rc.right, rc.bottom);
        }

        public void ClearClip()
        {
            Win32API.SelectClipRgn(dc_, IntPtr.Zero);
        }
    }
}
