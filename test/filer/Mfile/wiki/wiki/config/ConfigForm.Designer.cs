namespace wiki {
    partial class ConfigForm {
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
            System.Windows.Forms.TreeNode treeNode1 = new System.Windows.Forms.TreeNode("Main");
            System.Windows.Forms.TreeNode treeNode2 = new System.Windows.Forms.TreeNode("ノード2");
            System.Windows.Forms.TreeNode treeNode3 = new System.Windows.Forms.TreeNode("Editor", new System.Windows.Forms.TreeNode[] {
            treeNode2});
            this.panel1 = new System.Windows.Forms.Panel();
            this.CancelButton = new System.Windows.Forms.Button();
            this.OKButton = new System.Windows.Forms.Button();
            this.ConfigTreeView = new System.Windows.Forms.TreeView();
            this.ConfigPanel = new System.Windows.Forms.Panel();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.CancelButton);
            this.panel1.Controls.Add(this.OKButton);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel1.Location = new System.Drawing.Point(0, 303);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(398, 30);
            this.panel1.TabIndex = 1;
            // 
            // CancelButton
            // 
            this.CancelButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.CancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.CancelButton.Location = new System.Drawing.Point(318, 4);
            this.CancelButton.Name = "CancelButton";
            this.CancelButton.Size = new System.Drawing.Size(76, 23);
            this.CancelButton.TabIndex = 1;
            this.CancelButton.Text = "Cancel";
            this.CancelButton.UseVisualStyleBackColor = true;
            // 
            // OKButton
            // 
            this.OKButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.OKButton.Location = new System.Drawing.Point(237, 4);
            this.OKButton.Name = "OKButton";
            this.OKButton.Size = new System.Drawing.Size(76, 23);
            this.OKButton.TabIndex = 0;
            this.OKButton.Text = "OK";
            this.OKButton.UseVisualStyleBackColor = true;
            // 
            // ConfigTreeView
            // 
            this.ConfigTreeView.Dock = System.Windows.Forms.DockStyle.Left;
            this.ConfigTreeView.Location = new System.Drawing.Point(0, 0);
            this.ConfigTreeView.Name = "ConfigTreeView";
            treeNode1.Name = "MainNode";
            treeNode1.Text = "Main";
            treeNode2.Name = "ノード2";
            treeNode2.Text = "ノード2";
            treeNode3.Name = "EditorNode";
            treeNode3.Text = "Editor";
            this.ConfigTreeView.Nodes.AddRange(new System.Windows.Forms.TreeNode[] {
            treeNode1,
            treeNode3});
            this.ConfigTreeView.Size = new System.Drawing.Size(121, 303);
            this.ConfigTreeView.TabIndex = 3;
            // 
            // ConfigPanel
            // 
            this.ConfigPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ConfigPanel.Location = new System.Drawing.Point(121, 0);
            this.ConfigPanel.Name = "ConfigPanel";
            this.ConfigPanel.Size = new System.Drawing.Size(277, 303);
            this.ConfigPanel.TabIndex = 4;
            // 
            // ConfigForm
            // 
            this.AcceptButton = this.OKButton;
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(398, 333);
            this.Controls.Add(this.ConfigPanel);
            this.Controls.Add(this.ConfigTreeView);
            this.Controls.Add(this.panel1);
            this.Name = "ConfigForm";
            this.Text = "ConfigForm";
            this.panel1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button CancelButton;
        private System.Windows.Forms.Button OKButton;
        private System.Windows.Forms.TreeView ConfigTreeView;
        private System.Windows.Forms.Panel ConfigPanel;

    }
}