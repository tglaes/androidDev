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

    private final static int REQUEST_CODE_DETAILS = 1;
    private final static int REQUEST_CODE_NEW_TO_DO = 2;
    public final static String TITLE_KEY = "TITLEKEY";
    public final static String PRIORITY_KEY = "PRIORITYKEY";
    public final static String DESCRIPTION_KEY = "DESCRIPTIONKEY";

    public static int listViewItemIndex = 0;
    /*private static final String[] toDoList = new String[]{
            "Android Uebung machen", "Rechnerarchitektur"
    };*/
    private ArrayAdapter<ToDoEntry> adapter = null;
    private ListView toDoListView;
    private MenuItem newItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*/adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1); // des Adapters
        for (int i = 0; i < toDoList.length; i++) {
            adapter.add(toDoList[i]);
        }*/
        final MainActivity ma = this;

        adapter = new ArrayAdapter<ToDoEntry>(this, android.R.layout.simple_list_item_1);
        adapter.add(new ToDoEntry("Tristan ist der Beste", Priority.low, "Jo isser"));
        adapter.add(new ToDoEntry("Jurie ist der Beste", Priority.high, "Ist er nicht"));
        adapter.add(new ToDoEntry("Meris ist klasse!", Priority.middle, "Test"));

        adapter.sort(new ToDoEntry("Tristan ist der Beste", Priority.high, "Jo isser"));

        toDoListView = findViewById(R.id.toDoList);
        toDoListView.setAdapter(adapter);


        toDoListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView av, View v, int i, long l){
                Intent intent = new Intent(ma, DetailsActivity.class);
                intent.putExtra(TITLE_KEY, adapter.getItem(i).getTitle());
                intent.putExtra(PRIORITY_KEY, adapter.getItem(i).getPriority());
                intent.putExtra(DESCRIPTION_KEY, adapter.getItem(i).getDescription());
                listViewItemIndex = i;
                /* Starten der Activity */
                startActivityForResult(intent, REQUEST_CODE_DETAILS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DETAILS) {

            String newTitle = data.getStringExtra(DetailsActivity.TITLE_RESULT_KEY);
            Priority newPrio = (Priority) data.getSerializableExtra(DetailsActivity.PRIORITY_RESULT_KEY);
            String newDescription = data.getStringExtra(DetailsActivity.DESCRIPTION_RESULT_KEY);

            if(newTitle.equals("")){
                adapter.remove(adapter.getItem(listViewItemIndex));
            } else {
                ToDoEntry tDE = adapter.getItem(listViewItemIndex);
                tDE.setTitle(newTitle);
                tDE.setDescription(newDescription);
                tDE.setPriority(newPrio);
                adapter.sort(tDE);
            }
        } else if(requestCode == REQUEST_CODE_NEW_TO_DO){

            if(resultCode == RESULT_OK) {
                String newTitle = data.getStringExtra(NewActivity.NEW_TITLE_RESULT_KEY);
                Priority newPrio = (Priority) data.getSerializableExtra(NewActivity.NEW_PRIORITY_RESULT_KEY);
                String newDescription = data.getStringExtra(NewActivity.NEW_DESCRIPTION_RESULT_KEY);

                ToDoEntry tDE = new ToDoEntry(newTitle, newPrio, newDescription);
                adapter.add(tDE);
                adapter.sort(tDE);
            } else if(resultCode == RESULT_CANCELED){
                return;
            }

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

    public void newDoTo(MenuItem item) {
        Intent intent = new Intent(this, NewActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_TO_DO);
    }
}
