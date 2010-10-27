using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS
{
    //public interface IControl
    //{
    //    UndoManager UndoManager
    //    {
    //        get;
    //    }
    //    void Insert(CaretInfo s, CaretInfo e, string text);
    //    void Delete(CaretInfo s, CaretInfo e);
    //}

    //public delegate void TextUpdateEventHandler(VPos s, VPos e, VPos e2);
    internal interface IDocument
    {
        event TextUpdateEventHandler TextUpdateEvent;
        UndoManager UndoManager
        {
            get;
        }
        void Insert(DPos s, DPos e, string value);
        void Delete(DPos s, DPos e);
        void Replace(DPos s, DPos e, string newValue);

    }
}
