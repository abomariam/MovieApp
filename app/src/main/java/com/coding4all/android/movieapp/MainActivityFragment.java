package com.coding4all.android.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.coding4all.android.movieapp.Models.Movie;
import com.coding4all.android.movieapp.adapters.MovieAdapter;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    MovieAdapter myMoviesAdapter;
    GridView moviesGridView;
    ArrayList<Movie> moviesList;


    int PAGE_NUMBER;
    boolean UPDATING;
    public MainActivityFragment() {
    }


    public interface Callbacks {
        public void onItemSelected(Movie item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        PAGE_NUMBER = 1;

        moviesList = new ArrayList<Movie>();

        myMoviesAdapter = new MovieAdapter(getContext(),moviesList);

        moviesGridView = (GridView) rootView.findViewById(R.id.main_grid);
        moviesGridView.setAdapter(myMoviesAdapter);
        moviesGridView.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !UPDATING) {
                    updateMovies(PAGE_NUMBER);
                }
            }
        });

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = moviesList.get(position);
                Fragment fragment = new DetailsActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", movie);
                fragment.setArguments(bundle);

                if(isTablet(getActivity()))
                {
                    if(getActivity().findViewById(R.id.fragment_details) == null)
                        getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_details,fragment)
                            .commit();
                    else
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_details,fragment)
                                .commit();
                } else {

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main,fragment)
                            .commit();


                }


            }
        });




        return rootView;
    }


    @Override
    public void onStart(){
        super.onStart();
        updateMovies(PAGE_NUMBER);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (id == R.id.action_most_popular) {
            editor.putString(getString(R.string.sort_by), getString(R.string.most_popular_value));
            editor.commit();

            moviesList.clear();
            myMoviesAdapter.notifyDataSetChanged();
            PAGE_NUMBER = 1;
            updateMovies(PAGE_NUMBER);

            return true;
        } else if (id == R.id.action_highest_rated){
            editor.putString(getString(R.string.sort_by), getString(R.string.highest_rated_value));
            editor.commit();

            moviesList.clear();
            myMoviesAdapter.notifyDataSetChanged();
            PAGE_NUMBER = 1;
            updateMovies(PAGE_NUMBER);

            return true;
        }else if (id == R.id.action_favorite){
            editor.putString(getString(R.string.sort_by), getString(R.string.action_favorite));
            editor.commit();

            moviesList.clear();
            myMoviesAdapter.notifyDataSetChanged();
            PAGE_NUMBER = 1;
            updateMovies(PAGE_NUMBER);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(int page_number){
        UpdateMoviesTask task = new UpdateMoviesTask();
        task.execute(page_number);
    }


    private class UpdateMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {
        final String LOG_TAG = UpdateMoviesTask.class.getSimpleName();

        final String appid = "06348d3c6a44006d83d9f1d8c5a17721";


        @Override
        protected void onPreExecute() {
            UPDATING = true;
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {
            return Movie.getMovies(params[0], appid, getActivity());
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null && getActivity() != null) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String sortby = sharedPref.getString(getActivity().getString(R.string.sort_by), getActivity().getString(R.string.most_popular_value));

                if (sortby.equals(getActivity().getString(R.string.action_favorite))) {
                    moviesList.clear();
                }

                for (Movie movie : result) {
                    moviesList.add(movie);
                }

                myMoviesAdapter.notifyDataSetChanged();

                PAGE_NUMBER++;
            }
            UPDATING = false;
        }


    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
