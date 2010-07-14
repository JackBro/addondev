// testid.cpp : コンソール アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

using namespace std;

int _tmain(int argc, _TCHAR* argv[])
{

	//HANDLE hFile = CreateFileW(argv[1], 
	//	GENERIC_READ, 
	//	FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL);

	printf_s("%S\n", argv[1]);


	HANDLE hFile = CreateFileW(
		argv[1], GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, 0, NULL);

	//TCHAR szVolumePath[MAX_PATH];
	//wsprintf(szVolumePath, TEXT("\\\\.\\%C:"), m_drive);
	//hFile = CreateFile(szVolumePath, GENERIC_READ,
	//		FILE_SHARE_READ | FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL);

	DWORD cb;
	FILE_OBJECTID_BUFFER data;
	OVERLAPPED over;
	BOOL fOk = DeviceIoControl(
		hFile, 
		FSCTL_CREATE_OR_GET_OBJECT_ID, 
		//FSCTL_GET_OBJECT_ID,
		NULL, 
		0, 
		&data, 
		sizeof(data), 
		&cb, 
		NULL);
//LARGE_INTEGER  FileRef;
//  int Ret = -1; // Assume bad news
//  DWORD Read;
//  DWORD BytesPerFileRecordSegment = 1024; // See NTFS_VOLUME_DATA_BUFFER::BytesPerFileRecordSegment
//  C_ASSERT( sizeof(FileRef) == sizeof(NTFS_FILE_RECORD_INPUT_BUFFER) );
//  NTFS_FILE_RECORD_OUTPUT_BUFFER* nrb = (NTFS_FILE_RECORD_OUTPUT_BUFFER*)malloc( FIELD_OFFSET( NTFS_FILE_RECORD_OUTPUT_BUFFER, FileRecordBuffer ) + BytesPerFileRecordSegment ); 
//
// BOOL fOk = DeviceIoControl( hFile, FSCTL_GET_NTFS_FILE_RECORD, &FileRef, sizeof(FileRef),
//                        nrb, FIELD_OFFSET( NTFS_FILE_RECORD_OUTPUT_BUFFER, FileRecordBuffer ) + BytesPerFileRecordSegment,
//                        &Read, NULL );
	
	if(!fOk) {
		printf("err\n");
		CloseHandle(hFile);
		return 0;
	}

	//CloseHandle(hFile);

	FILE_ID_DESCRIPTOR fileIDDesc;
//	fileIDDesc.FileId.HighPart = 0 ;
//	fileIDDesc.FileId.LowPart = 0 ;
//	fileIDDesc.FileId.QuadPart = 0 ;
//fileIDDesc.FileId.u.HighPart = 0;
//fileIDDesc.FileId.u.LowPart = 0;

	fileIDDesc.Type = ObjectIdType; // enum value
	//fileIDDesc.ObjectId.Data1 = (data.ObjectId[0] << 24) 
	//	| (data.ObjectId[1] << 16) 
	//	| (data.ObjectId[2] << 8)
	//	| data.ObjectId[3];
		fileIDDesc.ObjectId.Data1 = (data.ObjectId[0] ) 
		| (data.ObjectId[1] << 8) 
		| (data.ObjectId[2] << 16)
		| (data.ObjectId[3]<< 24);

	//fileIDDesc.ObjectId.Data2 = ((data.ObjectId[4] << 8) ) | (data.ObjectId[5]);
	
	//fileIDDesc.ObjectId.Data3 = ((data.ObjectId[6] << 8) ) | (data.ObjectId[7]);
	
	fileIDDesc.ObjectId.Data2 = ((data.ObjectId[4] ) ) | (data.ObjectId[5]<< 8);
	fileIDDesc.ObjectId.Data3 = ((data.ObjectId[6]) ) | (data.ObjectId[7] << 8);

	fileIDDesc.ObjectId.Data4[0] = data.ObjectId[8];
	fileIDDesc.ObjectId.Data4[1] = data.ObjectId[9];
	fileIDDesc.ObjectId.Data4[2] = data.ObjectId[10];
	fileIDDesc.ObjectId.Data4[3] = data.ObjectId[11];
	fileIDDesc.ObjectId.Data4[4] = data.ObjectId[12];
	fileIDDesc.ObjectId.Data4[5] = data.ObjectId[13];
	fileIDDesc.ObjectId.Data4[6] = data.ObjectId[14];
	fileIDDesc.ObjectId.Data4[7] = data.ObjectId[15];
	
	fileIDDesc.dwSize             = sizeof(fileIDDesc); //24; 

	wchar_t szVolumePath[2048];

	wsprintf(szVolumePath, TEXT("\\\\.\\%c:"), 'D');
	//wsprintf(szVolumePath, TEXT("\\\\?\\Volume%s"), "{8217F7E6-5E8B-DF11-8AEC-000C761D1793}");
	//wsprintf(szVolumePath, TEXT("\\\\.\\%s"), "{8217F7E6-5E8B-DF11-8AEC-000C761D1793}");
   HANDLE hDisk=CreateFile(szVolumePath,
            GENERIC_READ, FILE_SHARE_READ|FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL);
			//GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, 0, NULL);


	HANDLE h = OpenFileById( 
					hDisk,
					&fileIDDesc,
					SYNCHRONIZE | FILE_READ_ATTRIBUTES,
					FILE_SHARE_READ|FILE_SHARE_WRITE,
					NULL,
					0 );


	//wchar_t pFileNameInfo[1024];
	//GetFileInformationByHandleEx(h, FileNameInfo, pFileNameInfo, sizeof(FILE_NAME_INFO) + 1000);

	//	FILE_NAME_INFO fni;
	//	int iret = GetFileInformationByHandleEx(h, FileNameInfo, &fni, sizeof(FILE_NAME_INFO));
	//		//dwError = GetLastError();
	//		cout << iret << endl;
	//		//cout << dwError << endl;
	//		cout << "len = " << fni.FileNameLength << endl;
	//		cout << "Exe-Name: " << fni.FileName << endl;
	//		for (int i=0; i<fni.FileNameLength; i++)
	//			printf("%c", fni.FileName[i]);
	//		printf("\n\n");


			 char buf[2048];
			GetFileInformationByHandleEx(h, FileNameInfo, buf, sizeof(buf));
	FILE_NAME_INFO& info = *(FILE_NAME_INFO*)buf;
		info.FileName[info.FileNameLength / 2] = 0;
		wprintf(L"%s\n", info.FileName);


			//CloseHandle(hHandle);

//int ss = sizeof(pFileNameInfo);
//pFileNameInfo[1023] = 0;
	//printf_s("path = %s\n", pFileNameInfo);
	//std::cout << " path = " << pFileNameInfo <<  std::endl;
 //wprintf(L"%s\n", pFileNameInfo);


CloseHandle(hDisk);
CloseHandle(h);
	return 0;
}

