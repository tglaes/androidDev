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
        List<String> stringListCopy = new ArrayList<>();


        // Füge Highscores aus S.P in Liste ein
        for(int i = 1; i<=11;i++){

            String score = sp.getString(HIGH_SCORE_KEY + i, "");
            if(score.equals("")){
                break;
            } else {
                    highscores.add(score);

                /*
                sp.edit().putString(HIGH_SCORE_KEY +1, "1600").apply();
                sp.edit().putString(HIGH_SCORE_KEY +2, "1538").apply();
                sp.edit().putString(HIGH_SCORE_KEY +3, "1400").apply();
                sp.edit().putString(HIGH_SCORE_KEY +4, "1100").apply();
                sp.edit().putString(HIGH_SCORE_KEY +5, "900").apply();
                sp.edit().putString(HIGH_SCORE_KEY +6, "700").apply();
                sp.edit().putString(HIGH_SCORE_KEY +7, "500").apply();
                sp.edit().putString(HIGH_SCORE_KEY +8, "600").apply();
                sp.edit().putString(HIGH_SCORE_KEY +9, "650").apply();
                sp.edit().putString(HIGH_SCORE_KEY +10, "767").apply();
                */
            }
        }

        // umwandeln von highscores in IntList
        for(String numeric : highscores)
        {
            intList.add(Integer.parseInt(numeric));
        }
        Collections.sort(intList, Collections.reverseOrder());
        if (intList.size() == 11) {
            // Entfernt das elfte (kleinste) Element
            intList.remove(10);
        }

        // von IntList zurück in StringList
        for(Integer myInt : intList) {
            //stringList.add(index++ + ". " + String.valueOf(myInt));
            stringList.add(String.valueOf(myInt));
        }

        // StringList wird sortiert zurück in SP geschrieben
        for (int j = 1; j <= 10; j++)
        {
            if(!(sp.getString(HIGH_SCORE_KEY + 11, "").isEmpty())) {
                sp.edit().putString(HIGH_SCORE_KEY + j, stringList.get(j - 1)).apply();
                sp.edit().putString(HIGH_SCORE_KEY + 11, "0").apply();
            }
        }
        /*
        // StringList erweitern um 1. Platz, 2. Platz ...
        stringListCopy.clear();
        int stringListSize = stringList.size();
        for (int k = 0; k <= stringListSize; k++)
        {
            if(!(stringList.get(k) == null || stringListSize ==0)) {
                stringListCopy.add(k, k + 1 + ". " + stringList.get(k));
            }
            //String strScore = stringList.get(k);
            /*
            strScore = k+1 + ". " + strScore;
            stringList.remove(0);
            stringList.add(strScore);

        }
        */
        if(stringList.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "No highscores yet!", Toast.LENGTH_LONG);
            toast.show();
        } else {

            adapter.addAll(stringList);
            highscoreListView.setAdapter(adapter);
        }
    }
}