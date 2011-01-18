using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace mftread {
    class Program {
        static void Main(string[] args) {
            CallBackTenTimes(
                new CallBackTenTimesProc(MyCallBackTenTimesProc)
            );
        }

        static void MyCallBackTenTimesProc() {
            Console.WriteLine("Hello, Callback!");
        }

        delegate void CallBackTenTimesProc();

        [DllImport("MFTReader.dll")]
        static extern unsafe void CallBackTenTimes(CallBackTenTimesProc proc);
    }

}
