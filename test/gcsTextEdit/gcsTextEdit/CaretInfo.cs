using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;

namespace AsControls
{
    public class CaretInfo
    {
        public int tl;
        public int ad;

        public int vl;
        public int rl;

        public int vx;
        public int rx;

        public CaretInfo()
        {
        }

        public CaretInfo(CaretInfo curpos)
        {
            tl = curpos.tl;
            ad = curpos.ad;
            vl = curpos.vl;
            rl = curpos.rl;
            vx = curpos.vx;
            rx = curpos.rx;
        }

        public void CopyTo(ref CaretInfo dist)
        {
            dist.tl=tl;  
            dist.ad=ad;  
            dist.vl=vl;  
            dist.rl=rl;  
            dist.vx=vx;  
            dist.rx=rx; 
        }

        public static Boolean operator ==(CaretInfo s, CaretInfo e)
        {
            return (s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl && s.vx == e.vx && s.rx == e.rx);
            //return (s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl);
        }

        public static Boolean operator !=(CaretInfo s, CaretInfo e)
        {
            return !(s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl && s.vx == e.vx && s.rx == e.rx);
            //return !(s.tl == e.tl && s.ad == e.ad && s.vl == e.vl && s.rl == e.rl);
        }

        public static Boolean operator <(CaretInfo s, CaretInfo e)
        {
            return (s.tl < e.tl || (s.tl == e.tl && s.ad < e.ad));
        }

        public static Boolean operator >(CaretInfo s, CaretInfo e)
        {
            return (s.tl > e.tl || (s.tl == e.tl && s.ad > e.ad));
        }

        public static Boolean operator <=(CaretInfo s, CaretInfo e)
        {
            return (s.tl < e.tl || (s.tl == e.tl && s.ad <= e.ad));
        }

        public static Boolean operator >=(CaretInfo s, CaretInfo e)
        {
            return (s.tl > e.tl || (s.tl == e.tl && s.ad >= e.ad));
        }

        //public override bool Equals(object obj) {
        //    CaretInfo caret = obj as CaretInfo;
        //    if (caret == null) return false;

        //    return (caret.ad == ad && caret.rl == rl && caret.rx == rx && caret.tl == tl && caret.vl == vl && caret.vx == vx);
        //}
    }
}
