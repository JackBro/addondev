namespace wiki {
    partial class DateTimeForm {
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

        #region Windows フォーム デザイナで生成されたコード

        /// <summary>
        /// デザイナ サポートに必要なメソッドです。このメソッドの内容を
        /// コード エディタで変更しないでください。
        /// </summary>
        private void InitializeComponent() {
            this.dateTimePicker = new System.Windows.Forms.DateTimePicker();
            this.okbutton = new System.Windows.Forms.Button();
            this.HourDomainUpDown = new System.Windows.Forms.DomainUpDown();
            this.MinDomainUpDown = new System.Windows.Forms.DomainUpDown();
            this.SecDomainUpDown = new System.Windows.Forms.DomainUpDown();
            this.cancelbutton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // dateTimePicker
            // 
            this.dateTimePicker.Format = System.Windows.Forms.DateTimePickerFormat.Short;
            this.dateTimePicker.Location = new System.Drawing.Point(3, 12);
            this.dateTimePicker.Name = "dateTimePicker";
            this.dateTimePicker.Size = new System.Drawing.Size(109, 19);
            this.dateTimePicker.TabIndex = 0;
            // 
            // okbutton
            // 
            this.okbutton.Location = new System.Drawing.Point(171, 51);
            this.okbutton.Name = "okbutton";
            this.okbutton.Size = new System.Drawing.Size(75, 23);
            this.okbutton.TabIndex = 2;
            this.okbutton.Text = "OK";
            this.okbutton.UseVisualStyleBackColor = true;
            // 
            // HourDomainUpDown
            // 
            this.HourDomainUpDown.Location = new System.Drawing.Point(132, 13);
            this.HourDomainUpDown.Name = "HourDomainUpDown";
            this.HourDomainUpDown.Size = new System.Drawing.Size(61, 19);
            this.HourDomainUpDown.TabIndex = 3;
            this.HourDomainUpDown.Text = "hour";
            // 
            // MinDomainUpDown
            // 
            this.MinDomainUpDown.Location = new System.Drawing.Point(199, 13);
            this.MinDomainUpDown.Name = "MinDomainUpDown";
            this.MinDomainUpDown.Size = new System.Drawing.Size(61, 19);
            this.MinDomainUpDown.TabIndex = 4;
            this.MinDomainUpDown.Text = "min";
            // 
            // SecDomainUpDown
            // 
            this.SecDomainUpDown.Location = new System.Drawing.Point(266, 12);
            this.SecDomainUpDown.Name = "SecDomainUpDown";
            this.SecDomainUpDown.Size = new System.Drawing.Size(61, 19);
            this.SecDomainUpDown.TabIndex = 5;
            this.SecDomainUpDown.Text = "sec";
            // 
            // cancelbutton
            // 
            this.cancelbutton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelbutton.Location = new System.Drawing.Point(252, 51);
            this.cancelbutton.Name = "cancelbutton";
            this.cancelbutton.Size = new System.Drawing.Size(75, 23);
            this.cancelbutton.TabIndex = 6;
            this.cancelbutton.Text = "Cancel";
            this.cancelbutton.UseVisualStyleBackColor = true;
            // 
            // DateTimeForm
            // 
            this.AcceptButton = this.okbutton;
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.cancelbutton;
            this.ClientSize = new System.Drawing.Size(332, 82);
            this.Controls.Add(this.cancelbutton);
            this.Controls.Add(this.SecDomainUpDown);
            this.Controls.Add(this.MinDomainUpDown);
            this.Controls.Add(this.HourDomainUpDown);
            this.Controls.Add(this.okbutton);
            this.Controls.Add(this.dateTimePicker);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.Name = "DateTimeForm";
            this.Text = "DateTimeForm";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DateTimePicker dateTimePicker;
        private System.Windows.Forms.Button okbutton;
        private System.Windows.Forms.DomainUpDown HourDomainUpDown;
        private System.Windows.Forms.DomainUpDown MinDomainUpDown;
        private System.Windows.Forms.DomainUpDown SecDomainUpDown;
        private System.Windows.Forms.Button cancelbutton;
    }
}