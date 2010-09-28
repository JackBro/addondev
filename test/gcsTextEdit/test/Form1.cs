﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using AsControls;

namespace test
{
    public partial class Form1 : Form
    {
        private Bitmap image = new Bitmap("test.png");
        private AsControls.gcsTextEdit edit = new AsControls.gcsTextEdit();
        //private AsControls.gcsTextEdit csedit2 = new AsControls.gcsTextEdit();
        private AsControls.Search sr;
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
            //csedit.Dock = DockStyle.Top;
            edit.Dock = DockStyle.Fill;
            //csedit.Height = this.Height / 2;

            //edit.DrawEventHandler += (g, line, x, y) => {
            //    g.DrawImage(image, new Point(x, y));
            //};

            bool flg = false;
            edit.MouseDown += (sender, e) => {
                //edit.SelectMode = AsControls.SelectType.Normal;
                if (edit.SelectMode == AsControls.SelectType.Rectangle) {
                    flg = true;
                }
            };
            edit.MouseUp += (sender, e) => {
                if (flg) {
                    edit.SelectMode = AsControls.SelectType.Normal;
                }
                flg = false;
            };
            edit.MouseMove += (sender, e) => {
                if (e.Button == MouseButtons.Left && (Control.ModifierKeys & Keys.Alt) == Keys.Alt) {
                    edit.SelectMode = AsControls.SelectType.Rectangle;
                }
            };
            //
            edit.KeyBind.setAction(Keys.Back, (editor) => {
                editor.BackSpace();
            });
            edit.KeyBind.setAction(Keys.Delete, (editor) => {
                editor.Delete();
            });

            edit.KeyBind.setAction(Keys.Up, (editor) => {
                editor.Up(false, false);
            });
            edit.KeyBind.setAction(Keys.Shift | Keys.Up, (editor) => {
                editor.Up(false, true);
            });

            edit.KeyBind.setAction(Keys.Down, (editor) => {
                editor.Down(false, false);
            });
            edit.KeyBind.setAction(Keys.Shift | Keys.Down, (editor) => {
                editor.Down(false, true);
            });

            edit.KeyBind.setAction(Keys.Left, (editor) => {
                editor.Left(false, false);
            });
            edit.KeyBind.setAction(Keys.Shift | Keys.Left, (editor) => {
                editor.Left(false, true);
            });

            edit.KeyBind.setAction(Keys.Right, (editor) => {
                editor.Right(false, false);
            });
            edit.KeyBind.setAction(Keys.Shift | Keys.Right, (editor) => {
                editor.Right(false, true);
            });

            edit.KeyBind.setAction(Keys.Control | Keys.C, (editor) => {
                editor.Copy();
            });
            edit.KeyBind.setAction(Keys.Control | Keys.X, (editor) => {
                editor.Cut();
            });
            edit.KeyBind.setAction(Keys.Control | Keys.V, (editor) => {
                editor.Paste();
            });

            edit.KeyBind.setAction(Keys.Control | Keys.A, (editor) => {
                editor.Home(true, false);
                editor.End(true, true);
            });

            edit.KeyBind.setAction(Keys.Control | Keys.Z, (editor) => {
                editor.Undo();
            });

            edit.KeyBind.setAction(Keys.Control | Keys.Y, (editor) => {
                editor.Redo();
            });

            //csedit.LinkClickEventHandler += (sender, e) => {
            //    MessageBox.Show(e.Link);
            //};



            wrapOToolStripMenuItem.Click += (sender, e) => {
                wrapOToolStripMenuItem.Checked = !wrapOToolStripMenuItem.Checked;
                wrapOffToolStripMenuItem.Checked = !wrapOToolStripMenuItem.Checked;


            };

            wrapOffToolStripMenuItem.Checked = true;
            wrapOffToolStripMenuItem.Click += (sender, e) => {
                wrapOffToolStripMenuItem.Checked = !wrapOffToolStripMenuItem.Checked;
                wrapOToolStripMenuItem.Checked = !wrapOffToolStripMenuItem.Checked;
            };

