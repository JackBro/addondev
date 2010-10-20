using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS {
    public interface ICommand {
        ICommand Execute(Document doc);
    }
}
