using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using ProtoBuf;

namespace wiki {
    public class Serializer {
        public static void Serialize<T>(string path, T data){
            using (var file = File.Create(path)) {
                ProtoBuf.Serializer.Serialize(file, data);
            } 
        }

        public static T Deserialize<T>(string path) {
            T item;
            using (var file = File.OpenRead(path)) {
                item = ProtoBuf.Serializer.Deserialize<T>(file);
            }
            return item;
        }
    }
}
