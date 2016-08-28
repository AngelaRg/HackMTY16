package mx.itesm.hackmty2016;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Cesar on 8/27/2016.
 */
public class Enemy extends GameObject{
    private Bitmap spritesheet;
    private Animation animation = new Animation();
    private int speed;

    public Enemy(Bitmap res, int w, int h, int numFrames){
        this.vectorPosition = new Vector2D();
        Random rand = new Random();
        int randomNum = rand.nextInt(((GamePanel.WIDTH-w) - 1) + 1) + 1;
        this.vectorPosition.setX(randomNum);

        height = h;
        width = w;

        randomNum = rand.nextInt((10 - 2) + 1) + 2;
        speed = randomNum;

        // direccion en x,y
        randomNum = rand.nextInt((1 - (-1)) + 1) - 1;
        dx = 1;
        randomNum = rand.nextInt((1 - (-1)) + 1) - 1;
        dy = -1;

        this.vectorPosition.setY(height);
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

    public void update(){
        moveEnemy();
        animation.update();
    }

    public void moveEnemy(){
        int x = (int)this.vectorPosition.getX();
        int y = (int)this.vectorPosition.getY();
        // revisar si se debe cambiar direccion
        // eje x
        if ( (x+width) > GamePanel.WIDTH || x <= 0){
            dx*= -1;
        }
        // eje y
        if ( (y+height) > GamePanel.HEIGHT || y <= 0){
            dy*= -1;
        }

        // actualizar posicion
        this.vectorPosition.setX(this.vectorPosition.getX() + (speed*dx));
        this.vectorPosition.setY(this.vectorPosition.getY() + (speed*dy));
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), this.vectorPosition.getX(), this.vectorPosition.getY(), null);
    }
}
