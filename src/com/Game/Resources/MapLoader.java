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

    // TODO Improve the maploader

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

        Scanner scanner = new Scanner(mapFile);
        String line;
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.contains("MAP")) {
                line = scanner.nextLine();
                if (line.contains("image")) {
                    level = ResourceLoader.load_image(line.split(":")[1].trim());
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
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("rgb_index")) {
                        rgb_index = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("index")) {
                        index = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("transparent")) {
                        transparent = Boolean.parseBoolean(args[i].split(":")[1].trim());
                    } else if (args[i].contains("solid")) {
                        solid = Boolean.parseBoolean(args[i].split(":")[1].trim());
                    } else if (args[i].contains("image")) {
                        String image_array[] = args[i].split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (args[i].contains("type")) {
                        type = Integer.parseInt(args[i].split(":")[1].trim());
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
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("posx")) {
                        posx = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("posy")) {
                        posy = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("image")) {
                        String image_array[] = args[i].split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (args[i].contains("health")) {
                        health = Float.parseFloat(args[i].split(":")[1].trim());
                    } else if (args[i].contains("max_velocity")) {
                        max_velocity = Float.parseFloat(args[i].split(":")[1].trim());
                    }
                }
                player = new Player(textures, posx, posy, health, null);
                player.set_max_velocity(max_velocity);
            } else if (line.contains("PHYSICS")) {
                line = scanner.nextLine();
                String args[] = line.split(";");
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("max_gravity")) {
                        max_gravity = Float.parseFloat(args[i].split(":")[1].trim());
                    } else if (args[i].contains("gravity")) {
                        gravity = Float.parseFloat(args[i].split(":")[1].trim());
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
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("posx")) {
                        posx = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("posy")) {
                        posy = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("image")) {
                        String image_array[] = args[i].split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (args[i].contains("health_bar")) {
                        health_bar = ResourceLoader.load_image(args[i].split(":")[1].trim());
                    } else if (args[i].contains("health")) {
                        health = Float.parseFloat(args[i].split(":")[1].trim());
                    } else if (args[i].contains("velocity")) {
                        velocity = Float.parseFloat(args[i].split(":")[1].trim());
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
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("image")) {
                        String image_array[] = args[i].split(":")[1].trim().split(" ");
                        textures = new Pixmap[image_array.length];
                        for (int j = 0; j < image_array.length; j++) {
                            textures[j] = ResourceLoader.load_image(image_array[j]);
                        }
                    } else if (args[i].contains("count")) {
                        count = Integer.parseInt(args[i].split(":")[1].trim());
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
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("image")) {
                        texture = ResourceLoader.load_image(args[i].split(":")[1].trim());
                    } else if (args[i].contains("startx")) {
                        startx = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("starty")) {
                        starty = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("destx")) {
                        destx = Integer.parseInt(args[i].split(":")[1].trim());
                    } else if (args[i].contains("desty")) {
                        desty = Integer.parseInt(args[i].split(":")[1].trim());
                    }
                }
                Platform platform = new Platform(texture, startx, starty, destx, desty, null);
                platforms.add(platform);
            }
        }
        int look_up[] = new int[tileInstances.size()];
        TileInstance tileInstances_arr[] = new TileInstance[tileInstances.size()];
        for (int i = 0; i < tileInstances.size(); i++) {
            look_up[tileInstances.get(i).get_index()] = tileInstances.get(i).get_rgb_index();
            tileInstances_arr[tileInstances.get(i).get_index()] = tileInstances.get(i);
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
                        tile.inc_frame_index();
                        tile.set_emiting_light(tile.get_emiting_light() == 0.5f ? 0.45f : 0.5f);
                    }

                    public int skip_ticks() {
                        return 10;
                    }
                }, (char) i);
                map.get_light_map().set_light_emitting_global((char) i, 0.5f, 5);
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

        physics = new Physics(map, gravity, max_gravity);
        clock = new GameClock(15, 1024);
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

        Platform platform_arr[] = new Platform[platforms.size()];

        for (int i = 0; i < platform_arr.length; i++) {
            platform_arr[i] = platforms.get(i);
            platform_arr[i].attach_to_game_clock(clock);
            platform_arr[i].set_map(map);
            physics.add_entity(platform_arr[i]);
        }

        physics.add_entity(player);
        physics.attach_to_game_clock(clock);
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
