package com.example.todolist;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.todolist.Constants.MULTIPLE_PERMISSION_CODE;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";

    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private DatabaseClient client;
    private Spinner spinner;

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
        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        client = new DatabaseClient(getApplicationContext());
        adapter = new CustomAdapter(getApplicationContext(),null);
        recyclerView.setAdapter(adapter);

        if(checkAndRequestPermissions()) {

            try {
                adapter = new CustomAdapter(getApplicationContext(), client.getAllDone());
                recyclerView.setAdapter(adapter);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Setting up Spinner to categorize notes by date, by most recently or by drafts
        spinner = findViewById(R.id.spinner);
        spinnerChoice(spinner);

    }


    @Override
    protected void onResume() {
        super.onResume();

        //Always resume from most recently
        spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition("Most Recently"));

        //Change tasks and show the new ones
        try {
            adapter.setTasks(client.getAllDone());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
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
//        int id = item.getItemId();
//
//        switch(id){
//            case R.id.action_settings:
//                Log.i(TAG, "Settings option is clicked");
//
//                break;
//            case R.id.action_calendar:
//                //change activity
//                Log.i(TAG, "Calendar option is clicked");
//                Intent myIntent = new Intent(this, CalendarActivity.class);
//                startActivity(myIntent);
//                break;
//            case R.id.action_indefinite:
//                Log.i(TAG, "Indefinite option is clicked");
//                break;
//            default:
//
//        }

        Intent myIntent = new Intent(this, CalendarActivity.class);
        startActivity(myIntent);


        return super.onOptionsItemSelected(item);
    }

    //Spinner Settings to show selected notes by category
    public void spinnerChoice (Spinner spinner){

        List<String> choices = new ArrayList<>();
        choices.add("Most Recently");
        choices.add("By Date");
        choices.add("Drafts");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,choices);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, adapterView.getItemAtPosition(i).toString());
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                try {
                    switch (selectedItem) {
                        case "Most Recently":
                            adapter = new CustomAdapter(getApplicationContext(), client.getAllDone());
                            //adapter.setTasks(client.getAllDone());
                            break;
                        case "By Date":
                            adapter = new CustomAdapter(getApplicationContext(), client.getDateNotes());

                            //adapter.setTasks(client.getDateNotes());
                            break;
                        case "Drafts":
                            adapter = new CustomAdapter(getApplicationContext(), client.getDraft());

                            // adapter.setTasks(client.getDraft());
                            break;
                    }

                    recyclerView.setAdapter(adapter);
                    adapter.recyclerViewGesture(getApplicationContext(), recyclerView);

                }catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
    }


    //Permissions Control and Request
    private boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    MULTIPLE_PERMISSION_CODE);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSION_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Storage & Location services permission granted");
                    // process the normal flow
                    //else any one or both the permissions are not granted
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked)
                    //so ask again explaining the usage of permission
                    //shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showDialogOK(
                                (dialog, which) -> {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            // proceed with logic by disabling the related features or quit the app.
                                            break;
                                    }
                                });
                    }
                    // Permission is denied (and never ask again is checked)
                    // shouldShowRequestPermissionRationale will return false
                    else {
                        Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                .show();
                        //proceed with logic by disabling the related features or quit the app.
                    }
                }
            }
        }
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Storage and Location Services Permission required for this app")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

}
