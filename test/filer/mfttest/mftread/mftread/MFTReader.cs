using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace mftread {

    public class MFTReader {

        public delegate bool CallBackProc(int per);
        [DllImport("MFTReader.dll")]
        static extern void GetMFT_File_Info(string driveletter, out IntPtr pfile_info, ref UInt64 size, CallBackProc proc);


        [DllImport("MFTReader.dll")]
        static extern void freeBuffer(IntPtr p);

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


        public unsafe void read(DriveInfo driveInfo) {
            string pathRoot = string.Concat(@"\\.\", driveInfo.Name);
            pathRoot = @"\\.\c:";
            var hVolume = Win32API.CreateFile(
                pathRoot,
                Win32API.GENERIC_READ | Win32API.GENERIC_WRITE,
                Win32API.FILE_SHARE_READ | Win32API.FILE_SHARE_WRITE,
                IntPtr.Zero,
                Win32API.OPEN_EXISTING,
                0,
                IntPtr.Zero);

            if (hVolume.ToInt32() == Win32API.INVALID_HANDLE_VALUE) {
                Win32API.CloseHandle(hVolume);
                return;
            }

            Win32API.NTFS_VOLUME_DATA_BUFFER ntfsVolData = new Win32API.NTFS_VOLUME_DATA_BUFFER();
            int ntfsVolDataSize = Marshal.SizeOf(ntfsVolData);
            IntPtr volBuffer = Marshal.AllocHGlobal(ntfsVolDataSize);
            //Win32API.ZeroMemory(volBuffer, ntfsVolDataSize);
            //Marshal.StructureToPtr(ntfsVolData, volBuffer, true);

            uint lpBytesReturned = 0;
            var ret = Win32API.DeviceIoControl(
                hVolume, 
                Win32API.FSCTL_GET_NTFS_VOLUME_DATA,
                IntPtr.Zero,
                0,
                volBuffer,
                ntfsVolDataSize,
                out lpBytesReturned,
                IntPtr.Zero);

            ntfsVolData = (Win32API.NTFS_VOLUME_DATA_BUFFER)Marshal.PtrToStructure(volBuffer, typeof(Win32API.NTFS_VOLUME_DATA_BUFFER));
            Marshal.FreeHGlobal(volBuffer);

            if (ret) {
                //Console.WriteLine("Volume Serial Number: 0X%.8X%.8X\n", ntfsVolData.VolumeSerialNumber.HighPart, ntfsVolData.VolumeSerialNumber.LowPart);
                Console.WriteLine("The number of bytes in a cluster: %u", ntfsVolData.BytesPerCluster);
                Console.WriteLine("The number of bytes in a file record segment: %u", ntfsVolData.BytesPerFileRecordSegment);
                Console.WriteLine("The number of bytes in a sector: %u", ntfsVolData.BytesPerSector);
                Console.WriteLine("The number of clusters in a file record segment: %u", ntfsVolData.ClustersPerFileRecordSegment);
                Console.WriteLine("The number of free clusters in the specified volume: %u", ntfsVolData.FreeClusters);
                //Console.WriteLine("The starting logical cluster number of the master file table mirror: 0X%.8X%.8X\n", ntfsVolData.Mft2StartLcn.HighPart, ntfsVolData.Mft2StartLcn.LowPart);
                //Console.WriteLine("The starting logical cluster number of the master file table: 0X%.8X%.8X\n", ntfsVolData.MftStartLcn.HighPart, ntfsVolData.MftStartLcn.LowPart);
                Console.WriteLine("The length of the master file table, in bytes: %u", ntfsVolData.MftValidDataLength);
                //Console.WriteLine("The ending logical cluster number of the master file table zone: 0X%.8X%.8X\n", ntfsVolData.MftZoneEnd.HighPart, ntfsVolData.MftZoneEnd.LowPart);
                //Console.WriteLine("The starting logical cluster number of the master file table zone: 0X%.8X%.8X\n", ntfsVolData.MftZoneStart.HighPart, ntfsVolData.MftZoneStart.LowPart);
                Console.WriteLine("The number of sectors: %u", ntfsVolData.NumberSectors);
                Console.WriteLine("Total Clusters (used and free): %u", ntfsVolData.TotalClusters);
                Console.WriteLine("The number of reserved clusters: %u\n", ntfsVolData.TotalReserved);
            }

            Int64 QuadPart = 1024; // 1024 or 2048
            long total_file_count = (ntfsVolData.MftValidDataLength / QuadPart);

            Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER ob = new Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER();
            int obSize = Marshal.SizeOf(ob) + ntfsVolData.BytesPerFileRecordSegment-1;
            //int obSize = sizeof(Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER) + ntfsVolData.BytesPerFileRecordSegment - 1;

            Win32API.NTFS_FILE_RECORD_INPUT_BUFFER mftRecordInput = new Win32API.NTFS_FILE_RECORD_INPUT_BUFFER();
            int mftRecordInputSize = Marshal.SizeOf(mftRecordInput);

            for (long i = 0; i < total_file_count; i++) {

                mftRecordInput.FileReferenceNumber = i;
                IntPtr input_buffer = Marshal.AllocHGlobal(mftRecordInputSize);
                Win32API.ZeroMemory(input_buffer, mftRecordInputSize);
                Marshal.StructureToPtr(mftRecordInput, input_buffer, true);


                IntPtr output_buffer = Marshal.AllocHGlobal(obSize);

                var bDioControl = Win32API.DeviceIoControl(
                    hVolume,
                    //Win32API.FSCTL_GET_NTFS_FILE_RECORD,
                    (uint)FSCTL.GET_NTFS_FILE_RECORD,
                    input_buffer,
                    mftRecordInputSize,
                    output_buffer,
                    obSize,
                    out lpBytesReturned,
                    IntPtr.Zero);
                //unsafe {
                Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER olData = (Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER)Marshal.PtrToStructure(output_buffer, typeof(Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER));
                //Win32API.FILE_RECORD_HEADER p_file_record_header = new Win32API.FILE_RECORD_HEADER();
                Win32API.FILE_RECORD_HEADER* p_file_record_header;


                    //Win32API.FILE_RECORD_HEADER* p_file_record_header = null;
                    //IntPtr op = new IntPtr(&olData.FileRecordBuffer[0]);
                fixed (byte* ptr = &olData.FileRecordBuffer[0]) {

                   // fixed (Win32API.FILE_RECORD_HEADER* p_file_record_header = (Win32API.FILE_RECORD_HEADER*)&olData.FileRecordBuffer[0]) 
                        //IntPtr ptr = Marshal.AllocHGlobal(sizeof(byte));
                        //Marshal.StructureToPtr(olData.FileRecordBuffer, ptr, false);
                        //IntPtr aop = Marshal.Read(op);
                        //Marshal.Copy(new IntPtr[] { op }, p_file_record_header.AttributesOffset, pAttr, Marshal.SizeOf(attr));
                        //IntPtr ptr = new IntPtr(op);

                        // 配列をピン止めしてGCの対象から外す
                        //GCHandle gch = GCHandle.Alloc(olData.FileRecordBuffer, GCHandleType.Pinned);
                        // 配列の先頭のアドレスを取得
                        //IntPtr ptr = gch.AddrOfPinnedObject();
                        //IntPtr ptr = (IntPtr)(&olData.FileRecordBuffer[0]);
                        //Point* pt = (Point*)p;
                        //IntPtr ptr = (IntPtr)(op);
                        //IntPtr ptr = (IntPtr)(&olData.FileRecordBuffer);
                        //IntPtr ptr = new IntPtr(op);
                        //Marshal.StructureToPtr(p_file_record_header, ptr, true);
                        //p_file_record_header = (Win32API.FILE_RECORD_HEADER)Marshal.PtrToStructure(ptr, typeof(Win32API.FILE_RECORD_HEADER));
                        p_file_record_header = (Win32API.FILE_RECORD_HEADER*)ptr;

                        Win32API.RECORD_ATTRIBUTE attr = new Win32API.RECORD_ATTRIBUTE();
                        IntPtr pAttr = Marshal.AllocHGlobal(Marshal.SizeOf(attr));
                        //Marshal.Copy(new IntPtr[] { ptr }, p_file_record_header.AttributesOffset, pAttr, Marshal.SizeOf(attr));

                        unsafe {
                            byte[] binary = olData.FileRecordBuffer;
                            var adr = Marshal.AllocHGlobal(binary.Length);
                            Marshal.Copy(binary, 0, adr, binary.Length);
                            System.IntPtr pAttribute;

                            Win32API.FILE_RECORD_HEADER* pHeader = (Win32API.FILE_RECORD_HEADER*)adr;
                            IntPtr pFirstAttribute = new IntPtr((byte*)ptr + pHeader->AttributesOffset);
                            Win32API.RECORD_ATTRIBUTE* pattr = (Win32API.RECORD_ATTRIBUTE*)pFirstAttribute;
                            int kk2 = 0;
                            //using (System.Runtime.InteropServices.HGlobal ptr = new System.Runtime.InteropServices.HGlobal(binary.Length)) {
                             //   System.Runtime.InteropServices.Marshal.Copy(binary, 0, ptr.Address, binary.Length);
                             //   System.WindowsNT.Devices.IO.FileTables1.FileRecordSegment segment = System.WindowsNT.Devices.IO.FileTables1.FileRecordSegment.FromPtr(ptr.Address, out pAttribute);
                             //   return new System.WindowsNT.Devices.IO.FileTables1.FileRecord(referenceNumber, segment, System.WindowsNT.Devices.IO.FileTables1.MFTAttribute.AllFromPtr(pAttribute));
                           // }
                        }
                        IntPtr current = (IntPtr)((int)ptr + p_file_record_header->AttributesOffset);
                        attr = (Win32API.RECORD_ATTRIBUTE)Marshal.PtrToStructure(current, typeof(Win32API.RECORD_ATTRIBUTE));
                        int kk = 0;
                    }

                    //IntPtr op = (IntPtr)(&olData.FileRecordBuffer[0]);
                    //Marshal.StructureToPtr(p_file_record_header, op, true);

                    //Win32API.RECORD_ATTRIBUTE attr = new Win32API.RECORD_ATTRIBUTE();
                    //IntPtr pAttr = Marshal.AllocHGlobal(Marshal.SizeOf(attr));
                    //Marshal.Copy(new IntPtr[]{ op}, p_file_record_header.AttributesOffset, pAttr, Marshal.SizeOf(attr));


                    //int size = Marshal.SizeOf(attr);
                    //IntPtr ptr = Marshal.AllocHGlobal(size);
                    //Marshal.StructureToPtr(obj, ptr, false);
                    //Marshal.StructureToPtr(attr, new IntPtr(op.ToPointer() +p_file_record_header.AttributesOffset), true);
                //}
            }

            Win32API.CloseHandle(hVolume);
        }

        public void GetFileInfo() {


            IntPtr pListA = IntPtr.Zero;
            UInt64 size = 0;

            GetMFT_File_Info("c", out pListA, ref size, CallBackEvent);

            MFT_FILE_INFO[] aryFileInfo = new MFT_FILE_INFO[size];
            int ssize = Marshal.SizeOf(typeof(MFT_FILE_INFO));
            for (int i = 0; i < (int)size; i++) {
                //ポインタを、sizeずつずらしていく。
                IntPtr current = new IntPtr(pListA.ToInt64() + (ssize * i));
                //IntPtr current = (IntPtr)((int)pListA + (sizes * i));
                //ポインタから構造体に変換して配列に格納。
                aryFileInfo[i] = (MFT_FILE_INFO)Marshal.PtrToStructure(current, typeof(MFT_FILE_INFO));

                //lstPointer = (IntPtr)((int)lstPointer + Marshal.SizeOf(lstArray[i]));
            }

            freeBuffer(pListA);
        }
        public void free() {
        }
    }
}
