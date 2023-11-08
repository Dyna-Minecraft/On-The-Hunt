package com.dyna.oth;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

    public static final String NAME = "On The Hunt [0.0.0]";
    public static final int HEIGHT = 240;
    public static final int WIDTH = HEIGHT * 16 / 9;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    private boolean running = false;

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            runStep();
        }
    }

    public void runStep() {
        tick();
        render();
    }

    public void tick() {
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        for (int i = 0; i < pixels.length; i++) {

        }


        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {

        Game game = new Game();
        game.setMinimumSize(new Dimension(WIDTH*2, HEIGHT*2));
        game.setPreferredSize(new Dimension(WIDTH*2, HEIGHT*2));
        game.setMaximumSize(new Dimension(WIDTH*2, HEIGHT*2));

        JFrame frame = new JFrame(Game.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}
