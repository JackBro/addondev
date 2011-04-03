using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using ProtoBuf;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System.Xml.Serialization;

namespace wiki {
    public class Serializer {
        public static void Serialize<T>(string path, T data){
            using (var file = File.Create(path)) {
                ProtoBuf.Serializer.Serialize(file, data);
            } 
        }

        public static T Deserialize<T>(string path, T defaultValue) {
            
            if (!File.Exists(path)) {
                return defaultValue;
            }
            FileInfo finfo = new FileInfo(path);
            if (finfo.Length == 0) {
                return defaultValue;
            }

            T item;
            using (var file = File.OpenRead(path)) {
                item = ProtoBuf.Serializer.Deserialize<T>(file);
            }
            return item;
        }
    }

    public class JsonSerializer {
        public static string Serialize(Object obj){
            return JsonConvert.SerializeObject(obj, new JavaScriptDateTimeConverter());
        }

        public static T Deserialize<T>(string json) {
            return JsonConvert.DeserializeObject<T>(json);
        }
    }

    public class XMLSerializer {
        public static void Serialize<T>(string path, Object obj) {
            System.Xml.Serialization.XmlSerializer seri = new XmlSerializer(typeof(T));
            FileStream fs=null;
            try {
                fs = new FileStream(path, FileMode.Create);
                seri.Serialize(fs, obj);
            } catch (Exception) {

                throw;
            } finally {
                if (fs != null) fs.Close();
            }
        }
        public static T Deserialize<T>(string path, T defaultValue) {
            XmlSerializer seri = new XmlSerializer(typeof(T));
            if (File.Exists(path)) {
                FileStream fs=null;
                try {
                    fs = new FileStream(path, FileMode.Open);
                    var xml = (T)seri.Deserialize(fs);
                    return xml;
                } catch (Exception) {

                    throw;
                } finally {
                    if(fs!=null) fs.Close();
                }
            } else {
                return defaultValue;
            }
        }
    }

    
}
