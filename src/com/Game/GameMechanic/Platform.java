package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;
import com.Game.Mathematics.Vector2f;
import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;


public class Platform extends Entity implements Tick {

    private Map map;
    private Vector2f start, end;
    private Vector2f direction;
    private float len;
    private char action;
    private Entity carry[];
    private int entity_index;

    public Platform(Pixmap pixmap, int startx, int starty, int endx, int endy, Map map) {
        this(new Pixmap[]{pixmap}, startx, starty, endx, endy, map);
    }

    public Platform(Pixmap[] pixmap, int startx, int starty, int endx, int endy, Map map) {
        super(pixmap, startx, starty);
        this.start = new Vector2f(startx, starty);
        this.end = new Vector2f(endx, endy);
        this.direction = new Vector2f(endx - startx, endy - starty);
        this.len = direction.get_len();
        this.direction.normalize();
        this.action = 'n';
        this.map = map;
        this.carry = new Entity[5];
        this.hovers(true);
        this.attach_event(new EntityEvent() {
            public void on_collision(Entity entity, Entity entity2) {
                if (!(entity2 instanceof Player))
                    return;
                if (entity2.get_velocity_y() >= 0) {
                    if (entity.get_y() - entity2.get_y() > 1) {
                        Vector2f old_position = new Vector2f(entity2.get_x(), entity2.get_y());
                        entity2.set_pos(entity2.get_x(), entity.get_y() - entity2.get_height());
                        if (Physics.check_for_collision(((Platform) entity).get_map(), entity2)) {
                            entity2.set_pos(old_position.get_x(), old_position.get_y());
                            return;
                        }
                        entity2.set_pos(old_position.get_x(), old_position.get_y());
                        ((Platform) entity).get_map().toggle_events(entity2, Map.ON_LEAVE);
                        entity2.set_pos(entity2.get_x(), entity.get_y() - entity2.get_height());
                        carry[entity_index] = entity2;
                        entity2.set_on_ground(true);
                        entity2.set_velocity_y(0);
                        entity_index++;
                    }
                }
            }
        });
        this.ignores_physics(true);
    }

    @Override
    public void tick() {
        if (action == 'n') {
            if (len > 1) {
                change_pos_by(direction.get_x(), direction.get_y());
                if (!Physics.check_for_collision(map, this) && (get_x() / map.get_tile_width() < map.get_width())) {
                    for (int i = 0; i < carry.length; i++) {
                        if (carry[i] != null) {
                            carry[i].change_pos_by(direction.get_x(), direction.get_y());
                            if (Physics.check_for_collision(map, carry[i])) {
                                carry[i].change_pos_by(-direction.get_x(), -direction.get_y());
                                carry[i] = null;
                            }
                        }
                    }
                } else {
                    change_pos_by(-direction.get_x(), -direction.get_y());
                    action = 'o';
                    direction = new Vector2f(start.get_x() - get_x(), start.get_y() - get_y());
                    len = direction.get_len();
                    direction.normalize();
                    return;
                }
                len--;
            } else {
                action = 'o';
                direction = new Vector2f(start.get_x() - get_x(), start.get_y() - get_y());
                len = direction.get_len();
                direction.normalize();
                return;
            }

        } else if (action == 'o') {
            if (len > 1) {
                change_pos_by(direction.get_x(), direction.get_y());
                if (!Physics.check_for_collision(map, this) && (get_x() / map.get_tile_width() < map.get_width())) {
                    for (int i = 0; i < carry.length; i++) {
                        if (carry[i] != null) {
                            carry[i].change_pos_by(direction.get_x(), direction.get_y());
                            if (Physics.check_for_collision(map, carry[i])) {
                                carry[i].change_pos_by(-direction.get_x(), -direction.get_y());
                                carry[i] = null;
                            }
                        }
                    }
                } else {
                    change_pos_by(-direction.get_x(), -direction.get_y());
                    action = 'n';
                    direction = new Vector2f(end.get_x() - get_x(), end.get_y() - get_y());
                    len = direction.get_len();
                    direction.normalize();
                    return;
                }
                len--;
            } else {
                action = 'n';
                direction = new Vector2f(end.get_x() - get_x(), end.get_y() - get_y());
                len = direction.get_len();
                direction.normalize();
                return;
            }
        }
        for (int i = 0; i < carry.length; i++) {
            if (carry[i] != null) {
                carry[i] = null;
            }
        }
        entity_index = 0;
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
