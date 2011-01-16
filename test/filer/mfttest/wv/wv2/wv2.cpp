// wv21.cpp : コンソール アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

#include <windows.h>
#include <stdio.h>
#include <winioctl.h>
 
typedef struct {
      ULONG Type;
      USHORT UsaOffset;
      USHORT UsaCount;
      USN Usn;
} NTFS_RECORD_HEADER, *PNTFS_RECORD_HEADER;
 
// Type needed for interpreting the MFT-records
typedef struct {
    NTFS_RECORD_HEADER RecHdr;    // An NTFS_RECORD_HEADER structure with a Type of 'FILE'.
    USHORT SequenceNumber;        // Sequence number - The number of times
                                                  // that the MFT entry has been reused.
    USHORT LinkCount;             // Hard link count - The number of directory links to the MFT entry
    USHORT AttributeOffset;       // Offset to the first Attribute - The offset, in bytes,
                                                  // from the start of the structure to the first attribute of the MFT
    USHORT Flags;                 // Flags - A bit array of flags specifying properties of the MFT entry
                                                  // InUse 0x0001 - The MFT entry is in use
                                                  // Directory 0x0002 - The MFT entry represents a directory
    ULONG BytesInUse;             // Real size of the FILE record - The number of bytes used by the MFT entry.
    ULONG BytesAllocated;         // Allocated size of the FILE record - The number of bytes
                                                  // allocated for the MFT entry
    ULONGLONG BaseFileRecord;     // reference to the base FILE record - If the MFT entry contains
                                                  // attributes that overflowed a base MFT entry, this member
                                                  // contains the file reference number of the base entry;
                                                  // otherwise, it contains zero
    USHORT NextAttributeNumber;   // Next Attribute Id - The number that will be assigned to
                                                  // the next attribute added to the MFT entry.
    USHORT Pading;                // Align to 4 byte boundary (XP)
    ULONG MFTRecordNumber;        // Number of this MFT Record (XP)
    USHORT UpdateSeqNum;          //
} FILE_RECORD_HEADER, *PFILE_RECORD_HEADER;
 
typedef enum {
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
} ATTRIBUTE_TYPE, *PATTRIBUTE_TYPE;

typedef struct {
      ATTRIBUTE_TYPE AttributeType;
      ULONG Length;
      BOOLEAN Nonresident;
      UCHAR NameLength;
      USHORT NameOffset;
      USHORT Flags; // 0x0001 = Compressed
      USHORT AttributeNumber;
} ATTRIBUTE, *PATTRIBUTE;
 
// Convert the Win32 system error code to string
void ErrorMessage(DWORD dwCode);

