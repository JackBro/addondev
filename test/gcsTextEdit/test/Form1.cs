using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using YYS;
using YYS.Parser;
using System.IO;
using System.Text.RegularExpressions;

namespace test
{
    public partial class Form1 : Form
    {
        private Bitmap image = new Bitmap("test.png");
        private YYS.GCsTextEdit edit = new YYS.GCsTextEdit();
        private YYS.Search sr;
        private YYS.IncrementalSearch incsr;

        public Form1()
        {
            InitializeComponent();
            this.AllowDrop = true;
            edit.Name = "1";
            edit.BackColor = Color.White;
            edit.LineNumberBackColor = Color.White;
            edit.Font = this.Font;
            edit.ShowReturn = true;
            edit.ShowWhiteSpace = true;
            edit.ShowZenWhiteSpace = true;
            edit.ShowTab = true;
            edit.ShowLineNumber = true;
 
            //edit.Document.AddPartition(new PartRule("#start", "#end", "test"));
            //edit.Document.AddPartition(new PartRule("/*", "*/", "test"));
            //edit.Document.setHighlight("test", new Highlight());
            edit.Document.AddHighlight(Document.DEFAULT_ID, new Highlight2());
            edit.Document.AddHighlight("default.comment", new Highlight());

            edit.Document.SetPartition(Document.DEFAULT_ID);
            
            //csedit.Dock = DockStyle.Top;
            edit.Dock = DockStyle.Fill;
            //csedit.Height = this.Height / 2;
            //edit.AllowDrop = true;
            //edit.DrawEventHandler += (g, line, x, y) => {
            //    g.DrawImage(image, new Point(x, y));
            //};
            edit.ContextMenuStrip = contextMenuStrip1;
            edit.KeyPress += (sender, e) => {
                e.Handled = false;
            };
            this.DragEnter += (sender, e)=>{
                if (e.Data.GetDataPresent(DataFormats.FileDrop)) {
                    e.Effect = DragDropEffects.Move;
                }
            };
            this.DragDrop += (sender, e) => {
                if (e.Data.GetDataPresent(DataFormats.FileDrop)){
				    string[] fullpaths = (string[])e.Data.GetData(DataFormats.FileDrop);
                    if (File.Exists(fullpaths[0])) {
                        string text = File.ReadAllText(fullpaths[0], Encoding.GetEncoding("shift_jis"));
                        this.edit.Text = text;
                        this.edit.MoveCursor(new DPos(0, 0));
                        if (!this.Focused) {
                            this.Activate();
                        }
                    }
                }
            };
            
            //
            edit.KeyMap.setAction(Keys.Back, (editor) => {
                editor.BackSpace();
            });
            edit.KeyMap.setAction(Keys.Delete, (editor) => {
                editor.Delete();
            });

            edit.KeyMap.setAction(Keys.Up, (editor) => {
                editor.Up(false, false);
            });
            edit.KeyMap.setAction(Keys.Shift | Keys.Up, (editor) => {
                editor.Up(false, true);
            });

            edit.KeyMap.setAction(Keys.Down, (editor) => {
                editor.Down(false, false);
            });
            edit.KeyMap.setAction(Keys.Shift | Keys.Down, (editor) => {
                editor.Down(false, true);
            });

            edit.KeyMap.setAction(Keys.Left, (editor) => {
                editor.Left(false, false);
            });
            edit.KeyMap.setAction(Keys.Shift | Keys.Left, (editor) => {
                editor.Left(false, true);
            });

            edit.KeyMap.setAction(Keys.Right, (editor) => {
                editor.Right(false, false);
            });
            edit.KeyMap.setAction(Keys.Shift | Keys.Right, (editor) => {
                editor.Right(false, true);
            });

            edit.KeyMap.setAction(Keys.Control | Keys.C, (editor) => {
                editor.Copy();
            });
            edit.KeyMap.setAction(Keys.Control | Keys.X, (editor) => {
                editor.Cut();
            });
            edit.KeyMap.setAction(Keys.Control | Keys.V, (editor) => {
                editor.Paste();
            });

            edit.KeyMap.setAction(Keys.Control | Keys.A, (editor) => {
                editor.Home(true, false);
                editor.End(true, true);
            });

            edit.KeyMap.setAction(Keys.Control | Keys.Z, (editor) => {
                editor.Document.Undo();
            });

            edit.KeyMap.setAction(Keys.Control | Keys.Y, (editor) => {
                editor.Document.Redo();
            });

            edit.KeyMap.setAction(Keys.Control | Keys.T, (editor) => {
                edit.RectSelect = !edit.RectSelect;
            });

            //csedit.LinkClickEventHandler += (sender, e) => {
            //    MessageBox.Show(e.Link);
            //};

            WrapOnToolStripMenuItem.Click += (sender, e) => {
                WrapOnToolStripMenuItem.Checked = !WrapOnToolStripMenuItem.Checked;
                WrapOffToolStripMenuItem.Checked = !WrapOnToolStripMenuItem.Checked;
                if (WrapOffToolStripMenuItem.Checked) {
                    edit.Wrap = WrapType.NonWrap;

                } else if (WrapOnToolStripMenuItem.Checked) {
                    edit.Wrap = WrapType.WindowWidth;
                }
            };


            WrapOffToolStripMenuItem.Click += (sender, e) => {
                WrapOffToolStripMenuItem.Checked = !WrapOffToolStripMenuItem.Checked;
                WrapOnToolStripMenuItem.Checked = !WrapOffToolStripMenuItem.Checked;
                if (WrapOffToolStripMenuItem.Checked) {
                    edit.Wrap = WrapType.NonWrap;

                } else if (WrapOnToolStripMenuItem.Checked) {
                    edit.Wrap = WrapType.WindowWidth;
                }
            };

            ShowLineNumToolStripMenuItem.Click += (sender, e) => {
                edit.ShowLineNumber = ShowLineNumToolStripMenuItem.Checked;
            };

            FindNextButton.Click += (sender, e) => {
                if (sr == null) {
                    //sr = edit.Sr();
                    sr = new Search(edit);
                }
                sr.SearchWord = FindTextBox.Text;
                if (RegxCheckBox.Checked) {
                    sr.Searcher = new YYS.RegexSearch();
                } else {
                    sr.Searcher = new YYS.NormalSearch();
                }
                sr.FindNextImpl();
            };

            FindPreButton.Click += (sender, e) => {
                if (sr == null) {
                    //sr = edit.Sr();
                    sr = new Search(edit);
                }
                sr.SearchWord = FindTextBox.Text;
                if (RegxCheckBox.Checked) {
                    sr.Searcher = new YYS.RegexSearchRev();
                } else {
                    sr.Searcher = new YYS.NormalSearchRev();
                }
                sr.FindPrevImpl();
            };

            ReplaceNextButton.Click += (sender, e) => {
                if (sr == null) {
                    //sr = edit.Sr();
                    sr = new Search(edit);
                }
                sr.SearchWord = FindTextBox.Text;
                sr.ReplaceWord = ReplaceTextBox.Text;
                sr.Searcher = new YYS.NormalSearch();
                sr.ReplaceImpl();
            };

            ReplaceAllButton.Click += (sender, e) => {
                if (sr == null) {
                    //sr = edit.Sr();
                    sr = new Search(edit);
                }
                sr.SearchWord = FindTextBox.Text;
                sr.ReplaceWord = ReplaceTextBox.Text;
                sr.Searcher = new YYS.NormalSearch();
                sr.ReplaceAllImpl();
            };


            IncSrcTextBox.TextChanged += (sender, e) => {
                if (incsr == null) {
                    //incsr = edit.IncSr();
                    incsr = new IncrementalSearch(edit);
                    //incsr.Searcher = new YYS.NormalSearch();
                }
                incsr.SearchWord = IncSrcTextBox.Text;

                if (IncSrcPreCheckBox.Checked) {
                    if (!(incsr.Searcher is YYS.NormalSearchRev)) {
                        incsr.Searcher = new YYS.NormalSearchRev();
                    }
                    incsr.FindPrev();
                }
                else {
                    if (!(incsr.Searcher is YYS.NormalSearch)) {
                        incsr.Searcher = new YYS.NormalSearch();
                    }
                    incsr.FindNext();
                }
            };


            CopyContextMenuItem.Click += (sender, e) => {
                edit.Copy();
            };

            CutContextMenuItem.Click += (sender, e) => {
                edit.Cut();
            };

            PasteContextMenuItem.Click += (sender, e) => {
                edit.Paste();
            };

            SelectAllContextMenuItem.Click += (sender, e) => {
                edit.SelectAll();
            };

            UndoContextMenuItem.Click += (sender, e) => {
                edit.Document.Undo();
            };
            RedoContextMenuItem.Click += (sender, e) => {
                edit.Document.Redo();
            };

            edit.Wrap = YYS.WrapType.WindowWidth;
            WrapOnToolStripMenuItem.Checked = true;


            edit.Text = @"/*mm*/mm
/*ss
f/=fff
fff
f if f=/
mmmmm
zxcvbns*/mmm
me";

            panel1.Controls.Add(edit);

            //csedit.Text = "生徒を\uD842\uDF9F\uD842\uDF9Fる";
            //csedit.Text = "生徒を\uD842\uDF9Fる\r\nmmmmhhhhhhhhhhhhhhhhhhhhhhmmmmmmmmm\r\nssssssssssssss";

            //csedit2.Name = "2";
            //csedit2.BackColor = Color.Brown;
            //csedit2.Dock = DockStyle.Top;
            //csedit2.Height = this.Height / 2;
            //csedit2.Document = csedit.Document;
            //csedit2.GotFocus += new EventHandler(csedit2_GotFocus);
            //this.Controls.Add(csedit2);

            //csedit2.Text = "aaaaaaaa";

            //StringBuilder b = new StringBuilder();
            //b.Insert(0, "生徒を\uD842\uDF9Fる\uD842\uDF9F");
            //int oo = b.Length;

            //CsEdit.TextBuffer buf = new CsEdit.TextBuffer("生徒を\uD842\uDF9Fる");
            //buf.Insert(4, "IN");
            ////buf.Insert(5, "IN2");
            ////buf.Append("E");
            //int i = buf.Length;
            //this.KeyDown += new KeyEventHandler(Form1_KeyDown);
            //this.KeyPress += new KeyPressEventHandler(Form1_KeyPress);
            //csedit.DragOver += new DragEventHandler(csedit_DragOver);

            openToolStripMenuItem.Click += (sender, e) => {
                using (OpenFileDialog fd = new OpenFileDialog()) {
                }
            };
        }

        private void fontToolStripMenuItem_Click(object sender, EventArgs e) {
            using (FontDialog fd = new FontDialog()) {
                var res = fd.ShowDialog(this);
                if (res == DialogResult.OK) {
                    edit.Font = fd.Font;
                }
            }
        }
    }
}
