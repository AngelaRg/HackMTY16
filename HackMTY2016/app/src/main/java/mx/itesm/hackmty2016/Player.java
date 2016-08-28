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
    private int lifes;

    public Player(Bitmap res, int w, int h, int numFrames) {
        this.playing = false;
        this.vectorPosition = new Vector2D();
        this.vectorPosition.setX(100);
        score = 0;
        lifes = 2;
        height = h;
        width = w;
        this.vectorPosition.setY(GamePanel.HEIGHT - height);
        resetBitmap(res, numFrames);
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
            //score++;
            startTime = System.nanoTime();
        }
        animation.update();
    }

    public void movePlayer(int mov){
        if (this.playing){
            // actualizar la posicion
            float pos = this.vectorPosition.getX() + mov;
            if((pos + width < GamePanel.WIDTH) &&  (pos > 0)){
                this.vectorPosition.setX(pos);
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), this.vectorPosition.getX(),  this.vectorPosition.getY(),null);
    }
    public int getScore(){return score;}
    public void setScore(int score) {this.score = score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }
}