int _tmain(int argc, _TCHAR* argv[])
{
      HANDLE hVolume;
      LPWSTR lpDrive = L"\\\\.\\c:";
      NTFS_VOLUME_DATA_BUFFER ntfsVolData = {0};
      BOOL bDioControl = FALSE;
      DWORD dwWritten = 0;
      DWORD lpBytesReturned = 0;
      FILE_RECORD_HEADER  FileRecHdr = {0};
      // Variables for MFT-reading
      NTFS_FILE_RECORD_INPUT_BUFFER   ntfsFileRecordInput;
      PNTFS_FILE_RECORD_OUTPUT_BUFFER ntfsFileRecordOutput;
     
      hVolume = CreateFile(lpDrive,
            GENERIC_READ | GENERIC_WRITE,
            FILE_SHARE_READ | FILE_SHARE_WRITE,
            NULL,
            OPEN_EXISTING,
            0,
            NULL);
 
      if(hVolume == INVALID_HANDLE_VALUE)
      {
            wprintf(L"CreateFile() failed!\n");
            ErrorMessage(GetLastError());
            if(CloseHandle(hVolume) != 0)
                  wprintf(L"hVolume handle was closed successfully!\n");
            else
            {
                  wprintf(L"Failed to close hVolume handle!\n");
                  ErrorMessage(GetLastError());
            }
            exit(1);
      }
      else
            wprintf(L"CreateFile() is pretty fine!\n");
     
      // get ntfsVolData by calling DeviceIoControl()
      // with CtlCode FSCTL_GET_NTFS_VOLUME_DATA
      // setup output buffer - FSCTL_GET_NTFS_FILE_RECORD depends on this
     
      // a call to FSCTL_GET_NTFS_VOLUME_DATA returns the structure NTFS_VOLUME_DATA_BUFFER
      bDioControl = DeviceIoControl(hVolume, FSCTL_GET_NTFS_VOLUME_DATA, NULL, 0, &ntfsVolData, sizeof(ntfsVolData), &dwWritten, NULL);
 
      // Failed or pending
      if(bDioControl == 0)
      {
            wprintf(L"DeviceIoControl() failed!\n");
            ErrorMessage(GetLastError());
            if(CloseHandle(hVolume) != 0)
                  wprintf(L"hVolume handle was closed successfully!\n");
            else
            {
                  wprintf(L"Failed to close hVolume handle!\n");
                  ErrorMessage(GetLastError());
            }
            exit(1);
      }
      else
            wprintf(L"1st DeviceIoControl(...,FSCTL_GET_NTFS_VOLUME_DATA,...) call is working...\n");
 
      //a call to FSCTL_GET_NTFS_VOLUME_DATA returns the structure NTFS_VOLUME_DATA_BUFFER
      ntfsFileRecordOutput = (PNTFS_FILE_RECORD_OUTPUT_BUFFER)
            malloc(sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1);
 
      if(ntfsFileRecordOutput == NULL)
            wprintf(L"Insufficient memory lol!\n");
      else
            wprintf(L"Memory allocated successfully!\n");
     
      // The MFT-record #5 is the root-dir???
      ntfsFileRecordInput.FileReferenceNumber.QuadPart = 5;
     
      bDioControl = DeviceIoControl(
            hVolume,
            FSCTL_GET_NTFS_FILE_RECORD,
        &ntfsFileRecordInput,
        sizeof(NTFS_FILE_RECORD_INPUT_BUFFER),
        ntfsFileRecordOutput,
        sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1,
        &lpBytesReturned, NULL);
     
      // Failed or pending
      if(bDioControl == 0)
      {
            wprintf(L"DeviceIoControl() failed!\n");
            ErrorMessage(GetLastError());
            if(CloseHandle(hVolume) != 0)
            {
                  wprintf(L"hVolume handle was closed successfully!\n");
            }
            else
            {
                  wprintf(L"Failed to close hVolume handle!\n");
                  ErrorMessage(GetLastError());
            }
            exit(1);
      }
      else
	  {

			PFILE_RECORD_HEADER p_file_record_header = (PFILE_RECORD_HEADER)ntfsFileRecordOutput->FileRecordBuffer;
			PATTRIBUTE attr = (PATTRIBUTE)((LPBYTE)p_file_record_header +  p_file_record_header->AttributeOffset); 

            wprintf(L"2nd DeviceIoControl(...,FSCTL_GET_NTFS_FILE_RECORD,...) call is working...\n");
     
            // read the record header from start of MFT-record
            if(!(memcpy(&FileRecHdr, &ntfsFileRecordOutput->FileRecordBuffer[0], sizeof(FILE_RECORD_HEADER))))
                  wprintf(L"memcpy() failed!\n");
            else
                  wprintf(L"memcpy() is OK!\n\n");
 
            wprintf(L"AttributeOffset: %u\n",FileRecHdr.AttributeOffset);
            wprintf(L"BaseFileRecord: %u\n",FileRecHdr.BaseFileRecord);
            wprintf(L"BytesAllocated: %u\n",FileRecHdr.BytesAllocated);
            wprintf(L"BytesInUse: %u\n",FileRecHdr.BytesInUse);
            wprintf(L"Flags: %u\n",FileRecHdr.Flags);
            wprintf(L"LinkCount: %u\n",FileRecHdr.LinkCount);
            wprintf(L"MFTRecordNumber: %u\n",FileRecHdr.MFTRecordNumber);
            wprintf(L"NextAttributeNumber: %u\n",FileRecHdr.NextAttributeNumber);
            wprintf(L"Pading: %u\n",FileRecHdr.Pading);
            wprintf(L"RecHdr: %u\n",FileRecHdr.RecHdr);
            wprintf(L"SequenceNumber: %u\n",FileRecHdr.SequenceNumber);
            wprintf(L"UpdateSeqNum: %u\n",FileRecHdr.UpdateSeqNum);

	  }
 
      if(CloseHandle(hVolume) != 0)
            wprintf(L"hVolume handle was closed successfully!\n");
      else
      {
            wprintf(L"Failed to close hVolume handle!\n");
            ErrorMessage(GetLastError());
      }
 
      // Free up the allocated memory by malloc()
      free(ntfsFileRecordOutput);
	return 0;
}

void ErrorMessage(DWORD dwCode)
{
    // get the error code...
    DWORD dwErrCode = dwCode;
    DWORD dwNumChar;
 
    LPWSTR szErrString = NULL;  // will be allocated and filled by FormatMessage
 
    dwNumChar = FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER |
                 FORMAT_MESSAGE_FROM_SYSTEM, // use windows internal message table
                 0,       // 0 since source is internal message table
                 dwErrCode, // this is the error code number
                 0,       // auto-determine language to use
                 (LPWSTR)&szErrString, // the messsage
                 0,                 // min size for buffer
                 0 );               // since getting message from system tables
      if(dwNumChar == 0)
            wprintf(L"FormatMessage() failed, error %u\n", GetLastError());
      //else
      //    wprintf(L"FormatMessage() should be fine!\n");
 
     wprintf(L"Error code %u:\n  %s\n", dwErrCode, szErrString) ;
 
      // This buffer used by FormatMessage()
    if(LocalFree(szErrString) != NULL)
            wprintf(L"Failed to free up the buffer, error %u\n", GetLastError());
      //else
      //    wprintf(L"Buffer has been freed\n");
  }