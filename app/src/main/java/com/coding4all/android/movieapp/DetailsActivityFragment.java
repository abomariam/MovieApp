package com.coding4all.android.movieapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.MovieColumns;
import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.MoviesProvider;
import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.ReviewColumns;
import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.TrailerColumns;
import com.coding4all.android.movieapp.Models.Movie;
import com.coding4all.android.movieapp.adapters.ReviewAdapter;
import com.coding4all.android.movieapp.adapters.TrailerAdapter;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {


    boolean isFavorited = false;
    FloatingActionButton fav_button;

    public DetailsActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public static DetailsActivityFragment newInstance(Bundle args) {
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);

        TextView textview_title = (TextView) rootView.findViewById(R.id.textview_movie_title);
        TextView textview_overview = (TextView) rootView.findViewById(R.id.textview_movie_overview);
        TextView textview_date = (TextView) rootView.findViewById(R.id.textview_movie_date);
        TextView textview_rate = (TextView) rootView.findViewById(R.id.textview_movie_rate);
        ImageView imageview_poster = (ImageView) rootView.findViewById(R.id.imageview_movie_poster);

        LinearLayout layout_reviews = (LinearLayout) rootView.findViewById(R.id.layout_reviews);
        LinearLayout layout_trailers = (LinearLayout) rootView.findViewById(R.id.layout_trailers);

        fav_button = (FloatingActionButton) rootView.findViewById(R.id.button_favorite);

        String BASE_URL = "http://image.tmdb.org/t/p/w342";

        Bundle mBundle = this.getArguments();

        if (mBundle != null && mBundle.containsKey("movie") ){
            final Movie movie = (Movie) mBundle.getParcelable("movie");


            Cursor cursor = getContext().getContentResolver().query(MoviesProvider.Movies.withId(movie.id), null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                fav_button.setImageResource(android.R.drawable.btn_star_big_on);
                isFavorited = true;
                cursor.close();
            }


            fav_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavorited){
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                getContext().getContentResolver().delete(MoviesProvider.Trailers.fromMovie(movie.id), null, null);
                                getContext().getContentResolver().delete(MoviesProvider.Reviews.fromMovie(movie.id), null, null);
                                getContext().getContentResolver().delete(MoviesProvider.Movies.withId(movie.id), null, null);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                fav_button.setImageResource(android.R.drawable.btn_star_big_off);
                                isFavorited = false;
                            }
                        }.execute();

                }else{
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                ContentValues cv = new ContentValues();
                                cv.put(MovieColumns._ID, movie.id);
                                cv.put(MovieColumns.TITLE, movie.title);
                                cv.put(MovieColumns.RELEASE_DATE, movie.release_date);
                                cv.put(MovieColumns.POSTER, movie.poster);
                                cv.put(MovieColumns.OVERVIEW, movie.overview);
                                cv.put(MovieColumns.VOTE_AVG, movie.vote_average);
                                cv.put(MovieColumns.POPULARITY, movie.popularity);
                                getContext().getContentResolver().insert(MoviesProvider.Movies.CONTENT_URI, cv);

                                if (movie.trailers != null) {
                                    for (int i = 0; i < movie.trailers.size(); i++) {
                                        cv = new ContentValues();
                                        cv.put(TrailerColumns._ID, movie.trailers.get(i).id);
                                        cv.put(TrailerColumns.KEY, movie.trailers.get(i).key);
                                        cv.put(TrailerColumns.TITLE, movie.trailers.get(i).title);
                                        cv.put(TrailerColumns.MOVIE_ID, movie.id);
                                        getContext().getContentResolver().insert(MoviesProvider.Trailers.CONTENT_URI, cv);
                                    }
                                }

                                if (movie.reviews != null) {
                                    for (int i = 0; i < movie.reviews.size(); i++) {
                                        cv = new ContentValues();
                                        cv.put(ReviewColumns._ID, movie.reviews.get(i).id);
                                        cv.put(ReviewColumns.AUTHOR, movie.reviews.get(i).author);
                                        cv.put(ReviewColumns.CONTENT, movie.reviews.get(i).content);
                                        cv.put(ReviewColumns.URL, movie.reviews.get(i).url);
                                        cv.put(ReviewColumns.MOVIE_ID, movie.id);
                                        getContext().getContentResolver().insert(MoviesProvider.Reviews.CONTENT_URI, cv);

                                    }
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                fav_button.setImageResource(android.R.drawable.btn_star_big_on);
                                isFavorited = true;
                            }
                        }.execute();
                    }
                }
            });


            textview_title.setText(movie.title);
            textview_overview.setText(movie.overview);
            textview_date.setText(movie.release_date.split("-")[0]);
            textview_rate.setText(Double.toString(movie.vote_average) + "/10");
            Picasso.with(getContext()).load(BASE_URL+movie.poster).into(imageview_poster);


            ReviewAdapter reviewsAdapter = new ReviewAdapter(getContext(),movie.reviews);

            for (int i = 0; i < reviewsAdapter.getCount(); i++) {
                View item = reviewsAdapter.getView(i,null,null);
                layout_reviews.addView(item);
            }


            TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(),movie.trailers);

            for (int i = 0; i < trailerAdapter.getCount(); i++) {
                View item = trailerAdapter.getView(i,null,null);
                layout_trailers.addView(item);
            }



        }

        return rootView;
    }


}