            CursorPos pos=null;
            edit.MouseClick+= (sender, e) => {
                pos = null;
            };
            FindTextBox.TextChanged += (sender, e) => {
                if (sr == null) {
                    sr = edit.Sr();
                }

                if (pos != null) {
                    //pos = edit.GetSelect().t1;
                    edit.cursor.MoveCur(new DPos(pos.line, pos.index), false);
                }

                sr.SearchWord = FindTextBox.Text;
                if (RegxCheckBox.Checked) {
                    sr.Searcher = new AsControls.RegexSearch();
                }
                else {
                    sr.Searcher = new AsControls.NormalSearch();
                }
                sr.FindNextImpl();
                if (pos == null) {
                    pos = edit.GetSelect().t1;
                    //edit.cursor.MoveCur(new DPos(pos.line, pos.index), false);
                }
                else {
                    CursorPos cur = edit.GetSelect().t1;
                    if (pos.line == cur.line && pos.index == cur.index) {
                        //edit.cursor.MoveCur(new DPos(pos.line, pos.index), false);
                        pos = edit.GetSelect().t1;
                    }
                    else {

                    }
                }
            };

            FindNextButton.Click += (sender, e) => {
                if (sr == null) {
                    sr = edit.Sr();
                }
                sr.SearchWord = FindTextBox.Text;
                if (RegxCheckBox.Checked) {
                    sr.Searcher = new AsControls.RegexSearch();
                } else {
                    sr.Searcher = new AsControls.NormalSearch();
                }
                sr.FindNextImpl();
            };

            FindPreButton.Click += (sender, e) => {
                if (sr == null) {
                    sr = edit.Sr();
                }
                sr.SearchWord = FindTextBox.Text;
                if (RegxCheckBox.Checked) {
                    sr.Searcher = new AsControls.RegexSearchRev();
                } else {
                    sr.Searcher = new AsControls.NormalSearchRev();
                }
                sr.FindPrevImpl();
            };

            ReplaceNextButton.Click += (sender, e) => {
                if (sr == null) {
                    sr = edit.Sr();
                }
                sr.SearchWord = FindTextBox.Text;
                sr.ReplaceWord = ReplaceTextBox.Text;
                sr.Searcher = new AsControls.NormalSearch();
                sr.ReplaceImpl();
            };

            ReplaceAllButton.Click += (sender, e) => {
                if (sr == null) {
                    sr = edit.Sr();
                }
                sr.SearchWord = FindTextBox.Text;
                sr.ReplaceWord = ReplaceTextBox.Text;
                sr.Searcher = new AsControls.NormalSearch();
                sr.ReplaceAllImpl();
            };

            panel1.Controls.Add(edit);
            //csedit.Text = "生徒を\uD842\uDF9F\uD842\uDF9Fる";
            //csedit.Text = "生徒を\uD842\uDF9Fる\r\nmmmmhhhhhhhhhhhhhhhhhhhhhhmmmmmmmmm\r\nssssssssssssss";

//            csedit.Text = @"123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
//mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
//TTTTTTTTTTTTgg
//ffffffffffffffffff
//hhhhhhhhhhhhhh
//kkkkkkkkkkkkkkkk
//kkkkkkkkkkkkkkkk
//kkkkkkkkkkkkkkkkk
//llllllllllllllllllll
//fffffffffffffffff
//eeeeeeeeeeeeeeeeeeeee
//r
//r
//r
//r
//r
//r
//
//ryyyyyyyyyyyyyyyyyyyyyyyyy
//yy
//yyy
//y
//yy";

//            csedit.Text = @"/*
//mmmmmmmmmmmmm
//*/
//";
            edit.Wrap = AsControls.WrapType.WindowWidth;
//            edit.Text = @"/*m*/
//1234mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmテスト1234567890ABCDEFG
//*/
//mm
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

        private void undoToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void redoToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void copyToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void cutToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void pasteToolStripMenuItem_Click(object sender, EventArgs e)
        {

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
