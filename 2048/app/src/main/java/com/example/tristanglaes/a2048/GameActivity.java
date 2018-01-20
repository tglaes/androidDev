package com.example.tristanglaes.a2048;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Interpolator;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.transform.Result;

public class GameActivity extends AppCompatActivity {

    private GridView gv;
    private ArrayAdapter<String> adapter;
    private int board[][];
    private int numberMoves;
    private int width;
    private int height;
    private int points;
    private String time;
    private MoveDirection move;
    private TextView pointsTv, movesTv,timeTv;
    private Button newGameBtn;
    private static String GAME_BOARD_KEY = "com.example.tristanglaes.a2048.GAMEBOARD";
    private static String GAME_TIME_KEY = "com.example.tristanglaes.a2048.TIME";
    private static String GAME_MOVES_KEY = "com.example.tristanglaes.a2048.MOVES";
    private static String GAME_POINTS_KEY = "com.example.tristanglaes.a2048.POINTS";
    private static String GAME_STORED_KEY = "com.example.tristanglaes.a2048.STORED";
    private Random rand;
    private GameTimer gt;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        board = new int[4][4];
        width = 4;
        height = 4;
        time = "0:00";
        gt = new GameTimer();
        pointsTv = findViewById(R.id.pointsTextView);
        movesTv = findViewById(R.id.movesTextView);
        timeTv = findViewById(R.id.timeTextView);
        rand = new Random();
        gv = findViewById(R.id.gameBoard);
        gv.setOnTouchListener(new View.OnTouchListener() {

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
                if(isMovePossible(MoveDirection.EAST)){
                    performMove(MoveDirection.EAST);
                    checkGame();
                }
            }

            public void onSwipeLeft() {
                if(isMovePossible(MoveDirection.WEST)){
                    performMove(MoveDirection.WEST);
                    checkGame();
                }
            }

            public void onSwipeTop() {
                if(isMovePossible(MoveDirection.NORTH)){
                    performMove(MoveDirection.NORTH);
                    checkGame();
                }
            }

            public void onSwipeBottom() {
                if(isMovePossible(MoveDirection.SOUTH)){
                    performMove(MoveDirection.SOUTH);
                    checkGame();
                }
            }


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        newGameBtn = findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });
        loadGame();
    }

    public void checkGame(){
        if(!isMovePossible()){
            Toast toast = Toast.makeText(getApplicationContext(), "YOU LOST!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            addPiece();
            updateBoard(board);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        gt.cancel(true);
        safeGame();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gt.cancel(true);
        safeGame();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadGame();
        updateBoard(board);
    }

    /**
     *
     * @param board
     */
    private void updateBoard(int board[][]){
        adapter = new ArrayAdapter<>(GameActivity.this, R.layout.support_simple_spinner_dropdown_item);
        List<String> boardList = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boardList.add(String.valueOf(board[j][i]));
            }
        }
        adapter.addAll(boardList);
        gv.setAdapter(adapter);
        pointsTv.setText(String.valueOf(points));
        movesTv.setText(String.valueOf(numberMoves));
    }

    /**
     *
     * @return 2 (with 90% chance) or 4 (with 10% chance)
     */
    private int getNewPiece() {
        if (rand.nextInt(100) <= 89) {
            return 2;
        } else {
            return 4;
        }
    }

    /**
     * Initialize the 2 pieces at the start
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
     * Setzt einen neuen Stein auf das Spielfeld
     */
    public void addPiece() {
        int x, y;

        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);

        } while (getPieceAt(x, y) > 0);

        setPieceAt(x, y, getNewPiece());

    }

    public int getNumPieces() {
        int numPieces = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (getPieceAt(i, j) > 0) {
                    numPieces++;
                }
            }
        }
        return numPieces;
    }

    public int getPieceAt(int x, int y) {
        return board[x][y];

    }

    public boolean isMovePossible() {
        if(getNumPieces() != (width * height)){
            return true;
        }

        return isMovePossible(MoveDirection.SOUTH) || isMovePossible(MoveDirection.WEST);
        // Move is possible when
        // they are 2 same
        // pieces in one row or column
        // Or obviously the board is
        // not full
    }

    public boolean isMovePossible(MoveDirection direction) {

        if(getNumPieces() == 0){
            return false;
        }
        if(direction == MoveDirection.SOUTH){
            for(int i = 0; i < width; i++){
                for(int j = 0; j < (height - 1); j++){
                    if(getPieceAt(i,j) > 0){
                        if((getPieceAt(i,j) == getPieceAt(i,j + 1)) || (getPieceAt(i,j + 1) == 0)){
                            return true;
                        }
                    }
                }
            }
        }else if(direction == MoveDirection.NORTH){
            for(int i = 0; i < width; i++){
                for(int j = (height - 1); j > 0; j--){
                    if(getPieceAt(i,j) > 0){
                        if((getPieceAt(i,j) == getPieceAt(i,j - 1)) || (getPieceAt(i,j - 1) == 0)){
                            return true;
                        }
                    }
                }
            }
        } else if(direction == MoveDirection.EAST){
            for(int i = 0; i < height; i++){
                for(int j = 0; j < (width -1); j++){
                    if(getPieceAt(j,i) > 0){
                        if((getPieceAt(j,i) == getPieceAt(j + 1, i)) || (getPieceAt(j + 1,i) == 0)){
                            return true;
                        }
                    }
                }
            }
        } else {
            for(int i = 0; i < height; i++){
                for(int j = (width - 1); j > 0; j--){
                    if(getPieceAt(j,i) > 0){
                        if((getPieceAt(j,i) == getPieceAt(j - 1, i)) || (getPieceAt(j - 1,i) == 0)){
                            return true;
                        }
                    }
                }
            }
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
                if(getPieceAt(x,y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x + 1, y)) {
                        setPieceAt(x + 1, y, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x,y));
                        result = true;
                    }
            }

        } else if (direction == MoveDirection.EAST) {
            for (int x = (width - 1); x > 0; x--) {
                if(getPieceAt(x,y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x - 1, y)) {
                        setPieceAt(x - 1, y, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x,y));
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
                if(getPieceAt(x,y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x, (y - 1))) {
                        setPieceAt(x, y - 1, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x,y));
                        result = true;
                    }
            }

        } else if (direction == MoveDirection.NORTH) {
            for (int y = 0; y < (height - 1); y++) {
                if(getPieceAt(x,y) > 0)
                    if (getPieceAt(x, y) == getPieceAt(x, (y + 1))) {
                        setPieceAt(x, y + 1, 0);
                        setPieceAt(x, y, (getPieceAt(x, y) * 2));
                        points = points + (getPieceAt(x,y));
                        result = true;
                    }
            }
        }
        return result;
    }

    /**
     *
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

    public boolean performMove(MoveDirection direction) {

        if (movePieces(direction)) {
            numberMoves++;
            if (mergeAll(direction)) {
                movePieces(direction);
            }
            return true;
        } else {
            if(mergeAll(direction)){
                numberMoves++;
                movePieces(direction);
                return true;
            }
        }
        return false;
    }

    public void setPieceAt(int x, int y, int piece) {
        board[x][y] = piece;
    }

    /**
     * Speichert den Spielstand in den SharedPreferences
     */
    private void safeGame(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < height; i++){
            for (int j=0; j<width; j++){
                editor.putInt(GAME_BOARD_KEY + i + j, getPieceAt(i,j));
            }
        }
        editor.putInt(GAME_MOVES_KEY, numberMoves);
        editor.putInt(GAME_POINTS_KEY, points);
        editor.putString(GAME_TIME_KEY, time.toString());
        editor.putBoolean(GAME_STORED_KEY, true);
        editor.apply();
    }

    private void startNewGame() {

        gt.cancel(true);
        time = "0:00";
        timeTv.setText(time);
        gt = new GameTimer();
        gt.execute();
        points = 0;
        numberMoves = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = 0;
            }
        }
        initBoardPieces();
        updateBoard(board);
        //timeTv.setText(time);
    }

    private void loadGame() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        for (int i = 0; i < height; i++){
            for (int j=0; j<width; j++){
                board[i][j] = preferences.getInt(GAME_BOARD_KEY + i + j,0);
            }
        }
        numberMoves = preferences.getInt(GAME_MOVES_KEY,0);
        points = preferences.getInt(GAME_POINTS_KEY,0);
        time = preferences.getString(GAME_TIME_KEY,"0");
        timeTv.setText(time);
        gt = new GameTimer();
        gt.execute();

    }

    protected void setGameTime(String time){
        timeTv.setText(time);
    }

    private class GameTimer extends AsyncTask<Void, Long, Void> {

        long minutes;
        long seconds;
        long startTime ;
        long estimatedTime;
        long lastTime;
        boolean run = true;

        @Override
        protected Void doInBackground(Void... params) {

            startTime = System.currentTimeMillis();
            lastTime = 0;

            while(run) {

                estimatedTime = System.currentTimeMillis() - startTime;
                if (estimatedTime - lastTime > 1000) {
                    lastTime = estimatedTime;

                    seconds = estimatedTime / 1000;
                    minutes = seconds / 60;
                    seconds = seconds % 60;

                    publishProgress(minutes, seconds);
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            run = false;
        }

        @Override
        protected void onProgressUpdate(Long ...time) {
            if(time[1] < 10){
                setGameTime(time[0] + ":0" + time[1]);
            } else {
                setGameTime(time[0] + ":" + time[1]);
            }
        }
    }
}