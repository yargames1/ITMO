CC = gcc
CFLAGS = -std=c17 -pedantic -Wall -Werror -Iinclude

OBJS = src/main.o src/bmp.o src/transform.o

image-transform.exe: $(OBJS)
	$(CC) $(CFLAGS) -o image-transform.exe $(OBJS)

src/main.o: src/main.c include/bmp.h include/transform.h
	$(CC) $(CFLAGS) -c src/main.c -o src/main.o

src/bmp.o: src/bmp.c include/bmp.h
	$(CC) $(CFLAGS) -c src/bmp.c -o src/bmp.o

src/transform.o: src/transform.c include/transform.h include/bmp.h
	$(CC) $(CFLAGS) -c src/transform.c -o src/transform.o

clean:
	rm -f src/*.o image-transform.exe
