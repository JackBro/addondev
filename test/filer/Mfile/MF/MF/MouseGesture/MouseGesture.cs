using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using MouseGesture_Net;

namespace MouseGesture_Net
{
    public delegate void MouseGestureEventHandler(object sender, MouseGestureEventArgs e);
    public class MouseGestureEventArgs : System.EventArgs
    {
        public Boolean Cancel;
        public MouseButtons button;
        public int x, y;
        public string Gesture;

        public MouseGestureEventArgs() { }
        public void SetParameters(MouseButtons button, int x, int y, string Gesture)
        {
            this.Cancel = false;
            this.button = button;
            this.x = x;
            this.y = y;
            this.Gesture = Gesture;
        }
    }

    public class MouseGesture : IMessageFilter
    {
        private const int WM_MOUSEMOVE = 0x0200;
        private const int WM_LBUTTONDOWN = 0x0201;
        private const int WM_LBUTTONUP = 0x0202;
        private const int WM_RBUTTONDOWN = 0x204;
        private const int WM_RBUTTONUP = 0x205;
        private const int WM_MBUTTONDOWN = 0x0207;
        private const int WM_MBUTTONUP = 0x0208;

        private MouseGestureTest mg;
        private Control control;

        private bool mgstartflg = false;
        private string gesture = string.Empty;
        //private Boolean EnableMButtonDown = true;
        private Boolean EnableLButtonUp = false;

        public event MouseGestureEventHandler MouseGestureEvent = null;
        public MouseGestureEventArgs MouseGestureArgs = new MouseGestureEventArgs();

        public MouseGesture(Control control)
        {
            mg = new MouseGestureTest();
            this.control = control;
        }

        private Boolean IsPointContainControl(int x, int y)
        {
            Point p = control.PointToClient(new Point(x, y));
            if ((p.X > 0) && (p.X < control.Width)
                && (p.Y > 0) && (p.Y < control.Height))
            {
                return true;
            }
            return false;
        }

        private void StackGestuer(int x, int y)
        {
            Arrow arrow = mg.Test(new Point(x, y));
            if (arrow != Arrow.none)
            {
                switch (arrow)
                {
                    case Arrow.up:
                        gesture += "ª";
                        break;
                    case Arrow.right:
                        gesture += "¨";
                        break;
                    case Arrow.down:
                        gesture += "«";
                        break;
                    case Arrow.left:
                        gesture += "©";
                        break;
                }
            }
        }

        private void ResetGesture()
        {
            mgstartflg = false;
            gesture = string.Empty;
            mg.End();
        }

        public bool PreFilterMessage(ref Message m)
        {          
            //if (Control.FromHandle(m.HWnd) != control) return false;
            Control c = Control.FromHandle(m.HWnd);
            //if (!(c is csExWB.cEXWB))
            //{
            //    //this.ResetGesture();
            //    return false;
            //}

            if (m.Msg == WM_MOUSEMOVE)
            {
                if (mgstartflg)
                {
                    Point pt = new Point(m.LParam.ToInt32());
                    StackGestuer(pt.X, pt.Y);
                }
            }
            else if (m.Msg == WM_RBUTTONUP)
            {
                MouseGestureArgs.SetParameters(MouseButtons.Right, 0, 0, gesture);
                MouseGestureEvent(c, MouseGestureArgs);

                mgstartflg = false;
                mg.End();

                if (gesture != string.Empty)
                {
                    gesture = string.Empty;
                    return true;
                }
            }
            else if (m.Msg == WM_RBUTTONDOWN)
            {
                Point pt = new Point(m.LParam.ToInt32());
                //if (IsPointContainControl(pt.X, pt.Y))
                //{
                    mgstartflg = true;
                    mg.Start(null, pt);
                //}
                //else
                //{
                //    mgstartflg = false;
                //    gesture = string.Empty;
                //    mg.End();
                //}
            }
            else if (m.Msg == WM_MBUTTONDOWN)
            {

                if (!(c is csExWB.cEXWB))
                {
                    //this.ResetGesture();
                    //return false;
                }

                Point pt = new Point(m.LParam.ToInt32());
                //if (IsPointContainControl(pt.X, pt.Y))
                //{
                    MouseGestureArgs.SetParameters(MouseButtons.Middle, pt.X, pt.Y, string.Empty);
                    MouseGestureEvent(c, MouseGestureArgs);
                    if (MouseGestureArgs.Cancel)
                        return true;
                //}
            }
            else if (m.Msg == WM_LBUTTONDOWN)
            {
                //Point pt = new Point(m.LParam.ToInt32());
                //if (IsPointContainControl(pt.X, pt.Y))
                //{
                //    mgstartflg = false;
                //    gesture = string.Empty;
                //    mg.End();
                //}
                this.ResetGesture();
                EnableLButtonUp = true;
            }
            else if (m.Msg == WM_LBUTTONUP)
            {
                if (EnableLButtonUp)
                {
                    EnableLButtonUp = false;
                }
            }
            return false;
        }

        public void Start()
        {
            Application.AddMessageFilter(this);
        }

        public void end()
        {
            Application.RemoveMessageFilter(this);
        }
    }
}