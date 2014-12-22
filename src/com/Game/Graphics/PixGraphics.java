package com.Game.Graphics;

import com.Game.Resources.ResourceLoader;

public class PixGraphics {

    private Pixmap render_target;
    private Pixmap font;
    private int font_width;
    private int color;

    public PixGraphics(Pixmap render_target) {
        this.render_target = render_target;
    }

    public void load_font(String src, int size_x) {
        font = ResourceLoader.load_image(src);
        font_width = size_x;
    }

    public void draw_char(char c, int x, int y) {
        int index = 0;
        if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
            index = Character.toUpperCase(c) - 65;
        else if (c >= 48 && c <= 57)
            index = (c - 48) + 26;
        render_target.blit_font(font, index * font_width, 0, font_width, font.get_height(), x, y, color);
    }

    public void draw_string(String str, int x, int y) {
        for (int i = 0; i < str.length(); i++) {
            draw_char(str.charAt(i), x + i * font_width, y);
        }
    }

    public void draw_string_centered(String str, int x, int y) {
        x -= (str.length() * font_width) >> 1;
        draw_string(str, x, y);
    }

    public void set_color(int color) {
        this.color = color;
    }

    public Pixmap get_render_target() { return render_target; }
}
