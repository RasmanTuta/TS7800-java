#include <sys/mman.h>
#include <fcntl.h>
#include <stdio.h>
#include <assert.h>

#include "twsi.h"

void twi_write(unsigned char dat, unsigned int *data, unsigned int *control, unsigned int *status, BOOL *success) {
	*data = dat;
	usleep(100);

	*control = (*control & ~IFLG) | ACK;
	usleep(100);
	while((*control & IFLG) == 0x0);        // Wait for an I2C event


	if((*status != ST_DATA_ACK) && (*status != MT_DATA_ACK )) {
//		if(verbose) fprintf(stderr,"Slave didn't ACK data\n");
//		exit(1);
		*success = FALSE;
		return;
	}

//	if(verbose) fprintf(stderr,"Slave ACKed data\n");
	*success = TRUE;
}
	
unsigned char twi_read(unsigned int *data, unsigned int *control, unsigned int *status, BOOL *success) {
//	if(verbose) fprintf(stderr, "Waiting for data from master\n");
	*control = (*control & ~IFLG) | ACK;
	while((*control & IFLG) == 0x0);	// Wait for an I2C event 
	if((*status != SR_DATA_ACK) && (*status != MR_DATA_ACK)) {
//		if(verbose) fprintf(stderr, "Error reading data from master(0x%x)\n", *status);
//		exit(1);
		*success = FALSE;
		return 0;
	}

	*success = TRUE;
	return (unsigned char)*data;
}

void twi_select(unsigned char addr, unsigned char dir, unsigned int *data, unsigned int *control, unsigned int *status, BOOL *success) {
	unsigned int timeout = 0;
//	if(verbose) fprintf(stderr,"Attempting to send start signal\n");
	*control = START|TWSIEN;        // Send start signal
	usleep(1);
	while((*control & IFLG) == 0)
		if(timeout++ > 10000) {
//			if(verbose) fprintf(stderr, "Timedout\n");
			*control = STOP|TWSIEN; // Send stop signal
//			if(verbose) fprintf(stderr,"Sent Stop signal\n");
//			exit(-1);
			*success = FALSE;
			return;
		}
	if((*status != MT_START) && (*status != MT_REP_START)) {
//		if(verbose) fprintf(stderr,"Couldn't send start signal(0x%x)\n",
//		  *status);
		*control = STOP|TWSIEN; // Send stop signal
//		if(verbose) fprintf(stderr,"Sent Stop signal\n");
//		exit(-1);
		*success = FALSE;
		return;
	}

//	if(verbose) fprintf(stderr,"Sent start signal succesfully\n"
//	  "Attempting to select slave\n");
	*data = addr | dir;     // Send SLA+W/R
	usleep(1);

	*control = (*control & ~IFLG) | ACK;
	usleep(1);

	while((*control & IFLG) == 0) ;

	if((*status != MT_SLA_ACK) && (*status != MR_SLA_ACK)) {
//		if(verbose) fprintf(stderr,"Slave didn't ACK select signal(0x%x)\n", *status);
		*control = STOP|TWSIEN; // Send stop signal
//		if(verbose) fprintf(stderr,"Sent Stop signal\n");
//		exit(-1);
		*success = FALSE;
		return;
		
	}
//	if(verbose) fprintf(stderr,"Succesfully selected slave\n");
	*success = TRUE;
}

void twi_init(unsigned int **data, unsigned int **control, unsigned int **status, BOOL *success) {
	int devmem;
	unsigned int *twi_regs;
	devmem = open("/dev/mem", O_RDWR|O_SYNC);
	assert(devmem != -1);
	twi_regs = (unsigned int *)mmap(0, getpagesize(), PROT_READ|PROT_WRITE,
	  MAP_SHARED, devmem, 0xF1011000);

	*control = &twi_regs[CONTROL];
	*data = &twi_regs[DATA];
	*status = &twi_regs[STATUS];
	
	*success = TRUE;
}
