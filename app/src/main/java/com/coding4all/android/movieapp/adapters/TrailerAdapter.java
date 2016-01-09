package com.coding4all.android.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coding4all.android.movieapp.R;
import com.coding4all.android.movieapp.Models.Trailer;

import java.util.ArrayList;

/**
 * Created by abomariam on 05/01/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Trailer> trailersList;

    public TrailerAdapter(Context c,ArrayList<Trailer> trailers) {
        mContext = c;
        trailersList = trailers;
    }

    @Override
    public int getCount() {
        if (trailersList != null) {
            return trailersList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return trailersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.listitem_trailer, null);

        } else {
            view =  convertView;
        }
        TextView textView_title = (TextView) view.findViewById(R.id.textview_trailer_title);
        textView_title.setText(trailersList.get(position).title);

        view.setOnClickListener(new TrailerOnClickListener(trailersList.get(position).key));
        return view;
    }

    private class TrailerOnClickListener implements View.OnClickListener{
        String key;

        public TrailerOnClickListener(String key){
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+key));
            mContext.startActivity(intent);
        }
    }
}
