using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace wiki {
    public enum SearchMode {
        All,
        Normal,
        Regex,
        Migemo
    }
    abstract class Search {
        public SearchMode Mode { get; protected set; }
        public String Pattern { get; set; }

        public abstract Predicate<Data> getSearch();

        public override bool Equals(object obj) {

            if (obj == null || this.GetType() != obj.GetType()) {
                return false;
            }
            
            Search s = (Search)obj;
            return (this.Mode == s.Mode) && (this.Pattern == s.Pattern);
        }
    }

    class SearchAll : Search {
        public SearchAll() {
            this.Mode = SearchMode.All;
            this.Pattern = "All";
        }
        public override Predicate<Data> getSearch() {
            return x => { return true; };
        }
    }

    class SearchNormal : Search{
        public SearchNormal(String Pattern) {
            this.Mode = SearchMode.Normal;
            this.Pattern = Pattern;
        }
        public override Predicate<Data> getSearch() {
            return x => {
                return x.Text.Contains(this.Pattern);
            };
        }
    }

    class SearchRegex : Search {
        private Regex reg;

        public SearchRegex(String Pattern) {
            this.Mode = SearchMode.Regex;
            this.Pattern = Pattern;
            reg = new Regex(this.Pattern, RegexOptions.Compiled);
        }

        public override Predicate<Data> getSearch() {
            return x => {
                return reg.IsMatch(x.Text);
            };
        }
    }

    class SearchMigemo : Search {
        private Regex reg;

        public SearchMigemo(String Pattern) {
            this.Mode = SearchMode.Migemo;
            this.Pattern = Pattern;
            reg = new Regex(this.Pattern, RegexOptions.Compiled);
        }

        public override Predicate<Data> getSearch() {
            return x => {
                return reg.IsMatch(x.Text);
            };
        }
    }
}
