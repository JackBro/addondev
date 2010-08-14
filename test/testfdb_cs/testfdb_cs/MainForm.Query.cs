using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;

namespace testfdb_cs
{
    partial class MainForm
    {
        private SQLiteWrap sqlitewrap = new SQLiteWrap();


        private bool hasFileData(string guid)
        {
            Int64 cnt =0;
            sqlitewrap.ExecuteQuery((cmd) =>
            {
                cmd.CommandText = String.Format("SELECT COUNT(*) FROM {0} WHERE guid = '{1}'", sqlitewrap.FileTable, guid);
                cnt = (Int64)cmd.ExecuteScalar();
            });
            return !(cnt == 0);
        }

        private bool hasTaggedFileData(string guid, string tag)
        {
            Int64 cnt =0;
            sqlitewrap.ExecuteQuery((cmd) =>
            {
                cmd.CommandText = String.Format("SELECT COUNT(*) FROM {1} WHERE guid = '{0}' AND tag = '{2}'", guid, sqlitewrap.TaggedFileTable, tag);
                cnt = (Int64)cmd.ExecuteScalar();
            });
            return !(cnt == 0);
        }

        private IEnumerable<string> insertTags(IEnumerable<string> tags)
        {
            IEnumerable<string> newtags = null;
            sqlitewrap.ExecuteQuery((cmd) =>
            {
                List<string> existtags = new List<string>();

                cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", sqlitewrap.TagTable);
                using (SQLiteDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        existtags.Add((string)reader[0]);
                    }
                }

                newtags = tags.Except(existtags);
                foreach (string tag in newtags)
                {
                    string strcmd = String.Format("INSERT INTO {0}(tag) VALUES('{1}')", sqlitewrap.TagTable, tag);
                    cmd.CommandText = strcmd;
                    cmd.ExecuteNonQuery();
                }

            });

            return newtags;
        }

        private IEnumerable<string> getAllTags()
        {
            List<string> tags = new List<string>();
            sqlitewrap.ExecuteQuery((cmd) =>
            {
                cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", sqlitewrap.TagTable);
                using (SQLiteDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        tags.Add((string)reader[0]);
                    }
                }
            });

            return tags;
        }

        private void insertFileData(List<FileData> filedatas, List<string> tags)
        {
            sqlitewrap.ExecuteQuery((cmd) =>
            {
                foreach (FileData file in filedatas)
                {
                    IEnumerable<string> newtags = file.tags.Union(tags).Except(file.tags.Intersect(tags));
                    file.tags = file.tags.Union(tags).ToList<string>();

                    if (hasFileData(file.guid))
                    {
                        //updateFile
                    }
                    else
                    {
                        cmd.CommandText = String.Format("INSERT INTO {0}(guid, name, tags, comment) VALUES('{1}', '{2}', '{3}', '{4}')",
                            sqlitewrap.FileTable, file.guid, file.name, file.getTagsConcat(), file.comment);
                        cmd.ExecuteNonQuery();
                    }
                    foreach (string tag in newtags)
                    {
                        if (!hasTaggedFileData(file.guid, tag))
                        {
                            cmd.CommandText = String.Format("INSERT INTO {0}(guid,tag) VALUES('{1}', '{2}')",
                                sqlitewrap.TaggedFileTable, file.guid, tag);
                            cmd.ExecuteNonQuery();
                        }
                    }
                }
            });
        }
    }
}