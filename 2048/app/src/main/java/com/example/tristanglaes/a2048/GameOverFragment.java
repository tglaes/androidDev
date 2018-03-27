package com.example.tristanglaes.a2048;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Tristan Glaes
 */
public class GameOverFragment extends DialogFragment {

    public GameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_over, container, false);
        final GameActivity ga = (GameActivity) getActivity();
        Button newGame = v.findViewById(R.id.newGameGameOverFrag);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ga.startNewGame();
                ga.gameTimer.restartTimer();
                dismiss();
            }
        });

        Button viewBoard = v.findViewById(R.id.viewBoardButton);
        viewBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Füge die Punktzahl in die Meldung ein.
        TextView gameOverTv = v.findViewById(R.id.gameOverTv);
        gameOverTv.setText(gameOverTv.getText().toString() + ga.pointsTv.getText().toString().substring(7));


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
