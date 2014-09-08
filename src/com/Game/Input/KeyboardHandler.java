package com.Game.Input;

import com.Game.Timing.GameClock;
import com.Game.Timing.Tick;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements Tick, KeyListener {
    public static final int MAX_KEYS = 256;
    private boolean keys[];
    private InputListener input_listener;

    public KeyboardHandler(InputListener listener) {
        this.input_listener = listener;
        this.keys = new boolean[MAX_KEYS];
    }

    public void attach_to_game_clock(GameClock clock) {
        clock.attach(this);
    }

    public void set_listener(Component component) {
        component.addKeyListener(this);
    }

    public void tick() {
        input_listener.handle_keys(keys);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() < MAX_KEYS)
            keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < MAX_KEYS)
            keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < MAX_KEYS)
            keys[e.getKeyCode()] = false;
    }

    public int tick_skip() {
        return 0;
    }
}
