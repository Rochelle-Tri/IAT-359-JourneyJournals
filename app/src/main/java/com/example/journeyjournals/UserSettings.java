package com.example.journeyjournals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {
    private Button saveButton;
    private EditText firstNameEditText;
    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);

        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);

    }

//    public void saveData(View view){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }

    public void goBack(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        firstName = firstNameEditText.getText().toString();
        Toast.makeText(this, firstName, Toast.LENGTH_SHORT).show();

        Intent retrieveIntent = getIntent();
        retrieveIntent.putExtra("First Name", firstName);
        setResult(RESULT_OK, retrieveIntent);
        finish();
    }
}
