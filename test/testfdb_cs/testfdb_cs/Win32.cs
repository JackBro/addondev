using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace testfdb_cs
{
    sealed class Win32
    {
        [StructLayout(LayoutKind.Sequential)]
        public struct FILEGUID
        {
            public ulong Data1;
            public ushort Data2;
            public ushort Data3;

            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 4)]
            public Byte[] Data4;

            public override string ToString()
            {
                return String.Format("{0}-{1}-{2}-{3}-{4}-{5}-{6}",
                    Data1.ToString(),
                    Data2.ToString(),
                    Data3.ToString(),
                    Data4[0].ToString(),
                    Data4[1].ToString(),
                    Data4[2].ToString(),
                    Data4[3].ToString());
            }

            public static FILEGUID parse(string guid)
            {
                FILEGUID fileguid = new FILEGUID();
                fileguid.Data4 = new Byte[4];

                string[] g = guid.Split('-');
                fileguid.Data1 = ulong.Parse(g[0]);
                fileguid.Data2 = ushort.Parse(g[1]);
                fileguid.Data3 = ushort.Parse(g[2]);
                fileguid.Data4[0] = Byte.Parse(g[3]);
                fileguid.Data4[1] = Byte.Parse(g[4]);
                fileguid.Data4[2] = Byte.Parse(g[5]);
                fileguid.Data4[3] = Byte.Parse(g[6]);

                return fileguid;
            }
        }

        [DllImport("fgutil.dll", EntryPoint = "getObjectID", CharSet = CharSet.Unicode)]
        public static extern Boolean getObjectID(string fullpath, ref FILEGUID guid);

        [DllImport("fgutil.dll", EntryPoint = "getFullPathByObjectID", CharSet = CharSet.Unicode)]
        public static extern Boolean getFullPathByObjectID(FILEGUID guid, [MarshalAs(UnmanagedType.BStr)]ref string fullpath);

        public static FILEGUID getObjectID(string fullpath)
        {
            FILEGUID guid = new Win32.FILEGUID();
            guid.Data4 = new Byte[4];

            Boolean rc = Win32.getObjectID(fullpath, ref guid);
            if (rc)
            {
                return guid;
            }

            return guid;
        }

        public static string getFullPathByObjectID(FILEGUID guid)
        {
            string path = "";
            Boolean rc = Win32.getFullPathByObjectID(guid, ref path);
            if (rc)
            {
                //MessageBox.Show(path.ToString());
            }

            return path;
        }

    }
}
