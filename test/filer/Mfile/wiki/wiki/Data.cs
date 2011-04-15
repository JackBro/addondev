using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel;
using System.Xml.Serialization;

namespace wiki {

    //[ProtoContract]
    //public class Data {
    //    [ProtoMember(1)]
    //    public long ID { get; set; }

    //    [ProtoMember(2), DefaultValue("")]
    //    public string Title { get; set; }

    //    private string _text;
    //    [ProtoMember(3), DefaultValue("")]
    //    public string Text { 
    //        get{
    //            return _text;
    //        } 
    //        set{
    //            _text = value;
    //            var index = _text.IndexOf("\n");
    //            if (index > 0) {
    //                Title = _text.Substring(0, index).Trim(new char[]{'\r', '\n'});
    //            }
    //            else {
    //                Title = _text;
    //            }
    //        }
    //    }

    //    [ProtoMember(4)]
    //    public DateTime CreationTime { get; set; }
    //    //[ProtoMember(5)]
    //    //public DateTime LastWriteTime { get; set; }
    //}

    public class CategoryData {
        public int ID { get; set; }
        public string Name { get; set; }
    }

    public class Data {
        public int ID { get; set; }

        //public int CategoryID { get; set; }

        [XmlIgnore]
        public string Title { get; set; }

        private string _text;
        public string Text {
            get {
                return _text;
            }
            set {
                _text = value;
                var index = _text.IndexOf("\n");
                if (index > 0) {
                    Title = _text.Substring(0, index).Trim(new char[] { '\r', '\n' });
                }
                else {
                    Title = _text;
                }
            }
        }

        public DateTime CreationTime { get; set; }
    }
}
