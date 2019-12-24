package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseClient client;
    private CalendarView calendar;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener((View v) -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });

        //Add-task button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        //Client for database methods
        client = new DatabaseClient(getApplicationContext());

        //Rest of the IDs
        calendar = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);

        try {

            final CustomAdapter adapter = new CustomAdapter(getApplicationContext(), client.getFromThisDay(16, 11, 2019));
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                Log.i(MainActivity.TAG, "This is " + dayOfMonth + " of " + month);

                try {
                    tasks = client.getFromThisDay(dayOfMonth, month, year);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                adapter.changeTasks(tasks);
                adapter.notifyDataSetChanged();

            });

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
