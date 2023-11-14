package com.dyna.oth.gfx;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private List<Sprite> sprites = new ArrayList<Sprite>();

    private static final int MAP_WIDTH = 64; // Must be 2^x
    private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

    private int[] tiles = new int[MAP_WIDTH * MAP_WIDTH * 2];
    private int[] colors = new int[MAP_WIDTH * MAP_WIDTH * 4];
    private int[] databits = new int[MAP_WIDTH * MAP_WIDTH];
    private int xScroll;
    private int yScroll;

    public final int w, h;

    private SpriteSheet sheet;

    public Screen(int w, int h, SpriteSheet sheet) {
        this.sheet = sheet;
        this.w = w;
        this.h = w;

        colors[0] = 0xff00ff;
        colors[1] = 0xff00ff;
        colors[2] = 0xff00ff;
        colors[3] = 0xff00ff;
    }

    public void render(int[] pixels, int offs, int row) {
        for (int yt = yScroll >> 3; yt <= (yScroll + w) >> 3; yt++) {
            int y0 = yt * 16 - yScroll;
            int y1 = yt - y0 + 16;
            if (y0 < 0) y0 = 0;
            if (y1 > h) y1 = h;
            for (int xt = xScroll >> 3; xt <= (xScroll + h) >> 3; xt++) {
                  int x0 = xt * 16 - xScroll;
                  int x1 = xt - x0 + 16;
                  if (x0 < 0) x0 = 0;
                  if (x1 > h) x1 = h;

                int tileIndex = (xt & (MAP_WIDTH_MASK)) + (yt & (MAP_WIDTH_MASK)) * MAP_WIDTH;
                for (int y = 0; y0 < y1; y++) {
                    int sp = ((y - yScroll) & 7) * sheet.width + ((x0 - xScroll) & 7);
                    int tp = offs+x0+y*row;
                    for (int x = x0; x < x1; x++) {
                        int col = tileIndex * 4 + sheet.pixels[sp++];
                        pixels[tp++] = xt * 1999 + yt * 1999
                                ;
                        // pixels[tp++] = colors[col];
                    }
                }
            }
        }
    }
}
