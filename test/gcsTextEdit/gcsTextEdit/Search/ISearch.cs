using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls {
    public interface ISearch {

        string SearchWord { get; set; }

        /// <summary>
        /// 検索を行う
        /// 下方向サーチオブジェクトの場合、stt &lt;= *beg の範囲
        /// 上方向サーチオブジェクトの場合、*beg &lt;= stt の範囲を検索
        /// </summary>
        /// <param name="str">対象文字列</param>
        /// <param name="len">対象文字列の長さ</param>
        /// <param name="stt">検索開始index。0なら先頭から</param>
        /// <param name="mbg">マッチ結果の先頭index</param>
        /// <param name="med">マッチ結果の終端indexの１個後ろ</param>
        /// <returns>マッチしたかどうか</returns>
        bool Search(string str, int len, int stt, ref int mbg, ref int med);
    }
}
