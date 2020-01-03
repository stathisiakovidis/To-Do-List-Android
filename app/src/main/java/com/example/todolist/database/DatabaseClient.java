package com.example.todolist.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * This class includes every AsyncTask that has to do with the database
 */
public class DatabaseClient {

    public static final String name = "todo-db";

    private AppDatabase db;

    public DatabaseClient(Context context){
        db = Room.databaseBuilder(context, AppDatabase.class, name).build();
    }

    //Get every task user has to do in this particular day
    public ArrayList<Task> getFromThisDay(long timeInMillis) throws ExecutionException, InterruptedException {

        class Async extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {

                ArrayList<Task> tasks = (ArrayList<Task>) db.userDao().getFromThisDay(timeInMillis);
                return tasks;
            }
        }

        Async async = new Async();
        return async.execute().get();

    }

    //Get all elements
    public ArrayList<Task> getAllDone() throws ExecutionException, InterruptedException {

        class Async extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {

                ArrayList<Task> tasks = (ArrayList<Task>) db.userDao().getAllDone();
                return tasks;
            }
        }

        Async async = new Async();
        return async.execute().get();

    }

    //Get Notes with Date
    public ArrayList<Task> getDateNotes() throws ExecutionException, InterruptedException {

        class Async extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {

                ArrayList<Task> tasks = (ArrayList<Task>) db.userDao().getDateNotes();
                return tasks;
            }
        }

        Async async = new Async();
        return async.execute().get();

    }

    //Get Notes with Date
    public ArrayList<Task> getDraft() throws ExecutionException, InterruptedException {

        class Async extends AsyncTask<Void, Void, ArrayList<Task>> {

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {

                ArrayList<Task> tasks = (ArrayList<Task>) db.userDao().getDraft();
                return tasks;
            }
        }

        Async async = new Async();
        return async.execute().get();

    }

    public Task getSpecific(int id) throws ExecutionException, InterruptedException {

        class Async extends AsyncTask<Void, Void, Task> {

            @Override
            protected Task doInBackground(Void... voids) {

                Task task = db.userDao().getSpecific(id);
                return task;
            }
        }

        Async async = new Async();
        return async.execute().get();

    }

    public void insert(Task task){

        class Async extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                db.userDao().insert(task);
                return null;
            }

        }

        Async async = new Async();
        async.execute();
    }

    public void update(Task task){

        class Async extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                db.userDao().update(task);
                return null;
            }

        }

        Async async = new Async();
        async.execute();
    }

    public void delete(Task task){

        class Async extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                db.userDao().delete(task);
                return null;
            }

        }

        Async async = new Async();
        async.execute();


    }


}
