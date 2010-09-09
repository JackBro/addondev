using System;
using System.Collections.Generic;
using System.Text;

namespace AsControls
{
    //public class UndoDelete : IUndoCommand
    //{
    //    IDocument document;
    //    VPos s, e;
    //    string text;

    //    public UndoDelete(IDocument document, VPos s, VPos e, string text)
    //    {
    //        this.document = document;
    //        this.s = new VPos();
    //        this.s.ad = s.ad;
    //        this.s.tl = s.tl;

    //        this.e = new VPos();
    //        this.e.ad = e.ad;
    //        this.e.tl = e.tl;

    //        //s.CopyTo(ref this.s);
    //        //e.CopyTo(ref this.e);
    //        this.text = text;
    //    }

    //    #region IUndoCommand メンバ

    //    public void Undo()
    //    {
    //        document.UndoManager.AcceptChanges = false;
    //        //insert
    //        document.Insert(s, e, text);
    //        document.UndoManager.AcceptChanges = true;
    //    }

    //    public void Redo()
    //    {
    //        document.UndoManager.AcceptChanges = false;
    //        //delete
    //        string buff;
    //        document.Delete(s, e, out buff);
    //        document.UndoManager.AcceptChanges = true;
    //    }

    //    #endregion
    //}

    //public class UndoInsert : IUndoCommand
    //{
    //    IDocument document;
    //    VPos s, e;
    //    string text;

    //    public UndoInsert(IDocument document, VPos s, VPos e, string text)
    //    {
    //        this.document = document;
    //        //s.CopyTo(ref this.s);
    //        //e.CopyTo(ref this.e);
    //        //this.s = new CursorPos(s);
    //        //this.e = new CursorPos(e);
    //        this.s = new VPos();
    //        this.s.ad = s.ad;
    //        this.s.tl = s.tl;

    //        this.e = new VPos();
    //        this.e.ad = e.ad;
    //        this.e.tl = e.tl;

    //        this.text = text;
    //    }

    //    #region IUndoCommand メンバ

    //    public void Undo()
    //    {
    //        document.UndoManager.AcceptChanges = false;
    //        //delete
    //        string buff;
    //        document.Delete(s, e, out buff);
    //        document.UndoManager.AcceptChanges = true;
    //    }

    //    public void Redo()
    //    {
    //        document.UndoManager.AcceptChanges = false;
    //        //insert
    //        document.Insert(s, e, text);
    //        document.UndoManager.AcceptChanges = true;
    //    }

    //    #endregion
    //}

    //public class UndoReplace : IUndoCommand
    //{
    //    IDocument document;
    //    VPos s, e, e2;
    //    string oldValue, newValue;

    //    public UndoReplace(IDocument document, VPos s, VPos e, VPos e2, string oldValue, string newValue)
    //    {
    //        this.document = document;

    //        this.s = new VPos();
    //        this.s.ad = s.ad;
    //        this.s.tl = s.tl;

    //        this.e = new VPos();
    //        this.e.ad = e.ad;
    //        this.e.tl = e.tl;

    //        this.e2 = new VPos();
    //        this.e2.ad = e2.ad;
    //        this.e2.tl = e2.tl;

    //        this.oldValue = oldValue;
    //        this.newValue = newValue;
    //    }

    //    #region IUndoCommand メンバ

    //    public void Undo()
    //    {
    //        document.UndoManager.AcceptChanges = false;

    //        string buff;
    //        document.Delete(s, e2, out buff);
    //        document.Insert(s, e, oldValue);
            
    //        //document.Replace(s, e, oldValue);

    //        document.UndoManager.AcceptChanges = true;
    //    }

    //    public void Redo()
    //    {
    //        document.UndoManager.AcceptChanges = false;

    //        string buff;
    //        document.Delete(s, e, out buff);
    //        document.Insert(s, e2, newValue);

    //        //document.Replace(s, e, newValue);

    //        document.UndoManager.AcceptChanges = true;
    //    }

    //    #endregion
    //}

    public class UndoManager
    {
        //internal bool AcceptChanges = true;
        public int MaxSize { get; set; }
        public Document Doc { get; set; }

        Stack<ICommand> undostack = new Stack<ICommand>();
        Stack<ICommand> redostack = new Stack<ICommand>();

        public Boolean CanUndo
        {
            get { return undostack.Count > 0; }
        }

        public Boolean CanRedo
        {
            get { return redostack.Count > 0; }
        }

        public UndoManager(Document doc) {
            Doc = doc;
            MaxSize = 32;
        }

        //public void Undo()
        //{
        //    if (!CanUndo) return;
        //    //if (undostack.Count == 0)
        //    //    throw new InvalidOperationException();

        //    IUndoCommand uedit = undostack.Pop();
        //    redostack.Push(uedit);
        //    uedit.Undo();
        //}

        //public void Redo()
        //{
        //    if (!CanRedo) return;
        //    //if (redostack.Count == 0)
        //    //    throw new InvalidOperationException();

        //    IUndoCommand uedit = redostack.Pop();
        //    undostack.Push(uedit);
        //    uedit.Redo();
        //}

        //public void Push(IUndoCommand command)
        //{
        //    if (AcceptChanges)
        //    {
        //        undostack.Push(command);
        //    }
        //}

        public void Invoke(ICommand command) {
            if (undostack.Count > MaxSize) {
                undostack.Pop();
            }
            redostack.Clear();
            undostack.Push(command);
        }

        public void Undo() {
            if (undostack.Count == 0) return;
            var command = undostack.Pop();
            ICommand cmd = command.Execute(Doc);
            redostack.Push(cmd);
        }

        public void Redo() {
            if (redostack.Count == 0) return;
            var command = redostack.Pop();
            ICommand cmd = command.Execute(Doc);
            undostack.Push(cmd);

        }

        public void Refresh() {
            undostack.Clear();
            redostack.Clear();
        }
    }
}
