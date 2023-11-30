package com.example.journeyjournals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDataBase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDataBase (Context c){
        context = c;
        helper = new MyHelper(context);
    }
    public long insertInitialData (String checklist) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(Constants.NAME, name);
//        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.CHECKLIST, checklist);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }
    public long insertData (String name, String location, String date, String duration, String notes, String checklist) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.DURATION, duration);
        contentValues.put(Constants.NOTES, notes);
        contentValues.put(Constants.CHECKLIST, checklist);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    //this is used in the recyclerview where it needs to extract data to display each entry in the album page
    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE,Constants.DURATION, Constants.NOTES };
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    //this is used in the EditJournal page where the user wants to change info in their journal entry
    public void updateJournalEntry(int entryId, String newName, String newLocation, String newDate, String newDuration, String newNotes) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.NAME, newName);
        values.put(Constants.LOCATION, newLocation);
        values.put(Constants.DATE, newDate);
        values.put(Constants.DURATION, newDuration);
        values.put(Constants.NOTES, newNotes);

        String whereClause = Constants.UID + "=?";
        String[] whereArgs = {String.valueOf(entryId)};

        db.update(Constants.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

}
