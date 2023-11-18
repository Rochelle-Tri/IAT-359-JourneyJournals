package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeUser;
     final int REQUEST_CODE = 0;
     String firstName_entered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeUser = (TextView) findViewById(R.id.welcomeUser);
        //comment comment
        //yes it worksssss
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK && data != null && data.hasExtra("First Name")){
                if ((data.hasExtra("First Name")));
                {
//                    String firstname_entered = data.getExtras().getString("First Name");
//                    welcomeUser.setText("Welcome" +firstname_entered);
                    String firstName_entered = data.getStringExtra("First Name");
                    welcomeUser.setText("Welcome " + firstName_entered + "!");
                }

            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void retrieveUserSetting(){
        Intent i = new Intent(this, UserSettings.class);
        ActivityCompat.startActivityForResult(this, i, REQUEST_CODE, null);
    }

//    private void welcomeUserData(){
//        if (firstName_entered!= null){
//            welcomeUser.setText("Welcome " + firstName_entered);
//        }
//    }
    protected void onResume(){
        super.onResume();
//        welcomeUserData();
    }
    public void gotoAllTrips(View view){
        Intent i = new Intent(this, JourneyAlbumRecyclerActivity.class);
        startActivity(i);
    }

    public void gotoNewJourney(View view){
        Intent i = new Intent(this, NewJourneyActivity.class);
        startActivity(i);
    }

    public void gotoUserSettings(View view){
//        Intent i = new Intent(this, UserSettings.class);
//        startActivity(i);
        retrieveUserSetting();
    }

//    @Override
//    public void onClick(View view) {
//
//    }
}