package com.example.journeyjournals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewJournalDetailActivity extends AppCompatActivity {

    EditText journeyNameTV, journeyLocationTV, journeyDateTV, journeyDurationTV, journeyNotesTV;

    private MyDataBase db;

    //light sensor stuff-------------------------------------------
    private LightSensorFunction lightSensorFunction;
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final String MY_PREFS = "MyPrefs";
    private boolean lightSensorEnabled = true;
    final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_detail);

        journeyNameTV = (EditText)findViewById(R.id.userJourneyNameTextView);
        journeyLocationTV = (EditText)findViewById(R.id.userJourneyLocationTextView);
        journeyDateTV = (EditText)findViewById(R.id.userJourneyDateTextView);
        journeyDurationTV = (EditText)findViewById(R.id.userJourneyTimeTextView);
        journeyNotesTV = (EditText)findViewById(R.id.notesBoxTextView);

        db = new MyDataBase(this);

        //light sensor----------------------------------------------------------------------------------------------
        lightSensorFunction = new LightSensorFunction();

        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);

        int initialBackgroundColor = getInitialBackgroundColor();
        // Update the background color based on the user's preference
        updateBackgroundColor(initialBackgroundColor);

        // Use LightSensorFunction methods to handle light sensor changes
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "There is no light sensor available for this device", Toast.LENGTH_SHORT).show();
            Log.d("NewJourneyActivity", "No light sensor available");

        } else {
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
            Log.d("NewJourneyActivity", "Light sensor registered");
        }
    }
    public void saveData (View view) {
//        Log.d("myTag", "smt went wrong");
        String name = journeyNameTV.getText().toString();
        String location = journeyLocationTV.getText().toString();
        String date = journeyDateTV.getText().toString();
        String duration = journeyDurationTV.getText().toString();
        String notes = journeyNotesTV.getText().toString();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        long id = db.insertData(name, location, date, duration, notes);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Your journal entry was created", Toast.LENGTH_SHORT).show();
        }
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void addPhotos(View view){
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
        Log.d("CameraActivity", "Failed to Start");
    }

//    private void loadDetailsFromDatabase() {
//
//        MyHelper helper = new MyHelper(this);
//        //SQLiteDatabase db = helper.getReadableDatabase();
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        // Define your query and columns
//        String selection = "id=?";
//        String[] selectionArgs = {String.valueOf(entryId)};
//
//        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE,Constants.DURATION, Constants.NOTES };
//        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.NAME));
//            location = cursor.getString(cursor.getColumnIndexOrThrow(Constants.LOCATION));
//            date = cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE));
//            duration = cursor.getString(cursor.getColumnIndexOrThrow(Constants.DURATION));
//            notes = cursor.getString(cursor.getColumnIndexOrThrow(Constants.NOTES));
//
//            // Close the cursor
//            cursor.close();
//        }
//        // Close the database
//        db.close();
//    }

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

            LightSensorFunction.handleLightSensorChange( NewJournalDetailActivity.this, lightIntensity, lightSensorEnabled);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
