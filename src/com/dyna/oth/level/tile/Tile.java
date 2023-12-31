package com.dyna.oth.level.tile;

import com.dyna.oth.entity.Entity;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.Level;

public class Tile {
    public static Tile[] tiles = new Tile[256];
    public static Tile sand = new SandTile(0);
    public static Tile rock = new RockTile(1);
    public static Tile deepsand = new DeepsandTile(2);

    public final byte id;

    public Tile(int id) {
        this.id = (byte) id;
        tiles[id] = this;
    }

    public void render(Screen screen, Level level, int x, int y) {
    }

    public boolean mayPass(Level level, int x, int y, Entity e) {
        return true;
    }
}
