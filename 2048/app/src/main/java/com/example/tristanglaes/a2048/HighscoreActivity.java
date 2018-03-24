package com.example.tristanglaes.a2048;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;




public class HighscoreActivity extends Activity {

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
        List<Integer> intList = new ArrayList<>();
        List<String> stringList = new ArrayList<>(intList.size());


        for(int i = 1; i<=11;i++){




            String score = sp.getString(HIGH_SCORE_KEY + i, "");
            //int scoreToInt = Integer.parseInt(score);
            if(score.equals("")){
                break;
            } else {
                    highscores.add(score);
            }
        }
        // umwandeln von StringList in IntList
        for(String numeric : highscores)
        {
            intList.add(Integer.parseInt(numeric));
        }
        Collections.sort(intList, Collections.reverseOrder());
        if (intList.size() == 11) {
            // Entfernt das elfte Element
            intList.remove(10);
        }
        // von IntList zurück in StringList
        for(Integer myInt : intList) {
            stringList.add(String.valueOf(myInt));
        }

        for (int j = 1; j <= 10; j++)
        {
            sp.edit().putString(HIGH_SCORE_KEY + j, stringList.get(j-1)).apply();
        }
        if(stringList.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "No highscores yet!", Toast.LENGTH_LONG);
            toast.show();
        } else {

            adapter.addAll(stringList);
            highscoreListView.setAdapter(adapter);
        }
    }
}