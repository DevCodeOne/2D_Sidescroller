package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;

public class Player extends Entity {

    private Map map;

    public Player(Pixmap texture, int x, int y, float health, Map map) {
        super(texture, x, y);
        set_health(health);
        this.map = map;
    }

    public Player(Pixmap[] frames, int x, int y, float health, Map map) {
        super(frames, x, y);
        set_health(health);
        this.map = map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public void change_pos_by(double x, double y) {
        position.add(x, y);
        rectangle.setRect(position.get_x() - (get_width() >> 1), position.get_y(), get_width(), get_height());
    }

}
