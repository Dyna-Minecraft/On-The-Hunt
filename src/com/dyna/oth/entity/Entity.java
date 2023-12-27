package com.dyna.oth.entity;

import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.Level;

public class Entity {
    public int x, y;
    public boolean removed;
    public Level level;

    public void render(Screen screen) {
    }

    public void remove() {
        removed = true;
    }

    public final void init(Level level) {
        this.level = level;
    }
}
