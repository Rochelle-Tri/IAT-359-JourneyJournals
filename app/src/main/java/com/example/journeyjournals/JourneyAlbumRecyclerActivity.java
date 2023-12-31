package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JourneyAlbumRecyclerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private RecyclerView myRecycler;
    private MyDataBase db;
    private CustomAdapter customAdapter;
    private MyHelper helper;
    private LinearLayoutManager mLayoutManager;

    //light sensor stuff-------------------------------------------
    private LightSensorFunction lightSensorFunction;
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final String MY_PREFS = "MyPrefs";
    private boolean lightSensorEnabled = true;
    final int REQUEST_CODE = 0;

    private TextView emptyAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_album_recycler);
        myRecycler = (RecyclerView) findViewById(R.id.rvItems);

        db = new MyDataBase(this);
        helper = new MyHelper(this);
        Cursor cursor = db.getData();

        //if there is nothing in the database, show a message saying the album is empty
        emptyAlbum = (TextView)findViewById(R.id.emptyListTextView);
        if(helper.areAllColumnsEmpty(Constants.TABLE_NAME)){
            emptyAlbum.setVisibility(View.VISIBLE);
        }
        else{
            emptyAlbum.setVisibility(View.GONE);
        }

       //get the info from each column
        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.LOCATION);
        int index3 = cursor.getColumnIndex(Constants.DATE);

        //put info into the arraylist that the recycler will use to display the info
        ArrayList<String> myArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String journeyName = cursor.getString(index1);
            String journeyLocation = cursor.getString(index2);
            String journeyDate = cursor.getString(index3);
            String s = journeyName +"," + journeyLocation + "," + journeyDate;
            myArrayList.add(s);
            cursor.moveToNext();
        }
        customAdapter = new CustomAdapter(myArrayList);
        myRecycler.setAdapter(customAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(mLayoutManager);

        //light sensor----------------------------------------------------------------------------------------------
//        lightSensorFunction = new LightSensorFunction();
//
//        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
//
//        int initialBackgroundColor = getInitialBackgroundColor();
//        // Update the background color based on the user's preference
//        updateBackgroundColor(initialBackgroundColor);
//
//        // Use LightSensorFunction methods to handle light sensor changes
//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//
//        if (lightSensor == null) {
//            Toast.makeText(this, "There is no light sensor available for this device", Toast.LENGTH_SHORT).show();
//            Log.d("NewJourneyActivity", "No light sensor available");
//
//        } else {
//            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
//            Log.d("NewJourneyActivity", "Light sensor registered");
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    //light sensor code ---------------------------------------------------------------------------------------------

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("NewJourneyActivity", "I am being called");
        retrieveUserSetting();

        int backgroundColor = getInitialBackgroundColor();
        updateBackgroundColor(backgroundColor);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
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
        Log.d("NewJourneyActivity", "Retrieved Background Color: " + backgroundColor);
        Log.d("NewJourneyActivity", "I am being called");
        updateBackgroundColor(backgroundColor);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void updateBackgroundColor(int color){
        Log.d("MainActivityCheck", "Updating Background Color: " + color);
        int bgColor = (color == Color.WHITE) ? Color.WHITE : Color.BLACK;
        getWindow().getDecorView().setBackgroundColor(bgColor);

    }

    private final SensorEventListener sensorListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event) {
            float lightIntensity = event.values[0];

            LightSensorFunction.handleLightSensorChange( JourneyAlbumRecyclerActivity.this, lightIntensity, lightSensorEnabled);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}