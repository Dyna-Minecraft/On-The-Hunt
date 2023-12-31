package com.dyna.oth.level.tile;

import com.dyna.oth.entity.Entity;
import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.Level;

public class DeepsandTile extends Tile {
    public DeepsandTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        int col = Color.get(-1, 480, 460, -1);
        screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
        screen.render(x * 16 + 8, y * 16 + 0, 0, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 0, col, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 0, col, 0);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return false;
    }
}
