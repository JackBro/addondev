using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace mftread {
    public class MFTReader {
        public void read(DriveInfo driveInfo) {
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
                    Win32API.FSCTL_GET_NTFS_FILE_RECORD,
                    input_buffer,
                    mftRecordInputSize,
                    output_buffer,
                    obSize,
                    out lpBytesReturned,
                    IntPtr.Zero);

                Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER olData = (Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER)Marshal.PtrToStructure(output_buffer, typeof(Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER));
                Win32API.FILE_RECORD_HEADER p_file_record_header = new Win32API.FILE_RECORD_HEADER();
                unsafe {


                   

                    //IntPtr op = new IntPtr(&olData.FileRecordBuffer[0]);
                    fixed (byte *op = &olData.FileRecordBuffer[0]) {
                        //IntPtr aop = Marshal.Read(op);
                        //Marshal.Copy(new IntPtr[] { op }, p_file_record_header.AttributesOffset, pAttr, Marshal.SizeOf(attr));
                        IntPtr ptr = (IntPtr)(*op);
                        Marshal.StructureToPtr(p_file_record_header, ptr, true);
                        
                        Win32API.RECORD_ATTRIBUTE attr = new Win32API.RECORD_ATTRIBUTE();
                        IntPtr pAttr = Marshal.AllocHGlobal(Marshal.SizeOf(attr));
                        Marshal.Copy(new IntPtr[] { ptr }, p_file_record_header.AttributesOffset, pAttr, Marshal.SizeOf(attr));

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
                }
            }

            Win32API.CloseHandle(hVolume);
        }

    }
}
