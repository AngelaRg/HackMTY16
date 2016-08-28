package mx.itesm.hackmty2016;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by hlg on 28/08/16.
 */
public class MiniMissile extends GameObject {

    private Bitmap sprite;
    private Animation animation = new Animation();


    public MiniMissile(Bitmap res, int w, int h, int numFrames, int d) {
        this.vectorPosition = new Vector2D();
        this.vectorPosition.setX(GamePanel.WIDTH - d);

        height = h;
        width = w;

        this.vectorPosition.setY(height);
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
