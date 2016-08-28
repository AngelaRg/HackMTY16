package mx.itesm.hackmty2016;

/**
 * Created by Angela on 27/08/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
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
    public static final int NUM_ENEMIES = 6;

    private MainThread thread;
    private Background bg;
    public Player player;
    private Enemy enemy;
    private ArrayList<Enemy> enemies;
    private Vector<Projectile> shots = new Vector<>();

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
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.scarymusic);
        mp.setLooping(true);
        mp.start();
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

        float touchX = event.getX();
        float touchY = event.getY();
        float playerX = player.getVectorPosition().getX();
        float playerY = player.getVectorPosition().getY();

        float slope = (touchY - playerY)/(touchX - playerX);
        Bitmap missile = BitmapFactory.decodeResource(getResources(), R.drawable.missile);
        Projectile shot = new Projectile(missile, 50, 50, slope, 13, playerX+slope, playerY+slope);
        shots.add(shot);

        return super.onTouchEvent(event);
    }

    public void update() {
        if(player.getPlaying()) {
            bg.update();
            player.update();
            for (Enemy en: enemies){
                en.update();
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
            canvas.restoreToCount(savedState);

        }
    }



}