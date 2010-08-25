﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace test
{
    public partial class Form1 : Form
    {
        private AsControls.gcsTextEdit csedit = new AsControls.gcsTextEdit();
        private AsControls.gcsTextEdit csedit2 = new AsControls.gcsTextEdit();
        public Form1()
        {
            InitializeComponent();
            //this.KeyPreview = true;
            csedit.Name = "1";
            csedit.BackColor = Color.White;
            csedit.Font = this.Font;
            //csedit.Dock = DockStyle.Top;
            csedit.Dock = DockStyle.Fill;
            //csedit.Height = this.Height / 2;
            csedit.GotFocus += new EventHandler(csedit_GotFocus);

            csedit.KeyBind.setAction(Keys.Control | Keys.V, (editor) =>
            {
                editor.Paste();
            });

            panel1.Controls.Add(csedit);
            //csedit.Text = "生徒を\uD842\uDF9F\uD842\uDF9Fる";
            //csedit.Text = "生徒を\uD842\uDF9Fる\r\nmmmmhhhhhhhhhhhhhhhhhhhhhhmmmmmmmmm\r\nssssssssssssss";
//            csedit.Text = @"<html>
//<head>
//<meta http-equiv=""content-type"" content=""text/html; charset=UTF-8"">
//	<title>HOGE</title>
//</head>
//<body>
//:)
//</body>
//</html>
//";
            csedit.Text = @"12345
title>HOGE</title>";

            csedit.Text = @"1234567890";

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

        void csedit2_GotFocus(object sender, EventArgs e)
        {
            Text = "Brown";
        }

        void csedit_GotFocus(object sender, EventArgs e)
        {
            Text = "White";
            //throw new NotImplementedException();
        }

        private void searchToolStripMenuItem_Click(object sender, EventArgs e)
        {
            csedit.SearchText("title");
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

        private void selectAllToolStripMenuItem_Click(object sender, EventArgs e)
        {
            int offset = csedit.offset;
            int i = 0;
            csedit.Insert(2, 3, "repl");
        }

        private void searchToolStripMenuItem1_Click(object sender, EventArgs e)
        {

        }
    }
}