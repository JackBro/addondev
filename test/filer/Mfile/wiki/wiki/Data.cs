using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProtoBuf;

namespace wiki {

    [ProtoContract]
    public class Data {
        [ProtoMember(1)]
        public long ID { get; set; }
        [ProtoMember(2)]
        public string Title { get; set; }
        [ProtoMember(3)]
        public string Text { get; set; }
        [ProtoMember(4)]
        public DateTime CreationTime { get; set; }
        //[ProtoMember(5)]
        //public DateTime LastWriteTime { get; set; }
    }
}
