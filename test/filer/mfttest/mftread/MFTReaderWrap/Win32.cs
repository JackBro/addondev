using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace MFTReaderWrap {
    public delegate bool CallBackProc(int per);
    
    public class Win32 {

        [DllImport("MFTReader.dll", CharSet=CharSet.Unicode)]
        public static extern int GetMFTFileRecord(
            string driveletter, 
            out IntPtr pfile_info, 
            ref UInt64 size, 
            CallBackProc proc, 
            ref UInt32 errorCode);

        [DllImport("MFTReader.dll")]
        public static extern void freeBuffer(IntPtr p);

        [StructLayout(LayoutKind.Sequential)]
        public struct GUID {
            public ulong Data1;
            public ushort Data2;
            public ushort Data3;
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 4)]
            public Byte[] Data4;
        }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Unicode)]
        public struct MFT_FILE_INFO {
            //public GUID Guid;
            public ulong DirectoryFileReferenceNumber;
            public bool IsDirectory;

            public ulong Size;
            public ulong CreationTime;
            public ulong LastWriteTime;
            public ulong LastAccessTime;

            [MarshalAsAttribute(UnmanagedType.LPWStr)]
            public String Name;
        }

        public const uint FORMAT_MESSAGE_FROM_SYSTEM = 0x00001000;
        [DllImport("kernel32.dll")]
        public static extern uint FormatMessage(
            uint dwFlags, 
            IntPtr lpSource,
            uint dwMessageId, 
            uint dwLanguageId,
            StringBuilder lpBuffer, 
            int nSize,
            IntPtr Arguments); 

    }
}
