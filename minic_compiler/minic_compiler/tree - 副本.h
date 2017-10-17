#ifndef MINIC_TREE
#define MINIC_TREE

#include "stdio.h"
#include "stdlib.h"

#define CHILDREN_NUM	128

#define NODE_TYPE_STMT		0	//statement
#define NODE_TYPE_EXPR		1	//expression
#define NODE_TYPE_TERM		2	//term
#define NODE_TYPE_OP_PLUS	3	//op +
#define NODE_TYPE_OP_MINUS	4	//op -
#define NODE_TYPE_CONST		5	//constant
#define NODE_TYPE_ID		6	//id

#define NODE_TYPE_STMT_ASIGN	100	//asignment statement
#define NODE_TYPE_OP_ASIGN		101	//op =
#define NODE_TYPE_SP_SC			201	//op ;


struct TreeNode {
	int type;
	int cnum;
	struct TreeNode* children[CHILDREN_NUM];
};

// create a tree node for a given type
struct TreeNode* createNode(int type){
	struct TreeNode* pnode = 
		(struct TreeNode*)malloc(sizeof(struct TreeNode));
	//set the type
	pnode->type = type;
	pnode->cnum = 0;
	return pnode;
}

// add a tree node as child to a given tree node
void addChild(struct TreeNode* parent, struct TreeNode* child){
	if(parent->cnum < CHILDREN_NUM){
		parent->children[parent->cnum] = child;
		parent->cnum++;
	}else{// we do not have enough space for the new child
		printf("children list is full!\n");
	}
}

struct TreeNode* buildTree(){
	struct TreeNode* pExpr = createNode(NODE_TYPE_EXPR);
	struct TreeNode* pExpr1 = createNode(NODE_TYPE_EXPR);
	struct TreeNode* pOp = createNode(NODE_TYPE_OP_MINUS);
	struct TreeNode* pTerm = createNode(NODE_TYPE_TERM);

	addChild(pExpr, pExpr1);
	addChild(pExpr, pOp);
	addChild(pExpr, pTerm);

	struct TreeNode* pExpr2 = createNode(NODE_TYPE_EXPR);
	struct TreeNode* pOp1 = createNode(NODE_TYPE_OP_PLUS);
	struct TreeNode* pTerm1 = createNode(NODE_TYPE_TERM);

	addChild(pExpr1, pExpr2);
	addChild(pExpr1, pOp1);
	addChild(pExpr1, pTerm1);

	struct TreeNode* pTerm2 = createNode(NODE_TYPE_TERM);
	addChild(pExpr2, pTerm2);

	struct TreeNode* pConst1 = createNode(NODE_TYPE_CONST);
	addChild(pTerm2, pConst1);

	struct TreeNode* pConst2 = createNode(NODE_TYPE_CONST);
	addChild(pTerm1, pConst2);

	struct TreeNode* pConst3 = createNode(NODE_TYPE_CONST);
	addChild(pTerm, pConst3);

	return pExpr;
}

//get name for a given type
char* getNameForType(int type){
	if(type == NODE_TYPE_EXPR){
		return "EXPR";
	}else if(type == NODE_TYPE_TERM){
		return "TERM";
	}else if(type == NODE_TYPE_OP_PLUS){
		return "OP_PLUS";
	}else if(type == NODE_TYPE_OP_MINUS){
		return "OP_MINUS";
	}else if(type == NODE_TYPE_CONST){
		return "CONST";
	}else if(type == NODE_TYPE_EXPR){
		return "EXPR";
	}else if(type == NODE_TYPE_STMT){
		return "STMT";
	}else if(type == NODE_TYPE_ID){
		return "ID";
	}else if(type == NODE_TYPE_STMT_ASIGN){
		return "STMT_ASIGN";
	}else if(type == NODE_TYPE_OP_ASIGN){
		return "OP_ASIGN";
	}else if(type == NODE_TYPE_SP_SC){
		return "SP_SC";
	}else{
		return "UNKNOWN";
	}
}

void printTree(struct TreeNode* root, int level){
	for(int j = 0; j < level; j++){
		printf("\t");
	}
	printf("type=%s, cnum=%d\n", getNameForType(root->type), root->cnum);
	for(int i = 0; i < root->cnum; i++){
		printTree(root->children[i], level+1);
	}
}



#endif