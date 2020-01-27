package com.example.todolist;

import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private DatabaseClient client;
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Calendar calendar;

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
        CalendarView calendarView = findViewById(R.id.calendarView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.taskbar));


        try {

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            adapter = new CustomAdapter(client.getFromThisDay(calendar.getTimeInMillis()));

            recyclerView.setAdapter(adapter);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            //Add listener to date change
            calendarView.setOnDateChangeListener(this);

            adapter.recyclerViewGesture(getApplicationContext(), recyclerView);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MainActivity.TAG, "OnResume is called");
        //Change tasks and show the new ones
        try {
            adapter.setTasks(client.getFromThisDay(calendar.getTimeInMillis()));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Log.i(MainActivity.TAG, "This is " + dayOfMonth + " of " + month);

        //Initialize these variables so I can use them in OnStart method
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);


        //Change tasks and show the new ones
        try {
            adapter.setTasks(client.getFromThisDay(calendar.getTimeInMillis()));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

        //Whenever I click a different date, recyclerView scrolls to the top
        //Otherwise it would stay wherever it was at the moment
        layoutManager.scrollToPositionWithOffset(0, 0);

    }

}
