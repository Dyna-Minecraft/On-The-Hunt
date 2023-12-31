package com.dyna.oth.entity;

import com.dyna.oth.InputHandler;
import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.Level;

import java.util.List;

public class Player extends Mob {
    private InputHandler input;
    private boolean wasAttacking;
    private int attackTime, attackDir;

    public Player(InputHandler input) {
        this.input = input;
        x = 24;
        y = 24;
    }

    public void tick() {
        super.tick();
        int xa = 0;
        int ya = 0;
        if (input.up) ya--;
        if (input.down) ya++;
        if (input.left) xa--;
        if (input.right) xa++;

        move(xa, ya);

        if (input.attack) {
            if (!wasAttacking) attack();
            wasAttacking = true;
        } else {
            wasAttacking = false;
        }
        if (attackTime > 0) attackTime--;
    }

    private void attack() {
        walkDist += 8;
        attackDir = dir;
        attackTime = 5;
        if (dir == 0) hurt(level.getEntities(x - 8, y + 4, x + 8, y + 12));
        if (dir == 1) hurt(level.getEntities(x - 8, y - 12, x + 8, y - 4));
        if (dir == 3) hurt(level.getEntities(x + 4, y - 8, x + 12, y + 8));
        if (dir == 2) hurt(level.getEntities(x - 12, y - 8, x - 4, y + 8));
    }

    private void hurt(List<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e != this) e.hurt(this, random.nextInt(2) + 1, attackDir);
        }
    }

    public void render(Screen screen) {
        int xt = 0;
        int yt = 14;

        int flip1 = (walkDist >> 3) & 1;
        int flip2 = (walkDist >> 3) & 1;

        if (dir == 1) {
            xt += 2;
        }
        if (dir > 1) {
            flip1 = 0;
            flip2 = ((walkDist >> 5) & 1);
            if (dir == 2) {
                flip1 = 1;
            }
            xt += 4 + ((walkDist >> 3) & 1) * 2;
        }

        int xo = x - 8;
        int yo = y - 11;

        if (attackTime > 0 && attackDir == 1) {
            screen.render(xo + 0, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
            screen.render(xo + 8, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
        }
        screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, Color.get(-1, 100, 411, 542), flip1); // top left
        screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, Color.get(-1, 100, 411, 542), flip1); // top right
        screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, Color.get(-1, 100, 411, 542), flip2); // bottom left
        screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, Color.get(-1, 100, 411, 542), flip2); // bottom right
        if (attackTime > 0 && attackDir == 2) {
            screen.render(xo - 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
            screen.render(xo - 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
        }
        if (attackTime > 0 && attackDir == 3) {
            screen.render(xo + 8 + 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
            screen.render(xo + 8 + 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
        }
        if (attackTime > 0 && attackDir == 0) {
            screen.render(xo + 0, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
            screen.render(xo + 8, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
        }
    }

    public void findStartPos(Level level) {
        while (true) {
            int x = random.nextInt(level.w);
            int y = random.nextInt(level.h);
            if (level.getTile(x, y).mayPass(level, x, y, this)) {
                this.x = x * 16 + 8;
                this.y = y * 16 + 8;
                return;
            }
        }
    }
}
