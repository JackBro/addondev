using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Microsoft.VisualBasic;
using Microsoft.VisualBasic.FileIO;

namespace wiki {

    class Category {
        public static string categoryfile = "category.xml";
        public static string Trust = "Trust";
        public static string Ext = ".xml";

        public int EditingID { get; set; }

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
        public List<CategoryData> categorylist { get; set; }
        private List<Data> datalist = new List<Data>();
        private Dictionary<int, ItemManager> manager = new Dictionary<int, ItemManager>();
        public EventHandler eventHandler;

        public void Load() {
            categorylist = XMLSerializer.Deserialize<List<CategoryData>>(Path.Combine(DataDir, categoryfile), new List<CategoryData>() { new CategoryData() { ID = 0, Name = "Trust" } });
            foreach (var item in categorylist) {
                var name = item.Name;
                var m = new ItemManager();
                m.Datas = XMLSerializer.Deserialize<List<Data>>(Path.Combine(DataDir, name + Category.Ext), new List<Data>());
                m.CategoryID = item.ID;
                m.eventHandler += eventHandler;
                datalist.AddRange(m.Datas);
                manager.Add(item.ID, m);
            }
            datalist.Sort(new IDComparer());
        }

        public void Save() {
            XMLSerializer.Serialize<List<CategoryData>>(Path.Combine(DataDir, categoryfile), categorylist);

            var lists = manager.ToList();
            foreach (var item in lists) {
                if (item.Value.IsDirty) {
                    var name = getCategoryName(item.Key);
                    XMLSerializer.Serialize<List<Data>>(Path.Combine(DataDir, name + Category.Ext), item.Value.Datas);
                }
            }
        }

        void m_eventHandler(object sender, CallBackEventArgs e) {
            //throw new NotImplementedException();
            if (e.type == ChangeType.Create) {
            }
        }

        public int getCategoryID(string categoryname) {

            var cat = categorylist.Find(n => {
                return n.Name == categoryname;
            });
            if (cat == null) return -1;
            return cat.ID;
        }
        public string getCategoryName(int id) {
            var cat = categorylist.Find(n => {
                return n.ID == id;
            });
            return cat.Name;

        }

        public List<Data> Filter(string categoryname, Predicate<Data> pre) {
            var ds = manager[getCategoryID(categoryname)].Datas;
            return ds.FindAll(pre);
        }

        public void Create(string text, DateTime creatiomtime, string categoryname) {
            var item = new Data { ID=GetNewID(), Text = text, CreationTime= creatiomtime };
            datalist.Add(item);
            manager[getCategoryID(categoryname)].Insert(item);
        }

        public Data Read(int id) {
            return datalist.Find(n => {
                return n.ID == id;
            });
        }

        public void UpDateText(int id, string text) {
            var item = GetItem(id);
            item.Text = text;
            foreach (var mana in manager.Values) {
                if (mana.UpDate(item, ChangeType.UpDateText)) {
                    break;
                }
            }
        }

        public void UpDateCreationTime(int id, DateTime creationtime) {
            var item = GetItem(id);
            item.CreationTime = creationtime;
            foreach (var mana in manager.Values) {
                if (mana.UpDate(item, ChangeType.UpDateCreationTime)) {
                    break;
                }
            }
        }

        public void UpDateCategory(int id, int fromid, int toid) {
            var item = GetItem(id);
            foreach (var mana in manager.Values) {
                if (mana.UpDate(item, fromid, toid)) {
                    mana.Remove(item);
                    if (manager.ContainsKey(toid)) {
                        manager[toid].Insert(item);
                    }
                    break;
                }
            }
        }

        public void Delete(int id) {
            var item = GetItem(id);
            if (item != null) {
                foreach (var mana in manager.Values) {
                    if (mana.Remove(item)) {
                        if (mana.CategoryID == 0) {//Trust
                            datalist.Remove(item);
                        }
                        else {
                            manager[0].Insert(item);
                        }
                        break;
                    }
                }
            }
        }

        public void Clear(string categoryname) {
            var id = getCategoryID(categoryname);
            if (id >= 0) {
                manager[id].Clear();
            }
        }

        public int GetNewID() {
            //int s = 0;
            //foreach (var m in manager.Values) {
            //    s+=m.GetDataCount();
            //}
            //if (s == 0) return 0;

            //var id = manager.Values.Max(n => {
            //    return n.GetMaxID();
            //});
            //id++;
            //return id ;

            if (datalist.Count == 0) return 0;

            return datalist.Last().ID + 1;
        }

