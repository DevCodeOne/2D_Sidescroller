package com.Game.GameMechanic;

import com.Game.Game;
import com.Game.Graphics.Pixmap;
import com.Game.Mathematics.Vector2f;
import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;


public class Platform extends Entity implements Tick {

    private Map map;
    private Vector2f direction;
    private double len;
    private double start_len;
    private Entity carry[];
    private int entity_index;

    public Platform(Pixmap pixmap, int startx, int starty, int endx, int endy, Map map) {
        this(new Pixmap[]{pixmap}, startx, starty, endx, endy, map);
    }

    public Platform(Pixmap[] pixmap, int startx, int starty, int endx, int endy, Map map) {
        super(pixmap, startx, starty);
        this.direction = new Vector2f(endx - startx, endy - starty);
        this.len = direction.get_len();
        this.start_len = direction.get_len();
        this.direction.normalize();
        this.map = map;
        this.carry = new Entity[5];
        this.hovers(true);
        this.attach_event(new EntityEvent() {
            public void on_collision(Entity entity, Entity entity2) {
                if (!(entity2 instanceof Player))
                    return;
                if (entity2.get_velocity_y() >= 0) {
                    if (Math.abs(entity.get_y() - (entity2.get_y() + entity2.get_height())) < 10) {
                        for (int i = 0; i < carry.length; i++) { // avoid duplicates
                            if (carry[i] == entity2)
                                return;
                        }
                        carry[entity_index] = entity2;
                        entity2.set_on_ground(true);
                        entity2.set_y(get_y() - entity2.get_height() + 1);
                        entity_index++;
                    }
                }
            }
        });
        this.ignores_physics(true);
    }

    @Override
    public void tick() {
        change_pos_by(direction.get_x(), direction.get_y());
        if ((!Physics.check_for_collision(map, this) && (get_x() / map.get_tile_width() < map.get_width())) && len > 1) {
            for (int i = 0; i < carry.length; i++) {
                if (carry[i] != null) {
                    carry[i].change_pos_by(direction.get_x(), direction.get_y());
                    if (Physics.check_for_collision(map, carry[i])) {
                        carry[i].change_pos_by(-direction.get_x(), -direction.get_y());
                    }
                }
            }
            len--;
        } else {
            change_pos_by(-direction.get_x(), -direction.get_y());
            direction.negate();
            len = start_len;
        }


        for (int i = 0; i < carry.length; i++) {
            if (carry[i] != null) {
                if (!carry[i].get_bounds().intersects(this.get_bounds()) || carry[i].get_velocity_y() < 0) {
                    carry[i] = null;
                    entity_index--;
                } else {
                    carry[i].set_on_ground(true);
                    carry[i].set_y(get_y() - carry[i].get_height() + 1);
                }
            }
        }
    }

    public void set_map(Map map) {
        this.map = map;
    }

    @Override
    public int tick_skip() {
        return 0;
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
    }

    public Map get_map() {
        return map;
    }
}
