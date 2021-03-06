package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;
import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;

public class Enemy extends Entity implements Tick {

    private float velocity;
    private Map map;
    private Pixmap health_bar;

    public Enemy(Pixmap texture, int x, int y, float health, float velocity, Map map) {
        this(new Pixmap[]{texture}, x, y, health, velocity, map);
    }

    public Enemy(Pixmap[] frames, int x, int y, float health, float velocity, Map map) {
        super(frames, x, y);
        this.velocity = velocity;
        this.map = map;
        this.set_health(health);
        this.set_max_velocity(velocity);
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
    }

    public void set_health_bar(Pixmap pixmap) {
        this.health_bar = pixmap;
    }

    @Override
    public void draw(Pixmap pixmap, Map map) {
        super.draw(pixmap, map);
        if (health_bar != null)
            draw_health_bar(pixmap, map.get_offx(), map.get_offy());
    }

    @Override
    public void draw(Pixmap pixmap, int offx, int offy) {
        super.draw(pixmap, offx, offy);
        if (health_bar != null)
            draw_health_bar(pixmap, offx, offy);
    }

    private void draw_health_bar(Pixmap pixmap, int offx, int offy) {
        int health_per = (int) ((get_health() / get_health_stat()) * health_bar.get_width());
        pixmap.blit(health_bar, 0, 0, health_per, health_bar.get_height(), (int) (get_x() + offx) - (get_pixmap().get_width() >> 1), (int) (get_y() + offy) - 10, true);
    }

    public void tick() {
        change_pos_by(velocity, 0);
        if (Physics.check_for_solid_object_bottom(map, this) && !Physics.check_for_collision(map, this) && (get_x() / map.get_tile_width() < map.get_width()) && get_x() > 0) {
            change_pos_by(velocity, 0);
            if (System.currentTimeMillis() - frame_last_changed() > 100)
                inc_frame_index();
        } else {
            flip_vertically();
            velocity *= -1;
            change_pos_by(velocity, 0);
            set_frame_index(0);
        }
    }

    public void set_map(Map map) {
        this.map = map;
    }

    public int tick_skip() {
        return 0;
    }
}
