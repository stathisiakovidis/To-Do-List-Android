package com.example.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Task> tasks;
    private Context context;

    public CustomAdapter(Context context, ArrayList<Task> tasks){
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(MainActivity.TAG, "THIS IS CALLED");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(MainActivity.TAG, "Binding is called");

        String currTitle = tasks.get(position).getTitle();
        String currBody = tasks.get(position).getBody();

        holder.title.setText(currTitle);
        holder.body.setText(currBody);

    }

    @Override
    public int getItemCount() {
        Log.i(MainActivity.TAG, "getItemCount is called");
        return tasks.size();
    }

    public void changeTasks(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.body = itemView.findViewById(R.id.body);

        }
    }
}
