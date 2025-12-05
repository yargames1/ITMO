#ifndef ERRORS_H
#define ERRORS_H

#include "bmp.h"

int map_read_status_to_errno(enum read_status st);
int map_write_status_to_errno(enum write_status st);

#endif // ERRORS_H
