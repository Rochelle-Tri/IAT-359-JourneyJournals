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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedColor;
        // checks if the radio button with ID RADIO_ON_ID is checked
        if (checkedId == RADIO_ON_ID) {
            // checked, the selectedColor set to Color.WHITE
            selectedColor = Color.BLACK;
            Log.d("UserSettings", "Dark Mode: On");
        } else {
            // not checked, the selectedColor set to Color.WHITE
            selectedColor = Color.WHITE;
            Log.d("UserSettings", "Dark Mode: Off");
        }
//
//        saveSelectedColor(selectedColor);
        getWindow().getDecorView().setBackgroundColor(selectedColor);

        //declares a boolean variable lightSensorEnabled and initializes it with the result of the expression
        boolean lightSensorEnabled = (checkedId == RADIO_ON_ID); // checks whether a radio button with the ID RADIO_ON_ID is checked
        saveLightSensorSetting(lightSensorEnabled);

        //If lightSensorEnabled is true, sets styleResId to R.style.DarkModeTextStyle
        int styleResId = (lightSensorEnabled) ? R.style.DarkModeTextStyle : R.style.LightModeTextStyle; //might change based on whether the light sensor is enabled or not
        applyTextStyleToViews(styleResId); // applies the selected text style (styleResId) to the views in the user interface

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

    //this is if the user presses the cancel button
    public void goBack(View view){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
        Intent saveResult = new Intent();
        boolean lightSensorEnabled = radioGroupColors.getCheckedRadioButtonId() == RADIO_ON_ID;

        // Save the light sensor setting
        saveResult.putExtra("LightSensorEnabled", lightSensorEnabled);
        setResult(RESULT_OK, saveResult);

        // update background color based on the light sensor setting
        int backgroundColor = (lightSensorEnabled) ? Color.WHITE : Color.WHITE;
        saveSelectedColor(backgroundColor);

        // finish the activity
        finish();
    }

    //this is if the user presses the save data button
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        firstName = firstNameEditText.getText().toString();

        //when user is done inputting their name and turning light sensor on/off, upon click it will save their preferences
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

        finish();
    }


}
