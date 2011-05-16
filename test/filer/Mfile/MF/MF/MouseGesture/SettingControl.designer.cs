namespace MouseGesture_Net
{
    partial class SettingControl
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

        #region コンポーネント デザイナで生成されたコード

        /// <summary> 
        /// デザイナ サポートに必要なメソッドです。このメソッドの内容を 
        /// コード エディタで変更しないでください。
        /// </summary>
        private void InitializeComponent()
        {
            this.GesturelistView = new System.Windows.Forms.ListView();
            this.GeaturetextBox = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.panel1 = new System.Windows.Forms.Panel();
            this.OKbutton = new System.Windows.Forms.Button();
            this.Clearbutton = new System.Windows.Forms.Button();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // GesturelistView
            // 
            this.GesturelistView.Dock = System.Windows.Forms.DockStyle.Top;
            this.GesturelistView.FullRowSelect = true;
            this.GesturelistView.HideSelection = false;
            this.GesturelistView.Location = new System.Drawing.Point(0, 0);
            this.GesturelistView.MultiSelect = false;
            this.GesturelistView.Name = "GesturelistView";
            this.GesturelistView.Size = new System.Drawing.Size(289, 252);
            this.GesturelistView.TabIndex = 0;
            this.GesturelistView.UseCompatibleStateImageBehavior = false;
            this.GesturelistView.View = System.Windows.Forms.View.Details;
            // 
            // GeaturetextBox
            // 
            this.GeaturetextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.GeaturetextBox.Location = new System.Drawing.Point(5, 18);
            this.GeaturetextBox.Name = "GeaturetextBox";
            this.GeaturetextBox.Size = new System.Drawing.Size(191, 19);
            this.GeaturetextBox.TabIndex = 6;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 3);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(45, 12);
            this.label1.TabIndex = 9;
            this.label1.Text = "Gesture";
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.OKbutton);
            this.panel1.Controls.Add(this.Clearbutton);
            this.panel1.Controls.Add(this.label1);
            this.panel1.Controls.Add(this.GeaturetextBox);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel1.Location = new System.Drawing.Point(0, 252);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(289, 55);
            this.panel1.TabIndex = 10;
            // 
            // OKbutton
            // 
            this.OKbutton.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.OKbutton.Location = new System.Drawing.Point(246, 18);
            this.OKbutton.Name = "OKbutton";
            this.OKbutton.Size = new System.Drawing.Size(40, 19);
            this.OKbutton.TabIndex = 11;
            this.OKbutton.Text = "OK";
            this.OKbutton.UseVisualStyleBackColor = true;
            this.OKbutton.Click += new System.EventHandler(this.OKbutton_Click);
            // 
            // Clearbutton
            // 
            this.Clearbutton.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.Clearbutton.Location = new System.Drawing.Point(200, 18);
            this.Clearbutton.Name = "Clearbutton";
            this.Clearbutton.Size = new System.Drawing.Size(40, 19);
            this.Clearbutton.TabIndex = 10;
            this.Clearbutton.Text = "Clear";
            this.Clearbutton.UseVisualStyleBackColor = true;
            this.Clearbutton.Click += new System.EventHandler(this.Clearbutton_Click);
            // 
            // SettingControl
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.GesturelistView);
            this.Name = "SettingControl";
            this.Size = new System.Drawing.Size(289, 307);
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListView GesturelistView;
        private System.Windows.Forms.TextBox GeaturetextBox;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button Clearbutton;
        private System.Windows.Forms.Button OKbutton;
    }
}
