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
    public long insertData (String name, String location, String date, String duration, String notes) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.DURATION, duration);
        contentValues.put(Constants.NOTES, notes);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }
    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE,Constants.DURATION, Constants.NOTES };
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public String getSelectedData(String id)
    {
        //select data of a specific journal entry
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE,Constants.DURATION, Constants.NOTES };

        String selection = Constants.UID + "='" +id+ "'";  //Constants.UID = 'id'
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.LOCATION);
            int index3 = cursor.getColumnIndex(Constants.DATE);
            int index4 = cursor.getColumnIndex(Constants.DURATION);
            int index5 = cursor.getColumnIndex(Constants.NOTES);
            String journeyName = cursor.getString(index1);
            String journeyLocation = cursor.getString(index2);
            String journeyDate = cursor.getString(index3);
            String journeyDuration = cursor.getString(index4);
            String journeyNotes = cursor.getString(index5);
            buffer.append(journeyName + " " + journeyLocation + " " + journeyDate + " " + journeyDuration + "\n");
        }
        return buffer.toString();
    }

}
