package com.example.journeyjournals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewImagesActivity extends AppCompatActivity {

//    private RecyclerView photoRecyclerView;
//    private PhotoAlbumAdapter photoAlbumAdapter;
//    private List<Photo> photoList = new ArrayList<>();  // Declare it at the class level
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_images);
//
//        // Retrieve the list of photo paths from the intent
////        photoList = getIntent().getParcelableArrayListExtra("photoList");
//        ArrayList<Photo> photoList = getIntent().getParcelableArrayListExtra("photoList");
//
//        Log.d("ViewImagesActivity", "Photo list size after retrieving from intent: " + (photoList != null ? photoList.size() : 0));
//
//
//        photoRecyclerView = findViewById(R.id.photoRecyclerView);
//        photoAlbumAdapter = new PhotoAlbumAdapter(createPhotoListFromPaths(photoList));
//
//        photoRecyclerView.setAdapter(photoAlbumAdapter);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        photoRecyclerView.setLayoutManager(layoutManager);
//
//
//        if (photoList != null && !photoList.isEmpty()) {
//            // Pass the list of photo paths to the adapter
//            photoAlbumAdapter.setPhotoList(photoList);
//        } else {
//            Log.e("ViewImagesActivity", "Photo list is null or empty");
//        }
//
//    }
//
//    private List<Photo> createPhotoListFromPaths(List<Photo> photoList) {
//        ArrayList<Photo> photos = new ArrayList<>();
//        if (photoList != null) {
//            for (Photo path : photoList) {
//                photos.add(new Photo(String.valueOf(path)));
//            }
//        }
//        return photos;
//    }
//
//    // Method to start ViewImagesActivity and pass the list of photos
//    public void viewImages(View view) {
//        // Pass the list of photos to ViewImagesActivity
//        Intent intent = new Intent(this, ViewImagesActivity.class);
//        intent.putParcelableArrayListExtra("photoList", new ArrayList<>(photoList));
//        startActivity(intent);
//    }

//    private RecyclerView recyclerView;
//    private List<PhotoModel> photoList;
//    private RecyclerViewAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display_photos);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        photoList = new ArrayList<>();
//
//        // Retrieve the image from the intent
//        Bitmap imageBitmap = getIntent().getParcelableExtra("imageBitmap");
//
//        // Add the image to the photoList
//        photoList.add(new PhotoModel(imageBitmap));
//
//        // Initialize and set up the RecyclerView
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(photoList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//
//    }

    private ImageView imageView;
    private Bitmap imageBitmap;
    private String photoPath;

    private List<PhotoModel> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photos);

        imageView = findViewById(R.id.displayImageView);

        // Retrieve the photo path from the intent
        photoPath = getIntent().getStringExtra("photoPath");

        // Load and set the image in the ImageView
        if (photoPath != null) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath);
            imageView.setImageBitmap(imageBitmap);
        } else {
            // Handle the case where the photoPath is null
            Log.e("ViewImagesActivity", "Photo path is null");
            finish(); // Finish the activity if no photo path is available
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the photoList in the outState bundle
//        ArrayList<Parcelable> parcelableList = new ArrayList<>();
//        for (PhotoModel photoModel : photoList) {
//            parcelableList.add((Parcelable) photoModel);
//        }
//        outState.putParcelableArrayList("photoList", parcelableList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the photoList from the savedInstanceState bundle
//        if (savedInstanceState != null) {
//            ArrayList<Parcelable> restoredParcelableList = savedInstanceState.getParcelableArrayList("photoList");
//            if (restoredParcelableList != null) {
//                List<PhotoModel> restoredPhotoList = new ArrayList<>();
//                for (Parcelable parcelable : restoredParcelableList) {
//                    if (parcelable instanceof PhotoModel) {
//                        restoredPhotoList.add((PhotoModel) parcelable);
//                    }
//                }
//                photoList.clear();
//                photoList.addAll(restoredPhotoList);
//                // Update the RecyclerView
//                adapter.updateData(photoList);
//            }
//        }
    }

    // When a new photo is captured and you want to update the RecyclerView
    private void updateRecyclerView(Bitmap newImageBitmap) {
        // Add the new image to the photoList
//        photoList.add(new PhotoModel(newImageBitmap));

        // Notify the adapter about the data change
//        adapter.updateData(photoList);
    }

    // Call this method whenever you want to update the RecyclerView with a new photo
    // For example, you can call this from a button click or from onActivityResult
//    private void onNewPhotoCaptured(Bitmap newImageBitmap) {
//        updateRecyclerView(newImageBitmap);
//    }

    public void goBack(View view){
//        Intent intent = new Intent(this, NewJournalDetailActivity.class);
//        startActivity(intent);
        finish();
    }
}
