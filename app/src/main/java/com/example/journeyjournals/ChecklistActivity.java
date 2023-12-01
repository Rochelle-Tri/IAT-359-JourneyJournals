package com.example.journeyjournals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChecklistActivity extends AppCompatActivity {


    String checklist;

    TextView checkListTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        checkListTV = (TextView)findViewById(R.id.checklistBoxTextView);

        // Retrieve the entry ID from Intent extras
        Bundle extra_data = getIntent().getExtras();
        if (extra_data!= null) {
            // retrieve value from bundle (supply the key, get the value) and show it
            checklist = extra_data.getString("CHECKLIST_KEY");
            checkListTV.setText(checklist);

        } else {
            // did not receive bundle with extra data
            Toast.makeText(this, "There is no data your checklist", Toast.LENGTH_SHORT).show();
        }
    }


    public void goBack(View view){
        finish();
    }
}