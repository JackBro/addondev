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
	int index;
	int ChangeTime;
	//LPWSTR Name;
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

void __stdcall GetRecord( PMFT_FILE_RECORD &proc )
{
	MFT_FILE_RECORD m_XArray[10];
	//m_XArray[0].Name = L"test0";
	m_XArray[0].index = 0;
	m_XArray[0].ChangeTime = 10;

	//m_XArray[1].Name = L"test1";
	m_XArray[1].index = 1;
	m_XArray[1].ChangeTime = 100;

	proc = &m_XArray[0];
}


#ifdef _MANAGED
#pragma managed(pop)
#endif

#ifdef __cplusplus
} // extern "C"
#endif