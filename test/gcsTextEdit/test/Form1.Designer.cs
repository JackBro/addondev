namespace test
{
    partial class Form1
    {
        /// <summary>
        /// 必要なデザイナ変数です。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 使用中のリソースをすべてクリーンアップします。
        /// </summary>
        /// <param name="disposing">マネージ リソースが破棄される場合 true、破棄されない場合は false です。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows フォーム デザイナで生成されたコード

        /// <summary>
        /// デザイナ サポートに必要なメソッドです。このメソッドの内容を
        /// コード エディタで変更しないでください。
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.searchToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.UndoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.RedoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.CutToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.CopyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.PasteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.viewToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.WrapOnToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.WrapOffToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.ShowLineNumToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.fontToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.panel1 = new System.Windows.Forms.Panel();
            this.SearchPanel = new System.Windows.Forms.Panel();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.ReplaceTextBox = new System.Windows.Forms.TextBox();
            this.FindNextButton = new System.Windows.Forms.Button();
            this.FindTextBox = new System.Windows.Forms.TextBox();
            this.ReplaceAllButton = new System.Windows.Forms.Button();
            this.FindAllButton = new System.Windows.Forms.Button();
            this.ReplaceNextButton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.FindPreButton = new System.Windows.Forms.Button();
            this.RegxCheckBox = new System.Windows.Forms.CheckBox();
            this.label3 = new System.Windows.Forms.Label();
            this.IncSrcTextBox = new System.Windows.Forms.TextBox();
            this.IncSrcRegxCheckBox = new System.Windows.Forms.CheckBox();
            this.IncSrcMigemoCheckBox = new System.Windows.Forms.CheckBox();
            this.IncSrcPreCheckBox = new System.Windows.Forms.CheckBox();
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.CopyContextMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.CutContextMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.PasteContextMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.SelectAllContextMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.UndoContextMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.RedoContextMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.menuStrip1.SuspendLayout();
            this.SearchPanel.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.contextMenuStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.searchToolStripMenuItem,
            this.viewToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Padding = new System.Windows.Forms.Padding(8, 2, 0, 2);
            this.menuStrip1.Size = new System.Drawing.Size(492, 24);
            this.menuStrip1.TabIndex = 0;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(36, 20);
            this.fileToolStripMenuItem.Text = "File";
            // 
            // openToolStripMenuItem
            // 
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.Size = new System.Drawing.Size(96, 22);
            this.openToolStripMenuItem.Text = "Open";
            // 
            // searchToolStripMenuItem
            // 
            this.searchToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.UndoToolStripMenuItem,
            this.RedoToolStripMenuItem,
            this.CutToolStripMenuItem,
            this.CopyToolStripMenuItem,
            this.PasteToolStripMenuItem});
            this.searchToolStripMenuItem.Name = "searchToolStripMenuItem";
            this.searchToolStripMenuItem.Size = new System.Drawing.Size(37, 20);
            this.searchToolStripMenuItem.Text = "Edit";
            // 
            // UndoToolStripMenuItem
            // 
            this.UndoToolStripMenuItem.Name = "UndoToolStripMenuItem";
            this.UndoToolStripMenuItem.Size = new System.Drawing.Size(99, 22);
            this.UndoToolStripMenuItem.Text = "Undo";
            // 
            // RedoToolStripMenuItem
            // 
            this.RedoToolStripMenuItem.Name = "RedoToolStripMenuItem";
            this.RedoToolStripMenuItem.Size = new System.Drawing.Size(99, 22);
            this.RedoToolStripMenuItem.Text = "Redo";
            // 
            // CutToolStripMenuItem
            // 
            this.CutToolStripMenuItem.Name = "CutToolStripMenuItem";
            this.CutToolStripMenuItem.Size = new System.Drawing.Size(99, 22);
            this.CutToolStripMenuItem.Text = "Cut";
            // 
            // CopyToolStripMenuItem
            // 
            this.CopyToolStripMenuItem.Name = "CopyToolStripMenuItem";
            this.CopyToolStripMenuItem.Size = new System.Drawing.Size(99, 22);
            this.CopyToolStripMenuItem.Text = "Copy";
            // 
            // PasteToolStripMenuItem
            // 
            this.PasteToolStripMenuItem.Name = "PasteToolStripMenuItem";
            this.PasteToolStripMenuItem.Size = new System.Drawing.Size(99, 22);
            this.PasteToolStripMenuItem.Text = "Paste";
            // 
            // viewToolStripMenuItem
            // 
            this.viewToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.WrapOnToolStripMenuItem,
            this.WrapOffToolStripMenuItem,
            this.ShowLineNumToolStripMenuItem,
            this.fontToolStripMenuItem});
            this.viewToolStripMenuItem.Name = "viewToolStripMenuItem";
            this.viewToolStripMenuItem.Size = new System.Drawing.Size(40, 20);
            this.viewToolStripMenuItem.Text = "view";
            // 
            // WrapOnToolStripMenuItem
            // 
            this.WrapOnToolStripMenuItem.Name = "WrapOnToolStripMenuItem";
            this.WrapOnToolStripMenuItem.Size = new System.Drawing.Size(141, 22);
            this.WrapOnToolStripMenuItem.Text = "Wrap On";
            // 
            // WrapOffToolStripMenuItem
            // 
            this.WrapOffToolStripMenuItem.Name = "WrapOffToolStripMenuItem";
            this.WrapOffToolStripMenuItem.Size = new System.Drawing.Size(141, 22);
            this.WrapOffToolStripMenuItem.Text = "Wrap Off";
            // 
            // ShowLineNumToolStripMenuItem
            // 
            this.ShowLineNumToolStripMenuItem.CheckOnClick = true;
            this.ShowLineNumToolStripMenuItem.Name = "ShowLineNumToolStripMenuItem";
            this.ShowLineNumToolStripMenuItem.Size = new System.Drawing.Size(141, 22);
            this.ShowLineNumToolStripMenuItem.Text = "ShowLineNum";
            // 
            // fontToolStripMenuItem
            // 
            this.fontToolStripMenuItem.Name = "fontToolStripMenuItem";
            this.fontToolStripMenuItem.Size = new System.Drawing.Size(141, 22);
            this.fontToolStripMenuItem.Text = "Font";
            this.fontToolStripMenuItem.Click += new System.EventHandler(this.fontToolStripMenuItem_Click);
            // 
            // panel1
            // 
            this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel1.Location = new System.Drawing.Point(0, 113);
            this.panel1.Margin = new System.Windows.Forms.Padding(4);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(492, 360);
            this.panel1.TabIndex = 1;
            // 
            // SearchPanel
            // 
            this.SearchPanel.AutoSize = true;
            this.SearchPanel.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.SearchPanel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.SearchPanel.Controls.Add(this.tableLayoutPanel1);
            this.SearchPanel.Dock = System.Windows.Forms.DockStyle.Top;
            this.SearchPanel.Location = new System.Drawing.Point(0, 24);
            this.SearchPanel.Name = "SearchPanel";
            this.SearchPanel.Size = new System.Drawing.Size(492, 89);
            this.SearchPanel.TabIndex = 2;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.AutoSize = true;
            this.tableLayoutPanel1.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.tableLayoutPanel1.ColumnCount = 6;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 60F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 60F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayoutPanel1.Controls.Add(this.ReplaceTextBox, 1, 1);
            this.tableLayoutPanel1.Controls.Add(this.FindNextButton, 4, 0);
            this.tableLayoutPanel1.Controls.Add(this.FindTextBox, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.ReplaceAllButton, 5, 1);
            this.tableLayoutPanel1.Controls.Add(this.FindAllButton, 5, 0);
            this.tableLayoutPanel1.Controls.Add(this.ReplaceNextButton, 4, 1);
            this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.label2, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.FindPreButton, 3, 0);
            this.tableLayoutPanel1.Controls.Add(this.RegxCheckBox, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.label3, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.IncSrcTextBox, 1, 2);
            this.tableLayoutPanel1.Controls.Add(this.IncSrcRegxCheckBox, 2, 2);
            this.tableLayoutPanel1.Controls.Add(this.IncSrcMigemoCheckBox, 3, 2);
            this.tableLayoutPanel1.Controls.Add(this.IncSrcPreCheckBox, 4, 2);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 3;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.Size = new System.Drawing.Size(490, 87);
            this.tableLayoutPanel1.TabIndex = 6;
            // 
            // ReplaceTextBox
            // 
            this.ReplaceTextBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ReplaceTextBox.Location = new System.Drawing.Point(50, 34);
            this.ReplaceTextBox.Name = "ReplaceTextBox";
            this.ReplaceTextBox.Size = new System.Drawing.Size(217, 22);
            this.ReplaceTextBox.TabIndex = 0;
            // 
            // FindNextButton
            // 
            this.FindNextButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.FindNextButton.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.FindNextButton.Location = new System.Drawing.Point(393, 3);
            this.FindNextButton.Name = "FindNextButton";
            this.FindNextButton.Size = new System.Drawing.Size(44, 25);
            this.FindNextButton.TabIndex = 2;
            this.FindNextButton.Text = "Next";
            this.FindNextButton.UseVisualStyleBackColor = true;
            // 
            // FindTextBox
            // 
            this.FindTextBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.FindTextBox.Location = new System.Drawing.Point(50, 3);
            this.FindTextBox.Name = "FindTextBox";
            this.FindTextBox.Size = new System.Drawing.Size(217, 22);
            this.FindTextBox.TabIndex = 1;
            // 
            // ReplaceAllButton
            // 
            this.ReplaceAllButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ReplaceAllButton.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.ReplaceAllButton.Location = new System.Drawing.Point(443, 34);
            this.ReplaceAllButton.Name = "ReplaceAllButton";
            this.ReplaceAllButton.Size = new System.Drawing.Size(44, 22);
            this.ReplaceAllButton.TabIndex = 5;
            this.ReplaceAllButton.Text = "All";
            this.ReplaceAllButton.UseVisualStyleBackColor = true;
            // 
            // FindAllButton
            // 
            this.FindAllButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.FindAllButton.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.FindAllButton.Location = new System.Drawing.Point(443, 3);
            this.FindAllButton.Name = "FindAllButton";
            this.FindAllButton.Size = new System.Drawing.Size(44, 25);
            this.FindAllButton.TabIndex = 3;
            this.FindAllButton.Text = "All";
            this.FindAllButton.UseVisualStyleBackColor = true;
            // 
            // ReplaceNextButton
            // 
            this.ReplaceNextButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ReplaceNextButton.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.ReplaceNextButton.Location = new System.Drawing.Point(393, 34);
            this.ReplaceNextButton.Name = "ReplaceNextButton";
            this.ReplaceNextButton.Size = new System.Drawing.Size(44, 22);
            this.ReplaceNextButton.TabIndex = 4;
            this.ReplaceNextButton.Text = "Next";
            this.ReplaceNextButton.UseVisualStyleBackColor = true;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label1.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label1.Location = new System.Drawing.Point(3, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 31);
            this.label1.TabIndex = 6;
            this.label1.Text = "find";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label2.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label2.Location = new System.Drawing.Point(3, 31);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(41, 28);
            this.label2.TabIndex = 7;
            this.label2.Text = "rep";
            this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // FindPreButton
            // 
            this.FindPreButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.FindPreButton.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.FindPreButton.Location = new System.Drawing.Point(333, 3);
            this.FindPreButton.Name = "FindPreButton";
            this.FindPreButton.Size = new System.Drawing.Size(54, 25);
            this.FindPreButton.TabIndex = 8;
            this.FindPreButton.Text = "Pre";
            this.FindPreButton.UseVisualStyleBackColor = true;
            // 
            // RegxCheckBox
            // 
            this.RegxCheckBox.AutoSize = true;
            this.RegxCheckBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.RegxCheckBox.Font = new System.Drawing.Font("MS UI Gothic", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.RegxCheckBox.Location = new System.Drawing.Point(273, 3);
            this.RegxCheckBox.Name = "RegxCheckBox";
            this.RegxCheckBox.Size = new System.Drawing.Size(54, 25);
            this.RegxCheckBox.TabIndex = 9;
            this.RegxCheckBox.Text = "Regx";
            this.RegxCheckBox.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.RegxCheckBox.UseVisualStyleBackColor = true;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label3.Font = new System.Drawing.Font("ＭＳ ゴシック", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label3.Location = new System.Drawing.Point(3, 59);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(41, 28);
            this.label3.TabIndex = 10;
            this.label3.Text = "IncSrc";
            this.label3.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // IncSrcTextBox
            // 
            this.IncSrcTextBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.IncSrcTextBox.Location = new System.Drawing.Point(50, 62);
            this.IncSrcTextBox.Name = "IncSrcTextBox";
            this.IncSrcTextBox.Size = new System.Drawing.Size(217, 22);
            this.IncSrcTextBox.TabIndex = 11;
            // 
            // IncSrcRegxCheckBox
            // 
            this.IncSrcRegxCheckBox.AutoSize = true;
            this.IncSrcRegxCheckBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.IncSrcRegxCheckBox.Font = new System.Drawing.Font("ＭＳ ゴシック", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.IncSrcRegxCheckBox.Location = new System.Drawing.Point(273, 62);
            this.IncSrcRegxCheckBox.Name = "IncSrcRegxCheckBox";
            this.IncSrcRegxCheckBox.Size = new System.Drawing.Size(54, 22);
            this.IncSrcRegxCheckBox.TabIndex = 12;
            this.IncSrcRegxCheckBox.Text = "Regx";
            this.IncSrcRegxCheckBox.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.IncSrcRegxCheckBox.UseVisualStyleBackColor = true;
            // 
            // IncSrcMigemoCheckBox
            // 
            this.IncSrcMigemoCheckBox.AutoSize = true;
            this.IncSrcMigemoCheckBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.IncSrcMigemoCheckBox.Font = new System.Drawing.Font("ＭＳ ゴシック", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.IncSrcMigemoCheckBox.Location = new System.Drawing.Point(333, 62);
            this.IncSrcMigemoCheckBox.Name = "IncSrcMigemoCheckBox";
            this.IncSrcMigemoCheckBox.Size = new System.Drawing.Size(54, 22);
            this.IncSrcMigemoCheckBox.TabIndex = 13;
            this.IncSrcMigemoCheckBox.Text = "migemo";
            this.IncSrcMigemoCheckBox.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.IncSrcMigemoCheckBox.UseVisualStyleBackColor = true;
            // 
            // IncSrcPreCheckBox
            // 
            this.IncSrcPreCheckBox.AutoSize = true;
            this.IncSrcPreCheckBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.IncSrcPreCheckBox.Font = new System.Drawing.Font("ＭＳ ゴシック", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.IncSrcPreCheckBox.Location = new System.Drawing.Point(393, 62);
            this.IncSrcPreCheckBox.Name = "IncSrcPreCheckBox";
            this.IncSrcPreCheckBox.Size = new System.Drawing.Size(44, 22);
            this.IncSrcPreCheckBox.TabIndex = 14;
            this.IncSrcPreCheckBox.Text = "Pre";
            this.IncSrcPreCheckBox.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.IncSrcPreCheckBox.UseVisualStyleBackColor = true;
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.CopyContextMenuItem,
            this.CutContextMenuItem,
            this.PasteContextMenuItem,
            this.toolStripSeparator2,
            this.SelectAllContextMenuItem,
            this.toolStripSeparator1,
            this.UndoContextMenuItem,
            this.RedoContextMenuItem});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(153, 170);
            // 
            // CopyContextMenuItem
            // 
            this.CopyContextMenuItem.Name = "CopyContextMenuItem";
            this.CopyContextMenuItem.Size = new System.Drawing.Size(168, 22);
            this.CopyContextMenuItem.Text = "Copy";
            // 
            // CutContextMenuItem
            // 
            this.CutContextMenuItem.Name = "CutContextMenuItem";
            this.CutContextMenuItem.Size = new System.Drawing.Size(152, 22);
            this.CutContextMenuItem.Text = "Cut";
            // 
            // PasteContextMenuItem
            // 
            this.PasteContextMenuItem.Name = "PasteContextMenuItem";
            this.PasteContextMenuItem.Size = new System.Drawing.Size(152, 22);
            this.PasteContextMenuItem.Text = "Paste";
            // 
            // SelectAllContextMenuItem
            // 
            this.SelectAllContextMenuItem.Name = "SelectAllContextMenuItem";
            this.SelectAllContextMenuItem.Size = new System.Drawing.Size(168, 22);
            this.SelectAllContextMenuItem.Text = "SelectAll";
            // 
            // UndoContextMenuItem
            // 
            this.UndoContextMenuItem.Name = "UndoContextMenuItem";
            this.UndoContextMenuItem.Size = new System.Drawing.Size(168, 22);
            this.UndoContextMenuItem.Text = "Undo";
            // 
            // RedoContextMenuItem
            // 
            this.RedoContextMenuItem.Name = "RedoContextMenuItem";
            this.RedoContextMenuItem.Size = new System.Drawing.Size(168, 22);
            this.RedoContextMenuItem.Text = "Redo";
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(149, 6);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(149, 6);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(492, 473);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.SearchPanel);
            this.Controls.Add(this.menuStrip1);
            this.Font = new System.Drawing.Font("ＭＳ ゴシック", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.MainMenuStrip = this.menuStrip1;
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.Name = "Form1";
            this.Text = "Form1";
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.SearchPanel.ResumeLayout(false);
            this.SearchPanel.PerformLayout();
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.contextMenuStrip1.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem searchToolStripMenuItem;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.ToolStripMenuItem UndoToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem RedoToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem CopyToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem CutToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem PasteToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem viewToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem WrapOnToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem WrapOffToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem fontToolStripMenuItem;
        private System.Windows.Forms.Panel SearchPanel;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.TextBox ReplaceTextBox;
        private System.Windows.Forms.Button FindNextButton;
        private System.Windows.Forms.TextBox FindTextBox;
        private System.Windows.Forms.Button ReplaceAllButton;
        private System.Windows.Forms.Button FindAllButton;
        private System.Windows.Forms.Button ReplaceNextButton;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button FindPreButton;
        private System.Windows.Forms.CheckBox RegxCheckBox;
        private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem openToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem ShowLineNumToolStripMenuItem;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.ToolStripMenuItem CopyContextMenuItem;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox IncSrcTextBox;
        private System.Windows.Forms.CheckBox IncSrcRegxCheckBox;
        private System.Windows.Forms.CheckBox IncSrcMigemoCheckBox;
        private System.Windows.Forms.CheckBox IncSrcPreCheckBox;
        private System.Windows.Forms.ToolStripMenuItem CutContextMenuItem;
        private System.Windows.Forms.ToolStripMenuItem PasteContextMenuItem;
        private System.Windows.Forms.ToolStripMenuItem SelectAllContextMenuItem;
        private System.Windows.Forms.ToolStripMenuItem UndoContextMenuItem;
        private System.Windows.Forms.ToolStripMenuItem RedoContextMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;

    }
}

