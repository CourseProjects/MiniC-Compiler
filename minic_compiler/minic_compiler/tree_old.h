#ifndef TREE_NODE_FILE
#define TREE_NODE_FILE

#include "stdio.h"
#include "stdlib.h"

#define TREE_TYPE_IDENTIFIER	1
#define TREE_TYPE_CONST			2
#define TREE_TYPE_OPERATOR_TIME	3
#define TREE_TYPE_OPERATOR_PLUS	4
#define TREE_TYPE_OPERATOR_MINUS 5

#define TREE_CHILDREN_MAX		10

#define TREE_TYPE_PLIST			100
#define TREE_TYPE_FBODY			101
#define TREE_TYPE_FUNCTION		102
#define TREE_TYPE_PROGRAM		103
#define TREE_TYPE_NAME			104
#define TREE_TYPE_TYPE			105

#define TREE_TYPE_STMTS			106
#define TREE_TYPE_STMT			107
#define TREE_TYPE_DSTMT			108
#define TREE_TYPE_IDLIST		109
#define TREE_TYPE_ASTMT			110
#define TREE_TYPE_RSTMT			111
#define TREE_TYPE_EXPR			112				//0608
#define TREE_TYPE_TERM			113


struct TreeNode{
	int type;
	int childrenNum;
	struct TreeNode* children[TREE_CHILDREN_MAX];
	char name[256];				//
	char number[256];			//

	float value;
	int index;				//20150603
};

struct TreeNode* createNode(int type){
	//allocate a block of memory from the system
	struct TreeNode* node = (struct TreeNode*)malloc(sizeof(struct TreeNode));
	if(node == NULL){
		printf("Out of memory!");
		exit(-1);
	}
	//initiliaze
	node->type = type;
	node->childrenNum = 0;
	for(int i = 0; i < TREE_CHILDREN_MAX; i++){
		node->children[i] = NULL;
	}
	return node;
}
void addChild(struct TreeNode* f, struct TreeNode* c){
	if(f->childrenNum >= TREE_CHILDREN_MAX){
		printf("out of index!");
		exit(-1);
	}
	f->children[f->childrenNum] = c;
	f->childrenNum += 1;
}

char* getNodeName(int type){
	if(type == TREE_TYPE_PLIST){
		return "PLIST";
	}else if(type == TREE_TYPE_FBODY){
		return "FBODY";
	}else if(type == TREE_TYPE_FUNCTION){
		return "FUNCTION";
	}else if(type == TREE_TYPE_PROGRAM){
		return "PROGRAM";
	}else if(type == TREE_TYPE_NAME){
		return "NAME";
	}else if(type == TREE_TYPE_TYPE){
		return "TYPE";
	}else if(type == TREE_TYPE_STMT){		//20150527
		return "STMT";
	}else if(type == TREE_TYPE_STMTS){
		return "STMTS";
	}else if(type == TREE_TYPE_DSTMT){
		return "DSTMT";
	}else if(type == TREE_TYPE_ASTMT){
		return "ASTMT";
	}else if(type == TREE_TYPE_RSTMT){
		return "RSTMT";
	}else if(type == TREE_TYPE_IDENTIFIER){
		return "IDENTIFIER";
	}else if(type == TREE_TYPE_EXPR){
		return "EXPR";
	}else if(type == TREE_TYPE_TERM){
		return "TERM";
	}else if(type == TREE_TYPE_OPERATOR_MINUS){
		return "MINUS";
	}else{
		return "UNKNOWN";
	}
}

void printTree(struct TreeNode* root, int level){
	int i;
	for(i = 0; i < level; i++){
		printf("    ");
	}
	printf("Type=%s, childrenNum=%d, value=%f\n", getNodeName(root->type), 
		root->childrenNum, root->value);
	for(i = 0; i < root->childrenNum; i++){
		printTree(root->children[i], level+1);
	}
}

void setValue(struct TreeNode* node, float val){
	node->value = val;
}

float getValue(struct TreeNode* node){
	return node->value;
}
#endif