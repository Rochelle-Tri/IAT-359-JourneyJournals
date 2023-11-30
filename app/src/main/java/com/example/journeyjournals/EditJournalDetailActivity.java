package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;

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

public class EditJournalDetailActivity extends AppCompatActivity {
    EditText journeyNameTV, journeyLocationTV, journeyDateTV, journeyDurationTV, journeyNotesTV;

    String name, location, date, duration, notes;
    private MyDataBase db;
    private int entryId;

    //light sensor stuff-------------------------------------------
    private LightSensorFunction lightSensorFunction;
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final String MY_PREFS = "MyPrefs";
    private boolean lightSensorEnabled = true;
    final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal_detail);

        journeyNameTV = (EditText)findViewById(R.id.userJourneyNameTextView);
        journeyLocationTV = (EditText)findViewById(R.id.userJourneyLocationTextView);
        journeyDateTV = (EditText)findViewById(R.id.userJourneyDateTextView);
        journeyDurationTV = (EditText)findViewById(R.id.userJourneyTimeTextView);
        journeyNotesTV = (EditText)findViewById(R.id.notesBoxTextView);

        db = new MyDataBase(this);

        // Retrieve the entry ID from Intent extras
        Bundle extra_data = getIntent().getExtras();
        if (extra_data!= null) {
            // retrieve value from bundle (supply the key, get the value)
            entryId = extra_data.getInt("ITEM_KEY");
            entryId = entryId + 1;
            loadDetailsFromDatabase();
            journeyNameTV.setText(name);
            journeyLocationTV.setText(location);
            journeyDateTV.setText(date);
            journeyDurationTV.setText(duration);
            journeyNotesTV.setText(notes);

        } else {
            // did not receive bundle with extra data
            Toast.makeText(this, "Didn't receive any data", Toast.LENGTH_SHORT).show();
        }

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
        String newName = journeyNameTV.getText().toString();
        String newLocation = journeyLocationTV.getText().toString();
        String newDate = journeyDateTV.getText().toString();
        String newDuration = journeyDurationTV.getText().toString();
        String newNotes = journeyNotesTV.getText().toString();
        Toast.makeText(this, "your journal has been updated", Toast.LENGTH_SHORT).show();
        //entryId = entryId -1;
        db.updateJournalEntry(entryId, newName, newLocation, newDate, newDuration, newNotes);
    }

    private void loadDetailsFromDatabase() {
        MyHelper helper = new MyHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define the query and columns
        String selection = "_id=?";
        String[] selectionArgs = {String.valueOf(entryId)};

        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE, Constants.DURATION, Constants.NOTES};

        try {
            Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.NAME));
                location = cursor.getString(cursor.getColumnIndexOrThrow(Constants.LOCATION));
                date = cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE));
                duration = cursor.getString(cursor.getColumnIndexOrThrow(Constants.DURATION));
                notes = cursor.getString(cursor.getColumnIndexOrThrow(Constants.NOTES));

                // Close the cursor
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the database
            db.close();
        }
    }
    public void viewChecklist(View view){
        Intent i = new Intent(this, EditChecklistActivity.class);
        i.putExtra ("ENTRYID_KEY", entryId);
        startActivity(i);
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void goAlbum(View view){
        Intent i = new Intent(this, JourneyAlbumRecyclerActivity.class);
        startActivity(i);
    }

    public void takePhotos(View view){
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
        Log.d("CameraActivity", "Failed to Start");
    }
    public void viewPhotos(View view){
        Toast.makeText(this, "Currently Work In Progress.", Toast.LENGTH_SHORT).show();
        Log.d("CameraActivity", "Test");
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

            LightSensorFunction.handleLightSensorChange( EditJournalDetailActivity.this, lightIntensity, lightSensorEnabled);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}