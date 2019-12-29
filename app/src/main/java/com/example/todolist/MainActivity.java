package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.todolist.database.AppDatabase;
import com.example.todolist.database.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view ->
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        fab.setOnClickListener(new FabListener());

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todo-db").build();

//        Save newTask = new Save();
//        newTask.execute();

    }

    /**
     * Manually will add some data.
     * This is a testing phase, it's not intended to be completely functional
     */
//    public class Save extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Task myTask = new Task();
//            myTask.setTitle("Title again");
//            myTask.setBody("Story of my life");
//            myTask.setDay((byte) 16);
//            myTask.setYear(2019);
//            myTask.setMonth((byte) 11);
//            db.userDao().insert(myTask);
//
//            Task secondTask = new Task(
//                    "Another title",
//                    "This is a small body",
//                    13,
//                    11,
//                    2019);
//            db.userDao().insert(secondTask);
//
//            Task thirdTask = new Task(
//                    "Do some shit",
//                    "I really have to do some shit, dude",
//                    24,
//                    11,
//                    2019);
//            db.userDao().insert(thirdTask);
//
//            ArrayList<Task> tasks = (ArrayList<Task>) db.userDao().getAll();
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Log.i(TAG, "Insert is done");
//
//        }
//    }

    //Creates option in menu (in example settings)
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

        switch(id){
            case R.id.action_settings:
                Log.i(TAG, "Settings option is clicked");

                break;
            case R.id.action_calendar:
                //change activity
                Log.i(TAG, "Calendar option is clicked");
                Intent myIntent = new Intent(this, CalendarActivity.class);
                startActivity(myIntent);
                break;
            case R.id.action_indefinite:
                Log.i(TAG, "Indefinite option is clicked");
                break;
            default:

        }

        return super.onOptionsItemSelected(item);
    }
}
