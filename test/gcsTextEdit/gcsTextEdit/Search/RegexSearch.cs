using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace AsControls {
    public class RegexSearch : ISearch {
        private Regex regex;
        private string regexStr;
        public RegexSearch() {
        }

        #region ISearch メンバ

        public string SearchWord {
            get { return regexStr; }
            set {
                if (regexStr != value) {
                    this.regex = new Regex(value, RegexOptions.Compiled);
                }
                regexStr = value; 
            }
        }
        public bool Search(string str, int len, int stt, ref int mbg, ref int med) {
            var match = this.regex.Match(str, stt);
            if (!match.Success)
                return false;

            mbg = match.Index;
            med = match.Index + match.Length;
            return true;
        }

        #endregion
    }

    public class RegexSearchRev : ISearch {
        private Regex regex;
        private string regexStr;
        public RegexSearchRev() {
        }

        #region ISearch メンバ

        public string SearchWord {
            get { return regexStr; }
            set {
                if (regexStr != value) {
                    this.regex = new Regex(value, RegexOptions.Compiled);
                }
                regexStr = value;
            }
        }
        public bool Search(string str, int len, int stt, ref int mbg, ref int med) {

            int d = -1;      
            int e = -1;

            for (int s = stt; s != e; s += d) {

                var match = this.regex.Match(str, s);
                if (match.Success && match.Index == s) {
                    //var match = matches[matches.Count - 1];
                    mbg = s;
                    med = s + match.Length;
                    return true;
                }
            }

            return false;

            //var matches = this.regex.Matches(str, stt);
            //if (matches.Count==0)
            //    return false;

            //var match = matches[matches.Count - 1];
            //mbg = match.Index;
            //med = match.Index + match.Length;
            //return true;
        }

        #endregion
    }
}
