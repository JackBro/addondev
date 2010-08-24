using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls
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

    public delegate void TextUpdateEventHandler(CaretInfo s, CaretInfo e, CaretInfo e2);
    public interface IDocument
    {
        event TextUpdateEventHandler TextUpdateEvent;
        UndoManager UndoManager
        {
            get;
        }
        void Insert(CaretInfo s, CaretInfo e, string value);
        void Delete(CaretInfo s, CaretInfo e, out string value);
        void Replace(CaretInfo s, CaretInfo e, string newValue);
    }
}
