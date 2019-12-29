package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.database.Task;

import java.util.ArrayList;

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

    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void changeTasks(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.body = itemView.findViewById(R.id.body);

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "This is a Toast", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
