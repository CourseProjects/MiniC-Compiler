#ifndef MINIC_SYMBOL
#define MINIC_SYMBOL

#define SYMBOL_NAME_LEN		16	// the length of name

//symbol types
enum SymbolType{
	SYMBOL_TYPE_INT = 0,
	SYMBOL_TYPE_FLOAT,
	SYMBOL_TYPE_CHAR,
	SYMBOL_TYPE_DOUBLE,
	SYMBOL_TYPE_STRING	 //------------
};
//symbol entry
struct SymbolEntry {
	char name[SYMBOL_NAME_LEN];	//name
	int  type;					//type
	int  addr;					//memory location
};

#define SYMBOL_TABLE_MAX	64	//max. number of entries in a table
//symbol table
struct SymbolTable {
	struct SymbolEntry* entries[SYMBOL_TABLE_MAX];	//entry list
	int number;								//number of entries
	//points to the table of enclosing scope
	struct SymbolTable* pre;				
};

//create a new symbol table
struct SymbolTable* createSymbolTable(struct SymbolTable* pre){
	struct SymbolTable* table = (struct SymbolTable*) malloc(
		sizeof(SymbolTable));
	table->number = 0;
	table->pre = pre;

	return table;
}

//create a symbol in a table
struct SymbolEntry* createSymbol(struct SymbolTable* table, char* name){
	struct SymbolEntry* se = (struct SymbolEntry*)malloc(
		sizeof(struct SymbolEntry));
	strcpy(se->name, name);
	//check if there is empty slot for this new symbol
	if(table->number >= SYMBOL_TABLE_MAX){
		printf("The table is full!\n");
		exit(0);
	}
	//store the new symbol entry in the table
	table->entries[table->number] = se;
	table->number++;	//increase the counter
	return se;
}

//find a symbol in a table according to the name
struct SymbolEntry* findSymbol(struct SymbolTable* table, char* name){
	int i = 0;
	for(; i < table->number; i++){
		if(strcmp(table->entries[i]->name, name) == 0){
			return table->entries[i];	//found here
		}
	}
	// the symbol is not found
	return NULL;
}

//set the type for a given symbol
void setType(struct SymbolTable* table, char* name, int type){
	struct SymbolEntry* se = findSymbol(table, name);
	if(se != NULL){
		se->type = type;
	}
}
//print out the entry
void printEntry(struct SymbolEntry* entry){
	printf("name:%s,type:%d\n", entry->name, entry->type);
}
//print out the table
void printTable(struct SymbolTable* table){
	int i = 0;
	for(; i < table->number; i++){
		printEntry(table->entries[i]);
	}
}

void buildTable(){
	struct SymbolTable* table = createSymbolTable(NULL);
	struct SymbolEntry* ep = createSymbol(table, "i");
	//setType(table, "i", SYMBOL_TYPE_FLOAT);
	ep->type = SYMBOL_TYPE_FLOAT;
	printTable(table);
}

#endif