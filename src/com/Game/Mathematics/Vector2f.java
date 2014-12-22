package com.Game.Mathematics;

public class Vector2f {

    private double x, y;

    public Vector2f(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vector2f) {
        this(vector2f.x, vector2f.y);
    }

    public Vector2f() {
        this(0, 0);
    }

    public void setTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setTo(Vector2f vector2f) {
        setTo(vector2f.x, vector2f.y);
    }

    public void add(double x, double y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    public void sub(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void mult(double x, double y) {
        this.x *= x;
        this.y *= y;
    }

    public void div(double x, double y) {
        this.x /= x;
        this.y /= y;
    }

    public void add(Vector2f vector2f) {
        add(vector2f.x, vector2f.y);
    }

    public void sub(Vector2f vector2f) {
        sub(vector2f.x, vector2f.y);
    }

    public void div(Vector2f vector2f) {
        div(vector2f.x, vector2f.y);
    }

    public void negate() {
        mult(-1, -1);
    }

    public double get_dot_product(Vector2f vector2f) {
        return x * vector2f.x + y * vector2f.y;
    }

    public double get_len() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double len = get_len();
        div(len, len);
    }

    public double get_x() {
        return x;
    }

    public double get_y() {
        return y;
    }

    public String toString() { return "[ " + x + ", " + y + " ]"; }
}
