package com.example.journeyjournals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewImagesActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap imageBitmap;
    private String photoPath;

    private List<PhotoModel> photoList;

    private long entryId;  // Declare entryId as a class-level variable
    private MyHelper db;   // Declare db as a class-level variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photos);

        imageView = findViewById(R.id.displayImageView);

        // Retrieve the photo path from the intent
        photoPath = getIntent().getStringExtra("photoPath");

        // Load and set the image in the ImageView
//        if (photoPath != null) {
//            Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath);
//            imageView.setImageBitmap(imageBitmap);
//        } else {
//            // Handle the case where the photoPath is null
//            Log.e("ViewImagesActivity", "Photo path is null");
//            finish(); // Finish the activity if no photo path is available
//        }
        // Use 'photoPath' to display the image
        if (photoPath != null && !photoPath.isEmpty()) {
            // Use your preferred method to load and display the image, for example, using an image loading library like Glide
            Glide.with(this).load(photoPath).into(imageView);
        } else {
            // Handle the case where 'photoPath' is not found
            Toast.makeText(this, "No photo path found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no photo path is available
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the photoList in the outState bundle
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    // When a new photo is captured and you want to update the RecyclerView
    private void updateRecyclerView(Bitmap newImageBitmap) {
        // Add the new image to the photoList

    }

    // Call this method whenever you want to update the RecyclerView with a new photo
    // For example, you can call this from a button click or from onActivityResult
//    private void onNewPhotoCaptured(Bitmap newImageBitmap) {
//        updateRecyclerView(newImageBitmap);
//    }

    public void goBack(View view){

        finish();
    }
}
