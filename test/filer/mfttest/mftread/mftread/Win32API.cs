﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Threading;

using BOOLEAN = System.Boolean;
using BYTE = System.Byte;
using DWORD = System.UInt32;
using DWORDLONG = System.UInt64;
using HANDLE = System.IntPtr;
using LARGE_INTEGER = System.Int64;
using LONGLONG = System.Int64;
using UINT32 = System.UInt32;
using ULONG = System.UInt32;
using USHORT = System.UInt16;
using USN = System.Int64;
using WCHAR = System.Char;
using WORD = System.Int16;


namespace mftread {

    public class Win32API {

        public const Int32 INVALID_HANDLE_VALUE = -1;

        public const UInt32 GENERIC_READ = 0x80000000;
        public const UInt32 GENERIC_WRITE = 0x40000000;
        public const UInt32 FILE_SHARE_READ = 0x00000001;
        public const UInt32 FILE_SHARE_WRITE = 0x00000002;

        public const UInt32 CREATE_NEW = 1;
        public const UInt32 CREATE_ALWAYS = 2;
        public const UInt32 OPEN_EXISTING = 3;
        public const UInt32 OPEN_ALWAYS = 4;
        public const UInt32 TRUNCATE_EXISTING = 5;

        public const int FSCTL_GET_NTFS_VOLUME_DATA = 0x00090064;
        public const int FSCTL_GET_NTFS_FILE_RECORD = 0x00090068;

        [DllImport("kernel32.dll", SetLastError = true)]
        public static extern IntPtr
            CreateFile(string lpFileName,
            uint dwDesiredAccess,
            uint dwShareMode,
            IntPtr lpSecurityAttributes,
            uint dwCreationDisposition,
            uint dwFlagsAndAttributes,
            IntPtr hTemplateFile);

        [DllImport("kernel32.dll", ExactSpelling = true, SetLastError = true, CharSet = CharSet.Auto)]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool DeviceIoControl(
            IntPtr hDevice,
            UInt32 dwIoControlCode,
            IntPtr lpInBuffer,
            Int32 nInBufferSize,
            IntPtr lpOutBuffer,
            Int32 nOutBufferSize,
            out uint lpBytesReturned,
            IntPtr lpOverlapped);

