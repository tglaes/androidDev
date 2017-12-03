package com.example.tristanglaes.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewActivity extends AppCompatActivity {

    private Button newBtn, cancelBtn;
    private EditText newTitle, newDescription;
    private Priority prio;
    private Spinner spinner;
    public final static String NEW_TITLE_RESULT_KEY = "NEWTITLERESULTKEY";
    public final static String NEW_PRIORITY_RESULT_KEY = "NEWPRIORESULTKEY";
    public final static String NEW_DESCRIPTION_RESULT_KEY = "NEWDESCRIPTIONRESULTKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newTitle = findViewById(R.id.newToDoTilteEt);
        newDescription = findViewById(R.id.newToDoDescriptionEt);
        spinner = findViewById(R.id.prioSpinner);


        newBtn = findViewById(R.id.newToDoBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent result = new Intent();
            result.putExtra(NEW_TITLE_RESULT_KEY, newTitle.getText().toString());
            result.putExtra(NEW_PRIORITY_RESULT_KEY, Priority.makeEnumFromString(spinner.getSelectedItem().toString()));
            result.putExtra(NEW_DESCRIPTION_RESULT_KEY, newDescription.getText().toString());
            setResult(RESULT_OK, result);
            finish();
            }
        });

        cancelBtn = findViewById(R.id.cancelToDoBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
