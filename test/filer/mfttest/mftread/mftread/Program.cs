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

            //MFTReader r = new MFTReader();
            //r.read(new DriveInfo("c"));

            //CallBackTenTimes(
            //    new CallBackTenTimesProc(MyCallBackTenTimesProc)
            //);
            IntPtr pListB = IntPtr.Zero;
            

            // リストデータ取得
            customList(out pListB);

            // ポインタを構造体へ変換
            MFT_FILE_INFO listB = (MFT_FILE_INFO)Marshal.PtrToStructure(pListB, typeof(MFT_FILE_INFO));

            IntPtr pListA = IntPtr.Zero;
            //IntPtr pListA = Marshal.AllocCoTaskMem(Marshal.SizeOf(typeof(RECORD))*2);
            int recsize = 0;
            MFT_FILE_INFO[] aryA = new MFT_FILE_INFO[2];
            customLists(out pListA, ref recsize);
            int sizes = Marshal.SizeOf(typeof(MFT_FILE_INFO));
            for (int i = 0; i < 2; i++) {
                //ポインタを、sizeずつずらしていく。
                //IntPtr current = new IntPtr(pListA.ToInt64() + (sizes * i));
                IntPtr current = (IntPtr)((int)pListA + (sizes * i));
                //ポインタから構造体に変換して配列に格納。
                aryA[i] = (MFT_FILE_INFO)Marshal.PtrToStructure(current, typeof(MFT_FILE_INFO));

                //lstPointer = (IntPtr)((int)lstPointer + Marshal.SizeOf(lstArray[i]));
            }

            freeBuffer(pListA);

            foreach (var item in aryA) {
                Console.WriteLine(item.DirectoryFileReferenceNumber);
                Console.WriteLine(item.Name);
            }

            MFT_FILE_INFO sysTime = new MFT_FILE_INFO();
            //IntPtr sysTimePtr = Marshal.AllocCoTaskMem(Marshal.SizeOf(sysTime));

            //GetRecordS(ref sysTimePtr);
            //sysTime = (RECORD)Marshal.PtrToStructure(sysTimePtr, sysTime.GetType());


            IntPtr aryXPtr = Marshal.AllocCoTaskMem(Marshal.SizeOf(typeof(MFT_FILE_INFO)) * 10); //IntPtr.Zero;
            GetRecord(ref aryXPtr);
            MFT_FILE_INFO[] aryX = new MFT_FILE_INFO[10];

            int size = Marshal.SizeOf(typeof(MFT_FILE_INFO));
            for (int i = 0; i <10; i++) {
                //ポインタを、sizeずつずらしていく。
                //IntPtr current = new IntPtr(aryXPtr.ToInt64() + (size * i));
                IntPtr current = (IntPtr)((int)aryXPtr + (size * i));
                //ポインタから構造体に変換して配列に格納。
                aryX[i] = (MFT_FILE_INFO)Marshal.PtrToStructure(current, typeof(MFT_FILE_INFO));

                //unsafe {
                //    RECORD obj = *(RECORD*)current;
                //    int k = 0;
                //}

                //lstPointer = (IntPtr)((int)lstPointer + Marshal.SizeOf(lstArray[i]));

            }

            //Marshal.FreeCoTaskMem(aryXPtr);

            foreach (var item in aryX) {
                Console.WriteLine(item.DirectoryFileReferenceNumber);
            }
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
