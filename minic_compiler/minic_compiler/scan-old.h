#ifndef MINIC_SCAN
#define MINIC_SCAN

#define SCAN_MAX_TOKEN_NUM	512		//4-13
#define SCAN_MAX_INPUT_BUF	1024	//4-13

struct ScanToken{
	int type;
	union{
		char str[64];
		int iVal;
		float fVal;
		char cVal;
	} value;
};

struct ScanToken scanTokens[SCAN_MAX_TOKEN_NUM];


// preprocessing
void preprocess(char* file){
	FILE* fp;
	FILE* ofp;
	char c;
	char next;
	int status = 0;
	if((fp = fopen(file, "r")) == NULL){ // open the input file
		printf("can not open the input file %s\n", file);
		return;
	}
	if((ofp = fopen("pre.c", "w")) == NULL){ // open the input file
		printf("can not open the input file %s\n", file);
		return;
	}
	while (!feof(fp)) 
	{
		c = fgetc(fp);  //read one char from input file
		if(c == '/'){
			next = fgetc(fp);
			if(next == '/'){
				status = 1;
				//printf("comment found!\n");
			}
		}
		if(status == 0){
			//printf("%c", c);
			fprintf(ofp, "%c", c);
		}
		if(c == '\n'){
			status = 0;
		}
	}
	fclose(fp);  //close the file
	fclose(ofp);
}

int isDigit(char c){
	if( c >= '0' && c <= '9'){
		return 1;
	}else{
		return 0;
	}
}
int isLetter(char c){
	if( c >= 'a' && c <= 'z'){
		return 1;
	}else if(c >= 'A' && c <= 'Z'){
		return 1;
	}else{
		return 0;
	}
}

int isSpace(char c){
	if( c == ' ' || c == '\t' || c == '\n'){
		return 1;
	}else{
		return 0;
	}
}

int isSeparator(char c){

	if( c == '(' || c == ')' || c == '{' ||
		c == '}' || c == '[' || c == ']' ||
		c == ',' || c == ';'){
		return 1;
	}else{
		return 0;
	}
}
void scan(char* file){
	FILE* fp;
	char c;
	int status; 
	if((fp = fopen(file, "r")) == NULL){ // open the input file
		printf("can not open the input file %s\n", file);
		return;
	}
	status = 0;
	while(!feof(fp)){
		c = fgetc(fp);		// read a new char
		if(status == 0){	// we are not in any words
			if(isDigit(c)){ // starting a constant
				printf("CONST:%c", c);
				status = 1;
			}
			if(isLetter(c)){
				printf("ID:%c", c);
				status = 2;
			}
			if(c == '+' || c == '-'){
				printf("OP:%c\n", c);
			}
			if(isSeparator(c)){
				printf("SP:%c\n", c);
			}
			if(isSpace(c)){
				continue;
			}
		}
		else if(status == 1){ // we are in a constant
			if(!isDigit(c)){	// need to stop here
				status = 0;
				ungetc(c, fp);
				printf("\n");
			}else{ // just output the number
				printf("%c", c);
			}
		}
		else if(status = 2){ // we are in a id
			if(isSpace(c) || isSeparator(c)){	// need to stop here
				status = 0;
				ungetc(c, fp);
				printf("\n");
			}else{ // just output the number
				printf("%c", c);
			}
		}
	}
	fclose(fp);
}

#endif