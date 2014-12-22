package com.Game.GameMechanic;

import com.Game.Graphics.Pixmap;
import com.Game.Mathematics.Vector2f;

import java.awt.geom.Rectangle2D;

public class Entity {

    public float max_velocity_x;
    protected Vector2f position;
    protected Vector2f velocity;
    protected Pixmap[] frames;
    protected boolean on_ground;
    protected int frame_index;
    protected long frame_changed;
    protected boolean flip_vertically;
    protected float health;
    protected float health_stat;
    protected boolean is_hovering;
    protected EntityEvent event;
    protected boolean has_events;
    protected boolean ignores_physics;
    protected Rectangle2D.Double rectangle;

    public Entity(Pixmap texture, int x, int y) {
        this(new Pixmap[]{texture}, x, y);
    }

    public Entity(Pixmap[] frames, int x, int y) {
        this.frames = new Pixmap[frames.length];
        System.arraycopy(frames, 0, this.frames, 0, frames.length);
        this.position = new Vector2f(x, y);
        this.velocity = new Vector2f();
        this.rectangle = new Rectangle2D.Double(position.get_x() - (get_width() >> 1), position.get_y(), get_width(), get_height());
    }

    public void draw(Pixmap pixmap, int offx, int offy) {
        if (-offx > position.get_x() || -offy > position.get_y() || -offx + pixmap.get_width() < position.get_x() || -offy + pixmap.get_height() < position.get_y())
            return;
        if (!flip_vertically)
            pixmap.blit(frames[frame_index], (int) (get_int_x() + offx) - (get_pixmap().get_width() >> 1), (get_int_y() + offy), true);
        else
            pixmap.blit_flip_vertically(frames[frame_index], (int) (get_int_x() + offx) - (get_pixmap().get_width() >> 1), (get_int_y() + offy), true);
    }

    public void draw(Pixmap pixmap) {
        if (!flip_vertically)
            pixmap.blit(frames[frame_index], (get_int_x()) - (get_pixmap().get_width() >> 1), get_int_y(), true);
        else
            pixmap.blit_flip_vertically(frames[frame_index], (get_int_x()) - (get_pixmap().get_width() >> 1), get_int_y(), true);
    }

    public void draw(Pixmap pixmap, Map map) {
        int startx = (int) get_x() / map.get_tile_width();
        int starty = (int) get_y() / map.get_tile_height();
        int endx = (int) (get_x() + get_width()) / map.get_tile_width();
        int endy = (int) (get_y() + get_height()) / map.get_tile_height();
        int len = (endx - startx) * (endy - starty);
        int offx = map.get_offx();
        int offy = map.get_offy();
        double brightness = 0;
        for (int i = startx; i < endx; i++)
            for (int j = starty; j < endy; j++) {
                brightness += map.get_data(i, j).get_brightness();
            }
        brightness /= (float) len;
        if (-offx > position.get_x() || -offy > position.get_y() || -offx + pixmap.get_width() < position.get_x() || -offy + pixmap.get_height() < position.get_y())
            return;
        if (!flip_vertically)
            pixmap.blit(frames[frame_index], (int) ((get_int_x() + offx) - (get_pixmap().get_width() >> 1) + 0.5f), (int) (get_int_y() + offy + 0.5f), (float)brightness, true);
        else
            pixmap.blit_flip_vertically(frames[frame_index], (int) ((get_int_x() + offx) - (get_pixmap().get_width() >> 1) + 0.5f), (int) (get_int_y() + offy + 0.5f), (float) brightness, true);
    }

    public void draw_bounding_box(Pixmap pixmap, Map map) {
        int offx = map.get_offx();
        int offy = map.get_offy();
        if (rectangle.getMinX() + offx > 0 && rectangle.getMinX() + offx < pixmap.get_width() && rectangle.getMinY() + offy > 0 && rectangle.getMinY() + offy < pixmap.get_height())
            pixmap.set_pixel(255 << 16, (int) rectangle.getMinX() + offx, (int) rectangle.getMinY() + offy);
        if (rectangle.getMaxX() + offx > 0 && rectangle.getMaxX() + offx < pixmap.get_width() && rectangle.getMaxY() + offy > 0 && rectangle.getMaxY() + offy < pixmap.get_height())
            pixmap.set_pixel(255 << 16, (int) rectangle.getMaxX() + offx, (int) rectangle.getMaxY() + offy);
    }

