using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Data.SQLite;

namespace testfdb_cs
{
    class TagDB
    {
        public delegate void tagsSelect(string guid, string name, string tags, string comment);
        public tagsSelect tagsSelectEvent = null;

        public string DBFileName{ get; set; }

        /// <summary>
        /// file data
        /// </summary>
        public string FileTable{ get; set; }
        
        /// <summary>
        /// file-tag
        /// </summary>
        public string TaggedFileTable{ get; set; }
        
        /// <summary>
        /// tag
        /// </summary>
        public string TagTable { get; set; }

        private SQLiteConnection connection;
        private SQLiteCommand cmd;
        private SQLiteTransaction transaction;

        public TagDB()
        {
            DBFileName = "file.db";
            FileTable = "filetable";
            TaggedFileTable = "taggedfiletable";
            TagTable = "tagtable";

            //System.IO.FileInfo fi = new System.IO.FileInfo(""); 
            //fi.CreationTime
        }

        public void Connection()
        {
            connection = new SQLiteConnection("Data Source=" + DBFileName);
            connection.Open();

            cmd = connection.CreateCommand();
        }

        public void Dispose()
        {
            cmd.Dispose();
            connection.Close();
        }

        private void beginTransaction()
        {
            transaction = this.connection.BeginTransaction();
        }

        private void commitTransaction()
        {
            transaction.Commit();
            transaction.Dispose();
            transaction = null;
        }

        public void createTable()
        {
            if (new FileInfo(DBFileName).Exists)
            {
                if (!existTable(FileTable))
                {
                    cmd.CommandText = String.Format("CREATE TABLE {0} (guid TEXT PRIMARY KEY, name TEXT, comment TEXT)", FileTable);
                    cmd.ExecuteNonQuery();
                }
                if (!existTable(TaggedFileTable))
                {
                    cmd.CommandText = String.Format("CREATE TABLE {0} (guid TEXT, tag TEXT)", TaggedFileTable);
                    cmd.ExecuteNonQuery();
                }

                if (!existTable(TagTable))
                {
                    cmd.CommandText = String.Format("CREATE TABLE {0} (tag TEXT PRIMARY KEY)", TagTable);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        private bool existTable(string tablename)
        {
            cmd.CommandText = String.Format("select count(*) from sqlite_master where type='table' and name='{0}'", tablename);
            Int64 cnt = (Int64)cmd.ExecuteScalar();
            return !(cnt == 0);
        }

        public string[] getAllTags()
        {

            beginTransaction();

            //List<string> tags = new List<string>();
            //cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", TagedFileTable);
            //using (SQLiteDataReader reader = cmd.ExecuteReader())
            //{
            //    while (reader.Read())
            //    {
            //        tags.Add((string)reader[0]);
            //    }
            //}

            List<string> tags = new List<string>();
            cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", TagTable);
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                while (reader.Read())
                {
                    tags.Add((string)reader[0]);
                }
            }

            commitTransaction();

            tags.Sort();

            return tags.ToArray<string>();
        }

        public void insertTag(string[] newtags)
        {
            beginTransaction();

            List<string> tags = new List<string>();
            cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", TagTable);
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                while (reader.Read())
                {
                    tags.Add((string)reader[0]);
                }
            }

            IEnumerable<string> addtags = newtags.Except(tags);

            foreach (string tag in addtags)
            {
                string strcmd = String.Format("INSERT INTO {0}(tag) VALUES('{1}')", TagTable, tag);
                cmd.CommandText = strcmd;
                cmd.ExecuteNonQuery();
            }
            commitTransaction();
        }

        private IEnumerable<string> unique(IEnumerable<string> tags)
        {
            foreach (string tag in tags)
            {
                yield return tag;
            }
        }

        public void insertFileData(List<TableData> filedatas, List<string> addtags)
        {
            beginTransaction();

            foreach(TableData file in filedatas)
            {
                IEnumerable<string> newtags = file.tags.Union(addtags).Except(file.tags.Intersect(addtags));
                file.tags = file.tags.Union(addtags).ToList<string>();

                if (hasFileData(file.guid))
                {
                    //updateFile
                }
                else
                {
                    string strcmd = String.Format("INSERT INTO {0}(guid, name, tags, comment) VALUES('{1}', '{2}', '{3}', '{4}')",
                        FileTable, file.guid, file.name, file.getTagsConcat(), file.comment);
                    cmd.CommandText = strcmd;
                    cmd.ExecuteNonQuery();
                }
                foreach (string tag in newtags)
                {
                    if (!hasTaggedFileData(file.guid, tag))
                    {
                        string tagcmd = String.Format("INSERT INTO {0}(guid,tag) VALUES('{1}', '{2}')",
                            TaggedFileTable, file.guid, tag);
                        cmd.CommandText = tagcmd;
                        cmd.ExecuteNonQuery();
                    }
                }
            }

            commitTransaction();
        }

        public void updateFileDataName(string guid, string name)
        {
            string strcmd = String.Format("UPDATE {0} SET name = '{1}' where guid = '{2}'", FileTable, name, guid);
            cmd.CommandText = strcmd;
            cmd.ExecuteNonQuery();
        }
        public void updateFileDataComment(string guid, string comment)
        {
            string strcmd = String.Format("UPDATE {0} SET comment = '{1}' where guid = '{2}'", FileTable, comment, guid);
            cmd.CommandText = strcmd;
            cmd.ExecuteNonQuery();
        }
        //public void updateFile(string guid, string name)
        //{
        //    string strcmd = String.Format("UPDATE {0} SET name = '{1}' where guid = '{2}'", FileTableName, name, guid);
        //    cmd.CommandText = strcmd;
        //    cmd.ExecuteNonQuery();
        //}

        //public void updateFile(string guid, List<string> tags)
        //{
        //}



        public void updateFile(string guid, TableData filedata)
        {
            //string strcmd = String.Format("UPDATE {0} SET comment = '{1}' where guid = '{2}'", FileTableName, comment, guid);
            //cmd.CommandText = strcmd;
            //cmd.ExecuteNonQuery();
        }

        //public void insertTags(string[] guids, string[] tags)
        //{
        //    beginTransaction();
        //    foreach (string guid in guids)
        //    {
        //        foreach(string tag in tags)
        //        {
        //            string strcmd = String.Format("INSERT INTO {0}(guid,tag) VALUES('{1}', '{2}')",
        //                TagTableName, guid, tag);
        //            cmd.CommandText = strcmd;
        //            cmd.ExecuteNonQuery();
        //        }
        //    }
        //    commitTransaction();
        //}

        public List<TableData> selectFileData(string[] tags)
        {
            List<TableData> filedatas = new List<TableData>();

            Func<string[], string[]> func = x =>
            {
                List<string> ret = new List<string>();
                foreach(string tag in x)
                {
                    ret.Add(String.Format("'{0}'", tag));
                }
                return ret.ToArray<string>();
            };

            string seltags = String.Join(",", func(tags));

                                      //SELECT * FROM file WHERE id IN (SELECT id FROM tag WHERE tag IN ('text', 'src') GROUP BY id HAVING COUNT(*) = 2)
            string sql = String.Format("SELECT * FROM {0} WHERE {1} IN (SELECT {1} FROM {2} WHERE {3} IN ({4}) GROUP BY {1} HAVING COUNT(*) = {5})",
                FileTable, "guid", TaggedFileTable, "tag", seltags, tags.Length);

            beginTransaction();

            cmd.CommandText = sql;
            //command.ExecuteNonQuery();
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                while (reader.Read())
                {
                    if (reader.FieldCount == 4)
                    {
                        
                        //tagsSelectEvent((string)reader[0], (string)reader[1], (string)reader[2], (string)reader[3]);
                        filedatas.Add(new TableData((string)reader[0], (string)reader[1], (string)reader[2], (string)reader[3]));
                    }
                }
            }

            commitTransaction();

            return filedatas;
        }

