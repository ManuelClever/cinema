package de.manuelclever.cinema.database.query.PSQL;

public class PSQLQScreening extends PSQLQueries {

    //join
    public static final String SCREENING_JOIN_MOVIE = INNER_JOIN + TABLE_MOVIE +
            ON + TABLE_SCREENING + P + MOVIE_ID + " = " + TABLE_MOVIE + P + MOVIE_ID;
    public static final String SCREENING_JOIN_THEATER = INNER_JOIN + TABLE_THEATER +
            ON + TABLE_SCREENING + P + THEATER_ID + " = " + TABLE_THEATER + P + THEATER_ID;

    //select
    public static final String SCREENING_SELECT_SCREENING = SELECT +
            SCREENING_ID + C +
            SCREENING_TIME + C +
            TABLE_SCREENING + P + MOVIE_ID + C +
            TABLE_SCREENING + P + THEATER_ID + FROM + TABLE_SCREENING;
    public static final String SCREENING_SELECT_MOVIE_TIME_THEATER = SELECT +
            SCREENING_ID + C +
            SCREENING_TIME + C +
            TABLE_MOVIE + P + MOVIE_ID + C +
            TABLE_MOVIE + P + MOVIE_NAME + C +
            TABLE_THEATER + P + THEATER_ID + C +
            TABLE_THEATER + P + THEATER_NAME + FROM + TABLE_SCREENING;
    public static final String SCREENING_SELECT_MOVIE_TIME = SELECT +
            TABLE_MOVIE + P + MOVIE_NAME + C +
            SCREENING_TIME + C + FROM + TABLE_SCREENING;
    public static final String SCREENING_SELECT_TIME_THEATER = SELECT +
            TABLE_SCREENING + P + SCREENING_ID + C +
            SCREENING_TIME + C +
            TABLE_THEATER + P + THEATER_ID + C +
            TABLE_THEATER + P + THEATER_NAME + FROM + TABLE_SCREENING;
    public static final String SCREENING_SELECT_SEATS = SELECT +
            TABLE_THEATER + P + COLUMN_SEATS + C +
            TABLE_BOOKING + P + COLUMN_SEATS + FROM + TABLE_SCREENING;

    public static final String SCREENING_UPDATE = UPDATE + TABLE_SCREENING + " SET (" +
            TABLE_SCREENING + P + MOVIE_ID + C +
            SCREENING_TIME + C +
            TABLE_SCREENING + P + THEATER_ID + ") = (?, ?, ?)";

    //delete
    public static final String SCREENING_DELETE = DELETE + FROM + TABLE_SCREENING;

    //parameter
    public static final String SCREENING_PARAMETER_ID = SCREENING_ID + PARAMETER;
    public static final String SCREENING_PARAMETER_MOVIE_ID = TABLE_SCREENING + P + MOVIE_ID + PARAMETER;
    public static final String SCREENING_PARAMETER_THEATER_ID = TABLE_THEATER + P + THEATER_ID + PARAMETER;
    public static final String SCREENING_PARAMETER_TIME = SCREENING_TIME + " BETWEEN ? AND ?";

    //queries
    public static final String SCREENING_QUERY_INSERT = INSERT_INTO + TABLE_SCREENING + "(" +
            MOVIE_ID + C +
            THEATER_ID + C +
            SCREENING_TIME + ") VALUES (?,?,?)" + RETURNING + SCREENING_ID + END;;
    public static final String SCREENING_QUERY_UPDATE_WHERE_IP = SCREENING_UPDATE + WHERE + SCREENING_PARAMETER_ID + END;
    public static final String SCREENING_QUERY_DELETE = SCREENING_DELETE + WHERE + SCREENING_PARAMETER_ID + END;
    public static final String SCREENING_QUERY_WHERE_TIME =
            CREATE_JSON + FROM_START + SCREENING_SELECT_MOVIE_TIME_THEATER +
                    SCREENING_JOIN_MOVIE +
                    SCREENING_JOIN_THEATER +
                    WHERE + SCREENING_PARAMETER_TIME + ORDER_BY + SCREENING_ID + FROM_END + AS + ROW + END;
    public static final String SCREENING_QUERY_WHERE_MOVIE_ID =
            CREATE_JSON + FROM_START + SCREENING_SELECT_TIME_THEATER +
                    SCREENING_JOIN_MOVIE +
                    SCREENING_JOIN_THEATER +
                    WHERE + SCREENING_PARAMETER_MOVIE_ID + ORDER_BY + SCREENING_ID + FROM_END + AS + ROW + END;
    public static final String SCREENING_QUERY_WHERE_MOVIE_ID_TIME =
            CREATE_JSON + FROM_START + SCREENING_SELECT_TIME_THEATER +
                    SCREENING_JOIN_MOVIE +
                    SCREENING_JOIN_THEATER +
                    WHERE + SCREENING_PARAMETER_MOVIE_ID + AND + SCREENING_PARAMETER_TIME + ORDER_BY + SCREENING_ID + FROM_END + AS + ROW + END;

    public static String screeningQueryWhereID(String select) {
        return CREATE_JSON + FROM_START + select +
                SCREENING_JOIN_MOVIE +
                SCREENING_JOIN_THEATER +
                WHERE + SCREENING_PARAMETER_ID + ORDER_BY + SCREENING_ID + FROM_END + AS + ROW + END;
    }
}
