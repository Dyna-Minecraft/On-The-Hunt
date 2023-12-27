package com.dyna.oth.entity;

public class Player extends Mob {
    public void move(int xa, int ya) {
        if (xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            return;
        }

        int r = 6;
        boolean mayPass = true;
        for (int c = 0; c < 4 && mayPass; c++) {
            int xt = ((x + xa) + (c % 2 * 2 - 1) * r) >> 4;
            int yt = ((y + ya) + (c / 2 * 2 - 1) * r) >> 4;
            if (!level.getTile(xt, yt).mayPass(level, xt, yt, this)) {
                mayPass = false;
            }
        }

        if (mayPass) {
            x += xa;
            y += ya;
        }
    }
}
