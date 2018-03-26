package com.example.tristanglaes.a2048;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ThemeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        RadioGroup radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioGroupTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ThemeActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                switch (i){
                    case R.id.blue_theme: editor.putString(GameActivity.THEME_KEY, "Blue");
                        break;
                    case R.id.black_theme: editor.putString(GameActivity.THEME_KEY, "Violet");
                        break;
                    case R.id.green_theme: editor.putString(GameActivity.THEME_KEY, "Green");
                        break;
                    case R.id.red_theme: editor.putString(GameActivity.THEME_KEY, "Red");
                        break;
                }
                editor.apply();
            }
        });

        // Auswählen des aktuellen Themes.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ThemeActivity.this);
        RadioButton rb;
        switch(preferences.getString(GameActivity.THEME_KEY, "Blue")){

            case "Blue":
                rb = findViewById(R.id.blue_theme);
                break;
            case "Violet":
                rb = findViewById(R.id.black_theme);
                break;
            case "Green":
                rb = findViewById(R.id.green_theme);
                break;
            case "Red":
                rb = findViewById(R.id.red_theme);
                break;
                default:
                    rb = findViewById(R.id.blue_theme);
                    break;
        }
        rb.setChecked(true);
    }
}