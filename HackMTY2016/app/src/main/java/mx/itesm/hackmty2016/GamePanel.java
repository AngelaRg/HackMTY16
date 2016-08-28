package mx.itesm.hackmty2016;

/**
 * Created by Angela on 27/08/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Vector;

import java.util.ArrayList;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static int WIDTH = 856;
    public static int HEIGHT = 480;
    public static final int NUM_ENEMIES = 3;

    private MainThread thread;
    private Background bg;
    public Player player;
    private Enemy enemy;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> shots;

    public GamePanel(Context context) {
        super(context);
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        HEIGHT = display.getHeight();
        WIDTH = display.getWidth();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.graveyard);
        bg = new Background(bgBitmap);

        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.magician);
        player = new Player(playerBitmap, playerBitmap.getWidth()/4, playerBitmap.getHeight(), 4);

        enemies = new ArrayList<Enemy>();
        shots = new ArrayList<>();
        for (int i=0; i < NUM_ENEMIES; i++) {
            Bitmap enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
            enemy = new Enemy(enemyBitmap, enemyBitmap.getWidth()/3, enemyBitmap.getHeight(), 3);
            enemies.add(enemy);
        }


        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(shots.size() <= 3) {
            float touchX = event.getX();
            float touchY = event.getY();
            float playerX = player.getVectorPosition().getX();
            float playerY = player.getVectorPosition().getY();

            float vel = (float)Math.sqrt(Math.pow((touchX - playerX),2) + Math.pow((touchY - playerY), 2));

            float slope = (touchY - playerY)/(touchX - playerX);
            Bitmap missile = BitmapFactory.decodeResource(getResources(), R.drawable.magic2);
            Projectile shot = new Projectile(missile, missile.getWidth()/3, missile.getHeight(), slope, 3, playerX, playerY, vel);
            shots.add(shot);
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        if(player.getPlaying()) {
            bg.update();
            player.update();
            for (Enemy en: enemies){
                en.update();
            }
            for (Projectile shot: shots){
                shot.update();
                float x = shot.getVectorPosition().getX();
                float y = shot.getVectorPosition().getY();

                if(x<0 || x>WIDTH) {
                    shots.remove(shot);
                } else {
                    if (y<0 || y>HEIGHT) {
                        shots.remove(shot);
                    }
                }

            }
        }

    }

    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            for (Enemy en: enemies){
                en.draw(canvas);
            }
            for (Projectile shot: shots){
                shot.draw(canvas);
            }
            canvas.restoreToCount(savedState);

        }
    }



}