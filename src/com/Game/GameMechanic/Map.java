package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;
import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;

import java.util.ArrayList;

public class Map implements Tick {

    public static final char ON_STEP = 0x1;
    public static final char ON_LEAVE = 0x2;
    private Tile map_val[][];
    private TileInstance tileInstances[];
    private int width, height;
    private int tile_width, tile_height;
    private char background_tile_id;
    private float offx, offy;
    private ArrayList<Tile> tick_event_handler;
    private int skip_ticks[];
    private LightMap map;

    public Map(Pixmap map, int look_up[], TileInstance tileInstances[]) {
        this.width = map.get_width();
        this.height = map.get_height();
        this.tile_width = tileInstances[0].get_pixmap().get_width();
        this.tile_height = tileInstances[0].get_pixmap().get_height();
        this.map_val = new Tile[width][height];
        this.tileInstances = tileInstances;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = map.get_pixel(i, j) & ~(0xFF << 24);
                for (int k = 0; k < look_up.length; k++) {
                    if (pixel == look_up[k]) {
                        map_val[i][j] = new Tile((char) k, tileInstances[(char) k].is_transparent(), tileInstances[(char) k].is_solid(), tileInstances[(char) k].get_frames_count());
                    }
                }
                if (map_val[i][j] == null)
                    System.out.println("No TileInstance found for " + Integer.toHexString(map.get_pixel(i, j) & ~(0xFF << 24)) + " @ " + i + ", " + j);
            }
        }
        this.tick_event_handler = new ArrayList<Tile>(width * height);
        this.skip_ticks = new int[width * height];
        this.map = new LightMap(this);
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
    }

    public void scroll_by(float x, float y) {
        offx  -= x;
        offy  -= y;
    }

    public void draw_map(Pixmap pixmap) {
        int tile_width = tileInstances[0].get_pixmap().get_width();
        int tile_height = tileInstances[0].get_pixmap().get_height();
        int startx = -(int)(offx)  / tile_width;
        int starty = -(int)(offy)  / tile_height;
        int offxpix = (int)(offx)  % tile_width;
        int offypix = (int)(offy)  % tile_height;
        int size_x = (pixmap.get_width() / tile_width) + 1;
        int size_y = (pixmap.get_height() / tile_height) + 1;
        if (startx + size_x >= this.width)
            size_x = this.width - startx;
        if (starty + size_y >= this.height)
            size_y = this.height - starty;
        map.render_light_map();
        Tile tile;
        for (int i = startx < 0 ? -startx : 0; i < size_x; i++) {
            for (int j = starty < 0 ? -starty : 0; j < size_y; j++) {
                tile = map_val[startx + i][(starty + j)];
                if (tile.is_transparent()) {
                    pixmap.blit(tileInstances[background_tile_id].get_pixmap(), offxpix + i * tile_width, offypix + j * tile_height, tile.get_brightness(), false);
                    pixmap.blit(tileInstances[tile.get_id()].get_frame(tile.get_frame_index()), offxpix + i * tile_width, offypix + j * tile_height, tile.get_brightness(), true);
                    continue;
                }
                pixmap.blit(tileInstances[tile.get_id()].get_frame(tile.get_frame_index()), offxpix + i * tile_width, offypix + j * tile_height, tile.get_brightness(), false);
            }
        }
    }

    public void toggle_events(Entity entity, char events) {
        int startx_l = (int) ((entity.get_x() - (entity.get_width() >> 1)) / get_tile_width());
        int startx_r = (int) (((entity.get_x() + (entity.get_width() >> 1)) / get_tile_width()));
        int starty_lr = (int) (((entity.get_y() - entity.get_height()) / get_tile_height()) + 1);
        int endy_lr = (int) (((entity.get_y() + entity.get_height()) / get_tile_height()) + .5f);
        for (int i = starty_lr; i <= endy_lr; i++) {
            for (int j = startx_l; j <= startx_r; j++) {
                if (get_data(j, i).has_events()) {
                    if ((events & ~ON_STEP) == 0)
                        get_data(j, i).call_on_step(entity);
                    else if ((events & ~ON_LEAVE) == 0)
                        get_data(j, i).call_on_leave(entity);
                }
            }
        }
    }

    public void attach_events_global(TileEvent event, char id) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map_val[i][j].get_id() == id) {
                    map_val[i][j].attach_events(event);
                }
            }
        }
    }

    public void attach_tick_event_global(TickTileEvent event, char id) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map_val[i][j].get_id() == id) {
                    map_val[i][j].attach_tick_events(event);
                    tick_event_handler.add(map_val[i][j]);
                }
            }
        }
    }

    public Tile get_data(int x, int y) {
        return map_val[(x > 0 && x < width) ? x : x <= 0 ? 0 : width - 1][(y >= 0 && y < height) ? y : y < 0 ? 0 : height - 1];
    }

    public TileInstance get_tile_instance(char id) {
        return tileInstances[id];
    }

    public void set_background_tile(char id) {
        background_tile_id = (char) 2;
    }

    public int get_offx () {
        return (int)(offx + 0.5f);
    }

    public int get_offy () {
        return (int)(offy + 0.5f);
    }

    public int get_width() {
        return width;
    }

    public int get_height() {
        return height;
    }

    public int get_tile_width() {
        return tile_width;
    }

    public int get_tile_height() {
        return tile_height;
    }

    @Override
    public void tick() {
        for (int i = 0; i < tick_event_handler.size(); i++)
            if (tick_event_handler.get(i).get_tick_event().skip_ticks() == skip_ticks[i]) {
                tick_event_handler.get(i).call_on_tick();
                skip_ticks[i] = 0;
            } else
                skip_ticks[i]++;
    }

    @Override
    public int tick_skip() {
        return 0;
    }

    public LightMap get_light_map() {
        return map;
    }
}
