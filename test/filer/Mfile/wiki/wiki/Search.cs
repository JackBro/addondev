using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace wiki {
    public enum SearchMode {
        All,
        Text,
        Regex,
        Migemo,
        DateTime
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

        public static bool operator ==(Search x, Search y) {
            if (Object.ReferenceEquals(x, null) && Object.ReferenceEquals(y, null)) return true;
            if (Object.ReferenceEquals(x, null) || Object.ReferenceEquals(y, null)) return false; 

            return (x.Mode == y.Mode) && (x.Pattern == y.Pattern);
        }
        public static Boolean operator !=(Search x, Search y) {
            return !(x == y);
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

    class SearchText : Search{
        public SearchText(String Pattern) {
            this.Mode = SearchMode.Text;
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

    class SearchDateTime : Search {
        private DateTime start, end;
        public SearchDateTime(DateTime start, DateTime end) {
            this.start = start;
            this.end = end;
            this.Mode = SearchMode.DateTime;
            this.Pattern = "datetime";
            
        }

        public override Predicate<Data> getSearch() {
            return x => {
                return (this.start <= x.CreationTime && x.CreationTime <= this.end);
            };         
        }
    }
}
