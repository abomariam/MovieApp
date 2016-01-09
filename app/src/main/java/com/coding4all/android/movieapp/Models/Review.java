package com.coding4all.android.movieapp.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
public class Review implements Parcelable {
    public String id;
    public String author;
    public String content;
    public String url;


    protected Review() {

    }
    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };


    public static ArrayList<Review> getReviews(int movie_id, String appid){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try{

            final String BASE_URL = "http://api.themoviedb.org/3/movie/"+Integer.toString(movie_id)+"/reviews?";
            final String APPID_PARAM = "api_key";

            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM,appid)
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
            String jsonStr = buffer.toString();

            try {

                return getDataFromJson(jsonStr);
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


    private static ArrayList<Review> getDataFromJson(String jsonStr) throws JSONException{


        final String RESULTS = "results";
        final String ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        JSONObject json = new JSONObject(jsonStr);
        JSONArray array = json.getJSONArray(RESULTS);

        ArrayList<Review> results = new ArrayList<Review>();

        for(int i = 0; i < array.length(); i++) {
            Review review = new Review();

            JSONObject obj = array.getJSONObject(i);



            review.id = obj.getString(ID);
            review.author = obj.getString(AUTHOR);
            review.content = obj.getString(CONTENT);
            review.url = obj.getString(URL);


            results.add(review);
        }


        return results;
    }
}
