#ifdef ALTERSERMSG_EXPORTS
#define ALTERSERMSG_API __declspec(dllexport)
#else
#define ALTERSERMSG_API __declspec(dllimport)
#endif

#include <string>
using namespace std;

class ALTERSERMSG_API CAlterSerMsgWithWMS{
public:
	CAlterSerMsgWithWMS(void);
	
};

extern "C" ALTERSERMSG_API int getinfo(string& retstr);
extern "C" ALTERSERMSG_API int run(char *source, char *destination, char *content);