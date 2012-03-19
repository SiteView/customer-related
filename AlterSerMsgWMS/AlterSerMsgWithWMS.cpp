// AlterSerMsgWithWMS.cpp : ���� DLL Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include "AlterSerMsgWithWMS.h"
#include <iostream>
using namespace std;
#include <string>

//#using "E:\��Ŀ\test\AlterSerMsgWithWMS\AlterSerMsgWithWMS\Debug\MsgWithWMS.dll"
#using "Debug\MsgWithWMS.dll"
using namespace MsgWithWMS;

#ifdef _MANAGED
#pragma managed(push, off)
#endif

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
    return TRUE;
}

#ifdef _MANAGED
#pragma managed(pop)
#endif

CAlterSerMsgWithWMS::CAlterSerMsgWithWMS()
{
	return;
}

void WriteLog( const char* str )
{
	char szLogFile[] = "C:\\dySMS.log";

	// �ж��ļ���С���ڲ����ļ��������ʵ��
	FILE *log = fopen( szLogFile,"a+");
	if( log != NULL )
	{
		fprintf( log, "%s\n",  str );
		fclose( log );
	}

	return;
}

//extern "C" ALTERSERMSG_API 
int getinfo(string&retstr)
{
	retstr = "aaa�����ͽӿ�";
	return 1;
}

//extern "C" ALTERSERMSG_API 
int run(char *source, char *destination, char *content)
{
	WriteLog("����run����");
	//AFX_MANAGE_STATE(AfxGetStaticModuleState());
	//USES_CONVERSION;

	//���ʼ�ģ���л�ȡ����������״̬���������ơ����������豸������
	string strAlertContent;// = content;
	char tmpBuf[1024] = {0};
	sprintf(tmpBuf, "param1:%s param2:%s param3:%s", source, destination, content);
	strAlertContent = tmpBuf;
	WriteLog(tmpBuf);
	//OutputDebugString(strAlertContent.c_str());

	string strMotDtr, strMonitor, strDevice;
	//���������
	//strAlertContent
	string::size_type nMotDtr = strAlertContent.find("@MonitorDstr@");
	if (nMotDtr != string::npos)
	{
		strMotDtr = strAlertContent.substr(0, nMotDtr);
	}
	//��ȡ������ping��ͨ��Ϣ���жϷ���������״̬
	bool PingTn = strMotDtr.find("ERROR");
	int status;
	if (true == PingTn)
	{
		status = -1;
	}
	else
	{
		status = 0;
	}

	//��������Ӧʱ��
	int responseTime = strMotDtr.find("(ms)=", 4);
	//string responseTime;
	//size_t nrponseT = strMotDtr.find("(ms)=");
	//size_t nrponlen = strMotDtr.length();
	//if (nrponseT != string::npos)
	//{
	//	responseTime = strMotDtr.substr(nrponseT+1, nrponseT+4);
	//}


	//��������
	size_t nMonitor = strAlertContent.find("@Monitor@");
	if (nMonitor != string::npos)
	{
		strMonitor = strAlertContent.substr(0, nMonitor);
	}

	//�豸����
	size_t nDevice = strAlertContent.find("@Device@");
	if (nDevice != string::npos)
	{
		strDevice = strAlertContent.substr(0, nDevice);
	}

	//hostIpͨ���豸����������IP
	string hostIp;
	size_t nhostIp = strDevice.find(":");
	size_t nhostlen = strDevice.length();
	if (nhostIp != string::npos)
	{
		hostIp = strDevice.substr(nhostIp+1, nhostlen);
	}

	//cpu����
	int cpuCount = 1;

	//cpuʹ����
	int cpuUsePercent = strMotDtr.find("CPU�ۺ�ʹ����(%)=", 4);
	//size_t ncpuUs = strMotDtr.find("CPU�ۺ�ʹ����(%)=");
	//if (ncpuUs != string::npos)
	//{
	//	cpuUsePercent = strMotDtr.substr(ncpuUs+1, ncpuUs+4);
	//}

	//��ȡ�ڴ��С
	int memoryTotal = strMotDtr.find("�����ڴ��ܿռ�(MB)=", 4);
	//size_t nmemTo = strMotDtr.find("�����ڴ��ܿռ�(MB)=");
	//if (nmemTo != string::npos)
	//{
	//	memoryTotal = strMotDtr.substr(nmemTo+1, nmemTo+4);
	//}

	//��ȡ�ڴ�ʹ����
	int memoryUsePercent = strMotDtr.find("�����ڴ�������(%)=", 4);
	//size_t nmemUs = strMotDtr.find("�����ڴ�������(%)=");
	//if (nmemUs != string::npos)
	//{
	//	memoryUsePercent = strMotDtr.substr(nmemUs+1, nmemUs+4);
	//}

	//ͨ����������ƻ�ȡdisk���������
	string diskName;
	size_t ndiskNa = strMonitor.find("Disk:");
	if (ndiskNa != string::npos)
	{
		diskName = strMotDtr.substr(ndiskNa+1, ndiskNa+2);
	}

	//��ȡdisk��С
	int diskTotal = strMotDtr.find("�ܿռ�(MB)=", 4);
	//size_t ndiskTo = strMotDtr.find("�ܿռ�(MB)=");
	//if (ndiskTo != string::npos)
	//{
	//	diskTotal = strMotDtr.substr(ndiskTo+1, ndiskTo+4);
	//}

	//��ȡdiskʹ����
	int diskUsePercent = strMotDtr.find("Diskʹ����(%)=", 4);
	//size_t ndiskUse = strMotDtr.find("Diskʹ����(%)=");
	//if (ndiskUse != string::npos)
	//{
	//	diskUsePercent = strMotDtr.substr(ndiskUse+1, ndiskUse+4);
	//}

	MsgWithWMSClass ^msgWithWMS=gcnew MsgWithWMSClass();

	if (status = -1)
	{
		msgWithWMS->SendStatusMSG("hostIp", -1, 0);

		return -1;
	}
	else
	{
		msgWithWMS->SendStatusMSG("hostIp", 0, responseTime);
		msgWithWMS->SendCpuMSG("hostIp", 1, cpuUsePercent);
		msgWithWMS->SendMemoryMSG("hostIp", memoryTotal, memoryUsePercent);
		msgWithWMS->SendDiskMSG("hostIp", "diskName", diskTotal, diskUsePercent);

		return 0;
	}
}