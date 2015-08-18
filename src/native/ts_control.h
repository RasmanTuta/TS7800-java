#ifndef _INCLUDED_TS_CONTROL
#define _INCLUDED_TS_CONTROL
#include "twsi.h"

void twi_write(unsigned char dat, unsigned int *data, unsigned int *control, unsigned int *status, BOOL *success);
unsigned char twi_read(unsigned int *data, unsigned int *control, unsigned int *status, BOOL *success);
void twi_select(unsigned char addr, unsigned char dir, unsigned int *data, unsigned int *control, unsigned int *status, BOOL *success);
void twi_init(unsigned int **data, unsigned int **control, unsigned int **status, BOOL *success);

#endif
