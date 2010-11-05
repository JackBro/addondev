using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS.Parser {
    internal class DefaultPartition : AbstractPartition {
        private IHighlight Highlight;
        public DefaultPartition(IHighlight Highlight) {
            this.ID = Document.DEFAULT_ID;
            this.Highlight = Highlight;
        }

        public override IHighlight GetHighlight() {
            return this.Highlight;
        }

        public override PartRule GetPartRule() {
            return null;
        }
    }
}
