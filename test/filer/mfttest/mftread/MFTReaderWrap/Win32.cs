using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace MFTReaderWrap {
    public class Win32 {

        [DllImport("MFTReader.dll")]
        public static extern unsafe void GetMFT_File_Info(string driveletter, out IntPtr pfile_info, ref UInt64 size, CallBackProc proc);

        [DllImport("MFTReader.dll")]
        public static extern unsafe void freeBuffer(IntPtr p);

        public event CallBackProc CallBackEvent;

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Unicode)]
        public struct MFT_FILE_INFO {
            public UInt64 DirectoryFileReferenceNumber;
            public bool IsDirectory;
            [MarshalAsAttribute(UnmanagedType.LPWStr)]
            public String Name;
            public UInt64 CreationTime;
            public UInt64 LastWriteTime;
        }

    }
}
