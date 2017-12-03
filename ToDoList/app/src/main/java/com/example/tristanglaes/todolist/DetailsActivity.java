package com.example.tristanglaes.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    public final static String TITLE_RESULT_KEY = "TITLERESULTKEY";
    public final static String PRIORITY_RESULT_KEY = "PRIORITYRESULTKEY";
    public final static String DESCRIPTION_RESULT_KEY = "DESCRIPTIONRESULTKEY";
    private Button applyBtn, cancelBtn, deleteBtn;
    private EditText toDoEt, descriptionEt;
    private Spinner prioSp;
    private String title, description, oldTitle, oldDescription;
    private Priority prio, oldPrio;
    private int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // View Elemente fuer Title, Priority und Beschreibung
        toDoEt = findViewById(R.id.tvToDoString);
        descriptionEt = findViewById(R.id.etDescription);
        prioSp = findViewById(R.id.toDoSpinner);

        Intent intent = getIntent();
        // Auslesen der uebergebenen Daten
        title = intent.getStringExtra(MainActivity.TITLE_KEY);
        description = intent.getStringExtra(MainActivity.DESCRIPTION_KEY);
        prio = (Priority) intent.getSerializableExtra(MainActivity.PRIORITY_KEY);

        oldTitle = title;
        oldDescription = description;
        oldPrio = prio;

        toDoEt.setText(title);
        descriptionEt.setText(description);

        switch (prio){
            case low: spinnerPosition = 2;
            break;
            case middle: spinnerPosition = 1;
            break;
            case high: spinnerPosition = 0;
            break;
        }

        prioSp.setSelection(spinnerPosition);

        applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(TITLE_RESULT_KEY, toDoEt.getText().toString());
                result.putExtra(PRIORITY_RESULT_KEY, Priority.makeEnumFromString(prioSp.getSelectedItem().toString()));
                result.putExtra(DESCRIPTION_RESULT_KEY, descriptionEt.getText().toString());
                setResult(RESULT_OK, result);
                finish();
            }
        });

        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(TITLE_RESULT_KEY, oldTitle);
                result.putExtra(PRIORITY_RESULT_KEY, oldPrio);
                result.putExtra(DESCRIPTION_RESULT_KEY, oldDescription);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(TITLE_RESULT_KEY, "");
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
