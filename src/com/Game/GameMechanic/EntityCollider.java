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

                    if (entity.get_bounds().collides(entity2.get_bounds()))
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
