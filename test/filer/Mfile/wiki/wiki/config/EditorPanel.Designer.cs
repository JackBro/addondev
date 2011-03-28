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
            this.label1 = new System.Windows.Forms.Label();
            this.FontSelectButton = new System.Windows.Forms.Button();
            this.FontTextBox = new System.Windows.Forms.TextBox();
            this.EditorFontDialog = new System.Windows.Forms.FontDialog();
            this.FontGroupBox = new System.Windows.Forms.GroupBox();
            this.MarkGroupBox = new System.Windows.Forms.GroupBox();
            this.checkBox4 = new System.Windows.Forms.CheckBox();
            this.checkBox3 = new System.Windows.Forms.CheckBox();
            this.checkBox2 = new System.Windows.Forms.CheckBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.FontGroupBox.SuspendLayout();
            this.MarkGroupBox.SuspendLayout();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(29, 40);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(28, 12);
            this.label1.TabIndex = 5;
            this.label1.Text = "Font";
            // 
            // FontSelectButton
            // 
            this.FontSelectButton.Location = new System.Drawing.Point(197, 35);
            this.FontSelectButton.Name = "FontSelectButton";
            this.FontSelectButton.Size = new System.Drawing.Size(75, 23);
            this.FontSelectButton.TabIndex = 4;
            this.FontSelectButton.Text = "button1";
            this.FontSelectButton.UseVisualStyleBackColor = true;
            // 
            // FontTextBox
            // 
            this.FontTextBox.Location = new System.Drawing.Point(63, 33);
            this.FontTextBox.Name = "FontTextBox";
            this.FontTextBox.Size = new System.Drawing.Size(100, 19);
            this.FontTextBox.TabIndex = 3;
            // 
            // FontGroupBox
            // 
            this.FontGroupBox.Controls.Add(this.label1);
            this.FontGroupBox.Controls.Add(this.FontTextBox);
            this.FontGroupBox.Controls.Add(this.FontSelectButton);
            this.FontGroupBox.Location = new System.Drawing.Point(15, 18);
            this.FontGroupBox.Name = "FontGroupBox";
            this.FontGroupBox.Size = new System.Drawing.Size(278, 81);
            this.FontGroupBox.TabIndex = 6;
            this.FontGroupBox.TabStop = false;
            this.FontGroupBox.Text = "Font";
            // 
            // MarkGroupBox
            // 
            this.MarkGroupBox.Controls.Add(this.checkBox4);
            this.MarkGroupBox.Controls.Add(this.checkBox3);
            this.MarkGroupBox.Controls.Add(this.checkBox2);
            this.MarkGroupBox.Controls.Add(this.checkBox1);
            this.MarkGroupBox.Location = new System.Drawing.Point(15, 105);
            this.MarkGroupBox.Name = "MarkGroupBox";
            this.MarkGroupBox.Size = new System.Drawing.Size(334, 70);
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
            this.checkBox4.Size = new System.Drawing.Size(80, 52);
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
            this.checkBox3.Size = new System.Drawing.Size(80, 52);
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
            this.checkBox2.Size = new System.Drawing.Size(80, 52);
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
            this.checkBox1.Size = new System.Drawing.Size(80, 52);
            this.checkBox1.TabIndex = 0;
            this.checkBox1.Text = "checkBox1";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // EditorPanel
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.MarkGroupBox);
            this.Controls.Add(this.FontGroupBox);
            this.Name = "EditorPanel";
            this.Size = new System.Drawing.Size(415, 241);
            this.FontGroupBox.ResumeLayout(false);
            this.FontGroupBox.PerformLayout();
            this.MarkGroupBox.ResumeLayout(false);
            this.MarkGroupBox.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button FontSelectButton;
        private System.Windows.Forms.TextBox FontTextBox;
        private System.Windows.Forms.FontDialog EditorFontDialog;
        private System.Windows.Forms.GroupBox FontGroupBox;
        private System.Windows.Forms.GroupBox MarkGroupBox;
        private System.Windows.Forms.CheckBox checkBox2;
        private System.Windows.Forms.CheckBox checkBox1;
        private System.Windows.Forms.CheckBox checkBox4;
        private System.Windows.Forms.CheckBox checkBox3;
    }
}
