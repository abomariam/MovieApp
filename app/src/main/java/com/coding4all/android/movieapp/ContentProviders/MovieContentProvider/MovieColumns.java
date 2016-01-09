package com.coding4all.android.movieapp.ContentProviders.MovieContentProvider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by abomariam on 06/01/16.
 */
public interface MovieColumns {
    @DataType(INTEGER) @PrimaryKey String _ID = "_id";
    @DataType(TEXT) String TITLE = "title";
    @DataType(REAL) String POPULARITY = "popularity";
    @DataType(INTEGER) String VOTE_COUNT = "vote_count";
    @DataType(REAL) String VOTE_AVG = "vote_avg";
    @DataType(TEXT) String POSTER = "poster";
    @DataType(TEXT) String OVERVIEW = "overview";
    @DataType(TEXT) String RELEASE_DATE = "release_date";

}
