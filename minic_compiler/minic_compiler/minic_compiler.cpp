#include "stdafx.h"

#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "symbol.h"
#include "tree.h"

//#include "token.h"
#include "scan.h"

#define ARG_NUM		1

#define TKN_CONST_INT		1		//integer
#define TKN_CONST_FLOAT		2		//float
#define TKN_ID				3		//identifier
#define	TKN_OP_PLUS			101		//+
#define TKN_OP_MINUS		102		//-
#define TKN_OP_MUL			103		//*
#define TKN_OP_DIV			104		// /
#define TKN_OP_ASIGN		105		// =
#define TKN_SP_SEMICOLON	201		// ;
#define TKN_END				1000	//the end of token stream	



// a = b + 20;
int tokens[10]={TKN_ID,    //token stream for the given statment
				TKN_OP_ASIGN,
				TKN_ID,
				TKN_OP_PLUS,
				TKN_CONST_INT,
				TKN_OP_MINUS,
				TKN_ID,
				TKN_SP_SEMICOLON,
				TKN_END
				};
int tokenIndex = 0;

struct SymbolTable* symbolTable = NULL;//4-8

//struct ScanToken scanTokens[SCAN_MAX_TOKEN_NUM];

void initScanTokens(){
	scanTokens[0].type = TKN_ID;			//a
	strcpy(scanTokens[0].value.str, "a"); 
	
	scanTokens[1].type = TKN_OP_ASIGN;		//=
	
	scanTokens[2].type = TKN_ID;			//b
	strcpy(scanTokens[2].value.str, "b");

	scanTokens[3].type = TKN_OP_PLUS;		//+
	
	scanTokens[4].type = TKN_CONST_INT;		//20
	scanTokens[4].value.iVal = 20;

	scanTokens[5].type = TKN_OP_MINUS;		//-
	
	scanTokens[6].type = TKN_ID;			//c
	strcpy(scanTokens[6].value.str, "c");
}

void ungetToken(){
	tokenIndex--;
}

int getNextToken(){
	int next = tokens[tokenIndex];
	if(next == TKN_END){
	}else{
		tokenIndex++;
	}
	return next;
}

void matchToken(int type){
	int lookahead = getNextToken();
	if(lookahead == type){
	}else{
		printf("Sytax error!\n");
	}
}

struct TreeNode* termHandler(){	// for nonterminal "term"
	int nextTkn = getNextToken();
	if(nextTkn == TKN_ID){
		struct TreeNode* root = createNode(NODE_TYPE_ID);
		printf("TKN_ID ");
		//////////////////////////4-8	
		struct SymbolEntry* sp = createSymbol(symbolTable, "i");
		sp->type = SYMBOL_TYPE_STRING;
		/////////////////////////////
		return root;
	}else if(nextTkn == TKN_CONST_INT){
		struct TreeNode* root = createNode(NODE_TYPE_CONST);
		printf("TKN_CONST_INT ");
		///////////////////////////4-8
		struct SymbolEntry* sp = createSymbol(symbolTable, "xxx");
		sp->type = SYMBOL_TYPE_INT;
		//////////////////////////////
		return root;
	}else {
		printf("Sytax error!\n");
		return NULL;
	}
}
struct TreeNode* expr2Handler(){//for nonterminal "expr'"
	int nextTkn = getNextToken();
	if(nextTkn == TKN_OP_PLUS){
		//matchToken(nextTkn);
		struct TreeNode* root = createNode(NODE_TYPE_EXPR);
		struct TreeNode* term = termHandler();
		printf("+ ");
		struct TreeNode* expr2 = expr2Handler();
		if(term != NULL){
			addChild(root, term);
		}
		if(expr2 != NULL){
			addChild(root, expr2);
		}
		return root;
	}else if(nextTkn == TKN_OP_MINUS){
		//matchToken(nextTkn);
		struct TreeNode* root = createNode(NODE_TYPE_EXPR);
		struct TreeNode* term = termHandler();
		printf("- ");
		struct TreeNode* expr2 = expr2Handler();
		if(term != NULL){
			addChild(root, term);
		}
		if(expr2 != NULL){
			addChild(root, expr2);
		}
		
		return root;
	}else{
		ungetToken();
		return NULL;
	}
}
struct TreeNode* exprHandler(){ //for nonterminal "expr"
	struct TreeNode* root = createNode(NODE_TYPE_EXPR);

	struct TreeNode* term = termHandler();
	struct TreeNode* expr2 = expr2Handler();
	if(term != NULL){
		addChild(root, term);
	}
	if(expr2 != NULL){
		addChild(root, expr2);
	}
	return root;
}
struct TreeNode* asignStmtHandler(){ // for nonterminal "asignstmt"
	struct TreeNode* root = createNode(NODE_TYPE_STMT_ASIGN);

	matchToken(TKN_ID);
	struct TreeNode* tknId = createNode(NODE_TYPE_ID);
	addChild(root, tknId);
	printf("TKN_ID ");				//translation
	
	matchToken(TKN_OP_ASIGN);
	struct TreeNode* tknAsign = createNode(NODE_TYPE_OP_ASIGN);
	addChild(root, tknAsign);
	
	struct TreeNode* expr = exprHandler();
	addChild(root, expr);
	
	matchToken(TKN_SP_SEMICOLON);
	struct TreeNode* tknSc = createNode(NODE_TYPE_SP_SC);
	addChild(root, tknSc);

	printf("= \n");	//translation

	return root;
}



int main(int argc, char* argv[])
{
	if(argc <= ARG_NUM){
		printf("The input file is missing!\n");
		return -1;
	}
	printf("%s\n", argv[1]);
	preprocess(argv[1]);

	symbolTable = createSymbolTable(NULL);
	struct TreeNode* root = asignStmtHandler();
	printTree(root, 0);
	return (0);
}

