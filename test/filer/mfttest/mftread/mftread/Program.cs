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
            //CallBackTenTimes(
            //    new CallBackTenTimesProc(MyCallBackTenTimesProc)
            //);
            RECORD sysTime = new RECORD();
            IntPtr sysTimePtr = Marshal.AllocCoTaskMem(Marshal.SizeOf(sysTime));

            GetRecordS(ref sysTimePtr);
            sysTime = (RECORD)Marshal.PtrToStructure(sysTimePtr, sysTime.GetType());


            IntPtr aryXPtr = IntPtr.Zero;
            GetRecord(ref aryXPtr);
            RECORD[] aryX = new RECORD[10];

            int size = Marshal.SizeOf(typeof(RECORD));
            for (int i = 0; i <10; i++) {
                //ポインタを、sizeずつずらしていく。
                IntPtr current = new IntPtr(aryXPtr.ToInt64() + (size * i));
                //ポインタから構造体に変換して配列に格納。
                aryX[i] = (RECORD)Marshal.PtrToStructure(current, typeof(RECORD));
            }

            //Marshal.FreeCoTaskMem(aryXPtr);

            foreach (var item in aryX) {
                Console.WriteLine(item.index);
            }
        }

        static void MyCallBackTenTimesProc() {
            Console.WriteLine("Hello, Callback!");
        }

        delegate void CallBackTenTimesProc();

        [DllImport("MFTReader.dll")]
        static extern unsafe void CallBackTenTimes(CallBackTenTimesProc proc);


        [StructLayout(LayoutKind.Sequential)]
        public struct RECORD {
            public int index;
            public int ChangeTime;
            //public String name;
        }

        [DllImport("MFTReader.dll")]
        static extern unsafe void GetRecord(ref IntPtr proc);

        [DllImport("MFTReader.dll")]
        static extern unsafe void GetRecordS(ref IntPtr proc);
    }

}
