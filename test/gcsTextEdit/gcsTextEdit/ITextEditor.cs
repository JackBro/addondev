using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls {
    public interface ITextEditor {
        void Copy();
        void Cut();
        void Paste();
        void BackSpace();
        void Delete();
        void Up();
        void Down();
        void Left();
        void Right();
    }
}
