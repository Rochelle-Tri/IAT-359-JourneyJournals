package com.example.journeyjournals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.journeyjournals.LightSensorFunction;

public class UserSettings extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    private Button saveButton;
    private EditText firstNameEditText;
    private RadioGroup radioGroupColors;
    String firstName;
    private TextView displayUserName;
    private static final int RADIO_ON_ID = R.id.radioOn;
    private static final int RADIO_OFF_ID = R.id.radioOff;
    private static final String MY_PREFS = "MyPrefs";
    private static final int DEFAULT_COLOR = Color.WHITE;
    private LightSensorFunction lightSensorFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);

        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);

        radioGroupColors = (RadioGroup) findViewById(R.id.radioGroupColors);
        radioGroupColors.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) this);

        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        boolean lightSensorEnabled = preferences.getBoolean("LightSensorEnabled", true);

        // Set the initial state of the radio group based on the saved preference
        if (lightSensorEnabled) {
            radioGroupColors.check(RADIO_ON_ID);
        } else {
            radioGroupColors.check(RADIO_OFF_ID);
        }
        lightSensorFunction = new LightSensorFunction();

    }

//    public void saveData(View view){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }

//    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
//
//    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedColor;
        if (checkedId == RADIO_ON_ID) {
            selectedColor = Color.BLACK;
            Log.d("UserSettings", "Dark Mode: On");
        } else {
            selectedColor = Color.WHITE;
            Log.d("UserSettings", "Dark Mode: Off");
        }
//
//        saveSelectedColor(selectedColor);
        getWindow().getDecorView().setBackgroundColor(selectedColor);

        boolean lightSensorEnabled = (checkedId == RADIO_ON_ID);
        saveLightSensorSetting(lightSensorEnabled);

        int styleResId = (lightSensorEnabled) ? R.style.DarkModeTextStyle : R.style.LightModeTextStyle;
        applyTextStyleToViews(styleResId);

        Log.d("UserSettings", "Light Sensor Enabled: " + lightSensorEnabled);
    }

    private void saveLightSensorSetting(boolean lightSensorEnabled) {
        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("LightSensorEnabled", lightSensorEnabled);
        editor.apply();
    }

    private void saveSelectedColor(int selectedColor) {
        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        getWindow().getDecorView().setBackgroundColor(selectedColor);
        editor.putInt("BackgroundColor", selectedColor);
        editor.apply();
    }

//    private int getInitialBackgroundColor() {
//        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
//        Log.d("UserSettingsCheck", "Is being returned");
//        return preferences.getInt("BackgroundColor", DEFAULT_COLOR);
//
//    }

    private void applyTextStyleToViews(int styleResId) {
        // Dynamically find and update the style for all TextViews
        ViewGroup rootView = findViewById(android.R.id.content);
        applyTextStyleRecursive(rootView, styleResId);
    }

    private void applyTextStyleRecursive(View view, int styleResId) {
        if (view instanceof TextView) {
            ((TextView) view).setTextAppearance(styleResId);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyTextStyleRecursive(viewGroup.getChildAt(i), styleResId);
            }
        }
    }

    public void goBack(View view){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
        Intent saveResult = new Intent();
        boolean lightSensorEnabled = radioGroupColors.getCheckedRadioButtonId() == RADIO_ON_ID;

        // Save the light sensor setting
        saveResult.putExtra("LightSensorEnabled", lightSensorEnabled);
        setResult(RESULT_OK, saveResult);

        // Update background color based on the light sensor setting
        int backgroundColor = (lightSensorEnabled) ? Color.WHITE : Color.WHITE;
        saveSelectedColor(backgroundColor);

//        LinearLayout mainLayout = findViewById(R.id.activity_main);
//        if (mainLayout != null) {
//            mainLayout.setBackgroundColor(backgroundColor);
//        }

        // Finish the activity
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        firstName = firstNameEditText.getText().toString();

        SharedPreferences preferences = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("First Name", firstName);
        editor.commit();

        int selectedColor;
        int checkedRatioButtonId = radioGroupColors.getCheckedRadioButtonId();

        if (checkedRatioButtonId == R.id.radioOn) {
            selectedColor = Color.BLACK;
//            getWindow().getDecorView().setBackgroundColor(selectedColor);
            Log.d("UserSettings", "Dark Mode: On");
        } else if (checkedRatioButtonId == R.id.radioOff) {
            selectedColor = Color.WHITE;
//            getWindow().getDecorView().setBackgroundColor(selectedColor);
            Log.d("UserSettings", "Dark Mode: Off");
        } else {
            selectedColor = Color.WHITE;
//            getWindow().getDecorView().setBackgroundColor(selectedColor);
            Log.d("UserSettings", "Dark Mode: Off");
        }

//        getWindow().getDecorView().setBackgroundColor(selectedColor);
        saveSelectedColor(selectedColor);

//        Intent retrieveIntent = getIntent();
//        retrieveIntent.putExtra("First Name", firstName);
//        getWindow().getDecorView().setBackgroundColor(selectedColor);
//        retrieveIntent.putExtra("BackgroundColor", selectedColor);
//        setResult(RESULT_OK, retrieveIntent);
//        getWindow().getDecorView().setBackgroundColor(selectedColor);
        finish();
    }


}
