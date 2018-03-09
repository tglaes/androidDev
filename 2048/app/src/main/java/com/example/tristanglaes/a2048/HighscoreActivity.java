package com.example.tristanglaes.a2048;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    private SharedPreferences sp;
    public static String HIGH_SCORE_KEY = "com.example.tristanglaes.a2048.HIGHSCORE";
    private List<String> highscores;
    private ListView highscoreListView;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highscoreListView = findViewById(R.id.highscoreListView);
        adapter = new ArrayAdapter<>(HighscoreActivity.this, R.layout.support_simple_spinner_dropdown_item);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        highscores = new ArrayList<>();

        for(int i = 1; i<=10;i++){

            String score = sp.getString(HIGH_SCORE_KEY + i, "");
            if(score.equals("")){
                break;
            } else {
                highscores.add( i + ". " + score);
            }
        }
        if(highscores.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "No highscores yet!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            adapter.addAll(highscores);
            highscoreListView.setAdapter(adapter);
        }
    }
}
