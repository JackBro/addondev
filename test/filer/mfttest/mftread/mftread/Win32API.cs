using System;
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

        //[DllImport("Kernel32.dll", CharSet = CharSet.Auto, SetLastError = true)]
        //public static extern bool DeviceIoControl(
        //        IntPtr hDevice,
        //        uint dwIoControlCode,
        //        ref long InBuffer,
        //        int nInBufferSize,
        //        ref long OutBuffer,
        //        int nOutBufferSize,
        //        ref int pBytesReturned,
        //        [In] ref NativeOverlapped lpOverlapped);

        [DllImport("kernel32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool
            CloseHandle(
            IntPtr hObject);

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
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct NTFS_RECORD_HEADER {
            public uint Type;
            public ushort UsaOffset;
            public ushort UsaCount;
            public ulong USN;
        }

        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        public struct FILE_RECORD_HEADER {
            public NTFS_RECORD_HEADER Ntfs;
            public ushort SequenceNumber;
            public ushort LinkCount;
            public ushort AttributesOffset;
            public ushort Flags;
            public uint BytesInUse;
            public uint BytesAllocated;
            public ulong BaseFileRecord;
            public ushort NextAttributeNumber;
        }

        [Flags()]
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
            public short AttributeNumber;
        } 
    }
}
