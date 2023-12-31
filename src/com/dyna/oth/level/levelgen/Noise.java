package com.dyna.oth.level.levelgen;

import com.dyna.oth.level.tile.Tile;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Noise {
    private static final Random random = new Random();
    public double[] values;
    private int w, h;

    public Noise(int w, int h, int featureSize) {
        this.w = w;
        this.h = h;

        values = new double[w * h];

        for (int y = 0; y < w; y += featureSize) {
            for (int x = 0; x < w; x += featureSize) {
                setSample(x, y, random.nextFloat() * 2 - 1);
            }
        }

        int stepSize = featureSize;
        double scale = 1.0 / w;
        double scaleModifier = 1;
        do {
            int halfStep = stepSize / 2;
            for (int y = 0; y < w; y += stepSize) {
                for (int x = 0; x < w; x += stepSize) {
                    double a = (sample(x, y));
                    double b = (sample(x + stepSize, y));
                    double c = (sample(x, y + stepSize));
                    double d = (sample(x + stepSize, y + stepSize));

                    double e = (a + b + c + d) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale;
                    setSample(x + halfStep, y + halfStep, e);
                }
            }
            for (int y = 0; y < w; y += stepSize) {
                for (int x = 0; x < w; x += stepSize) {
                    double a = (sample(x, y));
                    double b = (sample(x + stepSize, y));
                    double c = (sample(x, y + stepSize));
                    double d = (sample(x + halfStep, y + halfStep));
                    double e = (sample(x + halfStep, y - halfStep));
                    double f = (sample(x - halfStep, y + halfStep));

                    double H = (a + b + d + e) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
                    double g = (a + c + d + f) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
                    setSample(x + halfStep, y, H);
                    setSample(x, y + halfStep, g);
                }
            }
            stepSize /= 2;
            scale *= (scaleModifier + 1);
            scaleModifier *= 0.3;
        } while (stepSize > 1);
    }

    private double sample(int x, int y) {
        return values[(x & (w - 1)) + (y & (h - 1)) * w];
    }

    private void setSample(int x, int y, double value) {
        values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
    }

    public static byte[] getMap(int w, int h) {
        Noise noise1 = new Noise(w, h, w / 4);
        Noise noise2 = new Noise(w, h, w / 4);

        byte[] map = new byte[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = x + y * w;

                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;

                double xd = x / (w - 1.0) * 2 - 1;
                double yd = y / (h - 1.0) * 2 - 1;
                if (xd < 0) xd = -xd;
                if (yd < 0) yd = -yd;
                double dist = xd >= yd ? xd : yd;
                dist = dist * dist * dist * dist;
                dist = dist * dist * dist * dist;
                val = val + 1 - dist * 20;

                if (val < 0) {
                    map[i] = Tile.deepsand.id;
                } else if (val > 1) {
                    map[i] = Tile.rock.id;
                } else {
                    map[i] = Tile.sand.id;
                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        for (int j = 0; j < 10; j++) {
            int w = 128;
            int h = 128;

            byte[] map = Noise.getMap(w, h);

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = x + y * w;

                    if (map[i] == Tile.deepsand.id) pixels[i] = 0xeb9800;
                    if (map[i] == Tile.sand.id) pixels[i] = 0xf1c232;
                    if (map[i] == Tile.rock.id) pixels[i] = 0x726d72;
                }
            }
            img.setRGB(0, 0, w, h, pixels, 0, w);
            JOptionPane.showMessageDialog(null, null, "", JOptionPane.YES_NO_OPTION, new ImageIcon(img));
        }
    }
}
