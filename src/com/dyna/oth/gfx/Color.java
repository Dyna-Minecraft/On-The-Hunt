package com.dyna.oth.gfx;

public class Color {
    public static int get(int red, int green, int blue, int alpha) {
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }

    public static int get(int d) {
        if (d < 0) return 255;

        int r = d / 100 % 10;
        int g = d / 10 % 10;
        int b = d % 10;
        return r * 36 + g * 6 + b;
    }
}
