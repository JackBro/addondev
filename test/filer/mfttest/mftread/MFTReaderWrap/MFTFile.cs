using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MFTReaderWrap {
    public class MFTFile {
        public bool IsDirectory;
        public string Path;
        public string Name;
        public Int64 Size;
        public DateTime CreationTime;
        public DateTime LastWriteTime;
        public DateTime LastAccessTime;
    }
}
