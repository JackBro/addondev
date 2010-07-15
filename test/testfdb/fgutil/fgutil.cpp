#include <windows.h>
#include <wchar.h>

//typedef struct _FILE_OBJECTID {
//	BYTE ObjectId[16];
//} FILE_OBJECTID;

#ifdef __cplusplus
EXTERN_C 
{
#endif

//int __declspec(dllexport) __stdcall MySub(int a, int b);
//boolean __declspec(dllexport) __stdcall getObjectID(LPCWSTR path,  FILE_OBJECTID* fid);
boolean __declspec(dllexport) __stdcall getObjectID(LPCWSTR path, GUID* guid);
//boolean __declspec(dllexport) __stdcall getFullPathByObjectID(FILE_OBJECTID fid, BSTR *path);
boolean __declspec(dllexport) __stdcall getFullPathByObjectID(GUID guid, BSTR *path);

#ifdef __cplusplus
}
#endif


BOOL WINAPI DllMain (HINSTANCE hDLL, DWORD dwReason, LPVOID lpReserved)
{
    return TRUE;
}

//int __declspec(dllexport) __stdcall MySub(int a, int b)
//{
//    return a-b;
//}

//boolean __declspec(dllexport) __stdcall getObjectID(LPCWSTR path,  FILE_OBJECTID* fid)
boolean __declspec(dllexport) __stdcall getObjectID(LPCWSTR path, GUID* guid)
{
	HANDLE hFile = CreateFileW(path, GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, 0, NULL);
	

	if(hFile == INVALID_HANDLE_VALUE){
		return false;
	}

	DWORD cb;
	FILE_OBJECTID_BUFFER data;
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

	CloseHandle(hFile);

	if(!fOk) {	
		return false;
	}

  //  for (int i=0; i<16; i++) {
		//fid->ObjectId[i] = data.ObjectId[i];
  //     //ObjectId[i] = data.ObjectId[i];
  //  }

	guid->Data1 = data.ObjectId[0] 
		| (data.ObjectId[1] << 8) 
		| (data.ObjectId[2] << 16)
		| (data.ObjectId[3] << 24);
	
	guid->Data2 = data.ObjectId[4] | (data.ObjectId[5] << 8);
	guid->Data3 = data.ObjectId[6] | (data.ObjectId[7] << 8);

	guid->Data4[0] = data.ObjectId[8];
	guid->Data4[1] = data.ObjectId[9];
	guid->Data4[2] = data.ObjectId[10];
	guid->Data4[3] = data.ObjectId[11];
	guid->Data4[4] = data.ObjectId[12];
	guid->Data4[5] = data.ObjectId[13];
	guid->Data4[6] = data.ObjectId[14];
	guid->Data4[7] = data.ObjectId[15];

    return true;
}


//boolean __declspec(dllexport) __stdcall getFullPathByObjectID(FILE_OBJECTID fid, BSTR *path)
boolean __declspec(dllexport) __stdcall getFullPathByObjectID(GUID guid, BSTR *path)
{
	FILE_ID_DESCRIPTOR fileIDDesc;

	fileIDDesc.Type = ObjectIdType;

	//fileIDDesc.ObjectId.Data1 = fid.ObjectId[0] 
	//	| (fid.ObjectId[1] << 8) 
	//	| (fid.ObjectId[2] << 16)
	//	| (fid.ObjectId[3] << 24);
	//
	//fileIDDesc.ObjectId.Data2 = fid.ObjectId[4] | (fid.ObjectId[5] << 8);
	//fileIDDesc.ObjectId.Data3 = fid.ObjectId[6] | (fid.ObjectId[7] << 8);

	//fileIDDesc.ObjectId.Data4[0] = fid.ObjectId[8];
	//fileIDDesc.ObjectId.Data4[1] = fid.ObjectId[9];
	//fileIDDesc.ObjectId.Data4[2] = fid.ObjectId[10];
	//fileIDDesc.ObjectId.Data4[3] = fid.ObjectId[11];
	//fileIDDesc.ObjectId.Data4[4] = fid.ObjectId[12];
	//fileIDDesc.ObjectId.Data4[5] = fid.ObjectId[13];
	//fileIDDesc.ObjectId.Data4[6] = fid.ObjectId[14];
	//fileIDDesc.ObjectId.Data4[7] = fid.ObjectId[15];

	fileIDDesc.ObjectId = guid;
	
	fileIDDesc.dwSize             = sizeof(fileIDDesc); 

	TCHAR cDrive;
	wchar_t szVolumePath[32];
	for ( cDrive = 'c'; cDrive <= 'z'; cDrive++ )  {

		wsprintf(szVolumePath, TEXT("\\\\.\\%c:"), cDrive);
		HANDLE hDisk=CreateFile(szVolumePath, GENERIC_READ, FILE_SHARE_READ|FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL);

		if(hDisk == INVALID_HANDLE_VALUE){
			continue;
		}

		HANDLE h = OpenFileById( 
						hDisk,
						&fileIDDesc,
						SYNCHRONIZE | FILE_READ_ATTRIBUTES,
						FILE_SHARE_READ|FILE_SHARE_WRITE,
						NULL,
						0 );

		CloseHandle(hDisk);
		if(h == INVALID_HANDLE_VALUE){
			continue;
		}

		char buf[2048];
		GetFileInformationByHandleEx(h, FileNameInfo, buf, sizeof(buf));
		FILE_NAME_INFO& info = *(FILE_NAME_INFO*)buf;
		info.FileName[info.FileNameLength / 2] = 0;
		CloseHandle(h);

		wchar_t szVolume[2048];
		wsprintf(szVolume, TEXT("%c:"), cDrive);
		//wcscat(szVolume, info.FileName);
		//BSTR tmp = ::SysAllocString(info.FileName);
		BSTR tmp = ::SysAllocString(wcscat(szVolume, info.FileName));
		if(tmp == NULL){
			return false;
		}

		::SysFreeString(*path);
		*path = tmp;

		return true;
	}

	return false;
}