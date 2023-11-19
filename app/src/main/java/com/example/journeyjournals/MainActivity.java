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

//    private void welcomeUserData(){
//        if (firstName_entered!= null){
//            welcomeUser.setText("Welcome " + firstName_entered);
//        }
//    }
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

//        LinearLayout mainLayout = findViewById(R.id.activity_main);
//
//        if (mainLayout!=null){
//            int bgColor = (color == Color.BLACK) ? Color.WHITE : Color.BLACK;
//            mainLayout.setBackgroundColor(bgColor);
////            mainLayout.getWindow().getDecorView().setBackgroundColor(color);
//            int textColor = (color == Color.BLACK) ? Color.WHITE : Color.BLACK;
//            welcomeUser.setTextColor(textColor);
//        } else {
//            Log.e("MainActivityCheck", "Main layout is null");
//        }

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
//
//
//        if (lightSensorEnabled) {
//            // Automatically adjust background and font colors based on light intensity
//            if (lightIntensity < 10) {
//                updateBackgroundColor(Color.BLACK);
//                applyTextStyleToViews(R.style.DarkModeTextStyle);
//                Log.d("UserSettings", "Dark Mode: On");
//            } else {
//                updateBackgroundColor(Color.WHITE);
//                applyTextStyleToViews(R.style.LightModeTextStyle);
//                Log.d("UserSettings", "Dark Mode: Off");
//            }
//        } else{
//            updateBackgroundColor(Color.WHITE);
//            applyTextStyleToViews(R.style.LightModeTextStyle);
//            Log.d("UserSettings", "Light sensor disabled");
//        }
            LightSensorFunction.handleLightSensorChange( MainActivity.this, lightIntensity, lightSensorEnabled);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

//    private void setTextColorAndBackground(int textColor, int backgroundColor) {
//        welcomeUser.setTextColor(textColor);
//        LinearLayout mainLayout = findViewById(R.id.activity_main);
//        if (mainLayout != null) {
//            mainLayout.setBackgroundColor(backgroundColor);
//        }
//    }

//    private void applyTextColorAndBackgroundToViews(int textColor, int backgroundColor) {
//        // Find and update the color for each TextView
//        TextView textView1 = findViewById(R.id.textView1); // Replace with your actual TextView IDs
//        TextView textView2 = findViewById(R.id.textView2);
//        // Add more TextViews as needed
//
//        // Update text color and background for each TextView
//        if (textView1 != null) {
//            textView1.setTextColor(textColor);
//            textView1.setBackgroundColor(backgroundColor);
//        }
//
//        if (textView2 != null) {
//            textView2.setTextColor(textColor);
//            textView2.setBackgroundColor(backgroundColor);
//        }
//
//        // Add more TextViews as needed
//    }

//    private void applyTextStyleToViews(int styleResId) {
//        // Dynamically find and update the style for all TextViews
//        ViewGroup rootView = findViewById(android.R.id.content);
//        applyTextStyleRecursive(rootView, styleResId);
//    }
//
//    private void applyTextStyleRecursive(View view, int styleResId) {
//        if (view instanceof TextView) {
//            ((TextView) view).setTextAppearance(styleResId);
//        } else if (view instanceof ViewGroup) {
//            ViewGroup viewGroup = (ViewGroup) view;
//            for (int i = 0; i < viewGroup.getChildCount(); i++) {
//                applyTextStyleRecursive(viewGroup.getChildAt(i), styleResId);
//            }
//        }
//    }

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


//    @Override
//    public void onClick(View view) {
//
//    }
}