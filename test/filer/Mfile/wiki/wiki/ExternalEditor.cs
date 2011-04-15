using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Diagnostics;
using System.Text.RegularExpressions;
using System.Windows.Forms;

namespace wiki {

    public class UpDateEventArgs : EventArgs {
        public int ID;
        public string Text;
    }
    delegate void UpDateEventHandler(object sender, UpDateEventArgs e);

    class ExternalEditor {
        private Dictionary<string, DateTime> lastwrite = new Dictionary<string,DateTime>();
        private List<int> proessids;
        private FileSystemWatcher fw;
        private Regex regFilename = new Regex(@"^(\d+)\.txt", RegexOptions.Compiled);
        private string wpath;
        private System.Text.UTF8Encoding u = new UTF8Encoding(false);

        public event UpDateEventHandler UpDateEvent;

        public ExternalEditor(string wpath) {
            proessids = new List<int>();
            this.wpath = wpath;
        }

        public void Process(string exe, string args) {
            ProcessStartInfo info = new ProcessStartInfo();
            info.FileName = exe;
            if (args != null) info.Arguments = Path.Combine(wpath, args);
            //if (workingdir != null) info.WorkingDirectory = workingdir;

            //Process.Start(info);
            Process p = new Process();

            p.Exited += (s, e) => {
                proessids.Remove(p.Id);
                if (proessids.Count == 0) {
                    stopFw();
                }
            };
            p.StartInfo = info;
            p.Start();
            if (!proessids.Contains(p.Id)) {
                proessids.Add(p.Id);
                if (proessids.Count == 1) {

                    startFw();
                }
            }
        }

        public void write(long id, string text) {
            this.stopFw();
            var filepath = Path.Combine(this.wpath, id.ToString() + ".txt");
            //File.Create(filepath).Close();
            File.WriteAllText(filepath, text, u);
            
            //using (FileStream fs = File.Open(filepath, FileMode.Create, FileAccess.Write, FileShare.ReadWrite)) {
            //    using (TextWriter sr = new StreamWriter(fs, u)) {
            //        sr.Write(text);
            //    }
            //}
            this.Process(@"C:\Program Files\tpad\TeraPad.exe", filepath);
            //this.startFw();
            fw.EnableRaisingEvents = true;
        }

        private string ReadText(string filepath) {
            List<string> texts = new List<string>();
            //hStream = File::Open(sDir + "\\" + sNam, FileMode::Open, FileAccess::Read, FileShare::ReadWrite);
            //using (FileStream fs = File.Open(filepath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite)){
            using (FileStream fs = new FileStream(filepath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite)) {
                using (TextReader sr = new StreamReader(fs, u)) {
                    using (var ssr = TextReader.Synchronized(sr)) {

                        String line;
                        // Read and display lines from the file until the end of
                        // the file is reached.
                        while ((line = ssr.ReadLine()) != null) {
                            //Console.WriteLine(line);
                            texts.Add(line);
                        }
                    }
                    //sr.Close();
                }
                //fs.Close();
            }
            return string.Join("\n", texts.ToArray());
        }

        public void startFw() {
            if (fw == null) {
               
                fw = new FileSystemWatcher(this.wpath);
                //fw.WaitForChanged
                fw.NotifyFilter = NotifyFilters.LastWrite;
                fw.Changed += (s, e) => {
                    var m = regFilename.Match(e.Name);
                    if (m.Success) {
                        if (lastwrite.ContainsKey(e.FullPath)) {
                            if (DateTime.Now.Subtract(lastwrite[e.FullPath]).Seconds < 5) {
                                Console.WriteLine("retuern");
                                //lastwrite.Remove(e.FullPath);
                                return;
                            }
                            lastwrite[e.FullPath] = DateTime.Now;
                        }
                        else {
                            lastwrite.Add(e.FullPath, DateTime.Now);
                        }
                        Console.WriteLine("read");
                        if (UpDateEvent != null) {
                            try {
                                //fw.EnableRaisingEvents = false;
                                var text = ReadText(e.FullPath);// File.ReadAllText(e.FullPath, u);
                                var id = int.Parse(m.Groups[1].Value);
                                //fw.EnableRaisingEvents = true;
                                UpDateEvent(this, new UpDateEventArgs { ID = id, Text = text });
                                //UpDateEvent(this, new UpDateEventArgs { ID = id });
                            }
                            catch (Exception ex) {
                                MessageBox.Show(ex.Message);
                                //throw;
                            }
                        }

                    }
                    //if (e.LastWriteTime.Subtract(lastWriteTimeSav).Seconds < 10)
                    //    return;
                };
            }
            fw.EnableRaisingEvents = true;
        }
        public void stopFw() {
            if (fw != null) {
                fw.EnableRaisingEvents = false;
            }
        }
    }
}
