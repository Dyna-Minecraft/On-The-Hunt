package com.dyna.oth.level.tile;

import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.Level;

public class SandTile extends Tile {
    public SandTile(int id) {
        super(id);
    }

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x * 16 + 0, y * 16 + 0, 0, Color.get(-1, level.sandColor, level.SandDetailColor, level.sandColor), 0);
        screen.render(x * 16 + 8, y * 16 + 0, 0, Color.get(-1, level.sandColor, level.SandDetailColor, level.sandColor), 0);
        screen.render(x * 16 + 0, y * 16 + 8, 0, Color.get(-1, level.sandColor, level.SandDetailColor, level.sandColor), 0);
        screen.render(x * 16 + 8, y * 16 + 8, 0, Color.get(-1, level.sandColor, level.SandDetailColor, level.sandColor), 0);
    }
}
