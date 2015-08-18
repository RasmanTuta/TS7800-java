#include <unistd.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <stdio.h>
#include <fcntl.h>

#include "no_rasmantuta_ts7800_jni_PeekPoke.h"

unsigned char read8(int address);
unsigned short read16(int address);
unsigned int read32(int address);
void write8(int address, unsigned char value);
void write16(int address, unsigned short value);
void write32(int address, unsigned int value);
/*
 * Class:     no_rasmantuta_ts7800_jni_PeekPoke
 * Method:    tsPoke
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_no_rasmantuta_ts7800_jni_PeekPoke_tsPoke__II  (JNIEnv *env, jclass clazz, jint address, jint value){
	write32(address, value);
}

/*
 * Class:     no_rasmantuta_ts7800_jni_PeekPoke
 * Method:    tsPoke
 * Signature: (IS)V
 */
JNIEXPORT void JNICALL Java_no_rasmantuta_ts7800_jni_PeekPoke_tsPoke__IS  (JNIEnv *env, jclass clazz, jint address, jshort value){
	write16(address, value);
}

/*
 * Class:     no_rasmantuta_ts7800_jni_PeekPoke
 * Method:    tsPoke
 * Signature: (IB)V
 */
JNIEXPORT void JNICALL Java_no_rasmantuta_ts7800_jni_PeekPoke_tsPoke__IB  (JNIEnv *env, jclass clazz, jint address, jbyte value){
	write8(address, value);
}

/*
 * Class:     no_rasmantuta_ts7800_jni_PeekPoke
 * Method:    tsPeek32
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_no_rasmantuta_ts7800_jni_PeekPoke_tsPeek32  (JNIEnv *env, jclass clazz, jint address){
	return (jint)read32(address);
}

/*
 * Class:     no_rasmantuta_ts7800_jni_PeekPoke
 * Method:    tsPeek16
 * Signature: (I)S
 */
JNIEXPORT jshort JNICALL Java_no_rasmantuta_ts7800_jni_PeekPoke_tsPeek16  (JNIEnv *env, jclass clazz, jint address){
	return (jshort)read16(address);
}

/*
 * Class:     no_rasmantuta_ts7800_jni_PeekPoke
 * Method:    tsPeek8
 * Signature: (I)B
 */
JNIEXPORT jbyte JNICALL Java_no_rasmantuta_ts7800_jni_PeekPoke_tsPeek8  (JNIEnv *env, jclass clazz, jint address){
	return (jbyte)read8(address);
}

unsigned char* initPeakPoke(unsigned int address, int *handle){
	off_t addr, page;
	addr = address;
	unsigned char *start;
	*handle = open("/dev/mem", O_RDWR|O_SYNC);

	if (*handle == -1) {
		perror("open(/dev/mem):");
		return 0;
	}
	page = addr & 0xfffff000;
	start = mmap(0, getpagesize(), PROT_READ|PROT_WRITE, MAP_SHARED, *handle, page);
	if (start == MAP_FAILED) {
		perror("mmap:");
		return 0;
	}
	return start;
}

unsigned char read8(int address){
	int handle;

	fprintf(stderr,"In read8\n");
	unsigned char *start = initPeakPoke(address, &handle);
	unsigned char *chardat;
	if(start == 0)
		return 0; // java exception

	chardat = start + (address & 0xfff);
	close(handle);
	return *chardat;
}

unsigned short read16(int address){
	int handle;

	unsigned char *start = initPeakPoke(address, &handle);

	unsigned short *shortdat;

	if(start == 0)
		return 0; // java exception

	shortdat = (unsigned short *)(start + (address & 0xfff));

	unsigned short ret = *shortdat;
	close(handle);

	return ret;
}

unsigned int read32(int address){
	int handle;
	unsigned char *start = initPeakPoke(address, &handle);
	unsigned int *intdat;
	if(start == 0)
		return 0; // java exception

	intdat = (unsigned int *)(start + (address & 0xfff));
	close(handle);
	return *intdat;
}

void write8(int address, unsigned char value){
	int handle;

	fprintf(stderr,"In read8\n");
	unsigned char *start = initPeakPoke(address, &handle);
	unsigned char *chardat;
	if(start == 0)
		return; // java exception

	chardat = start + (address & 0xfff);
	*chardat = value;
	close(handle);
}

void write16(int address, unsigned short value){
	int handle;

	unsigned char *start = initPeakPoke(address, &handle);

	unsigned short *shortdat;

	if(start == 0)
		return; // java exception

	shortdat = (unsigned short *)(start + (address & 0xfff));
	*shortdat = value;
	close(handle);
}

void write32(int address, unsigned int value){
	int handle;
	unsigned char *start = initPeakPoke(address, &handle);
	unsigned int *intdat;
	if(start == 0)
		return; // java exception

	intdat = (unsigned int *)(start + (address & 0xfff));
	*intdat = value;
	close(handle);
}

/*
void throwIllegalArgument(char* message){
    jclass newExcCls;
    (*env)->ExceptionDescribe(env);
    (*env)->ExceptionClear(env);
    newExcCls = (*env)->FindClass(env,
                  "java/lang/IllegalArgumentException");
    if (newExcCls == NULL) {*/
        /* Unable to find the exception class, give up. */
/*        return;
    }
    (*env)->ThrowNew(env, newExcCls, message);
}*/

