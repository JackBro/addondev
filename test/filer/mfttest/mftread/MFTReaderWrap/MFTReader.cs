﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace MFTReaderWrap {

    public class MFTReader {

        public event CallBackProc CallBackEvent;
        private Win32.MFT_FILE_INFO[] aryFileInfo;
        private DriveInfo drive;

        public MFTReader() {
        }

        public void Read(DriveInfo driveinfo) {
            if (driveinfo.DriveFormat != "NTFS") {
            }
            if (driveinfo.DriveType != DriveType.Fixed
                && driveinfo.DriveType !=DriveType.Removable) {
            }
            this.drive = driveinfo;
            string drive = driveinfo.Name[0].ToString();
            //drive = @"\\.\" + drive + ":";
            drive = @"\\.\" + driveinfo.Name.Substring(0,2);

            UInt32 errorCode = 0;
            IntPtr pListA = IntPtr.Zero;
            UInt64 recordsize = 0;

            StringBuilder message = new StringBuilder(255);
            int ret = Win32.GetMFTFileRecord(drive, out pListA, ref recordsize, CallBackEvent, ref errorCode);
            if (ret == 1) {
                Win32.FormatMessage(
                  Win32.FORMAT_MESSAGE_FROM_SYSTEM,
                  IntPtr.Zero,
                  (uint)errorCode,
                  0,
                  message,
                  message.Capacity,
                  IntPtr.Zero);
            } else {

                aryFileInfo = new Win32.MFT_FILE_INFO[recordsize];
                Int64 size = Marshal.SizeOf(typeof(Win32.MFT_FILE_INFO));
                for (Int64 i = 0; i < (Int64)recordsize; i++) {
                    IntPtr current = new IntPtr(pListA.ToInt64() + (size * i));
                    aryFileInfo[i] = (Win32.MFT_FILE_INFO)Marshal.PtrToStructure(current, typeof(Win32.MFT_FILE_INFO));
                }
            }
            Win32.freeBuffer(pListA);

        }

        public List<MFTFile> GetFile() {
            var baseticks = new DateTime(1601,01,01).Ticks;

            var list = new List<MFTFile>();
            
            int start = 27;
            if (aryFileInfo == null) return list;
            if (aryFileInfo.Count() < start) {
                list.Clear();
                return list;
            }

            for (int i = start; i < aryFileInfo.Count(); i++) {
                if (aryFileInfo[i].Name != null) {

                    List<Int64> stack = new List<Int64>();
                    //var record = aryFileInfo[i];
                    Int32 parent = (Int32)aryFileInfo[i].DirectoryFileReferenceNumber;
                    while (parent != 5) {
                        stack.Add(parent);
                        parent = (Int32)aryFileInfo[parent].DirectoryFileReferenceNumber;
                    }
                    stack.Reverse();

                    StringBuilder buf = new StringBuilder();
                    buf.Append(drive.Name);
                    foreach (var item in stack) {
                        buf.Append(aryFileInfo[item].Name);
                        buf.Append(@"\");
                    }
                    //buf.Append(aryFileInfo[i].Name);

                    var record = aryFileInfo[i];
                    MFTFile file = new MFTFile();
                    file.Path = buf.ToString();
                    file.Name = record.Name;
                    file.Size = (long)record.Size;
                    file.IsDirectory = record.IsDirectory;
                    file.CreationTime = new DateTime((long)record.CreationTime - baseticks);
                    file.LastAccessTime = new DateTime((long)record.LastAccessTime - baseticks);
                    file.LastWriteTime = new DateTime((long)record.LastWriteTime - baseticks);
                    list.Add(file);
                }
            }
            return list;
        }

    }
}
