// MFTReadSector.cpp : コンソール アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"
#include <windows.h>
#include <stdlib.h>
#include <stdio.h>
#include "ntfs.h"

ULONG BytesPerFileRecord;
HANDLE hVolume;
BOOT_BLOCK bootb;
PFILE_RECORD_HEADER MFT;

// Template for padding
template <class T1, class T2> inline T1* Padd(T1* p, T2 n)
{
      return (T1*)((char *)p + n);
}
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
     
      wprintf(L"In FixupUpdateSequenceArray()...\n");
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
	  //overlap.Pointer = NULL;
      ReadFile(hVolume, buffer, count * bootb.BytesPerSector, &n, &overlap);

	  int m=0;
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
 
if(bootb.ClustersPerFileRecord < 0x80)
{
BytesPerFileRecord = bootb.ClustersPerFileRecord* bootb.SectorsPerCluster*bootb.BytesPerSector;
}
else{
	ULONG mm = 0x100 - bootb.ClustersPerFileRecord;
	BytesPerFileRecord = 1 << (0x100 - bootb.ClustersPerFileRecord);
}

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

int _tmain(int argc, _TCHAR* argv[])
{
	WCHAR drive[] = L"\\\\.\\C:";
	ULONG n;

      hVolume = CreateFile(
            drive,
            GENERIC_READ,
            FILE_SHARE_READ | FILE_SHARE_WRITE,
            0,
            OPEN_EXISTING,
            0,
            0);
 
      if(hVolume == INVALID_HANDLE_VALUE)
      {
            wprintf(L"CreateFile() failed, error %u\n", GetLastError());
            exit(1);
      }
      if(ReadFile(hVolume, &bootb, sizeof bootb, &n, 0) == 0)
      {
            wprintf(L"ReadFile() failed, error %u\n", GetLastError());
            exit(1);
      }
	
   LoadMFT();

	PFILE_RECORD_HEADER file = PFILE_RECORD_HEADER(new UCHAR[BytesPerFileRecord]);
	for(ULONG index=0; index<10; index++){
		//ULONG index=0;
		wprintf(L"ReadFileRecord() index %u\n", index);
		ReadFileRecord(index, file);

		LONGLONG i=0;
		PFILENAME_ATTRIBUTE fn;
		PSTANDARD_INFORMATION si;
		PATTRIBUTE attr = (PATTRIBUTE)((LPBYTE)file +  file->AttributesOffset); 
		if(file->Ntfs.Type =='ELIF'){
			while(true){
				if (attr->AttributeType<0 || attr->AttributeType>0x100) break;
						
				switch(attr->AttributeType)
				{
					case AttributeFileName:
						fn = PFILENAME_ATTRIBUTE(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
						if (fn->NameType & 1 || fn->NameType == 0)
						{
							if(file->Flags & 0x1){
								wprintf(L"FileName InUse\n") ;
							}else{
								wprintf(L"FileName NOt In Use\n") ;
							}

							if(file->Flags & 0x2){
								wprintf(L"FileName Directory\n") ;
							}else{
								wprintf(L"FileName File\n") ;
							}
							wprintf(L"FileName i : %u\n", i );
							wprintf(L"FileName DirectoryFileReferenceNumber : %u\n", fn->DirectoryFileReferenceNumber );
							wprintf(L"FileName DataSize : %u\n", fn->DataSize );
							

							fn->Name[fn->NameLength] = L'\0';
							wprintf(L"FileName Name :%s\n", fn->Name) ;
						}
						break;
					case AttributeStandardInformation:
						si = PSTANDARD_INFORMATION(PUCHAR(attr) + PRESIDENT_ATTRIBUTE(attr)->ValueOffset);
						wprintf(L"#####StandardInformation CreationTime : %u\n", si->CreationTime );
						break;
					default:
						break;
				};
				
				if (attr->Length>0 && attr->Length < file->BytesInUse)
					attr = PATTRIBUTE(PUCHAR(attr) + attr->Length);
				else
					if (attr->Nonresident == TRUE)
						attr = PATTRIBUTE(PUCHAR(attr) + sizeof(NONRESIDENT_ATTRIBUTE));
			}
		}
	}
	return 0;
}

