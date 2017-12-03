package com.example.tristanglaes.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    public static final String intentResultKey = "ResultKey";
    private Button applyBtn, cancelBtn;
    private TextView toDoTv, priorityTv;
    private String oldToDoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toDoTv = findViewById(R.id.tvToDoString);


        Intent intent = getIntent();
        oldToDoString = intent.getStringExtra(MainActivity.intentKey);
        toDoTv.setText(oldToDoString);

        applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(intentResultKey, toDoTv.getText());
                /* Rückgabewert und -daten setzen */
                setResult(RESULT_OK,result);
                finish();
            }
        });

        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(intentResultKey, oldToDoString);
                /* Rückgabewert und -daten setzen */
                setResult(RESULT_OK,result);
                finish();
            }
        });



    }
}
