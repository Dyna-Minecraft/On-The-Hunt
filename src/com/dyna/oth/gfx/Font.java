package com.dyna.oth.gfx;

public class Font {
    private String chars = "" + //
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
            "0123456789.,!?'\"-+=/\\%()<>      " + //
            "";

    public void draw(String msg, Screen screen, int x, int y) {
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            int ix = chars.indexOf(msg.charAt(i));
            if (ix >= 0) {
                screen.setTile(x + i, y, 0, ix + 30 * 32, (5 + 5 * 6 + 5 * 36) * 256 * 256 * 256, 0);
            }
        }
    }
}
