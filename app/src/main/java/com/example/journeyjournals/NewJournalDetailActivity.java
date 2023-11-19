package com.example.journeyjournals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class NewJournalDetailActivity extends AppCompatActivity{

    EditText journeyName, journeyLocation, journeyDate, journeyDuration, journeyNotes;
    MyDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_detail);

        journeyName = (EditText)findViewById(R.id.userJourneyNameTextView);
        journeyLocation = (EditText)findViewById(R.id.userJourneyLocationTextView);
        journeyDate = (EditText)findViewById(R.id.userJourneyDateTextView);
        journeyDuration = (EditText)findViewById(R.id.userJourneyTimeTextView);
        journeyNotes = (EditText)findViewById(R.id.notesBoxTextView);



    }
    public void saveData (View view) {
        String name = journeyName.getText().toString();
        String location = journeyLocation.getText().toString();
        String date = journeyDate.getText().toString();
        String duration = journeyDuration.getText().toString();
        String notes = journeyNotes.getText().toString();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        long id = db.insertData(name, location, date, duration, notes);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
