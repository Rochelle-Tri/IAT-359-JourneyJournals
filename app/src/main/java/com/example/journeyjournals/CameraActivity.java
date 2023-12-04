//package com.example.journeyjournals;
//
//import android.annotation.SuppressLint;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.Camera;
//import androidx.camera.core.CameraControl;
//import androidx.camera.core.CameraProvider;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.CameraX;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCaptureException;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.lifecycle.LifecycleOwner;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
//    private ActivityResultLauncher<Intent> startActivityForResult; // Declare as a class-level variable
//
//    PreviewView previewView;
//    private CameraProvider cameraProvider;
//
//    private Button photoSave, cameraShotAndShow;
//    private ImageCapture imageCapture;
//    private ImageView viewCapturedImg;
//
//    private static final int img_id = 1;
//
//    // new
//    private Executor executor = Executors.newSingleThreadExecutor();
//    private int REQUEST_CODE_PERMISSIONS = 1001;
//    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
//
//    private static final int REQUEST_IMAGE_CAPTURE = 1;
//    private List<String> photoList = new ArrayList<>();
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        Log.d("CameraActivity", "Checking: onCreate");
//
//        Button photoSave = findViewById(R.id.photoSave);
//        Button cameraShotAndShow = findViewById(R.id.cameraShotAndShow);
//        viewCapturedImg = findViewById(R.id.viewCapturedImg);
//
//        photoSave.setOnClickListener(this);
//        cameraShotAndShow.setOnClickListener(this);
//
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//
//        if (allPermissionsGranted()) {
//            startCamera();
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }
//
//        // initialize the launcher in onCreate
//        if (startActivityForResult == null) {
//            startActivityForResult = registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    result -> {
//                        if (result.getResultCode() == RESULT_OK) {
//                            Intent data = result.getData();
//                            if (data != null) {
//                                ArrayList<String> photoList = data.getStringArrayListExtra("photoList");
//                                // Handle the updated photoList as needed
//                            }
//                        }
//                    }
//            );
//        }
//    }
//
//    private Executor getExecutor() {
//        return ContextCompat.getMainExecutor(this);
//    }
//
//    private void startCamera() {
//
//        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//
//        cameraProviderFuture.addListener(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                    bindPreview(cameraProvider);
//
//                } catch (ExecutionException | InterruptedException e) {
//                    // This should never be reached.
//                }
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("CameraActivity", "Checking: onResume");
//        if (allPermissionsGranted()) {
//            startCamera();
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }
//    }
//    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
//
//        cameraProvider.unbindAll();
//
//        Preview preview = new Preview.Builder()
//                .build();
//
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//
//
//        imageCapture = new ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build();
////        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
//    }
//
//    @Override
//    public void onClick(View view) {
////        switch (view.getId()) {
////            case R.id.buttonCaptureSave: {
////                capturePhoto();
////                break;
////            }
////            case R.id.buttonCaptureShow: {
////                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                startActivityForResult(camera_intent, img_id);
////                break;
////            }
////        }
//
//        int viewId = view.getId();
//
//        if (viewId == R.id.photoSave) {
//            capturePhoto();
//        } else if (viewId == R.id.cameraShotAndShow) {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            // Use the launcher to start the activity for result
//            if (startActivityForResult != null) {
//                startActivityForResult.launch(cameraIntent);
//            } else {
//                Log.e("CameraActivity", "startActivityForResult is null");
//            }
//        }
//
//    }
//
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        Bitmap photo = (Bitmap) data.getExtras().get("data");
////        imageViewCaptured.setImageBitmap(photo);
//
//        //check if the result is OK and data is not null
//        if (resultCode == RESULT_OK && data != null) {
//            Bundle extras = data.getExtras();
//
//            //check if extras is not null
//            if (extras != null) {
//                // Perform your operations with data
//                Bitmap photo = (Bitmap) extras.get("data");
//                viewCapturedImg.setImageBitmap(photo);
//            } else {
//                //case where extras is null
//                Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            //case where the result is not OK
//            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//
////    private void capturePhoto() {
////
////        long timeStamp = System.currentTimeMillis();
////        String imageFileName = "JPEG_" + timeStamp + "_";
////        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////
////        File photoFile = null;
////        try {
////            photoFile = File.createTempFile(
////                    imageFileName,  /* prefix */
////                    ".jpg",         /* suffix */
////                    storageDir      /* directory */
////            );
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////        if (photoFile != null) {
////            ImageCapture.OutputFileOptions outputFileOptions =
////                    new ImageCapture.OutputFileOptions.Builder(photoFile).build();
////
////            imageCapture.takePicture(
////                    outputFileOptions,
////                    getExecutor(),
////                    new ImageCapture.OnImageSavedCallback() {
////                        @Override
////                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
////                            Toast.makeText(CameraActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
////                        }
////
////                        @Override
////                        public void onError(@NonNull ImageCaptureException exception) {
////                            Toast.makeText(CameraActivity.this, "Error saving image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
////                        }
////                    }
////            );
////        }
////    }
//
//    private boolean allPermissionsGranted() {
//
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void goHome(View view){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }
//    public void goBack(View view){
//        Intent intent = new Intent(this, NewJournalDetailActivity.class);
//        intent.putStringArrayListExtra("photoList", new ArrayList<>(photoList));
//        startActivity(intent);
//    }
//
//    // store multiple photo files here:
//
//    // Add this field to your CameraActivity ----------------------------------------------------------------------------------------------
////    private List<String> photoPaths = new ArrayList<>();
//
//    private void capturePhoto() {
//        long timeStamp = System.currentTimeMillis();
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//        File photoFile = null;
//        try {
//            photoFile = File.createTempFile(
//                    imageFileName,  /* prefix */
//                    ".jpg",         /* suffix */
//                    storageDir      /* directory */
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (photoFile != null) {
//            ImageCapture.OutputFileOptions outputFileOptions =
//                    new ImageCapture.OutputFileOptions.Builder(photoFile).build();
//
//            File finalPhotoFile = photoFile;
//            imageCapture.takePicture(
//                    outputFileOptions,
//                    getExecutor(),
//                    new ImageCapture.OnImageSavedCallback() {
//                        @Override
//                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                            Toast.makeText(CameraActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
//
////                            //log size of photolist after capturing photo
////                            Log.d("CameraActivity", "Photo list size after capturing a photo: " + photoList.size());
//                            Log.d("CameraActivity", "Photo path: " + finalPhotoFile.getAbsolutePath());
//
//                            // Now that the image is saved, add the photo path to the list
//                            String photoPath = finalPhotoFile.getAbsolutePath();
//                            photoList.add(photoPath);  // Store the photo path in the list
////                            finish();
//
//                            // Log size of the class-level photoList after capturing photo
//                            Log.d("CameraActivity", "Photo list size after capturing a photo: " + photoList.size());
//
//                            // send the updated photoList back to NewJournalDetailActivity
//                            setResultAndFinish(photoList);
//
////                            Intent resultIntent = new Intent();
////                            resultIntent.putStringArrayListExtra("photoList", (ArrayList<String>) photoList);
////                            setResult(RESULT_OK, resultIntent);
//                        }
//
//                        @Override
//                        public void onError(@NonNull ImageCaptureException exception) {
//                            Toast.makeText(CameraActivity.this, "Error saving image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//        }
//    }
//
//    private void setResultAndFinish(List<String> updatedPhotoPaths) {
//
//        Log.d("CameraActivity", "setResultAndFinish called");
//        Intent resultIntent = new Intent();
//        resultIntent.putStringArrayListExtra("photoList", (ArrayList<String>) updatedPhotoPaths);
//        setResult(RESULT_OK, resultIntent);
//
//        Log.d("CameraActivity", "Set result and finish. Photo paths: " + (updatedPhotoPaths != null ? updatedPhotoPaths.toString() : "null"));
//
////        finish();
//
//        Intent intent = new Intent(this, NewJournalDetailActivity.class);
//        intent.putStringArrayListExtra("photoList", new ArrayList<>(photoList));
//        startActivity(intent);
//    }
//    // Add this method to get the list of photo paths
//    public List<String> getphotoList() {
//        return photoList;
//    }
//}