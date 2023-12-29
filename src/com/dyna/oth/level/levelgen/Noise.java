package com.dyna.oth.level.levelgen;

public class Noise {
    public double[] values;
    private int w, h;

    /*
        A B A

        B C B

        A B A
     */

    public Noise(int w, int h) {
        this.w = w;
        this.h = h;

        values = new double[w * h];

        int stepSize = w;
        for (int y = 0; y < w; y += stepSize) {
            for (int x = 0; x < w; x += stepSize) {

            }
        }
    }

    private double sample(int x, int y) {
        return values[(x&(w-1))+(y&(h-1))*w];
    }
}
