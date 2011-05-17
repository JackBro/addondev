using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using System.Collections;

namespace lff.ListViewForm
{
    public enum ListViewItemSortType
    {
        Name,
        Name_R,
        Size,
        Size_R,
        Ext,
        Ext_R,
        LastWriteTime,
        LastWriteTime_R
    }

    public class CompalisonBuilder<T>
    {
        // 複数の項目を基準にしてソートする
        public Comparison<T> Combine(Comparison<T> before, Comparison<T> after)
        {
            return delegate(T x, T y)
            {
                int n = before(x, y);
                return (n == 0) ? after(x, y) : n;
            };
        }

        // ソートを逆順にする
        public Comparison<T> Reverse(Comparison<T> that)
        {
            return delegate(T x, T y) { return -that(x, y); };
        }
    }

    public class ListViewItemComparer
    {
        protected int _column;
        protected Dictionary<string, FileData> _fdatadic;
        protected int rev = 1;
        protected int DirectoryComparer(ListViewItem itemx, ListViewItem itemy)
        {
            if (itemx.Text != itemy.Text)
            {
                if (_fdatadic[itemx.Text].IsDirectory
                    && !_fdatadic[itemy.Text].IsDirectory)
                {
                    return -1;
                }
                else if (!_fdatadic[itemx.Text].IsDirectory
                    && _fdatadic[itemy.Text].IsDirectory)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                return 0;
            }
        }
    }
    public class ListViewItemNameComparer : ListViewItemComparer, IComparer
    {
        /// <summary>
        /// ListViewItemComparerクラスのコンストラクタ
        /// </summary>
        /// <param name="col">並び替える列番号</param>
        public ListViewItemNameComparer(int col, Dictionary<string, FileData> fdatadic)
        {
            _column = col;
            _fdatadic = fdatadic;
        }

        public ListViewItemNameComparer(Dictionary<string, FileData> fdatadic, Boolean reverse)
        {
            _fdatadic = fdatadic;
            if (reverse)
                rev = -1;
            else
                rev = 1;
        }

        //xがyより小さいときはマイナスの数、大きいときはプラスの数、
        //同じときは0を返す
        public int Compare(object x, object y)
        {
            //ListViewItemの取得
            ListViewItem itemx = (ListViewItem)x;
            ListViewItem itemy = (ListViewItem)y;

            //xとyを文字列として比較する
            //return string.Compare(itemx.Text, itemy.Text);

            int n = base.DirectoryComparer(itemx, itemy);
            return rev * ((n == 0) ? string.Compare(itemx.Text, itemy.Text) : n);
        }
    }
    public class ListViewItemSizeComparer : ListViewItemComparer, IComparer
    {

        /// <summary>
        /// ListViewItemComparerクラスのコンストラクタ
        /// </summary>
        /// <param name="col">並び替える列番号</param>
        public ListViewItemSizeComparer(int col, Dictionary<string, FileData> FDataDic)
        {
            _column = col;
        }

        public ListViewItemSizeComparer(Dictionary<string, FileData> fdatadic, Boolean reverse)
        {
            _fdatadic = fdatadic;
            if (reverse)
                rev = -1;
            else
                rev = 1;
        }

        //xがyより小さいときはマイナスの数、大きいときはプラスの数、
        //同じときは0を返す
        public int Compare(object x, object y)
        {
            //ListViewItemの取得
            ListViewItem itemx = (ListViewItem)x;
            ListViewItem itemy = (ListViewItem)y;

            FileData fdatax = _fdatadic[itemx.Text];
            FileData fdatay = _fdatadic[itemy.Text];

            //xとyを文字列として比較する
            //return string.Compare(itemx.SubItems[_column].Text,
            //    itemy.SubItems[_column].Text);
            
            int n = base.DirectoryComparer(itemx, itemy);
            int s = 0;
            if (fdatax.size > fdatay.size)
                s = -1;
            else if (fdatax.size < fdatay.size)
                s = 1;

            return rev * ((n == 0) ? s : n);
        }
    }
    public class ListViewItemExtComparer : ListViewItemComparer, IComparer
    {
        public ListViewItemExtComparer(Dictionary<string, FileData> fdatadic, Boolean reverse)
        {
            _fdatadic = fdatadic;
            if (reverse)
                rev = -1;
            else
                rev = 1;
        }

        //xがyより小さいときはマイナスの数、大きいときはプラスの数、
        //同じときは0を返す
        public int Compare(object x, object y)
        {
            //ListViewItemの取得
            ListViewItem itemx = (ListViewItem)x;
            ListViewItem itemy = (ListViewItem)y;

            FileData fdatax = _fdatadic[itemx.Text];
            FileData fdatay = _fdatadic[itemy.Text];

            int n = base.DirectoryComparer(itemx, itemy);
            return rev * ((n == 0) ? String.Compare(fdatax.ext, fdatay.ext) : n);
        }
    }

}
