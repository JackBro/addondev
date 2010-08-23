using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace testfdb_cs
{
    public class TableData
    {
        public string guid;
        public string name;
        public long size;
        public string ext;
        public List<string> tags;
        public string comment;
        public DateTime creationtime;
        public DateTime lastwritetime;

        public TableData(string guid, string name, long size, string ext, List<string> tags, string comment, DateTime creationtime, DateTime lastwritetime)
        {
            this.guid = guid;
            this.name = name;
            this.size = size;
            this.ext = ext;
            this.tags = tags;
            this.comment = comment;
            this.creationtime = creationtime;
            this.lastwritetime = lastwritetime;
        }

        //public TableData(string guid, string name, string tags, string comment)
        //{
        //    this.guid = guid;
        //    this.name = name;
        //    this.tags = parseTags(tags);
        //    this.comment = comment;
        //}

        public string TagsToString()
        {
            return String.Join(" ", tags.ToArray<string>());
        }

        //public static List<string> parseTags(string tags)
        //{
        //    return tags.Split(new char[] { ' ' }).ToList<string>();
        //}
    }
}
