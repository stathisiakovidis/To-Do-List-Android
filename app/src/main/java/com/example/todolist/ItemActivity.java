package com.example.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;
import com.google.android.gms.maps.GoogleMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        toolbar.setNavigationOnClickListener((View v) -> {
            //MUST get as putExtra which intent was the previous one, so it can return to it and not always to main
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });


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

    //Creates option in menu (in example settings)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        return true;
    }

    //Change icon
    //If location is added, then change to "Edit Icon" instead of "Add icon"
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (task.isLocation()) {
            menu.findItem(R.id.map_add).setIcon(R.drawable.ic_edit_location);
        } else {
            menu.findItem(R.id.map_add).setIcon(R.drawable.ic_add_location);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.map_add:
                Log.i(MainActivity.TAG, "Map add option is clicked");
                break;
            default:

        }

        return super.onOptionsItemSelected(item);
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
                dateText.setText(day + "\\" + (month+1) + "\\" + year);

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
