package com.Game.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

public class Pixmap {

    public static final int COLOR_TRANSPARENT = 238 << 16 | 255;
    private int pixels[];
    private int off[];
    private int width, height;

    public Pixmap(BufferedImage render_target) {
        this.width = render_target.getWidth();
        this.height = render_target.getHeight();
        this.pixels = ((DataBufferInt) render_target.getRaster().getDataBuffer()).getData();
        this.off = new int[height];
        for (int i = 0; i < height; i++) {
            off[i] = i * width;
        }
    }

    public static Pixmap load_image(String src) {
        try {
            BufferedImage image = ImageIO.read(Pixmap.class.getResource(src));
            BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics().drawImage(image, 0, 0, null);
            Pixmap pixmap = new Pixmap(tmp);
            return pixmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void blit(Pixmap pixmap, int x, int y) {
        int endx = Math.min(x + pixmap.width, this.width);
        int endy = Math.min(y + pixmap.height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                pixels[(i + x) + off[j + y]] = pixmap.pixels[i + pixmap.off[j]];
            }
        }
    }

    public void blit_flip_vertically(Pixmap pixmap, int x, int y) {
        int endx = Math.min(x + pixmap.width, this.width);
        int endy = Math.min(y + pixmap.height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                pixels[(i + x) + off[j + y]] = pixmap.pixels[(pixmap.width - 1) - i + pixmap.off[j]];
            }
        }
    }

    public void blit(Pixmap pixmap, int offx, int offy, int width, int height, int x, int y) {
        int endx = Math.min(x + width, this.width);
        int endy = Math.min(y + height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                pixels[(i + x) + off[j + y]] = pixmap.pixels[i + offx + (pixmap.off[j + offy])];
            }
        }
    }

    public void blit_transparent(Pixmap pixmap, int x, int y) {
        int endx = Math.min(x + pixmap.width, this.width);
        int endy = Math.min(y + pixmap.height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                if (pixmap.pixels[i + pixmap.off[j]] != COLOR_TRANSPARENT)
                    pixels[(i + x) + off[j + y]] = pixmap.pixels[i + pixmap.off[j]];
            }
        }
    }

    public void blit_flip_vertically_transparent(Pixmap pixmap, int x, int y) {
        int endx = Math.min(x + pixmap.width, this.width);
        int endy = Math.min(y + pixmap.height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                if (pixmap.pixels[(pixmap.width - 1) - i + pixmap.off[j]] != COLOR_TRANSPARENT)
                    pixels[(i + x) + off[j + y]] = pixmap.pixels[(pixmap.width - 1) - i + pixmap.off[j]];
            }
        }
    }

    public void blit_transparent(Pixmap pixmap, int offx, int offy, int width, int height, int x, int y) {
        int endx = Math.min(x + width, this.width);
        int endy = Math.min(y + height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                if (pixmap.pixels[i + offx + pixmap.off[j + offy]] != COLOR_TRANSPARENT)
                    pixels[(i + x) + off[j + y]] = pixmap.pixels[i + offx + (pixmap.off[j + offy])];
            }
        }
    }

    public void blit_font(Pixmap pixmap, int offx, int offy, int width, int height, int x, int y, int color) {
        int endx = Math.min(x + width, this.width);
        int endy = Math.min(y + height, this.height);
        int size_x = endx - x;
        int size_y = endy - y;
        for (int i = x < 0 ? -x : 0; i < size_x; i++) {
            for (int j = y < 0 ? -y : 0; j < size_y; j++) {
                if (pixmap.pixels[i + offx + pixmap.off[j + offy]] != COLOR_TRANSPARENT)
                    pixels[(i + x) + off[j + y]] = color;
            }
        }
    }

    public void clear() {
        int len = width * height;
        for (int i = 0; i < len; i++) {
            pixels[i] = 0;
        }
    }

    public void clear(int color) {
        int len = width * height;
        for (int i = 0; i < len; i++) {
            pixels[i] = color;
        }
    }

    public void set_pixel(int color, int x, int y) {
        pixels[x + off[y]] = color;
    }

    public int get_pixel(int x, int y) {
        return pixels[x + off[y]];
    }

    public int get_width() {
        return width;
    }

    public int get_height() {
        return height;
    }
}
