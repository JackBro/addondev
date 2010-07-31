using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Data.SQLite;

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
            return String.Join(",", tags.ToArray<string>());
        }

        public static List<string> parseTags(string tags)
        {
            return tags.Split(new char[] { ',' }).ToList<string>();
        }
    }

    class TagDB
    {
        public delegate void tagsSelect(string guid, string name, string tags, string comment);
        public tagsSelect tagsSelectEvent = null;

        public string DBFileName{ get; set; }

        public string FileTable{ get; set; }

        public string TagedFileTable{ get; set; }

        public string TagTable { get; set; }

        private SQLiteConnection connection;
        private SQLiteCommand cmd;
        private SQLiteTransaction transaction;

        public TagDB()
        {
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
                    cmd.CommandText = String.Format("CREATE TABLE {0} (guid TEXT PRIMARY KEY, name TEXT, tags TEXT, comment TEXT)", FileTable);
                    cmd.ExecuteNonQuery();
                }
                if (!existTable(TagedFileTable))
                {
                    cmd.CommandText = String.Format("CREATE TABLE {0} (guid TEXT PRIMARY KEY, tag TEXT)", TagedFileTable);
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

        public void insertTag(string newtag)
        {
            beginTransaction();

            string strcmd = String.Format("INSERT INTO {0}(tag) VALUES('{1}')", TagTable, newtag);
            cmd.CommandText = strcmd;
            cmd.ExecuteNonQuery();

            commitTransaction();
        }

        public void insertFileData(List<FileData> filedatas, List<string> addtags)
        {
            beginTransaction();

            foreach(FileData file in filedatas)
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
                    if (!hasTagData(file.guid, tag))
                    {
                        string tagcmd = String.Format("INSERT INTO {0}(guid,tag) VALUES('{1}', '{2}')",
                            TagedFileTable, file.guid, tag);
                        cmd.CommandText = tagcmd;
                        cmd.ExecuteNonQuery();
                    }
                }
            }

            commitTransaction();
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

        //public void updateFile(string guid, string comment)
        //{
        //    string strcmd = String.Format("UPDATE {0} SET comment = '{1}' where guid = '{2}'", FileTableName, comment, guid);
        //    cmd.CommandText = strcmd;
        //    cmd.ExecuteNonQuery();
        //}

        public void updateFile(string guid, FileData filedata)
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

        public List<FileData> selectFileData(string[] tags)
        {
            List<FileData> filedatas = new List<FileData>();

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
                FileTable, "guid", TagedFileTable, "tag", seltags, tags.Length);

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
                        filedatas.Add(new FileData((string)reader[0], (string)reader[1], (string)reader[2], (string)reader[3]));
                    }
                }
            }

            commitTransaction();

            return filedatas;
        }

        public FileData selectFileData(string guid)
        {

            FileData res = null;

            beginTransaction();

            List<string> tags = new List<string>();
            cmd.CommandText = String.Format("SELECT * FROM {0} WHERE guid = '{1}'", TagedFileTable, guid);
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
                    res = new FileData((string)reader[0], (string)reader[1], tags, (string)reader[3]);
                }
            }

            commitTransaction();

            return res;
        }

        public IList<FileData> selectFileData(List<string> names, List<string> words)
        {
            IList<FileData> files = new List<FileData>();

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
                    files.Add(new FileData((string)reader[0], (string)reader[1], (string)reader[2], (string)reader[3]));
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

        public bool hasTagData(string guid, string tag)
        {
            cmd.CommandText = String.Format("SELECT COUNT(*) FROM {1} WHERE guid = '{0}' AND tag = '{2}'", guid, TagedFileTable, tag);
            Int64 cnt = (Int64)cmd.ExecuteScalar();
            return !(cnt == 0);
        }

    }
}
