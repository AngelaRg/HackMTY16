package mx.itesm.hackmty2016;

/**
 * Created by Angela on 27/08/2016.
 */

import android.graphics.Bitmap;
        import android.graphics.Canvas;


public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;

    public Player(Bitmap res, int w, int h, int numFrames) {
        this.playing = true;
        this.vectorPosition = new Vector2D();
        this.vectorPosition.setX(100);
        dy = 0;
        score = 0;
        height = h;
        width = w;
        this.vectorPosition.setY(GamePanel.HEIGHT - height);
        resetBitmap(res,numFrames);
        startTime = System.nanoTime();
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
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
    }

    public void movePlayer(int mov){
        // actualizar la posicion
        this.vectorPosition.setX(this.vectorPosition.getX() + mov);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), this.vectorPosition.getX(),  this.vectorPosition.getY(),null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}
}
