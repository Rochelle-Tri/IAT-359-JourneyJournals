package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewJourneyActivity extends AppCompatActivity {

    EditText journeyName, journeyLocation, journeyChecklist;
    MyDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey);
        journeyName = (EditText)findViewById(R.id.userJourneyNameTextView);
        journeyLocation = (EditText)findViewById(R.id.userJourneyLocationTextView);
        journeyChecklist = (EditText)findViewById(R.id.checklistBoxTextView);

        db = new MyDataBase(this);
    }

    public void viewJourneyDetails (View view) {
        String name = journeyName.getText().toString();
        String location = journeyLocation.getText().toString();
        String checkList = journeyChecklist.getText().toString();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        long id = db.insertInitialData(name, location, checkList);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
        journeyName.setText("");
        journeyLocation.setText("");
        journeyChecklist.setText("");

        //go to details view of this journey
        Intent intent = new Intent(this, NewJournalDetailActivity.class);
        Toast.makeText(this, "go to recyclerView",Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}