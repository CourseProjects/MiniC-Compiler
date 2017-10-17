#ifndef MINIC_TOKEN
#define MINIC_TOKEN

struct ScanToken{
	int type;
	union{
		char str[64];
		int iVal;
		float fVal;
		char cVal;
	} value;
};

#endif