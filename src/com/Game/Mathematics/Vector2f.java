package com.Game.Mathematics;

public class Vector2f {

    private float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vector2f) {
        this(vector2f.x, vector2f.y);
    }

    public Vector2f() {
        this(0, 0);
    }

    public void setTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setTo(Vector2f vector2f) {
        setTo(vector2f.x, vector2f.y);
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void sub(float x, float y) {
        this.x -= x;
        this.y -= y;
    }

    public void mult(float x, float y) {
        this.x *= x;
        this.y *= y;
    }

    public void div(float x, float y) {
        this.x /= x;
        this.y /= y;
    }

    public void add(Vector2f vector2f) {
        add(vector2f.x, vector2f.y);
    }

    public void sub(Vector2f vector2f) {
        sub(vector2f.x, vector2f.y);
    }

    public void mult(Vector2f vector2f) {
        mult(vector2f.x, vector2f.y);
    }

    public void div(Vector2f vector2f) {
        div(vector2f.x, vector2f.y);
    }

    public void negate() {
        mult(-1, -1);
    }

    public float get_dot_product(Vector2f vector2f) {
        return x * vector2f.x + y * vector2f.y;
    }

    public float get_len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        float len = get_len();
        div(len, len);
    }

    public float get_x() {
        return x;
    }

    public float get_y() {
        return y;
    }
}
