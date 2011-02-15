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
        public string Text { get; set; }
        [ProtoMember(3)]
        public DateTime CreationTime { get; set; }
        //[ProtoMember(5)]
        //public DateTime LastWriteTime { get; set; }
    }
}
