using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace YYS {
    public interface ITextEditor {

        //IDocument Document { get; set; }
        //KeyMap KeyMap { get; set; }
        Document GetDocument();

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

        void MoveCursor(DPos dp);
        void SetSelction(DPos s, DPos e);
        void GetSelction(out DPos s, out DPos e);
        void SelectAll();
    }
}
