package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;

public class Player extends Entity {


    public Player(Pixmap texture, int x, int y, float health) {
        super(texture, x, y);
        set_health(health);
    }

    public Player(Pixmap[] frames, int x, int y, float health) {
        super(frames, x, y);
        set_health(health);
    }

}
