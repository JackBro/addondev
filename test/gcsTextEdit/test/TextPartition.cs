using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using YYS.Parser;

namespace test {
    class TextPartition : AbstractPartition {
        private Highlight h = new Highlight();

        public TextPartition() {
            
        }

        public override IHighlight GetHighlight() {
            return h;
        }

        public override PartRule GetPartRule() {
            return null;
        }
    }
}
