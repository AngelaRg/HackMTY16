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
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Vector;

import java.util.ArrayList;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static int WIDTH = 856;
    public static int HEIGHT = 480;
    public static final int NUM_ENEMIES = 10;
    public static final int NUM_HEARTS = 2;
    public static final int NUM_MISSILES = 2;

    private MainThread thread;
    private Background bg;
    public Player player;
    private Enemy enemy;
    private ArrayList<Enemy> enemies;
    private Heart heart;
    private ArrayList<Heart> hearts;
    private ArrayList<Projectile> shots;
    private ArrayList<MiniMissile> missileCount;

    private boolean gameOver;
    private boolean beforeStart;

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

        gameOver = false;
        beforeStart = true;
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

        hearts = new ArrayList<Heart>();
        for (int i = 1; i <= NUM_HEARTS; i++ ) {
            Bitmap heartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
            heart = new Heart(heartBitmap, heartBitmap.getWidth(), heartBitmap.getHeight(), 1, i*100);
            hearts.add(heart);
        }

        missileCount = new ArrayList<>();
        for(int i=1; i<=NUM_MISSILES; i++) {
            Bitmap miniMissileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.minimagic);
            MiniMissile mm = new MiniMissile(miniMissileBitmap, miniMissileBitmap.getWidth(), miniMissileBitmap.getHeight(), 1, i*100);
            missileCount.add(mm);
        }

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (beforeStart){
            player.setPlaying(true);
            beforeStart = false;
        } else if(gameOver){
            resetGame();
        } else if(shots.size() < NUM_MISSILES) {
            float touchX = event.getX();
            float touchY = event.getY();
            float playerX = player.getVectorPosition().getX();
            float playerY = player.getVectorPosition().getY();

            float distX = (float)Math.sqrt(Math.pow((touchX - playerX),2) + Math.pow((playerY - playerY), 2));
            float distY = (float)Math.sqrt(Math.pow((playerX - playerX),2) + Math.pow((touchY - playerY), 2));
            float velX = distX/30.0f;
            float velY = distY/30.0f;

            if(touchX-playerX < 0) {
                velX = (-1.0f)*velX;
            }

            //float slope = (-1.0f)*Math.abs(touchY - playerY)/(touchX - playerX);
            Bitmap missile = BitmapFactory.decodeResource(getResources(), R.drawable.magic2);
            Projectile shot = new Projectile(missile, missile.getWidth()/3, missile.getHeight(), 3, playerX, playerY, velX, velY);
            shots.add(shot);
            if(missileCount.size()-1 >= 0)
                missileCount.remove(missileCount.size()-1);
        }

        return super.onTouchEvent(event);
    }



    public void update() {
        if(player.getPlaying()) {
            bg.update();
            player.update();
            Bitmap miniMissileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.minimagic);
            MiniMissile mm = new MiniMissile(miniMissileBitmap, miniMissileBitmap.getWidth(), miniMissileBitmap.getHeight(), 1, (missileCount.size()+1)*100);
            for (Enemy en: enemies){
                en.update();

                for(Projectile shot: shots) {
                    if(collision(en, shot)) {
                        shots.remove(shot);
                        enemies.remove(en);
                        missileCount.add(mm);
                        Bitmap enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
                        enemy = new Enemy(enemyBitmap, enemyBitmap.getWidth()/3, enemyBitmap.getHeight(), 3);
                        enemies.add(enemy);
                    }
                }
            }
            for (Projectile shot: shots) {
                shot.update();
                float x = shot.getVectorPosition().getX();
                float y = shot.getVectorPosition().getY();

                if(x<0 || x>WIDTH) {
                    shots.remove(shot);
                    missileCount.add(mm);
                } else {
                    if (y<0 || y>HEIGHT) {
                        shots.remove(shot);
                        missileCount.add(mm);
                    }
                }

            }
        }

    }

    public void checkColision(){
        if (player.getPlaying()){
            for (Enemy en: enemies){
                if (collision(en,player)){
                    enemies.remove(en);
                    // agregar nuevo enemigo
                    Bitmap enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
                    enemy = new Enemy(enemyBitmap, enemyBitmap.getWidth()/3, enemyBitmap.getHeight(), 3);
                    enemies.add(enemy);

                    player.setLifes(player.getLifes() - 1);
                    hearts.remove(hearts.size()-1);
                    if (player.getLifes() == 0){
                        endGame();
                    }
                }
            }
        }

    }

    public void endGame(){
        enemies.clear();
        player.setPlaying(false);
        gameOver = true;
    }

    public void resetGame(){
        gameOver = false;
        // resetear player
        player.setPlaying(true);
        player.setLifes(2);
        player.resetScore();
        player.setStartTime(System.nanoTime());
        player.getVectorPosition().setX(100);
        // resetear listas
        enemies = new ArrayList<Enemy>();
        shots = new ArrayList<>();
        for (int i=0; i < NUM_ENEMIES; i++) {
            Bitmap enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
            enemy = new Enemy(enemyBitmap, enemyBitmap.getWidth()/3, enemyBitmap.getHeight(), 3);
            enemies.add(enemy);
        }

        hearts = new ArrayList<Heart>();
        for (int i = 1; i <= NUM_HEARTS; i++ ) {
            Bitmap heartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
            heart = new Heart(heartBitmap, heartBitmap.getWidth(), heartBitmap.getHeight(), 1, i*100);
            hearts.add(heart);
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
            if(player.getPlaying()) {
                player.draw(canvas);
                for (Enemy en: enemies){
                    en.draw(canvas);
                }
                for (Heart ha: hearts) {
                    ha.draw(canvas);
                }
                for (Projectile shot: shots){
                    shot.draw(canvas);
                }
                if(!missileCount.isEmpty()) {
                    for (MiniMissile mm : missileCount) {
                        mm.draw(canvas);
                    }
                }
            }
            if (beforeStart){
                Bitmap menuBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu2);
                canvas.drawBitmap(menuBitmap, WIDTH / 4 - menuBitmap.getWidth() / 8, 0, null);
            }
            if(gameOver){
                Bitmap goBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gameover2);
                canvas.drawBitmap(goBitmap, WIDTH / 4 - goBitmap.getWidth() / 8, 0, null);
            }
            canvas.restoreToCount(savedState);

        }
    }



}