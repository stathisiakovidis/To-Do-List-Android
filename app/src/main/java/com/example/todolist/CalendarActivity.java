package com.example.todolist;

import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private RecyclerView recyclerView;
    private DatabaseClient client;
    private CalendarView calendarView;
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int dayOfMonth, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        //Add-task button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new FabListener());

        //Client for database methods
        client = new DatabaseClient(getApplicationContext());

        //Rest of the IDs
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);

        try {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            adapter = new CustomAdapter(
                    getApplicationContext(),
                    client.getFromThisDay(
                            dayOfMonth,
                            month,
                            year));

            recyclerView.setAdapter(adapter);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            //Add listener to date change
            calendarView.setOnDateChangeListener(this::onSelectedDayChange);

            adapter.recyclerViewGesture(getApplicationContext(), recyclerView);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MainActivity.TAG, "OnStart is called");
        //Change tasks and show the new ones
        try {
            adapter.changeTasks(client.getFromThisDay(dayOfMonth, month, year));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Log.i(MainActivity.TAG, "This is " + dayOfMonth + " of " + month);
        
        //Initialize these variables so I can use them in OnStart method
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;

        //Change tasks and show the new ones
        try {
            adapter.changeTasks(client.getFromThisDay(dayOfMonth, month, year));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

        //Whenever I click a different date, recyclerView scrolls to the top
        //Otherwise it would stay wherever it was at the moment
        layoutManager.scrollToPositionWithOffset(0, 0);

    }

}
