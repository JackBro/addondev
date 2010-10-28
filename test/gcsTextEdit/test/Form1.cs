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
        //private YYS.ITextEditor edit = new YYS.GCsTextEdit();
        //private AsControls.gcsTextEdit csedit2 = new AsControls.gcsTextEdit();
        private YYS.Search sr;
        private YYS.IncrementalSearch incsr;
        public Form1()
        {
            InitializeComponent();
            //this.KeyPreview = true;
            edit.Name = "1";
            edit.BackColor = Color.White;
            edit.LineNumberBackColor = Color.White;
            edit.Font = this.Font;
            edit.ShowReturn = true;
            edit.ShowWhiteSpace = true;
            edit.ShowZenWhiteSpace = true;
            edit.ShowTab = true;
            edit.Document.AddPartition(new PartRule("#start", "#end", "test"));
            edit.Document.setHighlight("test", new Highlight());
            //csedit.Dock = DockStyle.Top;
            edit.Dock = DockStyle.Fill;
            //csedit.Height = this.Height / 2;
            edit.ShowLineNumber = true;
            edit.DrawEventHandler += (g, line, x, y) => {
                g.DrawImage(image, new Point(x, y));
            };
            edit.ContextMenuStrip = contextMenuStrip1;
            edit.KeyPress += (sender, e) => {
                e.Handled = false;
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
                editor.GetDocument().Undo();
            });

            edit.KeyMap.setAction(Keys.Control | Keys.Y, (editor) => {
                editor.GetDocument().Redo();
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
                    sr = edit.Sr();
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
                    sr = edit.Sr();
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
                    sr = edit.Sr();
                }
                sr.SearchWord = FindTextBox.Text;
                sr.ReplaceWord = ReplaceTextBox.Text;
                sr.Searcher = new YYS.NormalSearch();
                sr.ReplaceImpl();
            };

            ReplaceAllButton.Click += (sender, e) => {
                if (sr == null) {
                    sr = edit.Sr();
                }
                sr.SearchWord = FindTextBox.Text;
                sr.ReplaceWord = ReplaceTextBox.Text;
                sr.Searcher = new YYS.NormalSearch();
                sr.ReplaceAllImpl();
            };


            IncSrcTextBox.TextChanged += (sender, e) => {
                if (incsr == null) {
                    incsr = edit.IncSr();
                    incsr.Searcher = new YYS.NormalSearch();
                }
                incsr.SearchWord = FindTextBox.Text;
                incsr.FindNext();
            };

            edit.Wrap = YYS.WrapType.WindowWidth;
            WrapOnToolStripMenuItem.Checked = true;

//            edit.Text = @"/*sss*/
//#start
///*
//e
//ee
//*/
//#end";

            edit.Text = "/*ss\ns*/mmm\nme";

            panel1.Controls.Add(edit);

            //csedit.Text = "生徒を\uD842\uDF9F\uD842\uDF9Fる";
            //csedit.Text = "生徒を\uD842\uDF9Fる\r\nmmmmhhhhhhhhhhhhhhhhhhhhhhmmmmmmmmm\r\nssssssssssssss";


//            edit.Text = @"1234mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmテスト1234567890ABCDEFG
//*/
//mmmmmmmmmmmmmmmmmmmmmmmmmm
//12
//a";
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
