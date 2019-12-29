package com.example.todolist;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.example.todolist.database.DatabaseClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    private RecyclerView mainRecyclerView;
    private CustomAdapter recyclerAdapter;
    private DatabaseClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new FabListener());

        //Setting Notes as main content
        mainRecyclerView = findViewById(R.id.main_recycler_view);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        client = new DatabaseClient(getApplicationContext());
        try {

            recyclerAdapter = new CustomAdapter(getApplicationContext(),client.getAll());
            mainRecyclerView.setAdapter(recyclerAdapter);
            RecyclerViewGesture();
        }catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    //Swipe Gesture for RecyclerView
    public void RecyclerViewGesture(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //Swipe left or right
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        recyclerAdapter.getTasks().remove(position);
                        recyclerAdapter.notifyItemRemoved(position);
                        break;


                    case ItemTouchHelper.RIGHT:

                        break;
                }
            }
            //Deletion on swipe icon
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mainRecyclerView);
    }
}
