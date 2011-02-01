using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace mftread {

    public struct MFT_FILE_INFO {
        public UInt64 ParentID;
        public bool IsDirectory;
        public String Name;
        public UInt64 Size;
        public UInt64 CreationTime;
        public UInt64 LastWriteTime;
    }

    public class MFTReader {
        IntPtr hVolume, secthVolume;
        Win32API.BOOT_BLOCK bootb;
        public unsafe MFT_FILE_INFO[] read(DriveInfo driveInfo) {
            string pathRoot = string.Concat(@"\\.\", driveInfo.Name.Substring(0, 2));

            hVolume = Win32API.CreateFile(
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

            //Win32API.BOOT_BLOCK bb = new Win32API.BOOT_BLOCK(); 
            IntPtr pbb = Marshal.AllocHGlobal(Marshal.SizeOf(typeof(Win32API.BOOT_BLOCK)));
            //Win32API.ZeroMemory(pbb, Marshal.SizeOf(typeof(Win32API.BOOT_BLOCK)));
            //Marshal.StructureToPtr(bb, pbb, true);

            uint read=0;;
            var readret = Win32API.ReadFile(hVolume, pbb, (uint)Marshal.SizeOf(typeof(Win32API.BOOT_BLOCK)), ref read, IntPtr.Zero);

             bootb = (Win32API.BOOT_BLOCK)Marshal.PtrToStructure(pbb, typeof(Win32API.BOOT_BLOCK));
             LoadMFT();
             //IntPtr pfile = Marshal.AllocHGlobal(Marshal.SizeOf(typeof(Win32API.FILE_RECORD_HEADER)));
             Win32API.FILE_RECORD_HEADER* file = (Win32API.FILE_RECORD_HEADER*)MFT;
             Win32API.STANDARD_INFORMATION* sisec;
             for (ulong index = 0; index < 10; index++) {
                 ReadFileRecord(index, file);
                 Win32API.RECORD_ATTRIBUTE* attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)file + file->AttributesOffset); 
                 if (file->Ntfs.Type == 1162627398) {//'ELIF'){
                     while (true) {
                         if (attr->AttributeType < 0 || (int)attr->AttributeType > 0x100) break;

                         switch (attr->AttributeType) {
                             case Win32API.AttributeType.AttributeFileName:
                                 Win32API.RESIDENT_ATTRIBUTE* regsttr = (Win32API.RESIDENT_ATTRIBUTE*)attr;
                                 Win32API.FILENAME_ATTRIBUTE fattr =
                                     (Win32API.FILENAME_ATTRIBUTE)Marshal.PtrToStructure((IntPtr)((((byte*)attr) + regsttr->ValueOffset)), typeof(Win32API.FILENAME_ATTRIBUTE));

                                 //fileinfos[i].ParentID = fattr.DirectoryFileReferenceNumber;
                                 //fileinfos[i].IsDirectory = ((p_file_record_header->Flags & 0x2) == 2);
                                 var n = fattr.Name;
                                 var s = fattr.DataSize;
                                 break;

                             case Win32API.AttributeType.AttributeStandardInformation:
                                 //var off = (Win32API.RESIDENT_ATTRIBUTE*)attr;
                                 sisec = (Win32API.STANDARD_INFORMATION*)((byte*)attr + ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueOffset);
                                 //Win32API.STANDARD_INFORMATION sattr =
                                 //(Win32API.STANDARD_INFORMATION)Marshal.PtrToStructure(new IntPtr(&attr + off->ValueOffset), typeof(Win32API.STANDARD_INFORMATION));
                                 var ctiem = sisec->CreationTime;
                                 var lwtiem = sisec->LastWriteTime;
                                 //fileinfos[i].CreationTime = si->CreationTime;
                                 //fileinfos[i].LastWriteTime = si->LastWriteTime;
                                 break;
                             case Win32API.AttributeType.AttributeData:
                                 if (attr->NonResident == 1) {

                                     //fileinfos[i].Size = ((Win32API.NONRESIDENT_ATTRIBUTE*)attr)->DataSize;
                                 }
                                 else {
                                     //fileinfos[i].Size = ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueLength;
                                 }
                                 break;
                             default:
                                 break;
                         }

                         if (attr->Length > 0 && attr->Length < file->BytesInUse)
                             attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + attr->Length);
                         else
                             if (attr->NonResident == 1)//TRUE)
                                 attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + sizeof(Win32API.NONRESIDENT_ATTRIBUTE));
                     }
                 }
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
            MFT_FILE_INFO[] fileinfos = new MFT_FILE_INFO[total_file_count];

            Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER ob = new Win32API.NTFS_FILE_RECORD_OUTPUT_BUFFER();
            int obSize = Marshal.SizeOf(ob) + ntfsVolData.BytesPerFileRecordSegment - 1;
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
                    Win32API.FSCTL_GET_NTFS_FILE_RECORD,
                    input_buffer,
                    mftRecordInputSize,
                    output_buffer,
                    obSize,
                    out lpBytesReturned,
                    IntPtr.Zero);

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

                fixed (byte* ptr = &outbuff.FileRecordBuffer[0]) {
                    p_file_record_header = (Win32API.FILE_RECORD_HEADER*)ptr;

                    Win32API.RECORD_ATTRIBUTE* attr = (Win32API.RECORD_ATTRIBUTE*)((int)ptr + p_file_record_header->AttributesOffset);

                    Win32API.STANDARD_INFORMATION* si;
                    if (p_file_record_header->Ntfs.Type == 1162627398) {//'ELIF'){
                        while (true) {
                            if (attr->AttributeType < 0 || (int)attr->AttributeType > 0x100) break;
                           
                            switch (attr->AttributeType) {
                                case Win32API.AttributeType.AttributeFileName:
                                    Win32API.RESIDENT_ATTRIBUTE* regsttr = (Win32API.RESIDENT_ATTRIBUTE*)attr;
                                    Win32API.FILENAME_ATTRIBUTE fattr =
                                        (Win32API.FILENAME_ATTRIBUTE)Marshal.PtrToStructure((IntPtr)((((byte*)attr) + regsttr->ValueOffset)), typeof(Win32API.FILENAME_ATTRIBUTE));

                                    fileinfos[i].ParentID = fattr.DirectoryFileReferenceNumber;
                                    fileinfos[i].IsDirectory = ((p_file_record_header->Flags & 0x2) == 2);
                                    //fileinfos[i].Name = fattr.Name;
                                    fileinfos[i].Size = fattr.DataSize;
                                    break;

                                case Win32API.AttributeType.AttributeStandardInformation:
                                    //var off = (Win32API.RESIDENT_ATTRIBUTE*)attr;
                                    si = (Win32API.STANDARD_INFORMATION*)((byte*)attr + ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueOffset);
                                    //Win32API.STANDARD_INFORMATION sattr =
                                    //(Win32API.STANDARD_INFORMATION)Marshal.PtrToStructure(new IntPtr(&attr + off->ValueOffset), typeof(Win32API.STANDARD_INFORMATION));
                                    //var ctiem = si->CreationTime;
                                    //var lwtiem = si->LastWriteTime;
                                    fileinfos[i].CreationTime = si->CreationTime;
                                    fileinfos[i].LastWriteTime = si->LastWriteTime;
                                    break;
                                case Win32API.AttributeType.AttributeData:
                                    if (attr->NonResident == 1) {

                                        fileinfos[i].Size = ((Win32API.NONRESIDENT_ATTRIBUTE*)attr)->DataSize;
                                    }
                                    else {
                                        fileinfos[i].Size = ((Win32API.RESIDENT_ATTRIBUTE*)attr)->ValueLength;
                                    }
                                    break;
                                default:
                                    break;
                            }

                            if (attr->Length > 0 && attr->Length < p_file_record_header->BytesInUse)
                                attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + attr->Length);
                            else
                                if (attr->NonResident == 1)//TRUE)
                                    attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + sizeof(Win32API.NONRESIDENT_ATTRIBUTE));
                        }

                        //if (fileinfos[i].Name != null && fileinfos[i].Name.Contains("wv.ncb")) {
                        //    var f = fileinfos[i];
                        //    var ss = f.Size;
                        //    int h = 0;
                        //}
                    }
                   
                }
                Marshal.FreeHGlobal(output_buffer);
                Marshal.FreeHGlobal(input_buffer);
            }

            Win32API.CloseHandle(hVolume);

            return fileinfos;
        }
        IntPtr MFT;
        UInt32 BytesPerFileRecord;
        public unsafe void LoadMFT() {
            //uint h = 0x100;
            BytesPerFileRecord = bootb.ClustersPerFileRecord < 0x80 
                ? bootb.ClustersPerFileRecord * bootb.SectorsPerCluster* bootb.BytesPerSector
                : (uint)(1 << (int)(0x100 - bootb.ClustersPerFileRecord));
            //if (bootb.ClustersPerFileRecord < 0x80) {
            //    BytesPerFileRecord = bootb.ClustersPerFileRecord * bootb.SectorsPerCluster * bootb.BytesPerSector;
            //}
            //else {
            //    int hh = (int)(0x100-bootb.ClustersPerFileRecord);
            //    BytesPerFileRecord = (uint)(1 << hh);
            //}
            //Win32API.FILE_RECORD_HEADER mft = new Win32API.FILE_RECORD_HEADER();
            int MFTSize= Marshal.SizeOf(BytesPerFileRecord);
            //MFT = Marshal.AllocHGlobal(MFTSize);
            //Win32API.ZeroMemory(MFT, MFTSize);
            MFT = Marshal.AllocHGlobal((int)BytesPerFileRecord);
            Win32API.ZeroMemory(MFT, (int)BytesPerFileRecord);
            
            //Marshal.StructureToPtr(mft, MFT, true);

            ReadSector((Int64)((bootb.MftStartLcn) * (bootb.SectorsPerCluster)),
                (BytesPerFileRecord) / (bootb.BytesPerSector), MFT);

            //Win32API.FILE_RECORD_HEADER* thismp = (Win32API.FILE_RECORD_HEADER*)MFT;
            //var nnn = (Win32API.FILE_RECORD_HEADER)Marshal.PtrToStructure(MFT, typeof(Win32API.FILE_RECORD_HEADER));
            //int bb = 0;
            FixupUpdateSequenceArray((Win32API.FILE_RECORD_HEADER*)MFT);
        }

        public unsafe void ReadSector(Int64 sector, ulong count, IntPtr buffer) {
            uint n = 0;

            Win32API.OVERLAPPED ov = new Win32API.OVERLAPPED();
            UInt64 q = (UInt64)sector * bootb.BytesPerSector;
            ov.OffsetHigh = (uint)(q & 0xffffffff00000000);
            ov.Offset = (uint)(q & 0x00000000ffffffff);
            Win32API.ReadFile(hVolume, buffer, (uint)count * bootb.BytesPerSector, ref n, ref ov);

            //System.Threading.NativeOverlapped ov = new System.Threading.NativeOverlapped();
            //ov.OffsetHigh = (int)(q & 0x0000ffff);
            //ov.OffsetLow = (int)(q & 0xffff0000);
            //IntPtr pov = Marshal.AllocHGlobal(Marshal.SizeOf(ov));
            //Win32API.ZeroMemory(pov, Marshal.SizeOf(ov));
            //Marshal.StructureToPtr(Marshal.SizeOf(ov), pov, true);
            //Win32API.ReadFile(hVolume, buffer, (uint)count * bootb.BytesPerSector, ref n, pov);


            //System.Threading.NativeOverlapped ov = new System.Threading.NativeOverlapped();
            //UInt64 q = (UInt64)sector * bootb.BytesPerSector;
            //ov.OffsetHigh = (int)(q & 0xffffffff00000000);
            //ov.OffsetLow = (int)(q & 0x00000000ffffffff);
            //Win32API.ReadFile(hVolume, buffer, (uint)count * bootb.BytesPerSector, IntPtr.Zero, &ov);
        }

        public unsafe void FixupUpdateSequenceArray(Win32API.FILE_RECORD_HEADER* file) {
            //ulong i = 0;
            //ushort* usa = (ushort*)((byte*)(file)+file->Ntfs.UsaOffset);
            //ushort* sector = (ushort*)(file);

            //for (i = 1; i < file->Ntfs.UsaCount; i++){
            //    sector[255] = usa[i];
            //    sector += 256;
            //}
            
        }

        unsafe void memcpy(byte* src, byte* dst, int bytesize) {
            byte* sentinel = src + bytesize;
            while (src < sentinel) {
                dst[0] = src[0];
                src++;
                dst++;
            }
        }

        static unsafe void CopyMemory(void* outDest, void* inSrc, uint inNumOfBytes) {
            // 転送先をuint幅にalignする
            const uint align = sizeof(uint) - 1;
            uint offset = (uint)outDest & align;
            // ↑ポインタは32bitとは限らないので本来このキャストはuintではダメだが、
            // 今は下位2bitだけあればいいのでこれでOK。
            if (offset != 0)
                offset = align - offset;
            offset = global::System.Math.Min(offset, inNumOfBytes);

            // 先頭の余り部分をbyteでちまちまコピー
            byte* srcBytes = (byte*)inSrc;
            byte* dstBytes = (byte*)outDest;
            for (uint i = 0; i < offset; i++)
                dstBytes[i] = srcBytes[i];

            // uintで一気に転送
            uint* dst = (uint*)((byte*)outDest + offset);
            uint* src = (uint*)((byte*)inSrc + offset);
            uint numOfUInt = (inNumOfBytes - offset) / sizeof(uint);
            for (uint i = 0; i < numOfUInt; i++)
                dst[i] = src[i];

            // 末尾の余り部分をbyteでちまちまコピー
            for (uint i = offset + numOfUInt * sizeof(uint); i < inNumOfBytes; i++)
                dstBytes[i] = srcBytes[i];
        }


        public unsafe void ReadFileRecord(ulong index, Win32API.FILE_RECORD_HEADER* file) {
            ulong clusters = bootb.ClustersPerFileRecord;
            if (clusters > 0x80)
                clusters = 1;

            IntPtr p = Marshal.AllocHGlobal((int)(bootb.BytesPerSector * bootb.SectorsPerCluster * (int)clusters));
            UInt64 vcn = (UInt64)(index) * BytesPerFileRecord / bootb.BytesPerSector / bootb.SectorsPerCluster;
            ReadVCN(file, Win32API.AttributeType.AttributeData, vcn, clusters, p);
            long m = (bootb.SectorsPerCluster * bootb.BytesPerSector / BytesPerFileRecord) - 1;
            long n = m > 0 ? ((long)(index) & m) : 0;
            //memcpy(file, p + n * BytesPerFileRecord, BytesPerFileRecord);

            //Marshal.Copy((int)p + n * BytesPerFileRecord, file, BytesPerFileRecord);
            //Win32API.FILE_RECORD_HEADER fattr = (Win32API.FILE_RECORD_HEADER)Marshal.PtrToStructure((IntPtr)((byte*)(p) + n * BytesPerFileRecord), typeof(Win32API.FILE_RECORD_HEADER));
            //Marshal.StructureToPtr(fattr, (IntPtr)file, true);
            //parentid[i] = (Int32)fattr.DirectoryFileReferenceNumber;

            //memcpy((byte*)(p.ToInt32() + n * BytesPerFileRecord), (byte*)file, (int)BytesPerFileRecord);
            CopyMemory(file, (void*)((byte*)p.ToPointer() + n * BytesPerFileRecord) , BytesPerFileRecord);
            int ll = 0;
            Marshal.FreeHGlobal(p);
            FixupUpdateSequenceArray(file);
        }

        public unsafe void ReadVCN(Win32API.FILE_RECORD_HEADER* file, Win32API.AttributeType type, 
            UInt64 vcn, ulong count, IntPtr buffer) {
            Win32API.NONRESIDENT_ATTRIBUTE* attr = (Win32API.NONRESIDENT_ATTRIBUTE*)FindAttribute(file, type, null);
            ReadExternalAttribute(attr, vcn, count, buffer);
        }

        public unsafe Win32API.RECORD_ATTRIBUTE* FindAttribute(Win32API.FILE_RECORD_HEADER* file, 
            Win32API.AttributeType type,
            string name) 
        {
            Win32API.RECORD_ATTRIBUTE* attr=null;
            //for (attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)file + file->AttributesOffset);
            //    (int)attr->AttributeType != -1;
            //    attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)attr + attr->Length)) {
            for (attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)(file) + file->AttributesOffset);
                (int)attr->AttributeType != -1;
                attr = (Win32API.RECORD_ATTRIBUTE*)((byte*)(attr) + attr->Length)) {
                if (attr->AttributeType == type) {
                    if (name == null && attr->NameLength == 0)
                        return attr;
                }
            }
            return attr;
        }

        public unsafe void ReadExternalAttribute(
            Win32API.NONRESIDENT_ATTRIBUTE* attr, 
            UInt64 vcn, 
            ulong count, 
            IntPtr buffer) {

              UInt64 lcn=0, runcount=0;
              ulong readcount, left;
              byte* bytes = (byte*)(buffer.ToPointer());
              for(left = count; left > 0; left -= readcount){
                    FindRun(attr, vcn, ref lcn, ref runcount);
                    readcount = (Math.Min(runcount, left));
                    ulong n = readcount * bootb.BytesPerSector * bootb.SectorsPerCluster;

                    if (lcn == 0) {
                        //memset(bytes, 0, n);
                        Win32API.ZeroMemory(new IntPtr(bytes), (int)n);
                    }
                    else {
                        ReadLCN(lcn, readcount, (IntPtr)bytes);
                    }          
                    vcn += readcount;
                    bytes += n;
              }
              //buffer = new IntPtr(bytes);
        }

        public void ReadLCN(ulong lcn, ulong count, IntPtr buffer) {
            ReadSector((long)(lcn * bootb.SectorsPerCluster), count * bootb.SectorsPerCluster, buffer);
        }

        public unsafe bool FindRun(Win32API.NONRESIDENT_ATTRIBUTE* attr, UInt64 vcn, ref UInt64 lcn, ref UInt64 count) {
            byte* run = null;
              lcn = 0;
              ulong baseoff = attr->LowVCN;
         
              if (vcn < attr->LowVCN || vcn > attr->HighVCN)
                    return false;

              for (run = (byte*)((byte*)(attr) + attr->RunArrayOffset); *run != 0; run += RunLength(run))
              {
                    lcn += RunLCN(run);
                    count = RunCount(run);
         
                    if (baseoff <= vcn && vcn < baseoff + count)
                    {
                          lcn = RunLCN(run) == 0 ? 0 : lcn + vcn - baseoff;
                          count -= (vcn - baseoff);
                          return true;
                    }
                    else
                          baseoff += count;
              }
              return false;
        }

        unsafe ulong RunLength(byte* run) {
              return (ulong)((*run & 0xf) + ((*run >> 4) & 0xf) + 1);
        }

        unsafe ulong RunLCN(byte* run) {
              long i = 0;
              byte n1 = 0 , n2 = 0;
              Int64 lcn = 0;

              n1 = (byte)(*run & 0xf);
              n2 = (byte)((*run >> 4) & 0xf);
         
              lcn = n2 == 0 ? 0 : (byte)(run[n1 + n2]);
             
              for (i = n1 + n2 - 1; i > n1; i--)
                    lcn = (lcn << 8) + run[i];
              return (ulong)lcn;
        }

        unsafe UInt64 RunCount(byte* run) {
            byte n = (byte)(*run & 0xf);
              UInt64 count = 0;
              ulong i;
             
              for (i = n; i > 0; i--)
                    count = (count << 8) + run[i];
         
              return count;
        }
    }

}
