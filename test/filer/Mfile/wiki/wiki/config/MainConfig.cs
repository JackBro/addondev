using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net.NetworkInformation;

namespace wiki.config {
    public partial class MainConfig : UserControl {
        public bool IsPortChanged { get; set; }
        private Config config;
        public MainConfig(Config config) {
            InitializeComponent();

            IsPortChanged = false;

            this.config = config;
            PortTextBox.Text = this.config.Port.ToString();
            //PortTextBox.KeyPress += (s, e) => {
            //    if (e.KeyChar < '0' || e.KeyChar > '9') {
            //        e.Handled = true;
            //    }
            //};
            PortSearchButton.Click += (s, e) => {
                var port = 0;
                try {
                    port = int.Parse(PortTextBox.Text);
                }
                catch (Exception) {
                    MessageBox.Show("ERROR");
                    return;
                    //throw;
                }
                
                var ret = true;
                while (ret) {
                    ret = PortCheck("localhost", port, 2);
                    if (port > 10000 || !ret) {
                        break;
                    }
                    port++;
                }
                string msg = "OK";
                if (!ret) {
                    PortTextBox.Text = port.ToString();
                    IsPortChanged = true;
                }
                else {
                    msg = "NG";
                }
                MessageBox.Show(msg);
            };
        }

        private bool PortCheck(string host, int port, int timeout) {

            System.Net.Sockets.TcpClient tcp = new System.Net.Sockets.TcpClient();
            try {
                IAsyncResult result = tcp.Client.BeginConnect(host, port, null, null);
                bool bret = result.AsyncWaitHandle.WaitOne(timeout * 1000, true);
                if (bret) {
                    tcp.Client.Shutdown(System.Net.Sockets.SocketShutdown.Both);
                    tcp.Client.Close();
                }
                return bret;
            }
            catch(Exception e) {
                return false;
            }
            finally {
                tcp.Close();
                GC.Collect();
                GC.WaitForPendingFinalizers();
            }
        }
    }
}
