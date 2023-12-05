package com.example.journeyjournals;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LightSensorFunction extends AppCompatActivity {
    public static void handleLightSensorChange(AppCompatActivity context, float lightIntensity, boolean lightSensorEnabled) {
        if (lightSensorEnabled) {
            // Automatically adjust background and font colors based on light intensity
            if (lightIntensity < 5) {
                updateBackgroundColor( context, Color.BLACK);
                applyTextStyleToViews( context, R.style.DarkModeTextStyle);
                Log.d("UserSettings", "Dark Mode: On");
            } else {
                updateBackgroundColor( context, Color.WHITE);
                applyTextStyleToViews(context, R.style.LightModeTextStyle);
                Log.d("UserSettings", "Dark Mode: Off");
            }
        } else {
            // Handle the case when the light sensor is disabled
            updateBackgroundColor( context, Color.WHITE);
            applyTextStyleToViews( context, R.style.LightModeTextStyle);
            Log.d("UserSettings", "Light sensor disabled");
        }
    }

    private static void updateBackgroundColor(AppCompatActivity context, int color) {
        // Update the background color based on the color parameter
//        if (context instanceof AppCompatActivity) {
//            ((AppCompatActivity) context).getWindow().getDecorView().setBackgroundColor(color);
//        }
        context.getWindow().getDecorView().setBackgroundColor(color);

    }

    private static void applyTextStyleToViews(AppCompatActivity context, int styleResId) {
        // Dynamically find and update the style for all TextViews
//        if (context instanceof AppCompatActivity) {
//            ViewGroup rootView = ((AppCompatActivity) context).findViewById(android.R.id.content);
//            applyTextStyleRecursive(rootView, styleResId);
//        }

        ViewGroup rootView = context.findViewById(android.R.id.content);
        applyTextStyleRecursive(rootView, styleResId);
    }

    private static void applyTextStyleRecursive(View view, int styleResId) {
        if (view instanceof TextView) {
            ((TextView) view).setTextAppearance(styleResId);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyTextStyleRecursive(viewGroup.getChildAt(i), styleResId);
            }
        }
    }
}
