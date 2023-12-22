package com.dyna.oth;

import com.dyna.oth.gfx.Color;
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
    private InputHandler inputHandler = new InputHandler(this);

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
                    colors[pp++] = (r * 255 / 5) << 16 | (g * 255 / 5) << 8 | (b * 255 / 5);
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
        double nsPerTick = 1000000000.0 / 60.0;
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
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render();
        screen.render(0, 0, 0 + 14 * 32, Color.get(-1, 555, 555, 555), 0);
        screen.render(8, 0, 1 + 14 * 32, Color.get(-1, 555, 555, 555), 0);
        screen.render(0, 8, 0 + 15 * 32, Color.get(-1, 555, 555, 555), 0);
        screen.render(8, 0, 1 + 15 * 32, Color.get(-1, 555, 555, 555), 0);
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
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}
