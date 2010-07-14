#include <windows.h>

#ifdef __cplusplus
EXTERN_C 
{
#endif

int __declspec(dllexport) __stdcall MySub(int a, int b);

#ifdef __cplusplus
}
#endif



BOOL WINAPI DllMain (HINSTANCE hDLL, DWORD dwReason, LPVOID lpReserved)
{
    return TRUE;
}

int __declspec(dllexport) __stdcall MySub(int a, int b)
{
    return a-b;
}

boolean __declspec(dllexport) __stdcall getObjectID(LPCWSTR path,  BYTE ObjectId[16])
{
	HANDLE hFile = CreateFileW(path, GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, 0, NULL);
	
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

    for (int i=0; i<16; i++) {
       ObjectId[i] = data.ObjectId[i];
    }

    return true;
}


boolean __declspec(dllexport) __stdcall getFullPathByObjectID(BYTE ObjectId[16], BSTR *path)
{
	FILE_ID_DESCRIPTOR fileIDDesc;

	fileIDDesc.Type = ObjectIdType;

	fileIDDesc.ObjectId.Data1 = ObjectId[0] 
		| (ObjectId[1] << 8) 
		| (ObjectId[2] << 16)
		| (ObjectId[3] << 24);
	
	fileIDDesc.ObjectId.Data2 = ObjectId[4] | (ObjectId[5] << 8);
	fileIDDesc.ObjectId.Data3 = ObjectId[6] | (ObjectId[7] << 8);

	fileIDDesc.ObjectId.Data4[0] = ObjectId[8];
	fileIDDesc.ObjectId.Data4[1] = ObjectId[9];
	fileIDDesc.ObjectId.Data4[2] = ObjectId[10];
	fileIDDesc.ObjectId.Data4[3] = ObjectId[11];
	fileIDDesc.ObjectId.Data4[4] = ObjectId[12];
	fileIDDesc.ObjectId.Data4[5] = ObjectId[13];
	fileIDDesc.ObjectId.Data4[6] = ObjectId[14];
	fileIDDesc.ObjectId.Data4[7] = ObjectId[15];
	
	fileIDDesc.dwSize             = sizeof(fileIDDesc); 

	wchar_t szVolumePath[2048];
	wsprintf(szVolumePath, TEXT("\\\\.\\%c:"), 'D');
	HANDLE hDisk=CreateFile(szVolumePath, GENERIC_READ, FILE_SHARE_READ|FILE_SHARE_WRITE, NULL, OPEN_EXISTING, 0, NULL);

	if(hDisk == INVALID_HANDLE_VALUE){
		return false;
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
		return false;
	}

	char buf[2048];
	GetFileInformationByHandleEx(h, FileNameInfo, buf, sizeof(buf));
	FILE_NAME_INFO& info = *(FILE_NAME_INFO*)buf;
	info.FileName[info.FileNameLength / 2] = 0;
	//wprintf(L"%s\n", info.FileName);
    CloseHandle(h);

	BSTR tmp = ::SysAllocString(info.FileName);
	if(tmp == NULL){
        return false;
    }

	::SysFreeString(*path);
	*path = tmp;


	return true;
}