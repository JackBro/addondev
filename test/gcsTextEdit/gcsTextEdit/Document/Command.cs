using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS {

    class Insert : ICommand {
        private DPos stt_;
        private string buf_;

        public Insert(DPos s, string str) {
            stt_ = new DPos(s);
            buf_ = str;
        }

        #region ICommand メンバ

        public ICommand Execute(Document doc) {

            // 挿入
            DPos s = new DPos(stt_); //, e;
            DPos e = new DPos();
            bool aa = doc.InsertingOperation(ref s, buf_, ref e);

            // イベント発火
            //doc.Fire_TEXTUPDATE(s, s, e, aa, true);
            doc.Fire_TEXTUPDATE(s, s, e, aa, true);

            // 逆操作オブジェクトを返す
            return new Delete(s, e);
        }

        #endregion
    };

    class Delete : ICommand {
        private DPos stt_;
        private DPos end_;

        public Delete(DPos s, DPos e) {
            stt_ = new DPos(s);
            end_ = new DPos(e);
        }

        #region ICommand メンバ

        public ICommand Execute(Document doc) {
            //DocImpl & di = d.impl();

            //// 削除
            //unicode* buf;
            //ulong siz;
            //DPos s = stt_, e = end_;
            //bool aa = di.DeletingOperation(s, e, buf, siz);

            //// イベント発火
            //di.Fire_TEXTUPDATE(s, e, s, aa, true);

            //// 逆操作オブジェクトを返す
            //return new Insert(s, buf, siz, true);

            string buf;
            DPos s = new DPos(stt_);
            DPos e = new DPos(end_);
            bool aa = doc.DeletingOperation(ref s, ref e, out buf);
            //TextUpdate(s, e, s, aa, true);
            doc.Fire_TEXTUPDATE(s, e, s, aa, true);

            return new Insert(s, buf);
        }

        #endregion
    }

    class Replace : ICommand {
        private DPos stt_;
        private DPos end_;
        private string buf_;

        public Replace(DPos s, DPos e, string str) {
            stt_ = new DPos(s);
            end_ = new DPos(e);
            buf_ = str;
        }

        #region ICommand メンバ

        public ICommand Execute(Document doc) {
            //DocImpl & di = d.impl();

            //// 削除
            //unicode* buf;
            //ulong siz;
            //DPos s = stt_, e = end_;
            //bool aa = di.DeletingOperation(s, e, buf, siz);

            //// 挿入
            //DPos e2;
            //aa = (di.InsertingOperation(s, buf_, len_, e2) || aa);

            //// イベント発火
            //di.Fire_TEXTUPDATE(s, e, e2, aa, true);

            //// 逆操作オブジェクトを返す
            //return new Replace(s, e2, buf, siz, true);

            string buf;
            DPos s = new DPos(stt_);
            DPos e = new DPos(end_);
            bool aa = doc.DeletingOperation(ref s, ref e, out buf);

            DPos e2 = new DPos();
            aa = (doc.InsertingOperation(ref s, buf_, ref e2) || aa);

            doc.Fire_TEXTUPDATE(s, e, e2, aa, true);

            return new Replace(s, e2, buf);
        }

        #endregion
    }
}
