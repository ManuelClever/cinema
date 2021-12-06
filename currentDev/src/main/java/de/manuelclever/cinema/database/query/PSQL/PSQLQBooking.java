package de.manuelclever.cinema.database.query.PSQL;

public class PSQLQBooking extends PSQLQueries {

    //join
    public static final String BOOKING_JOIN_CUSTOMER = INNER_JOIN + TABLE_CUSTOMER +
            ON + TABLE_BOOKING + P + CUSTOMER_ID + " = " + TABLE_CUSTOMER + P + CUSTOMER_ID;
    public static final String BOOKING_JOIN_BONUSCARD = INNER_JOIN + TABLE_BONUS_CARD +
            ON + TABLE_BOOKING + P + CUSTOMER_ID + " = " + TABLE_BONUS_CARD + P + BONUS_CARD_ID;
    public static final String BOOKING_JOIN_SCREENING = INNER_JOIN + TABLE_SCREENING +
            ON + TABLE_BOOKING + P + SCREENING_ID + " = " + TABLE_SCREENING + P + SCREENING_ID;
    public static final String BOOKING_JOIN_THEATER = INNER_JOIN + TABLE_THEATER +
            ON + TABLE_SCREENING + P + THEATER_ID + " = " + TABLE_THEATER + P + THEATER_ID;
    public static final String BOOKING_JOIN_MOVIE = INNER_JOIN + TABLE_MOVIE +
            ON + TABLE_SCREENING + P + MOVIE_ID + " = " + TABLE_MOVIE + P + MOVIE_ID;

    //select
    public static final String BOOKING_SELECT_BOOKING = SELECT +
            BOOKING_ID + C +
            TABLE_CUSTOMER + P + CUSTOMER_ID + C +
            TABLE_CUSTOMER + P + CUSTOMER_LAST_NAME + C +
            TABLE_CUSTOMER + P + CUSTOMER_FIRST_NAME + C +
            TABLE_CUSTOMER + P + CUSTOMER_EMAIL + C +
            TABLE_CUSTOMER + P + CUSTOMER_STREET + C +
            TABLE_CUSTOMER + P + CUSTOMER_HOUSE_NUM + C +
            TABLE_CUSTOMER + P + CUSTOMER_HOUSE_EXTRA + C +
            TABLE_CUSTOMER + P + CUSTOMER_POSTAL + C +
            TABLE_CUSTOMER + P + CUSTOMER_CITY + C +
            TABLE_CUSTOMER + P + CUSTOMER_COUNTRY + C +
            TABLE_MOVIE + P + MOVIE_ID + C +
            TABLE_MOVIE + P + MOVIE_NAME + C +
            TABLE_MOVIE + P + MOVIE_LENGTH + C +
            TABLE_MOVIE + P + MOVIE_OTHER + C +
            TABLE_SCREENING + P + SCREENING_ID + C +
            TABLE_SCREENING + P + SCREENING_TIME + C +
            TABLE_THEATER + P + THEATER_ID + C +
            TABLE_THEATER + P + THEATER_NAME + C +
            BOOKING_BILL + FROM + TABLE_BOOKING;
    public static final String BOOKING_SELECT_SEATS = SELECT +
            COLUMN_SEATS + FROM + TABLE_BOOKING;
    public static final String BOOKING_SELECT_SIMPLE = SELECT +
            BOOKING_ID + C +
            TABLE_CUSTOMER + P + CUSTOMER_LAST_NAME + C +
            TABLE_CUSTOMER + P + CUSTOMER_FIRST_NAME + C +
            TABLE_MOVIE + P + MOVIE_NAME + C +
            TABLE_SCREENING + P + SCREENING_TIME + C +
            TABLE_THEATER + P + THEATER_NAME + C +
            COLUMN_SEATS + FROM + TABLE_BOOKING;
    public static final String BOOKING_SELECT_FOR_SCREENING = SELECT +
            BOOKING_ID + C +
            TABLE_MOVIE + P + MOVIE_NAME + C +
            TABLE_CUSTOMER + P + CUSTOMER_LAST_NAME + C +
            TABLE_CUSTOMER + P + CUSTOMER_FIRST_NAME + C +
            COLUMN_SEATS + FROM + TABLE_BOOKING;

    //parameter
    public static final String BOOKING_PARAMETER_ID = BOOKING_ID + PARAMETER;
    public static final String BOOKING_PARAMETER_SCREENING_ID = SCREENING_ID + PARAMETER;
    public static final String BOOKING_PARAMETER_CUSTOMER_ID = CUSTOMER_ID + PARAMETER;

    //delete
    public static final String BOOKING_DELETE = DELETE + FROM + TABLE_BOOKING;

    //queries
    public static final String BOOKING_QUERY_INSERT = INSERT_INTO + TABLE_BOOKING + "(" +
            CUSTOMER_ID + C +
            SCREENING_ID + C +
            BOOKING_BILL + ") VALUES (?,?,?)" + RETURNING + BOOKING_ID + END;
    public static final String BOOKING_QUERY_DELETE_WHERE_ID = BOOKING_DELETE + WHERE + BOOKING_PARAMETER_ID + END;
    public static final String BOOKING_QUERY_WHERE_SCREENING_ID =
            CREATE_JSON + FROM_START + BOOKING_SELECT_FOR_SCREENING +
                    BOOKING_JOIN_SCREENING +
                    WHERE + BOOKING_PARAMETER_SCREENING_ID + ORDER_BY + BOOKING_ID + FROM_END + AS + ROW + END;
    public static final String BOOKING_QUERY_WHERE_TIME =
            CREATE_JSON + FROM_START + BOOKING_SELECT_BOOKING +
                    BOOKING_JOIN_SCREENING +
                    WHERE + PSQLQScreening.SCREENING_PARAMETER_TIME + ORDER_BY + BOOKING_ID + FROM_END + AS + ROW + END;

    public static String bookingQueryWhereID(String select) {
        return CREATE_JSON + FROM_START + select +
                BOOKING_JOIN_CUSTOMER +
                BOOKING_JOIN_BONUSCARD +
                BOOKING_JOIN_SCREENING +
                BOOKING_JOIN_MOVIE +
                BOOKING_JOIN_THEATER +
                WHERE + BOOKING_PARAMETER_ID + ORDER_BY + BOOKING_ID + FROM_END + AS + ROW + END;
    }
}
