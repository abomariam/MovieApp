package com.coding4all.android.movieapp.ContentProviders.MovieContentProvider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by abomariam on 06/01/16.
 */
public interface ReviewColumns {
    @DataType(TEXT) @PrimaryKey
    String _ID = "_id";

    @DataType(TEXT) String AUTHOR = "author";
    @DataType(TEXT) String CONTENT = "content";
    @DataType(TEXT) String URL = "url";

    @DataType(INTEGER) @References(table = MoviesDatabase.TRAILERS, column = MovieColumns._ID)
    String MOVIE_ID = "movie_id";

}
