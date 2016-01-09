package com.coding4all.android.movieapp.ContentProviders.MovieContentProvider;

/**
 * Created by abomariam on 06/01/16.
 */

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = MoviesProvider.AUTHORITY,
        database = MoviesDatabase.class)
public final class MoviesProvider {

    public static final String AUTHORITY = "com.coding4all.android.movieapp.MoviesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String TRAILERS = "trailers";
        String REVIEWS = "reviews";

        String FROM_MOVIE = "fromMovie";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }


    @TableEndpoint(table = MoviesDatabase.MOVIES) public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                path = Path.MOVIES + "/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }


    }

    @TableEndpoint(table = MoviesDatabase.TRAILERS) public static class Trailers {


        @ContentUri(
                path = Path.TRAILERS,
                type = "vnd.android.cursor.dir/trailers")
        public static final Uri CONTENT_URI = buildUri(Path.TRAILERS);

        @InexactContentUri(
                name = "TRAILERS_FROM_MOVIE",
                path = Path.TRAILERS + "/" + Path.FROM_MOVIE + "/#",
                type = "vnd.android.cursor.dir/trailers",
                whereColumn = TrailerColumns.MOVIE_ID,
                pathSegment = 2)
        public static Uri fromMovie(long movieId) {
            return buildUri(Path.TRAILERS, Path.FROM_MOVIE, String.valueOf(movieId));
        }
    }

    @TableEndpoint(table = MoviesDatabase.REVIEWS) public static class Reviews {


        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/reviews")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "Reviews_FROM_MOVIE",
                path = Path.REVIEWS + "/" + Path.FROM_MOVIE + "/#",
                type = "vnd.android.cursor.dir/reviews",
                whereColumn = ReviewColumns.MOVIE_ID,
                pathSegment = 2)
        public static Uri fromMovie(long movieId) {
            return buildUri(Path.REVIEWS, Path.FROM_MOVIE, String.valueOf(movieId));
        }
    }


}
