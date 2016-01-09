package com.coding4all.android.movieapp.ContentProviders.MovieContentProvider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by abomariam on 06/01/16.
 */
@Database(version = MoviesDatabase.VERSION)
public final class MoviesDatabase {
    public static final int VERSION = 1;

    private MoviesDatabase(){

    }

    @Table(MovieColumns.class) @IfNotExists
    public static final String MOVIES = "movies";

    @Table(TrailerColumns.class) @IfNotExists
    public static final String TRAILERS = "trailers";

    @Table(ReviewColumns.class) @IfNotExists
    public static final String REVIEWS = "reviews";


}
