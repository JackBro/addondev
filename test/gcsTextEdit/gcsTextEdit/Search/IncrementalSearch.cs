using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS {
    public class IncrementalSearch {

        private Search search;
        public ISearch Searcher {
            get { return search.Searcher; }
            set { search.Searcher = value; } 
        }
 
        private GCsTextEdit view;
        private DPos pos = null;

        public string SearchWord{
            get { return search.SearchWord; }
            set { search.SearchWord = value; }
        }

        public IncrementalSearch(GCsTextEdit view) {
            search = new Search(view);
            this.view = view;
        }

        public void Cancel() {
            pos = null;
        }

        public bool FindNext() {

            if (pos != null) {
                //view.cursor.MoveCur(new DPos(pos.tl, pos.ad), false);
                view.MoveCursor(new DPos(pos.tl, pos.ad));
            }

            if (search.FindNextImpl()) {

                DPos dummy;
                if (pos == null) {
                    view.GetSelction(out pos, out dummy);
                }
                else {
                    DPos cur;
                    view.GetSelction(out cur, out dummy);
                    if (pos.tl == cur.tl && pos.ad == cur.ad) {
                        //pos = view.GetSelect().t1;
                        view.GetSelction(out pos, out dummy);
                    }
                }

                return true;
            }

            return false;
        }

        public bool FindPrev() {
            if (pos != null) {
                //view.cursor.MoveCur(new DPos(pos.tl, pos.ad), false);
                view.MoveCursor(new DPos(pos.tl, pos.ad));
            }

            if (search.FindPrevImpl()) {

                DPos dummy;
                if (pos == null) {
                    //pos = view.GetSelect().t1;
                    view.GetSelction(out pos, out dummy);
                }
                else {
                    //DPos cur = view.GetSelect().t1;
                    DPos cur;
                    view.GetSelction(out cur, out dummy);
                    if (pos.tl == cur.tl && pos.ad == cur.ad) {
                        //pos = view.GetSelect().t1;
                        view.GetSelction(out pos, out dummy);
                    }
                }

                return true;
            }

            return false;
        }
    }
}
