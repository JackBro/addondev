// MFTReader.cpp : DLL アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"

#include <windows.h>
#include <winioctl.h>

#ifdef __cplusplus
extern "C" {
#endif

#ifdef _MANAGED
#pragma managed(push, off)
#endif

typedef struct {
	ULONGLONG ChangeTime;

}

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



#ifdef _MANAGED
#pragma managed(pop)
#endif

#ifdef __cplusplus
} // extern "C"
#endif