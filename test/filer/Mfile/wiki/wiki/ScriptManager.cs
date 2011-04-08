using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using Jint;
using System.IO;
using System.Windows.Forms;

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
            en.SetFunction("alert", new Action<object>(n=>{
                MessageBox.Show(n.ToString());
            }));

            //en.SetFunction("cs_exe", new Action<string>(Exe));
            //en.SetFunction("cs_exe", new Action<string, string>(Exe));
            //en.SetFunction("cs_exeWait", new Action<string, string, string>(ExeWait));
            //en.SetFunction("cs_exe", new Action<string, string, string>(Exe));
            en.SetFunction("cs_exeWait", new Action<string, string, bool, string>(ExeWait));

            en.SetFunction("cs_process", new Action<string, string, bool, string>(ExeWait));

        }

        public void Run(string filename) {
            Run(filename, null);
        }

        public void Eval(string script) {
            object result = en.Run(script);
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

        void ExeWait(string exe, string args, bool wait, string workingdir) {

            ProcessStartInfo info = new ProcessStartInfo();
            info.FileName = exe;
            if (args !=null) info.Arguments = args;
            if (workingdir != null) info.WorkingDirectory = workingdir;

            //Process.Start(info);
            Process p = new Process();
            p.StartInfo = info;
            p.Start();
            if (wait) p.WaitForExit();
        }
    }
}
