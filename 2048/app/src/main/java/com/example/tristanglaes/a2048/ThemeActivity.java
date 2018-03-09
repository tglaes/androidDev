package com.example.tristanglaes.a2048;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class ThemeActivity extends AppCompatActivity {

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
                    case 2131165223: editor.putString(GameActivity.THEME_KEY, "Blue");
                        break;
                    case 2131165221: editor.putString(GameActivity.THEME_KEY, "Violet");
                        break;
                    case 2131165246: editor.putString(GameActivity.THEME_KEY, "Green");
                        break;
                    case 2131165283: editor.putString(GameActivity.THEME_KEY, "Red");
                        break;
                }
                editor.apply();
            }
        });
    }


}
