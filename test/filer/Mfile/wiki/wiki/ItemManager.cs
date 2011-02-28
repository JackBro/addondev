using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace wiki {
    public enum ChangeType {
        Insert,
        UpDate,
        Delete
    }
    public class CallBackEventArgs : EventArgs {
        public Data Item;
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

        //public void Insert(Data data) {
        //    IsDirty = true;
        //    data.ID = this.GetNewID();
        //    var i = datas.BinarySearch(data, new DateTimeComparer());
        //    i = i < 0 ? 0 : i;
        //    datas.Insert(i, data);
        //    //datas.Add(data);
        //    var index = datas.IndexOf(data);
        //    if (eventHandler != null) {
        //        eventHandler(this, new CallBackEventArgs {Index=index, Item = data, type= ChangeType.Add }); 
        //    }
        //}
        
        public static int Insert(List<Data> list, Data item) {
            if (list.Count == 0) {
                item.ID = 0;
                list.Add(item);
                return 0;
            }

            var cmp = new DateTimeComparer();

            int r = list.Count - 1;
            int l = 0;
            int comp;
            while (l < r) {
                int m = (r + l) >> 1;
                //comp = this.datas[m].CompareTo(elem);
                comp = cmp.Compare(list[m], item);
                if (comp < 0) r = m - 1;
                else if (comp > 0) l = m + 1;
                else return -1; // 重複不可
            }

            //comp = this.buffer[l].CompareTo(elem);
            comp = cmp.Compare(list[l], item);

            var index = comp;

            if (comp < 0)
                //this.datas.Insert(l, elem); 
                index = l;
            else if (comp > 0)
                //this.datas.Insert(l + 1, elem);
                index = l + 1;

            //elem.ID = this.GetNewID();
            list.Insert(index, item);

            return index;
        }

        public void Insert(Data item) {
            IsDirty = true;
            ItemManager.Insert(this.datas, item);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.Insert });
            }

        }
        //public void Insert(Data elem) {
        //    IsDirty = true;

        //    if (this.datas.Count == 0) {
        //        elem.ID = 0;
        //        this.datas.Add(elem);
        //        if (eventHandler != null) {
        //            eventHandler(this, new CallBackEventArgs { Item = elem, type = ChangeType.Insert });
        //        }
        //        return;
        //    }

        //    var cmp = new DateTimeComparer();
            
        //    int r = this.datas.Count - 1;
        //    int l = 0;
        //    int comp;
        //    while (l < r) {
        //        int m = (r + l) >> 1;
        //        //comp = this.datas[m].CompareTo(elem);
        //        comp = cmp.Compare(this.datas[m], elem);
        //        if (comp > 0) r = m - 1;
        //        else if (comp < 0) l = m + 1;
        //        else return; // 重複不可
        //    }

        //    //comp = this.buffer[l].CompareTo(elem);
        //    comp = cmp.Compare(this.datas[l], elem);

        //    var index = comp;

        //    if (comp < 0)
        //        //this.datas.Insert(l, elem); 
        //        index = l;
        //    else if (comp > 0)
        //        //this.datas.Insert(l + 1, elem);
        //        index = l + 1;

        //    elem.ID = this.GetNewID();
        //    this.datas.Insert(index, elem);

        //    if (eventHandler != null) {
        //        eventHandler(this, new CallBackEventArgs { Item = elem, type = ChangeType.Insert });
        //    }
        //}


        public void Delete(long id) {
            IsDirty = true;
            var item = GetItem(id);
            if (item != null) {
                //datas.Remove(item);
                if (eventHandler != null) {
                    eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.Delete });
                }
            }
        }

        public void UpDate(Data item) {
            IsDirty = true;
            var index = datas.IndexOf(item);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.UpDate });
            }
        }

        public long GetNewID() {
            if(datas.Count == 0){
                return 0;
            }

            //long mm = 0;
            //var max = datas.Max(new Func<Data, long>((x) => {
            //    if (mm < x.ID) mm = x.ID;
            //    return mm;
            //}));
            //return mm + 1;

            var maxid = datas.Max(n=>n.ID);
            return maxid + 1;
            
        }

        public List<Data> Filter(Predicate<Data> pre) {
            return datas.FindAll(pre);
            //datas.Select(
            //datas.FindAll(x => { return true; });
        }

        public Data GetItem(long id){
            //var dydata = new Data { ID=id };
            //long mm = 0;
            //var max = datas.Max(new Func<Data, long>((x)=>{
            //    if (mm < x.ID) mm = x.ID;
            //    return mm;
            //}));
            //var index = datas.BinarySearch(dydata, new IDComparer());

            var index = datas.FindIndex(new Predicate<Data>((x) => {
                return x.ID == id;
            }));

            if (index >= 0 && index < datas.Count)
                return datas[index];

            return null;
        }

        public List<Data> GetItem(List<long> ids) {
            var res = new List<Data>();
            foreach (var id in ids) {
                var data = this.GetItem(id);
                if (data != null) {
                    res.Add(data);
                }
            }
            return res;
        }
    }

    class IDComparer : IComparer<Data> {
        //x<y => -1、x>y => 1, x==y => 0
        #region IComparer<Data> メンバ

        public int Compare(Data x, Data y) {
            if (x.ID < y.ID) return -1;
            if (x.ID > y.ID) return 1;

            return 0;
        }

        #endregion
    }
    class DateTimeComparer : IComparer<Data> {

        #region IComparer<Data> メンバ

        public int Compare(Data x, Data y) {
            if (x.CreationTime.Ticks < y.CreationTime.Ticks) return -1;
            if (x.CreationTime.Ticks > y.CreationTime.Ticks) return 1;

            return 0;
        }

        #endregion
    }
}
