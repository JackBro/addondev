using System.Collections.Generic;

namespace YYS.Parser {
    public delegate void HighlightChangeEventHandler();

    public interface IParser {
        event HighlightChangeEventHandler HighlightChangeEvent;

        void SetHighlight(IHighlight highlight);
        void AddHighlight(string partionID, IHighlight highlight);
        bool Parse(List<Line> text, int ad, int s, int e);
    }
}
