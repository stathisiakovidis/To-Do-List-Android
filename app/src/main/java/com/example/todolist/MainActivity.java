package com.example.todolist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    private RecyclerView mainRecyclerView;
    private CustomAdapter recyclerAdapter;
    private DatabaseClient client;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new FabListener());


        //Setting Notes as main content
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        client = new DatabaseClient(getApplicationContext());
        recyclerAdapter = new CustomAdapter(getApplicationContext(),null);
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
             recyclerAdapter = new CustomAdapter(getApplicationContext(), client.getAllDone());
         } catch (ExecutionException e) {
            e.printStackTrace();
         } catch (InterruptedException e) {
            e.printStackTrace();
            }
        }else {
            requestStoragePermission();
        }
        //Setting up Spinner to categorize notes by date, by most recently or by drafts
        Spinner spinner = findViewById(R.id.spinner);
        spinnerChoice(spinner);

    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "OnStart is called");
        //Change tasks and show the new ones
        try {
            recyclerAdapter.changeTasks(client.getAllDone());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerAdapter.notifyDataSetChanged();
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                try {
                    if (selectedItem.equals("Most Recently")){
                        recyclerAdapter.changeTasks(reverseList(client.getAllDone()));
                        mainRecyclerView.setAdapter(recyclerAdapter);
                        recyclerAdapter.recyclerViewGesture(getApplicationContext(), mainRecyclerView);
                    }else if(selectedItem.equals("By Date")){
                        recyclerAdapter.changeTasks(client.getDateNotes());
                        mainRecyclerView.setAdapter(recyclerAdapter);
                        recyclerAdapter.recyclerViewGesture(getApplicationContext(), mainRecyclerView);
                    }else if (selectedItem.equals("Drafts")){
                        recyclerAdapter.changeTasks(client.getDraft());
                        mainRecyclerView.setAdapter(recyclerAdapter);
                        recyclerAdapter.recyclerViewGesture(getApplicationContext(), mainRecyclerView);
                    }
                }catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Reverse Task List to show the most recently
    public ArrayList<Task> reverseList(ArrayList<Task> list) {
        for(int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        return list;
    }

}
