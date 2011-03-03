using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using Sgry.Azuki.WinForms;

namespace wiki {

    public class ImeOnOffEventArgs : EventArgs {
        public ImeMode Mode;
    }

    delegate void ImeOnOffEventHandler(object sender, ImeOnOffEventArgs e); 

    class AzukiControlEx : AzukiControl {

        public event ImeOnOffEventHandler ImeOnOffEvent;

        [DllImport("imm32.dll")]
        static extern int ImmGetVirtualKey(int hwnd);

        const int WM_KEYDOWN = 0x100;
        const int VK_PROCESSKEY = 0xE5;

        const int VK_A = 0x41;
        const int VK_Z = 0x5A;

        int prekey = 'a';
        DateTime st = DateTime.Now;

        Keys prekeys = Keys.A;

        public override bool PreProcessMessage(ref Message msg) {

            if (msg.Msg.Equals(WM_KEYDOWN)) {
                if (msg.WParam.ToInt32().Equals(VK_PROCESSKEY)) {
                    int key = ImmGetVirtualKey((int)this.Handle);

                    if (VK_A <= key && key <= VK_Z) {

                        DateTime bk = st;
                        int prekeybk = prekey;

                        prekey = key;
                        st = DateTime.Now;
                        if (key == prekeybk && (DateTime.Now - bk).TotalMilliseconds < 200) {
                            this.ImeMode = this.ImeMode == ImeMode.On || this.ImeMode == ImeMode.Hiragana ? ImeMode.Off : ImeMode.On;
                            if (ImeOnOffEvent != null) {
                                ImeOnOffEvent(this, new ImeOnOffEventArgs() { Mode = this.ImeMode });
                            }
                            
                            return true;
                        }
                    }
                }
                else {
                    Keys keyCode = (Keys)(int)msg.WParam & Keys.KeyCode;
                    //if (keyCode == Keys.Up || keyCode == Keys.Down)
                    //    return true;

                    if (Keys.A <= keyCode && keyCode <= Keys.Z) {
                        DateTime bk = st;
                        Keys prekeybk = prekeys;

                        prekeys = keyCode;
                        st = DateTime.Now;

                        if (keyCode == prekeybk && (DateTime.Now - bk).TotalMilliseconds < 200) {
                            //var tic = DateTime.Now - bk;
                            //Console.WriteLine(tic.TotalMilliseconds);
                            //if (tic.TotalMilliseconds < 200) {
                            this.ImeMode = this.ImeMode == ImeMode.On || this.ImeMode == ImeMode.Hiragana ? ImeMode.Off : ImeMode.On;
                            if (ImeOnOffEvent != null) {
                                ImeOnOffEvent(this, new ImeOnOffEventArgs() { Mode = this.ImeMode });
                            }
                            return true;
                            //}
                        }
                    }
                }
            }

            return base.PreProcessMessage(ref msg);
        }
    }
}
