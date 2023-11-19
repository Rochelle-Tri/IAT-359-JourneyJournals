package com.example.journeyjournals;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewJournalDetailActivity extends AppCompatActivity {

    EditText journeyNameTV, journeyLocationTV, journeyDateTV, journeyDurationTV, journeyNotesTV;

    String name, location, date, duration, notes;
    private MyDataBase db;

    private int entryId;


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
        Cursor cursor = db.getData();

        //get the info from each column
//        int index1 = cursor.getColumnIndex(Constants.NAME);
//        int index2 = cursor.getColumnIndex(Constants.LOCATION);
//        int index3 = cursor.getColumnIndex(Constants.DATE);
//        int index4 = cursor.getColumnIndex(Constants.DURATION);
//        int index5 = cursor.getColumnIndex(Constants.NOTES);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            String journeyName = cursor.getString(index1);
//            String journeyLocation = cursor.getString(index2);
//            String journeyDate = cursor.getString(index3);
//            String journeyDuration = cursor.getString(index4);
//            String journeyNotes = cursor.getString(index5);
//
//            // update text view to show corresponding journey details
//            journeyNameTV.setText(journeyName);
//            journeyLocationTV.setText(journeyLocation);
//            journeyDateTV.setText(journeyDate);
//            journeyDurationTV.setText(journeyDuration);
//            journeyNotesTV.setText(journeyNotes);
//        }

        // Retrieve the entry ID from Intent extras
//        Bundle extra_data = getIntent().getExtras();
//        entryId = extra_data.getInt("ITEM_KEY");
//
//        // Load other details from the database using the entry ID
//        loadDetailsFromDatabase();
//        journeyNameTV.setText(name);
//        journeyLocationTV.setText(location);
//        journeyDateTV.setText(date);
//        journeyDurationTV.setText(duration);
//        journeyNotesTV.setText(notes);
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
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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
}
