package com.dyna.oth.level.tile;

import com.dyna.oth.entity.Entity;
import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.Level;

public class RockTile extends Tile {
    public RockTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        int rc1 = 111;
        int rc2 = 222;
        int rc3 = 333;
        screen.render(x * 16 + 0, y * 16 + 0, 32, Color.get(rc1, level.sandColor, rc2, rc3), 0);
        screen.render(x * 16 + 8, y * 16 + 0, 32, Color.get(rc1, level.sandColor, rc2, rc3), 0);
        screen.render(x * 16 + 0, y * 16 + 8, 32, Color.get(rc1, level.sandColor, rc2, rc3), 0);
        screen.render(x * 16 + 8, y * 16 + 8, 32, Color.get(rc1, level.sandColor, rc2, rc3), 0);
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return false;
    }

}
