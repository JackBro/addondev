using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SQLite;
using System.IO;

namespace testfdb_cs
{
    class SQLiteWrap
    {
        private SQLiteConnection connection;
        private SQLiteTransaction transaction;

        private string DBFileName;

        /// <summary>
        /// file data
        /// </summary>
        public string FileTable { get; set; }

        /// <summary>
        /// file-tag
        /// </summary>
        public string TaggedFileTable { get; set; }

        /// <summary>
        /// tag
        /// </summary>
        public string TagTable { get; set; }

        public SQLiteWrap()
        {
            DBFileName = "file.db";
            FileTable = "FileTable";
            TaggedFileTable = "TaggedFileTable";
            TagTable = "TagTable";
        }

        public void Connection()
        {
            connection = new SQLiteConnection("Data Source=" + DBFileName);
            if (connection != null) {
                connection.Open();
                connection.Close();

                createTable();
            }
           
        }

        private void createTable()
        {
            if (new FileInfo(DBFileName).Exists)
            {
                if (!existTable(FileTable))
                {
                    ExecuteQuery((cmd) =>
                    {
                        cmd.CommandText = String.Format("CREATE TABLE {0} (filetableid INTEGER PRIMARY KEY, guid TEXT, name TEXT, ext TEXT, comment TEXT, createtime DATETIME)", FileTable);
                        cmd.ExecuteNonQuery();
                    });
                }
                //if (!existTable(TaggedFileTable))
                //{
                //    ExecuteQuery((cmd) =>
                //    {
                //        cmd.CommandText = String.Format("CREATE TABLE {0} (id INTEGER PRIMARY KEY, guid TEXT, tag TEXT)", TaggedFileTable);
                //        cmd.ExecuteNonQuery();
                //    });
                //}
                if (!existTable(TagTable))
                {
                    ExecuteQuery((cmd) =>
                    {
                        cmd.CommandText = String.Format("CREATE TABLE {0} (tagtableid INTEGER PRIMARY KEY, tag TEXT, filetableid INTEGER)", TagTable);
                        cmd.ExecuteNonQuery();
                    });
                }
            }
        }

        private bool existTable(string tablename)
        {
            Int64 cnt = 0;
            this.ExecuteQuery((cmd) =>
            {
                cmd.CommandText = String.Format("select count(*) from sqlite_master where type='table' and name='{0}'", tablename);
                cnt = (Int64)cmd.ExecuteScalar();
            });
            return !(cnt == 0);
        }



        private void beginTransaction()
        {
            transaction = this.connection.BeginTransaction();
        }

        private void commitTransaction()
        {
            if (transaction != null)
            {
                transaction.Commit();
                transaction.Dispose();
                transaction = null;
            }
        }

        public void ExecuteQuery(Action<SQLiteCommand> func)
        {
            connection.Open();
            beginTransaction();

            using (SQLiteCommand command = connection.CreateCommand())
            {
                func(command);
            }

            commitTransaction();
            connection.Close();
        }
    }
}
