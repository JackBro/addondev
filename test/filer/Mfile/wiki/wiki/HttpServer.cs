using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;

namespace wiki {

    class RequestEventArgs : EventArgs {  
        public HttpListenerRequest Request;
        public string Response;
    }
    delegate void RequestEventHandler(object sender, RequestEventArgs e);

    class HttpServer {
        public event RequestEventHandler RequestEvent;
        private RequestEventArgs reqArgs;

        public event EventHandler<EventArgs> StopEvent;

        private HttpListener listener;

        private int port;
        public int Port {
            get { return this.port; }
            set {
                this.port = value;

            }
        }

        public HttpServer() {
            Port = 8088;
        }

        public void stop() {
            if (listener != null && listener.IsListening) {
                listener.Close();
                if (StopEvent != null) {
                    StopEvent(this, new EventArgs());
                }
            }
        }

        public void start(){
            if (listener != null) {
                listener.Stop();
                listener.Prefixes.Clear();
            }
            else {
                listener = new HttpListener();
            }
            //string prefix = "http://localhost:8088/";
            string prefix = "http://localhost:" + this.port.ToString() +"/";
            //listener = new HttpListener();
            listener.Prefixes.Add(prefix); // プレフィックスの登録

            listener.Start();

            while (true) {
                HttpListenerContext context = listener.GetContext();
                HttpListenerRequest req = context.Request;
                HttpListenerResponse res = context.Response;

                string resString = "tttt";
                Console.WriteLine(req.RawUrl);
                if (RequestEvent != null) {
                    reqArgs = new RequestEventArgs { Request = req };
                    RequestEvent(this, reqArgs);
                    resString = reqArgs.Response;
                }
                
                //if(resString)

                Encoding enc = Encoding.UTF8;
                byte[] buffer = enc.GetBytes(resString);
                res.OutputStream.Write(buffer, 0, buffer.Length);
                res.Close();
            }        
        }
    }
}
