#include <unistd.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <stdio.h>
#include <fcntl.h>
#include <assert.h>

#include "ts_control.h"
#include "no_rasmantuta_ts7800_jni_ReadAnalog.h"

volatile unsigned short* runAdc ( int channels, int *handle);

/*
 * Class:     no_rasmantuta_ts7800_jni_ReadAnalog
 * Method:    tsReadAndAverage
 * Signature: (I)[S
 */
JNIEXPORT jshortArray JNICALL Java_no_rasmantuta_ts7800_jni_ReadAnalog_tsReadAndAverage
  (JNIEnv *env, jclass clazz, jint channels){
	unsigned short values[] = {0xffff, 0xffff, 0xffff, 0xffff, 0xffff, 0xffff};
	unsigned int ivalues[] = {0, 0, 0, 0, 0, 0};
	unsigned int numcounts[] = {0, 0, 0, 0, 0, 0};
	int handle = 0;
	volatile unsigned short *vals = runAdc(channels, &handle);

	int i;
	for(i=0; i<2048; i++){
		unsigned short val = (unsigned short)vals[i];
		if(val == 0xffff)
			continue;
		int channel = (val & 0xf000) >> 12;
		int rawval = val & 0x0fff;
		int index = -1;
		if(channel < 4 && channel >= 0){
			index = channel;
		}else if(channel == 6 || channel == 7){
			index = channel - 2;
		}else{
			continue;
		}

		ivalues[index] += rawval;
		numcounts[index]++;
	}

	for(i=0; i<6; i++){
		if(numcounts[i] > 0){
			values[i] = (unsigned short)(ivalues[i]/numcounts[i]);
		}
	}

    jshortArray js;
    js=(*env)->NewShortArray(env, 6);
    (*env)->SetShortArrayRegion(env, js, 0, 6, values);

    close(handle);
    return js;
}

/*
 * Class:     no_rasmantuta_ts7800_jni_ReadAnalog
 * Method:    tsRawRead
 * Signature: (I)[S
 */
JNIEXPORT jshortArray JNICALL Java_no_rasmantuta_ts7800_jni_ReadAnalog_tsRawRead
(JNIEnv *env, jclass clazz, jint channels){
	int handle = 0;
	volatile unsigned short *vals = runAdc(channels, &handle);
    jshortArray js;
    js=(*env)->NewShortArray(env, 2048);
    (*env)->SetShortArrayRegion(env, js, 0,
	2048, (unsigned short *)vals);

    close(handle);
    return js;
}




volatile unsigned short* runAdc ( int channels, int *handle){

	volatile unsigned int *fpga;
	volatile unsigned short *sram;
	unsigned int i;
	int chans = channels;
	*handle = open("/dev/mem", O_RDWR|O_SYNC);

	fpga = (unsigned int *)mmap(0, 0x1004, PROT_READ|PROT_WRITE,
	  MAP_SHARED, *handle, 0xE8100000);
    sram = (unsigned short *)&fpga[SRAM];

	for(i=0;i<2048;i++) sram[i] = 0xFFFF;

	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);

	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	twi_write(chans | 0x40, data, control, status, &success);
	*control = STOP|TWSIEN; // Send stop signal


//EWD: Implement this in a more elegant way. That is write out all FF's then print values as they are available.
//2KSPS => a new sample is available every 500uS
	usleep(1100000);

//	for(i=0;i<2048;i++) {
//		printf("0x%x\n", sram[i]);
//	}
	return sram;
}
