using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;
using System.Runtime.CompilerServices;

namespace mftread {
    class Program {
        static void Main(string[] args) {

            ///List<mftread.MFT_FILE_INFO> mftfiles;
            MFTReader r = new MFTReader();
            DateTime s = DateTime.Now;
            var mftfiles = r.read(new DriveInfo("c"));
            var tickgetfiles = DateTime.Now - s;
            Console.WriteLine("read MFT is " + tickgetfiles.TotalMilliseconds.ToString() + "msec");
            Console.WriteLine("file num = " + mftfiles.Length.ToString());

            //CallBackTenTimes(
            //    new CallBackTenTimesProc(MyCallBackTenTimesProc)
            //);
        }

        static void MyCallBackTenTimesProc() {
            Console.WriteLine("Hello, Callback!");
        }

        delegate void CallBackTenTimesProc();

        [DllImport("MFTReader.dll")]
        static extern unsafe void CallBackTenTimes(CallBackTenTimesProc proc);


        delegate bool CallBackProc(int per);
        [DllImport("MFTReader.dll")]
        static extern unsafe void GetMFT_File_Info(string driveletter,  IntPtr pfile_info, ref UInt64 size, CallBackProc proc);
        /*
typedef struct {
	ULONGLONG DirectoryFileReferenceNumber;
	bool IsDirectory;
	LPWSTR Name;
	ULONGLONG Size; 
	ULONGLONG CreationTime;
	ULONGLONG LastWriteTime;

} MFT_FILE_INFO, *PMFT_FILE_INFO;
*/
        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Unicode)]
        public struct MFT_FILE_INFO {
            public UInt64 DirectoryFileReferenceNumber;
            public bool IsDirectory;
            [MarshalAsAttribute(UnmanagedType.LPWStr)]
            public String Name;
            public UInt64 CreationTime;
            public UInt64 LastWriteTime;
        }

        [DllImport("MFTReader.dll")]
        static extern unsafe void GetRecord(ref IntPtr proc);

        [DllImport("MFTReader.dll")]
        static extern unsafe void GetRecordS(ref IntPtr proc);

        [DllImport("MFTReader.dll")]
        static extern unsafe void customList(out IntPtr p);


        [DllImport("MFTReader.dll")]
        static extern unsafe void customLists(out IntPtr p, ref int size);


        [DllImport("MFTReader.dll")]
        static extern unsafe void freeBuffer(IntPtr p);
    }

}
