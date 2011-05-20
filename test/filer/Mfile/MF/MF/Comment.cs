using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MF {
    class Comment {
        public string GUID { get; set; }
        public string Text { get; set; }
        public string Name { get; set; }
    }

    class CommentMg {
        private List<Comment> comments;

        private string getGUID(string path) {
            return path;
        }

        public CommentMg() {
            comments = new List<Comment>();
        }

        public Comment Get(string path) {
            var g = getGUID(path);
            return comments.First(x => {
                return x.GUID == g;
            });
        }
        public void Insert(string path, String text) {
            var g = getGUID(path);
            var cm = Get(path);
            if (cm == null) {
                comments.Add(new Comment { GUID = getGUID(path), Text = text });
            }
            else {
                cm.Text = text; 
            }
        }
        public void Delete(string path) {
            var g = getGUID(path);
            var cm = Get(path);
            if(cm!=null){
                comments.Remove(cm);
            }
        }
    }
}
