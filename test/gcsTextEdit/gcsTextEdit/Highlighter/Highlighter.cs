using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Text.RegularExpressions;
using System.Globalization;

namespace AsControls
{
    public class Highlighter
    {
        public Dictionary<Match, HighlightInfo> RegMatchDic;
        public Dictionary<MatchEx, HighlightInfo> RegMatchExDic;
        public Dictionary<Regex, HighlightInfo> RegDic;

        public Highlighter()
        {
            RegMatchDic = new Dictionary<Match, HighlightInfo>();
            RegDic = new Dictionary<Regex,HighlightInfo>();

            RegMatchExDic = new Dictionary<MatchEx, HighlightInfo>();
        }

        public void Add(string pattern, TokenType type, Color color)
        {
            Regex regex = new Regex(pattern, RegexOptions.Compiled);

            HighlightInfo info = new HighlightInfo();
            info.type = type;
            info.FontColor = color;

            RegDic.Add(regex, info);
        }

        public HighlightInfo GetInfo(Match m)
        {
            return RegMatchDic[m];
        }

        public HighlightInfo GetMatchInfo(MatchEx m)
        {
            return RegMatchExDic[m];
        }

        public List<Match> RegMatchList(string text)
        {
            RegMatchDic.Clear();
            List<Match> mlist = new List<Match>();
            foreach (Regex reg in RegDic.Keys)
            {
                foreach (Match m in reg.Matches(text))
                {
                    RegMatchDic.Add(m, RegDic[reg]);
                    mlist.Add(m);
                }
            }
            mlist.Sort(new MatchComparer());

            return mlist;
        }

        public List<MatchEx> RegMatchList(IBuffer buf)
        {
            RegMatchExDic.Clear();
            List<MatchEx> mlist = new List<MatchEx>();
            foreach (Regex reg in RegDic.Keys)
            {
                foreach (Match m in reg.Matches(buf.ToString()))
                {
                    StringInfo sinfo1 = new StringInfo(buf.ToString().Substring(0, m.Index));
                    StringInfo sinfo2 = new StringInfo(buf.ToString().Substring(m.Index, m.Length));

                    int sindex = sinfo1.LengthInTextElements;
                    int slength = sinfo2.LengthInTextElements;

                    MatchEx mex = new MatchEx(sindex,
                        slength,
                        m.Success);
                    RegMatchExDic.Add(mex, RegDic[reg]);

                    mlist.Add(mex);
                }
            }
            mlist.Sort(new MatchExComparer());

            return mlist;
        }

        public void Parse(IBuffer buffer, List<AttributeInfo> attributeList)
        {
            if (buffer.Length < attributeList.Count)
            {
                attributeList.RemoveRange(buffer.Length, attributeList.Count - buffer.Length);
            }
            else if(buffer.Length > attributeList.Count)
            {
                int cnt = buffer.Length - attributeList.Count;
                for(int i=0; i<cnt; i++)
                {
                    attributeList.Add(new AttributeInfo());
                }
            }
            List<MatchEx> mlist = this.RegMatchList(buffer);

            int strIndex = 0;
            for (int i = 0; i < mlist.Count; i++)
            {
                if (mlist[i].Success)
                {
                    MatchEx mm = mlist[i];
                    if (mm.Index < strIndex) break;

                    Color color = this.GetMatchInfo(mm).FontColor;
                    TokenType type = this.GetMatchInfo(mm).type;

                    int ist = mm.Index;
                    int notmlen = ist - strIndex;
                    SetToken(buffer, strIndex, ist, attributeList, Color.Black, TokenType.TXT);
                    SetToken(buffer, ist, ist + mm.Length, attributeList, color, type);
                    strIndex = mm.Index + mm.Length;
                }
            }

            if (strIndex > 0 && strIndex < buffer.Length)
            {
                int len = buffer.Length - strIndex;
                for (int i = strIndex; i < buffer.Length; i++)
                {
                    attributeList[i].len = len;
                    attributeList[i].forecolor = Color.Black;
                    len--;
                }
            }
            else if (strIndex == 0)
            {
                SetToken(buffer, 0, buffer.Length, attributeList, Color.Black, TokenType.TXT);
            }
        }

        private void SetToken(IBuffer buffer, int start, int end, List<AttributeInfo> attributeList, Color color, TokenType type)
        {
            for (int i = start; i < end; )
            {
                int j;
                int splen;
                switch (buffer[i])
                {
                    case "　":
                        j = i;
                        while (j < end && buffer[j] == "　")
                        {
                            attributeList[j].forecolor = color;
                            attributeList[j].Token = TokenType.ZSP;
                            j++;
                        }
                        splen = j - i;
                        for (int n = i; n < j; n++)
                        {
                            attributeList[n].len = splen;
                            splen--;
                        }
                        i = j;
                        break;
                    case "\t":
                        j = i;
                        while (j < end && buffer[j] == "\t")
                        {
                            attributeList[j].forecolor = color;
                            attributeList[j].Token = TokenType.TAB;
                            j++;
                        }
                        splen = j - i;
                        for (int n = i; n < j; n++)
                        {
                            attributeList[n].len = splen;
                            splen--;
                        }
                        i = j;
                        break;
                    default:
                        j = i;
                        while (j < end && (buffer[j] != "\t" && buffer[j] != "　"))
                        {
                            attributeList[j].forecolor = color;
                            //attributeList[j].Token = TokenType.TXT;
                            attributeList[j].Token = type;
                            j++;
                        }
                        splen = j - i;
                        for (int n = i; n < j; n++)
                        {
                            attributeList[n].len = splen;
                            splen--;
                        }
                        i = j;
                        break;
                }
            }
        }
    }

    public class MatchExComparer : IComparer<MatchEx>
    {
        #region IComparer<Match> メンバ

        public int Compare(MatchEx x, MatchEx y)
        {
            return x.Index < y.Index ? 1 : 0;
        }

        #endregion
    }

    public class MatchComparer : IComparer<Match>
    {
        #region IComparer<Match> メンバ

        public int Compare(Match x, Match y)
        {
            return x.Index < y.Index ? 1 : 0;
        }

        #endregion
    }
}
