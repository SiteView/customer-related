// AlterSerMsgWithWMS.cpp : 定义 DLL 应用程序的入口点。
//

#include "stdafx.h"
#include "AlterSerMsgWithWMS.h"
#include <iostream>
using namespace std;
#include <string>

//#using "E:\项目\test\AlterSerMsgWithWMS\AlterSerMsgWithWMS\Debug\MsgWithWMS.dll"
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

	// 判断文件大小：在不打开文件的情况下实现
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
	retstr = "aaa服务发送接口";
	return 1;
}

//extern "C" ALTERSERMSG_API 
int run(char *source, char *destination, char *content)
{
	WriteLog("进入run函数");
	//AFX_MANAGE_STATE(AfxGetStaticModuleState());
	//USES_CONVERSION;

	//从邮件模板中获取服务器连接状态、监测点名称、监测点所在设备的名称
	string strAlertContent;// = content;
	char tmpBuf[1024] = {0};
	sprintf(tmpBuf, "param1:%s param2:%s param3:%s", source, destination, content);
	strAlertContent = tmpBuf;
	WriteLog(tmpBuf);
	//OutputDebugString(strAlertContent.c_str());

	string strMotDtr, strMonitor, strDevice;
	//监测器描述
	//strAlertContent
	string::size_type nMotDtr = strAlertContent.find("@MonitorDstr@");
	if (nMotDtr != string::npos)
	{
		strMotDtr = strAlertContent.substr(0, nMotDtr);
	}
	//获取描述中ping不通信息来判断服务器连接状态
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

	//服务器响应时间
	int responseTime = strMotDtr.find("(ms)=", 4);
	//string responseTime;
	//size_t nrponseT = strMotDtr.find("(ms)=");
	//size_t nrponlen = strMotDtr.length();
	//if (nrponseT != string::npos)
	//{
	//	responseTime = strMotDtr.substr(nrponseT+1, nrponseT+4);
	//}


	//监测点名称
	size_t nMonitor = strAlertContent.find("@Monitor@");
	if (nMonitor != string::npos)
	{
		strMonitor = strAlertContent.substr(0, nMonitor);
	}

	//设备名称
	size_t nDevice = strAlertContent.find("@Device@");
	if (nDevice != string::npos)
	{
		strDevice = strAlertContent.substr(0, nDevice);
	}

	//hostIp通过设备名解析主机IP
	string hostIp;
	size_t nhostIp = strDevice.find(":");
	size_t nhostlen = strDevice.length();
	if (nhostIp != string::npos)
	{
		hostIp = strDevice.substr(nhostIp+1, nhostlen);
	}

	//cpu个数
	int cpuCount = 1;

	//cpu使用率
	int cpuUsePercent = strMotDtr.find("CPU综合使用率(%)=", 4);
	//size_t ncpuUs = strMotDtr.find("CPU综合使用率(%)=");
	//if (ncpuUs != string::npos)
	//{
	//	cpuUsePercent = strMotDtr.substr(ncpuUs+1, ncpuUs+4);
	//}

	//获取内存大小
	int memoryTotal = strMotDtr.find("物理内存总空间(MB)=", 4);
	//size_t nmemTo = strMotDtr.find("物理内存总空间(MB)=");
	//if (nmemTo != string::npos)
	//{
	//	memoryTotal = strMotDtr.substr(nmemTo+1, nmemTo+4);
	//}

	//获取内存使用率
	int memoryUsePercent = strMotDtr.find("物理内存利用率(%)=", 4);
	//size_t nmemUs = strMotDtr.find("物理内存利用率(%)=");
	//if (nmemUs != string::npos)
	//{
	//	memoryUsePercent = strMotDtr.substr(nmemUs+1, nmemUs+4);
	//}

	//通过监测器名称获取disk监测器命名
	string diskName;
	size_t ndiskNa = strMonitor.find("Disk:");
	if (ndiskNa != string::npos)
	{
		diskName = strMotDtr.substr(ndiskNa+1, ndiskNa+2);
	}

	//获取disk大小
	int diskTotal = strMotDtr.find("总空间(MB)=", 4);
	//size_t ndiskTo = strMotDtr.find("总空间(MB)=");
	//if (ndiskTo != string::npos)
	//{
	//	diskTotal = strMotDtr.substr(ndiskTo+1, ndiskTo+4);
	//}

	//获取disk使用率
	int diskUsePercent = strMotDtr.find("Disk使用率(%)=", 4);
	//size_t ndiskUse = strMotDtr.find("Disk使用率(%)=");
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