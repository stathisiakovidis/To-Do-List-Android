package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    private RecyclerView mainRecyclerView;
    private CustomAdapter recyclerAdapter;
    private DatabaseClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new FabListener());

        //Setting up Spinner to categorize notes by date, by most recently or by drafts
        Spinner spinner = findViewById(R.id.spinner);
        spinnerChoice(spinner);

        //Setting Notes as main content
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        client = new DatabaseClient(getApplicationContext());
        try {

            recyclerAdapter = new CustomAdapter(getApplicationContext(),client.getAll());
            mainRecyclerView.setAdapter(recyclerAdapter);
            recyclerAdapter.recyclerViewGesture(getApplicationContext(), mainRecyclerView);
        }catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

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


    public void spinnerChoice (Spinner spinner){

        List<String> choices = new ArrayList<>();
        choices.add("Most Recently");
        choices.add("By Date");
        choices.add("Drafts");

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,R.layout.spinner_item,choices);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }

}
