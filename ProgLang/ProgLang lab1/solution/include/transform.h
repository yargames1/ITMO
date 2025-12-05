#ifndef TRANSFORM_H
#define TRANSFORM_H

#include "bmp.h"

// Тип функции трансформации
typedef void (*transform_func)(const struct image*, struct image*);

// one record: имя команды и сама функция
struct transform_entry {
    const char *name;
    transform_func func;
};

void image_do_nothing(const struct image *src, struct image *dst);
void image_flip_horizontal(const struct image *src, struct image *dst);
void image_flip_vertical(const struct image *src, struct image *dst);
void image_rotate_cw90(const struct image *src, struct image *dst);
void image_rotate_ccw90(const struct image *src, struct image *dst);

#endif // TRANSFORM_H
