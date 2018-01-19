package com.example.tristanglaes.a2048;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button playBtn, highscoresBtn, exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initializiere Buttons und füge ihnen OnClickListeners hinzu.
        playBtn = findViewById(R.id.playBtn);
        highscoresBtn = findViewById(R.id.highScoreBtn);
        exitBtn = findViewById(R.id.exitBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        highscoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(HighscoreActivity.HIGH_SCORE_KEY + 1,"1234567");
                editor.putString(HighscoreActivity.HIGH_SCORE_KEY + 2,"1000000");
                editor.apply();

                Intent i = new Intent(MenuActivity.this, HighscoreActivity.class);
                startActivity(i);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}