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

    //public delegate void TextUpdateEventHandler(VPos s, VPos e, VPos e2);
    public interface IDocument
    {
        event TextUpdateEventHandler TextUpdateEvent;
        UndoManager UndoManager
        {
            get;
        }
        void Insert(VPos s, VPos e, string value);
        void Delete(VPos s, VPos e, out string value);
        void Replace(VPos s, VPos e, string newValue);
    }
}
