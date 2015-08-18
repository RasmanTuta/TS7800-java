PREFIX=/usr
DEBUG?=n
DMALLOC?=n
JAVA?=n
CROSSCOMPILE?=n
ZEROCONF?=avahi
ZEROCONF_LOOKUP?=n

ifeq ($(CROSSCOMPILE),y)
CC=arm-linux-gcc
NM=arm-linux-nm
AR=arm-linux-ar
CXX=arm-linux-g++
CPP=arm-linux-cpp
LD=arm-linux-ld
endif

ifeq ($(JAVA),y)
JAVAHOME := /usr/java/j2sdk1.4.2_18
JAVAFLAGS=-I$(JAVAHOME)/include -I$(JAVAHOME)/include/linux
else
JAVAFLAGS=
endif

ifeq ($(DMALLOC),y)
DEBUGFLAGS=-DDMALLOC -DDMALLOC_FUNC_CHECK
EXTRALIBS=-ldmalloc
DEBUG=y
endif

ifeq ($(DEBUG),y)
DEBUGFLAGS := $(DEBUGFLAGS) -g -O0 -D_DEBUG -DDEBUG
else
DEBUGFLAGS=-Os
endif

ifeq ($(ZEROCONF),avahi)
ZEROCONFFILE=zeroconf_avahi.c
else
ZEROCONFFILE=zeroconf.c
endif

ifeq ($(ZEROCONF_LOOKUP),y)
ZEROCONFFILE := $(ZEROCONFFILE) zeroconf_lookup.c
CFLAGS := $(CFLAGS) -DZEROCONF_LOOKUP
endif

INSTALL = install
INSTALL_FILE = $(INSTALL) -m 644
INSTALL_LIB  = $(INSTALL) -m 644

LIBS := -lm -lpthread -ldl $(EXTRALIBS)
CFLAGS := $(CFLAGS) -Wall -Wno-strict-aliasing $(DEBUGFLAGS) -fpic -D_LINUX $(JAVAFLAGS) -I. -Iutils

lib_rasmantuta_ts7800cs := PeekPoke.c \
	Watchdog.c \
	ReadAnalog.c 

lib_rasmantuta_ts7800objs := $(lib_rasmantuta_ts7800cs:.c=.o)

all: lib_rasmantuta_ts7800.so


lib_rasmantuta_ts7800.so: $(lib_rasmantuta_ts7800objs)
	$(CC) -g -shared -o lib_rasmantuta_ts7800.so $(lib_rasmantuta_ts7800objs) $(LIBS)

clean: 
	-rm *.a *~ *.so *.o

install: all
	$(INSTALL_LIB) lib_rasmantuta_ts7800.so $(INSTALLPREFIX)/$(PREFIX)/lib

uninstall:
	-rm $(INSTALLPREFIX)/$(PREFIX)/lib/lib_rasmantuta_ts7800.*
	