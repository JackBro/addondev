using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace CsEdit
{
    class View
    {
        private Document doc;
        public Document Document
        {
            get { return doc; }
            set 
            {
                doc = value;
                //add evevnt
            }
        }

        private CursorInfo cur;

        private class ViewRect
        {
            public Rectangle rc;
            public int YMIN;
            public int YMAX;
            public int TLMIN;
            public int SXB, SXE;   // 選択範囲のx座標
            public int SYB, SYE;   // 選択範囲のy座標

            public ViewRect()
            {
                rc = new Rectangle();
                YMIN = 0;
                YMAX = 0;
                TLMIN = 0;
                SXB = SXE = SYB = SYE = 0;
            }
        }

        //private class ScrollInfo
        //{
        //    int udScr_tl_;   // 一番上に表示される論理行のTLine_Index
        //    int udScr_vrl_; //一番上に表示される表示行のVRLine_Index
        //    int nPage;
        //    int 
        //}

        int udScr_tl_;   // 一番上に表示される論理行のTLine_Index
        int udScr_vrl_; //一番上に表示される表示行のVRLine_Index

        public void GetVPos(int x, int y, ref CursorPos curPos)
        {
            //x = 0;
            int tl = udScr_tl_;
            int vl = vScrollBar.Value - udScr_vrl_;
            int rl = y / m_lineHeight + udScr_vrl_;
            if (rl >= 0) // View上端より下の場合、下方向を調べる
                while (tl < doc.LineList.Count && doc.GetvlCnt(tl) <= rl)
                {
                    vl += doc.GetvlCnt(tl);
                    rl -= doc.GetvlCnt(tl);
                    ++tl;
                }
            else           // View上端より上の場合、上方向を調べる
                while (0 <= tl && rl < 0)
                {
                    vl -= doc.GetvlCnt(tl);
                    rl += doc.GetvlCnt(tl);
                    --tl;
                }

            if (tl == doc.LineList.Count) // EOFより下に行ってしまう場合の補正
            {
                --tl;
                vl -= doc.GetvlCnt(tl);
                rl = doc.GetvlCnt(tl) - 1;
            }
            else if (tl == -1) // ファイル頭より上に行ってしまう場合の補正
            {
                tl = vl = rl = 0;
            }

            curPos.tl = tl;
            curPos.vl = vl + rl;
            curPos.rl = rl;

            if (rl < doc.GetvlCnt(tl))
            {
                string str = doc.LineList[tl].LineText;
                int adend = doc.LineList[tl].WrapList[rl];
                int ad = (rl == 0 ? 0 : doc.LineList[tl].WrapList[rl - 1]);
                int vx = (rl == 0 ? 0 : getStringWidth(str[ad++].ToString()));

                while (ad < adend)
                {
                    //int nvx = (str[ad]=='\t'
                    //    ? fnt().nextTab(vx)
                    //    :  vx + fnt().W(&str[ad])
                    //);
                    int nvx = vx + getStringWidth(str[ad].ToString());

                    if (x + 2 < nvx)
                        break;
                    vx = nvx;
                    ++ad;
                }

                curPos.ad = ad;
                curPos.rx = curPos.vx = vx;
            }
            else
            {
                curPos.ad = curPos.rx = curPos.vx = 0;
            }
        }
    }
}
