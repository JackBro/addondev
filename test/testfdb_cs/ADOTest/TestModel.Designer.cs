﻿//------------------------------------------------------------------------------
// <auto-generated>
//     このコードはツールによって生成されました。
//     ランタイム バージョン:2.0.50727.3615
//
//     このファイルへの変更は、以下の状況下で不正な動作の原因になったり、
//     コードが再生成されるときに損失したりします。
// </auto-generated>
//------------------------------------------------------------------------------

[assembly: global::System.Data.Objects.DataClasses.EdmSchemaAttribute()]

// 元のファイル名:
// 生成日: 2010/08/18 15:59:53
namespace ADOTest
{
    
    /// <summary>
    /// スキーマの TestModelContainer にはコメントがありません。
    /// </summary>
    public partial class TestModelContainer : global::System.Data.Objects.ObjectContext
    {
        /// <summary>
        /// アプリケーション構成ファイルの 'TestModelContainer' セクションにある接続文字列を使用して新しい TestModelContainer オブジェクトを初期化します。
        /// </summary>
        public TestModelContainer() : 
                base("name=TestModelContainer", "TestModelContainer")
        {
            this.OnContextCreated();
        }
        /// <summary>
        /// 新しい TestModelContainer オブジェクトを初期化します。
        /// </summary>
        public TestModelContainer(string connectionString) : 
                base(connectionString, "TestModelContainer")
        {
            this.OnContextCreated();
        }
        /// <summary>
        /// 新しい TestModelContainer オブジェクトを初期化します。
        /// </summary>
        public TestModelContainer(global::System.Data.EntityClient.EntityConnection connection) : 
                base(connection, "TestModelContainer")
        {
            this.OnContextCreated();
        }
        partial void OnContextCreated();
        /// <summary>
        /// スキーマの Table1 にはコメントがありません。
        /// </summary>
        public global::System.Data.Objects.ObjectQuery<Table1> Table1
        {
            get
            {
                if ((this._Table1 == null))
                {
                    this._Table1 = base.CreateQuery<Table1>("[Table1]");
                }
                return this._Table1;
            }
        }
        private global::System.Data.Objects.ObjectQuery<Table1> _Table1;
        /// <summary>
        /// スキーマの Table2 にはコメントがありません。
        /// </summary>
        public global::System.Data.Objects.ObjectQuery<Table2> Table2
        {
            get
            {
                if ((this._Table2 == null))
                {
                    this._Table2 = base.CreateQuery<Table2>("[Table2]");
                }
                return this._Table2;
            }
        }
        private global::System.Data.Objects.ObjectQuery<Table2> _Table2;
        /// <summary>
        /// スキーマの Table1 にはコメントがありません。
        /// </summary>
        public void AddToTable1(Table1 table1)
        {
            base.AddObject("Table1", table1);
        }
        /// <summary>
        /// スキーマの Table2 にはコメントがありません。
        /// </summary>
        public void AddToTable2(Table2 table2)
        {
            base.AddObject("Table2", table2);
        }
    }
    /// <summary>
    /// スキーマの TestModel.Table1 にはコメントがありません。
    /// </summary>
    /// <KeyProperties>
    /// ID
    /// </KeyProperties>
    [global::System.Data.Objects.DataClasses.EdmEntityTypeAttribute(NamespaceName="TestModel", Name="Table1")]
    [global::System.Runtime.Serialization.DataContractAttribute(IsReference=true)]
    [global::System.Serializable()]
    public partial class Table1 : global::System.Data.Objects.DataClasses.EntityObject
    {
        /// <summary>
        /// 新しい Table1 オブジェクトを作成します。
        /// </summary>
        /// <param name="id">ID の初期値。</param>
        /// <param name="text">text の初期値。</param>
        public static Table1 CreateTable1(long id, string text)
        {
            Table1 table1 = new Table1();
            table1.ID = id;
            table1.text = text;
            return table1;
        }
        /// <summary>
        /// スキーマのプロパティ ID にはコメントがありません。
        /// </summary>
        [global::System.Data.Objects.DataClasses.EdmScalarPropertyAttribute(EntityKeyProperty=true, IsNullable=false)]
        [global::System.Runtime.Serialization.DataMemberAttribute()]
        public long ID
        {
            get
            {
                return this._ID;
            }
            set
            {
                this.OnIDChanging(value);
                this.ReportPropertyChanging("ID");
                this._ID = global::System.Data.Objects.DataClasses.StructuralObject.SetValidValue(value);
                this.ReportPropertyChanged("ID");
                this.OnIDChanged();
            }
        }
        private long _ID;
        partial void OnIDChanging(long value);
        partial void OnIDChanged();
        /// <summary>
        /// スキーマのプロパティ text にはコメントがありません。
        /// </summary>
        [global::System.Data.Objects.DataClasses.EdmScalarPropertyAttribute(IsNullable=false)]
        [global::System.Runtime.Serialization.DataMemberAttribute()]
        public string text
        {
            get
            {
                return this._text;
            }
            set
            {
                this.OntextChanging(value);
                this.ReportPropertyChanging("text");
                this._text = global::System.Data.Objects.DataClasses.StructuralObject.SetValidValue(value, false);
                this.ReportPropertyChanged("text");
                this.OntextChanged();
            }
        }
        private string _text;
        partial void OntextChanging(string value);
        partial void OntextChanged();
    }
    /// <summary>
    /// スキーマの TestModel.Table2 にはコメントがありません。
    /// </summary>
    /// <KeyProperties>
    /// ID
    /// </KeyProperties>
    [global::System.Data.Objects.DataClasses.EdmEntityTypeAttribute(NamespaceName="TestModel", Name="Table2")]
    [global::System.Runtime.Serialization.DataContractAttribute(IsReference=true)]
    [global::System.Serializable()]
    public partial class Table2 : global::System.Data.Objects.DataClasses.EntityObject
    {
        /// <summary>
        /// 新しい Table2 オブジェクトを作成します。
        /// </summary>
        /// <param name="id">ID の初期値。</param>
        /// <param name="name">name の初期値。</param>
        public static Table2 CreateTable2(long id, string name)
        {
            Table2 table2 = new Table2();
            table2.ID = id;
            table2.name = name;
            return table2;
        }
        /// <summary>
        /// スキーマのプロパティ ID にはコメントがありません。
        /// </summary>
        [global::System.Data.Objects.DataClasses.EdmScalarPropertyAttribute(EntityKeyProperty=true, IsNullable=false)]
        [global::System.Runtime.Serialization.DataMemberAttribute()]
        public long ID
        {
            get
            {
                return this._ID;
            }
            set
            {
                this.OnIDChanging(value);
                this.ReportPropertyChanging("ID");
                this._ID = global::System.Data.Objects.DataClasses.StructuralObject.SetValidValue(value);
                this.ReportPropertyChanged("ID");
                this.OnIDChanged();
            }
        }
        private long _ID;
        partial void OnIDChanging(long value);
        partial void OnIDChanged();
        /// <summary>
        /// スキーマのプロパティ name にはコメントがありません。
        /// </summary>
        [global::System.Data.Objects.DataClasses.EdmScalarPropertyAttribute(IsNullable=false)]
        [global::System.Runtime.Serialization.DataMemberAttribute()]
        public string name
        {
            get
            {
                return this._name;
            }
            set
            {
                this.OnnameChanging(value);
                this.ReportPropertyChanging("name");
                this._name = global::System.Data.Objects.DataClasses.StructuralObject.SetValidValue(value, false);
                this.ReportPropertyChanged("name");
                this.OnnameChanged();
            }
        }
        private string _name;
        partial void OnnameChanging(string value);
        partial void OnnameChanged();
    }
}
