using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Microsoft.VisualBasic;
using Microsoft.VisualBasic.FileIO;

namespace wiki {

    class Category {
        public static string Trust = "Trust";

        private string datapath;
        public string DataDir { 
            get{
                return datapath;
            } 
            set{
                datapath = value;
                if (!Directory.Exists(datapath)) {
                    Directory.CreateDirectory(datapath);
                }
            } 
        }
        //private readonly string Trust = "Trust";
        private Dictionary<string, ItemManager> manager = new Dictionary<string, ItemManager>();
        public EventHandler eventHandler;
        public void Load(List<string> names) {
            foreach (var name in names) {
                var m = new ItemManager();
                m.DataPath = Path.Combine(DataDir, name);
                m.eventHandler += eventHandler;
                m.Name = name;
                m.Load();
                manager.Add(name, m);
            }
        }

        public void Save() {
            var lists = manager.ToList();
            foreach (var item in lists) {
                if (item.Value.IsDirty) {
                    item.Value.Save();
                }
            }
        }
        public void Save(string name) {
            if (manager.ContainsKey(name)) {
                manager[name].Save();
            }
        }

        //public ItemManager create(string name) {
        //    var m = new ItemManager();
        //    manager.Add(name, m);
        //    return m;
        //}

        public void DeleteFile(string name) {
            if (manager.ContainsKey(name)) {
                manager.Remove(name);
                var file = Path.Combine(this.DataDir, name);
                if (File.Exists(file)) {
                    Microsoft.VisualBasic.FileIO.FileSystem.DeleteFile(
                        file,
                        UIOption.OnlyErrorDialogs,
                        RecycleOption.SendToRecycleBin);
                }
            }
        }

        public void DeleteItem(string name, long id) {
            if (manager.ContainsKey(name)) {
                var m = manager[name];
                if (name == Trust) {
                    m.Remove(id);
                } else {
                    if (manager.ContainsKey(Trust)) {
                        var d = m.GetItem(id);
                        m.Remove(id);
                        manager[Trust].Insert(d);
                    }
                }
            }
        }

        public void ClearItem(string name) {
            if (manager.ContainsKey(name)) {
                manager[name].Clear();
            }
        }

        public long GetNewID() {
            int s = 0;
            foreach (var m in manager.Values) {
                s+=m.GetDataCount();
            }
            if (s == 0) return 0;

            var id = manager.Values.Max(n => {
                return n.GetMaxID();
            });
            id++;
            return id ;
        }

        public ItemManager getManger(string name) {
            if (!manager.ContainsKey(name)) {
                var m = new ItemManager();
                m.DataPath = Path.Combine(DataDir, name); //name;
                m.eventHandler += eventHandler;
                m.Name = name;
                m.IsDirty = true;
                manager.Add(name, m);
            }
            return manager[name];
        }
    }

    public enum ChangeType {
        Insert,
        UpDate,
        Delete,
        Clear
    }
    public class CallBackEventArgs : EventArgs {
        public string Name;
        public Data Item;
        public ChangeType type;
    }
    delegate void EventHandler(object sender, CallBackEventArgs e);

    class ItemManager {
        public event EventHandler eventHandler;
        public bool IsDirty = false;


        public string Name { get; set; }
        public string DataPath { get; set; }
        public Data EditingData {
            get;
            set;
        }

        private List<Data> datas = new List<Data>();

        public void Load(){
            datas = XMLSerializer.Deserialize<List<Data>>(DataPath + ".xml", new List<Data>());
            //datas = Serializer.Deserialize<List<Data>>(DataPath, new List<Data>());
        }
        public void Save(){
            XMLSerializer.Serialize<List<Data>>(DataPath + ".xml", datas);
            //Serializer.Serialize<List<Data>>(DataPath, datas);
            IsDirty = false;
        }
        
        public static int Insert(List<Data> list, Data item) {
            if (list.Count == 0) {
                //item.ID = 0;
                list.Add(item);
                return 0;
            }

            var cmp = new DateTimeComparer();

            int r = list.Count - 1;
            int l = 0;
            int comp;
            while (l < r) {
                int m = (r + l) >> 1;
                comp = cmp.Compare(list[m], item);
                if (comp < 0) r = m - 1;
                else if (comp > 0) l = m + 1;
                else return -1; // 重複不可
            }

            comp = cmp.Compare(list[l], item);

            var index = comp;

            if (comp < 0)
                index = l;
            else if (comp > 0)
                index = l + 1;

            list.Insert(index, item);

            return index;
        }

        public void Insert(Data item) {
            IsDirty = true;
            ItemManager.Insert(this.datas, item);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs {Name=this.Name, Item = item, type = ChangeType.Insert });
            }

        }

        public void Remove(long id) {
            IsDirty = true;
            var item = GetItem(id);
            if (item != null) {
                datas.Remove(item);
                if (eventHandler != null) {
                    eventHandler(this, new CallBackEventArgs { Name = this.Name, Item = item, type = ChangeType.Delete });
                }
            }
        }
        public void Clear() {
            IsDirty = true;
            datas.Clear();
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { Name = this.Name, type = ChangeType.Clear });
            }
        }

        public void UpDate(Data item) {
            IsDirty = true;
            var index = datas.IndexOf(item);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { Name = this.Name, Item = item, type = ChangeType.UpDate });
            }
        }

        public long GetNewID() {
            if(datas.Count == 0){
                return 0;
            }

            var maxid = datas.Max(n=>n.ID);
            return maxid + 1;
            
        }

        public int GetDataCount() {
            return datas.Count;
        }

        public long GetMaxID() {
            if (datas.Count == 0) return 0;

            return datas.Max(n => n.ID);
        }

        public List<Data> Filter(Predicate<Data> pre) {
            return datas.FindAll(pre);
        }

        public Data GetItem(long id){
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
