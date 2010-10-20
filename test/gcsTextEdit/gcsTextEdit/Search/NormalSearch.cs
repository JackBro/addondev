using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS {
    public class NormalSearch : ISearch {
        //private string searchword;
        public StringComparison Option { get; set; }

        public NormalSearch() {
            this.Option = StringComparison.CurrentCultureIgnoreCase;
        }

        //public NormalSearch(string searchword, StringComparison option) {
        public NormalSearch(StringComparison option) {
            //this.searchword = searchword;
            this.Option = option; 
        }

        //public NormalSearch(string searchword) {
        //    this.searchword = searchword;
        //    this.Option = StringComparison.CurrentCulture; 
        //}

        #region ISearch メンバ

        public string SearchWord {
            get;
            set;
        }
        public bool Search(string str, int len, int stt, ref int mbg, ref int med) {
            //int n = s_.Search(str, stt + s_.keylen());
            int n = str.IndexOf(SearchWord, stt, this.Option);
            if (n < 0)
                return false;
            mbg = n;
            med = n + SearchWord.Length;
            return true;
        }

        #endregion
    }

    public class NormalSearchRev : ISearch {
        //private string searchword;
        //public string Word;
        public StringComparison Option { get; set; }

        public NormalSearchRev() {
            this.Option = StringComparison.CurrentCultureIgnoreCase;
        }

        //public NormalSearchRev(string searchword, StringComparison option) {
        public NormalSearchRev(StringComparison option) {
            //this.searchword = searchword;
            this.Option = option;
        }

        //public NormalSearchRev(string searchword) {
        //    this.searchword = searchword;
        //    this.Option = StringComparison.CurrentCulture;
        //}

        #region ISearch メンバ

        public string SearchWord {
            get;
            set;
        }
        public bool Search(string str, int len, int stt, ref int mbg, ref int med) {
            //int n = s_.Search(str, stt + s_.keylen());
            int n = str.LastIndexOf(SearchWord, stt, this.Option);
            if (n < 0)
                return false;
            mbg = n;
            med = n + SearchWord.Length;
            return true;
        }

        #endregion
    }
}
