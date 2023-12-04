package com.example.journeyjournals;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewJournalDetailActivity extends AppCompatActivity {

    EditText journeyNameTV, journeyLocationTV, journeyDateTV, journeyDurationTV, journeyNotesTV;

    private MyDataBase db;

    //light sensor stuff-------------------------------------------
    private LightSensorFunction lightSensorFunction;
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final String MY_PREFS = "MyPrefs";
    private boolean lightSensorEnabled = true;
    final int REQUEST_CODE = 0;

    //camera stuff ----------------------------------------------
//    private RecyclerView photoRecyclerView;
//    private LinearLayoutManager layoutManager;
//    private PhotoAlbumAdapter photoAlbumAdapter;
//
//    private List<String> photoList = new ArrayList<>();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private Bitmap imageBitmap;

    @SuppressLint("MissingInflatedId")
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

        //light sensor----------------------------------------------------------------------------------------------
        lightSensorFunction = new LightSensorFunction();

        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);

        int initialBackgroundColor = getInitialBackgroundColor();
        // Update the background color based on the user's preference
        updateBackgroundColor(initialBackgroundColor);

        // Use LightSensorFunction methods to handle light sensor changes
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "There is no light sensor available for this device", Toast.LENGTH_SHORT).show();
            Log.d("NewJourneyActivity", "No light sensor available");

        } else {
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
            Log.d("NewJourneyActivity", "Light sensor registered");
        }

        //camera activity ----------------------------------------------------------------------------------------------

//        Log.d("NewJournalDetailActivity", "Checking: onCreate");
//        // Handle camera photos
//        setupPhotoRecyclerView();
//
//        // Initialize layoutManager
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//
//        // Retrieve photo paths from intent extras
//        ArrayList<String> photoPaths = getIntent().getStringArrayListExtra("photoList");
//
//        // Check if the intent contains the photoList
//        if (photoPaths != null) {
//            // Convert photo paths to Photo objects
//            ArrayList<Photo> photoList = createPhotoListFromPaths(photoPaths);
//
//            if (photoList != null && !photoList.isEmpty()) {
//                // Pass the list of photo paths to the adapter
//                photoAlbumAdapter.setPhotoList(photoList);
//            }
//        } else {
//            // Handle the case where the intent does not contain the photoList
//            Log.d("NewJournalDetailActivity", "photoList not created");
//        }

        // Check and request camera permission at runtime
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA},
//                    REQUEST_CAMERA_PERMISSION);
//        }

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
            Toast.makeText(this, "Your journal entry was created", Toast.LENGTH_SHORT).show();
        }
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

