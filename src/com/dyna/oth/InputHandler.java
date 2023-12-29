package com.dyna.oth;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class InputHandler implements KeyListener {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean attack;

    public void releaseAll() {
        up = down = left = right = false;
    }

    public InputHandler(Game game) {
        game.addKeyListener(this);
    }

    public void keyPressed(KeyEvent ke) {
        toggle(ke, true);
    }

    public void keyReleased(KeyEvent ke) {
        toggle(ke, false);
    }

    private void toggle(KeyEvent ke, boolean pressed) {
        if (ke.getKeyCode() == KeyEvent.VK_W) up = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_S) down = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_A) left = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_D) right = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_UP) up = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) down = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) left = pressed;
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) right = pressed;

        if (ke.getKeyCode() == InputEvent.BUTTON1_DOWN_MASK) attack = pressed;
    }

    public void keyTyped(KeyEvent ke) {
    }
}
