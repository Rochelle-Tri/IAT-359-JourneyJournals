package com.example.journeyjournals;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    private CameraProvider cameraProvider;

    private Button buttonCaptureSave, buttonCaptureShow;
    private ImageCapture imageCapture;
    private ImageView imageViewCaptured;

    private static final int img_id = 1;

    // new
    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button buttonCaptureSave = findViewById(R.id.buttonCaptureSave);
        Button buttonCaptureShow = findViewById(R.id.buttonCaptureShow);
        imageViewCaptured = findViewById(R.id.imageViewCapturedImg);

        buttonCaptureSave.setOnClickListener(this);
        buttonCaptureShow.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.buttonCaptureSave: {
//                capturePhoto();
//                break;
//            }
//            case R.id.buttonCaptureShow: {
//                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(camera_intent, img_id);
//                break;
//            }
//        }

        int viewId = view.getId();

        if (viewId == R.id.buttonCaptureSave) {
            capturePhoto();
        } else if (viewId == R.id.buttonCaptureShow) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, img_id);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap photo = (Bitmap) data.getExtras().get("data");
//        imageViewCaptured.setImageBitmap(photo);

        // Check if the result is OK and data is not null
        if (resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();

            // Check if extras is not null
            if (extras != null) {
                // Perform your operations with data
                Bitmap photo = (Bitmap) extras.get("data");
                imageViewCaptured.setImageBitmap(photo);
            } else {
                // Handle the case where extras is null
                Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the result is not OK
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void capturePhoto() {
//        long timeStamp = System.currentTimeMillis();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//
//
//        imageCapture.takePicture(
//                new ImageCapture.OutputFileOptions.Builder(
//                        getContentResolver(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        contentValues
//                ).build(),
//                getExecutor(),
//                new ImageCapture.OnImageSavedCallback() {
//                    @Override
//                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                        Toast.makeText(CameraActivity.this, "Saving...", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(@NonNull ImageCaptureException exception) {
//                        Toast.makeText(CameraActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

        long timeStamp = System.currentTimeMillis();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File photoFile = null;
        try {
            photoFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photoFile != null) {
            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(photoFile).build();

            imageCapture.takePicture(
                    outputFileOptions,
                    getExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Toast.makeText(CameraActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Toast.makeText(CameraActivity.this, "Error saving image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        CameraX.unbindAll();
//    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        // Release camera resources or other resources here
//        if (cameraProvider != null) {
//            cameraProvider.unbindAll();
//        }
//    }
}