// MFTReader.cpp : DLL アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

#include <windows.h>
#include <winioctl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>


// Template for padding
template <class T1, class T2> inline T1* Padd(T1* p, T2 n)
{
      return (T1*)((char *)p + n);
}

#ifdef __cplusplus
extern "C" {
#endif

#ifdef _MANAGED
#pragma managed(push, off)
#endif


ULONG BytesPerFileRecord;
HANDLE hVolume;
BOOT_BLOCK bootb;
PFILE_RECORD_HEADER MFT;


ULONG RunLength(PUCHAR run)
{
      wprintf(L"In RunLength()...\n");
      return (*run & 0xf) + ((*run >> 4) & 0xf) + 1;
}
 
LONGLONG RunLCN(PUCHAR run)
{
      LONG i = 0;
      UCHAR n1 = 0 , n2 = 0;
      LONGLONG lcn = 0;
 
      wprintf(L"In RunLCN()...\n");
      n1 = *run & 0xf;
      n2 = (*run >> 4) & 0xf;
 
      lcn = n2 == 0 ? 0 : CHAR(run[n1 + n2]);
     
      for (i = n1 + n2 - 1; i > n1; i--)
            lcn = (lcn << 8) + run[i];
      return lcn;
}
 
ULONGLONG RunCount(PUCHAR run)
{
      UCHAR n = *run & 0xf;
      ULONGLONG count = 0;
      ULONG i;
 
      wprintf(L"In RunCount()...\n");
     
      for (i = n; i > 0; i--)
            count = (count << 8) + run[i];
 
      return count;
}
 
BOOL FindRun(PNONRESIDENT_ATTRIBUTE attr, ULONGLONG vcn, PULONGLONG lcn, PULONGLONG count)
{
      PUCHAR run = NULL;
      *lcn = 0;
      ULONGLONG base = attr->LowVcn;
 
      wprintf(L"In FindRun()...\n");
 
      if (vcn < attr->LowVcn || vcn > attr->HighVcn)
            return FALSE;
 
      for(run = PUCHAR(Padd(attr, attr->RunArrayOffset));   *run != 0;  run += RunLength(run))
      {
            *lcn += RunLCN(run);
            *count = RunCount(run);
 
            if (base <= vcn && vcn < base + *count)
            {
                  *lcn = RunLCN(run) == 0 ? 0 : *lcn + vcn - base;
                  *count -= ULONG(vcn - base);
                  return TRUE;
            }
            else
                  base += *count;
      }
      return FALSE;
}



PATTRIBUTE FindAttribute(PFILE_RECORD_HEADER file,ATTRIBUTE_TYPE type, PWSTR name)
{
      PATTRIBUTE attr = NULL;
 
      wprintf(L"FindAttribute() - Finding attributes...\n");
 
      for (attr = PATTRIBUTE(Padd(file, file->AttributesOffset));
            attr->AttributeType != -1;attr = Padd(attr, attr->Length))
      {
            if (attr->AttributeType == type)
            {
                  if (name == 0 && attr->NameLength == 0)
                        return attr;
                  if (name != 0 && wcslen(name) == attr->NameLength && _wcsicmp(name,
                        PWSTR(Padd(attr, attr->NameOffset))) == 0)
                  return attr;
            }
      }
      return 0;
}



VOID FixupUpdateSequenceArray(PFILE_RECORD_HEADER file)
{
      ULONG i = 0;
      PUSHORT usa = PUSHORT(Padd(file, file->Ntfs.UsaOffset));
      PUSHORT sector = PUSHORT(file);
	 
      OutputDebugString(L"In FixupUpdateSequenceArray()...\n");
      for (i = 1; i < file->Ntfs.UsaCount; i++)
      {
            sector[255] = usa[i];
            sector += 256;
      }
}

VOID ReadSector(ULONGLONG sector, ULONG count, PVOID buffer)
{
      ULARGE_INTEGER offset;
      OVERLAPPED overlap = {0};
      ULONG n;
 
      wprintf(L"ReadSector() - Reading the sector...\n");
      wprintf(L"Sector: %lu\n", sector);
 
      offset.QuadPart = sector * bootb.BytesPerSector;
      overlap.Offset = offset.LowPart;
      overlap.OffsetHigh = offset.HighPart;
      ReadFile(hVolume, buffer, count * bootb.BytesPerSector, &n, &overlap);
}

VOID ReadLCN(ULONGLONG lcn, ULONG count, PVOID buffer)
{
      wprintf(L"\nReadLCN() - Reading the LCN, LCN: 0X%.8X\n", lcn);
      ReadSector(lcn * bootb.SectorsPerCluster,count * bootb.SectorsPerCluster, buffer);
}

VOID ReadExternalAttribute(PNONRESIDENT_ATTRIBUTE attr,ULONGLONG vcn, ULONG count, PVOID buffer)
{
      ULONGLONG lcn, runcount;
      ULONG readcount, left;
      PUCHAR bytes = PUCHAR(buffer);
 
      wprintf(L"ReadExternalAttribute() - Reading the Non resident attributes...\n");
 
      for(left = count; left > 0; left -= readcount)
      {
            FindRun(attr, vcn, &lcn, &runcount);
            readcount = ULONG(min(runcount, left));
            ULONG n = readcount * bootb.BytesPerSector * bootb.SectorsPerCluster;
 
            if(lcn == 0)
                  memset(bytes, 0, n);
            else
            {
                  ReadLCN(lcn, readcount, bytes);
                  wprintf(L"LCN: 0X%.8X\n", lcn);
            }          
            vcn += readcount;
            bytes += n;
      }
}

VOID LoadMFT()
{
      wprintf(L"In LoadMFT() - Loading MFT...\n");
 
      BytesPerFileRecord = bootb.ClustersPerFileRecord < 0x80
            ? bootb.ClustersPerFileRecord* bootb.SectorsPerCluster
            * bootb.BytesPerSector: 1 << (0x100 - bootb.ClustersPerFileRecord);
 
      wprintf(L"\nBytes Per File Record = %u\n\n", BytesPerFileRecord);
      wprintf(L"======THESE INFO ARE NOT ACCURATE FOR DISPLAY LOL!=====\n");
      wprintf(L"bootb.BootSectors = %u\n", bootb.BootSectors);
      wprintf(L"bootb.BootSignature = %u\n", bootb.BootSignature);
      wprintf(L"bootb.BytesPerSector = %u\n", bootb.BytesPerSector);
      wprintf(L"bootb.ClustersPerFileRecord = %u\n", bootb.ClustersPerFileRecord);
      wprintf(L"bootb.ClustersPerIndexBlock = %u\n", bootb.ClustersPerIndexBlock);
      wprintf(L"bootb.Code = %u\n", bootb.Code);
      wprintf(L"bootb.Format = %u\n", bootb.Format);
      wprintf(L"bootb.Jump = %u\n", bootb.Jump);
      wprintf(L"bootb.Mbz1 = %u\n", bootb.Mbz1);
      wprintf(L"bootb.Mbz2 = %u\n", bootb.Mbz2);
      wprintf(L"bootb.Mbz3 = %u\n", bootb.Mbz3);
      wprintf(L"bootb.MediaType = 0X%X\n", bootb.MediaType);
      wprintf(L"bootb.Mft2StartLcn = 0X%.8X\n", bootb.Mft2StartLcn);
      wprintf(L"bootb.MftStartLcn = 0X%.8X\n", bootb.MftStartLcn);
      wprintf(L"bootb.NumberOfHeads = %u\n", bootb.NumberOfHeads);
      wprintf(L"bootb.PartitionOffset = %lu\n", bootb.PartitionOffset);
      wprintf(L"bootb.SectorsPerCluster = %u\n", bootb.SectorsPerCluster);
      wprintf(L"bootb.SectorsPerTrack = %u\n", bootb.SectorsPerTrack);
      wprintf(L"bootb.TotalSectors = %lu\n", bootb.TotalSectors);
//      wprintf(L"bootb.VolumeSerialNumber = 0X%.8X%.8X\n\n", bootb.VolumeSerialNumber.HighPart, bootb.VolumeSerialNumber.HighPart);
 
 
      MFT = PFILE_RECORD_HEADER(new UCHAR[BytesPerFileRecord]);
 
      ReadSector((bootb.MftStartLcn)*(bootb.SectorsPerCluster), (BytesPerFileRecord)/(bootb.BytesPerSector), MFT);

      FixupUpdateSequenceArray(MFT);
}

VOID ReadVCN(PFILE_RECORD_HEADER file, ATTRIBUTE_TYPE type,ULONGLONG vcn, ULONG count, PVOID buffer)
{
      PATTRIBUTE attrlist = NULL;
      PNONRESIDENT_ATTRIBUTE attr = PNONRESIDENT_ATTRIBUTE(FindAttribute(file, type, 0));
 
      wprintf(L"In ReadVCN()...\n");
      if (attr == 0 || (vcn < attr->LowVcn || vcn > attr->HighVcn))
      {
            // Support for huge files
            attrlist = FindAttribute(file, AttributeAttributeList, 0);
            DebugBreak();
      }
      ReadExternalAttribute(attr, vcn, count, buffer);
}


VOID ReadFileRecord(ULONG index, PFILE_RECORD_HEADER file)
{
      ULONG clusters = bootb.ClustersPerFileRecord;
 
      wprintf(L"ReadFileRecord() - Reading the file records..\n");
      if (clusters > 0x80)
            clusters = 1;
 
      PUCHAR p = new UCHAR[bootb.BytesPerSector* bootb.SectorsPerCluster * clusters];
      ULONGLONG vcn = ULONGLONG(index) * BytesPerFileRecord/bootb.BytesPerSector/bootb.SectorsPerCluster;
      ReadVCN(MFT, AttributeData, vcn, clusters, p);
      LONG m = (bootb.SectorsPerCluster * bootb.BytesPerSector/BytesPerFileRecord) - 1;
      ULONG n = m > 0 ? (index & m) : 0;
      memcpy(file, p + n * BytesPerFileRecord, BytesPerFileRecord);
      delete [] p;
      FixupUpdateSequenceArray(file);
}
///////////////////////////////////////////

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
		GENERIC_READ,
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

	/////////////////
	ULONG n;
    if(ReadFile(hVolume, &bootb, sizeof bootb, &n, 0) == 0){
        //wprintf(L"ReadFile() failed, error %u\n", GetLastError());
		//exit(1);
		*errCode = GetLastError();   
		return 1;
	}

	LoadMFT();
	//	return 0;
	/////////////////

	//wprintf(L"Total file count = %u\n", total_file_count);
	bool abort = 0;
	int per = 0;
	for(i = 0; i < total_file_count;i++)
	{
		//mftRecordInput.FileReferenceNumber.QuadPart = i;

		//// prior to calling the DeviceIoControl() we need to load
		//// an input record with which entry number we want

		//// setup outputbuffer - FSCTL_GET_NTFS_FILE_RECORD depends on this
		//output_buffer = (PNTFS_FILE_RECORD_OUTPUT_BUFFER)malloc(sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1);

		//if(output_buffer == NULL)
		//{
		//	  //wprintf(L"malloc() failed - insufficient memory!\n");
		//	  //ErrorMessage(GetLastError());
		//	  *errCode = GetLastError();
		//	  return 1;
		//}

		//bDioControl = DeviceIoControl(hVolume, 
		//	FSCTL_GET_NTFS_FILE_RECORD,
		//	&mftRecordInput,
		//	  sizeof(mftRecordInput), 
		//	  output_buffer,
		//	  sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER) + (ntfsVolData.BytesPerFileRecordSegment)- 1, 
		//	  &dwWritten, 
		//	  NULL);

		//// More data will make DeviceIoControl() fails...
		///*if(bDioControl == 0)
		//{
		//	  wprintf(L"DeviceIoControl(...,FSCTL_GET_NTFS_FILE_RECORD,...) failed!\n");
		//	  ErrorMessage(GetLastError());
		//	  exit(1);
		//}*/

		//// FSCTL_GET_NTFS_FILE_RECORD retrieves one MFT entry
		//// FILE_RECORD_HEADER is the Base struct for the MFT entry
		//// that we will work from
		//PFILE_RECORD_HEADER p_file_record_header = (PFILE_RECORD_HEADER)output_buffer->FileRecordBuffer;

		//////////////////
		PFILE_RECORD_HEADER p_file_record_header = PFILE_RECORD_HEADER(new UCHAR[BytesPerFileRecord]);
		//ULONG index=0;
		ReadFileRecord(i, p_file_record_header);
		//////////////////////////////

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
		//free(output_buffer);
		
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