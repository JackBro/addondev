using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls
{
    public class Search
    {
        private Document doc;
        public string searchstr;

        public Search(Document doc)
        {
            this.doc = doc;
        }

        public Boolean FindNext(CaretInfo s, ref CaretInfo beg, ref CaretInfo end)
        {
            int start = s.ad;
            for (int i = s.tl; i < doc.tlNum; i++)
            {
                string str = doc.LineList[i].Text.ToString();
                int index = str.IndexOf(searchstr, start);
                start = 0;
                if (index >=0)
                {
                    beg.tl = end.tl = i;
                    beg.ad = index;
                    end.ad = beg.ad + searchstr.Length;

                    return true;
                }
            }

            return false;
        }
    }
}
