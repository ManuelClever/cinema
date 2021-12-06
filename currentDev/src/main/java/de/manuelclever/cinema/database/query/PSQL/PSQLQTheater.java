package de.manuelclever.cinema.database.query.PSQL;

public class PSQLQTheater extends PSQLQueries {
    
    //select
    public static final String THEATER_SELECT_THEATER = SELECT +
            THEATER_ID + C +
            THEATER_NAME + C +
            THEATER_BLOCK + C +
            THEATER_EXTRAS + FROM + TABLE_THEATER;
    public static final String THEATER_SELECT_NO_SPECIAL = SELECT +
            THEATER_NAME + C +
            THEATER_BLOCK + C + FROM + TABLE_THEATER;
    public static final String THEATER_SELECT_NAME = SELECT +
            THEATER_NAME + FROM + TABLE_THEATER;
    public static final String THEATER_SELECT_ID = SELECT +
            THEATER_ID + FROM + TABLE_THEATER;
    public static final String THEATER_SELECT_SEATS = SELECT +
            THEATER_BLOCK + FROM + TABLE_THEATER;

    //parameter
    public static final String THEATER_PARAMETER_ID = THEATER_ID + PARAMETER;
    public static final String THEATER_PARAMETER_NAME = THEATER_NAME + PARAMETER;

    //delete
    public static final String THEATER_DELETE = DELETE + FROM + TABLE_THEATER;

    //queries
    public static final String THEATER_QUERY_INSERT = INSERT_INTO + TABLE_THEATER + "(" +
            THEATER_NAME + C +
            THEATER_BLOCK + C +
            THEATER_EXTRAS + ") VALUES (?,?,?)"  + RETURNING + THEATER_ID + END;
    public static final String THEATER_QUERY_SELECT_NAME_WHERE_ID =
            THEATER_SELECT_NAME + WHERE + THEATER_PARAMETER_ID + END;
    public static final String THEATER_QUERY_DELETE = THEATER_DELETE + WHERE + THEATER_PARAMETER_ID + END;
    public static final String THEATER_QUERY_ALL = CREATE_JSON + FROM_START + THEATER_SELECT_THEATER +
            FROM + TABLE_THEATER + ORDER_BY + THEATER_ID + FROM_END + AS + ROW + END;

    public static String theaterQueryWhereID(String select) {
        return CREATE_JSON + FROM_START + select +
                WHERE + THEATER_PARAMETER_ID + ORDER_BY + THEATER_ID + FROM_END + AS + ROW + END;
    }
    public static String theaterQueryWhereName(String select) {
        return CREATE_JSON + FROM_START + select +
                THEATER_PARAMETER_NAME + ORDER_BY + THEATER_ID + FROM_END + AS + ROW + END;
    }
    
}
