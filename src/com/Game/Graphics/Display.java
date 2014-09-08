package com.Game.Graphics;

import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public abstract class Display extends Canvas implements Tick {

    private int width, height;
    private Frame display;
    private BufferedImage offscreen;
    private Pixmap pixmap;
    private long render_time;
    private int fps;
    private int counter;
    private long last_time;

    public Display(int width, int height, int resx, int resy) {
        this.width = width;
        this.height = height;
        setSize(width, height);
        setLocation(0, 0);
        display = new Frame();
        display.setSize(width, height);
        display.setLocationRelativeTo(null);
        display.setLayout(null);
        display.setVisible(true);
        display.setResizable(false);
        display.add(this);
        display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        createBufferStrategy(2);
        offscreen = new BufferedImage(resx, resy, BufferedImage.TYPE_INT_RGB);
        pixmap = new Pixmap(offscreen);
        requestFocus();
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
    }

    private void render(BufferStrategy strategy) {
        if (!strategy.contentsLost()) {
            Graphics g = strategy.getDrawGraphics();
            g.drawImage(offscreen, 0, 0, width, height, null);
            strategy.show();
        }
    }

    public abstract void draw_graphics(Pixmap pixmap);

    @Override
    public void tick() {
        if (System.currentTimeMillis() - last_time >= 1000) {
            last_time = System.currentTimeMillis();
            fps = counter;
            counter = 0;
        }
        long time = System.currentTimeMillis();
        draw_graphics(pixmap);
        render(getBufferStrategy());
        long render_time = System.currentTimeMillis() - time;
        this.render_time = render_time;
        counter++;
        on_tick();
    }

    public abstract void on_tick();

    public int get_resx() {
        return pixmap.get_width();
    }

    public int get_resy() {
        return pixmap.get_height();
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public long get_render_time() {
        return render_time;
    }

    public int get_fps() {
        return fps;
    }
}
