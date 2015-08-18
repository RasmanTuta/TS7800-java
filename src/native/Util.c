#include <unistd.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <stdio.h>
#include <fcntl.h>
#include <assert.h>

#include "ts_control.h"
#include "no_rasmantuta_ts7800_jni_Util.h"


/*
 * Class:     no_rasmantuta_ts7800_jni_Util
 * Method:    tsSetRedLEDOn
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_no_rasmantuta_ts7800_jni_Util_tsSetRedLEDOn  (JNIEnv *env, jclass clazz, jboolean on){
	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);

	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	if(on == JNI_TRUE){
		twi_write(LED, data, control, status, &success);
	}else{
		twi_write(0, data, control, status, &success);
	}
	*control = STOP|TWSIEN; // Send stop signal
}

/*
 * Class:     no_rasmantuta_ts7800_jni_Util
 * Method:    tsReadOdometer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_no_rasmantuta_ts7800_jni_Util_tsReadOdometer  (JNIEnv *env, jclass clazz){
	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);

	twi_select(AVR_ADDR, WRITE,data , control, status, &success);

	twi_write(OTP_R, data, control, status, &success);
	twi_write(134, data, control, status, &success);
	twi_select(AVR_ADDR, READ, data, control, status, &success);

	int odom = (twi_read(data, control, status, &success) << 24);
	odom |= (twi_read(data, control, status, &success) << 16);
	odom |= (twi_read(data, control, status, &success) << 8);
	odom |= twi_read(data, control, status, &success);
	usleep(1);
	*control = STOP|TWSIEN; 	//Send stop signal
	//
	//if(verbose) fprintf(stderr,"SENT Stop signal\n");
	odom = 0xFFFFFF - (odom & 0xFFFFFF);
	//printf("TS-7800 has been running for %u hours\n", odom);
	//	}
	return odom;
}

/*
 * Class:     no_rasmantuta_ts7800_jni_Util
 * Method:    tsReadBirthDate
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_no_rasmantuta_ts7800_jni_Util_tsReadBirthDate  (JNIEnv *env, jclass clazz){
	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);
	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	//
	twi_write(OTP_R, data, control, status, &success);
	twi_write(145, data, control, status, &success);
	twi_select(AVR_ADDR, READ, data, control, status, &success);
	//
	int bday = (twi_read(data, control, status, &success) << 24);
	bday |= (twi_read(data, control, status, &success) << 16);
	bday |= (twi_read(data, control, status, &success) << 8);
	bday |= twi_read(data, control, status, &success);
	usleep(1);
	*control = STOP|TWSIEN; 	//Send stop signal
	//
	//if(verbose) fprintf(stderr,"SENT Stop signal\n");
	//printf("TS-7800 was born on %02d/%02d/%04d\n", (bday>>24), (bday>>16)&0xFF, bday&0xFFFF);
	return bday;
}

/*
 * Class:     no_rasmantuta_ts7800_jni_Util
 * Method:    tsReadMACAddress
 * Signature: ()[S
 */
JNIEXPORT jshortArray JNICALL Java_no_rasmantuta_ts7800_jni_Util_tsReadMACAddress  (JNIEnv *env, jclass clazz){
	unsigned int *data, *control, *status;
	BOOL success = FALSE;
	twi_init(&data, &control, &status, &success);
	unsigned short mac[6];
	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	twi_write(OTP_R, data, control, status, &success);
	twi_write(0, data, control, status, &success);
	twi_select(AVR_ADDR, READ, data, control, status, &success);
	mac[5] = twi_read(data, control, status, &success);
	mac[4] = twi_read(data, control, status, &success);
	mac[3] = twi_read(data, control, status, &success);
	mac[2] = twi_read(data, control, status, &success);
	usleep(1);
	*control = STOP|TWSIEN; 	//Send stop signal
	//if(verbose) fprintf(stderr,"SENT Stop signal\n");
	//
	twi_select(AVR_ADDR, WRITE, data, control, status, &success);
	twi_write(OTP_R, data, control, status, &success);
	twi_write(4, data, control, status, &success);
	twi_select(AVR_ADDR, READ, data, control, status, &success);
	mac[1] = twi_read(data, control, status, &success);
	mac[0] = twi_read(data, control, status, &success);
	twi_read(data, control, status, &success); twi_read(data, control, status, &success);	//We don't care about the other two bytes
	usleep(1);
	*control = STOP|TWSIEN; 	//Send stop signal
	//if(verbose) fprintf(stderr,"SENT Stop signal\n");
	//
	//printf("HWaddr %02x:%02x:%02x:%02x:%02x:%02x\n", mac[5],mac[4],mac[3],mac[2],mac[1],mac[0]);
    jshortArray js;
    js=(*env)->NewShortArray(env, 6);
    (*env)->SetShortArrayRegion(env, js, 0, 6, mac);

    return js;

}
