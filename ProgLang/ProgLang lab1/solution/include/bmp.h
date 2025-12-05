#ifndef BMP_H
#define BMP_H

#include <stdint.h>
#include <stdio.h>

// Строго 3 байта: b, g, r
#pragma pack(push, 1)
struct pixel {
    uint8_t b, g, r;
};

// BITMAPFILEHEADER
struct bmp_file_header {
    uint16_t bf_type;
    uint32_t bf_size;
    uint16_t bf_reserved1;
    uint16_t bf_reserved2;
    uint32_t bf_off_bits;
};

// BITMAPINFOHEADER
struct bmp_info_header {
    uint32_t bi_size;
    int32_t  bi_width;
    int32_t  bi_height;
    uint16_t bi_planes;
    uint16_t bi_bit_count;
    uint32_t bi_compression;
    uint32_t bi_size_image;
    int32_t  bi_x_pels_per_meter;
    int32_t  bi_y_pels_per_meter;
    uint32_t bi_clr_used;
    uint32_t bi_clr_important;
};
#pragma pack(pop)

struct image {
    uint64_t width, height;
    struct pixel *data;
    int is_bottom_up;
};

enum read_status  { READ_OK = 0, READ_INVALID_SIGNATURE, READ_INVALID_HEADER, READ_INVALID_BITS, READ_IO_ERROR };
enum write_status { WRITE_OK = 0, WRITE_ERROR };


enum read_status from_bmp(FILE* in, struct image* img);
enum write_status to_bmp(FILE* out, const struct image* img);

void image_free(struct image *img);

#endif
