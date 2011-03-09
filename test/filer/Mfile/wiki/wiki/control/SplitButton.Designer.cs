namespace wiki.control {
    partial class SplitButton {
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
            this.ToggleButton = new System.Windows.Forms.Button();
            this.DropDownButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // ToggleButton
            // 
            this.ToggleButton.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ToggleButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.ToggleButton.Location = new System.Drawing.Point(0, 0);
            this.ToggleButton.Name = "ToggleButton";
            this.ToggleButton.Size = new System.Drawing.Size(39, 32);
            this.ToggleButton.TabIndex = 0;
            this.ToggleButton.Text = "button1";
            this.ToggleButton.UseVisualStyleBackColor = true;
            // 
            // DropDownButton
            // 
            this.DropDownButton.Dock = System.Windows.Forms.DockStyle.Right;
            this.DropDownButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.DropDownButton.Location = new System.Drawing.Point(39, 0);
            this.DropDownButton.Name = "DropDownButton";
            this.DropDownButton.Size = new System.Drawing.Size(10, 32);
            this.DropDownButton.TabIndex = 1;
            this.DropDownButton.UseVisualStyleBackColor = true;
            // 
            // SplitButton
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.ToggleButton);
            this.Controls.Add(this.DropDownButton);
            this.Name = "SplitButton";
            this.Size = new System.Drawing.Size(49, 32);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button DropDownButton;
        internal System.Windows.Forms.Button ToggleButton;
    }
}
