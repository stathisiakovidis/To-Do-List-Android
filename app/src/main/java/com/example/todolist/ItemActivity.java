package com.example.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class ItemActivity extends AppCompatActivity {

    private Task task;
    private EditText title;
    private TextView dateText;
    private EditText body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        //IDs
        title = findViewById(R.id.title);
        dateText = findViewById(R.id.dateText);
        body = findViewById(R.id.body);

        task = new Task();

        //Add listeners to date textView
        DateListener dateListener = new DateListener();
        dateText.setOnClickListener(dateListener);
        dateText.setOnLongClickListener(dateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        String currTitle = title.getText().toString();
        String currBody = body.getText().toString();

        if(!currTitle.isEmpty()){
            DatabaseClient client = new DatabaseClient(getApplicationContext());


            task.setTitle(currTitle);
            task.setBody(currBody);

            client.insert(task);

            Log.i(MainActivity.TAG, "Title: " + currTitle);
            Log.i(MainActivity.TAG, "Body: " + currBody);
            Log.i(MainActivity.TAG, "Date: " + task.getDay() + " " + task.getMonth() + " " + task.getYear());


            Log.i(MainActivity.TAG, "Insert is done");
        }

    }

    private class DateListener implements View.OnClickListener, View.OnLongClickListener {


        @Override
        public void onClick(View v) {

            Calendar calendar = Calendar.getInstance();
            int currYear = calendar.get(Calendar.YEAR);
            int currMonth = calendar.get(Calendar.MONTH);
            int currDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ItemActivity.this,
                    (datePicker, year, month, day) -> {

                //Save date to the current task object
                task.setDay(day);
                task.setMonth(month);
                task.setYear(year);

                //Display the date
                dateText.setText(day + "\\" + month + "\\" + year);

                    }, currYear, currMonth, currDay);
            datePickerDialog.show();

        }

        @Override
        public boolean onLongClick(View v) {

            dateText.setText("date");
            Log.i(MainActivity.TAG, "Long click");
            return true;
        }
    }
}
