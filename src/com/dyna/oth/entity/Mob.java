package com.dyna.oth.entity;

import java.util.ArrayList;
import java.util.List;

public class Mob extends Entity {
    protected int walkDist = 0;
    protected int dir = 0;

    public Mob() {
        x = y = 8;
        xr = 4;
        yr = 3;
    }

    public boolean move(int xa, int ya) {
        if (xa != 0 || ya != 0) {
            walkDist++;
            if (xa < 0) dir = 2;
            if (xa > 0) dir = 3;
            if (ya < 0) dir = 1;
            if (ya > 0) dir = 0;
            boolean stopped = true;
            if (xa != 0 && move2(xa, 0)) stopped = false;
            if (ya != 0 && move2(0, ya)) stopped = false;
            return !stopped;
        }
        return true;
    }

    private List<List<Entity>> hitResults = new ArrayList<List<Entity>>();
    private boolean move2(int xa, int ya) {
        if (xa != 0 && ya != 0) throw new IllegalArgumentException("Move2 can't move on multiple axes!");
        for (int c = 0; c < 4; c++) {
            int xt = ((x + xa) + (c % 2 * 2 - 1) * xr) >> 4;
            int yt = ((y + ya) + (c / 2 * 2 - 1) * yr) >> 4;
            if (!level.getTile(xt, yt).mayPass(level, xt, yt, this)) {
                return false;
            }
        }

        List<Entity> isInside;
        List<Entity> wasInside;
        if (hitResults.size() > 0) {
            isInside = hitResults.remove(hitResults.size() - 1);
        } else {
            isInside = new ArrayList<Entity>();
        }
        if (hitResults.size() > 0) {
            wasInside = hitResults.remove(hitResults.size() - 1);
        } else {
            wasInside = new ArrayList<Entity>();
        }
        level.getEntities(wasInside, x - xr, y - yr, x + xr, y + yr);
        level.getEntities(isInside, x + xa - xr, y + ya - yr, x + xa + xr, y + ya + yr);
        isInside.removeAll(wasInside);
        for (int i = 0; i < isInside.size(); i++) {
            Entity e = isInside.get(i);
            if (e == this) continue;

            if (e.tiles(this)) {
                return false;
            }
        }
        isInside.clear();
        wasInside.clear();
        hitResults.add(wasInside);
        hitResults.add(isInside);

        x += xa;
        y += ya;
        return true;
    }

    public boolean tiles(Mob mob) {
        return true;
    }
}
