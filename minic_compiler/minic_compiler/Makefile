main: minic_compiler.o
	g++ -o minic_compiler minic_compiler.o

main.o: minic_compiler.cpp tree.h
	g++ -o minic_compiler.o -c minic_compiler.cpp
clean:
	        rm *.o minic_compiler
