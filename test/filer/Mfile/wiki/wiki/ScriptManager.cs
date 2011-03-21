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
        public bool IsCache { get; set; }
        private JintEngine en;
        private Dictionary<string, string> scripts = new Dictionary<string, string>();
        public void init(){
            en = new JintEngine();
            en.DisableSecurity();
            en.AllowClr = true;

            en.SetFunction("print", new Action<object>(Console.WriteLine));

            //en.SetFunction("cs_exe", new Action<string>(Exe));
            //en.SetFunction("cs_exe", new Action<string, string>(Exe));
            //en.SetFunction("cs_exeWait", new Action<string, string, string>(ExeWait));
            //en.SetFunction("cs_exe", new Action<string, string, string>(Exe));
            en.SetFunction("cs_exeWait", new Action<string, string, string, bool>(ExeWait));

        }

        public void Run(string filename) {
            Run(filename, null);
        }

        public void Run(string filename, string args) {
            var path = Path.Combine(ScriptDir, filename);
            if (!File.Exists(path)) return;

            var script = File.ReadAllText(path);
            if (args != null) {
                en.SetParameter("args", args);
            }
            //en.SetFunction("deleteitem", new Action<int>((id) => {
            //    Console.WriteLine("deleteitem id = " + id.ToString());
            //}));
            object result = en.Run(script);
        }

        //void Exe(string exe) {
        //    ExeWait(exe, null, null, "false");
        //}

        //void Exe(string exe, string args) {
        //    ExeWait(exe, args, null, "false");
        //}
        //void ExeWait(string exe, string args, string wait) {
        //    ExeWait(exe, args, null, wait);
        //}

        //void Exe(string exe, string args, string WorkingDirectory) {
        //    ExeWait(exe, args, WorkingDirectory, "false");
        //}

        //void Exe(string exe, string args, string WorkingDirectory, string wait) {
        //    Exe(exe, args, WorkingDirectory, bool.Parse(wait));
        //}

        void ExeWait(string exe, string args, string WorkingDirectory, bool wait) {

            ProcessStartInfo info = new ProcessStartInfo();
            info.FileName = exe;
            if (args !=null) info.Arguments = args;
            if (WorkingDirectory != null) info.WorkingDirectory = WorkingDirectory;
            
            //Process.Start(info);
            Process p = new Process();
            p.StartInfo = info;
            p.Start();
            if (wait) p.WaitForExit();
        }
    }
}
