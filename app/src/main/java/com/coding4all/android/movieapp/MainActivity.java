package com.coding4all.android.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String sort_by = sharedPref.getString(getString(R.string.sort_by),getString(R.string.most_popular_value));

        if (sort_by.equals(getString(R.string.highest_rated_value))) {
            menu.findItem(R.id.action_most_popular).setChecked(false);
            menu.findItem(R.id.action_favorite).setChecked(false);
            menu.findItem(R.id.action_highest_rated).setChecked(true);
        } else if (sort_by.equals(getString(R.string.most_popular_value))) {
            menu.findItem(R.id.action_most_popular).setChecked(true);
            menu.findItem(R.id.action_favorite).setChecked(false);
            menu.findItem(R.id.action_highest_rated).setChecked(false);
        } else if (sort_by.equals(getString(R.string.action_favorite))) {
            menu.findItem(R.id.action_most_popular).setChecked(false);
            menu.findItem(R.id.action_highest_rated).setChecked(false);
            menu.findItem(R.id.action_favorite).setChecked(true);
        }
        return true;
    }


}
