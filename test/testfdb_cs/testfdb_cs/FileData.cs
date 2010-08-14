using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace testfdb_cs
{
    public class FileData
    {
        public string guid;
        public string name;
        public List<string> tags;
        public string comment;

        public FileData(string guid, string name, List<string> tags, string comment)
        {
            this.guid = guid;
            this.name = name;
            this.tags = tags;
            this.comment = comment;
        }

        public FileData(string guid, string name, string tags, string comment)
        {
            this.guid = guid;
            this.name = name;
            this.tags = parseTags(tags);
            this.comment = comment;
        }

        public string getTagsConcat()
        {
            return String.Join(" ", tags.ToArray<string>());
        }

        public static List<string> parseTags(string tags)
        {
            return tags.Split(new char[] { ' ' }).ToList<string>();
        }
    }
}
