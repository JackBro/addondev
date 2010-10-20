using System;
using System.Collections.Generic;
using System.Text;

namespace YYS
{
    public interface IUndoCommand
    {
        void Undo();
        void Redo();
    }
}