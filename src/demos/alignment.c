/* A simple example of memory alignment. Run on a 64-bit Mac OS x machine. */

/* To compile on gcc, run: gcc -o alignment alignment.c -Wall -O2, and then run using ./alignment. */

#include <stdio.h>
#include <stdlib.h>

typedef unsigned char byte; 

typedef struct myStruct {
<<<<<<< HEAD
	double d;
	int a;
=======
	int a;
	double d;
>>>>>>> changes
	char c;
	byte b;
} myStruct;

int main(int argc, char** argv){
	printf("On this machine, size of int = %lu bytes.\n", sizeof(int));
	printf("On this machine, size of a double = %lu bytes.\n", sizeof(double));
	printf("On this machine, size of a char = %lu bytes. \n", sizeof(char));
	printf("On this machine, size of a byte = %lu bytes. \n", sizeof(byte));
	printf("On this machine, size of a myStruct = %lu bytes.\n", sizeof(myStruct)); // Padding at word length (8 bytes for a 64-bit machine)
	return EXIT_SUCCESS;	
}
