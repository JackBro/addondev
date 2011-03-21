using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;

namespace wiki {

    public enum ShowType
    {
        Large,
        List
    }

    public class Config {
        public static string SettingPath = "setting.xml";
        public int ShowNum { get; set; }
        public ShowType ShowType { get; set; }
        public int Port { get; set; }
        public FormWindowState WindowState { get; set; }
        public Size WindowSize { get; set; }
        public int ListViewW { get; set; }
        public int BrowserH { get; set; }

        public string htmlPath { get; set; }
        public string ScriptDirPath { get; set; }

        public List<string> ComeFormWords { get; set; }

        public string NewTemplete { get; set; }

        public Config() {
            ComeFormWords = new List<string>();
            this.WindowState = FormWindowState.Normal;
            this.Port = 8088;
            this.ShowNum = 20;
            this.ShowType = ShowType.List;

        }
    }
}
