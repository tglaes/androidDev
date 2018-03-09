package com.example.tristanglaes.a2048;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    // Keys für die SharedPreferences
    private static String GAME_BOARD_KEY = "com.example.tristanglaes.a2048.GAMEBOARD";
    private static String GAME_TIME_KEY = "com.example.tristanglaes.a2048.TIME";
    private static String GAME_MOVES_KEY = "com.example.tristanglaes.a2048.MOVES";
    private static String GAME_POINTS_KEY = "com.example.tristanglaes.a2048.POINTS";
    private static String GAME_STORED_KEY = "com.example.tristanglaes.a2048.STORED";
    public static String THEME_KEY = "com.example.tristanglaes.a2048.THEME";

    // TableLayout welches das Raster für das Spielfeld bilded.

    private TableLayout tl;

    // TextViews welche die GUI für Punkte, Spielzüge und Spielzeit ist.
    private static TextView pointsTv, movesTv, timeTv;
    // Das intere Spielfeld.
    private int board[][];
    // Die Breite des Boards.
    private int width;
    // Die Höhe des Boards.
    private int height;
    // Die Anzahl der Spielzüge.
    private int numberMoves;
    // Die Punkte.
    private int points;
    // Spielzeit in Sekunden.
    private int time;
    // Der neues Spiel Button.
    private Button newGameBtn;
    // Randomklasse zum berechnen der neuen Spielsteine und deren Position.
    private Random rand;
    // Timerklasse für die Spielzeit.
    private Timer timer;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        width = 4;
        height = 4;
        board = new int[width][height];
        time = 0;
        timer = new Timer();
        rand = new Random();
        pointsTv = findViewById(R.id.pointsTextView);
        movesTv = findViewById(R.id.movesTextView);
        timeTv = findViewById(R.id.timeTextView);
        newGameBtn = findViewById(R.id.newGameBtn);
        tl = findViewById(R.id.gameBoard);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        theme = preferences.getString(THEME_KEY,"Blue");


        tl.setOnTouchListener(new View.OnTouchListener() {

            private final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener());

            final class GestureListener extends GestureDetector.SimpleOnGestureListener {

                private static final int SWIPE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    boolean result = false;
                    try {
                        float diffY = e2.getY() - e1.getY();
                        float diffX = e2.getX() - e1.getX();
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
                                    onSwipeRight();
                                } else {
                                    onSwipeLeft();
                                }
                                result = true;
                            }
                        } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                            result = true;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return result;
                }
            }

            public void onSwipeRight() {
                if (isMovePossible(MoveDirection.EAST)) {
                    performMove(MoveDirection.EAST);
                    checkGame();
                }
            }

            public void onSwipeLeft() {
                if (isMovePossible(MoveDirection.WEST)) {
                    performMove(MoveDirection.WEST);
                    checkGame();
                }
            }

            public void onSwipeTop() {
                if (isMovePossible(MoveDirection.NORTH)) {
                    performMove(MoveDirection.NORTH);
                    checkGame();
                }
            }

            public void onSwipeBottom() {
                if (isMovePossible(MoveDirection.SOUTH)) {
                    performMove(MoveDirection.SOUTH);
                    checkGame();
                }
            }


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
                return;
            }
        });

        // Laden des letzten Spiels
        loadGame();
        // Starten des Timers
        timer.schedule(new GameTimer(), 0, 1000);
    }

    /**
     * Überprüft, ob der Spieler verloren (kein Zug mehr möglich) oder gewonnen (2048 Stein) hat.
     * Fall er gewonnen hat kann er auswählen ob er ein neues Spiel starten oder weiterspielen will.
     */
    public void checkGame() {
        addPiece();
        updateBoard(board);
        if (!isMovePossible()) {
            Toast toast = Toast.makeText(getApplicationContext(), "YOU LOST!", Toast.LENGTH_LONG);
            toast.show();
            //TODO: RUFE SPiel verloren auf.
            //TODO: Eintragen in Highscores.
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(HighscoreActivity.HIGH_SCORE_KEY, pointsTv.getText().toString());
            editor.apply();

        } else {
            //TODO: Checke ob der Spieler einen 2048 Stein hat -> Anzeigen des Gewonnenbildschirm und fragen ob er weiterspielen will.
            //TODO: Wenn er neues Spiel wählt Highscores.
        }
    }

    /**
     * Updated das Spielfeld, die Punkte und die Spielzüge.
     *
     * @param board
     */
    private void updateBoard(int board[][]) {

        TextView texts[] = new TextView[16];
        texts[0] = findViewById(R.id.text0);
        texts[1] = findViewById(R.id.text1);
        texts[2] = findViewById(R.id.text2);
        texts[3] = findViewById(R.id.text3);
        texts[4] = findViewById(R.id.text4);
        texts[5] = findViewById(R.id.text5);
        texts[6] = findViewById(R.id.text6);
        texts[7] = findViewById(R.id.text7);
        texts[8] = findViewById(R.id.text8);
        texts[9] = findViewById(R.id.text9);
        texts[10] = findViewById(R.id.text10);
        texts[11] = findViewById(R.id.text11);
        texts[12] = findViewById(R.id.text12);
        texts[13] = findViewById(R.id.text13);
        texts[14] = findViewById(R.id.text14);
        texts[15] = findViewById(R.id.text15);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int index = (j * 4) + i;
                String fieldValue = String.valueOf(board[i][j]).equals("0") ? "" : String.valueOf(board[i][j]);
                texts[index].setText(fieldValue);
                // Setzte die Farbe
                texts[index].setBackgroundColor(getColor(fieldValue));
            }
        }

        pointsTv.setText("SCORE: " + String.valueOf(points));
        movesTv.setText("MOVES: " + String.valueOf(numberMoves));
    }


    /**
     * Speichert das Spiel und bricht den Timer ab.
     */
    @Override
    protected void onStop() {
        super.onStop();
        safeGame();
        timer.cancel();
    }

    /**
     * Speichert das Spiel und bricht den Timer ab.
     */
    @Override
    protected void onPause() {
        super.onPause();
        safeGame();
        timer.cancel();
    }

    /**
     * Lädt das letzte Spiel.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadGame();
        updateBoard(board);
    }

    /**
     * Generiert einen neuen Stein.
     *
     * @return 2 (90% Chance) oder 4 (mit 10% Chance)
     */
    private int getNewPiece() {
        if (rand.nextInt(100) <= 89) {
            return 2;
        } else {
            return 4;
        }
    }

    /**
     * Initializiert das Spielfeld mit 0 und fügt 2 Startsteine hinzu.
     */
    private void initBoardPieces() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setPieceAt(i, j, 0);
            }
        }
        addPiece();
        addPiece();
    }

    /**
     * Setzt einen neuen Stein auf das Spielfeld.
     */
    public void addPiece() {
        int x, y;

        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);

        } while (getPieceAt(x, y) > 0);

        setPieceAt(x, y, getNewPiece());

    }

    /**
     * @return Die Anzahl der Steine auf dem Spielfeld.
     */
    public int getNumPieces() {
        int numPieces = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (getPieceAt(i, j) > 0) {
                    numPieces++;
                }
            }
        }
        Log.e(String.valueOf(numPieces), "Pieces:");
        return numPieces;
    }

    /**
     * @param x Breite des Boards
     * @param y Höhe des Boards
     * @return Den Stein an dem gewünschten Spielfeld.
     */
    public int getPieceAt(int x, int y) {
        return board[x][y];

    }

    /**
     * @return True, wenn noch mindestens ein Zug möglich ist, false sonst.
     */
    public boolean isMovePossible() {
        if (getNumPieces() != (width * height)) {
            return true;
        }

        return isMovePossible(MoveDirection.SOUTH) || isMovePossible(MoveDirection.WEST);
        // Move is possible when
        // they are 2 same
        // pieces in one row or column
        // Or obviously the board is
        // not full
    }

    /**
     * @param direction Die Richtung des Spielzuges.
     * @return True, wenn ein Zug in die Richtung möglich ist, false sonst.
     */
    public boolean isMovePossible(MoveDirection direction) {

        if (getNumPieces() == 0) {
            return false;
        }
        switch (direction) {
            case SOUTH:
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < (height - 1); j++) {
                        if (getPieceAt(i, j) > 0) {
                            if ((getPieceAt(i, j) == getPieceAt(i, j + 1)) || (getPieceAt(i, j + 1) == 0)) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case EAST:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < (width - 1); j++) {
                        if (getPieceAt(j, i) > 0) {
                            if ((getPieceAt(j, i) == getPieceAt(j + 1, i)) || (getPieceAt(j + 1, i) == 0)) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case WEST:
                for (int i = 0; i < height; i++) {
                    for (int j = (width - 1); j > 0; j--) {
                        if (getPieceAt(j, i) > 0) {
                            if ((getPieceAt(j, i) == getPieceAt(j - 1, i)) || (getPieceAt(j - 1, i) == 0)) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case NORTH:
                for (int i = 0; i < width; i++) {
                    for (int j = (height - 1); j > 0; j--) {
                        if (getPieceAt(i, j) > 0) {
                            if ((getPieceAt(i, j) == getPieceAt(i, j - 1)) || (getPieceAt(i, j - 1) == 0)) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    /**
     * Moves the piece to the next position (+1) if possible
     *
     * @param x
     * @param y
     * @param direction
     * @return true if the piece was moved otherwise false
     */
    private boolean movePieceSingleStep(int x, int y, MoveDirection direction) {

        if (direction == MoveDirection.SOUTH) {
            if (y == (height - 1)) {
                return false;
            }
            if (getPieceAt(x, y) > 0) {
                if (getPieceAt(x, y + 1) == 0) {
                    setPieceAt(x, y + 1, getPieceAt(x, y));
                    setPieceAt(x, y, 0);
                    return true;
                }
            }
        } else if (direction == MoveDirection.NORTH) {
            if (y == 0) {
                return false;
            }
            if (getPieceAt(x, y) > 0) {
                if (getPieceAt(x, y - 1) == 0) {
                    setPieceAt(x, y - 1, getPieceAt(x, y));
                    setPieceAt(x, y, 0);
                    return true;
                }
            }
        }
        if (direction == MoveDirection.WEST) {
            if (x == 0) {
                return false;
            }
            if (getPieceAt(x, y) > 0) {
                if (getPieceAt(x - 1, y) == 0) {
                    setPieceAt(x - 1, y, getPieceAt(x, y));
                    setPieceAt(x, y, 0);
                    return true;
                }
            }
        } else if (direction == MoveDirection.EAST) {
            if (x == (width - 1)) {
                return false;
            }
            if (getPieceAt(x, y) > 0) {
                if (getPieceAt(x + 1, y) == 0) {
                    setPieceAt(x + 1, y, getPieceAt(x, y));
                    setPieceAt(x, y, 0);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Moves the pieces as far as possible !NO MERGE YET!
     *
     * @param x
     * @param y
     * @param direction
     */
    private boolean movePiece(int x, int y, MoveDirection direction) {

        boolean result = false;
        if (direction == MoveDirection.SOUTH) {
            while (movePieceSingleStep(x, y, direction)) {
                y++;
                result = true;
            }

        } else if (direction == MoveDirection.NORTH) {
            while (movePieceSingleStep(x, y, direction)) {
                y--;
                result = true;
            }

        } else if (direction == MoveDirection.WEST) {
            while (movePieceSingleStep(x, y, direction)) {
                x--;
                result = true;
            }
        } else if (direction == MoveDirection.EAST) {
            while (movePieceSingleStep(x, y, direction)) {
                x++;
                result = true;
            }
        }
        return result;
    }

    /**
     * Moves the whole row either to EAST or WEST !NO MERGE YET!
     *
     * @param y
     * @param direction
     */
    private boolean moveRow(int y, MoveDirection direction) {

        boolean result = false;
        if (direction == MoveDirection.WEST) {
            for (int x = 1; x < width; x++) {
                boolean mresult = movePiece(x, y, direction); // Move all Pieces
                // in one
                // direction
                result = mresult || result;
            }
        } else if (direction == MoveDirection.EAST) {
            for (int x = (width - 2); x >= 0; x--) {
                boolean mresult = movePiece(x, y, direction); // Move all Pieces
                // in one
                // direction
                result = mresult || result;
            }
        }
        return result;
    }

    /**
     * Merges all pieces in one direction after a move has happened
     *
     * @param direction
     * @return false if no merge happened, true otherwise
     */
    private boolean mergeAll(MoveDirection direction) {
        boolean result = false;

        if (direction == MoveDirection.SOUTH
                || direction == MoveDirection.NORTH) {
            for (int x = 0; x < width; x++) {
                boolean mresult = mergeColumn(x, direction);
                result = mresult || result;
            }
        } else {
            for (int y = 0; y < height; y++) {
                boolean mresult = mergeRow(y, direction);
                result = mresult || result;
            }
        }
        return result;
    }

    /**
     * Merges a single row in one direction
     *
     * @param y
     * @param direction
     * @return false if no merge happened, true otherwise
     */
    private boolean mergeRow(int y, MoveDirection direction) {

        boolean result = false;
        if (direction == MoveDirection.WEST) {

            for (int x = 0; x < (width - 1); x++) {
                if (getPieceAt(x, y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x + 1, y)) {
                        setPieceAt(x + 1, y, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x, y));
                        result = true;
                    }
            }

        } else if (direction == MoveDirection.EAST) {
            for (int x = (width - 1); x > 0; x--) {
                if (getPieceAt(x, y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x - 1, y)) {
                        setPieceAt(x - 1, y, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x, y));
                        result = true;
                    }
            }
        }
        return result;
    }

    /**
     * Merges one single column in one direction
     *
     * @param x
     * @param direction
     * @return false if no merge happened, true otherwise
     */
    private boolean mergeColumn(int x, MoveDirection direction) {

        boolean result = false;
        if (direction == MoveDirection.SOUTH) {

            for (int y = (height - 1); y > 0; y--) {
                if (getPieceAt(x, y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x, (y - 1))) {
                        setPieceAt(x, y - 1, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x, y));
                        result = true;
                    }
            }

        } else if (direction == MoveDirection.NORTH) {
            for (int y = 0; y < (height - 1); y++) {
                if (getPieceAt(x, y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x, (y + 1))) {
                        setPieceAt(x, y + 1, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x, y));
                        result = true;
                    }
            }
        }
        return result;
    }

    /**
     * Moves the whole column either to SOUTH or WEST !NO MERGE YET!
     *
     * @param x
     * @param direction
     * @return true if something was moved, otherwise false
     */
    private boolean moveColumn(int x, MoveDirection direction) {

        boolean result = false;
        if (direction == MoveDirection.SOUTH) {
            for (int y = (height - 2); y >= 0; y--) {
                boolean mresult = movePiece(x, y, direction); // Move all Pieces
                // in one
                // direction
                result = mresult || result;
            }
        } else if (direction == MoveDirection.NORTH) {
            for (int y = 1; y < height; y++) {
                boolean mresult = movePiece(x, y, direction); // Move all Pieces
                // in one
                // direction
                result = mresult || result;
            }
        }
        return result;
    }

    /**
     * @param direction
     * @return
     */
    private boolean movePieces(MoveDirection direction) {
        boolean result = false;
        if (direction == MoveDirection.SOUTH
                || direction == MoveDirection.NORTH) {
            for (int j = 0; j < width; j++) {
                boolean mresult = moveColumn(j, direction);
                result = mresult || result;
            }
        } else {
            for (int y = 0; y < height; y++) {
                boolean mresult = moveRow(y, direction);
                result = mresult || result;
            }
        }
        return result;
    }

    /**
     * Führt einen Zug durch.
     *
     * @param direction
     * @return
     */
    public boolean performMove(MoveDirection direction) {

        if (movePieces(direction)) {
            numberMoves++;
            if (mergeAll(direction)) {
                movePieces(direction);
            }
            return true;
        } else {
            if (mergeAll(direction)) {
                numberMoves++;
                movePieces(direction);
                return true;
            }
        }
        return false;
    }

    /**
     * Setzt eine Stein mit einem Wert an die gewünschte Position.
     *
     * @param x
     * @param y
     * @param piece
     */
    public void setPieceAt(int x, int y, int piece) {
        board[x][y] = piece;
    }

    /**
     * Speichert den Spielstand(Anzahl der Spielzüge, Punkte, Spielzeit, Board) in den SharedPreferences.
     */
    private void safeGame() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                editor.putInt(GAME_BOARD_KEY + i + j, getPieceAt(i, j));
            }
        }
        editor.putInt(GAME_MOVES_KEY, numberMoves);
        editor.putInt(GAME_POINTS_KEY, points);
        editor.putInt(GAME_TIME_KEY, time);
        editor.putBoolean(GAME_STORED_KEY, true);
        editor.apply();
    }

    /**
     * Lädt ein Spiel aus den SharedPreferences.
     */
    private void loadGame() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = preferences.getInt(GAME_BOARD_KEY + i + j, 0);
            }
        }
        numberMoves = preferences.getInt(GAME_MOVES_KEY, 0);
        points = preferences.getInt(GAME_POINTS_KEY, 0);
        time = preferences.getInt(GAME_TIME_KEY, 0);
    }


    /**
     * @param seconds
     * @return Die Zeit im Format m:ss
     */
    private String convertTime(int seconds) {

        String time;

        int minutes = seconds / 60;
        int newSeconds = seconds % 60;

        if (newSeconds < 10) {
            time = minutes + ":0" + newSeconds;
        } else {
            time = minutes + ":" + newSeconds;
        }
        return time;
    }

    /**
     * Startet ein neues Spiel, indem alle Variabeln zurückgesetzt werden.
     */
    private void startNewGame() {

        time = 0;
        timeTv.setText("TIME: 0:00");
        points = 0;
        numberMoves = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = 0;
            }
        }
        initBoardPieces();
        updateBoard(board);
    }

    /**
     * Timer Taskklasse die von der Timerklasse jede Sekunde aufgerufen wird. Die run() Methode ruft die
     * Methode runOnUiThread auf und aktualisiert so die Spielzeit.
     */
    class GameTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeTv.setText("TIME: " + convertTime(time++));
                }
            });
        }
    }

    private int getColor(String fieldValue) {
        int color = 0;

        if (theme.equals("Blue")) {
            switch (fieldValue) {
                case "":
                    color = getResources().getColor(R.color.white);
                    break;
                case "2":
                    color = getResources().getColor(R.color.b2);
                    break;
                case "4":
                    color = getResources().getColor(R.color.b4);
                    break;
                case "8":
                    color = getResources().getColor(R.color.b8);
                    break;
                case "16":
                    color = getResources().getColor(R.color.b16);
                    break;
                case "32":
                    color = getResources().getColor(R.color.b32);
                    break;
                case "64":
                    color = getResources().getColor(R.color.b64);
                    break;
                case "128":
                    color = getResources().getColor(R.color.b128);
                    break;
                case "256":
                    color = getResources().getColor(R.color.b256);
                    break;
                case "512":
                    color = getResources().getColor(R.color.b512);
                    break;
                case "1024":
                    color = getResources().getColor(R.color.b1024);
                    break;
                case "2048":
                    color = getResources().getColor(R.color.b2048);
                    break;
                case "4096":
                    color = getResources().getColor(R.color.b4096);
                    break;

            }
        } else if (theme.equals("Violet")) {
            switch (fieldValue) {
                case "":
                    color = getResources().getColor(R.color.white);
                    break;
                case "2":
                    color = getResources().getColor(R.color.v2);
                    break;
                case "4":
                    color = getResources().getColor(R.color.v4);
                    break;
                case "8":
                    color = getResources().getColor(R.color.v8);
                    break;
                case "16":
                    color = getResources().getColor(R.color.v16);
                    break;
                case "32":
                    color = getResources().getColor(R.color.v32);
                    break;
                case "64":
                    color = getResources().getColor(R.color.v64);
                    break;
                case "128":
                    color = getResources().getColor(R.color.v128);
                    break;
                case "256":
                    color = getResources().getColor(R.color.v256);
                    break;
                case "512":
                    color = getResources().getColor(R.color.v512);
                    break;
                case "1024":
                    color = getResources().getColor(R.color.v1024);
                    break;
                case "2048":
                    color = getResources().getColor(R.color.v2048);
                    break;
                case "4096":
                    color = getResources().getColor(R.color.v4096);
                    break;
            }
        } else if (theme.equals("Green")) {
            switch (fieldValue) {
                case "":
                    color = getResources().getColor(R.color.white);
                    break;
                case "2":
                    color = getResources().getColor(R.color.g2);
                    break;
                case "4":
                    color = getResources().getColor(R.color.g4);
                    break;
                case "8":
                    color = getResources().getColor(R.color.g8);
                    break;
                case "16":
                    color = getResources().getColor(R.color.g16);
                    break;
                case "32":
                    color = getResources().getColor(R.color.g32);
                    break;
                case "64":
                    color = getResources().getColor(R.color.g64);
                    break;
                case "128":
                    color = getResources().getColor(R.color.g128);
                    break;
                case "256":
                    color = getResources().getColor(R.color.g256);
                    break;
                case "512":
                    color = getResources().getColor(R.color.g512);
                    break;
                case "1024":
                    color = getResources().getColor(R.color.g1024);
                    break;
                case "2048":
                    color = getResources().getColor(R.color.g2048);
                    break;
                case "4096":
                    color = getResources().getColor(R.color.g4096);
                    break;
            }
        } else if (theme.equals("Red")) {
            switch (fieldValue) {
                case "":
                    color = getResources().getColor(R.color.white);
                    break;
                case "2":
                    color = getResources().getColor(R.color.r2);
                    break;
                case "4":
                    color = getResources().getColor(R.color.r4);
                    break;
                case "8":
                    color = getResources().getColor(R.color.r8);
                    break;
                case "16":
                    color = getResources().getColor(R.color.r16);
                    break;
                case "32":
                    color = getResources().getColor(R.color.r32);
                    break;
                case "64":
                    color = getResources().getColor(R.color.r64);
                    break;
                case "128":
                    color = getResources().getColor(R.color.r128);
                    break;
                case "256":
                    color = getResources().getColor(R.color.r256);
                    break;
                case "512":
                    color = getResources().getColor(R.color.r512);
                    break;
                case "1024":
                    color = getResources().getColor(R.color.r1024);
                    break;
                case "2048":
                    color = getResources().getColor(R.color.r2048);
                    break;
                case "4096":
                    color = getResources().getColor(R.color.r4096);
                    break;
            }
        }
        return color;
    }
}