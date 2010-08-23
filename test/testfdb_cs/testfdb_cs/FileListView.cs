using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Collections;

namespace testfdb_cs {


    class ListViewItemComparer<T> : IComparer<T> {

        public SortOrder Order { get; set; }
        public int ColumnIndex { get; set; }

        public Func<T, T, int, int> SortFunc { get; set; }

        #region IComparer<T> メンバ

        public int Compare(T x, T y) {
            //ListViewItem itemx = x as ListViewItem;
            //ListViewItem itemy = y as ListViewItem;
            int result = SortFunc(x, y, ColumnIndex);
            if (Order == SortOrder.Descending) {
                result = -result;
            }
            return result;
        }

        #endregion
    }

    class FileListView<T>:ListView {
        private List<T> datas;
        //private ListViewItemCollection itemCollection;
        private Dictionary<T, ListViewItem> dataMap;

        public ListView.ListViewItemCollection myCol;
        public ListView.SelectedIndexCollection mySel;

        public Func<T, string, string> LabelFunc { get; set; }

        public Func<T, int, int> SortFunc { get; set; }

        public ListViewItemComparer<T> listViewItemComparer { get; set; }

        public FileListView() {
            dataMap = new Dictionary<T, ListViewItem>();

            myCol = new ListView.ListViewItemCollection(this);
            mySel = new ListView.SelectedIndexCollection(this);
           
            this.VirtualMode = true;
            this.RetrieveVirtualItem += new RetrieveVirtualItemEventHandler(FileListView_RetrieveVirtualItem);
           
            listViewItemComparer = new ListViewItemComparer<T>();
            listViewItemComparer.Order = SortOrder.Ascending;
            this.ColumnClick += (sender, e) => {
                listViewItemComparer.ColumnIndex = e.Column;
                listViewItemComparer.Order = listViewItemComparer.Order == SortOrder.Descending ? SortOrder.Ascending : SortOrder.Descending;
                datas.Sort(listViewItemComparer);
                this.Refresh();
            };

        }

        void FileListView_RetrieveVirtualItem(object sender, RetrieveVirtualItemEventArgs e)
        {
            if (e.Item == null)
            {
                if (!dataMap.ContainsKey(datas.ElementAt(e.ItemIndex)))
                {
                    e.Item = createItem(datas.ElementAt(e.ItemIndex));
                    dataMap.Add(datas.ElementAt(e.ItemIndex), e.Item);
                }
                else
                {
                    e.Item = dataMap[datas.ElementAt(e.ItemIndex)];
                }
            }
            else
            {
                if (!dataMap.ContainsKey((T)e.Item.Tag))
                {
                    e.Item = createItem(datas.ElementAt(e.Item.Index));
                    dataMap.Add(datas.ElementAt(e.Item.Index), e.Item);
                }
                else
                {
                    e.Item = dataMap[(T)e.Item.Tag];
                }
            }
        }

        public void inputData(List<T> datas) {
            this.datas = datas;
            this.VirtualListSize = this.datas.Count();
        }

        public ListViewItem createItem(T data) {
            ListViewItem item = new ListViewItem();
            //string[] dy = new string[Columns.Count];
            //foreach (ColumnHeader colum in Columns) {
            //    item.SubItems.Add(new ListViewItem.ListViewSubItem(
            //}
            foreach (ColumnHeader colum in Columns) {
                item.SubItems.Add(new ListViewItem.ListViewSubItem());
                //item.SubItems.Add(LabelFunc(data, colum.Name)).
                item.SubItems[colum.Index].Text = LabelFunc(data, colum.Name);
                item.Tag = data;
            }
            return item;
        }

        public void setitem() {
            dataMap.Clear();

            this.Items.Clear();

            //List<ListViewItem> items = new List<ListViewItem>();
            //if (itemCollection == null) itemCollection = new ListViewItemCollection(this);
            //itemCollection.Clear();
           
            //foreach(T data in datas){
            //    items.Add(createItem(data));
            //    //itemCollection.Add(createItem(data));
            //}

            //Items.AddRange(items.ToArray<ListViewItem>());
        }

        public T getData(ListViewItem item)
        {
            return (T)item.Tag;
        }

        public IEnumerable<T> getSelectItemData(){
            List<T> selectdatas = new List<T>();

            foreach (int index in mySel) {
                selectdatas.Add(datas[index]);
            }
            return selectdatas;
        }

    }
}
