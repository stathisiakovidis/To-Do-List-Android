package com.example.todolist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static com.example.todolist.Constants.ERROR_DIALOG_REQUEST;
import static com.example.todolist.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.todolist.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class ItemActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    //Map Parameters
    private boolean mLocationPermissionGranted = false;

    private Task task;
    private EditText title;
    private TextView dateText;
    private EditText body;
    private DatabaseClient client;
    private boolean done = false;
    private boolean isUpdate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(checkMapServices()){
            BlankFragment fragment = new BlankFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        }

        //Back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        //IDs
        title = findViewById(R.id.title);
        dateText = findViewById(R.id.dateText);
        body = findViewById(R.id.body);

        client = new DatabaseClient(getApplicationContext());

        task = new Task();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        if(id != -1){
            try {

                task = client.getSpecific(id);

                isUpdate = true;

                fillItems(task);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Add listeners to date textView
        DateListener dateListener = new DateListener();
        dateText.setOnClickListener(dateListener);
        dateText.setOnLongClickListener(dateListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
            }
            else{
                getLocationPermission();
            }
        }
    }

    private void fillItems(Task task) {

        if(task.getType().equals("DATE")) {

            Log.i(MainActivity.TAG, "Type: " + task.getType());
            Calendar calendar = task.getCalendar();
            LocalDate localDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int day = localDate.getDayOfMonth();
            int month = localDate.getMonthValue();
            int year = localDate.getYear();

            dateText.setText(day + "\\" + month + "\\" + year);
        }
        title.setText(task.getTitle());
        body.setText(task.getBody());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Do this only if the task is not saved properly
        if(!done) {

            task.setType(Task.Type.DRAFT.toString());
            task.setTitle(title.getText().toString());
            task.setBody(body.getText().toString());

            if(!isUpdate)
                client.insert(task);
            else
                client.update(task);
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
                String currDate = dateText.getText().toString();

                if(!currTitle.isEmpty()){
                    DatabaseClient client = new DatabaseClient(getApplicationContext());

                    task.setTitle(currTitle);
                    task.setBody(currBody);

                    if(!currDate.isEmpty()){
                        task.setType(Task.Type.DATE.toString());
                    }else {
                        task.setType(Task.Type.NODATE.toString());
                    }

                    if(!isUpdate)
                        client.insert(task);
                    else
                        client.update(task);
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

    //Google map Setup
    /*private void runMap(){
        View map = findViewById(R.id.map);
    }*/

    /*
    * In this part we are checking if all services are updated and compatible with
    * the device we are targeting. Also if for some case user refused to enable
    * GPS, we force him to do it by accessing to his settings section
    */
    //Check google services
    public boolean isServicesOK(){

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        if(available == ConnectionResult.SUCCESS){
            //Everything is OK user can make map request
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //An error occured but we can fix it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ItemActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You cant make map request.",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //Check if GPS is enabled
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }

    //If GPS is disabled suggest user to turn it on from Settings
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Check if services and map are updated and eligible to use
    private boolean checkMapServices(){
        if(isServicesOK() && isMapsEnabled()){
                return true;
            }
        return false;
    }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            mLocationPermissionGranted = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

}
