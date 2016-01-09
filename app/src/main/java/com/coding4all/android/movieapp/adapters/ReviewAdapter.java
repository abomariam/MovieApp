package com.coding4all.android.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coding4all.android.movieapp.R;
import com.coding4all.android.movieapp.Models.Review;

import java.util.ArrayList;

/**
 * Created by abomariam on 05/01/16.
 */
public class ReviewAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Review> reviewsList;

    public ReviewAdapter(Context c,ArrayList<Review> reviews) {
        mContext = c;
        reviewsList = reviews;
    }

    @Override
    public int getCount() {
        if (reviewsList != null) {
            return reviewsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return reviewsList.get(position);
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
            view = inflator.inflate(R.layout.listitem_review, null);

        } else {
            view =  convertView;
        }
        TextView textView_author = (TextView) view.findViewById(R.id.textview_review_author);
        textView_author.setText(reviewsList.get(position).author);

        TextView textView_content = (TextView) view.findViewById(R.id.textview_review_content);
        textView_content.setText(reviewsList.get(position).content);

        return view;
    }
}
