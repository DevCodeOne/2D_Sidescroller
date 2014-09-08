package com.Game.GameMechanic;

import com.Game.Timing.Tick;

import java.util.ArrayList;

public class EntityCollider implements Tick {

    private ArrayList<Entity> entities;

    public EntityCollider() {
        this.entities = new ArrayList<Entity>();
    }

    public void add_entity(Entity entity) {
        entities.add(entity);
    }

    public void tick() {
        Entity entity;
        Entity entity2;
        for (int i = 0; i < entities.size(); i++) {
            entity = entities.get(i);
            for (int j = 0; j < entities.size(); j++) {
                if (j != i) {
                    entity2 = entities.get(j);

                    if (entity.get_x() < entity2.get_x())
                        if (entity.get_x() + entity.get_width() < entity2.get_x())
                            continue;
                    if (entity.get_x() > entity2.get_x())
                        if (entity.get_x() > entity2.get_x() + entity2.get_width())
                            continue;
                    if (entity.get_y() < entity2.get_y())
                        if (entity.get_y() + entity.get_height() < entity2.get_y())
                            continue;
                    if (entity.get_y() > entity2.get_y())
                        if (entity.get_y() > entity2.get_y() + entity2.get_height())
                            continue;

                    if (entity.has_events())
                        entity.call_on_collision(entity2);
                }

            }
        }
    }

    public int tick_skip() {
        return 0;
    }

}
