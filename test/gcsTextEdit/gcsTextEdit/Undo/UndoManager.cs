using System;
using System.Collections.Generic;
using System.Text;

namespace AsControls
{
    public class UndoManager
    {
        public int MaxSize { get; set; }
        public Document Doc { get; set; }

        //Stack<ICommand> undostack = new Stack<ICommand>();
        //Stack<ICommand> redostack = new Stack<ICommand>();
        Stack<List<ICommand>> undostack = new Stack<List<ICommand>>();
        Stack<List<ICommand>> redostack = new Stack<List<ICommand>>();

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

        public void Invoke(ICommand command) {
            if (undostack.Count > MaxSize) {
                undostack.Pop();
            }
            redostack.Clear();
            undostack.Push(new List<ICommand>() { command });
        }

        public void Invoke(List<ICommand> commands) {
            if (commands.Count == 0) return;

            if (undostack.Count > MaxSize) {
                undostack.Pop();
            }
            redostack.Clear();
            undostack.Push(commands);
        }

        public void Undo() {
            if (undostack.Count == 0) return;
            //var command = undostack.Pop();
            //ICommand cmd = command.Execute(Doc);
            //redostack.Push(cmd);
            var cs =new List<ICommand>(); 
            var cmds = undostack.Pop();
            //foreach (var cmd in cmds) {
            //    cs.Add(cmd.Execute(Doc));              
            //}
            for (int i = cmds.Count - 1; i >= 0; i--) {
                cs.Add(cmds[i].Execute(Doc)); 
            }
            redostack.Push(cs);
        }

        public void Redo() {
            if (redostack.Count == 0) return;
            //var command = redostack.Pop();
            //ICommand cmd = command.Execute(Doc);
            //undostack.Push(cmd);
            var cs = new List<ICommand>();
            var cmds = redostack.Pop();
            //foreach (var cmd in commands) {
            //    cs.Add(cmd.Execute(Doc));
            //}
            for (int i = cmds.Count - 1; i >= 0; i--) {
                cs.Add(cmds[i].Execute(Doc));
            }
            undostack.Push(cs);
        }

        public void Refresh() {
            undostack.Clear();
            redostack.Clear();
        }
    }
}
