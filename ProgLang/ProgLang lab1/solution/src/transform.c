#include "bmp.h"
#include "transform.h"
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>



// Вспомогательная функция для всех трансформаций:
// Проверяет аргументы, освобождает старую память, выделяет dst->data.
// Возвращает 0, если всё нормально, иначе 1.
static int prepare_output_image(const struct image *src, struct image *dst, uint64_t out_width, uint64_t out_height) {
    if (!src || !dst) {
        fprintf(stderr, "transform: NULL pointer passed as image\n");
        return 1;
    }
    if (src->width == 0 || src->height == 0 || !src->data) {
        fprintf(stderr, "transform: source image is empty or data is NULL\n");
        return 1;
    }
    if (dst->data) {
        free(dst->data);
        dst->data = NULL;
    }
    dst->width = out_width;
    dst->height = out_height;
    dst->is_bottom_up = 1;
    size_t pixels = out_width * out_height;
    dst->data = malloc(sizeof(struct pixel) * pixels);
    if (!dst->data) {
        fprintf(stderr, "transform: failed to allocate memory for destination image\n");
        return 1;
    }
    return 0;
}

// Сами трансформации непосредственно
void image_do_nothing(const struct image *src, struct image *dst) {
    if (prepare_output_image(src, dst, src->width, src->height)) return;
    memcpy(dst->data, src->data, sizeof(struct pixel) * src->width * src->height);
}


void image_flip_horizontal(const struct image *src, struct image *dst) {
    if (prepare_output_image(src, dst, src->width, src->height)) return;
    for (uint64_t y = 0; y < src->height; ++y) {
        for (uint64_t x = 0; x < src->width; ++x) {
            dst->data[y * src->width + x] = src->data[y * src->width + (src->width - 1 - x)];
        }
    }
}


void image_flip_vertical(const struct image *src, struct image *dst) {
    if (prepare_output_image(src, dst, src->width, src->height)) return;
    for (uint64_t y = 0; y < src->height; ++y) {
        for (uint64_t x = 0; x < src->width; ++x) {
            dst->data[y * src->width + x] = src->data[(src->height - 1 - y) * src->width + x];
        }
    }
}


void image_rotate_cw90(const struct image *src, struct image *dst) {
    if (prepare_output_image(src, dst, src->height, src->width)) return;
    // После ротации на 90° CW: новое изображение имеет width=height, height=width
    // Точка (x, y) из src идёт в (src->height - 1 - y, x) в dst
    for (uint64_t y = 0; y < src->height; ++y) {
        for (uint64_t x = 0; x < src->width; ++x) {
            uint64_t new_x = src->height - 1 - y;
            uint64_t new_y = x;
            dst->data[new_y * dst->width + new_x] = src->data[y * src->width + x];
        }
    }
}


void image_rotate_ccw90(const struct image *src, struct image *dst) {
    if (prepare_output_image(src, dst, src->height, src->width)) return;
    // После ротации на 90° CCW: новое изображение имеет width=height, height=width
    // Точка (x, y) из src идёт в (y, src->width - 1 - x) в dst
    for (uint64_t y = 0; y < src->height; ++y) {
        for (uint64_t x = 0; x < src->width; ++x) {
            uint64_t new_x = y;
            uint64_t new_y = src->width - 1 - x;
            dst->data[new_y * dst->width + new_x] = src->data[y * src->width + x];
        }
    }
}
