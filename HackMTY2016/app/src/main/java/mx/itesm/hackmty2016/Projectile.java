package mx.itesm.hackmty2016;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by hlg on 27/08/16.
 */
public class Projectile extends GameObject {
    float velocity;
    int height;
    int width;
    float slope;
    private Bitmap spritesheet;
    private Animation animation = new Animation();

    public Projectile (Bitmap res, int w, int h, float m, int numFrames, float initX, float initY, float vel) {
        this.vectorPosition = new Vector2D(initX, initY);
        width = w;
        height = h;
        slope = m;
        velocity = vel;
        resetBitmap(res, numFrames);
    }

    public void resetBitmap(Bitmap res, int numFrames){
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(80);
    }

    public void update() {
        //Random rnum = new Random();
        float adjustFactor = 0.1f;
        if(slope > 0) {
            vectorPosition.setX(vectorPosition.getX()-this.slope*velocity*adjustFactor);
            vectorPosition.setY(vectorPosition.getY()-this.slope*velocity*adjustFactor);
        } else {
            vectorPosition.setX(vectorPosition.getX()-this.slope*velocity*adjustFactor);
            vectorPosition.setY(vectorPosition.getY()+this.slope*velocity*adjustFactor);
        }

    }

    public void setSlope(float m) {
        this.slope = m;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), this.vectorPosition.getX(),  this.vectorPosition.getY(),null);
    }
}
