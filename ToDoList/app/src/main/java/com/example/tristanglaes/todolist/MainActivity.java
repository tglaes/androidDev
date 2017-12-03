package com.example.tristanglaes.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 1;
    public final static String intentKey = "ToDoString";
    public static int listViewItemIndex = 0;
    private static final String[] toDoList = new String[]{
            "ANdroid uebung"
    };
    private ArrayAdapter<String> adapter = null;
    private ListView toDoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1); // des Adapters
        for (int i = 0; i < toDoList.length; i++) {
            adapter.add(toDoList[i]);
        }

        toDoListView = findViewById(R.id.toDoList);
        toDoListView.setAdapter(adapter);
        final MainActivity ma = this;
        toDoListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView av, View v, int i, long l){
                Intent intent = new Intent(ma, DetailsActivity.class);
                intent.putExtra(intentKey, adapter.getItem(i));
                listViewItemIndex = i;
                /* Starten der Activity */
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            String result = data.getStringExtra(DetailsActivity.intentResultKey);
            adapter.remove(adapter.getItem(listViewItemIndex));
            adapter.insert(result, listViewItemIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.newToDo){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
