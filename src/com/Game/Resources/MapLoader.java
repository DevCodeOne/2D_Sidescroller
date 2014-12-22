package com.Game.Resources;

import com.Game.GameMechanic.*;
import com.Game.Graphics.Pixmap;
import com.Game.Timing.GameClock;

import java.util.ArrayList;
import java.util.Scanner;

public class MapLoader {

    /*
    * MAP :
    * IMAGE : String
    * TILE :
    * index : INTEGER; transparent : true : false solid : true : false Image : String rgb_index : integer Type : integer (for traps or torches)
    * PLAYER :
    * posx : Integer; posy : Integer; Image : String; max_velocity : Float
    * PHYSICS :
    * GRAVITY : float MAX_GRAVITY : float
    * PLATFORM :
    * startx : Integer starty : INTEGER destx : Integer desty : INTEGER Image : String
    *
    *
    * */

    // TODO Improve the maploader (clean that shit up)

    public static LoadedMap loadMap(String mapFile) {
        ArrayList<TileInstance> tileInstances = new ArrayList<TileInstance>();
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        ArrayList<Platform> platforms = new ArrayList<Platform>();
        Pixmap level = null;
        Player player = null;
        Entity hearts[] = null;
        Physics physics;
        Map map;
        GameClock clock;

        float gravity = 0;
        float max_gravity = 0;
        float ambient_light = 0;

        Scanner scanner = new Scanner(mapFile);
        String line;
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.contains("MAP")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                for (String arg : args) {
                    if (arg.contains("level")) {
                        level = ResourceLoader.load_image(arg.split(":")[1].trim());
                    } else if (arg.contains("light")) {
                        ambient_light = Float.parseFloat(arg.split(":")[1].trim());
                    }
                }
            } else if (line.contains("TILE")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                int index = 0;
                int rgb_index = 0;
                int type = 0;
                boolean transparent = false;
                boolean solid = false;
                Pixmap textures[] = null;
                for (String arg : args) {
                    if (arg.contains("rgb_index")) {
                        rgb_index = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("index")) {
                        index = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("transparent")) {
                        transparent = Boolean.parseBoolean(arg.split(":")[1].trim());
                    } else if (arg.contains("solid")) {
                        solid = Boolean.parseBoolean(arg.split(":")[1].trim());
                    } else if (arg.contains("image")) {
                        String image_array[] = arg.split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (arg.contains("type")) {
                        type = Integer.parseInt(arg.split(":")[1].trim());
                    }
                }
                TileInstance instance = new TileInstance(transparent, solid, textures, type, index, rgb_index);
                tileInstances.add(instance);
            } else if (line.contains("PLAYER")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                int posx = 0, posy = 0;
                float health = 0;
                float max_velocity = 0;
                Pixmap textures[] = null;
                for (String arg : args) {
                    if (arg.contains("posx")) {
                        posx = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("posy")) {
                        posy = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("image")) {
                        String image_array[] = arg.split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (arg.contains("health")) {
                        health = Float.parseFloat(arg.split(":")[1].trim());
                    } else if (arg.contains("max_velocity")) {
                        max_velocity = Float.parseFloat(arg.split(":")[1].trim());
                    }
                }
                player = new Player(textures, posx, posy, health, null);
                player.set_max_velocity(max_velocity);
            } else if (line.contains("PHYSICS")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                for (String arg : args) {
                    if (arg.contains("max_gravity")) {
                        max_gravity = Float.parseFloat(arg.split(":")[1].trim());
                    } else if (arg.contains("gravity")) {
                        gravity = Float.parseFloat(arg.split(":")[1].trim());
                    }
                }

            } else if (line.contains("ENEMY")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                int posx = 0, posy = 0;
                float health = 0;
                float velocity = 0;
                Pixmap textures[] = null;
                Pixmap health_bar = null;
                for (String arg : args) {
                    if (arg.contains("posx")) {
                        posx = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("posy")) {
                        posy = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("image")) {
                        String image_array[] = arg.split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (arg.contains("health_bar")) {
                        health_bar = ResourceLoader.load_image(arg.split(":")[1].trim());
                    } else if (arg.contains("health")) {
                        health = Float.parseFloat(arg.split(":")[1].trim());
                    } else if (arg.contains("velocity")) {
                        velocity = Float.parseFloat(arg.split(":")[1].trim());
                    }
                }
                Enemy enemy = new Enemy(textures, posx, posy, health, velocity, null);
                if (health_bar != null)
                    enemy.set_health_bar(health_bar);
                enemies.add(enemy);
            } else if (line.contains("HEARTH")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                Pixmap textures[] = null;
                int count = 0;
                for (String arg : args) {
                    if (arg.contains("image")) {
                        String image_array[] = arg.split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (arg.contains("count")) {
                        count = Integer.parseInt(arg.split(":")[1].trim());
                    }
                }
                hearts = new Entity[count];
                for (int j = 0; j < count; j++) {
                    hearts[j] = new Entity(textures, j * textures[0].get_width() + 16, 10);
                }
            } else if (line.contains("PLATFORM")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                Pixmap texture = null;
                int startx = 0;
                int starty = 0;
                int destx = 0;
                int desty = 0;
                for (String arg : args) {
                    if (arg.contains("image")) {
                        texture = ResourceLoader.load_image(arg.split(":")[1].trim());
                    } else if (arg.contains("startx")) {
                        startx = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("starty")) {
                        starty = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("destx")) {
                        destx = Integer.parseInt(arg.split(":")[1].trim());
                    } else if (arg.contains("desty")) {
                        desty = Integer.parseInt(arg.split(":")[1].trim());
                    }
                }
                Platform platform = new Platform(texture, startx, starty, destx, desty, null);
                platforms.add(platform);
            }
        }
        int look_up[] = new int[tileInstances.size()];
        TileInstance tileInstances_arr[] = new TileInstance[tileInstances.size()];
        for (TileInstance tileInstance : tileInstances) {
            look_up[tileInstance.get_index()] = tileInstance.get_rgb_index();
            tileInstances_arr[tileInstance.get_index()] = tileInstance;
        }
        map = new Map(level, look_up, tileInstances_arr);

        for (int i = 0; i < tileInstances_arr.length; i++) {
            if (tileInstances_arr[i].get_type() == TileInstance.TYPE_BACKGROUND) {
              map.set_background_tile((char) i);
            } else if (tileInstances_arr[i].get_type() == TileInstance.TYPE_TORCH) {
                map.attach_tick_event_global(new TickTileEvent() {
                    public void tick(Tile tile) {
                        tile.inc_frame_index();
                    }

                    public int skip_ticks() {
                        return 5;
                    }
                }, (char) i);
                map.get_light_map().set_light_emitting_global((char) i, 0.75f, 10);
            } else if (tileInstances_arr[i].get_type() == TileInstance.TYPE_LAVA) {
                map.attach_tick_event_global(new TickTileEvent() {
                    public void tick(Tile tile) {
                        if (tile.get_emiting_light() == 0) {
                            tile.set_brightness((float) ((Math.sin(0) * 0.025f) + 0.45f));
                            return;
                        }
                        double rad = Math.asin((tile.get_emiting_light() - 0.45f) * 80.0f);
                        if (rad > 1.5)
                            tile.inc_frame_index();
                        rad = (rad + 0.1f) % (Math.PI * 0.5);
                        tile.set_emiting_light((float) ((Math.sin(rad) * 0.0125f) + 0.45f)); /* [ 0.45 -> 0.5]*/
                    }

                    public int skip_ticks() {
                        return 4;
                    }
                }, (char) i);
                map.get_light_map().set_light_emitting_global((char) i, 0.45f, 5);
            } else if (tileInstances_arr[i].get_type() == TileInstance.TYPE_TRAP) {
                map.attach_events_global(new TileEvent() {
                    @Override
                    public void on_step(Tile tile, Entity entity) {
                        if (entity.is_hovering())
                            return;
                        tile.set_frame_index(1);
                        entity.dec_health(0.01f);
                    }

                    @Override
                    public void on_leave(Tile tile, Entity entity) {
                        if (entity.is_hovering())
                            return;
                        tile.set_frame_index(0);
                    }
                }, (char) i);
            } else if (tileInstances_arr[i].get_type() == TileInstance.TYPE_TRAPDOOR) {
                map.attach_events_global(new TileEvent() {
                    @Override
                    public void on_step(Tile tile, Entity entity) {
                        if (entity.is_hovering())
                            return;
                        tile.set_solid(false);
                        tile.set_frame_index(1);
                    }

                    @Override
                    public void on_leave(Tile tile, Entity entity) {
                        if (entity.is_hovering())
                            return;
                        tile.set_solid(true);
                        tile.set_frame_index(0);
                    }
                }, (char) i);
            }
        }

        map.get_light_map().set_ambient_light(ambient_light);

        physics = new Physics(map, gravity, max_gravity);
        clock = new GameClock(16, 1024);
        player.setMap(map);
        player.set_max_velocity(5);
        player.attach_event(new EntityEvent() {
            @Override
            public void on_collision(Entity entity, Entity entity2) {
                if ((entity2 instanceof Enemy))
                    entity2.dec_health(0.1f);
            }
        });

        Enemy enemy_arr[] = new Enemy[enemies.size()];

        for (int i = 0; i < enemy_arr.length; i++) {
            enemy_arr[i] = enemies.get(i);
            enemy_arr[i].set_map(map);
            enemy_arr[i].hovers(true);
            enemy_arr[i].attach_to_game_clock(clock);
            enemy_arr[i].attach_event(new EntityEvent() {
                @Override
                public void on_collision(Entity entity, Entity entity2) {
                    if (entity2 instanceof Enemy) {
                        // bump effect should change direction still buggy
                        return;
                    }
                    // maybe fight animation
                    entity2.dec_health(0.05f);
                }
            });
            enemy_arr[i].hovers(true);
            physics.add_entity(enemy_arr[i]);
        }

        physics.attach_to_game_clock(clock);

        Platform platform_arr[] = new Platform[platforms.size()];

        for (int i = 0; i < platform_arr.length; i++) {
            platform_arr[i] = platforms.get(i);
            platform_arr[i].attach_to_game_clock(clock);
            platform_arr[i].set_map(map);
            physics.add_entity(platform_arr[i]);
        }

        physics.add_entity(player);
        map.attach_to_game_clock(clock);

        return new LoadedMap(map, tileInstances_arr, player, physics, clock, hearts, enemy_arr, platform_arr);
    }

    static public class LoadedMap {

        public LoadedMap(Map map, TileInstance tileInstance[], Entity player, Physics physics, GameClock clock, Entity hearts[], Enemy enemies[], Platform platforms[]) {
            this.map = map;
            this.tileInstance = tileInstance;
            this.player = player;
            this.physics = physics;
            this.clock = clock;
            this.hearts = hearts;
            this.enemies = enemies;
            this.platforms = platforms;
        }

        public Map map;
        public TileInstance tileInstance[];
        public Entity player;
        public Physics physics;
        public GameClock clock;
        public Entity hearts[];
        public Enemy enemies[];
        public Platform platforms[];
    }
}
