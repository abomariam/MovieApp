package com.coding4all.android.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coding4all.android.movieapp.Helper;
import com.coding4all.android.movieapp.Models.Movie;
import com.coding4all.android.movieapp.R;

import java.util.ArrayList;

/**
 * Created by abomariam on 09/01/16.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Movie> moviesList;
    private String BASE_URL = "http://image.tmdb.org/t/p/w342";

    public MovieAdapter(Context c,ArrayList<Movie> movies) {
        mContext = c;
        moviesList = movies;
    }

    public int getCount() {
        return moviesList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        View view;

        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.image_view_main, null);
            imageView = (ImageView) view.findViewById(R.id.image_view_main);

        } else {
            imageView = (ImageView) convertView;
        }
        Helper.loadImage(mContext, BASE_URL + moviesList.get(position).poster, imageView);
        return imageView;
    }
}
