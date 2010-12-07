using System.Collections.Generic;

namespace YYS.Parser {
    public delegate void ReParseAllEventHandler();

    public interface IParser {
        event ReParseAllEventHandler ReParseAll;
        void SetHighlight(IHighlight highlight);
        void AddHighlight(string partionID, IHighlight highlight);
        bool Parse(List<Line> text, int ad, int s, int e);
    }
}
