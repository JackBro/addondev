using System.Collections.Generic;

namespace YYS.Parser {
    interface IParser {
        void SetHighlight(IHighlight highlight);
        void AddHighlight(string partionID, IHighlight highlight);
        bool Parse(List<Line> text, int ad, int s, int e);
    }
}