        [DllImport("Kernel32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern bool DeviceIoControl(
                IntPtr hDevice,
                uint dwIoControlCode,
                ref long InBuffer,
                int nInBufferSize,
                ref long OutBuffer,
                int nOutBufferSize,
                ref int pBytesReturned,
                [In] ref NativeOverlapped lpOverlapped);

        [DllImport("kernel32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool
            CloseHandle(
            IntPtr hObject);

        [StructLayout(LayoutKind.Explicit, Size = 20)]
        public struct OVERLAPPED {
            [FieldOffset(0)]
            public uint Internal;

            [FieldOffset(4)]
            public uint InternalHigh;

            [FieldOffset(8)]
            public uint Offset;

            [FieldOffset(12)]
            public uint OffsetHigh;

            [FieldOffset(8)]
            public IntPtr Pointer;

            [FieldOffset(16)]
            public IntPtr hEvent;
        }
        //[StructLayout(LayoutKind.Sequential)]
        //public struct OVERLAPPED {
        //    public uint Internal;
        //    public uint InternalHigh;
        //    public uint Offset;
        //    public uint OffsetHigh;
        //    public IntPtr hEvent;
        //}
        //[StructLayout(LayoutKind.Sequential, Pack = 8)]
        //public struct OVERLAPPED {
        //    private IntPtr InternalLow;
        //    private IntPtr InternalHigh;
        //    public long Offset;
        //    public IntPtr EventHandle;
        //}
        //public struct OVERLAPPED {
        //    public UIntPtr Internal;
        //    public UIntPtr InternalHigh;
        //    public uint Offset;
        //    public uint OffsetHigh;
        //    public IntPtr EventHandle;
        //}

        [DllImport("kernel32", SetLastError = true)]
        public static extern bool ReadFile(
         IntPtr hFile,
         IntPtr aBuffer,
         UInt32 cbToRead,
         ref UInt32 cbThatWereRead,
         IntPtr pOverlapped);

        [DllImport("kernel32", SetLastError = true)]
        public static extern bool ReadFile(
         IntPtr hFile,
         IntPtr aBuffer,
         UInt32 cbToRead,
         ref UInt32 cbThatWereRead,
         ref OVERLAPPED lpOverlapped);

        [DllImport("kernel32.dll", SetLastError = true)]
        public static extern unsafe int ReadFile(IntPtr handle, IntPtr bytes, uint numBytesToRead,
          IntPtr numBytesRead, NativeOverlapped* overlapped);


        [DllImport("kernel32.dll")]
        public static extern void ZeroMemory(IntPtr ptr, int size);


        [StructLayout(LayoutKind.Sequential)]
        public struct NTFS_VOLUME_DATA_BUFFER {
            public Int64 VolumeSerialNumber;
            public Int64 NumberSectors;
            public Int64 TotalClusters;
            public Int64 FreeClusters;
            public Int64 TotalReserved;
            public Int32 BytesPerSector;
            public Int32 BytesPerCluster;
            public Int32 BytesPerFileRecordSegment;
            public Int32 ClustersPerFileRecordSegment;
            public Int64 MftValidDataLength;
            public Int64 MftStartLcn;
            public Int64 Mft2StartLcn;
            public Int64 MftZoneStart;
            public Int64 MftZoneEnd;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct NTFS_FILE_RECORD_INPUT_BUFFER {
            public LARGE_INTEGER FileReferenceNumber;
        }

        [StructLayout(LayoutKind.Sequential)]
        public struct NTFS_FILE_RECORD_OUTPUT_BUFFER {
            public LARGE_INTEGER FileReferenceNumber;
            public DWORD FileRecordLength;
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 1)]
            public BYTE[] FileRecordBuffer;

            internal static NTFS_FILE_RECORD_OUTPUT_BUFFER FromPtr(System.IntPtr ptr) {
                NTFS_FILE_RECORD_OUTPUT_BUFFER result = default(NTFS_FILE_RECORD_OUTPUT_BUFFER);
                result = (NTFS_FILE_RECORD_OUTPUT_BUFFER)Marshal.PtrToStructure(ptr, typeof(NTFS_FILE_RECORD_OUTPUT_BUFFER));
                unsafe {
                    byte* pFileRecord = (byte*)ptr + (int)Marshal.OffsetOf(typeof(NTFS_FILE_RECORD_OUTPUT_BUFFER), "FileRecordBuffer");
                    result.FileRecordBuffer = new BYTE[result.FileRecordLength];
                    Marshal.Copy(new System.IntPtr(pFileRecord), result.FileRecordBuffer, 0, (int)result.FileRecordLength);
                }
                return result;
            }
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct NTFS_RECORD_HEADER {
            public uint Type;
            public ushort UsaOffset;
            public ushort UsaCount;
            public ulong USN;
        }

        //[StructLayout(LayoutKind.Sequential, Pack = 1)]
        //public struct FILE_RECORD_HEADER {
        //    public NTFS_RECORD_HEADER Ntfs;
        //    public ushort SequenceNumber;
        //    public ushort LinkCount;
        //    public ushort AttributesOffset;
        //    public ushort Flags;
        //    public ulong BytesInUse;
        //    public ulong BytesAllocated;
        //    public UInt64 BaseFileRecord;
        //    public ushort NextAttributeNumber;
        //}
        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct FILE_RECORD_HEADER {
            /// <summary>
            /// An NTFS_RECORD_HEADER structure with a member Type of 'FILE'.
            /// </summary>
            public NTFS_RECORD_HEADER Ntfs;

            /// <summary>
            /// The number of time that the MFT entry has been used.
            /// </summary>
            public ushort SequenceNumber;

            /// <summary>
            /// The number of directory links to the MFT entry.
            /// </summary>
            public ushort LinkCount;

            /// <summary>
            /// Represent the offset in b ytes from the start of the structure to the first attribute of the MFT entry.
            /// </summary>
            public ushort AttributesOffset;

            /// <summary>
            /// A bit array of flags specifying properties of the MFT entry.
            /// The values can be:
            /// InUse = 0x1
            /// Directory = 0x2
            /// </summary>
            public ushort Flags;

            /// <summary>
            /// The number of bytes used by the MFT entry.
            /// </summary>
            public uint BytesInUse;

            /// <summary>
            /// The number of bytes allocated for the MFT entry.
            /// </summary>
            public uint BytesAllocated;

            /// <summary>
            /// If the MFT entry contains attributes that overflowed a base MFT enrty,
            /// this member contains the file reference number of the base entry.
            /// Otherwise it contains zero.
            /// </summary>
            public ulong BaseFileRecord;

            /// <summary>
            /// The number that will be assigned to the next attribute added to the MFT entry.
            /// </summary>
            public ushort NextAttributeNumber;
        } 

        //[Flags()]
        public enum AttributeType : int {
            AttributeStandardInformation = 0x10,
            AttributeAttributeList = 0x20,
            AttributeFileName = 0x30,
            AttributeObjectId = 0x40,
            AttributeSecurityDescriptor = 0x50,
            AttributeVolumeName = 0x60,
            AttributeVolumeInformation = 0x70,
            AttributeData = 0x80,
            AttributeIndexRoot = 0x90,
            AttributeIndexAllocation = 0xA0,
            AttributeBitmap = 0xB0,
            AttributeReparsePoint = 0xC0,
            AttributeEAInformation = 0xD0,
            AttributeEA = 0xE0,
            AttributePropertySet = 0xF0,
            AttributeLoggedUtilityStream = 0x100
        } 

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct RECORD_ATTRIBUTE {
            public AttributeType AttributeType;
            public int Length;
            public byte NonResident;
            public byte NameLength;
            public ushort NameOffset;
            public ushort Flags;
            public ushort AttributeNumber;
        }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Unicode, Pack = 1)]
        public unsafe struct FILENAME_ATTRIBUTE {
            public UInt64 DirectoryFileReferenceNumber;
            public UInt64 CreationTime;
            public UInt64 ChangeTime;
            public UInt64 LastWriteTime;
            public UInt64 LastAccessTime;
            public UInt64 AllocatedSize;
            public UInt64 DataSize;
            public ULONG FileAttributes;
            public ULONG AlignmentOrReserved;
            public Byte NameLength;
            public Byte NameType;
            [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 260)]
            public string Name; //WCHAR
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct RESIDENT_ATTRIBUTE {
            public RECORD_ATTRIBUTE Attribute;
            public uint ValueLength;
            public ushort ValueOffset;
            public ushort Flags;
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public unsafe struct NONRESIDENT_ATTRIBUTE {
            public RECORD_ATTRIBUTE Attribute;
            public ulong LowVCN;
            public ulong HighVCN;
            public ushort RunArrayOffset;
            public byte CompressionUnit;
            public fixed byte Padding[5];
            public ulong AllocatedSize;
            public ulong DataSize;
            public ulong InitializedSize;
            public ulong CompressedSize;
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public unsafe struct STANDARD_INFORMATION {
            public UInt64 CreationTime;
            public UInt64 ChangeTime;
            public UInt64 LastWriteTime;
            public UInt64 LastAccessTime;
            public ulong FileAttributes;
            public fixed ulong Alignment[3];
            public ulong QuotaID;
            public ulong SecurityID;
            public Int64 QuotaCharge;
            public Int64 USN;
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct BOOT_BLOCK {
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 3)]
            public byte[] Jump;
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 8)]
            public byte[] Format;
            public ushort BytesPerSector;
            public byte SectorsPerCluster;
            public ushort BootSectors;
            public byte Mbz1;
            public ushort Mbz2;
            public ushort Reserved1;
            public Char MediaType;
            public ushort Mbz3;
            public ushort SectorsPerTrack;
            public ushort NumberOfHeads;
            public uint PartitionOffset;
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 8)]
            public byte[] Reserved2;
            public ulong TotalSectors;
            public ulong MftStartLcn;
            public ulong Mft2StartLcn;
            public uint ClustersPerFileRecord;
            public uint ClustersPerIndexBlock;
            public ulong VolumeSerialNumber;
            [MarshalAs(UnmanagedType.ByValArray, SizeConst = 430)]
            public byte[] BootCode;
            public ushort BootSignature;
        } 
    }
}
