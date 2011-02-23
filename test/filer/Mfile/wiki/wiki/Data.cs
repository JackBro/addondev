using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProtoBuf;
using System.ComponentModel;

namespace wiki {

    [ProtoContract]
    public class Data {
        [ProtoMember(1)]
        public long ID { get; set; }

        [ProtoMember(2), DefaultValue("")]
        public string Title { get; set; }

        private string _text;
        [ProtoMember(3), DefaultValue("")]
        public string Text { 
            get{
                return _text;
            } 
            set{
                _text = value;
                var index = _text.IndexOf("\n");
                if (index > 0) {
                    Title = _text.Substring(0, index).Trim(new char[]{'\r', '\n'});
                }
                else {
                    Title = _text;
                }
            }
        }

        [ProtoMember(4)]
        public DateTime CreationTime { get; set; }
        //[ProtoMember(5)]
        //public DateTime LastWriteTime { get; set; }

        //public override string ToString() {
        //    var date = string.Format("{0} {1}", CreationTime.ToShortDateString(), CreationTime.ToLongTimeString());
        //    var text = this.Text.Replace("\r\n", "\n").Replace("\r", "\n").Replace("\n", "\\n");
        //    var json = string.Format("\"id\":\"{0}\", \"title\":\"{1}\", \"date\":\"{2}\", \"text\":\"{3}\"", this.ID, this.Title, date, text);
        //    json = "{" + json + "}";
        //    return json;
        //}

        public string ToJsonString() {
            var date = string.Format("{0} {1}", CreationTime.ToShortDateString(), CreationTime.ToLongTimeString());
            var text = this.Text.Replace("\r\n", "\n").Replace("\r", "\n").Replace("\n", "\\n");
            var json = string.Format("\"id\":\"{0}\", \"title\":\"{1}\", \"date\":\"{2}\", \"text\":\"{3}\"", this.ID, this.Title, date, text);
            json = "{" + json + "}";
            return json;
        }
    }
}
