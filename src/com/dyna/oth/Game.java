package com.dyna.oth;

import com.dyna.oth.gfx.Screen;
import com.dyna.oth.gfx.SpriteSheet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

public class Game extends Canvas implements Runnable {

    public static final String NAME = "On The Hunt [0.0.0]";
    public static final int HEIGHT = 240;
    public static final int WIDTH = HEIGHT * 16 / 9;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private boolean running = false;
    private int tickCount;
    private Screen screen;


    public void start() {
        running = true;
        new Thread(this).start();
    }

    private void init() {
        try {
            screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        running = false;
    }

    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 60.0;
        System.out.println(unprocessed);
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

        screen.render(pixels, 0, WIDTH);

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, WIDTH * 3, HEIGHT * 3, null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {

        Game game = new Game();
        game.setMinimumSize(new Dimension(WIDTH * 2, HEIGHT * 2));
        game.setPreferredSize(new Dimension(WIDTH * 2, HEIGHT * 2));
        game.setMaximumSize(new Dimension(WIDTH * 2, HEIGHT * 2));

        JFrame frame = new JFrame(Game.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.setContentPane(panel);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
        System.out.println("Game started");
    }
}
