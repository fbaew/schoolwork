main: numbers parser
	gcc lex.yy.c y.tab.c -o main

numbers: numbers.l
	lex numbers.l

parser:  treeParse.y
	bison -d -y treeParse.y

clean:
	rm main
