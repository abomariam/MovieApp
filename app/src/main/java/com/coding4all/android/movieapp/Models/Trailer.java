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
public class Trailer implements Parcelable {
    public String id;
    public String key;
    public String title;


    protected Trailer() {

    }
    protected Trailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };


    private static ArrayList<Trailer> getTrailersFromJson(String trailerJsonStr) throws JSONException {


        final String TRAILER_RESULTS = "results";
        final String TRAILER_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_TITLE = "name";

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONArray trailersArray = trailerJson.getJSONArray(TRAILER_RESULTS);

        ArrayList<Trailer> results = new ArrayList<Trailer>();

        for(int i = 0; i < trailersArray.length(); i++) {
            Trailer trailer = new Trailer();

            JSONObject trailerObj = trailersArray.getJSONObject(i);



            trailer.id = trailerObj.getString(TRAILER_ID);
            trailer.key = trailerObj.getString(TRAILER_KEY);
            trailer.title = trailerObj.getString(TRAILER_TITLE);

            results.add(trailer);
        }


        return results;
    }



    public static ArrayList<Trailer> getTrailers(int movie_id,String appid){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{

            final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/"+Integer.toString(movie_id)+"/videos?";
            final String APPID_PARAM = "api_key";

            Uri buildUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
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
            String trailerJsonStr = buffer.toString();

            try {

                return getTrailersFromJson(trailerJsonStr);
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
}