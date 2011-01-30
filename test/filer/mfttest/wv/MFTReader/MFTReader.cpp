// MFTReader.cpp : DLL アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

#include <windows.h>
#include <winioctl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>

#ifdef __cplusplus
extern "C" {
#endif

#ifdef _MANAGED
#pragma managed(push, off)
#endif

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

typedef struct {
	//GUID ObjectId;
	ULONGLONG DirectoryFileReferenceNumber;
	bool IsDirectory;

	ULONGLONG Size; 
	ULONGLONG CreationTime;
	ULONGLONG LastWriteTime;
	//ULONGLONG LastAccessTime;
	//LPWSTR Path;
	LPWSTR Name;

} MFT_FILE_INFO, *PMFT_FILE_INFO;

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
    return TRUE;
}

typedef bool (__stdcall *CallBackProc)( int per );

void __stdcall freeBuffer(void* p)
{
	free(p);
}

/*


Return
	0:OK
	1:ERROR
	2:Abort
*/
int __stdcall GetMFTFileRecord(LPWSTR lpDrive, MFT_FILE_INFO*& pfile_info, LONGLONG* size, CallBackProc proc, DWORD* errCode)
{
	HANDLE hVolume;
	//LPWSTR lpDrive = L"\\\\.\\c:";
	//LPWSTR lpDrive = L"\\\\.\\";
	//lstrcatW(lpDrive, driveletter);
	//lstrcatW(lpDrive, L":" ); 
	//wprintf(L"The lpDrive: %s\n",lpDrive);
	//MessageBox(NULL, lpDrive, lpDrive, MB_OK);

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
		//wprintf(L"CreateFile() failed!\n");
		//ErrorMessage(GetLastError());
		*errCode = GetLastError();
		if(CloseHandle(hVolume) != 0){
			 //wprintf(L"hVolume handle was closed successfully!\n");
		}else
		{
			//wprintf(L"Failed to close hVolume handle!\n");
			//ErrorMessage(GetLastError());
			*errCode = GetLastError();
		}
		return 1;
	}
	//else
	//	wprintf(L"CreateFile() is pretty fine!\n");    

	// a call to FSCTL_GET_NTFS_VOLUME_DATA returns the structure NTFS_VOLUME_DATA_BUFFER
	bDioControl = DeviceIoControl(hVolume, FSCTL_GET_NTFS_VOLUME_DATA, NULL, 0, &ntfsVolData,
		sizeof(NTFS_VOLUME_DATA_BUFFER), &dwWritten, NULL);

	if(bDioControl == 0)
	{
		//wprintf(L"DeviceIoControl() failed!\n");
		//ErrorMessage(GetLastError());
		*errCode = GetLastError();
		if(CloseHandle(hVolume) != 0){
			  //wprintf(L"hVolume handle was closed successfully!\n");
		}else
		{
			  //wprintf(L"Failed to close hVolume handle!\n");
			  //ErrorMessage(GetLastError());
			  *errCode = GetLastError();
		}
		return 1;
	}
	//else
	//	wprintf(L"DeviceIoControl(...,FSCTL_GET_NTFS_VOLUME_DATA,...) is working...\n\n");

	//wprintf(L"Volume Serial Number: 0X%.8X%.8X\n",ntfsVolData.VolumeSerialNumber.HighPart,ntfsVolData.VolumeSerialNumber.LowPart);
	//wprintf(L"The number of bytes in a cluster: %u\n",ntfsVolData.BytesPerCluster);
	//wprintf(L"The number of bytes in a file record segment: %u\n",ntfsVolData.BytesPerFileRecordSegment);
	//wprintf(L"The number of bytes in a sector: %u\n",ntfsVolData.BytesPerSector);
	//wprintf(L"The number of clusters in a file record segment: %u\n",ntfsVolData.ClustersPerFileRecordSegment);
	//wprintf(L"The number of free clusters in the specified volume: %u\n",ntfsVolData.FreeClusters);
	//wprintf(L"The starting logical cluster number of the master file table mirror: 0X%.8X%.8X\n",ntfsVolData.Mft2StartLcn.HighPart, ntfsVolData.Mft2StartLcn.LowPart);
	//wprintf(L"The starting logical cluster number of the master file table: 0X%.8X%.8X\n",ntfsVolData.MftStartLcn.HighPart, ntfsVolData.MftStartLcn.LowPart);
	//wprintf(L"The length of the master file table, in bytes: %u\n",ntfsVolData.MftValidDataLength);
	//wprintf(L"The ending logical cluster number of the master file table zone: 0X%.8X%.8X\n",ntfsVolData.MftZoneEnd.HighPart, ntfsVolData.MftZoneEnd.LowPart);
	//wprintf(L"The starting logical cluster number of the master file table zone: 0X%.8X%.8X\n",ntfsVolData.MftZoneStart.HighPart, ntfsVolData.MftZoneStart.LowPart);
	//wprintf(L"The number of sectors: %u\n",ntfsVolData.NumberSectors);
	//wprintf(L"Total Clusters (used and free): %u\n",ntfsVolData.TotalClusters);
	//wprintf(L"The number of reserved clusters: %u\n\n",ntfsVolData.TotalReserved);

	num.QuadPart = 1024; // 1024 or 2048

	// We divide the MftValidDataLength (Master file table length) by 1024 to find
	// the total entry count for the MFT
	total_file_count = (ntfsVolData.MftValidDataLength.QuadPart/num.QuadPart);

	//total_file_count /= 1000; //test

	pfile_info = (MFT_FILE_INFO*)malloc(sizeof(MFT_FILE_INFO)*total_file_count);
	if(pfile_info == NULL){
		//wprintf(L"malloc() failed - insufficient memory!\n");
		//ErrorMessage(GetLastError());
		*errCode = GetLastError();
		return 1;
	}
	memset(pfile_info, 0, sizeof(MFT_FILE_INFO)*total_file_count);

	//wprintf(L"Total file count = %u\n", total_file_count);
	bool abort = 0;
	int per = 0;
	for(i = 0; i < total_file_count;i++)
	{
		mftRecordInput.FileReferenceNumber.QuadPart = i;

		// prior to calling the DeviceIoControl() we need to load
		// an input record with which entry number we want

		// setup outputbuffer - FSCTL_GET_NTFS_FILE_RECORD depends on this
		output_buffer = (PNTFS_FILE_RECORD_OUTPUT_BUFFER)malloc(sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1);

		if(output_buffer == NULL)
		{
			  //wprintf(L"malloc() failed - insufficient memory!\n");
			  //ErrorMessage(GetLastError());
			  *errCode = GetLastError();
			  return 1;
		}

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
		PFILE_RECORD_HEADER p_file_record_header = (PFILE_RECORD_HEADER)output_buffer->FileRecordBuffer;

		PFILENAME_ATTRIBUTE fn;
		PSTANDARD_INFORMATION si;
		//POBJECTID_ATTRIBUTE objid;

		PATTRIBUTE attr = (PATTRIBUTE)((LPBYTE)p_file_record_header +  p_file_record_header->AttributesOffset); 

		int stop = min(8,p_file_record_header->NextAttributeNumber);
		if(p_file_record_header->Ntfs.Type =='ELIF'){
			while(true){
				if (attr->AttributeType<0 || attr->AttributeType>0x100) break;
				
				switch(attr->AttributeType)
				{
					case AttributeFileName:
						fn = PFILENAME_ATTRIBUTE(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
						if (fn->NameType || fn->NameType == 0)
						{
							fn->Name[fn->NameLength] = L'\0';
							
							pfile_info[i].DirectoryFileReferenceNumber = fn->DirectoryFileReferenceNumber;
							if(p_file_record_header->Flags & 0x2){
								pfile_info[i].IsDirectory = true;
							}else{
								pfile_info[i].IsDirectory = false;
							}
		
							pfile_info[i].Name = new WCHAR[lstrlenW(fn->Name)+1]; 
							lstrcpyW(pfile_info[i].Name , fn->Name);
							//pfile_info[i].Name = fn->Name;				
							pfile_info[i].Size = fn->DataSize;


						}
						break;
					case AttributeStandardInformation:
						si = PSTANDARD_INFORMATION(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
						pfile_info[i].CreationTime = si->CreationTime;
						pfile_info[i].LastWriteTime = si->LastWriteTime;
						//pfile_info[i].LastAccessTime = si->LastAccessTime;
						break;
					//case AttributeObjectId:
					//	objid = POBJECTID_ATTRIBUTE(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
					//	pfile_info[i].ObjectId = objid->ObjectId;
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
		free(output_buffer);
		
		//if((CallBackProc!=NULL) && (per < i*100/total_file_count)){
		if(proc != NULL && (per < i*100/total_file_count)){
			per = i*100/total_file_count;
			abort = proc(per);
			if(abort){
				break;
			}
		}
	}

	*size = i;
	// Let verify
	//wprintf(L"i\'s count = %u\n", i);

	//======================
	if(CloseHandle(hVolume) != 0){
		//wprintf(L"hVolume handle was closed successfully!\n");
	}
	else
	{
		//wprintf(L"Failed to close hVolume handle!\n");
		//ErrorMessage(GetLastError());
		*errCode = GetLastError();
		//free(output_buffer);
		return 1;
	}
	// De-allocate the memory by malloc()
	//free(output_buffer);

	//_CrtDumpMemoryLeaks();
	//free(pfile_info);

	return abort?2:0;
}

#ifdef _MANAGED
#pragma managed(pop)
#endif

#ifdef __cplusplus
} // extern "C"
#endif