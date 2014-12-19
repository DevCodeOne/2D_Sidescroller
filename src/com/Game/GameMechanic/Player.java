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

}
