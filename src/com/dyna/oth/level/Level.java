package com.dyna.oth.level;

import com.dyna.oth.entity.Entity;
import com.dyna.oth.entity.Player;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    public int w, h;

    public byte[] tiles;
    public byte[] data;

    public int sandColor = 550; //this colour is uglyy

    public List<Entity> entities = new ArrayList<Entity>();

    public Level(int w, int h) {
        this.w = w;
        this.h = h;
        tiles = new byte[w * h];
        tiles = new byte[w * h];

        Random random = new Random();

        for (int i = 0; i < w * h; i++) {
            tiles[i] = Tile.sand.id;
            if (random.nextInt(20) == 0) {
                tiles[i] = Tile.rock.id;
            }
        }
    }

    public void render(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.w + 15) >> 4;
        int h = (screen.h + 15) >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                getTile(x, y).render(screen, this, x, y);
            }
        }
        screen.setOffset(0, 0);
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return Tile.sand;
        return Tile.tiles[tiles[x + y * 4]];
    }

    public void add(Entity entity) {
        entities.add(entity);
        entity.init(this);
    }
}
