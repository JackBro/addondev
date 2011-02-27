using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using Jint;
using System.IO;

namespace wiki {
    class ScriptManager {
        public string ScriptDir { get; set; }
        private JintEngine en;
        private Dictionary<string, string> scripts = new Dictionary<string, string>();
        public void init(){
            en = new JintEngine();
            en.DisableSecurity();
            en.AllowClr = true;

            en.SetFunction("print", new Action<object>(Console.WriteLine));

            
            en.SetFunction("cs_exe", new Action<string>(Exe));
            en.SetFunction("cs_exe", new Action<string, string>(Exe));
            en.SetFunction("cs_exe", new Action<string, string, string>(Exe));




        }

        public void Run(string filename, string args) {
            var path = Path.Combine(ScriptDir, filename);
            if (!File.Exists(path)) return;

            var script = File.ReadAllText(path);
            en.SetParameter("args", args);
            //en.SetFunction("deleteitem", new Action<int>((id) => {
            //    Console.WriteLine("deleteitem id = " + id.ToString());
            //}));
            object result = en.Run(script);
        }

        void Exe(string exe) {

            Exe(exe, null, null);
        }

        void Exe(string exe, string args) {

            Exe(exe, args, null);
        }
        void Exe(string exe, string args, string WorkingDirectory) {

            ProcessStartInfo info = new ProcessStartInfo();
            info.FileName = exe;
            if (args !=null) info.Arguments = args;
            if (WorkingDirectory != null) info.WorkingDirectory = WorkingDirectory;
            Process.Start(info);

        }
    }
}
