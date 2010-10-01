using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls.Parser {
    public interface IHighlight {
        AsControls.Parser.Attribute getDefault();
        List<Rule> getRules();
    }
}