//    public void takePhotos(View view){
//        Intent i = new Intent(this, CameraActivity.class);
//        // start CameraActivity using startActivityForResult
//        startActivityForResult(i, REQUEST_CODE);
//        Log.d("CameraActivity", "Failed to Start");
//    }
//
//    public void viewPhotos(View view){
//        // Retrieve photo paths from intent extras
//        ArrayList<String> photoPaths = getIntent().getStringArrayListExtra("photoList");
//
//        // Log the size of the retrieved photoPaths
////        Log.d("NewJournalDetailActivity", "Photo paths size before starting ViewImagesActivity: " + (photoPaths != null ? photoPaths.size() : 0));
//
//        if (photoPaths != null && !photoPaths.isEmpty()) {
//            // Convert photo paths to Photo objects
//            ArrayList<Photo> photoList = createPhotoListFromPaths(photoPaths);
//
//            Log.d("NewJournalDetailActivity", "viewPhotos: Photo paths size before starting ViewImagesActivity: " + (photoPaths != null ? photoPaths.size() : 0));
//            Log.d("NewJournalDetailActivity", "viewPhotos: Photo paths content: " + (photoPaths != null ? photoPaths.toString() : "null"));
//
//            // Log the size of the photoList
//            Log.d("NewJournalDetailActivity", "viewPhotos: Photo list size before starting ViewImagesActivity: " + (photoList != null ? photoList.size() : 0));
//
//            // Pass the list of photos to ViewImagesActivity
//            Intent intent = new Intent(this, ViewImagesActivity.class);
//            intent.putParcelableArrayListExtra("photoList", photoList);
//            startActivity(intent);
//
//        } else {
//            Log.e("NewJournalDetailActivity", "Error on viewPhotos click: Photo paths list is null or empty");
//        }
//
//    }

    public void openCamera(View view) {
        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, open the camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, open the camera
                openCamera(null);
            } else {
                // Camera permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
//            imageBitmap = (Bitmap) data.getExtras().get("data");
//            // Save or display the image as needed
//
//            // Start the second activity
////            Intent intent = new Intent(this, DisplayPhotosActivity.class);
////            intent.putExtra("imageBitmap", imageBitmap);
////            startActivity(intent);
//        }
//    }

    public void displayPhoto(View view) {

        if (imageBitmap != null) {
            // Start the DisplayPhotosActivity
            Intent intent = new Intent(this, ViewImagesActivity.class);
            intent.putExtra("imageBitmap", imageBitmap);
            startActivity(intent);


        } else {
            // Handle the case where the imageBitmap is null (not captured)
            Toast.makeText(this, "No image captured yet", Toast.LENGTH_SHORT).show();
        }
    }

    // Add viewImages -----------------------------------------------------------------------------------------------

//    private void setupPhotoRecyclerView() {
//        //initialize this first
//        RecyclerView recyclerView = findViewById(R.id.photoRecyclerView);
//        if (recyclerView != null) {
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//            recyclerView.setLayoutManager(layoutManager);
//
//            // Retrieve photo paths from intent extras
//            ArrayList<String> photoPaths = getIntent().getStringArrayListExtra("photoList");
//
//            // Convert photo paths to Photo objects
//            ArrayList<PhotoDELETE> photoList = createPhotoListFromPaths(photoPaths);
//
//            if (photoList != null && !photoList.isEmpty()) {
//                PhotoAlbumAdapter adapter = new PhotoAlbumAdapter(photoList);
//                recyclerView.setAdapter(adapter);
//            }
//        }
//    }
//    public void viewImages(View view) {
//        Intent intent = new Intent(this, ViewImagesActivity.class);
//        // Pass the list of photos to ViewImagesActivity
//        ArrayList<Photo> photos = createPhotoListFromPaths(getIntent().getStringArrayListExtra("photoList"));
//        intent.putParcelableArrayListExtra("photoList", photos);
//        startActivity(intent);
//    }

    // Other methods...

//    private ArrayList<PhotoDELETE> createPhotoListFromPaths(ArrayList<String> photoPaths) {
//        ArrayList<PhotoDELETE> photos = new ArrayList<>();
//        if (photoPaths != null) {
//            for (String path : photoPaths) {
//                photos.add(new PhotoDELETE(path));
//            }
//        }
//        // Log the size of the created photo list
//        Log.d("NewJournalDetailActivity", "createPhotoListFromPaths: Created photo list size: " + photos.size());
//
//        return photos;
//    }

    //light sensor code ---------------------------------------------------------------------------------------------

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("NewJournalDetailActivity", "onResume");
        retrieveUserSetting();

        int backgroundColor = getInitialBackgroundColor();
        updateBackgroundColor(backgroundColor);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }


    }
    protected void onPause(){
        super.onPause();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorListener);
    }

    private int getInitialBackgroundColor(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        return preferences.getInt("backgroundColor", DEFAULT_COLOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Check if user disabled light sensor in settings
            lightSensorEnabled = data.getBooleanExtra("LightSensorEnabled", true);

            // Retrieve user settings and update UI
            retrieveUserSetting();

//            // Update the photoList with the new photo path
//            ArrayList<String> receivedPhotoPaths = data.getStringArrayListExtra("photoList");
//            Log.d("NewJournalDetailActivity", "Received photo paths: " + (receivedPhotoPaths != null ? receivedPhotoPaths.toString() : "null"));
//
//            if (receivedPhotoPaths != null && !receivedPhotoPaths.isEmpty()) {
//                // Process the receivedPhotoPaths
//                Log.d("NewJournalDetailActivity", "Received photo paths size: " + receivedPhotoPaths.size());
//
//                // For example, convert received photo paths to Photo objects if needed
//                ArrayList<Photo> receivedPhotoList = createPhotoListFromPaths(receivedPhotoPaths);
//                Log.d("NewJournalDetailActivity", "Received photo list size after conversion: " + (receivedPhotoList != null ? receivedPhotoList.size() : 0));
//
//                //  use receivedPhotoList in your logic
//
//                // Ensure that the adapter is correctly updated
//                if (photoAlbumAdapter != null) {
//                    photoAlbumAdapter.setPhotoList(receivedPhotoList);
//                }
//            } else {
//                Log.e("NewJournalDetailActivity", "Received photo paths list is null or empty");
//            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            // Pass the imageBitmap to EditJournalDetailActivity
            Intent intent = new Intent(this, EditJournalDetailActivity.class);
            intent.putExtra("imageBitmap", imageBitmap);
//            startActivity(intent);
        }
    }

    private void retrieveUserSetting(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        lightSensorEnabled = preferences.getBoolean("LightSensorEnabled", true);

        int backgroundColor = preferences.getInt("backgroundColor", DEFAULT_COLOR);
        Log.d("NewJourneyActivity", "Retrieved Background Color: " + backgroundColor);
        Log.d("NewJourneyActivity", "I am being called");
        updateBackgroundColor(backgroundColor);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void updateBackgroundColor(int color){
        Log.d("MainActivityCheck", "Updating Background Color: " + color);
        int bgColor = (color == Color.WHITE) ? Color.WHITE : Color.BLACK;
        getWindow().getDecorView().setBackgroundColor(bgColor);

    }

    private final SensorEventListener sensorListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event) {
            float lightIntensity = event.values[0];

            LightSensorFunction.handleLightSensorChange( NewJournalDetailActivity.this, lightIntensity, lightSensorEnabled);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    //when user clicks 'view images', pass list of photo paths to this activity
}
