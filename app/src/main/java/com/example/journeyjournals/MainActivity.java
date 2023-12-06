package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.journeyjournals.LightSensorFunction;
import androidx.appcompat.app.AppCompatActivity;

//the main activity serves as the main menu
public class MainActivity extends AppCompatActivity {

    private TextView welcomeUser;
     final int REQUEST_CODE = 0;
     String firstName_entered;
     private static final int DEFAULT_COLOR = Color.WHITE;
    private static final String MY_PREFS = "MyPrefs";
    private boolean lightSensorEnabled = true;  // Flag to enable/disable light sensor

    private LightSensorFunction lightSensorFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeUser = findViewById(R.id.welcomeUser);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null){
            Toast.makeText(this, "There is no light sensor available for this device", Toast.LENGTH_SHORT).show();
        } else{
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);

        int initialBackgroundColor = getInitialBackgroundColor();

        // Update the background color based on the user's preference
        updateBackgroundColor(initialBackgroundColor);
        lightSensorFunction = new LightSensorFunction();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Check if user disabled light sensor in settings
            lightSensorEnabled = data.getBooleanExtra("LightSensorEnabled", true);

            // Retrieve user settings and update UI
            retrieveUserSetting();
        }
    }

    private void retrieveUserSetting(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        lightSensorEnabled = preferences.getBoolean("LightSensorEnabled", true);

        int backgroundColor = preferences.getInt("backgroundColor", DEFAULT_COLOR);
        Log.d("MainActivityCheck", "Retrieved Background Color: " + backgroundColor);
        updateBackgroundColor(backgroundColor);
//        getWindow().getDecorView().setBackgroundColor(backgroundColor);

        String firstName = preferences.getString("First Name", "");
        if(!firstName.isEmpty()){
            welcomeUser.setText("Welcome " + firstName + "!");

        }

    }


    protected void onResume(){
        super.onResume();
//        welcomeUserData();
        retrieveUserSetting();

        int backgroundColor = getInitialBackgroundColor();
        updateBackgroundColor(backgroundColor);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "There is no light sensor available for this device", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onPause(){
        super.onPause();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorListener);
    }

    private int getInitialBackgroundColor(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        return preferences.getInt("backgroundColor", DEFAULT_COLOR);
    }

    private void updateBackgroundColor(int color){
        Log.d("MainActivityCheck", "Updating Background Color: " + color);

        int bgColor = (color == Color.WHITE) ? Color.WHITE : Color.BLACK;
        getWindow().getDecorView().setBackgroundColor(bgColor);
//        mainLayout.setBackgroundColor(bgColor);
        int textColor = (color == Color.BLACK) ? Color.WHITE : Color.BLACK;
        welcomeUser.setTextColor(textColor);
    }

    private final SensorEventListener sensorListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event) {
            float lightIntensity = event.values[0];

            LightSensorFunction.handleLightSensorChange( MainActivity.this, lightIntensity, lightSensorEnabled);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void gotoAllTrips(View view){
        Intent i = new Intent(this, JourneyAlbumRecyclerActivity.class);
        startActivity(i);
    }

    public void gotoNewJourney(View view){
        Intent i = new Intent(this, NewJourneyActivity.class);
        startActivity(i);
    }

    public void gotoUserSettings(View view){
        Intent i = new Intent(this, UserSettings.class);
        startActivity(i);
//        ActivityCompat.startActivityForResult(i, REQUEST_CODE);
    }

}