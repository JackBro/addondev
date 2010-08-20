using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace testfdb_cs {
    class FileListView<T>:ListView {
        private IEnumerable<T> datas;
        //private ListViewItemCollection itemCollection;

        public Func<T, string, string> LabelFunc { get; set; }

        public void inputData(IEnumerable<T> datas) {
            this.datas = datas;
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
            this.Items.Clear();

            List<ListViewItem> items = new List<ListViewItem>();
            //if (itemCollection == null) itemCollection = new ListViewItemCollection(this);
            //itemCollection.Clear();
           
            foreach(T data in datas){
                items.Add(createItem(data));
                //itemCollection.Add(createItem(data));
            }

            Items.AddRange(items.ToArray<ListViewItem>());
        }

        public IEnumerable<T> getSelectItemData(){
            List<T> selectdatas = new List<T>();
            foreach(ListViewItem item in SelectedItems){
                selectdatas.Add((T)item.Tag);
            }
            return selectdatas;
        }

    }
}
