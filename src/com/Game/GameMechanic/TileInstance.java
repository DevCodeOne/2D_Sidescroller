package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;

public class TileInstance {

    private boolean solid;
    private boolean transparent;
    private Pixmap[] pixmap;
    private int frames;

    public TileInstance(boolean transparent, boolean solid, Pixmap pixmap) {
        this.transparent = transparent;
        this.solid = solid;
        this.pixmap = new Pixmap[1];
        this.pixmap[0] = pixmap;
        this.frames = 1;
    }

    public TileInstance(boolean transparent, boolean solid, Pixmap[] pixmap) {
        this.transparent = transparent;
        this.solid = solid;
        this.pixmap = new Pixmap[pixmap.length];
        this.frames = pixmap.length;
        for (int i = 0; i < pixmap.length; i++)
            this.pixmap[i] = pixmap[i];
    }

    public boolean is_solid() {
        return solid;
    }

    public boolean is_transparent() {
        return transparent;
    }

    public Pixmap get_pixmap() {
        return pixmap[0];
    }

    public Pixmap get_frame(int index) {
        return pixmap[index];
    }

    public int get_frames_count() {
        return frames;
    }

}
