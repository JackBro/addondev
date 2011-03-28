using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    public partial class ConfigForm : Form {
        public Font EditorFont { get; private set; }
        private Dictionary<string, UserControl> configpanels = new Dictionary<string, UserControl>();

        public ConfigForm() {
            InitializeComponent();

            //var node = ConfigTreeView.Nodes["EditorNode"];
            //var p = new EditorPanel();
            //p.Dock= DockStyle.Fill;
            //ConfigPanel.Controls.Add(p);
            //configpanels.Add("EditorNode", p);
            //configpanels
            createPanel("EditorNode", new EditorPanel());

            ConfigTreeView.NodeMouseClick += (sender, e) => {
                if (configpanels.ContainsKey(e.Node.Name)) {
                    var cp = configpanels[e.Node.Name];
                    cp.BringToFront();
                }
            };
        }

        private void createPanel(string name, UserControl uc) {
            uc.Dock = DockStyle.Fill;
            ConfigPanel.Controls.Add(uc);
            configpanels.Add(name, uc);
        }
    }
}
