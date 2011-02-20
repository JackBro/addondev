using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace wiki {
    public partial class DateTimeForm : Form {

        private DateTime _time;
        public DateTime Time {
            get { return _time; }
            set {
                _time = value;

                dateTimePicker.Text = _time.ToShortDateString();

                HourDomainUpDown.SelectedIndex = _time.Hour;
                MinDomainUpDown.SelectedIndex = _time.Minute;
                SecDomainUpDown.SelectedIndex = _time.Second;
            }
        }

        private Dictionary<Object, List<int>> RangDic = new Dictionary<Object, List<int>>();

        public DateTimeForm() {
            InitializeComponent();

            RangDic.Add(HourDomainUpDown, new List<int>() { 0, 23 });
            RangDic.Add(MinDomainUpDown, new List<int>() { 0, 59 });
            RangDic.Add(SecDomainUpDown, new List<int>() { 0, 59 });
           
            for (int i = 0; i < 24; i++) {
                HourDomainUpDown.Items.Add(string.Format("{0,0:D2}", i));
            }
            for (int i = 0; i < 60; i++) {
                MinDomainUpDown.Items.Add(string.Format("{0,0:D2}", i));
            }

            for (int i = 0; i < 60; i++) {
                SecDomainUpDown.Items.Add(string.Format("{0,0:D2}", i));
            }

            HourDomainUpDown.KeyPress += new KeyPressEventHandler(DomainUpDown_KeyPress);
            MinDomainUpDown.KeyPress += new KeyPressEventHandler(DomainUpDown_KeyPress);
            SecDomainUpDown.KeyPress += new KeyPressEventHandler(DomainUpDown_KeyPress);

            HourDomainUpDown.KeyUp += new KeyEventHandler(DomainUpDown_KeyUp);
            MinDomainUpDown.KeyUp += new KeyEventHandler(DomainUpDown_KeyUp);
            SecDomainUpDown.KeyUp += new KeyEventHandler(DomainUpDown_KeyUp);

            HourDomainUpDown.Enter += new System.EventHandler(DomainUpDown_Enter);
            MinDomainUpDown.Enter += new System.EventHandler(DomainUpDown_Enter);
            SecDomainUpDown.Enter += new System.EventHandler(DomainUpDown_Enter);

            okbutton.Click += (sender, e) => {
                var val =dateTimePicker.Value; 
                var h = int.Parse(HourDomainUpDown.Text);
                var m = int.Parse(MinDomainUpDown.Text);
                var s = int.Parse(SecDomainUpDown.Text);
                if (_time.Year == val.Year && _time.Month == val.Month && _time.Day == val.Day
                    && _time.Hour == h && _time.Minute == m && _time.Second == s) {
                    this.DialogResult = DialogResult.Cancel;
                } else {
                    _time = new DateTime(val.Year, val.Month, val.Day, h, m, s);
                    this.DialogResult = DialogResult.OK;
                }
                this.Close();
            };
            cancelbutton.Click += (sender, e) => {
                this.DialogResult = DialogResult.Cancel;
                this.Close();
            };
        }

        void DomainUpDown_Enter(object sender, EventArgs e) {
            DomainUpDown domainUpDown = sender as DomainUpDown;
            domainUpDown.Select(0, HourDomainUpDown.Text.Length);
        }

        void DomainUpDown_KeyUp(object sender, KeyEventArgs e) {
            DomainUpDown domainUpDown = sender as DomainUpDown;
            var text = domainUpDown.Text;
            if (text.Length == 0) {
                text = "0";
            } else if (text.Length == 2) {
                var t = int.Parse(text);
                if (t > RangDic[sender][1]) {
                    text = RangDic[sender][1].ToString();
                }
            }
            domainUpDown.Text = text;
        }

        void DomainUpDown_KeyPress(object sender, KeyPressEventArgs e) {
            if (e.KeyChar < '0' || e.KeyChar > '9') {
                e.Handled = true;
            }
        }
    }
}
