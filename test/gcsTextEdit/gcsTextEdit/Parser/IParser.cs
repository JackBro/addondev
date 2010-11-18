using System.Collections.Generic;

namespace YYS.Parser {
    interface IParser {
        bool Parser(List<Line> lines, int ad, int s, int e);
    }
}