        public TableData selectFileData(string guid)
        {

            TableData res = null;

            beginTransaction();

            List<string> tags = new List<string>();
            cmd.CommandText = String.Format("SELECT * FROM {0} WHERE guid = '{1}'", TaggedFileTable, guid);
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                while (reader.Read())
                {
                    tags.Add((string)reader[0]);
                }
            }

            cmd.CommandText = String.Format("SELECT * FROM {0} WHERE guid = '{1}'", FileTable, guid);
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                if (reader.Read())
                {
                    res = new TableData((string)reader[0], (string)reader[1], tags, (string)reader[3]);
                }
            }

            commitTransaction();

            return res;
        }

        public IList<TableData> selectFileData(List<string> names, List<string> words)
        {
            IList<TableData> files = new List<TableData>();

            List<string> selectconds = new List<string>();
            foreach(string name in names)
            {
                selectconds.Add(String.Format("{0} like '%{1}%'", "name", name));
            }
            foreach (string word in words)
            {
                selectconds.Add(String.Format("{0} like '%{1}%'", "comment", word));
            }

            string cond = String.Join(" AND ", selectconds.ToArray<string>());


            beginTransaction();

            cmd.CommandText = String.Format("SELECT * FROM {0} WHERE {1}", FileTable, cond);
            using (SQLiteDataReader reader = cmd.ExecuteReader())
            {
                while (reader.Read())
                {
                    files.Add(new TableData((string)reader[0], (string)reader[1], (string)reader[2], (string)reader[3]));
                }
            }

            commitTransaction();

            return files;
        }

        public bool hasFileData(string guid)
        {
            cmd.CommandText = String.Format("SELECT COUNT(*) FROM {0} WHERE guid = '{1}'", FileTable, guid);
            Int64 cnt = (Int64)cmd.ExecuteScalar();
            return !(cnt==0);
        }

        public bool hasTaggedFileData(string guid, string tag)
        {
            cmd.CommandText = String.Format("SELECT COUNT(*) FROM {1} WHERE guid = '{0}' AND tag = '{2}'", guid, TaggedFileTable, tag);
            Int64 cnt = (Int64)cmd.ExecuteScalar();
            return !(cnt == 0);
        }

        public bool hasTagData(string tag)
        {
            cmd.CommandText = String.Format("SELECT COUNT(*) FROM {0} WHERE tag = '{1}'", tag);
            Int64 cnt = (Int64)cmd.ExecuteScalar();
            return !(cnt == 0);
        }

    }
}
