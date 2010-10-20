using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS {
    public interface ITextEditor {
        void Copy();
        void Cut();
        void Paste();
        void BackSpace();
        void Delete();
        
        void Up(bool wide, bool select);
        void Down(bool wide, bool select);
        void Left(bool wide, bool select);
        void Right(bool wide, bool select);

        void Home(bool wide, bool select);
        void End(bool wide, bool select);

        bool CanUndo();
        bool CanRedo();
        void Undo();
        void Redo();

        //string getSelectText();

    }
}
