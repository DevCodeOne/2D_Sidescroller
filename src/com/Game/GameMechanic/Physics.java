package com.Game.GameMechanic;

import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;

import java.util.ArrayList;

public class Physics implements Tick {

    private ArrayList<Entity> entities;
    private Map map;
    private float factor, max_factor;
    private EntityCollider collider;

    public Physics(Map map, float factor, float max_factor) {
        this.map = map;
        this.factor = factor;
        this.max_factor = max_factor;
        this.entities = new ArrayList<Entity>();
        this.collider = new EntityCollider();
    }

    public static boolean check_for_collision(Map map, Entity entity) {
        int left = (int) ((entity.get_bounds().getMinX() / (float)map.get_tile_width()) + 0.5f);
        int top = (int) ((entity.get_y() / (float)map.get_tile_height()) + 0.5f);
        int right = (int) ((entity.get_bounds().getMaxX() / (float)map.get_tile_width()) + 0.5f);
        int bottom = (int) ((entity.get_bounds().getMaxY() / map.get_tile_height()));
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j < right; j++) {
                if (map.get_data(j, i).is_solid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean check_for_solid_object_bottom(Map map, Entity entity) {
        int left = (int) ((entity.get_bounds().getMinX() / (float)map.get_tile_width()) + 0.5f);
        int right = (int) ((entity.get_bounds().getMaxX() / (float)map.get_tile_width()) + 0.5f);
        int bottom = (int) (((entity.get_bounds().getMaxY()) / (float) map.get_tile_height())) + 1;
        for (int i = left; i < right; i++) {
            if (map.get_data(i, bottom).is_solid()) {
                return true;
            }
        }
        return false;
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
        clock.attach(collider);
    }

    public void tick() {
        one_step();
    }

    public void one_step() {
        boolean walk_y = false;
        boolean walk_x = true;
        for (Entity entity : entities) {
            if (entity.ignores_physics())
                continue;

            if (entity.get_velocity_y() < max_factor && !entity.is_on_ground())
                entity.inc_velocity_y(factor);

            if (entity.get_velocity_y() < 0 || !entity.is_on_ground()) {
                entity.change_pos_by(0, entity.get_velocity_y());

                walk_y = true;
                if (check_for_collision(map, entity)) {
                    entity.change_pos_by(0, -entity.get_velocity_y());
                    int pos_block = ((int) ((entity.get_y() / (float) map.get_tile_height())) + 1) * map.get_tile_height();
                    entity.change_pos_by(0, -(entity.get_y() - pos_block) - 2);
                    if (entity.get_velocity_y() > 0) {
                        entity.set_velocity_y(0);
                        entity.set_on_ground(true);
                        walk_y = false;
                    }
                }
                if (walk_y) {
                    entity.set_on_ground(false);
                    entity.change_pos_by(0, -entity.get_velocity_y());
                }
            }

            // x-dir
            walk_x = true;
            entity.change_pos_by(entity.get_velocity_x(), entity.get_velocity_y());
            if (check_for_collision(map, entity)) {
                entity.change_pos_by(-entity.get_velocity_x(), -entity.get_velocity_y());
                entity.set_velocity_x(0);
                walk_x = false;
            }
            if (walk_x) {
                entity.change_pos_by(-entity.get_velocity_x(), -entity.get_velocity_y());
                if (entity.is_on_ground()) {
                    if (entity.get_velocity_x() > 0.25f)
                        entity.inc_velocity_x(-0.25f);
                    else if (entity.get_velocity_x() < -0.25f)
                        entity.inc_velocity_x(0.25f);
                    else
                        entity.set_velocity_x(0);
                }
            }

            if (walk_x || walk_y) {
                map.toggle_events(entity, Map.ON_LEAVE);
                entity.change_pos_by(walk_x ? entity.get_velocity_x() : 0, walk_y ? entity.get_velocity_y() : 0);
                map.toggle_events(entity, Map.ON_STEP);
            }

            if (!check_for_solid_object_bottom(map, entity))
                entity.set_on_ground(false);



        }
    }

    public void add_entity(Entity entity) {
        entities.add(entity);
        collider.add_entity(entity);
    }

    public int tick_skip() {
        return 0;
    }

}
