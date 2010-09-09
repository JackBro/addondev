using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls {
    interface ICommand {
        event TextUpdateEventHandler TextUpdate;
        ICommand Execute(Document doc);
    }
}
