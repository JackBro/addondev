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