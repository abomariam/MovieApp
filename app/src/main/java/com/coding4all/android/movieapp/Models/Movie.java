package com.coding4all.android.movieapp.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.MovieColumns;
import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.MoviesProvider;
import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.ReviewColumns;
import com.coding4all.android.movieapp.ContentProviders.MovieContentProvider.TrailerColumns;
import com.coding4all.android.movieapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by abomariam on 04/01/16.
 */
public class Movie implements Parcelable {
    public int id;
    public String title;
    public double popularity;
    public int vote_count ;
    public double vote_average ;
    public String poster;
    public String overview;
    public String release_date;
    public ArrayList<Trailer> trailers;
    public ArrayList<Review> reviews;

    protected Movie() {

    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        popularity = in.readDouble();
        vote_count = in.readInt();
        vote_average = in.readDouble();
        poster = in.readString();
        overview = in.readString();
        release_date = in.readString();
        if (in.readByte() == 0x01) {
            trailers = new ArrayList<Trailer>();
            in.readList(trailers, Trailer.class.getClassLoader());
        } else {
            trailers = null;
        }
        if (in.readByte() == 0x01) {
            reviews = new ArrayList<Review>();
            in.readList(reviews, Review.class.getClassLoader());
        } else {
            reviews = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeInt(vote_count);
        dest.writeDouble(vote_average);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(release_date);
        if (trailers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trailers);
        }
        if (reviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviews);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public static ArrayList<Movie> getMovies(int page_number, String appid, Activity context){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);


        String sortby = sharedPref.getString(context.getString(R.string.sort_by),context.getString(R.string.most_popular_value));

        if (sortby.equals(context.getString(R.string.action_favorite))){

            Cursor cursor = context.getContentResolver().query(MoviesProvider.Movies.CONTENT_URI, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                ArrayList<Movie> moviesList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    Movie movie = new Movie();
                    movie.id = cursor.getInt(cursor.getColumnIndex(MovieColumns._ID));
                    movie.title = cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE));
                    movie.release_date = cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE));
                    movie.poster = cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER));
                    movie.overview = cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW));
                    movie.vote_average = cursor.getDouble(cursor.getColumnIndex(MovieColumns.VOTE_AVG));
                    movie.popularity = cursor.getDouble(cursor.getColumnIndex(MovieColumns.POPULARITY));

                    Cursor trailersCursor = context.getContentResolver().query(MoviesProvider.Trailers.fromMovie(movie.id), null, null, null, null);
                    if (trailersCursor != null && trailersCursor.getCount() > 0) {
                        ArrayList<Trailer> trailersList = new ArrayList<>();

                        while (trailersCursor.moveToNext()) {

                            Trailer trailer = new Trailer();
                            trailer.id = trailersCursor.getString(trailersCursor.getColumnIndex(TrailerColumns._ID));
                            trailer.key = trailersCursor.getString(trailersCursor.getColumnIndex(TrailerColumns.KEY));
                            trailer.title = trailersCursor.getString(trailersCursor.getColumnIndex(TrailerColumns.TITLE));
                            trailersList.add(trailer);
                        }
                        movie.trailers = trailersList;
                        trailersCursor.close();
                    }


                    Cursor reviewsCursor = context.getContentResolver().query(MoviesProvider.Reviews.fromMovie(movie.id), null, null, null, null);
                    if (reviewsCursor != null && reviewsCursor.getCount() > 0) {
                        ArrayList<Review> reviewsList = new ArrayList<>();

                        while (reviewsCursor.moveToNext()) {

                            Review review = new Review();
                            review.id = reviewsCursor.getString(reviewsCursor.getColumnIndex(ReviewColumns._ID));
                            review.author = reviewsCursor.getString(reviewsCursor.getColumnIndex(ReviewColumns.AUTHOR));
                            review.content = reviewsCursor.getString(reviewsCursor.getColumnIndex(ReviewColumns.CONTENT));
                            review.url = reviewsCursor.getString(reviewsCursor.getColumnIndex(ReviewColumns.URL));

                            reviewsList.add(review);
                        }
                        movie.reviews = reviewsList;
                        reviewsCursor.close();
                    }
                    moviesList.add(movie);
                }

                cursor.close();
                return moviesList;
            }
            return null;
        }

        try{

            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORTBY_PARAM = "sort_by";
            final String APPID_PARAM = "api_key";
            final String PAGE_PARAM = "page";

            Uri buildUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORTBY_PARAM, sortby)
                    .appendQueryParameter(APPID_PARAM,appid)
                    .appendQueryParameter(PAGE_PARAM,Integer.toString(page_number))
                    .build();

            URL url = new URL(buildUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line+"\n");
            }

            if (buffer.length() == 0){
                return null;
            }
            String movieJsonStr = buffer.toString();

            try {
                return getMoviesDataFromJson(movieJsonStr, appid);
            } catch (JSONException e){
                Log.e("MovieAPP", e.getMessage(), e);
                e.printStackTrace();
            }

        } catch (IOException e){
            Log.e("MovieAPP","Error",e);
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (final IOException e){
                    Log.e("MovieAPP","Error",e);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr, String appid) throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_TITLE = "title";
        final String MDB_POPULARITY = "popularity";
        final String MDB_VOTE_COUNT = "vote_count";
        final String MDB_VOTE_AVG = "vote_average";
        final String MDB_POSTER = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_DATE = "release_date";



        JSONObject movieJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray(MDB_RESULTS);

        ArrayList<Movie> results = new ArrayList<Movie>();

        for(int i = 0; i < moviesArray.length(); i++) {
            Movie movie = new Movie();

            JSONObject movieObj = moviesArray.getJSONObject(i);

            movie.id = movieObj.getInt(MDB_ID);
            movie.title = movieObj.getString(MDB_TITLE);
            movie.popularity = movieObj.getDouble(MDB_POPULARITY);
            movie.overview = movieObj.getString(MDB_OVERVIEW);
            movie.poster = movieObj.getString(MDB_POSTER);
            movie.release_date = movieObj.getString(MDB_DATE);
            movie.vote_average = movieObj.getDouble(MDB_VOTE_AVG);
            movie.vote_count = movieObj.getInt(MDB_VOTE_COUNT);
            movie.trailers = Trailer.getTrailers(movie.id, appid);
            movie.reviews = Review.getReviews(movie.id, appid);

            results.add(movie);
        }


        return results;

    }
}
