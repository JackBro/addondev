namespace wiki.control {
    partial class SearchControl {
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
            this.SearchComboBox = new System.Windows.Forms.ComboBox();
            this.NextButton = new System.Windows.Forms.Button();
            this.PrevButton = new System.Windows.Forms.Button();
            this.MIgemoCheckBox = new System.Windows.Forms.CheckBox();
            this.CloseButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // SearchComboBox
            // 
            this.SearchComboBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.SearchComboBox.FormattingEnabled = true;
            this.SearchComboBox.Location = new System.Drawing.Point(25, 3);
            this.SearchComboBox.Name = "SearchComboBox";
            this.SearchComboBox.Size = new System.Drawing.Size(153, 20);
            this.SearchComboBox.TabIndex = 0;
            // 
            // NextButton
            // 
            this.NextButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.NextButton.Location = new System.Drawing.Point(183, 3);
            this.NextButton.Name = "NextButton";
            this.NextButton.Size = new System.Drawing.Size(56, 23);
            this.NextButton.TabIndex = 1;
            this.NextButton.Text = "Next";
            this.NextButton.UseVisualStyleBackColor = true;
            // 
            // PrevButton
            // 
            this.PrevButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.PrevButton.Location = new System.Drawing.Point(245, 3);
            this.PrevButton.Name = "PrevButton";
            this.PrevButton.Size = new System.Drawing.Size(56, 23);
            this.PrevButton.TabIndex = 2;
            this.PrevButton.Text = "Prev";
            this.PrevButton.UseVisualStyleBackColor = true;
            // 
            // MIgemoCheckBox
            // 
            this.MIgemoCheckBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.MIgemoCheckBox.AutoSize = true;
            this.MIgemoCheckBox.Location = new System.Drawing.Point(307, 7);
            this.MIgemoCheckBox.Name = "MIgemoCheckBox";
            this.MIgemoCheckBox.Size = new System.Drawing.Size(63, 16);
            this.MIgemoCheckBox.TabIndex = 3;
            this.MIgemoCheckBox.Text = "Migemo";
            this.MIgemoCheckBox.UseVisualStyleBackColor = true;
            // 
            // CloseButton
            // 
            this.CloseButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.CloseButton.Location = new System.Drawing.Point(3, 3);
            this.CloseButton.Name = "CloseButton";
            this.CloseButton.Size = new System.Drawing.Size(20, 20);
            this.CloseButton.TabIndex = 5;
            this.CloseButton.Text = "x";
            this.CloseButton.UseVisualStyleBackColor = true;
            // 
            // SearchControl
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.CloseButton);
            this.Controls.Add(this.PrevButton);
            this.Controls.Add(this.NextButton);
            this.Controls.Add(this.MIgemoCheckBox);
            this.Controls.Add(this.SearchComboBox);
            this.Name = "SearchControl";
            this.Size = new System.Drawing.Size(376, 30);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        internal System.Windows.Forms.Button CloseButton;
        internal System.Windows.Forms.ComboBox SearchComboBox;
        internal System.Windows.Forms.Button NextButton;
        internal System.Windows.Forms.Button PrevButton;
        internal System.Windows.Forms.CheckBox MIgemoCheckBox;
    }
}
