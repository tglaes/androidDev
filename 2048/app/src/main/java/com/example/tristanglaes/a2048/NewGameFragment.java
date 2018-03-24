package com.example.tristanglaes.a2048;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewGameFragment extends DialogFragment {

    private Button newGame, cancel;

    public NewGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_game, container, false);

        newGame = v.findViewById(R.id.newGameButton);
        cancel = v.findViewById(R.id.cancelNewGameButton);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity ga = (GameActivity) getActivity();
                ga.startNewGame();
                ga.gameTimer.restartTimer();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity ga = (GameActivity) getActivity();
                ga.gameTimer.startTimer();
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        GameActivity ga = (GameActivity) getActivity();
        ga.gameTimer.startTimer();
        super.onDetach();
    }
}
