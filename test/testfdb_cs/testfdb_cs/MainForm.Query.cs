using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.Linq;
using System.IO;
using System.Data.Common;

namespace testfdb_cs
{
    partial class MainForm
    {

        private string DBFileName = "file.db";

        /// <summary>
        /// file data
        /// </summary>
        public string FileTableName = "FileTable";

        /// <summary>
        /// tag
        /// </summary>
        public string TagTableName = "TagTable";

        private SQLiteWrap sqlitewrap = new SQLiteWrap();

        private void createTable() {
            if (new FileInfo(DBFileName).Exists) return;

            DbConnection cnn;
            using (cnn = new SQLiteConnection()) {
                cnn.ConnectionString = "Data Source=" + DBFileName;
                cnn.Open();
                using (DbCommand cmd = cnn.CreateCommand()) {
                    cmd.CommandText = String.Format("CREATE TABLE {0} (filetableid INTEGER PRIMARY KEY, guid TEXT, name TEXT, size INTEGER, ext TEXT, comment TEXT, creationtime DATETIME, lastwritetime DATETIME)", FileTableName);
                    cmd.ExecuteNonQuery();

                    cmd.CommandText = String.Format("CREATE TABLE {0} (tagtableid INTEGER PRIMARY KEY, tag TEXT, filetableid INTEGER)", TagTableName);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        private IEnumerable<string> insertTags(IEnumerable<string> tags)
        {
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
            List<string> tags;
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
                            filetable.size = fileinfo.Length;
                            filetable.ext = fileinfo.Extension;
                            filetable.comment = "";
                            filetable.creationtime = fileinfo.CreationTime;
                            filetable.lastwritetime = fileinfo.LastWriteTime;
                        
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
            }
        }

        private void update()
        {

        }
    }
}