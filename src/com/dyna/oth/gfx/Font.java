package com.dyna.oth.gfx;

public class Font {
    private String chars = "" + //
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
            "0123456789.,!?'\"-+=/\\%()<>      " + //
            "";

    private int white = (5 + 5 * 6 + 5 * 36) * 256 * 256 * 256;
    private int black = 0;

    public void draw(String msg, Screen screen, int x, int y) {
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            int ix = chars.indexOf(msg.charAt(i));
            if (ix >= 0) {
                int color = msg.charAt(i) != ' ' ? white : black;
                screen.setTile(x + i, y, ix + 30 * 32, color, 0);
            }
        }
    }
}