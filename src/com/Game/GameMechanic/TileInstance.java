package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;

public class TileInstance {

    private boolean solid;
    private boolean transparent;
    private Pixmap[] pixmap;
    private int frames;
    private int type;
    private int rgb_index;
    private int index;

    public static final int TYPE_TORCH = 1;
    public static final int TYPE_TRAP = 2;
    public static final int TYPE_LAVA = 3;
    public static final int TYPE_TRAPDOOR = 4;
    public static final int TYPE_BACKGROUND = 5;

    public TileInstance(boolean transparent, boolean solid, Pixmap pixmap[], int type, int index, int rgb_index) {
        this(transparent, solid, pixmap);
        this.type = type;
        this.rgb_index = rgb_index;
        this.index = index;
    }

    public TileInstance(boolean transparent, boolean solid, Pixmap pixmap, int type, int index, int rgb_index) {
        this(transparent, solid, pixmap);
        this.type = type;
        this.rgb_index = rgb_index;
        this.index = index;
    }

    protected TileInstance(boolean transparent, boolean solid, Pixmap pixmap) {
        this(transparent, solid, new Pixmap[]{pixmap});
    }

    protected TileInstance(boolean transparent, boolean solid, Pixmap[] pixmap) {
        this.transparent = transparent;
        this.solid = solid;
        this.pixmap = new Pixmap[pixmap.length];
        this.frames = pixmap.length;
        System.arraycopy(pixmap, 0, this.pixmap, 0, pixmap.length);
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

    public int get_type() { return type; }

    public int get_rgb_index() { return rgb_index; }

    public int get_index() { return index; }

}
