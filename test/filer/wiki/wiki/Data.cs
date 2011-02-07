using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace wiki {
    class Data {
        public long ID { get; set; }
        public string Title { get; set; }
        public string Text { get; set; }
        public DateTime CreationTime { get; set; }
        public DateTime LastWriteTime { get; set; }
    }
}
