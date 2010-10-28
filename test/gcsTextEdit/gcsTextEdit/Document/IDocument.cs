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
    public interface IDocument
    {
        //event TextUpdateEventHandler TextUpdateEvent;
        //UndoManager UndoManager
        //{
        //    get;
        //}

        string Text { get; set; }
        int Count { get; }

        void Insert(DPos s, DPos e, string value);
        void Delete(DPos s, DPos e);
        void Replace(DPos s, DPos e, string newValue);

        void Execute(ICommand command);

        void Undo();
        void Redo();
        bool CanUndo();
        bool CanRedo();

        
        string GetText(int line);
        int GetLength(int line);

    }
}
