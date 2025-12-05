#include "bmp.h"
#include "errors.h"
#include "transform.h"
#include <errno.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>



static const struct transform_entry transformations[] = {
    { "none",  image_do_nothing },
    { "fliph", image_flip_horizontal },
    { "flipv", image_flip_vertical },
    { "cw90",  image_rotate_cw90 },
    { "ccw90", image_rotate_ccw90 }
};
#define NUM_TRANSFORMATIONS (sizeof(transformations) / sizeof(transformations[0]))


int main(int argc, char *argv[]) {
    if (argc != 4) {
        fprintf(stderr, "Usage: %s <source-image> <transformed-image> <transformation>\n", argv[0]);
        fprintf(stderr, "Available transformations: none, fliph, flipv, cw90, ccw90\n");
        return EINVAL; // некорректный аргумент (22)
    }

    FILE *in = fopen(argv[1], "rb");
    if (!in) {
        fprintf(stderr, "Error: cannot open file %s\n", argv[1]);
        return ENOENT; // файл не найден (2)
    }

    struct image img_in = {0};
    enum read_status r_status = from_bmp(in, &img_in);
    if (fclose(in) != 0)
        fprintf(stderr, "Warning: could not close input file %s\n", argv[1]);

    if (r_status != READ_OK) {
        fprintf(stderr, "Error: failed to read BMP file (%d)\n", r_status);
        return map_read_status_to_errno(r_status);
    }
    if (img_in.data == NULL) {
        fprintf(stderr, "Error: failed to allocate memory for image\n");
        return ENOMEM; // недостаточно памяти (12)
    }

    struct image img_out = {0};
    const char *cmd = argv[3];
    int found = 0;
    for (size_t i = 0; i < NUM_TRANSFORMATIONS; ++i) {
        if (strcmp(cmd, transformations[i].name) == 0) {
            transformations[i].func(&img_in, &img_out);
            found = 1;
            break;
        }
    }
    if (!found) {
        fprintf(stderr, "Unknown transformation: %s\n", cmd);
        image_free(&img_in);
        return EINVAL;
    }
    if (img_out.data == NULL) {
        fprintf(stderr, "Error: failed to allocate memory for output image\n");
        image_free(&img_in);
        return ENOMEM;
    }

    FILE *out = fopen(argv[2], "wb");
    if (!out) {
        fprintf(stderr, "Error: cannot open file %s for writing\n", argv[2]);
        image_free(&img_in);
        image_free(&img_out);
        return EACCES; // нет прав (13)
    }

    enum write_status w_status = to_bmp(out, &img_out);
    if (fclose(out) != 0)
        fprintf(stderr, "Warning: could not close output file %s\n", argv[2]);
    image_free(&img_in);
    image_free(&img_out);

    if (w_status != WRITE_OK) {
        fprintf(stderr, "Error: failed to write BMP file (%d)\n", w_status);
        return map_write_status_to_errno(w_status);
    }

    printf("Transformation '%s' applied, result saved to %s\n", cmd, argv[2]);
    return 0;
}
