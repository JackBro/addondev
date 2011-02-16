using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    class ListViewEx : ListView {
        private List<Data> items;

        public ListViewEx(List<Data> items) {
            this.items = items;

            this.FullRowSelect = true;
            this.HideSelection = false;
            this.View = View.Details;
            ColumnHeader headerName = new ColumnHeader();
            headerName.Name = "name";
            headerName.Text = "name";
            this.Columns.Add(headerName);
            headerName.Width = -2;

            this.VirtualMode = true;
            this.VirtualListSize = items.Count;

            this.RetrieveVirtualItem += (sender, e) => {
                if (e.ItemIndex < this.items.Count) {
                    var data = this.items[e.ItemIndex];
                    var item = new ListViewItem();
                    item.Text = data.Text;
                    e.Item = item;
                }
            };
        }

        public void DeleteItem(Data item) {
            items.Remove(item);
            this.VirtualListSize = items.Count;
        }

        public void AddItem(Data item){
            items.Add(item);
            this.VirtualListSize = items.Count;
        }

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
    }
}