    public void set_pos(float x, float y) {
        position.setTo(x, y);
        rectangle.setRect(position.get_x() - (get_width() >> 1), position.get_y(), get_width(), get_height());
    }

    public void change_pos_by(float x, float y) {
        position.setTo(x + position.get_x(), y + position.get_y());
        rectangle.setRect(position.get_x() - (get_width() >> 1), position.get_y(), get_width(), get_height());
    }

    public void hovers(boolean val) {
        is_hovering = val;
    }

    public void set_max_velocity(float val) {
        this.max_velocity_x = val;
    }

    public void jump(int iterations, float factor) {
        if (!on_ground) {
            return;
        }
        on_ground = false;
        inc_velocity_y(iterations * factor);
    }

    public void walk(float x) {
        if (max_velocity_x >= Math.abs(velocity.get_x() + x)) {
            velocity.add(x, 0);
        } else {
            velocity.setTo((velocity.get_x() + x > 0) ? Math.abs(max_velocity_x) : -Math.abs(max_velocity_x), velocity.get_y());
        }
    }

    public void inc_frame_index() {
        frame_changed = System.currentTimeMillis();
        frame_index = (frame_index + 1) % frames.length;
    }

    public void attach_event(EntityEvent event) {
        this.event = event;
        this.has_events = true;
    }

    public void call_on_collision(Entity entity) {
        event.on_collision(this, entity);
    }

    public void dec_health(double val) {
        health -= val;
    }

    public void inc_health(double val) {
        health += val;
    }

    public void flip_vertically() {
        flip_vertically = !flip_vertically;
    }

    public void inc_velocity_y(float val) {
        velocity.add(0, val);
    }

    public void inc_velocity_x(float val) {
        velocity.add(val, 0);
    }

    public float get_x() {
        return (((int)(position.get_x() * 1000)) / (float)1000);
    }

    public float get_y() {
        return (((int)(position.get_y() * 1000)) / (float)1000);
    }

    public int get_int_x() {
        return (int)(position.get_x() + 0.5f);
    }

    public int get_int_y() {
        return (int)(position.get_y() + 0.5f);
    }

    public int get_width() {
        return frames[frame_index].get_width();
    }

    public int get_height() {
        return frames[frame_index].get_height();
    }

    public float get_velocity_y() {
        return velocity.get_y();
    }

    public void set_velocity_y(float val) {
        velocity.setTo(velocity.get_x(), val);
    }

    public float get_velocity_x() {
        return velocity.get_x();
    }

    public void set_velocity_x(float val) {
        velocity.setTo(val, velocity.get_y());
    }

    public boolean is_on_ground() {
        return on_ground;
    }

    public void set_on_ground(boolean val) {
        on_ground = val;
    }

    public long frame_last_changed() {
        return frame_changed;
    }

    public int get_frame_index() {
        return frame_index;
    }

    public void set_frame_index(int val) {
        frame_changed = System.currentTimeMillis();
        if (val < frames.length)
            frame_index = val;
    }

    public void ignores_physics(boolean val) {
        ignores_physics = val;
    }

    public Pixmap get_pixmap() {
        return frames[0];
    }

    public Pixmap get_frame(int index) {
        return frames[index];
    }

    public boolean is_flipped() {
        return flip_vertically;
    }

    public double get_health() {
        return health;
    }

    public void set_health(float val) {
        health = val;
        health_stat = val;
    }

    public boolean is_hovering() {
        return is_hovering;
    }

    public boolean has_events() {
        return has_events;
    }

    public double get_health_stat() {
        return health_stat;
    }

    public boolean ignores_physics() {
        return ignores_physics;
    }

    public Rectangle2D.Double get_bounds() {
        return rectangle;
    }

}
