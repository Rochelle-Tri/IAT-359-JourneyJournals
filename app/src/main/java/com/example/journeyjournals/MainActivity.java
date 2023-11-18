package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //comment comment
        //yes it worksssss
    }

    public void gotoAllTrips(View v){
        Intent i = new Intent(this, JourneyAlbumRecyclerActivity.class);
        startActivity(i);
    }

    public void gotoNewJourney(View v){
        Intent i = new Intent(this, NewJourneyActivity.class);
        startActivity(i);
    }

    public void gotoUserSettings(View v){
        Intent i = new Intent(this, UserSettings.class);
        startActivity(i);
    }

}