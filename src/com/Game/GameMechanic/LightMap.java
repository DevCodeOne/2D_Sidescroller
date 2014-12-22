package com.Game.GameMechanic;

public class LightMap {

    private Map map;
    private float ambient_light;

    public LightMap(Map map) {
        this.map = map;
    }

    public void render_light_map() {
        for (int i = 0; i < map.get_width(); i++) {
            for (int j = 0; j < map.get_height(); j++) {
                map.get_data(i, j).set_brightness(ambient_light);
            }
        }
        Tile tile;
        for (int i = 0; i < map.get_width(); i++) {
            for (int j = 0; j < map.get_height(); j++) {
                if (map.get_data(i, j).emits_light()) {
                    tile = map.get_data(i, j);
                    for (int k = -tile.get_radius(); k < tile.get_radius(); k++) {
                        for (int l = -tile.get_radius(); l < tile.get_radius(); l++) {
                            int dis = (int) (Math.sqrt(k * k + l * l) + 0.5f);
                            if (dis < map.get_data(i, j).get_radius()) {
                                map.get_data(i + k, j + l).set_brightness(((1 - (dis / (float) tile.get_radius())) * tile.get_emiting_light()) + map.get_data(i + k, j + l).get_brightness());
                            }
                        }
                    }
                }
            }
        }
    }

    public void set_ambient_light(float ambient_light) {
        this.ambient_light = ambient_light;
    }

    public void set_light_emitting_global(char id, float emiting_light, int radius) {
        Tile tile;
        for (int i = 0; i < map.get_width(); i++) {
            for (int j = 0; j < map.get_height(); j++) {
                if (map.get_data(i, j).get_id() == id) {
                    tile = map.get_data(i, j);
                    tile.emits_light(true);
                    tile.set_emiting_light(emiting_light);
                    tile.set_radius(radius);
                }
            }
        }
    }
}
