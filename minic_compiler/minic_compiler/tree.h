#ifndef MINIC_TREE
#define MINIC_TREE

#include <stdio.h>
#include <stdlib.h>
#define CHILDREN_NUM 128


#define NODE_TYPE_EXPR 1
#define NODE_TYPE_TERM 2
#define NODE_TYPE_OP_PLUS 3
#define NODE_TYPE_OP_MINUS 4
#define NODE_TYPE_CONST 5
#define NODE_TYPE_ID    6

#define NODE_TYPE_STMT_ASIGN 100
#define NODE_TYPE_OP_ASIGN        101
#define NODE_TYPE_SP_SC      201

struct TreeNode{
  int type;
  int cnum;
  struct TreeNode* children[CHILDREN_NUM];
};

struct TreeNode* createNode(int type){
  struct TreeNode* pnode = (struct TreeNode*)malloc(sizeof(struct TreeNode));
  //set type
  pnode->type = type;
  pnode->cnum = 0;
  return pnode;
}

#endif

//add a tree node as a child to a given tree node
void addChild(struct TreeNode* parent, struct TreeNode* child)
{
  if(parent->cnum < CHILDREN_NUM){
    parent -> children[parent->cnum] = child;
    parent -> cnum++;
  }
  else{//not enough space for thenew chiild
    printf("children list is full!\n");
  }
}

struct TreeNode* buildTree(){
  struct TreeNode* pExpr = createNode(NODE_TYPE_EXPR);
  struct TreeNode* pExpr1 = createNode(NODE_TYPE_EXPR);
  struct TreeNode* p0p = createNode(NODE_TYPE_OP_MINUS);
  struct TreeNode* pTerm = createNode(NODE_TYPE_TERM);
  
  addChild(pExpr, pExpr1);
  addChild(pExpr, p0p);
  addChild(pTerm, pTerm);
  
  struct TreeNode* pExpr2 = createNode(NODE_TYPE_EXPR);
  struct TreeNode* p0p1 = createNode(NODE_TYPE_OP_PLUS);
  struct TreeNode* pTerm1 = createNode(NODE_TYPE_TERM);

  addChild(pExpr1, pExpr2);
  addChild(pExpr1, p0p1);
  addChild(pExpr1, pTerm1);

  struct TreeNode* pTerm2 = createNode(NODE_TYPE_TERM);
  addChild(pExpr2, pTerm2);

  struct TreeNode* pConst1 = createNode(NODE_TYPE_TERM);
  addChild(pTerm2, pConst1);

  struct TreeNode* pConst2 = createNode(NODE_TYPE_TERM);
  addChild(pTerm1, pConst2);

  struct TreeNode* pConst3 = createNode(NODE_TYPE_TERM);
  addChild(pTerm, pConst3);

  return pExpr;
}

char* getNameforType(int type) {
    if(type == NODE_TYPE_EXPR) {
      return (char*)"EXPR";
  }
  else if(type == NODE_TYPE_TERM){
    return (char*)"TERM";
  }
  else if(type == NODE_TYPE_OP_PLUS) {
    return (char*)"OP_PLUS";
  }
  else if(type == NODE_TYPE_OP_MINUS) {
    return (char*)"OP_MINUS";
  }
  else if(type == NODE_TYPE_CONST){
    return (char*)"CONST";
  }
    
  else if(type == NODE_TYPE_ID){
      return (char*)"ID";
  }
  else if(type == NODE_TYPE_STMT_ASIGN){
      return (char*)"STMT_ASSIGN";
  }
  else if(type == NODE_TYPE_OP_ASIGN){
      return (char*)"OP_ASIGN";
  }
  else if(type == NODE_TYPE_SP_SC){
      return (char*)"OP_SC";
  }
  else{
    return (char*)"UNKNOWN";
    }
}
void printTree(struct TreeNode* root, int level){
  for(int i = 0; i < level; i++)
    printf("\t");
  printf("type=%s, cnum=%d\n", getNameforType(root ->type), root->cnum);
  for(int i = 0; i < root -> cnum; i++){
    printTree(root->children[i], level+1);
  }
}

//#endif
