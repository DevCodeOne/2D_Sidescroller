package com.Game.Timing;

public class GameClock implements Runnable {

    private Tick tick[];
    private int tick_skips[];
    private int index;
    private long delay;
    private boolean interrupted;
    private Thread loop;

    public GameClock(long delay, int max_threads) {
        this.delay = delay;
        this.tick = new Tick[max_threads];
        this.tick_skips = new int[max_threads];
        this.loop = new Thread(this);
    }

    public void attach(Tick tick) {
        this.tick[index] = tick;
        index++;
    }

    public void run() {
        while (!interrupted) {
            try {
                long time = System.currentTimeMillis();
                for (int i = 0; i < index; i++) {
                    if (tick[i].tick_skip() == 0)
                        tick[i].tick();
                    else if (tick[i].tick_skip() == tick_skips[i]) {
                        tick[i].tick();
                        tick_skips[i] = 0;
                    } else
                        tick_skips[i]++;
                }
                long dif = System.currentTimeMillis() - time;
                if (dif < delay)
                    Thread.sleep(delay - dif);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void interrupt() {
        interrupted = true;
    }

    public void start_clock() {
        loop.start();
    }
}
