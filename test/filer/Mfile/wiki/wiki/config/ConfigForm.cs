using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using wiki.config;

namespace wiki {
    public partial class ConfigForm : Form {
        public Font EditorFont { get; private set; }

        private Dictionary<string, UserControl> configpanels = new Dictionary<string, UserControl>();
        private Config config;
        internal EditorConfig editorconfig;
        internal MainConfig mainconfig;

        public ConfigForm(Config config) {

            InitializeComponent();

            this.config = config;

            mainconfig = new MainConfig();
            createPanel("MainNode", mainconfig);
            
            editorconfig = new EditorConfig(config);
            createPanel("EditorNode", editorconfig);

            ConfigTreeView.NodeMouseClick += (sender, e) => {
                if (configpanels.ContainsKey(e.Node.Name)) {
                    var cp = configpanels[e.Node.Name];
                    cp.BringToFront();
                }
            };

            this.OKButton.Click += (s, e) => {
                editorconfig.Accept();
                
                DialogResult = DialogResult.OK;
                this.Close();

            };
            this.CancelButton.Click += (s, e) => {
                DialogResult = DialogResult.Cancel;
                this.Close();
            };
        }

        private void createPanel(string name, UserControl uc) {
            uc.Dock = DockStyle.Fill;
            ConfigPanel.Controls.Add(uc);
            configpanels.Add(name, uc);
        }
    }
}
