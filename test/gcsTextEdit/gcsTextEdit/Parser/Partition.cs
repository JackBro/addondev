using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS.Parser {
    class Partition {
        public string ID;
        public IHighlight Highlight;
        //private IHighlight highlight;
        //public IHighlight Highlight{
        //    get{return highlight;}
        //    set{
        //        DefaultHighlight h = new DefaultHighlight(value.getDefault());

        //        var rules = highlight.getRules();
        //        foreach (var item in rules) {
        //            if (item is PartRule) {
        //                h.getRules().Add(item);
        //            }
        //        }
        //        //h.getRules().AddRange(value.getRules());
        //        highlight = h;
        //    }
        //}

        public Partition Parent;
        public PartRule rule;

        public List<Partition> Children = new List<Partition>();
        public Partition GetChildren(String ID) {
            foreach (var item in Children) {
                if (item.ID == ID) {
                    return item;
                }
            }
            return null;
        }

        public Partition(PartRule rule, IHighlight Highlight) {
            this.rule = rule;
            this.ID = this.rule.id;
            this.Highlight = Highlight;
        }

        public Partition(String ID, IHighlight Highlight) {
            this.ID = ID;
            this.Highlight = Highlight;
        }

        public void AddPartition(Partition partition) {
            partition.Parent = this;
            Children.Add(partition);
        }
    }
}
