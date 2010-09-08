using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls {
    class RegexSearch : ISearch {

        public RegexSearch() {
        }

        #region ISearch メンバ

        public bool Search(string str, int len, int stt, ref int mbg, ref int med) {
            throw new NotImplementedException();
        }

        #endregion
    }
}
