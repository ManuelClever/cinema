package de.manuelclever.cinema.database.query.PSQL;

public class PSQLQMovie extends PSQLQueries {
    //movie
    public static final String MOVIE_JOIN_SCREENING = INNER_JOIN + TABLE_SCREENING +
            ON + TABLE_MOVIE + P + MOVIE_ID + " = " + TABLE_SCREENING + P + MOVIE_ID;

    public static final String MOVIE_SELECT_MOVIE = SELECT +
            MOVIE_ID + C +
            MOVIE_NAME + C +
            MOVIE_ORIGINAL_NAME + C +
            MOVIE_GENRE + C +
            MOVIE_DESCRIPTION + C +
            MOVIE_LENGTH + C +
            MOVIE_OTHER + C +
            MOVIE_ACTORS + C +
            MOVIE_DIRECTORS + C +
            MOVIE_COUNTRY + C +
            MOVIE_YEAR + C +
            MOVIE_AGE_RESTR + C +
            MOVIE_STUDIO + C +
            MOVIE_TRAILER + C +
            MOVIE_TAGS + FROM + TABLE_MOVIE;
    public static final String MOVIE_SELECT_SIMPLE = SELECT +
            MOVIE_NAME + C +
            MOVIE_GENRE + C +
            MOVIE_DESCRIPTION + C +
            MOVIE_OTHER + C +
            MOVIE_AGE_RESTR + C + FROM + TABLE_MOVIE;
    public static final String MOVIE_SELECT_NAME = SELECT +
            MOVIE_NAME + FROM + TABLE_MOVIE;

    public static final String MOVIE_UPDATE = UPDATE + TABLE_MOVIE + " SET (" +
            MOVIE_NAME + C +
            MOVIE_ORIGINAL_NAME + C +
            MOVIE_GENRE + C +
            MOVIE_DESCRIPTION + C +
            MOVIE_LENGTH + C +
            MOVIE_ACTORS + C +
            MOVIE_DIRECTORS + C +
            MOVIE_COUNTRY + C +
            MOVIE_YEAR + C +
            MOVIE_AGE_RESTR + C +
            MOVIE_OTHER + C +
            MOVIE_STUDIO + C +
            MOVIE_TRAILER + C +
            MOVIE_TAGS + ") = (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String MOVIE_DELETE = DELETE + FROM + TABLE_MOVIE;

    public static final String MOVIE_PARAMETER_ID = MOVIE_ID + PARAMETER;
    public static final String MOVIE_PARAMETER_NAME = MOVIE_NAME + PARAMETER;
    public static final String MOVIE_PARAMETER_ORIGINAL_NAME = MOVIE_ORIGINAL_NAME + PARAMETER;
    public static final String MOVIE_PARAMETER_GENRE = MOVIE_GENRE + PARAMETER;
    public static final String MOVIE_PARAMETER_YEAR = MOVIE_YEAR + PARAMETER;
    public static final String MOVIE_PARAMETER_AGE_RESTR = MOVIE_AGE_RESTR + PARAMETER;
    public static final String MOVIE_PARAMETER_TAGS = MOVIE_TAGS + PARAMETER;

    public static final String MOVIE_QUERY_INSERT = INSERT_INTO + TABLE_MOVIE + "(" +
            MOVIE_NAME + C +
            MOVIE_ORIGINAL_NAME + C +
            MOVIE_GENRE + C +
            MOVIE_DESCRIPTION + C +
            MOVIE_LENGTH + C +
            MOVIE_OTHER + C +
            MOVIE_ACTORS + C +
            MOVIE_DIRECTORS + C +
            MOVIE_COUNTRY + C +
            MOVIE_YEAR + C +
            MOVIE_AGE_RESTR + C +
            MOVIE_STUDIO + C +
            MOVIE_TRAILER + C +
            MOVIE_TAGS + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"  + RETURNING + MOVIE_ID + END;;
    public static final String MOVIE_QUERY_UPDATE_WHERE_IP = MOVIE_UPDATE + WHERE + MOVIE_PARAMETER_ID + END;
    public static final String MOVIE_QUERY_DELETE = MOVIE_DELETE + WHERE + MOVIE_PARAMETER_ID + END;

    public static String movieQueryWhereID(String select) {
        return CREATE_JSON + FROM_START + select +
                WHERE + MOVIE_PARAMETER_ID + ORDER_BY + MOVIE_ID + FROM_END + AS + ROW + END;
    }

    public static String movieQueryWhereIDOneResult(String select) {
        return CREATE_JSON + FROM_START + select +
                MOVIE_PARAMETER_ID + ORDER_BY + MOVIE_ID + FROM_END + AS + ROW + END;
    }
}
