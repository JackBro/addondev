using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace mftread {
    class Program {
        static void Main(string[] args) {
            MFTReader r = new MFTReader();
            r.read(new DriveInfo("c"));
        }
    }
}
