package com.example.todolist;

import android.content.Intent;
import android.util.Log;
import android.view.View;

public class FabListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        Log.i(MainActivity.TAG, "New item option is clicked");
        Intent myIntent = new Intent(v.getContext(), ItemActivity.class);
        v.getContext().startActivity(myIntent);

    }
}
