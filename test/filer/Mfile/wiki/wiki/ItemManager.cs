using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace wiki {

    public class CallBackEventArgs : EventArgs {
        public Data Item;
        public int Index;
    }
    delegate void EventHandler(object sender, CallBackEventArgs e);

    class ItemManager {
        public event EventHandler eventHandler;

        private List<Data> datas;

        public void Load(){
        }
        public void Save(){
        }

        public void Insert() {
            
        }

        public void Delete() {
        }

        public void UpDate() {

        }

        public void Filter() {
            datas.FindAll(x => { return true; });
        }
    }
}
