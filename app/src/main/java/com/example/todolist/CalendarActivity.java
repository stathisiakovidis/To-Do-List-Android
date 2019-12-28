package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity /*implements CalendarView.OnDateChangeListener*/ {

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

        Log.i(MainActivity.TAG, String.valueOf(calendar.getDateTextAppearance()));
        try {

            final CustomAdapter adapter = new CustomAdapter(
                    getApplicationContext(),
                    client.getFromThisDay(
                            getCurrDayOfMonth(),
                            getCurrMonth(),
                            getCurrYear()));

            recyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                Log.i(MainActivity.TAG, "This is " + dayOfMonth + " of " + month);

                try {
                    tasks = client.getFromThisDay(dayOfMonth, month, year);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Change tasks and show the new ones
                adapter.changeTasks(tasks);
                adapter.notifyDataSetChanged();

                //Whenever I click a different date, recyclerView scrolls to the top
                //Otherwise it would stay wherever it was at the moment
                layoutManager.scrollToPositionWithOffset(0, 0);


            });

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private int getCurrDayOfMonth(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.getDayOfMonth();

    }

    private int getCurrMonth(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.getMonthValue() - 1;
    }

    private int getCurrYear(){
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear();
    }

//    @Override
//    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//        Log.i(MainActivity.TAG, "This is " + dayOfMonth + " of " + month);
//
//        try {
//            tasks = client.getFromThisDay(dayOfMonth, month, year);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //Change tasks and show the new ones
//        adapter.changeTasks(tasks);
//        adapter.notifyDataSetChanged();
//
//        //Whenever I click a different date, recyclerView scrolls to the top
//        //Otherwise it would stay wherever it was at the moment
//        layoutManager.scrollToPositionWithOffset(0, 0);
//
//    }
}
