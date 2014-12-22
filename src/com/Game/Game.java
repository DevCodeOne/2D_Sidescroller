package com.Game;

import com.Game.GameMechanic.Player;
import com.Game.Graphics.Display;
import com.Game.Graphics.PixGraphics;
import com.Game.Graphics.Pixmap;
import com.Game.Input.InputListener;
import com.Game.Input.KeyboardHandler;
import com.Game.Resources.MapLoader;
import com.Game.Resources.ResourceLoader;

import java.awt.event.KeyEvent;

public class Game extends Display implements InputListener {

    public static MapLoader.LoadedMap map;
    static KeyboardHandler handler;
    static PixGraphics pixgraphics;
    static String actions = "";

    public Game(int width, int height, int resx, int resy) {
        super(width, height, resx, resy);
        init();
    }

    // TODO init game + connect to different server

    public void init(String ip, int port) {
        pixgraphics = new PixGraphics(getPixmap());
        pixgraphics.load_font("res/font.png", 8);
        // connect to server
        // Load map from server + missing resources
    }

    public void init() { // init game + server
        pixgraphics = new PixGraphics(getPixmap());
        pixgraphics.load_font("res/font.png", 8);
        map = MapLoader.loadMap(ResourceLoader.load_text_file("res/Map.map"));
        attach_to_game_clock(map.clock);
        handler = new KeyboardHandler(this);
        handler.set_listener(this);
        handler.attach_to_game_clock(map.clock);
        map.clock.start_clock();
    }

    @Override
    public void draw_graphics(Pixmap graphics) {
        graphics.clear(20 << 16 | 28 << 8 | 31);
        map.map.draw_map(graphics);
        ((Player)map.player).draw(pixgraphics, map.map);
        for (int i = 0; i < map.platforms.length; i++)
            map.platforms[i].draw(graphics, map.map);
        for (int i = 0; i < map.enemies.length; i++)
            map.enemies[i].draw(graphics, map.map);
        for (int i = 0; i < map.hearts.length; i++)
            map.hearts[i].draw(graphics);

        // fps
        pixgraphics.draw_string(Integer.toString(get_fps()), 20, 40);
        pixgraphics.draw_string("Fps", 50, 40);
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
        while (actions.length() > 0) {
            switch (actions.charAt(actions.length() -1)) {
                case 'S' :
                    sprint = true;
                    break;
                case 'D' :
                    if (map.player.is_on_ground())
                        map.player.walk(0.75f);
                    else
                        map.player.walk(0.25f);
                    if (System.currentTimeMillis() - map.player.frame_last_changed() > 75) {
                        int anim = (map.player.get_frame_index() + 1) % 3;
                        map.player.set_frame_index(anim);
                    }
                    if (map.player.is_flipped())
                        map.player.flip_vertically();
                    break;
                case 'A' :
                    if (map.player.is_on_ground())
                        map.player.walk(-0.75f);
                    else
                        map.player.walk(-0.25f);
                    if (System.currentTimeMillis() - map.player.frame_last_changed() > 75) {
                        map.player.set_frame_index((map.player.get_frame_index() + 1) % 3);
                    }
                    if (!map.player.is_flipped())
                        map.player.flip_vertically();
                    break;
                case 'J' :
                    map.player.jump(10, -1.25f);
                    break;
            }
            actions = actions.substring(0, actions.length() - 1);
        }

        double life = (map.player.get_health() / map.player.get_health_stat()) * map.hearts.length;
        for (int i = 0; i < map.hearts.length; i++) {
            if (life >= 1)
                map.hearts[i].set_frame_index(0);
            else if (life >= 0.5f)
                map.hearts[i].set_frame_index(1);
            else
                map.hearts[i].set_frame_index(2);
            life--;
        }

        if (map.player.get_y() + map.map.get_offy() > (get_resy() - (get_resy() / 5)))
            map.map.scroll_by(0, 5);
        if (map.player.get_y() + map.map.get_offy() < (get_resy() / 5))
            map.map.scroll_by(0, -5);
        map.map.scroll_by(((map.player.get_int_x() + map.map.get_offx()) - (get_resx() >> 1)), 0);
        if (!map.player.is_on_ground() && map.player.get_velocity_y() <= 0)
            map.player.set_frame_index(4);
    }

    public int tick_skip() {
        return 0;
    }
}
