package mx.itesm.hackmty2016;

import android.graphics.Bitmap;

/**
 * Created by hlg on 27/08/16.
 */
public class Projectile extends GameObject {
    int height;
    int width;
    float slope;

    public Projectile (Bitmap res, int w, int h, float m, int numFrames, float initX, float initY) {
        this.vectorPosition = new Vector2D(initX, initY);
        width = w;
        height = h;
        slope = m;
    }

    public void update() {
        vectorPosition.setX(vectorPosition.getX()+this.slope);
        vectorPosition.setY(vectorPosition.getY()+this.slope);
    }

    public void setSlope(float m) {
        this.slope = m;
    }
}
