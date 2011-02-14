using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MF {
    class FileItem {
        public string Name { get; set; }
        public bool IsFile { get; set; }
        public string type { get; set; }
        public long Size { get; set; }
        public DateTime LastWriteTime { get; set; }
        public int NameWidth { get; set; }
    }
}
