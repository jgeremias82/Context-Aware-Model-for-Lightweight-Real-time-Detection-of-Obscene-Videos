#include <stdlib.h>
#include <stdio.h>
#include "md5.h"

int main(int argc, char** argv)
{
	MD5 md5;
	
	//puts(md5.digestString("HELLO THERE I AM MD5!"));

	FILE *pf;
	char str[100]="\0";
	if ((pf = fopen("arquivo.csv", "a")) == NULL)
	{
		printf("\nNao consigo abrir o arquivo ! ");
		exit(1);
	}

	strcat(str,argv[1]);
	strcat(str, ";");
	strcat(str, md5.digestFile(argv[1]));

	fputs(str, pf);
	putc('\n', pf);
			
	if (ferror(pf))
	{
		perror("Erro na gravacao");
		fclose(pf);
		exit(1);
	}
	fclose(pf);
	return 0;

}