using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using shiranui;
using Plugin;
using ConfigUtil;

namespace MouseGesture_Net
{
    public class MouseGestureManager
    {
        private const string filename = "mousegesture.xml";
        private List<MouseGestureCommand> gesturelist;
        private MouseGestureConfig config;
        private MouseGesture mousegesture;

        public MouseGestureManager()
        {
            gesturelist = new List<MouseGestureCommand>();
            config = new MouseGestureConfig();
            config.f = new SettingControl();
            config.LoadFormEvent += new ConfigEventHandler(config_LoadFormEvent);
            config.AppliedEvent += new ConfigEventHandler(config_AppliedEvent);
            //config.ShowFormEvent += new ShowFormEventHandler(config_ShowSettingEvent);
            //config.AppEvent += new AppEventHandler(config_AppEvent);
            ConfigContainer container1 = new ConfigContainer("MouseGestureManager");
            container1.ConfigList.Add(config);
            ConfigManager.GetInstance().ConfigContainerList.Add(container1);
            //ConfigManager.GetInstance().ConfigList.Add(config);
        }

        void config_LoadFormEvent(object sender)
        {
            config.f.list = gesturelist;
            //throw new Exception("The method or operation is not implemented.");
        }

        void config_AppliedEvent(object sender)
        {
            SettingControl setting = ((MouseGestureConfig)sender).f;
            gesturelist = setting.list;
        }

        void mousehook_MouseGestureEvent(object sender, MouseGestureEventArgs e)
        {
            //if (!(sender is csExWB.cEXWB))
            //{
            //    //e.Cancel = false;
            //    return;
            //}
            if (e.button == MouseButtons.Middle)
            {
                if (MainFormHandler.Form.tabcontrol.SelectedTab == null) return;

                csExWB.cEXWB wb = MainFormHandler.Form.tabcontrol.SelectedTab.Browser;
                //if (wb == sender)
                //{
                    string url = wb.GetUrlFromPoint(e.x, e.y);

                    if (!string.IsNullOrEmpty(url))
                    {
                        MainFormHandler.Form.OpenNewTab(url);
                        e.Cancel = true;
                    }
                //}
            }
            else if(e.Gesture != string.Empty)
            {
                foreach(MouseGestureCommand gesture in gesturelist)
                {
                    if (gesture.Gesture == e.Gesture)
                    {
                        CommandManager.GetInstance.ExecCommand(gesture.CommandName);
                        break;
                    }
                }
            }
        }

        public Boolean Start(Control control)
        {
            if (mousegesture == null)
            {
                mousegesture = new MouseGesture(control);
                mousegesture.MouseGestureEvent += new MouseGestureEventHandler(mousehook_MouseGestureEvent);
            }
            mousegesture.Start();
            return true;
        }

        public void End()
        {
            mousegesture.end();
        }

        public void Add(string gesture, string commandname)
        {
            gesturelist.Add(new MouseGestureCommand(gesture, commandname));
        }

        public void Save()
        {
            SerializeXML<List<MouseGestureCommand>>.Save(filename, gesturelist);
        }
        public void Load()
        {
            SerializeXML<List<MouseGestureCommand>>.Load(filename);
        }
    }

    public class MouseGestureCommand
    {
        private string _gesture;
        private string _commandname;

        public MouseGestureCommand()
        {
        }
        public MouseGestureCommand(string gesture, string commandname)
        {
            this._gesture = gesture;
            this._commandname = commandname;
        }
        public string Gesture
        {
            get { return this._gesture; }
            set { this._gesture = value; }
        }
        public string CommandName
        {
            get { return this._commandname; }
            set { this._commandname = value; }
        }
    }

    public class MouseGestureConfig : ConfigUseForm
    {
        public SettingControl f;
        public MouseGestureConfig()
        {
        }

        public override string name
        {
            get { return "MouseGestureConfig"; }
        }

        protected override UserControl User
        {
            get 
            {
                if (f == null)
                    f = new SettingControl();

                return f;
            }
        }
    }
}
