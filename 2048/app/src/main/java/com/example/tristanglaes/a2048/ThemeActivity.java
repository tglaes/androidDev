package com.example.tristanglaes.a2048;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
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
    }
}