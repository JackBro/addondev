using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace MFT {

    [Serializable]
    public struct MFT_FILE_INFO {
        public Int32 ParentID;
        public bool IsDirectory;
        //public String Path;
        public String Name;
        public UInt64 Size;
        public DateTime CreationTime;
        public DateTime LastWriteTime;
        //public ulong CreationTime;
        //public ulong LastWriteTime;
    }

    //public class CallBackEventArgs : EventArgs {
    //    private Point location;
    //    public string Link { get; private set; }
    //    public MouseButtons Button { get; private set; }
    //    public int Clicks { get; private set; }
    //    public int X { get { return Location.X; } private set { location.X = value; } }
    //    public int Y { get { return Location.Y; } private set { location.Y = value; } }
    //    public Point Location { get { return location; } private set { location = value; } }
    //    public int Delta { get; private set; }

    //    public CallBackEventArgs(MouseEventArgs e, string link) {
    //        Button = e.Button;
    //        Clicks = e.Clicks;
    //        Location = e.Location;
    //        Delta = e.Delta;
    //        Link = link;
    //    }
    //}


    public delegate bool CallBackProc(int per);

    public class MFTReader {

        DateTime s20, s50, s90;
        TimeSpan sp20, sp50, sp90;

        public event CallBackProc CallBackEvent;

        public unsafe MFT_FILE_INFO[] read(DriveInfo driveInfo) {

            TimeSpan utcOffset = System.TimeZoneInfo.Local.BaseUtcOffset;
            var baseticks = (ulong)(new DateTime(1601, 01, 01).Ticks + utcOffset.Ticks);

            string pathRoot = string.Concat(@"\\.\", driveInfo.Name.Substring(0, 2));

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
                //Marshal.GetLastWin32Error
                return new MFT_FILE_INFO[1];
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
                Console.WriteLine("The number of bytes in a cluster: {0}", ntfsVolData.BytesPerCluster);
                Console.WriteLine("The number of bytes in a file record segment: {0}", ntfsVolData.BytesPerFileRecordSegment);
                Console.WriteLine("The number of bytes in a sector: {0}", ntfsVolData.BytesPerSector);
                Console.WriteLine("The number of clusters in a file record segment: {0}", ntfsVolData.ClustersPerFileRecordSegment);
                Console.WriteLine("The number of free clusters in the specified volume: {0}", ntfsVolData.FreeClusters);
                //Console.WriteLine("The starting logical cluster number of the master file table mirror: 0X%.8X%.8X\n", ntfsVolData.Mft2StartLcn.HighPart, ntfsVolData.Mft2StartLcn.LowPart);
                //Console.WriteLine("The starting logical cluster number of the master file table: 0X%.8X%.8X\n", ntfsVolData.MftStartLcn.HighPart, ntfsVolData.MftStartLcn.LowPart);
                Console.WriteLine("The length of the master file table, in bytes: {0}", ntfsVolData.MftValidDataLength);
                //Console.WriteLine("The ending logical cluster number of the master file table zone: 0X%.8X%.8X\n", ntfsVolData.MftZoneEnd.HighPart, ntfsVolData.MftZoneEnd.LowPart);
                //Console.WriteLine("The starting logical cluster number of the master file table zone: 0X%.8X%.8X\n", ntfsVolData.MftZoneStart.HighPart, ntfsVolData.MftZoneStart.LowPart);
                Console.WriteLine("The number of sectors: {0}", ntfsVolData.NumberSectors);
                Console.WriteLine("Total Clusters (used and free): {0}", ntfsVolData.TotalClusters);
                Console.WriteLine("The number of reserved clusters: {0}\n", ntfsVolData.TotalReserved);
            }

            Int64 QuadPart = 1024; // 1024 or 2048
            long total_file_count = (ntfsVolData.MftValidDataLength / QuadPart);

            //total_file_count = total_file_count / 1000; //test
            //Int32[] parentid = new Int32[total_file_count];
            MFT_FILE_INFO[] fileinfos = new MFT_FILE_INFO[total_file_count];

            Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER ob = new Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER();
            int obSize = Marshal.SizeOf(ob) + ntfsVolData.BytesPerFileRecordSegment - 1;
            //int obSize = sizeof(Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER) + ntfsVolData.BytesPerFileRecordSegment - 1;

            Win32API.NTFS_FILE_RECORD_INPUT_BUFFER mftRecordInput = new Win32API.NTFS_FILE_RECORD_INPUT_BUFFER();
            int mftRecordInputSize = Marshal.SizeOf(mftRecordInput);


            //Win32API.FILE_RECORD_HEADER hdum = new Win32API.FILE_RECORD_HEADER();
            //int hdumSize = Marshal.SizeOf(hdum);
            for (long i = 0; i < total_file_count; i++) {



                mftRecordInput.FileReferenceNumber = i;

                IntPtr input_buffer = Marshal.AllocHGlobal(mftRecordInputSize);
                Win32API.ZeroMemory(input_buffer, mftRecordInputSize);
                Marshal.StructureToPtr(mftRecordInput, input_buffer, true);

                IntPtr output_buffer = Marshal.AllocHGlobal(obSize);

                if (i == total_file_count * 0.2)
                    s20 = DateTime.Now;
                else if (i == total_file_count * 0.5)
                    s50 = DateTime.Now;
                else if (i == total_file_count * 0.9)
                    s90 = DateTime.Now;

                var bDioControl = Win32API.DeviceIoControl(
                    hVolume,
                    Win32API.FSCTL_GET_NTFS_FILE_RECORD,
                    input_buffer,
                    mftRecordInputSize,
                    output_buffer,
                    obSize,
                    out lpBytesReturned,
                    IntPtr.Zero);


                //IntPtr hp = Marshal.AllocHGlobal(hdumSize);
                //uint read = 0;
                //Marshal.StructureToPtr(hdum, hp, true);
                //byte[] buffer = new byte[Marshal.SizeOf(hdum)];
                //fixed (byte* p = buffer) {
                //    //ReadFile(readhandle, p, lpNumberOfBytesRead, &n, 0);
                //    Win32API.ReadFile(hVolume, hp, (uint)(1024 * ntfsVolData.BytesPerCluster), ref read, IntPtr.Zero);
                //    Win32API.FILE_RECORD_HEADER* ph = (Win32API.FILE_RECORD_HEADER*)((int)hp);
                //}

                //fixed (Win32API.NTFS_FILE_RECORD_INPUT_BUFFER* ptr = (&mftRecordInput)) {
                //    var bDioControl = Win32API.DeviceIoControl(
                //        hVolume,
                //        Win32API.FSCTL_GET_NTFS_FILE_RECORD,
                //        (IntPtr)ptr,
                //        mftRecordInputSize,
                //        output_buffer,
                //        obSize,
                //        out lpBytesReturned,
                //        IntPtr.Zero);
                //}


                Win32API.FILE_RECORD_HEADER* p_file_record_header;

                var outbuff = Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER.FromPtr(output_buffer);
                //byte* ptr = (byte*)output_buffer + (int)Marshal.OffsetOf(typeof(Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER), "FileRecordBuffer");

                fixed (byte* ptr = &outbuff.FileRecordBuffer[0]) {
                    p_file_record_header = (Win32API.FILE_RECORD_HEADER*)ptr;

                    Win32API.RECORD_ATTRIBUTE* attr = (Win32API.RECORD_ATTRIBUTE*)((int)ptr + p_file_record_header->AttributesOffset);

                    Win32API.STANDARD_INFORMATION* si;
                    if (p_file_record_header->Ntfs.Type == 1162627398) {//'ELIF'
                        int stop = Math.Min(8, (int)p_file_record_header->NextAttributeNumber);
                        for (int j=0;j<stop;j++){
                        //while (true) {
                            if (attr->AttributeType < 0 || (int)attr->AttributeType > 0x100) break;

                            switch (attr->AttributeType) {
                                case Win32API.AttributeType.AttributeFileName:
                                    //Win32API.RESIDENT_ATTRIBUTE* regsttr = (Win32API.RESIDENT_ATTRIBUTE*)attr;
                                    Win32API.FILENAME_ATTRIBUTE fattr =
                                        (Win32API.FILENAME_ATTRIBUTE)Marshal.PtrToStructure((IntPtr)((((byte*)attr) + ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueOffset)), typeof(Win32API.FILENAME_ATTRIBUTE));
                                    //parentid[i] = (Int32)fattr.DirectoryFileReferenceNumber;
                                    fileinfos[i].ParentID = (Int32)fattr.DirectoryFileReferenceNumber;                                    
                                    fileinfos[i].IsDirectory = ((p_file_record_header->Flags & 0x2) == 2);
                                    fileinfos[i].Name = fattr.Name.Substring(0, fattr.NameLength);
                                    //if (fattr.NameLength > 3) {
                                    //    fileinfos[i].Name = fattr.Name.Substring(0, fattr.NameLength);
                                    //}
                                    //else {
                                    //    fileinfos[i].Name = fattr.Name.Substring(0, fattr.NameLength);
                                    //}
                                    //fileinfos[i].Name = fattr.Name;
                                    //fileinfos[i].Size = fattr.DataSize;
                                    //goto BB;
                                    break;

                                case Win32API.AttributeType.AttributeStandardInformation:
                                    //var off = (Win32API.RESIDENT_ATTRIBUTE*)attr;
                                    si = (Win32API.STANDARD_INFORMATION*)((byte*)attr + ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueOffset);
                                    //Win32API.STANDARD_INFORMATION sattr =
                                    //(Win32API.STANDARD_INFORMATION)Marshal.PtrToStructure(new IntPtr(&attr + off->ValueOffset), typeof(Win32API.STANDARD_INFORMATION));
                                    //fileinfos[i].CreationTime = si->CreationTime;
                                    //fileinfos[i].LastWriteTime = si->LastWriteTime;
                                    fileinfos[i].CreationTime = new DateTime((long)(si->CreationTime + baseticks));
                                    fileinfos[i].LastWriteTime =new DateTime((long)(si->LastWriteTime + baseticks));
                                    break;
                                case Win32API.AttributeType.AttributeData:
                                    if (attr->NonResident == 1) {
                                        fileinfos[i].Size = ((Win32API.NONRESIDENT_ATTRIBUTE*)attr)->DataSize;
                                    } else {
                                        fileinfos[i].Size = ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueLength;
                                    }
                                    break;
                                default:
                                    break;
                            }

                            if (attr->Length > 0 && attr->Length < (int)p_file_record_header->BytesInUse)
                                attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + attr->Length);
                            else
                                if (attr->NonResident == 1)//TRUE)
                                    attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + sizeof(Win32API.NONRESIDENT_ATTRIBUTE));
                        }
                    //BB:
                    //    ;
                        //if (fileinfos[i].Name != null && fileinfos[i].Name.Contains("wv.ncb")) {
                        //    var f = fileinfos[i];
                        //    var ss = f.Size;
                        //    int h = 0;
                        //}
                    }

                }
                Marshal.FreeHGlobal(output_buffer);
                Marshal.FreeHGlobal(input_buffer);


                if (i == total_file_count * 0.21)
                    sp20 = DateTime.Now - s20;
                else if (i == total_file_count * 0.51)
                    sp50 = DateTime.Now - s50;
                else if (i == total_file_count * 0.91)
                    sp90 = DateTime.Now - s90;
            }

            Win32API.CloseHandle(hVolume);

            var di20 = sp20.TotalMilliseconds;
            var di50 = sp50.TotalMilliseconds;
            var di90 = sp90.TotalMilliseconds;

            //{
            //    int start = 27;
            //    StringBuilder buf = new StringBuilder();
            //    var stack = new List<Int32>();
            //    int count = fileinfos.Count();
            //    for (int i = start; i < count; i++) {
            //        if (fileinfos[i].Name != null) {

            //            stack.Clear();
            //            var parent = parentid[i];
            //            while ((parent != 0 && parent != 5) && parent < count) {
            //                stack.Add(parent);
            //                parent = parentid[parent];
            //            }
            //            stack.Reverse();

            //            buf.Remove(0, buf.Length);
            //            buf.Append(driveInfo.Name);
            //            foreach (var item in stack) {
            //                buf.Append(fileinfos[item].Name);
            //                buf.Append(@"\");
            //            }
            //            fileinfos[i].Path = buf.ToString();
            //        }
            //    }
            //}

            return fileinfos;
        }
    }
}
