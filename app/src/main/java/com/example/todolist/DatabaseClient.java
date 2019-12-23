package com.example.todolist;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DatabaseClient {

    public static final String name = "todo-db";

    private AppDatabase db;
    //private ArrayList<Task> tasks;

    public DatabaseClient(Context context){

        db = Room.databaseBuilder(context, AppDatabase.class, name).build();

    }

    public ArrayList<Task> getFromThisDay(int dayOfTheMonth, int month, int year) throws ExecutionException, InterruptedException {

        class Async extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {

                ArrayList<Task> tasks = (ArrayList<Task>) db.userDao().getFromThisDay(dayOfTheMonth, month, year);
                return tasks;
            }
        }

        Async async = new Async();
        return async.execute().get();


    }




}
