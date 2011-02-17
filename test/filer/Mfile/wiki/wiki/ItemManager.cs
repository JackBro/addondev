using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace wiki {
    public enum ChangeType {
        Add,
        UpDate,
        Delete
    }
    public class CallBackEventArgs : EventArgs {
        public Data Item;
        public int Index;
        public ChangeType type;
    }
    delegate void EventHandler(object sender, CallBackEventArgs e);

    class ItemManager {
        public event EventHandler eventHandler;
        public bool IsDirty = false;

        public string DataPath { get; set; }
        public Data EditingData {
            get;
            set;
        }
        private List<Data> datas = new List<Data>();

        public void Load(){
            datas = Serializer.Deserialize<List<Data>>(DataPath, new List<Data>());      
        }
        public void Save(){
            Serializer.Serialize<List<Data>>(DataPath, datas);
            IsDirty = false;
        }

        public void Insert(Data data) {
            IsDirty = true;
            data.ID = this.GetNewID();
            datas.Add(data);
            var index = datas.IndexOf(data);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs {Index=index, Item = data, type= ChangeType.Add }); 
            }
        }

        public void Delete() {
            IsDirty = true;
        }

        public void UpDate(Data item) {
            IsDirty = true;
            var index = datas.IndexOf(item);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { Index = index, Item = item, type = ChangeType.UpDate });
            }
        }

        public long GetNewID() {
            if(datas.Count == 0){
                return 0;
            }
            return datas[datas.Count - 1].ID + 1;
        }

        public List<Data> Filter(Predicate<Data> pre) {
            return datas.FindAll(pre);
            //datas.FindAll(x => { return true; });
        }

        public Data GetItem(long id){
            var dydata = new Data { ID=id };
            var index = datas.BinarySearch(dydata, new srarchID());
            if (index > 0 && index < datas.Count)
                return datas[index];

            return null;
        }
    }

    class srarchID : IComparer<Data> {
        //x<y => -1、x>y => 1, x==y => 0
        #region IComparer<Data> メンバ

        public int Compare(Data x, Data y) {
            if (x.ID < x.ID) return -1;
            if (x.ID > x.ID) return 1;

            return 0;
        }

        #endregion
    }
}
