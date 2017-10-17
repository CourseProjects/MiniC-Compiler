#ifndef MINIC_SYMBOL
#define MINIC_SYMBOL

#define SYMBOL_NAME_LEN 16 //lgt name

enum SymbolType{
  SYMBOL_TYPE_INT = 0,
  SYMBOL_TYPE_STRING,
  SYMBOL_TYPE_FLOAT,
  SYMBOL_TYPE_CHAR,
  SYMBOL_TYPE_DOUBLE
};

struct SymbolEntry {
  char name[SYMBOL_NAME_LEN];
  int type;
  int addr;
};

#define SYMBOL_TABLE_MAX  64 //max, nb of entires in a table

//symbol table
struct SymbolTable{
  struct SymbolEntry* entries[SYMBOL_TABLE_MAX]; //entry list
  int number;//nb entries
  //points to the table of eclosing scope
  struct SymbolTable* pre;
};

struct SymbolTable* createSymbolTable(struct SymbolTable* pre){
  struct SymbolTable* table = (struct SymbolTable*) malloc(sizeof(SymbolTable));
  table->number = 0;
  table->pre = pre;

  return table;
  }

struct SymbolEntry* createSymbol(struct SymbolTable* table, char* name){
  struct SymbolEntry* se = (struct SymbolEntry*) malloc(sizeof(struct SymbolEntry*));
  strcpy(se->name, name);
  //check if there is an empty slot for this new symbol
  if(table->number>= SYMBOL_TABLE_MAX){
    printf("The table is full!\n");
    exit(0);
  }
  //store new symbol entry in table
  table->entries[table->number] = se;
  table->number++; //increase counter
  return se;
}

struct SymbolEntry* findSymbol(struct SymbolTable* table, char* name){
  int i = 0;
  for(;i<table->number;i++){
    if(strcmp(table->entries[i]->name, name) == 0){
      return /*&(*/table->entries[i]/*)*/; //found here
    }
  }
  //symbol not found
  return NULL;
}

//set a type for a given symbol
void setType(struct SymbolTable* table, char* name, int type){
  struct SymbolEntry* se = findSymbol(table, name);
  if(se){
    se->type = type;
  }
}

void printEntry(struct SymbolEntry* entry){
  printf("name=%s, type=%d\n", entry->name, entry->type);
}

void printTable(struct SymbolTable* table){
  int i = 0;
  /*for (; i<table->number;i++)
    printf("%s ", (char*)table -> entries[i] ->name);
    printf("\n");*/
  for(;i<table->number;i++){
    printEntry(table->entries[i]);
  }
}

void buildTable(){
  struct SymbolTable* table = createSymbolTable(NULL);
  /*struct SymbolEntry* ep =*/ createSymbol(table, (char*)"i");
  setType(table, (char*)"i", SYMBOL_TYPE_FLOAT);
  //ep-> type = SYMBOL_TYPE_FLOAT;
  printTable(table);
}
#endif
