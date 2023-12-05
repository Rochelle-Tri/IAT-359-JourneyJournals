package com.example.journeyjournals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDataBase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDataBase (Context c){
        context = c;
        helper = new MyHelper(context);
    }
    public long insertData (String name, String location, String date, String duration, String notes, String checklist, String photoPath) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.DURATION, duration);
        contentValues.put(Constants.NOTES, notes);
        contentValues.put(Constants.CHECKLIST, checklist);
        contentValues.put(Constants.PHOTO_PATH, photoPath);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    //this is used in the recyclerview where it needs to extract data to display each entry in the album page
    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE,Constants.DURATION, Constants.NOTES, Constants.PHOTO_PATH };
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null, null);
        return cursor;
    }

    //used in the EditJournal page where the user wants to change info in their journal entry
    public void updateJournalEntry(int entryId, String newName, String newLocation, String newDate, String newDuration, String newNotes, String newPhoto) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.NAME, newName);
        values.put(Constants.LOCATION, newLocation);
        values.put(Constants.DATE, newDate);
        values.put(Constants.DURATION, newDuration);
        values.put(Constants.NOTES, newNotes);

        // Add PHOTO_PATH only if it is not null or empty
        if (newPhoto != null && !newPhoto.isEmpty()) {
            values.put(Constants.PHOTO_PATH, newPhoto);
        }

        String whereClause = Constants.UID + "=?";
        String[] whereArgs = {String.valueOf(entryId)};

        db.update(Constants.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    // Method to delete a row from the database based on the _id
    public void deleteRow(long entryId) {
        db = helper.getWritableDatabase();
        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(entryId)};
        db.delete(Constants.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    //----added code to view images
    public SQLiteDatabase getReadableDatabase() {
        return helper.getReadableDatabase();
    }
    // Method to retrieve a specific row from the database based on the _id
    public Cursor getEntryById(long entryId) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.LOCATION, Constants.CHECKLIST, Constants.DATE, Constants.DURATION, Constants.NOTES, Constants.PHOTO_PATH};
        String selection = Constants.UID + "=?";
        String[] selectionArgs = {String.valueOf(entryId)};

        return db.query(Constants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    // Add a method to update PHOTO_PATH in an existing entry
    public void updatePhotoPath(long entryId, String newPhotoPath) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.PHOTO_PATH, newPhotoPath);

        String whereClause = Constants.UID + "=?";
        String[] whereArgs = {String.valueOf(entryId)};

        try {
            int rowsAffected = db.update(Constants.TABLE_NAME, values, whereClause, whereArgs);

            if (rowsAffected > 0) {
                Log.d("MyDataBase", "Photo path updated successfully");
            } else {
                Log.e("MyDataBase", "Failed to update photo path");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MyDataBase", "Exception while updating photo path");
        } finally {
            db.close();
        }
    }

    // Add a method to retrieve PHOTO_PATH
    public String getPhotoPath(long entryId) {
        db = helper.getReadableDatabase();

        String[] columns = {Constants.PHOTO_PATH};
        String selection = Constants.UID + "=?";
        String[] selectionArgs = {String.valueOf(entryId)};

        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String photoPath = null;

        if (cursor != null && cursor.moveToFirst()) {
            photoPath = cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHOTO_PATH));
            cursor.close();
        }

        db.close();
        return photoPath;
    }
}
