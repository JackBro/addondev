namespace wiki {
    partial class EditorConfig {
        /// <summary> 
        /// 必要なデザイナ変数です。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// 使用中のリソースをすべてクリーンアップします。
        /// </summary>
        /// <param name="disposing">マネージ リソースが破棄される場合 true、破棄されない場合は false です。</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region コンポーネント デザイナで生成されたコード

        /// <summary> 
        /// デザイナ サポートに必要なメソッドです。このメソッドの内容を 
        /// コード エディタで変更しないでください。
        /// </summary>
        private void InitializeComponent() {
            this.FontSelectButton = new System.Windows.Forms.Button();
            this.EditorFontDialog = new System.Windows.Forms.FontDialog();
            this.BackColorButton = new System.Windows.Forms.Button();
            this.FontColorButton = new System.Windows.Forms.Button();
            this.EditorColorDialog = new System.Windows.Forms.ColorDialog();
            this.panel = new System.Windows.Forms.Panel();
            this.ShowTabCheckBox = new System.Windows.Forms.CheckBox();
            this.ShowEolCheckBox = new System.Windows.Forms.CheckBox();
            this.ShowSpaceCheckBox = new System.Windows.Forms.CheckBox();
            this.ShowZenSpaceCheckBox = new System.Windows.Forms.CheckBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.listBox1 = new System.Windows.Forms.ListBox();
            this.NewButton = new System.Windows.Forms.Button();
            this.EditButton = new System.Windows.Forms.Button();
            this.DeleteButton = new System.Windows.Forms.Button();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // FontSelectButton
            // 
            this.FontSelectButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FontSelectButton.Location = new System.Drawing.Point(316, 20);
            this.FontSelectButton.Name = "FontSelectButton";
            this.FontSelectButton.Size = new System.Drawing.Size(75, 23);
            this.FontSelectButton.TabIndex = 4;
            this.FontSelectButton.Text = "Font";
            this.FontSelectButton.UseVisualStyleBackColor = true;
            // 
            // BackColorButton
            // 
            this.BackColorButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.BackColorButton.Location = new System.Drawing.Point(316, 77);
            this.BackColorButton.Name = "BackColorButton";
            this.BackColorButton.Size = new System.Drawing.Size(75, 23);
            this.BackColorButton.TabIndex = 3;
            this.BackColorButton.Text = "Color";
            this.BackColorButton.UseVisualStyleBackColor = true;
            // 
            // FontColorButton
            // 
            this.FontColorButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FontColorButton.Location = new System.Drawing.Point(316, 48);
            this.FontColorButton.Name = "FontColorButton";
            this.FontColorButton.Size = new System.Drawing.Size(75, 23);
            this.FontColorButton.TabIndex = 2;
            this.FontColorButton.Text = "Color";
            this.FontColorButton.UseVisualStyleBackColor = true;
            // 
            // panel
            // 
            this.panel.Dock = System.Windows.Forms.DockStyle.Left;
            this.panel.Location = new System.Drawing.Point(5, 17);
            this.panel.Name = "panel";
            this.panel.Size = new System.Drawing.Size(287, 220);
            this.panel.TabIndex = 5;
            // 
            // ShowTabCheckBox
            // 
            this.ShowTabCheckBox.AutoSize = true;
            this.ShowTabCheckBox.Location = new System.Drawing.Point(322, 106);
            this.ShowTabCheckBox.Name = "ShowTabCheckBox";
            this.ShowTabCheckBox.Size = new System.Drawing.Size(43, 16);
            this.ShowTabCheckBox.TabIndex = 6;
            this.ShowTabCheckBox.Text = "Tab";
            this.ShowTabCheckBox.UseVisualStyleBackColor = true;
            // 
            // ShowEolCheckBox
            // 
            this.ShowEolCheckBox.AutoSize = true;
            this.ShowEolCheckBox.Location = new System.Drawing.Point(322, 128);
            this.ShowEolCheckBox.Name = "ShowEolCheckBox";
            this.ShowEolCheckBox.Size = new System.Drawing.Size(40, 16);
            this.ShowEolCheckBox.TabIndex = 7;
            this.ShowEolCheckBox.Text = "Eol";
            this.ShowEolCheckBox.UseVisualStyleBackColor = true;
            // 
            // ShowSpaceCheckBox
            // 
            this.ShowSpaceCheckBox.AutoSize = true;
            this.ShowSpaceCheckBox.Location = new System.Drawing.Point(322, 150);
            this.ShowSpaceCheckBox.Name = "ShowSpaceCheckBox";
            this.ShowSpaceCheckBox.Size = new System.Drawing.Size(55, 16);
            this.ShowSpaceCheckBox.TabIndex = 8;
            this.ShowSpaceCheckBox.Text = "Space";
            this.ShowSpaceCheckBox.UseVisualStyleBackColor = true;
            // 
            // ShowZenSpaceCheckBox
            // 
            this.ShowZenSpaceCheckBox.AutoSize = true;
            this.ShowZenSpaceCheckBox.Location = new System.Drawing.Point(322, 172);
            this.ShowZenSpaceCheckBox.Name = "ShowZenSpaceCheckBox";
            this.ShowZenSpaceCheckBox.Size = new System.Drawing.Size(74, 16);
            this.ShowZenSpaceCheckBox.TabIndex = 9;
            this.ShowZenSpaceCheckBox.Text = "ZenSpace";
            this.ShowZenSpaceCheckBox.UseVisualStyleBackColor = true;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.panel);
            this.groupBox1.Controls.Add(this.ShowZenSpaceCheckBox);
            this.groupBox1.Controls.Add(this.ShowSpaceCheckBox);
            this.groupBox1.Controls.Add(this.ShowEolCheckBox);
            this.groupBox1.Controls.Add(this.ShowTabCheckBox);
            this.groupBox1.Controls.Add(this.BackColorButton);
            this.groupBox1.Controls.Add(this.FontColorButton);
            this.groupBox1.Controls.Add(this.FontSelectButton);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Padding = new System.Windows.Forms.Padding(5);
            this.groupBox1.Size = new System.Drawing.Size(415, 242);
            this.groupBox1.TabIndex = 10;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "groupBox1";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.DeleteButton);
            this.groupBox2.Controls.Add(this.EditButton);
            this.groupBox2.Controls.Add(this.NewButton);
            this.groupBox2.Controls.Add(this.listBox1);
            this.groupBox2.Location = new System.Drawing.Point(0, 248);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(375, 203);
            this.groupBox2.TabIndex = 11;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "groupBox2";
            // 
            // listBox1
            // 
            this.listBox1.FormattingEnabled = true;
            this.listBox1.ItemHeight = 12;
            this.listBox1.Location = new System.Drawing.Point(6, 18);
            this.listBox1.Name = "listBox1";
            this.listBox1.Size = new System.Drawing.Size(282, 172);
            this.listBox1.TabIndex = 0;
            // 
            // NewButton
            // 
            this.NewButton.Location = new System.Drawing.Point(294, 18);
            this.NewButton.Name = "NewButton";
            this.NewButton.Size = new System.Drawing.Size(75, 23);
            this.NewButton.TabIndex = 1;
            this.NewButton.Text = "New";
            this.NewButton.UseVisualStyleBackColor = true;
            // 
            // EditButton
            // 
            this.EditButton.Location = new System.Drawing.Point(294, 47);
            this.EditButton.Name = "EditButton";
            this.EditButton.Size = new System.Drawing.Size(75, 23);
            this.EditButton.TabIndex = 2;
            this.EditButton.Text = "Edit";
            this.EditButton.UseVisualStyleBackColor = true;
            // 
            // DeleteButton
            // 
            this.DeleteButton.Location = new System.Drawing.Point(294, 167);
            this.DeleteButton.Name = "DeleteButton";
            this.DeleteButton.Size = new System.Drawing.Size(75, 23);
            this.DeleteButton.TabIndex = 3;
            this.DeleteButton.Text = "Delete";
            this.DeleteButton.UseVisualStyleBackColor = true;
            // 
            // EditorConfig
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoScroll = true;
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Name = "EditorConfig";
            this.Size = new System.Drawing.Size(415, 463);
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button FontSelectButton;
        private System.Windows.Forms.FontDialog EditorFontDialog;
        private System.Windows.Forms.Button BackColorButton;
        private System.Windows.Forms.Button FontColorButton;
        private System.Windows.Forms.ColorDialog EditorColorDialog;
        private System.Windows.Forms.Panel panel;
        private System.Windows.Forms.CheckBox ShowTabCheckBox;
        private System.Windows.Forms.CheckBox ShowEolCheckBox;
        private System.Windows.Forms.CheckBox ShowSpaceCheckBox;
        private System.Windows.Forms.CheckBox ShowZenSpaceCheckBox;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.ListBox listBox1;
        private System.Windows.Forms.Button EditButton;
        private System.Windows.Forms.Button NewButton;
        private System.Windows.Forms.Button DeleteButton;
    }
}
