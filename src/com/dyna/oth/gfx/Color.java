package com.dyna.oth.gfx;

public class Color {
    public static int get(int black, int darkGray, int lightGray, int white) {
        return (get(white) << 24) + (get(lightGray) << 16) + (get(darkGray) << 8) + (get(black));
    }

    public static int get(int d) {
        if (d < 0) return 255;

        int r = d / 100 % 10;
        int g = d / 10 % 10;
        int b = d % 10;
        return r * 36 + g * 6 + b;
    }
}
