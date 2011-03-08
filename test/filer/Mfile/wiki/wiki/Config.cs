using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;

namespace wiki {
    class Config {
        public int Show { get; set; }
        public int Port { get; set; }
        public FormWindowState WindowState { get; set; }
        public Size WindowSize { get; set; }
        public int ListViewW { get; set; }
        public int BrowserH { get; set; }

        public string htmlPath { get; set; }
        public string ScriptDirPath { get; set; }

        public List<string> ComeFormWords { get; set; }


        public Config() {
            ComeFormWords = new List<string>();
        }
    }
}
