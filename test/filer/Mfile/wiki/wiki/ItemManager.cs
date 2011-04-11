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
        public static string Ext = ".xml";

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
        private Dictionary<int, string> category = new Dictionary<int, string>();
        private Dictionary<int, ItemManager> manager = new Dictionary<int, ItemManager>();
        //private Dictionary<string, ItemManager> manager = new Dictionary<string, ItemManager>();
        public EventHandler eventHandler;
        public void Load(List<string> names) {
            var i=0;
            foreach (var name in names) {
                var m = new ItemManager();
                //m.DataPath = Path.Combine(DataDir, name);
                m.eventHandler += eventHandler;
                m.Name = name;
                m.Load(DataDir);
                category.Add(i, name);
                //manager.Add(name, m);
                manager.Add(i, m);
                i++;
            }
        }

        public int getCategoryID(string categoryname) {
            if (category.Values.Contains(categoryname)) {
                var cat = from p in category.Keys
                          where category[p] == categoryname
                          select p;
                if (cat.Count() > 0) {
                    return cat.ElementAt(0);
                }
            }
            return -1;
        }

        public void Save() {
            var lists = manager.ToList();
            foreach (var item in lists) {
                if (item.Value.IsDirty) {
                    item.Value.Save(DataDir);
                }
            }
        }
        public void Save(string name) {
            if (category.Values.Contains(name)) {
                var cat = from p in category.Keys
                          where category[p] == name
                          select p;

                if (cat.Count()>0 && manager.ContainsKey(cat.ElementAt(0))) {
                    manager[cat.ElementAt(0)].Save(DataDir);
                }
            }
            //if (manager.ContainsKey(name)) {
            //    manager[name].Save();
            //}
        }

        //public ItemManager create(string name) {
        //    var m = new ItemManager();
        //    manager.Add(name, m);
        //    return m;
        //}
        public bool RenameFile(string from, string to) {
            var topath = Path.Combine(this.DataDir, to + Ext);
            if (File.Exists(topath)) {
                return false;
            }
            var frompath = Path.Combine(this.DataDir, from + Ext);
            File.Move(frompath, topath);

            var id = getCategoryID(from);
            if (id >= 0) {
                category[id] = to;
                manager[id].Name = to;
                manager[id].IsDirty = true;
            }
            return true;
        }

        public void DeleteFile(string name) {
            var id = getCategoryID(name);
            if (id >= 0) {
                manager.Remove(id);
                var file = Path.Combine(this.DataDir, name);
                if (File.Exists(file)) {
                    Microsoft.VisualBasic.FileIO.FileSystem.DeleteFile(
                        file,
                        UIOption.OnlyErrorDialogs,
                        RecycleOption.SendToRecycleBin);
                }
            }
            //if (manager.ContainsKey(name)) {
            //    manager.Remove(name);
            //    var file = Path.Combine(this.DataDir, name);
            //    if (File.Exists(file)) {
            //        Microsoft.VisualBasic.FileIO.FileSystem.DeleteFile(
            //            file,
            //            UIOption.OnlyErrorDialogs,
            //            RecycleOption.SendToRecycleBin);
            //    }
            //}
        }

        public void DeleteItem(string name, long id) {
            var cid = getCategoryID(name);
            if (cid >= 0) {
                var m = manager[cid];
                if (name == Trust) {
                    m.Remove(id);
                }
                else {
                    var tid = getCategoryID(Trust);
                    if (tid >= 0) {
                        var d = m.GetItem(id);
                        m.Remove(id);
                        manager[tid].Insert(d);
                    }
                }
            }
            //if (manager.ContainsKey(name)) {
            //    var m = manager[name];
            //    if (name == Trust) {
            //        m.Remove(id);
            //    } else {
            //        if (manager.ContainsKey(Trust)) {
            //            var d = m.GetItem(id);
            //            m.Remove(id);
            //            manager[Trust].Insert(d);
            //        }
            //    }
            //}
        }

        public void ClearItem(string name) {
            var id = getCategoryID(name);
            if (id >= 0) {
                manager[id].Clear();
            }
            //if (manager.ContainsKey(name)) {
            //    manager[name].Clear();
            //}
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

        public Data getItem(long id){
            foreach (var m in manager.Values) {
                var item = m.GetItem(id);
                if (item != null) return item;
            }
            return null;
        }

        public List<Data> GetItem(List<long> ids) {
            var res = new List<Data>();
            foreach (var id in ids) {
                var data = this.getItem(id);
                if (data != null) {
                    res.Add(data);
                }
            }
            return res;
        }

        private int getNewCategoryID() {
            var i=0;
            while(category.ContainsKey(i)){
                i++;
            }
            return i;
        }

        public ItemManager getManger(string name) {
            var id = getCategoryID(name);
            if (id < 0) {
                id = getNewCategoryID();
                category.Add(id, name);
            }
            if(!manager.ContainsKey(id)){
                var m = new ItemManager();
                //m.DataPath = Path.Combine(DataDir, name); //name;
                m.eventHandler += eventHandler;
                m.Name = name;
                m.IsDirty = true;

                manager.Add(id, m);

            }
            return manager[id];

            //if (!manager.ContainsKey(name)) {
            //    var m = new ItemManager();
            //    m.DataPath = Path.Combine(DataDir, name); //name;
            //    m.eventHandler += eventHandler;
            //    m.Name = name;
            //    m.IsDirty = true;
            //    manager.Add(name, m);
            //}
            //return manager[name];
        }
    }

    public enum ChangeType {
        Insert,
        UpDate,
        //UpDateText,
        //UpDateCreationTime,
        //UpDateCategory,
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
        //public string DataPath { get; set; }
        public Data EditingData {
            get;
            set;
        }

        private List<Data> datas = new List<Data>();

        public void Load(string DataDir) {
            datas = XMLSerializer.Deserialize<List<Data>>(Path.Combine(DataDir, Name + Category.Ext), new List<Data>());
            //datas = Serializer.Deserialize<List<Data>>(DataPath, new List<Data>());
        }
        public void Save(string DataDir) {
            if (IsDirty) {
                XMLSerializer.Serialize<List<Data>>(Path.Combine(DataDir, Name + Category.Ext), datas);
                //Serializer.Serialize<List<Data>>(DataPath, datas);
            }
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

    class Items {
        public event EventHandler eventHandler;

        private Dictionary<int, string> category = new Dictionary<int, string>();
        private IDComparer idcomp = new IDComparer();
        List<Data> list = new List<Data>();

        public Items() {
            category.Add(0, "Trust");
        }

        public void AddCate() {

        }

        public void sort() {
            list.Sort(idcomp);
        }
        public long getNewID() {
            return list.Last().ID + 1;
        }

        public void Create(string text, DateTime creation, int category) {
            var item = new Data { ID = getNewID(), Text = text, CreationTime = creation };
            list.Add(item);
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.Insert });
            }
        }
        public Data Read(long id) {
            var data =
                from p in list
                where p.ID == id
                select p;
            if (data.Count() == 0) return null;

            return data.ElementAt(0);
        }
        public List<Data> Read(int category) {
            var datas =
                from p in list
                where p.Category == category
                select p;

            return datas.ToList();
        }
        public void UpDate(long id, string text) {
            var item = Read(id);
            if (item != null) {
                item.Text = text;
                if (eventHandler != null) {
                    eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.UpDateText });
                }
            }
        }
        public void UpDate(long id, DateTime creationtime) {
            var item = Read(id);
            if (item != null) {
                item.CreationTime = creationtime;
                if (eventHandler != null) {
                    eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.UpDateCreationTime });
                }
            }
        }
        public void UpDate(long id, int category) {
            var item = Read(id);
            if (item != null) {
                item.Category = category;
                if (eventHandler != null) {
                    eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.UpDateCategory });
                }
            }
        }
        public void Delete(long id) {
            var item = Read(id);
            if (item != null) {
                list.Remove(item);
                if (eventHandler != null) {
                    eventHandler(this, new CallBackEventArgs { Item = item, type = ChangeType.Delete });
                }
            }
        }

    }
}
