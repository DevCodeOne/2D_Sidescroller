package com.Game;

import com.Game.GameMechanic.*;
import com.Game.Graphics.Display;
import com.Game.Graphics.PixGraphics;
import com.Game.Graphics.Pixmap;
import com.Game.Input.InputListener;
import com.Game.Input.KeyboardHandler;
import com.Game.Timing.GameClock;

import java.awt.event.KeyEvent;

public class Main extends Display implements InputListener {

    static Map map;
    static TileInstance tileInstances[];
    static KeyboardHandler handler;
    static Entity player;
    static Entity hearts[];
    static PixGraphics pixgraphics;
    static Physics physics;
    static Enemy enemy[];
    static GameClock clock;
    static String actions = "N";
    static int count_enemies = 20;

    public Main(int width, int height, int resx, int resy) {
        super(width, height, resx, resy);
    }

    public static void main(String[] args) {
        Main m = new Main(1600, 992, 800, 496);
        pixgraphics = new PixGraphics(m.getPixmap());
        pixgraphics.load_font("res/font.png", 8);
        tileInstances = new TileInstance[7];
        tileInstances[0] = new TileInstance(false, true, Pixmap.load_image("res/brick.png"));
        tileInstances[1] = new TileInstance(false, true, Pixmap.load_image("res/moss_brick.png"));
        tileInstances[2] = new TileInstance(false, false, Pixmap.load_image("res/air.png"));
        tileInstances[3] = new TileInstance(true, false, Pixmap.load_image("res/grass.png"));
        tileInstances[4] = new TileInstance(true, false, new Pixmap[]{Pixmap.load_image("res/trap_hidden.png"), Pixmap.load_image("res/trap_exposed.png")});
        tileInstances[5] = new TileInstance(true, true, new Pixmap[]{Pixmap.load_image("res/trap_door.png"), Pixmap.load_image("res/trap_door2.png")});
        tileInstances[6] = new TileInstance(true, false, new Pixmap[]{Pixmap.load_image("res/torch.png"), Pixmap.load_image("res/torch2.png")});
        map = new Map(Pixmap.load_image("res/map.png"), new int[]{0, (127 << 8 | 14), (~0) & ~(0xFF << 24), 255, 255 << 16, 127 << 16, 148 << 8 | 255}, tileInstances);
        map.set_background_tile((char) 2);
        map.get_light_map().set_light_emitting_global((char) 6, 0.75f, 10);
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
        }, (char) 4);
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
        }, (char) 5);
        map.attach_tick_event_global(new TickTileEvent() {
            public void tick(Tile tile) {
                tile.inc_frame_index();
            }

            public int skip_ticks() {
                return 5;
            }
        }, (char) 6);
        handler = new KeyboardHandler(m);
        handler.set_listener(m);
        player = new Player(new Pixmap[]{
                Pixmap.load_image("res/player.png"), Pixmap.load_image("res/player2.png"), Pixmap.load_image("res/player.png"), Pixmap.load_image("res/player3.png"),
                Pixmap.load_image("res/player4.png")},
                0, 0, 10);
        player.set_max_velocity(5);
        player.attach_event(new EntityEvent() {
            @Override
            public void on_collision(Entity entity, Entity entity2) {
                if ((entity2 instanceof Enemy))
                    entity2.dec_health(0.1f);
            }
        });
        hearts = new Entity[10];
        for (int i = 0; i < 10; i++) {
            hearts[i] = new Entity(new Pixmap[]{Pixmap.load_image("res/heart.png"), Pixmap.load_image("res/half_heart.png"), Pixmap.load_image("res/empty_heart.png")},
                    i * 32 + 5, 10);
        }
        physics = new Physics(map, 0.75f, 7f);
        clock = new GameClock(16, 1024);
        m.attach_to_game_clock(clock);
        handler.attach_to_game_clock(clock);
        physics.attach_to_game_clock(clock);
        map.attach_to_game_clock(clock);
        enemy = new Enemy[count_enemies];
        Pixmap[] pixmap = new Pixmap[]{Pixmap.load_image("res/enemy.png"), Pixmap.load_image("res/enemy2.png"),
                Pixmap.load_image("res/enemy.png"), Pixmap.load_image("res/enemy3.png")};
        Pixmap health = Pixmap.load_image("res/healthbar.png");
        for (int i = 0; i < count_enemies; i++) {
            enemy[i] = new Enemy(pixmap, ((int) (Math.random() * (map.get_width())) + 3) * map.get_tile_width(), 0, 10, 1, map);
            enemy[i].set_health_bar(health);
            enemy[i].set_max_velocity(1);
            enemy[i].attach_event(new EntityEvent() {
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
            enemy[i].hovers(true);
        }
        for (int i = 0; i < count_enemies; i++)
            enemy[i].attach_to_game_clock(clock);
        physics.add_entity(player);
        for (int i = 0; i < count_enemies; i++) {
            physics.add_entity(enemy[i]);
        }
        clock.start_clock();
    }

    @Override
    public void draw_graphics(Pixmap graphics) {
        graphics.clear(20 << 16 | 28 << 8 | 31);
        map.draw_map(graphics);
        player.draw(graphics, map.get_offx(), map.get_offy(), map);
        for (int i = 0; i < count_enemies; i++)
            enemy[i].draw(graphics, map.get_offx(), map.get_offy(), map);
        for (int i = 0; i < 10; i++) {
            hearts[i].draw(graphics);
        }
        pixgraphics.set_color(255 << 16 | 255 << 8 | 255);
        pixgraphics.draw_string_centered("Player", (int) player.get_x() + map.get_offx() + player.get_width() / 2, (int) player.get_y() + map.get_offy() - 10);
        pixgraphics.draw_string(Integer.toString(get_fps()), 20, 40);
        pixgraphics.draw_string("Fs", 50, 40);
    }

    @Override
    public void handle_keys(boolean[] keys) {
        if (keys[KeyEvent.VK_SPACE])
            actions = actions.concat("J");

        if (keys[KeyEvent.VK_D]) {
            actions = actions.concat("D");
        } else if (keys[KeyEvent.VK_A]) {
            actions = actions.concat("A");
        }

        if (keys[KeyEvent.VK_SHIFT])
            actions = actions.concat("S");
        if (keys[KeyEvent.VK_ESCAPE])
            System.exit(0);
    }

    public void on_tick() {
        boolean sprint = false;
        if (!(actions.length() > 1)) {
            player.set_frame_index(0);
            if (player.is_on_ground())
                if (Math.abs(player.get_velocity_x()) > 1f) {
                    if (player.get_velocity_x() > 0)
                        player.inc_velocity_x(-1f);
                    else
                        player.inc_velocity_x(1f);
                } else
                    player.set_velocity_x(0);
        }
        if (actions.length() > 1 && actions.charAt(actions.length() - 1) == 'S') {
            actions = actions.substring(0, actions.length() - 1);
            sprint = true;
        }
        if (actions.length() > 1 && actions.charAt(actions.length() - 1) == 'D') {
            actions = actions.substring(0, actions.length() - 1);
            if (player.is_on_ground())
                player.walk(!sprint ? 0.5f : 1.0f);
            else
                player.walk(0.25f);
            if (System.currentTimeMillis() - player.frame_last_changed() > 75) {
                int anim = (player.get_frame_index() + 1) % 3;
                player.set_frame_index(anim);
            }
            if (player.is_flipped())
                player.flip_vertically();
        } else if (actions.length() > 1 && actions.charAt(actions.length() - 1) == 'A') {
            actions = actions.substring(0, actions.length() - 1);
            if (player.is_on_ground())
                player.walk(!sprint ? -0.5f : -1.0f);
            else
                player.walk(-0.25f);
            if (System.currentTimeMillis() - player.frame_last_changed() > 75) {
                int anim = (player.get_frame_index() + 1) % 3;
                player.set_frame_index(anim);
            }
            if (!player.is_flipped())
                player.flip_vertically();
        }
        if (actions.length() > 1 && actions.charAt(actions.length() - 1) == 'J') {
            actions = actions.substring(0, actions.length() - 1);
            player.jump(10, -1.25f);
        }
        float life = player.get_health();
        for (int i = 0; i < 10; i++) {
            if (life >= 1)
                hearts[i].set_frame_index(0);
            else if (life >= 0.5f)
                hearts[i].set_frame_index(1);
            else
                hearts[i].set_frame_index(2);
            life--;
        }

        if (player.get_y() > -map.get_offy() + get_resy() - 150)
            map.scroll_by(0, 6);
        if (player.get_y() < -map.get_offy() + 150)
            map.scroll_by(0, -6);
        map.scroll_by((int) ((player.get_x() + map.get_offx()) - (get_resx() >> 1)), 0);
        if (!player.is_on_ground() && player.get_velocity_y() <= 0)
            player.set_frame_index(4);
    }

    public int tick_skip() {
        return 0;
    }
}
