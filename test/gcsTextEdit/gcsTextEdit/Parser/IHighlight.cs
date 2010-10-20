using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS.Parser {
    public interface IHighlight {
        YYS.Parser.Attribute getDefault();
        List<Rule> getRules();
    }
}
