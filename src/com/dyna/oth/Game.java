package com.dyna.oth;

import com.dyna.oth.entity.Player;
import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Font;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.gfx.SpriteSheet;
import com.dyna.oth.level.Level;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    public static final String NAME = "On The Hunt";
    public static final int HEIGHT = 120;
    public static final int WIDTH = 160;
    private static final int SCALE = 3;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private boolean running = false;
    private Screen screen;
    private InputHandler input = new InputHandler(this);
    private int walkDist = 0;
    private int dir = 0;

    private int[] colors1 = new int[256];
    private int[] colors2 = new int[256];
    private int tickCount = 0;

    private Level level;
    private Player player;

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    private void init() {
        level = new Level(8, 8);
        player = new Player();

        level.add(player);

        int pp = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);
                    int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

                    int r1 = ((rr + mid) / 2) * 200 / 255 + 35;
                    int g1 = ((gg + mid) / 2) * 200 / 255 + 35;
                    int b1 = ((bb + mid) / 2) * 200 / 255 + 35;
                    colors1[pp] = r1 << 16 | g1 << 8 | b1;

                    int r2 = ((rr + mid) / 2) * 200 / 255 + 45;
                    int g2 = ((gg + mid) / 2) * 200 / 255 + 45;
                    int b2 = ((bb + mid) / 2) * 200 / 255 + 55;
                    colors2[pp++] = r2 << 16 | g2 << 8 | b2;
                }
            }
        }
        try {
            screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getClassLoader().getResourceAsStream("icons.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 60;
        int frames = 0;
        int ticks = 0;
        long lastTimer1 = System.currentTimeMillis();

        init();

        while (running) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (unprocessed >= 1) {
                ticks++;
                tick();
                unprocessed -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;
                System.out.println(ticks + " ticks, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;

        if (!hasFocus()) {
            input.releaseAll();
        }

        int xa = 0;
        int ya = 0;
        boolean walked = false;
        if (input.up) {
            ya--;
        }
        if (input.down) {
            ya++;
        }

        if (input.left) {
            xa--;
        }
        if (input.right) {
            xa++;
        }

        if (xa!=0 && ya!=0) {
            player.move(xa, ya);
            walkDist++;
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        level.render(screen, player.x, player.y);

        for (int y = 0; y < screen.h; y++) {
            for (int x = 0; x < screen.w; x++) {
                pixels[x + y * WIDTH] = colors1[screen.pixels[x + y * screen.w]];
            }
        }
        screen.clear();
        {
            int xo = WIDTH / 2 - 8;
            int yo = HEIGHT / 2 - 8;

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

            screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, Color.get(-1, 100, 411, 542), flip1); // top left
            screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, Color.get(-1, 100, 411, 542), flip1); // top right
            screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, Color.get(-1, 100, 411, 542), flip2); // bottom left
            screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, Color.get(-1, 100, 411, 542), flip2); // bottom right
        }

        Font.draw("abcdefghi 0123456789", screen, 0, 0, Color.get(-1, 555, 555, 555));

        if (!hasFocus()) {
            String msg = "Click to focus!";
            int xx = (WIDTH - msg.length() * 8) / 2;
            int yy = (HEIGHT - 8) / 2;
            int w = msg.length();
            int h = 1;

            screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
            screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
            screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);
            for (int x = 0; x < w; x++) {
                screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
                screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
            }
            for (int y = 0; y < h; y++){
                screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
                screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
            }

            if ((tickCount/20)%2==0) {
                Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
            } else {
                Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
            }
        }

        for (int y = 0; y < screen.h; y++) {
            for (int x = 0; x < screen.w; x++) {
                int cc = screen.pixels[x + y * screen.w];
                if (cc < 255) pixels[x + y * WIDTH] = colors2[cc];
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());

        int ww = WIDTH * 3;
        int hh = HEIGHT * 3;
        int xo = (getWidth() - ww) / 2;
        int yo = (getHeight() - hh) / 2;
        g.drawImage(image, xo, yo, ww, hh, null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(Game.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}
