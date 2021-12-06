package de.manuelclever.cinema.database.query;

public abstract class DataDesignations {
    //
    public static final String ROW = "row";
    public static final String ROWS = "rows";
    public static final String COLUMN = "column";
    public static final String COLUMNS = "columns";
    public static final String TYPE = "type";
    public static final String SIZE = "size";
    public static final String BLOCK = "block";
    public static final String PRICE = "price";
    public static final String MULTIPLIER = "multiplier";
    public static final String DISCOUNT = "discount";
    public static final String P = ".";
    public static final String C = ", ";

    //tables and columns
    public static final String SCHEME = "cinema_schema";

    public static final String COLUMN_SEATS = "seats";

    public static final String TABLE_CUSTOMER = SCHEME + P + "customer";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_LAST_NAME = "last_name";
    public static final String CUSTOMER_FIRST_NAME = "first_name";
    public static final String CUSTOMER_BIRTHDAY = "birthday";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_DATE_OF_ENTRY = "date_of_entry";
    public static final String CUSTOMER_STREET = "street";
    public static final String CUSTOMER_HOUSE_NUM = "number";
    public static final String CUSTOMER_HOUSE_EXTRA = "extra";
    public static final String CUSTOMER_POSTAL = "postal";
    public static final String CUSTOMER_CITY = "city";
    public static final String CUSTOMER_COUNTRY = "country";

    public static final String TABLE_BONUS_CARD = SCHEME + P + "bonus_card";
    public static final String BONUS_CARD_ID = "bonus_card_id";
    public static final String BONUS_CARD_BALANCE = "balance";
    public static final String BONUS_CARD_POINTS = "points";

    public static final String TABLE_THEATER = SCHEME + P + "theater";
    public static final String THEATER_ID = "theater_id";
    public static final String THEATER_NAME = "name";
    public static final String THEATER_BLOCK = "block";
    public static final String THEATER_EXTRAS = "extras";

    public static final String TABLE_MOVIE = SCHEME + P + "movie";
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_NAME = "name";
    public static final String MOVIE_ORIGINAL_NAME = "original_name";
    public static final String MOVIE_GENRE = "genre";
    public static final String MOVIE_DESCRIPTION = "description";
    public static final String MOVIE_LENGTH = "length";
    public static final String MOVIE_OTHER = "other";
    public static final String MOVIE_ACTORS = "actors";
    public static final String MOVIE_DIRECTORS = "directors";
    public static final String MOVIE_COUNTRY = "country";
    public static final String MOVIE_YEAR = "year";
    public static final String MOVIE_AGE_RESTR = "age_restriction";
    public static final String MOVIE_STUDIO = "studio";
    public static final String MOVIE_TRAILER = "trailer";
    public static final String MOVIE_TAGS = "tags";

    public static final String TABLE_SCREENING = SCHEME + P + "screening";
    public static final String SCREENING_ID = "screening_id";
    public static final String SCREENING_TIME = "screening_time";

    public static final String TABLE_BOOKING = SCHEME + P + "booking";
    public static final String BOOKING_ID = "booking_id";
    public static final String BOOKING_BILL = "bill";
}
