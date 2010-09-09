using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls {
    public interface ICommand {
        ICommand Execute(Document doc);
    }
}
