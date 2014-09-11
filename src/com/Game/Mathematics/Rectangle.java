package com.Game.Mathematics;

public class Rectangle {

    private Vector2f min;
    private Vector2f max;

    public Rectangle(Vector2f min, Vector2f max) {
        this.min = new Vector2f(min);
        this.max = new Vector2f(max);
    }

    public void setTo(Vector2f min, Vector2f max) {
        this.min = new Vector2f(min);
        this.max = new Vector2f(max);
    }

    public void translate(Vector2f vector2f) {
        min.add(vector2f);
        max.add(vector2f);
    }

    public void translate(float x, float y) {
        min.add(x, y);
        max.add(x, y);
    }

    public boolean collides(Rectangle rectangle) {
        if (rectangle.get_max().get_x() < get_min().get_x() || rectangle.get_min().get_x() > get_max().get_x()
                || rectangle.get_max().get_y() < get_min().get_y() || rectangle.get_min().get_y() > get_max().get_y()) {
            return false;
        }
        return true;
    }

    public Vector2f get_min() {
        return min;
    }

    public Vector2f get_max() {
        return max;
    }

    public float get_width() {
        return max.get_x() - min.get_x();
    }

    public float get_height() {
        return max.get_y() - min.get_y();
    }
}
