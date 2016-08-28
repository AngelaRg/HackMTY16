package mx.itesm.hackmty2016;

/**
 * Created by Ovidio on 8/27/2016.
 */
public class Vector2D {
    private float x;
    private float y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void multConst(float c) {
        x *= c;
        y *= c;
    }

    public void divConst(float c) {
        x /= c;
        y /= c;
    }

    public void sum(Vector2D v) {
        x += v.getX();
        y += v.getY();
    }

    public void substract(Vector2D v) {
        x -= v.getX();
        y -= v.getY();
    }

    public float getX(){
        return x;
    }
    public float getY() {
        return y;
    }
    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
}