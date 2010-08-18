using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SQLite;


namespace ADOTest {
    class Program {
        static void Main(string[] args) {
            var connection = new SQLiteConnection("Data Source=" + "adotest.db");
            connection.Open();
            using (SQLiteCommand command = connection.CreateCommand()) {
                command.CommandText = String.Format("CREATE TABLE {0} (ID INTEGER PRIMARY KEY, text TEXT)", "Table1");
                command.ExecuteNonQuery();

                command.CommandText = String.Format("CREATE TABLE {0} (ID INTEGER PRIMARY KEY, name TEXT)", "Table2");
                command.ExecuteNonQuery();

                //command.CommandText = String.Format("INSERT INTO {0}(ID,text) VALUES('{1}', '{2}')", "guidtable", Guid.NewGuid(), "test");
                //command.ExecuteNonQuery();
            }
            connection.Close();

            //using (TestModelContainer test = new TestModelContainer(@"metadata=res://*/Model1.csdl|res://*/Model1.ssdl|res://*/Model1.msl;provider=System.Data.SQLite;provider connection string='data source=""adotest.db""'"))
            using (TestModelContainer test = new TestModelContainer())
            {


                Table1 table = new Table1();
                //table.ID = 0;
                table.text = "test1";
                test.AddToTable1(table);
                test.SaveChanges();

                //var query = from c in test.Table1
                //            //where c.text == "London"
                //            select c;
                //foreach (Table1 c in query) {
                //    Console.WriteLine("query = " + c.ID);
                //}

                //var fquery = from c in db.FileTable
                //            where c.guid == strguid
                //            select c;
                test.AttachTo("Table1", new Table1() { ID = table.ID });
                test.AddToTable2(new Table2() { name="add" });
                //foreach (FileTable f in fquery) {
                //db.AttachTo(filetable.EntityKey.EntitySetName, 
                //     new FileTable() { filetableid = filetable.filetableid });
                //     foreach (string tag in tags) {
                //        db.AddToTagTable(new TagTable() { tag = tag, FileTable = filetable });
                //    }
                
            }
        }
    }
}
