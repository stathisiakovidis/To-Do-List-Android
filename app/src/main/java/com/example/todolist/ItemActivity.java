package com.example.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.database.Task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemActivity extends AppCompatActivity {

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
        toolbar.setNavigationOnClickListener((View v) -> {
            //MUST get as putExtra which intent was the previous one, so it can return to it and not always to main
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });

        //IDs
        title = findViewById(R.id.title);
        dateText = findViewById(R.id.dateText);
        body = findViewById(R.id.body);

        //Add listeners to date textView
        DateListener dateListener = new DateListener();
        dateText.setOnClickListener(dateListener);
        dateText.setOnLongClickListener(dateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        DatePicker picker;

        String currTitle = title.getText().toString();
        String currDate = dateText.getText().toString() ;

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate;
        try {
            myDate = df.parse(currDate);
            String myText = myDate.getDate() + "-" + (myDate.getMonth() + 1) + "-" + (1900 + myDate.getYear());
            Log.i(MainActivity.TAG, myText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task task = new Task();



    }

    class DateListener implements View.OnClickListener, View.OnLongClickListener {


        @Override
        public void onClick(View v) {

            Calendar calendar = Calendar.getInstance();
            int currYear = calendar.get(Calendar.YEAR);
            int currMonth = calendar.get(Calendar.MONTH);
            int currDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ItemActivity.this,
                    (datePicker, year, month, day) -> {
                        dateText.setText(day + "\\" + month + "\\" + year );

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
