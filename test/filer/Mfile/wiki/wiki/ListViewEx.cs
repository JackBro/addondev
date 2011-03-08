using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    class ListViewEx : ListView {
        private List<Data> items;

        public int Page { get; set; }

        public List<Data> DataItems{
            get { return this.items; }
        }
        
        public bool IsPre {
            get {
                return (Page > 0);
            }
        }

        //public bool IsNext {
        //    get {

        //    }
        //}

        //public List<Data> GetCurrent() {
            
        //}

        public ListViewEx(List<Data> items) {

            Page = 0;

            this.items = items;

            this.FullRowSelect = true;
            this.HideSelection = false;
            this.View = View.Details;

            ColumnHeader headerID = new ColumnHeader();
            headerID.Name = "id";
            headerID.Text = "ID";
            this.Columns.Add(headerID);

            ColumnHeader headerName = new ColumnHeader();
            headerName.Name = "title";
            headerName.Text = "Title";
            this.Columns.Add(headerName);
            //headerName.Width = -2;

            ColumnHeader headerCreationTime = new ColumnHeader();
            headerCreationTime.Name = "creationtime";
            headerCreationTime.Text = "CreationTime";
            this.Columns.Add(headerCreationTime);
            //headerCreationTime.Width = -2;

            this.VirtualMode = true;
            this.VirtualListSize = items.Count;

            this.RetrieveVirtualItem += (sender, e) => {
                if (e.ItemIndex < this.items.Count) {
                    var data = this.items[e.ItemIndex];
                    var item = new ListViewItem();
                    item.Text = data.ID.ToString();
                    item.SubItems.Add(data.Title);
                    item.SubItems.Add(data.CreationTime.ToString("yyyy/MM/dd HH:mm:ss"));
                    e.Item = item;
                }
            };
        }

        public void DeleteItem(Data item) {
            items.Remove(item);
            this.VirtualListSize = items.Count;
        }

        public int AddItem(Data item){
            //items.Add(item);
            var i = ItemManager.Insert(items, item);
            this.VirtualListSize = items.Count;
            return i;
        }

        //public void AddItem(int index, Data item) {
        //    //items.Add(item);
        //    //items.Insert(index, item);
        //    ItemManager.Insert(items, item);
        //    this.VirtualListSize = items.Count;
        //}

        public void UpDate(Data item){
            
        }
        private List<Data> nulldata = new List<Data>();
        public List<Data> GetActive() {
            if (this.SelectedIndices.Count > 0) {
                var a = new List<Data>();
                foreach (int index in this.SelectedIndices) {
                    a.Add(this.items[index]);
                }
                return a;
            }

            return nulldata;
        }

        public Data GetSelectedItem() {
            if (this.SelectedIndices.Count == 1) {
                return this.items[this.SelectedIndices[0]];
            }
            return null;
        }
    }
}
