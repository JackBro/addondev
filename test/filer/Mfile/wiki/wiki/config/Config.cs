using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using System.Xml.Serialization;
using System.ComponentModel;

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

        public string ItemTemplete { get; set; }

        public bool IsUseMigemo { get; set; }
        public string MigemoDictPath { get; set; }

        //editor
        [XmlIgnore]
        public Color EditorFontColor {
            get { return ColorTranslator.FromHtml(EditorFontColorText); }
            set{
                EditorFontColorText = ColorTranslator.ToHtml(value);
            } 
        }
        [XmlIgnore]
        public Color EditorBackColor {
            get { return ColorTranslator.FromHtml(EditorBackColorText); }
            set {
                EditorBackColorText = ColorTranslator.ToHtml(value);
            }
        }

        public string EditorFontColorText { get; set; }
        public string EditorBackColorText { get; set; }


        private Font _editorFont;
        [XmlIgnore]
        public Font EditorFont {
            get { return _editorFont; }
            set { _editorFont = value; }
        }

        public string EditorFontText {
            get {
                return TypeDescriptor.GetConverter(typeof(Font)).ConvertToString(EditorFont);
            }
            set {
                EditorFont = (Font)TypeDescriptor.GetConverter(typeof(Font)).ConvertFromString(value);
            }
        }


        public bool ShowTab { get; set; }
        public bool ShowEol { get; set; }
        public bool ShowSpace { get; set; }
        public bool ShowZenSpace { get; set; }

        public bool EdiorWrap { get; set; }

        [XmlIgnore]
        public List<string> CompleList;


        public Config() {
            ComeFormWords = new List<string>();
            this.WindowState = FormWindowState.Normal;
            this.Port = 8088;
            this.ShowNum = 20;
            this.ShowType = ShowType.List;

            ShowTab = false;
            ShowEol = false;
            ShowSpace = false;
            ShowZenSpace = false;

            EdiorWrap = false;

            EditorFontColorText = ColorTranslator.ToHtml(SystemColors.WindowText);
            EditorBackColorText = ColorTranslator.ToHtml(SystemColors.Window);

            _editorFont = System.Windows.Forms.SystemInformation.MenuFont;

            CompleList = new List<string>();
        }
    }
}
