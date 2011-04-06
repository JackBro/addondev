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
        public MainConfig() {
            InitializeComponent();

            PingButton.Click += (s, e) => {
                PingButton.Text = PortCheck("localhost", int.Parse(textBox1.Text), 2).ToString();
            };
        }
        //接続ホスト /// 接続ポート /// タイムアウト ///
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
