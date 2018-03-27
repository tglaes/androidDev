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

/**
 * @author Iurie Golovencic, Meris Krupic, Tristan Glaes
 */
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

        // Füge Highscores aus S.P in Liste ein
        for(int i = 1; i<=11;i++){

            String score = sp.getString(HIGH_SCORE_KEY + i, "");
            if(score.equals("")){
                break;
            } else {
                    highscores.add(score);
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

        if(stringList.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(), "No highscores yet!", Toast.LENGTH_LONG);
            toast.show();
        } else {

            // Die Stelle des Highscores anzeigen.
            for(int i = 0; i < stringList.size(); i++){

                stringList.set(i,(i + 1) + ". " + stringList.get(i));
            }

            adapter.addAll(stringList);
            highscoreListView.setAdapter(adapter);
        }
    }
}