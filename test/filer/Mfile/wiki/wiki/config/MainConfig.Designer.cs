﻿namespace wiki.config {
    partial class MainConfig {
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
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.PortSearchButton = new System.Windows.Forms.Button();
            this.PortTextBox = new System.Windows.Forms.TextBox();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.PortSearchButton);
            this.groupBox1.Controls.Add(this.PortTextBox);
            this.groupBox1.Location = new System.Drawing.Point(29, 19);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(352, 100);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "groupBox1";
            // 
            // PortSearchButton
            // 
            this.PortSearchButton.Location = new System.Drawing.Point(134, 33);
            this.PortSearchButton.Name = "PortSearchButton";
            this.PortSearchButton.Size = new System.Drawing.Size(50, 19);
            this.PortSearchButton.TabIndex = 1;
            this.PortSearchButton.Text = "Search";
            this.PortSearchButton.UseVisualStyleBackColor = true;
            // 
            // PortTextBox
            // 
            this.PortTextBox.Location = new System.Drawing.Point(6, 33);
            this.PortTextBox.Name = "PortTextBox";
            this.PortTextBox.Size = new System.Drawing.Size(100, 19);
            this.PortTextBox.TabIndex = 0;
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            // 
            // MainConfig
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.groupBox1);
            this.Name = "MainConfig";
            this.Size = new System.Drawing.Size(384, 301);
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.TextBox PortTextBox;
        private System.Windows.Forms.Button PortSearchButton;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
    }
}
