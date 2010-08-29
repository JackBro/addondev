using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls {
    public static class Util {

        public static bool isHighSurrogate(char ch) {
            return (0xD800 <= ch && ch <= 0xDBFF);
        }

        public static bool isLowSurrogate(char ch) {
            return (0xDC00 <= ch && ch <= 0xDFFF);
        }

        public static VPos Max(VPos x, VPos y) {
            return (y<x ? x : y);
        }

        public static VPos Min(VPos x, VPos y) {
            return (x<y ? x : y);
        }
    }
}