        public Data GetItem(int id) {
            //foreach (var m in manager.Values) {
            //    var item = m.GetItem(id);
            //    if (item != null) return item;
            //}
            //return null;
            return datalist.Find(n => {
                return n.ID == id;
            });
        }

        public List<Data> GetItem(List<int> ids) {
            //var res = new List<Data>();
            //foreach (var id in ids) {
            //    var data = this.getItem(id);
            //    if (data != null) {
            //        res.Add(data);
            //    }
            //}
            //return res;

            return datalist.FindAll(n => {
                return ids.Contains(n.ID);
            });
        }

        public bool CreateFile(string categoryname) {
            var id = getCategoryID(categoryname);
            if (id < 0) {
                var newid = 0;
                while (true) {
                    bool res = categorylist.Exists(n => {
                        return n.ID == newid;
                    });
                    if (!res) {
                        break;
                    }
                    newid++;
                }
                categorylist.Add(new CategoryData() { ID=newid, Name= categoryname });

                var m = new ItemManager();
                m.CategoryID = newid;
                m.eventHandler += eventHandler;
                manager.Add(newid, m);
            }
            
            return true;
        }
        public bool RenameFile(string from, string to) {
            var topath = Path.Combine(this.DataDir, to + Ext);
            if (File.Exists(topath)) {
                return false;
            }
            var frompath = Path.Combine(this.DataDir, from + Ext);
            File.Move(frompath, topath);

            var id = getCategoryID(from);
            if (id >= 0) {
                categorylist.Find(n => {
                    return n.ID == id;
                }).Name = to;
                manager[id].CategoryID = getCategoryID(to);
                manager[id].IsDirty = true;
            }
            return true;
        }

        public void DeleteFile(string categoryname) {
            var id = getCategoryID(categoryname);
            if (id >= 0) {
                manager.Remove(id);
                var file = Path.Combine(this.DataDir, categoryname);
                if (File.Exists(file)) {
                    Microsoft.VisualBasic.FileIO.FileSystem.DeleteFile(
                        file,
                        UIOption.OnlyErrorDialogs,
                        RecycleOption.SendToRecycleBin);
                }
            }
        }
    }

    public enum ChangeType {
        Create,
        UpDateText,
        UpDateCreationTime,
        UpDateCategory,
        Delete,
        Clear
    }
    public class CallBackEventArgs : EventArgs {
        public Data Item;
        public ChangeType type;
        public int FromCategoryID;
        public int ToCategoryID;
    }
    delegate void EventHandler(object sender, CallBackEventArgs e);

    class ItemManager {
        public event EventHandler eventHandler;
        public bool IsDirty = false;
        public int CategoryID;

        private List<Data> datas = new List<Data>();
        public List<Data> Datas { get { return datas; } set { datas = value; } }
        
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
                eventHandler(this, new CallBackEventArgs { FromCategoryID = this.CategoryID, Item = item, type = ChangeType.Create });
            }
        }

        public bool Remove(Data item) {
            if (item != null) {
                var index = datas.IndexOf(item);
                if (index >= 0) {
                    IsDirty = true;
                    datas.Remove(item);
                }
                if (index >= 0 && eventHandler != null) {             
                    
                    eventHandler(this, new CallBackEventArgs { FromCategoryID = this.CategoryID, Item = item, type = ChangeType.Delete });
                    return true;
                }
            }
            return false;
        }

        public void Clear() {
            IsDirty = true;
            datas.Clear();
            if (eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { FromCategoryID = this.CategoryID, type = ChangeType.Clear });
            }
        }

        public bool UpDate(Data item, ChangeType changetype) {    
            var index = datas.IndexOf(item);
            if (index >= 0) {
                IsDirty = true;
            }
            if (index >= 0 && eventHandler != null) {
                eventHandler(this, new CallBackEventArgs { FromCategoryID = this.CategoryID, Item = item, type = changetype });
                return true;
            }

            return false;
        }
        public bool UpDate(Data item, int fromid, int toid) {
            var index = datas.IndexOf(item);
            if (index >= 0) {
                IsDirty = true;
            }

            if (index >= 0) {
                return true;
            }

            return false;
        }

        public List<Data> Filter(Predicate<Data> pre) {
            return datas.FindAll(pre);
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
