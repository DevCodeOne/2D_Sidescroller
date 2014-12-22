package com.Game.GameMechanic;

import com.Game.Graphics.PixGraphics;
import com.Game.Graphics.Pixmap;

public class Player extends Entity {

    private String name = "Player";

    public Player(Pixmap texture, int x, int y, float health) {
        this(new Pixmap[]{texture}, x, y, health);
    }

    public Player(Pixmap[] frames, int x, int y, float health) {
        super(frames, x, y);
        set_health(health);
    }

    public void draw(PixGraphics graphics, Map map) {
        draw(graphics.get_render_target(), map);
        graphics.set_color(80 << 8 | 255);
        graphics.draw_string_centered(name, get_int_x() + map.get_offx(), get_int_y() + map.get_offy() - 10);
    }

    public void draw(Pixmap pixmap, Map map) {
        if (!is_on_ground() && get_velocity_y() < 0) { // jump
            int old_index = get_frame_index();
            set_frame_index(3);
            super.draw(pixmap, map);
            set_frame_index(old_index);
        } else if (is_on_ground() && get_velocity_x() == 0) {
            int old_index = get_frame_index();
            set_frame_index(0);
            super.draw(pixmap, map);
            set_frame_index(old_index);
        } else {
            super.draw(pixmap, map);
        }
    }

    @Override
    public void change_pos_by(float x, float y) {
        position.add(x, y);
        rectangle.setRect(position.get_x() - (get_width() >> 1), position.get_y(), get_width(), get_height());
    }

    public void set_name(String name) { this.name = name; }

}
