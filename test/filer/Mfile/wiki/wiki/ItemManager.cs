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
            datas.Add(data);
            var index = datas.IndexOf(data);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs {Index=index, Item = data, type= ChangeType.Add }); 
            }
        }

        public void Delete() {
        }

        public void UpDate() {

        }

        public List<Data> Filter(Predicate<Data> pre) {
            return datas.FindAll(pre);
            //datas.FindAll(x => { return true; });
        }
    }
}
