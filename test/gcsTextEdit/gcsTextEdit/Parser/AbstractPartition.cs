using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS.Parser {
    public abstract class AbstractPartition {
        public string ID;
        //public IHighlight Highlight;
        public AbstractPartition Parent;
        //public PartRule rule;

        public List<AbstractPartition> Children;// = new List<Partition>();
        public AbstractPartition GetChildren(String ID) {
            foreach (var item in Children) {
                if (item.ID == ID) {
                    return item;
                }
            }
            return null;
        }
        //public Partition(string ID, PartRule rule, IHighlight Highlight) {
        //    this.ID = ID;
        //    this.rule = rule;
        //    this.Highlight = Highlight;
        //}

        //public Partition(IHighlight Highlight) {
        //    this.ID = Document.DEFAULT_ID;
        //    this.Highlight = Highlight;
        //}

        public abstract IHighlight GetHighlight();
        public abstract PartRule GetPartRule();

        public void AddPartition(AbstractPartition partition) {
            partition.Parent = this;
            Children.Add(partition);
        }
    }
}
