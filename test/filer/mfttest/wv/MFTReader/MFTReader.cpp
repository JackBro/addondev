// MFTReader.cpp : DLL アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

#include <windows.h>
#include <winioctl.h>
#include <stdio.h>
#include <stdlib.h>


#ifdef __cplusplus
extern "C" {
#endif

#ifdef _MANAGED
#pragma managed(push, off)
#endif

typedef struct {
	int index;
	int ChangeTime;
	LPWSTR Name;
} MFT_FILE_RECORD, *PMFT_FILE_RECORD;

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
    return TRUE;
}

typedef void (__stdcall *CallBackTenTimesProc)( void );

void __stdcall CallBackTenTimes( CallBackTenTimesProc proc )
{
    int i;
    for( i=0; i<10; i++ )
    {
        proc();
    }
}

void __stdcall GetRecord( MFT_FILE_RECORD* &proc )
{
	MFT_FILE_RECORD m_XArray[10];
	//m_XArray[0].Name = L"test0";
	m_XArray[0].index = 0;
	m_XArray[0].ChangeTime = 10;

	//m_XArray[1].Name = L"test1";
	m_XArray[1].index = 1;
	m_XArray[1].ChangeTime = 100;

	proc = m_XArray;

	//SetSystemTime
}

void __stdcall GetRecordS(void*  proc )
{
	MFT_FILE_RECORD m_XArray;
	m_XArray.index = 0;
	m_XArray.ChangeTime = 10;

	proc = &m_XArray;
	//proc->index=100;
}


void __stdcall customList(MFT_FILE_RECORD*& p)
{
	p = (MFT_FILE_RECORD*)malloc(sizeof(MFT_FILE_RECORD));
	memset(p, 0, sizeof(MFT_FILE_RECORD));
	p->index=100;
	p->ChangeTime=1000;
}

void __stdcall customLists(MFT_FILE_RECORD*& p)
{
	p = (MFT_FILE_RECORD*)malloc(sizeof(MFT_FILE_RECORD)*2);
	memset(p, 0, sizeof(MFT_FILE_RECORD)*2);
	p->index=100;
	p->ChangeTime=1000;
	p->Name = L"name1";

	p[1].index=200;
	p[1].ChangeTime=2000;
	p[1].Name = L"name2";

}

void __stdcall freeBuffer(void* p)
{
	free(p);
}

void __stdcall GetRecode()
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
 
	  total_file_count /= 1000; //test
      wprintf(L"Total file count = %u\n", total_file_count);
 
      for(i = 0; i < total_file_count;i++)
      {
            mftRecordInput.FileReferenceNumber.QuadPart = i;
 
            // prior to calling the DeviceIoControl() we need to load
            // an input record with which entry number we want
 
            // setup outputbuffer - FSCTL_GET_NTFS_FILE_RECORD depends on this
            output_buffer = (PNTFS_FILE_RECORD_OUTPUT_BUFFER)malloc(sizeof(NTFS_FILE_RECORD_OUTPUT_BUFFER)+ntfsVolData.BytesPerFileRecordSegment-1);
 
            if(output_buffer == NULL)
            {
                  wprintf(L"malloc() failed - insufficient memory!\n");
                  ErrorMessage(GetLastError());
                  exit(1);
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
            PFILE_RECORD_HEADER p_file_record_header =       (PFILE_RECORD_HEADER)output_buffer->FileRecordBuffer;

			PFILENAME_ATTRIBUTE fn;
			PSTANDARD_INFORMATION si;

			PATTRIBUTE attr = (PATTRIBUTE)((LPBYTE)p_file_record_header +  p_file_record_header->AttributesOffset); 

			int stop = min(8,p_file_record_header->NextAttributeNumber);
			if(p_file_record_header->Ntfs.Type =='ELIF'){
				while(true){
					if (attr->AttributeType<0 || attr->AttributeType>0x100) break;
					
					switch(attr->AttributeType)
					{
						case AttributeFileName:
							fn = PFILENAME_ATTRIBUTE(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
							if (fn->NameType & WIN32_NAME || fn->NameType == 0)
							{

								if(p_file_record_header->Flags & 0x1){
									wprintf(L"FileName Flags 0x1\n") ;
								}else{
									wprintf(L"FileName Flags not 0x1\n") ;
								}
								wprintf(L"FileName i : %u\n", i );
								wprintf(L"FileName DirectoryFileReferenceNumber : %u\n", fn->DirectoryFileReferenceNumber );
								wprintf(L"FileName DataSize : %u\n", fn->DataSize );
								

								fn->Name[fn->NameLength] = L'\0';
								wprintf(L"FileName Name :%s\n", fn->Name) ;

								LPWSTR lp = fn->Name;
								wprintf(L"FileName Name :%s\n", lp) ;
							}
							break;
						case AttributeStandardInformation:
							si = PSTANDARD_INFORMATION(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
							wprintf(L"StandardInformation CreationTime : %u\n", si->CreationTime );
							break;
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
      free(output_buffer);
}

#ifdef _MANAGED
#pragma managed(pop)
#endif

#ifdef __cplusplus
} // extern "C"
#endif