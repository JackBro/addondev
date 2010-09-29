using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace AsControls {

    //public class CursorPos {
    //    /// <summary>
    //    /// tl
    //    /// 0-
    //    /// </summary>
    //    public int line;
    //    /// <summary>
    //    /// ad
    //    /// 0-
    //    /// </summary>
    //    public int index;
    //}

    public class Tuple<T1, T2> {
        public T1 t1;
        public T2 t2;

        public Tuple() { }

        public Tuple(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }
    }

    public class Tuple<T1, T2, T3> {
        public T1 t1;
        public T2 t2;
        public T3 t3;

        public Tuple() { }

        public Tuple(T1 t1, T2 t2, T3 t3) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }
    }
    
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

        public static bool IsAlphabet(string str) {
            return Regex.IsMatch(str, "^[a-zA-Z]+$");
        }

        public static bool IsHiragana(string str) {
            return Regex.IsMatch(str, @"^\p{IsHiragana}*$");
        }

        public static bool IsKatakana(string str) {
            return Regex.IsMatch(str, @"^\p{IsKatakana}*$");
        }

        public static bool IsKanji(string str) {
            return Regex.IsMatch(str, @"^\p{IsCJKUnifiedIdeographs}*$");
        }


        public static CharType getCharType(char c) {
            string str = c.ToString();
            if (Char.IsDigit(c)) return CharType.Digit;
            if (IsAlphabet(str)) return CharType.Alphabet;
            if (IsHiragana(str)) return CharType.Hiragana;
            if (IsKatakana(str)) return CharType.Katakana;
            if (IsKanji(str)) return CharType.Kanji;
            if (c == '\t') return CharType.Tab;
            if (c == ' ') return CharType.WSHan;
            if (c == '\x3000') return CharType.WSZen;

            return CharType.Unknown;
        }

        public static bool isIdentifierPart(char c) {
            string str = c.ToString();
            if (Char.IsDigit(c) || Char.IsControl(c)) return false;
            if ((c >= 33 && c <= 47)
                || (c >= 58 && c <= 74)
                || (c >= 91 && c <= 96)
                || (c >= 123 && c <= 126)) return false;
            if (c == '\t' || c == ' ' || c == '\x3000') return false;

            return true;
        }

        public enum CharType {
            Unknown,
            Digit,
            Alphabet,
            Hiragana,
            Katakana,
            Kanji,
            Tab,
            WSHan,
            WSZen
        }
    }
}
