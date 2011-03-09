using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace wiki {
    enum SearchMode {
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
        }
        public override Predicate<Data> getSearch() {
            return x => { return true; };
        }
    }

    class SearchNormal : Search{
        public SearchNormal() {
            this.Mode = SearchMode.Normal;
        }
        public override Predicate<Data> getSearch() {
            return x => {
                return x.Text.Contains(this.Pattern);
            };
        }
    }

    class SearchRegex : Search {
        private Regex reg;
        private string pattern;
        public new String Pattern {
            get { return this.pattern; }
            set {
                this.pattern = value;
                reg = new Regex(this.pattern, RegexOptions.Compiled);
            }
        }

        public SearchRegex() {
            this.Mode = SearchMode.Regex;
        }

        public override Predicate<Data> getSearch() {
            return x => {
                return reg.IsMatch(x.Text);
            };
        }
    }
}
