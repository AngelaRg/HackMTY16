package mx.itesm.hackmty2016;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by hlg on 27/08/16.
 */
public class Projectile extends GameObject {
    float vX, vY;
    int height;
    int width;
    private Bitmap spritesheet;
    private Animation animation = new Animation();

    public Projectile (Bitmap res, int w, int h, int numFrames, float initX, float initY, float velX, float velY) {
        this.vectorPosition = new Vector2D(initX, initY);
        width = w;
        height = h;
        vX = velX;
        vY = velY;
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
        float adjustFactor = 1.0f;

        vectorPosition.setX(vectorPosition.getX()+vX);
        vectorPosition.setY(vectorPosition.getY()-vY);


    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), this.vectorPosition.getX(),  this.vectorPosition.getY(),null);
    }
}
