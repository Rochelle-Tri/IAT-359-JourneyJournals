package com.example.journeyjournals;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;

import android.text.TextUtils;

import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewJournalDetailActivity extends AppCompatActivity {

    EditText journeyNameTV, journeyLocationTV, journeyDateTV, journeyDurationTV, journeyNotesTV;

    private MyDataBase db;

    //light sensor stuff-------------------------------------------
    private LightSensorFunction lightSensorFunction;
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final String MY_PREFS = "MyPrefs";
    private boolean lightSensorEnabled = true;
    final int REQUEST_CODE = 0;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private Bitmap imageBitmap;
    private String currentPhotoPath;  // Add this variable

    private int entryId; // Declare the entryId variable here



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


        // Retrieve the entry ID from Intent extras
        Bundle extra_data = getIntent().getExtras();
        if (extra_data != null) {
            entryId = extra_data.getInt("entryId", -1);
        } else {
            Toast.makeText(this, "No entryId", Toast.LENGTH_SHORT).show();
        }

    }
    public void saveData (View view) {
//        Log.d("myTag", "smt went wrong");
        String name = journeyNameTV.getText().toString();
        String location = journeyLocationTV.getText().toString();
        String date = journeyDateTV.getText().toString();
        String duration = journeyDurationTV.getText().toString();
        String notes = journeyNotesTV.getText().toString();

        //get the checklist data from the previous page
        Bundle extra_data = getIntent().getExtras();
        String checklist = extra_data.getString("CHECKLIST_KEY");

        // check if all fields are filled out before data can be inserted in the database
        // cCheck if imageBitmap is null before proceeding
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(date) || TextUtils.isEmpty(duration)) {
            Toast.makeText(this, "Please input all fields before saving", Toast.LENGTH_SHORT).show();
        } else {
            // Load the full-sized image using the currentPhotoPath
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);

            // Check if imageBitmap is null before proceeding
            if (imageBitmap != null) {
                // Save the image path to the database
                String imagePath = saveImageToFile(imageBitmap);

                // Save the image path and other fields to the database
                long id = db.insertData(name, location, date, duration, notes, checklist, imagePath);
                if (id < 0) {
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Your journal entry was created", Toast.LENGTH_SHORT).show();
                }
                finish();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                // Handle the case where imageBitmap is null
                Toast.makeText(this, "Failed to load image. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goHome(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void openCamera(View view) {
        // check if camera permission is granted

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // permission is granted, open the camera

            // Create an Intent for capturing an image
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // creates a file for the photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // starts CameraActivity with the entryId as an extra
                cameraIntent.putExtra("entryId", entryId);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            // permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        // save the orientation information in the EXIF data
        saveOrientationToExif(image.getAbsolutePath());
        return image;
    }

    private void saveOrientationToExif(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = ExifInterface.ORIENTATION_NORMAL;

            // find the orientation based on the device's camera sensor
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ExifInterface.ORIENTATION_NORMAL;
                    break;
                case Surface.ROTATION_90:
                    orientation = ExifInterface.ORIENTATION_ROTATE_90;
                    break;
                case Surface.ROTATION_180:
                    orientation = ExifInterface.ORIENTATION_ROTATE_180;
                    break;
                case Surface.ROTATION_270:
                    orientation = ExifInterface.ORIENTATION_ROTATE_270;
                    break;
            }

            exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
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


    public void displayPhoto(View view) {

        if (currentPhotoPath != null) {
            // Pass the currentPhotoPath to ViewImagesActivity
            Intent intent = new Intent(this, ViewImagesActivity.class);
            intent.putExtra("photoPath", currentPhotoPath);
            startActivity(intent);
        } else {
            // Handle the case where the currentPhotoPath is null (no photo captured)
            Toast.makeText(this, "No photo captured yet", Toast.LENGTH_SHORT).show();
        }
    }


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
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            // Check if user disabled light sensor in settings
            lightSensorEnabled = data.getBooleanExtra("LightSensorEnabled", true);

            // Retrieve user settings and update UI
            retrieveUserSetting();

            // Get the captured image file
            File imageFile = new File(currentPhotoPath);

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);

        }
    }

    private String saveImageToFile(Bitmap bitmap) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void retrieveUserSetting(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        lightSensorEnabled = preferences.getBoolean("LightSensorEnabled", true);

        int backgroundColor = preferences.getInt("backgroundColor", DEFAULT_COLOR);
        Log.d("NewJourneyActivity", "Retrieved Background Color: " + backgroundColor);

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
