using System;
using System.Collections.Generic;
using System.Text;

namespace AsControls
{
    public interface IUndoCommand
    {
        void Undo();
        void Redo();
    }
}