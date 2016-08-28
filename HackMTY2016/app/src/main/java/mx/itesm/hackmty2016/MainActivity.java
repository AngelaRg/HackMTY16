package mx.itesm.hackmty2016;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.Window;
        import android.view.WindowManager;

import java.util.List;


public class MainActivity extends Activity implements SensorEventListener {

    private Sensor mAccelerometer;
    private GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        gamePanel = new GamePanel(this);

        setContentView(gamePanel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
           // return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    protected  void onResume() { //ACTIVA EL SENSOR Y LANZA ENTRADAS
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE); //obtiene del SO los servicios de sensores
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER); //enlisto los servicios para obtener solo el sensor
        if(sensors.size() > 0 ){ //si mi dispositivo android tiene el acelerometro
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME); //registrarlo al Sensor Manager
        }
    }

    protected  void onPause() {
        //deja de recibir entradas
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onPause();

    }

    protected  void onStop() {
        //deja de recibir entradas
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onStop();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (this.gamePanel.player !=null){
            this.gamePanel.player.vectorPosition.setX(this.gamePanel.player.vectorPosition.getX()
                    + (int)event.values[1]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}