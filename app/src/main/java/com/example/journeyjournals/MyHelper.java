package com.example.journeyjournals;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

// This class creates the tables for the database
public class MyHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    Constants.TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.LOCATION + " TEXT," +
                    Constants.CHECKLIST + " TEXT, " +
                    Constants.DATE + " TEXT," +
                    Constants.DURATION + " TEXT, " +
                    Constants.NOTES + " TEXT, " +
                    Constants.PHOTO_PATH + " TEXT)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

    public MyHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
//            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }

    // Method to check if any is any data inserted in the database
    public boolean areAllColumnsEmpty(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        // Check if there are any rows in the table
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false; // There are rows in the table
        }

        cursor.close();
        db.close();
        return true; // The table is empty
    }

    //camera stuff---------------------------------------

    // Method to retrieve the photo path for a given entry ID
    public String getPhotoPath(long entryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Constants.PHOTO_PATH};
        String selection = Constants.UID + "=?";
        String[] selectionArgs = {String.valueOf(entryId)};

        try {
            Cursor cursor = db.query(
                    Constants.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                String photoPath = cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHOTO_PATH));
                cursor.close();
                db.close();
                return photoPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return null; // Return null if entry ID is not found or there's an error
    }
}
