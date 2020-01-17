package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;

import java.util.ArrayList;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>  {

    private ArrayList<Task> tasks;
    private Context context;

    public CustomAdapter(Context context, ArrayList<Task> tasks){
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String currTitle = tasks.get(position).getTitle();
        String currBody = tasks.get(position).getBody();

        holder.title.setText(currTitle);
        holder.body.setText(currBody);
        holder.id = tasks.get(position).getId();
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(ArrayList<Task> tasks){
        this.tasks = reverseList(tasks);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView body;
        private int id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.body = itemView.findViewById(R.id.body);

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "This is a Toast", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), ItemActivity.class);

            //Log.i(MainActivity.TAG, "ID is: " );

            intent.putExtra("id", id);
            v.getContext().startActivity(intent);
        }
    }

    //Swipe Gesture for RecyclerView
    public void recyclerViewGesture(Context context, RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //Swipe left or right
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                DatabaseClient client = new DatabaseClient(context);

                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:

                        //Delete from database
                        Task currTask = tasks.get(position);
                        client.delete(currTask);

                        //Remove from view
                        tasks.remove(position);
                        notifyItemRemoved(position);

                        break;
                    case ItemTouchHelper.RIGHT:

                        break;
                }
            }
            //Deletion on swipe icon
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    //Reverse Task List to show the most recently
    public ArrayList<Task> reverseList(ArrayList<Task> list) {
        for(int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        return list;
    }

}
