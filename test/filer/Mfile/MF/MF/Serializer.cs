using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Xml.Serialization;

namespace MF {
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
