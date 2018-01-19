package com.example.tristanglaes.a2048;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    private SharedPreferences sp;
    public static String HIGH_SCORE_KEY = "com.example.tristanglaes.a2048.HIGHSCORE";
    private List<String> highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        highscores = new ArrayList<>();

        for(int i = 1; i<=10;i++){

            String score = sp.getString("com.example.tristanglaes.a2048.HIGHSCORE" + i, "");
            if(score.equals("")){
                break;
            } else {
                highscores.add(score);
            }
        }

        Toast toast = Toast.makeText(getApplicationContext(), highscores.toString(), Toast.LENGTH_LONG);
        toast.show();
    }
}
