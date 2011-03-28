namespace wiki {
    partial class EditorPanel {
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
            this.FontNameLabel = new System.Windows.Forms.Label();
            this.FontSelectButton = new System.Windows.Forms.Button();
            this.EditorFontDialog = new System.Windows.Forms.FontDialog();
            this.FontGroupBox = new System.Windows.Forms.GroupBox();
            this.MarkGroupBox = new System.Windows.Forms.GroupBox();
            this.checkBox4 = new System.Windows.Forms.CheckBox();
            this.checkBox3 = new System.Windows.Forms.CheckBox();
            this.checkBox2 = new System.Windows.Forms.CheckBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.ColorGroupBox = new System.Windows.Forms.GroupBox();
            this.EditorColorDialog = new System.Windows.Forms.ColorDialog();
            this.FonColorLabel = new System.Windows.Forms.Label();
            this.BackColorLabel = new System.Windows.Forms.Label();
            this.FontColorButton = new System.Windows.Forms.Button();
            this.BackColorButton = new System.Windows.Forms.Button();
            this.FontGroupBox.SuspendLayout();
            this.MarkGroupBox.SuspendLayout();
            this.ColorGroupBox.SuspendLayout();
            this.SuspendLayout();
            // 
            // FontNameLabel
            // 
            this.FontNameLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.FontNameLabel.Location = new System.Drawing.Point(9, 15);
            this.FontNameLabel.Name = "FontNameLabel";
            this.FontNameLabel.Size = new System.Drawing.Size(237, 23);
            this.FontNameLabel.TabIndex = 5;
            this.FontNameLabel.Text = "Font";
            this.FontNameLabel.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // FontSelectButton
            // 
            this.FontSelectButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FontSelectButton.Location = new System.Drawing.Point(253, 15);
            this.FontSelectButton.Name = "FontSelectButton";
            this.FontSelectButton.Size = new System.Drawing.Size(75, 23);
            this.FontSelectButton.TabIndex = 4;
            this.FontSelectButton.Text = "Font";
            this.FontSelectButton.UseVisualStyleBackColor = true;
            // 
            // FontGroupBox
            // 
            this.FontGroupBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.FontGroupBox.Controls.Add(this.FontNameLabel);
            this.FontGroupBox.Controls.Add(this.FontSelectButton);
            this.FontGroupBox.Location = new System.Drawing.Point(15, 18);
            this.FontGroupBox.Name = "FontGroupBox";
            this.FontGroupBox.Size = new System.Drawing.Size(334, 46);
            this.FontGroupBox.TabIndex = 6;
            this.FontGroupBox.TabStop = false;
            this.FontGroupBox.Text = "Font";
            // 
            // MarkGroupBox
            // 
            this.MarkGroupBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.MarkGroupBox.Controls.Add(this.checkBox4);
            this.MarkGroupBox.Controls.Add(this.checkBox3);
            this.MarkGroupBox.Controls.Add(this.checkBox2);
            this.MarkGroupBox.Controls.Add(this.checkBox1);
            this.MarkGroupBox.Location = new System.Drawing.Point(15, 70);
            this.MarkGroupBox.Name = "MarkGroupBox";
            this.MarkGroupBox.Size = new System.Drawing.Size(334, 48);
            this.MarkGroupBox.TabIndex = 7;
            this.MarkGroupBox.TabStop = false;
            this.MarkGroupBox.Text = "Mark";
            // 
            // checkBox4
            // 
            this.checkBox4.AutoSize = true;
            this.checkBox4.Dock = System.Windows.Forms.DockStyle.Left;
            this.checkBox4.Location = new System.Drawing.Point(243, 15);
            this.checkBox4.Name = "checkBox4";
            this.checkBox4.Size = new System.Drawing.Size(80, 30);
            this.checkBox4.TabIndex = 3;
            this.checkBox4.Text = "checkBox4";
            this.checkBox4.UseVisualStyleBackColor = true;
            // 
            // checkBox3
            // 
            this.checkBox3.AutoSize = true;
            this.checkBox3.Dock = System.Windows.Forms.DockStyle.Left;
            this.checkBox3.Location = new System.Drawing.Point(163, 15);
            this.checkBox3.Name = "checkBox3";
            this.checkBox3.Size = new System.Drawing.Size(80, 30);
            this.checkBox3.TabIndex = 2;
            this.checkBox3.Text = "checkBox3";
            this.checkBox3.UseVisualStyleBackColor = true;
            // 
            // checkBox2
            // 
            this.checkBox2.AutoSize = true;
            this.checkBox2.Dock = System.Windows.Forms.DockStyle.Left;
            this.checkBox2.Location = new System.Drawing.Point(83, 15);
            this.checkBox2.Name = "checkBox2";
            this.checkBox2.Size = new System.Drawing.Size(80, 30);
            this.checkBox2.TabIndex = 1;
            this.checkBox2.Text = "checkBox2";
            this.checkBox2.UseVisualStyleBackColor = true;
            // 
            // checkBox1
            // 
            this.checkBox1.AutoSize = true;
            this.checkBox1.Dock = System.Windows.Forms.DockStyle.Left;
            this.checkBox1.Location = new System.Drawing.Point(3, 15);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.Size = new System.Drawing.Size(80, 30);
            this.checkBox1.TabIndex = 0;
            this.checkBox1.Text = "checkBox1";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // ColorGroupBox
            // 
            this.ColorGroupBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.ColorGroupBox.Controls.Add(this.BackColorButton);
            this.ColorGroupBox.Controls.Add(this.FontColorButton);
            this.ColorGroupBox.Controls.Add(this.BackColorLabel);
            this.ColorGroupBox.Controls.Add(this.FonColorLabel);
            this.ColorGroupBox.Location = new System.Drawing.Point(15, 124);
            this.ColorGroupBox.Name = "ColorGroupBox";
            this.ColorGroupBox.Size = new System.Drawing.Size(334, 75);
            this.ColorGroupBox.TabIndex = 8;
            this.ColorGroupBox.TabStop = false;
            this.ColorGroupBox.Text = "Color";
            // 
            // FonColorLabel
            // 
            this.FonColorLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.FonColorLabel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.FonColorLabel.Location = new System.Drawing.Point(9, 15);
            this.FonColorLabel.Name = "FonColorLabel";
            this.FonColorLabel.Size = new System.Drawing.Size(238, 23);
            this.FonColorLabel.TabIndex = 0;
            // 
            // BackColorLabel
            // 
            this.BackColorLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.BackColorLabel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.BackColorLabel.Location = new System.Drawing.Point(8, 43);
            this.BackColorLabel.Name = "BackColorLabel";
            this.BackColorLabel.Size = new System.Drawing.Size(239, 23);
            this.BackColorLabel.TabIndex = 1;
            // 
            // FontColorButton
            // 
            this.FontColorButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FontColorButton.Location = new System.Drawing.Point(253, 14);
            this.FontColorButton.Name = "FontColorButton";
            this.FontColorButton.Size = new System.Drawing.Size(75, 23);
            this.FontColorButton.TabIndex = 2;
            this.FontColorButton.Text = "Color";
            this.FontColorButton.UseVisualStyleBackColor = true;
            // 
            // BackColorButton
            // 
            this.BackColorButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.BackColorButton.Location = new System.Drawing.Point(253, 41);
            this.BackColorButton.Name = "BackColorButton";
            this.BackColorButton.Size = new System.Drawing.Size(75, 23);
            this.BackColorButton.TabIndex = 3;
            this.BackColorButton.Text = "Color";
            this.BackColorButton.UseVisualStyleBackColor = true;
            // 
            // EditorPanel
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.ColorGroupBox);
            this.Controls.Add(this.MarkGroupBox);
            this.Controls.Add(this.FontGroupBox);
            this.Name = "EditorPanel";
            this.Size = new System.Drawing.Size(361, 228);
            this.FontGroupBox.ResumeLayout(false);
            this.MarkGroupBox.ResumeLayout(false);
            this.MarkGroupBox.PerformLayout();
            this.ColorGroupBox.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label FontNameLabel;
        private System.Windows.Forms.Button FontSelectButton;
        private System.Windows.Forms.FontDialog EditorFontDialog;
        private System.Windows.Forms.GroupBox FontGroupBox;
        private System.Windows.Forms.GroupBox MarkGroupBox;
        private System.Windows.Forms.CheckBox checkBox2;
        private System.Windows.Forms.CheckBox checkBox1;
        private System.Windows.Forms.CheckBox checkBox4;
        private System.Windows.Forms.CheckBox checkBox3;
        private System.Windows.Forms.GroupBox ColorGroupBox;
        private System.Windows.Forms.Button BackColorButton;
        private System.Windows.Forms.Button FontColorButton;
        private System.Windows.Forms.Label BackColorLabel;
        private System.Windows.Forms.Label FonColorLabel;
        private System.Windows.Forms.ColorDialog EditorColorDialog;
    }
}
