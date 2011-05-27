using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MF {
    class Config {
        public int div { get; set; }
        public List<string> ExtList { get; set; }

        public Config() {
        }

        public void setDefault(){
            div = 3;
            ExtList = new List<string>(){".exe",".lnk"};
        }
    }
}
