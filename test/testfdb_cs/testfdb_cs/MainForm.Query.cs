using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.IO;

namespace testfdb_cs
{
    partial class MainForm
    {
        private void init() {

        }

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
            //IEnumerable<string> newtags = null;
            //sqlitewrap.ExecuteQuery((cmd) =>
            //{
            //    List<string> existtags = new List<string>();

            //    cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", sqlitewrap.TagTable);
            //    using (SQLiteDataReader reader = cmd.ExecuteReader())
            //    {
            //        while (reader.Read())
            //        {
            //            existtags.Add((string)reader[0]);
            //        }
            //    }

            //    newtags = tags.Except(existtags);
            //    foreach (string tag in newtags)
            //    {
            //        string strcmd = String.Format("INSERT INTO {0}(tag) VALUES('{1}')", sqlitewrap.TagTable, tag);
            //        cmd.CommandText = strcmd;
            //        cmd.ExecuteNonQuery();
            //    }

            //});

            //return newtags;

            IEnumerable<string> newtags = null;
            using (FileDataModelContainer db = new FileDataModelContainer()) {

                var query = from c in db.TagTable
                            //where !tags.Contains(c.tag)
                            select c.tag;
                newtags = tags.Except(query).ToList();
                foreach(string t in newtags){
                    TagTable tabtable = new TagTable();
                    tabtable.tag = t;
                    db.AddToTagTable(tabtable);
               }
                db.SaveChanges();
            }
            return newtags;
        }

        private IEnumerable<string> getAllTags()
        {
            //List<string> tags = new List<string>();
            //sqlitewrap.ExecuteQuery((cmd) =>
            //{
            //    cmd.CommandText = String.Format("SELECT DISTINCT tag FROM {0}", sqlitewrap.TagTable);
            //    using (SQLiteDataReader reader = cmd.ExecuteReader())
            //    {
            //        while (reader.Read())
            //        {
            //            tags.Add((string)reader[0]);
            //        }
            //    }
            //});

            //return tags;

            List<string> tags;// = new List<string>();
            using (FileDataModelContainer db = new FileDataModelContainer()) {
                var query = from c in db.TagTable
                            select c.tag;
                //tags = query.ToList<string>();
                tags = query.Distinct().ToList<string>();
                //foreach (TagTable tag in query) {
                //    Console.WriteLine("query = " + tag.tag);
                //}
            }

            return tags;
        }

        private void insertFileData(IEnumerable<string> fullpaths, List<string> tags) {
            using (FileDataModelContainer db = new FileDataModelContainer()) {

                foreach (string fullpath in fullpaths) {
                    string strguid = Win32.getObjectID(fullpath).ToString();
                    var query = from c in db.FileTable
                                where c.guid == strguid
                                select c;

                    if(query.Count() == 0){
                        FileInfo fileinfo = new FileInfo(fullpath);
                        //FileTable filetable = new FileTable(){
                        //    guid=strguid, 
                        //    name=fileinfo.Name, 
                        //    ext=fileinfo.Extension,
                        //    comment="", 
                        //    createtime=fileinfo.CreationTime
                        //};
                        
                        FileTable filetable = new FileTable();
                            filetable.guid = strguid;
                            filetable.name = fileinfo.Name;
                            filetable.ext = fileinfo.Extension;
                            filetable.comment = "";
                            filetable.createtime = fileinfo.CreationTime;
                        
                        db.AddToFileTable(filetable);
                        db.SaveChanges();

                        foreach (string tag in tags) {
                            filetable.TagTable.Add(new TagTable() { tag = tag, FileTable = filetable });
                        }
                        //var fquery = from c in db.FileTable
                        //            where c.guid == strguid
                        //            select c;
                        
                        //foreach (FileTable f in fquery) {
                        //db.AttachTo(filetable.EntityKey.EntitySetName, 
                       //     new FileTable() { filetableid = filetable.filetableid });
                       //     foreach (string tag in tags) {
                        //        db.AddToTagTable(new TagTable() { tag = tag, FileTable = filetable });
                        //    }

                        //}
                        //TagTable tagtable = new TagTable() { id = filetable.TagTable.id };
                        //db.AttachTo("FileTable", new FileTable() { filetableid = filetable.filetableid });
                        //foreach (string tag in tags) {
                        //    db.AddToTagTable(new TagTable() { tag = tag, FileTable = filetable });
                        //}

                    }else{
                        foreach (FileTable filetable in query) {

                            //var queryt = from c in db.TagTable
                            //             where c.FileTable.filetableid == filetable.filetableid
                            //             select c.tag;
                            foreach (string tag in tags) {
                                //Console.WriteLine("tag = " + tag);
                                filetable.TagTable.Add(new TagTable() { tag = tag, FileTable = filetable });
                            }
                            //db.AttachTo("FileTable", new FileTable() { filetableid = filetable.filetableid });
                            //foreach (string tag in tags) {
                            //    //db.AddToTagTable(new TagTable() { tag = tag, FileTable = filetable });
                            //    filetable.TagTable.Add(new TagTable() { tag = tag, FileTable = filetable });
                            //}
                        }
                    }
                }

                db.SaveChanges();

                //db.AttachTo("FileTable", new FileTable() { filetableid = filetable.filetableid });
                //foreach (string tag in tags) {
                //    db.AddToTagTable(new TagTable() { tag = tag, FileTable = filetable });
                //}
            }
        }

        //private void insertFileData(List<TableData> filedatas, List<string> tags)
        //{
        //    sqlitewrap.ExecuteQuery((cmd) =>
        //    {
        //        foreach (TableData file in filedatas)
        //        {
        //            IEnumerable<string> newtags = file.tags.Union(tags).Except(file.tags.Intersect(tags));
        //            file.tags = file.tags.Union(tags).ToList<string>();

        //            if (hasFileData(file.guid))
        //            {
        //                //updateFile
        //            }
        //            else
        //            {
        //                cmd.CommandText = String.Format("INSERT INTO {0}(guid, name, tags, comment) VALUES('{1}', '{2}', '{3}', '{4}')",
        //                    sqlitewrap.FileTable, file.guid, file.name, file.getTagsConcat(), file.comment);
        //                cmd.ExecuteNonQuery();
        //            }
        //            foreach (string tag in newtags)
        //            {
        //                if (!hasTaggedFileData(file.guid, tag))
        //                {
        //                    cmd.CommandText = String.Format("INSERT INTO {0}(guid,tag) VALUES('{1}', '{2}')",
        //                        sqlitewrap.TaggedFileTable, file.guid, tag);
        //                    cmd.ExecuteNonQuery();
        //                }
        //            }
        //        }
        //    });
        //}
    }
}