package com.example.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;

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
    private DatabaseClient client;
    private boolean done = false;

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

        client = new DatabaseClient(getApplicationContext());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Do this only if the task is not saved properly
        if(!done) {

            task.setType(Task.Type.DRAFT.toString());
            task.setTitle(title.getText().toString());
            task.setBody(body.getText().toString());

            client.insert(task);
        }
    }

    //Creates option in menu (in example settings)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_done:
                Log.i(MainActivity.TAG, "Done option is clicked");

                String currTitle = title.getText().toString();
                String currBody = body.getText().toString();

                if(!currTitle.isEmpty()){
                    DatabaseClient client = new DatabaseClient(getApplicationContext());

                    task.setTitle(currTitle);
                    task.setBody(currBody);

                    client.insert(task);

                    //The task is saved properly
                    done = true;

                    Log.i(MainActivity.TAG, "Insert is done");
                }

                finish();
                break;
            case R.id.action_calendar:
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

                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                task.setCalendar(calendar);

                task.setType(Task.Type.DATE.toString());

                //Display the date
                dateText.setText(day + "\\" + (month+1) + "\\" + year);

                    }, currYear, currMonth, currDay);
            datePickerDialog.show();

        }

        @Override
        public boolean onLongClick(View v) {

            dateText.setText("date");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);

            task.setType(Task.Type.NODATE.toString());

            Log.i(MainActivity.TAG, "Long click");
            return true;
        }
    }
}
