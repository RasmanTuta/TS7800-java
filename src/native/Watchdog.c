#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <assert.h>
#include <string.h>

#include "ts_control.h"
#include "no_rasmantuta_ts7800_jni_Watchdog.h"

/*
 * Class:     no_rasmantuta_ts7800_jni_Watchdog
 * Method:    tsFeed
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_no_rasmantuta_ts7800_jni_Watchdog_tsFeed  (JNIEnv *env, jclass clazz){
	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);
	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	twi_write(WDT_8s, data, control, status, &success);
	*control = STOP|TWSIEN; // Send stop signal
//	if(verbose) fprintf(stderr,"Sent Stop signal\n");
}

/*
 * Class:     no_rasmantuta_ts7800_jni_Watchdog
 * Method:    tsDisable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_no_rasmantuta_ts7800_jni_Watchdog_tsDisable  (JNIEnv *env, jclass clazz){
	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);
	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	twi_write(WDT_OFF, data, control, status, &success);
	*control = STOP|TWSIEN; // Send stop signal
//	if(verbose) fprintf(stderr,"Sent Stop signal\n");
}
