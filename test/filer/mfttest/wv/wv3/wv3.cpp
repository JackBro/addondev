// wv3.cpp : コンソール アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

#include <windows.h>
#include <stdio.h>
#include <winioctl.h>
#include <crtdbg.h>

#ifdef _DEBUG
#define   new                   new(_NORMAL_BLOCK, __FILE__, __LINE__)
#define   malloc(s)             _malloc_dbg(s, _NORMAL_BLOCK, __FILE__, __LINE__)
#define   calloc(c, s)          _calloc_dbg(c, s, _NORMAL_BLOCK, __FILE__, __LINE__)
#define   realloc(p, s)         _realloc_dbg(p, s, _NORMAL_BLOCK, __FILE__, __LINE__)
#define   _recalloc(p, c, s)    _recalloc_dbg(p, c, s, _NORMAL_BLOCK, __FILE__, __LINE__)
#define   _expand(p, s)         _expand_dbg(p, s, _NORMAL_BLOCK, __FILE__, __LINE__)
#endif
 
// These types should be stored in separate
// include file, not done here
typedef struct {
      ULONG Type;
      USHORT UsaOffset;
      USHORT UsaCount;
      USN Usn;
} NTFS_RECORD_HEADER, *PNTFS_RECORD_HEADER;
 
typedef struct {
      NTFS_RECORD_HEADER Ntfs;
      USHORT SequenceNumber;
      USHORT LinkCount;
      USHORT AttributesOffset;
      USHORT Flags; // 0x0001 = InUse, 0x0002 = Directory
      ULONG BytesInUse;
      ULONG BytesAllocated;
      ULONGLONG BaseFileRecord;
      USHORT NextAttributeNumber;
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
 
typedef struct {
      ATTRIBUTE Attribute;
      ULONG ValueLength;
      USHORT ValueOffset;
      USHORT Flags; // 0x0001 = Indexed
} RESIDENT_ATTRIBUTE, *PRESIDENT_ATTRIBUTE;
 
typedef struct {
      ATTRIBUTE Attribute;
      ULONGLONG LowVcn;
      ULONGLONG HighVcn;
      USHORT RunArrayOffset;
      UCHAR CompressionUnit;
      UCHAR AlignmentOrReserved[5];
      ULONGLONG AllocatedSize;
      ULONGLONG DataSize;
      ULONGLONG InitializedSize;
      ULONGLONG CompressedSize; // Only when compressed
} NONRESIDENT_ATTRIBUTE, *PNONRESIDENT_ATTRIBUTE;
 
typedef struct {
      ULONGLONG CreationTime;
      ULONGLONG ChangeTime;
      ULONGLONG LastWriteTime;
      ULONGLONG LastAccessTime;
      ULONG FileAttributes;
      ULONG AlignmentOrReservedOrUnknown[3];
      ULONG QuotaId; // NTFS 3.0 only
      ULONG SecurityId; // NTFS 3.0 only
      ULONGLONG QuotaCharge; // NTFS 3.0 only
      USN Usn; // NTFS 3.0 only
} STANDARD_INFORMATION, *PSTANDARD_INFORMATION;
 
typedef struct {
      ATTRIBUTE_TYPE AttributeType;
      USHORT Length;
      UCHAR NameLength;
      UCHAR NameOffset;
      ULONGLONG LowVcn;
      ULONGLONG FileReferenceNumber;
      USHORT AttributeNumber;
      USHORT AlignmentOrReserved[3];
} ATTRIBUTE_LIST, *PATTRIBUTE_LIST;
 
typedef struct {
      ULONGLONG DirectoryFileReferenceNumber;
      ULONGLONG CreationTime; // Saved when filename last changed
      ULONGLONG ChangeTime; // ditto
      ULONGLONG LastWriteTime; // ditto
      ULONGLONG LastAccessTime; // ditto
      ULONGLONG AllocatedSize; // ditto
      ULONGLONG DataSize; // ditto
      ULONG FileAttributes; // ditto
      ULONG AlignmentOrReserved;
      UCHAR NameLength;
      UCHAR NameType; // 0x01 = Long, 0x02 = Short
      WCHAR Name[1];
} FILENAME_ATTRIBUTE, *PFILENAME_ATTRIBUTE;

typedef struct {
    GUID ObjectId;
    union {
        struct {
            GUID BirthVolumeId;
            GUID BirthObjectId;
            GUID DomainId;
        } ;
        UCHAR ExtendedInfo[48];
    };
} OBJECTID_ATTRIBUTE, *POBJECTID_ATTRIBUTE;

typedef struct {
	//GUID ObjectId;
	ULONGLONG DirectoryFileReferenceNumber;
	bool IsDirectory;

	ULONGLONG Size; 
	ULONGLONG CreationTime;
	ULONGLONG LastWriteTime;
	ULONGLONG LastAccessTime;
	LPWSTR Name;

} MFT_FILE_INFO, *PMFT_FILE_INFO;

#define WIN32_NAME 1
 
// Format the Win32 system error code to string
void ErrorMessage(DWORD dwCode);

BOOL FixFileRecord(PFILE_RECORD_HEADER file);

// Fixing the offset
#define FIXOFFSET(x, y) ((CHAR*)(x) + (y))
 
int _tmain(int argc, _TCHAR* argv[])
{
      HANDLE hVolume;
      LPWSTR lpDrive = L"\\\\.\\c:";
      NTFS_VOLUME_DATA_BUFFER ntfsVolData = {0};
      BOOL bDioControl = FALSE;
      DWORD dwWritten = 0;
      LARGE_INTEGER num;
      LONGLONG total_file_count, i;
      NTFS_FILE_RECORD_INPUT_BUFFER mftRecordInput;
      PNTFS_FILE_RECORD_OUTPUT_BUFFER output_buffer;
     
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
 
      // a call to FSCTL_GET_NTFS_VOLUME_DATA returns the structure NTFS_VOLUME_DATA_BUFFER
      bDioControl = DeviceIoControl(hVolume, FSCTL_GET_NTFS_VOLUME_DATA, NULL, 0, &ntfsVolData,
            sizeof(NTFS_VOLUME_DATA_BUFFER), &dwWritten, NULL);
 
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
            wprintf(L"DeviceIoControl(...,FSCTL_GET_NTFS_VOLUME_DATA,...) is working...\n\n");
     
      wprintf(L"Volume Serial Number: 0X%.8X%.8X\n",ntfsVolData.VolumeSerialNumber.HighPart,ntfsVolData.VolumeSerialNumber.LowPart);
      wprintf(L"The number of bytes in a cluster: %u\n",ntfsVolData.BytesPerCluster);
      wprintf(L"The number of bytes in a file record segment: %u\n",ntfsVolData.BytesPerFileRecordSegment);
      wprintf(L"The number of bytes in a sector: %u\n",ntfsVolData.BytesPerSector);
      wprintf(L"The number of clusters in a file record segment: %u\n",ntfsVolData.ClustersPerFileRecordSegment);
      wprintf(L"The number of free clusters in the specified volume: %u\n",ntfsVolData.FreeClusters);
      wprintf(L"The starting logical cluster number of the master file table mirror: 0X%.8X%.8X\n",ntfsVolData.Mft2StartLcn.HighPart, ntfsVolData.Mft2StartLcn.LowPart);
      wprintf(L"The starting logical cluster number of the master file table: 0X%.8X%.8X\n",ntfsVolData.MftStartLcn.HighPart, ntfsVolData.MftStartLcn.LowPart);
      wprintf(L"The length of the master file table, in bytes: %u\n",ntfsVolData.MftValidDataLength);
      wprintf(L"The ending logical cluster number of the master file table zone: 0X%.8X%.8X\n",ntfsVolData.MftZoneEnd.HighPart, ntfsVolData.MftZoneEnd.LowPart);
      wprintf(L"The starting logical cluster number of the master file table zone: 0X%.8X%.8X\n",ntfsVolData.MftZoneStart.HighPart, ntfsVolData.MftZoneStart.LowPart);
      wprintf(L"The number of sectors: %u\n",ntfsVolData.NumberSectors);
      wprintf(L"Total Clusters (used and free): %u\n",ntfsVolData.TotalClusters);
      wprintf(L"The number of reserved clusters: %u\n\n",ntfsVolData.TotalReserved);
 
      num.QuadPart = 1024; // 1024 or 2048
     
      // We divide the MftValidDataLength (Master file table length) by 1024 to find
      // the total entry count for the MFT
      total_file_count = (ntfsVolData.MftValidDataLength.QuadPart/num.QuadPart);
 
	  //total_file_count = 50; //test
      wprintf(L"Total file count = %u\n", total_file_count);
 
	  MFT_FILE_INFO* pfile_info = (MFT_FILE_INFO*)malloc(sizeof(MFT_FILE_INFO)*total_file_count);
      for(i = 0; i < total_file_count;i++)
      {
            mftRecordInput.FileReferenceNumber.QuadPart = i;
 //int kkk = sizeof(mftRecordInput);
            // prior to calling the DeviceIoControl() we need to load
            // an input record with which entry number we want
 //int kk = sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1;
            // setup outputbuffer - FSCTL_GET_NTFS_FILE_RECORD depends on this
            output_buffer = (PNTFS_FILE_RECORD_OUTPUT_BUFFER)malloc(sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1);
 
            if(output_buffer == NULL)
            {
                  wprintf(L"malloc() failed - insufficient memory!\n");
                  ErrorMessage(GetLastError());
                  exit(1);
            }
			DWORD ooo = FSCTL_GET_NTFS_FILE_RECORD;
            bDioControl = DeviceIoControl(hVolume, 
				FSCTL_GET_NTFS_FILE_RECORD,
				&mftRecordInput,
                  sizeof(mftRecordInput), 
				  output_buffer,
                  sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER) + (ntfsVolData.BytesPerFileRecordSegment)- 1, 
				  &dwWritten, 
				  NULL);
 
            // More data will make DeviceIoControl() fails...
            /*if(bDioControl == 0)
            {
                  wprintf(L"DeviceIoControl(...,FSCTL_GET_NTFS_FILE_RECORD,...) failed!\n");
                  ErrorMessage(GetLastError());
                  exit(1);
            }*/
 
            // FSCTL_GET_NTFS_FILE_RECORD retrieves one MFT entry
            // FILE_RECORD_HEADER is the Base struct for the MFT entry
            // that we will work from
            PFILE_RECORD_HEADER p_file_record_header =       (PFILE_RECORD_HEADER)output_buffer->FileRecordBuffer;

			PFILENAME_ATTRIBUTE fn;
			PSTANDARD_INFORMATION si;
			POBJECTID_ATTRIBUTE objid;

			PATTRIBUTE attr = (PATTRIBUTE)((LPBYTE)p_file_record_header +  p_file_record_header->AttributesOffset); 
PRESIDENT_ATTRIBUTE preg;
			int stop = min(8,p_file_record_header->NextAttributeNumber);
			if(p_file_record_header->Ntfs.Type =='ELIF'){
				while(true){
					if (attr->AttributeType<0 || attr->AttributeType>0x100) break;
					
					switch(attr->AttributeType)
					{
						case AttributeFileName:
							preg =  PRESIDENT_ATTRIBUTE(attr);
							fn = PFILENAME_ATTRIBUTE(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
							if (fn->NameType & WIN32_NAME || fn->NameType == 0)
							{
								if(p_file_record_header->Flags & 0x1){
									wprintf(L"FileName InUse\n") ;
								}else{
									wprintf(L"FileName NOt In Use\n") ;
								}

								if(p_file_record_header->Flags & 0x2){
									wprintf(L"FileName Directory\n") ;
								}else{
									wprintf(L"FileName File\n") ;
								}
								wprintf(L"FileName i : %u\n", i );
								wprintf(L"FileName DirectoryFileReferenceNumber : %u\n", fn->DirectoryFileReferenceNumber );
								wprintf(L"FileName DataSize : %u\n", fn->DataSize );
								

								fn->Name[fn->NameLength] = L'\0';
								wprintf(L"FileName Name :%s\n", fn->Name) ;

								//pfile_info[i].Name = (LPWSTR)malloc((lstrlenW(fn->Name)+1)*sizeof(WCHAR)); 
								pfile_info[i].Name = new WCHAR[lstrlenW(fn->Name)+1]; 
								lstrcpyW(pfile_info[i].Name , fn->Name);
								pfile_info[i].Size = fn->DataSize;
								//LPWSTR lp = fn->Name;
								//wprintf(L"FileName Name :%s\n", lp) ;
								if (lstrcmpW(fn->Name, L"3-chmode1.raw")==0) {
								//if(i==28635){
									int h=0;
								}
							}
							break;
						case AttributeStandardInformation:
 preg = PRESIDENT_ATTRIBUTE(attr);
							si = PSTANDARD_INFORMATION(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
							wprintf(L"#####StandardInformation CreationTime : %u\n", si->CreationTime );
							break;
						//case AttributeObjectId:
						//	objid = POBJECTID_ATTRIBUTE(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
						//	wprintf(L"ObjectId Data1 : %u\n", objid->ObjectId.Data1 );
						//	wprintf(L"ObjectId Data2 : %u\n", objid->ObjectId.Data2 );
						//	wprintf(L"ObjectId Data3 : %u\n", objid->ObjectId.Data3 );
						//	wprintf(L"ObjectId Data4[0] : %u\n", objid->ObjectId.Data4[0] );
						//	wprintf(L"ObjectId Data4[1] : %u\n", objid->ObjectId.Data4[1] );
						//	wprintf(L"ObjectId Data4[2] : %u\n", objid->ObjectId.Data4[2] );
						//	wprintf(L"ObjectId Data4[3] : %u\n", objid->ObjectId.Data4[3] );
						//	wprintf(L"ObjectId Data4[3] : %u\n", objid->ObjectId.Data4[4] );
						//	wprintf(L"ObjectId Data4[3] : %u\n", objid->ObjectId.Data4[5] );
						//	wprintf(L"ObjectId Data4[3] : %u\n", objid->ObjectId.Data4[6] );
						//	wprintf(L"ObjectId Data4[3] : %u\n", objid->ObjectId.Data4[7] );
						//	break;
						default:
							break;
					};
					
					if (attr->Length>0 && attr->Length < p_file_record_header->BytesInUse)
						attr = PATTRIBUTE(PUCHAR(attr) + attr->Length);
					else
						if (attr->Nonresident == TRUE)
							attr = PATTRIBUTE(PUCHAR(attr) + sizeof(NONRESIDENT_ATTRIBUTE));
				}
			}
			wprintf(L"\n");
			free(output_buffer);
      }
     
      // Let verify
      wprintf(L"i\'s count = %u\n", i);
 
      //======================
      if(CloseHandle(hVolume) != 0)
            wprintf(L"hVolume handle was closed successfully!\n");
      else
      {
            wprintf(L"Failed to close hVolume handle!\n");
            ErrorMessage(GetLastError());
      }
      // De-allocate the memory by malloc()
      //free(output_buffer);
	  //free(pfile_info);
 
	  _CrtDumpMemoryLeaks();
      return 0;
}

BOOL FixFileRecord(PFILE_RECORD_HEADER file)
{
	//int sec = 2048;
	PUSHORT usa = PUSHORT(PUCHAR(file) + file->Ntfs.UsaOffset);
	PUSHORT sector = PUSHORT(file);
	
	if (file->Ntfs.UsaCount>4) 
		return FALSE;
	for (ULONG i=1;i<file->Ntfs.UsaCount;i++)
	{
		sector[255] = usa[i];
		sector+= 256;
	}
	
	return TRUE;
}
 
void ErrorMessage(DWORD dwCode)
{
    // get the error code...
    DWORD dwErrCode = dwCode;
      DWORD dwNumChar;
 
    LPWSTR szErrString = NULL;  // will be allocated and filled by FormatMessage
 
    dwNumChar = FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER |
                 FORMAT_MESSAGE_FROM_SYSTEM, // use windows internal message table
                 0,         // 0 since source is internal message table
                 dwErrCode, // this is the error code number
                 0,         // auto-determine language to use
                 (LPWSTR)&szErrString, // the messsage
                 0,                    // min size for buffer
                 0 );                  // since getting message from system tables
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