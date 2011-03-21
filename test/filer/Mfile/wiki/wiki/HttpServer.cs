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
        private bool isbusy;

        private int port;
        public int Port {
            get { return this.port; }
            set {
                this.port = value;

            }
        }

        public HttpServer(int port) {
            this.Port = port;
        }

        public void stop() {
            if (listener != null && listener.IsListening) {
                //isbusy = false;
                //listener.Close();
                listener.Stop();
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
            isbusy = true;
            //listener.BeginGetContext(OnGetContext, listener);

            while (isbusy) {
                HttpListenerContext context = listener.GetContext();
                HttpListenerRequest req = context.Request;
                HttpListenerResponse res = context.Response;

                string resString = "accept";
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

            //listener.Close();
        }

        private void OnGetContext(IAsyncResult ar) {
            try {
                HttpListenerContext context = ((HttpListener)ar.AsyncState).EndGetContext(ar);
                OnRequest(context);
                listener.BeginGetContext(OnGetContext, listener);
            } catch (HttpListenerException e) {
                //Debug.WriteLine(e);
            }
        }
        private void OnRequest(HttpListenerContext context) {
            HttpListenerRequest req = context.Request;
            HttpListenerResponse res = context.Response;

            string resString = "accept";
            Console.WriteLine(req.RawUrl);
            if (RequestEvent != null) {
                reqArgs = new RequestEventArgs { Request = req };
                RequestEvent(this, reqArgs);
                resString = reqArgs.Response;
            }

            Encoding enc = Encoding.UTF8;
            byte[] buffer = enc.GetBytes(resString);
            res.OutputStream.Write(buffer, 0, buffer.Length);
            res.Close();
        }
    }
}
