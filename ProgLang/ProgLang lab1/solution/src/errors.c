#include "bmp.h"
#include "errors.h"
#include <errno.h>
#include <stdint.h>
#include <stdio.h>


int map_read_status_to_errno(enum read_status st) {
    switch (st) {
        case READ_OK:                 return 0;
        case READ_INVALID_HEADER:     return EINVAL;
        case READ_INVALID_SIGNATURE:  return EBADMSG;
        case READ_INVALID_BITS:       return ENOTSUP;
        default:                      return EFAULT;
    }
}

int map_write_status_to_errno(enum write_status st) {
    switch (st) {
        case WRITE_OK:    return 0;
        case WRITE_ERROR: return EIO;
        default:          return 0;
    }
}
