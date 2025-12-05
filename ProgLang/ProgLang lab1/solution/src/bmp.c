#include "bmp.h"
#include <stdlib.h>
#include <string.h>


static size_t row_padding(uint64_t width) {
    return (4 - (width * 3) % 4) % 4;
}


enum read_status from_bmp(FILE *in, struct image *img) {
    if (!in || !img) return READ_INVALID_HEADER;

    struct bmp_file_header file_header;
    struct bmp_info_header info_header;
    // Считаем оба заголовка
    if (fread(&file_header, sizeof(file_header), 1, in) != 1) return READ_INVALID_HEADER;
    if (fread(&info_header, sizeof(info_header), 1, in) != 1) return READ_INVALID_HEADER;

    if (file_header.bf_type != 0x4D42) return READ_INVALID_SIGNATURE;
    if (info_header.bi_bit_count != 24 || info_header.bi_compression != 0) return READ_INVALID_BITS;

    img->width = info_header.bi_width;
    img->height = (info_header.bi_height > 0) ? info_header.bi_height : -info_header.bi_height;
    img->is_bottom_up = info_header.bi_height > 0;  // ← СОХРАНЯЕМ ФОРМАТ

    uint64_t pixels = img->width * img->height;
    img->data = malloc(sizeof(struct pixel) * pixels);
    if (!img->data) return READ_INVALID_HEADER;

    if (fseek(in, file_header.bf_off_bits, SEEK_SET) != 0) {
        free(img->data); img->data = NULL; return READ_INVALID_HEADER;
    }

    size_t padding = row_padding(img->width);
    for (uint64_t y = 0; y < img->height; y++) {
        uint64_t dst_y = img->is_bottom_up ? (img->height - 1 - y) : y;
        if (fread(img->data + dst_y * img->width, sizeof(struct pixel), img->width, in) != img->width) {
            free(img->data); img->data = NULL; return READ_INVALID_HEADER;
        }
        if (fseek(in, (long)padding, SEEK_CUR) != 0) {
            free(img->data); img->data = NULL; return READ_INVALID_HEADER;
        }
    }

    return READ_OK;
}


enum write_status to_bmp(FILE *out, const struct image *img) {
    if (!out || !img || !img->data) return WRITE_ERROR;

    size_t padding = row_padding(img->width);
    uint64_t image_size = (img->width * 3 + padding) * img->height;
    uint64_t file_size = 14 + 40 + image_size; // 14 - BITMAPFILEHEADER, 40 - BITMAPINFOHEADER

    struct bmp_file_header file_header = {
        .bf_type = 0x4D42,
        .bf_size = (uint32_t)file_size,
        .bf_reserved1 = 0,
        .bf_reserved2 = 0,
        .bf_off_bits = 14 + 40
    };

    struct bmp_info_header info_header = {
        .bi_size = 40,
        .bi_width = (int32_t)img->width,
        .bi_height = (int32_t)img->height,
        .bi_planes = 1,
        .bi_bit_count = 24,
        .bi_compression = 0,
        .bi_size_image = (uint32_t)image_size,
        .bi_x_pels_per_meter = 0,
        .bi_y_pels_per_meter = 0,
        .bi_clr_used = 0,
        .bi_clr_important = 0
    };

    // Пишем оба заголовка
    if (fwrite(&file_header, sizeof(file_header), 1, out) != 1) return WRITE_ERROR;
    if (fwrite(&info_header, sizeof(info_header), 1, out) != 1) return WRITE_ERROR;

    uint8_t pad[3] = {0, 0, 0};
    for (uint64_t y = 0; y < img->height; y++) {
        if (fwrite(img->data + y * img->width, sizeof(struct pixel), img->width, out) != img->width) return WRITE_ERROR;
        if (padding && fwrite(pad, 1, padding, out) != padding) return WRITE_ERROR;
    }

    return WRITE_OK;
}


void image_free(struct image *img) {
    free(img->data);
    img->data = NULL;
}
