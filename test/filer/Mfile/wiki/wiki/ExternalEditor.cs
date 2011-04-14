using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Diagnostics;

namespace wiki {
    class ExternalEditor {
        private 

        public ExternalEditor() {
        
            FileSystemWatcher fn = new FileSystemWatcher();
            fn.NotifyFilter = NotifyFilters.LastWrite;
            fn.Changed += (s, e) => {

            };
        }

        public void Process() {
            ProcessStartInfo info = new ProcessStartInfo();
            info.FileName = exe;
            if (args != null) info.Arguments = args;
            if (workingdir != null) info.WorkingDirectory = workingdir;

            //Process.Start(info);
            Process p = new Process();
            p.StartInfo = info;
            p.Start();
            if (wait) p.WaitForExit();
        }
    }
}
