package com.dyna.oth.entity.particle;

import com.dyna.oth.entity.Entity;
import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Font;
import com.dyna.oth.gfx.Screen;

public class TextParticle extends Entity {
    private String msg;
    private int col;
    private int time = 0;
    public double xa, ya, za;
    public double xx, yy, zz;

    public TextParticle(String msg, int x, int y, int col) {
        this.msg = msg;
        this.x = x;
        this.y = y;
        this.col = col;
        xx = x;
        yy = y;
        zz = 8;
        xa = random.nextGaussian() * 0.1;
        ya = random.nextGaussian() * 0.1;
        za = random.nextFloat() + 1;
    }

    public void tick() {
        time++;
        if (time > 60) {
            remove();
        }
        xx += xa;
        yy += ya;
        zz += za;
        if (zz < 0) {
            zz = 0;
            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }
        za -= 0.1;
        x = (int) (xx);
        y = (int) (yy);
    }

    public void render(Screen screen) {
        Font.draw(msg, screen, x - msg.length() * 4 + 1, y - (int) (zz) + 1, Color.get(-1, 0, 0, 0));
        Font.draw(msg, screen, x - msg.length() * 4, y - (int) (zz), col);
    }
}
