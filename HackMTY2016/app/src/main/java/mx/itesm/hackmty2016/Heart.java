package mx.itesm.hackmty2016;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Angela on 28/08/2016.
 */
public class Heart extends GameObject {
    private Bitmap sprite;
    private Animation animation = new Animation();


    public Heart(Bitmap res, int w, int h, int numFrames, int desplaz) {
        this.vectorPosition = new Vector2D();
        this.vectorPosition.setX(GamePanel.WIDTH - desplaz);

        height = h;
        width = w;

        this.vectorPosition.setY(height/2);
        resetBitmap(res, numFrames);
    }

    public void resetBitmap(Bitmap res, int numFrames) {
        Bitmap[] image = new Bitmap[numFrames];
        sprite = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(sprite, i * width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(80);
    }

    public void update(){
        animation.update();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), this.vectorPosition.getX(), this.vectorPosition.getY(), null);
    }
}