package com.dyna.oth;

import com.dyna.oth.gfx.Color;
import com.dyna.oth.gfx.Font;
import com.dyna.oth.gfx.Screen;
import com.dyna.oth.gfx.SpriteSheet;

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
    private int tickCount;
    private Screen screen;
    private InputHandler input = new InputHandler(this);
    private int walkDist = 0;
    private int dir = 0;

    private int[] colors = new int[256];

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    private void init() {
        int pp = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);
                    int mid = (rr * 30 + gg * 59 + bb * 11) / 100;
                    rr = ((rr + mid) / 2) * 200 / 255 + 35;
                    gg = ((gg + mid) / 2) * 200 / 255 + 35;
                    bb = ((bb + mid) / 2) * 200 / 255 + 35;
                    colors[pp++] = rr << 16 | gg << 8 | bb;
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
        if (!hasFocus()) {
            input.releaseAll();
        }

        boolean walked = false;
        int horizontalSpeed = 0;
        int verticalSpeed = 0;
        if (input.up) {
            dir = 1;
            walked = true;
            verticalSpeed = -1;
        }
        if (input.down) {
            dir = 0;
            walked = true;
            verticalSpeed = 1;
        }
        if (input.left) {
            dir = 2;
            walked = true;
            horizontalSpeed = -1;
        }
        if (input.right) {
            dir = 3;
            walked = true;
            horizontalSpeed = 1;
        }
        if (horizontalSpeed != 0 && verticalSpeed != 0) {
            horizontalSpeed *= 7;
            verticalSpeed *= 7;
        }
        else {
            horizontalSpeed *= 10;
            verticalSpeed *= 10;
        }

        screen.yScroll += verticalSpeed;
        screen.xScroll += horizontalSpeed;

        if (walked) walkDist++;
        tickCount++;
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        screen.render();
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

            screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, Color.get(-1, 100, 220, 542), flip1);
            screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, Color.get(-1, 100, 220, 542), flip1);
            screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, Color.get(-1, 100, 220, 542), flip2);
            screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, Color.get(-1, 100, 220, 542), flip2);
        }

        Font.draw("abcdefghi 0123456789", screen, 0, 0, Color.get(-1, 555, 555, 555));
        for (int y = 0; y < screen.h; y++) {
            for (int x = 0; x < screen.w; x++) {
                pixels[x + y * WIDTH] = colors[screen.pixels[x + y * screen.w]];
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
