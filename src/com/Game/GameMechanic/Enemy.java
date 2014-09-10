package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;
import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;

public class Enemy extends Entity implements Tick {

    private char dir;
    private float velocity;
    private Map map;
    private Pixmap health_bar;

    public Enemy(Pixmap texture, int x, int y, float health, float velocity, Map map) {
        super(texture, x, y);
        this.velocity = velocity;
        this.dir = 'L';
        this.map = map;
        this.set_health(health);
    }

    public Enemy(Pixmap[] frames, int x, int y, float health, float velocity, Map map) {
        super(frames, x, y);
        this.velocity = velocity;
        this.dir = 'L';
        this.map = map;
        this.set_health(health);
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
    }

    public void set_health_bar(Pixmap pixmap) {
        this.health_bar = pixmap;
    }

    @Override
    public void draw(Pixmap pixmap, int offx, int offy) {
        if (-offx > get_x() || -offy > get_y() || -offx + pixmap.get_width() < get_x() || -offy + pixmap.get_height() < get_y())
            return;
        if (!is_flipped())
            pixmap.blit(get_frame(get_frame_index()), (int) (get_x() + offx) - (get_pixmap().get_width() >> 1), (int) (get_y() + offy), true);
        else
            pixmap.blit_flip_vertically(get_frame(get_frame_index()), (int) (get_x() + offx) - (get_pixmap().get_width() >> 1), (int) (get_y() + offy), true);
        if (health_bar == null)
            return;
        int health_per = (int) ((get_health() / get_health_stat()) * 10) * health_bar.get_width() / 10;
        pixmap.blit(health_bar, 0, 0, health_per, health_bar.get_height(), (int) (get_x() + offx) - (get_pixmap().get_width() >> 1), (int) (get_y() + offy) - 10, true);
    }

    public void tick() {
        if (dir == 'L') {
            change_pos_by(velocity, 0);
            if (Physics.check_for_solid_object_bottom(map, this) && !Physics.check_for_collision_side(map, this) && (get_x() / map.get_tile_width() < map.get_width())) {
                change_pos_by(-velocity, 0);
                walk(velocity);
                if (System.currentTimeMillis() - frame_last_changed() > 100)
                    inc_frame_index();
                return;
            } else {
                flip_vertically();
                change_pos_by(-velocity, 0);
                set_frame_index(0);
                dir = 'R';
                change_pos_by(-velocity, 0);
                if (Physics.check_for_solid_object_bottom(map, this) && !Physics.check_for_collision_side(map, this) && get_x() > 0) {
                    change_pos_by(velocity, 0);
                    walk(-velocity);
                    if (System.currentTimeMillis() - frame_last_changed() > 100)
                        inc_frame_index();
                    return;
                }
            }
        } else if (dir == 'R') {
            change_pos_by(-velocity, 0);
            if (Physics.check_for_solid_object_bottom(map, this) && !Physics.check_for_collision_side(map, this) && get_x() > 0) {
                change_pos_by(velocity, 0);
                walk(-velocity);
                if (System.currentTimeMillis() - frame_last_changed() > 100)
                    inc_frame_index();
                return;
            } else {
                flip_vertically();
                change_pos_by(velocity, 0);
                set_frame_index(0);
                dir = 'L';
                change_pos_by(velocity, 0);
                if (Physics.check_for_solid_object_bottom(map, this) && !Physics.check_for_collision_side(map, this) && (get_x() / map.get_tile_width() < map.get_width())) {
                    change_pos_by(-velocity, 0);
                    walk(velocity);
                    if (System.currentTimeMillis() - frame_last_changed() > 100)
                        inc_frame_index();
                    return;
                }
            }
        }
    }

    public void change_dir() {
        if (dir == 'R')
            dir = 'L';
        else if (dir == 'L')
            dir = 'R';
        flip_vertically();
    }

    public int tick_skip() {
        return 0;
    }
}
