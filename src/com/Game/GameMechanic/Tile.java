package com.Game.GameMechanic;

public class Tile {
    private char id;
    private boolean is_solid;
    private boolean is_transparent;
    private TileEvent event;
    private TickTileEvent tick_event;
    private boolean has_events;
    private boolean has_tick_events;
    private boolean emits_light;
    private int frame_index;
    private int max_frames;
    private int radius;
    private float brightness;
    private float emiting_light;

    public Tile(char id, boolean transparent, boolean solid, int max_frames) {
        this.id = id;
        this.is_solid = solid;
        this.is_transparent = transparent;
        this.max_frames = max_frames;
    }

    public void call_on_step(Entity entity) {
        if (has_events)
            event.on_step(this, entity);
    }

    public void call_on_leave(Entity entity) {
        if (has_events)
            event.on_leave(this, entity);
    }

    public void call_on_tick() {
        if (has_tick_events)
            tick_event.tick(this);
    }

    public void attach_events(TileEvent event) {
        this.event = event;
        this.has_events = true;
    }

    public void attach_tick_events(TickTileEvent event) {
        this.tick_event = event;
        this.has_tick_events = true;
    }

    public void emits_light(boolean val) {
        this.emits_light = val;
    }

    public void inc_frame_index() {
        frame_index = (frame_index + 1) % max_frames;
    }

    public char get_id() {
        return id;
    }

    public boolean has_events() {
        return has_events;
    }

    public boolean has_tick_events() {
        return has_tick_events;
    }

    public boolean is_transparent() {
        return is_transparent;
    }

    public boolean is_solid() {
        return is_solid;
    }

    public void set_solid(boolean val) {
        is_solid = val;
    }

    public int get_frame_index() {
        return frame_index;
    }

    public void set_frame_index(int val) {
        frame_index = val % max_frames;
    }

    public TickTileEvent get_tick_event() {
        return tick_event;
    }

    public int get_radius() {
        return radius;
    }

    public void set_radius(int val) {
        this.radius = val;
    }

    public boolean emits_light() {
        return emits_light;
    }

    public float get_brightness() {
        return brightness;
    }

    public void set_brightness(float val) {
        this.brightness = Math.min(val, 1);
    }

    public float get_emiting_light() {
        return emiting_light;
    }

    public void set_emiting_light(float val) {
        this.emiting_light = Math.min(val, 1);
    }
}
