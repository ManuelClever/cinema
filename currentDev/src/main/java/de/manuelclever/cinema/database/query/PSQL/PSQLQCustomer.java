package de.manuelclever.cinema.database.query.PSQL;

public class PSQLQCustomer extends PSQLQueries {

    //join
    public static final String CUSTOMER_JOIN_BONUS_CARD = INNER_JOIN + TABLE_BONUS_CARD +
            ON + CUSTOMER_ID + " = " + BONUS_CARD_ID;

    //select
    public static final String CUSTOMER_SELECT_CUSTOMER = SELECT +
            CUSTOMER_ID + C +
            CUSTOMER_LAST_NAME + C +
            CUSTOMER_FIRST_NAME + C +
            CUSTOMER_BIRTHDAY + C +
            CUSTOMER_EMAIL + C +
            CUSTOMER_DATE_OF_ENTRY + C +
            CUSTOMER_STREET + C +
            CUSTOMER_HOUSE_NUM + C +
            CUSTOMER_HOUSE_EXTRA + C +
            CUSTOMER_POSTAL + C +
            CUSTOMER_CITY + C +
            CUSTOMER_COUNTRY + FROM + TABLE_CUSTOMER;
    public static final String CUSTOMER_SELECT_NO_ADDRESS = SELECT +
            CUSTOMER_ID + C +
            CUSTOMER_LAST_NAME + C +
            CUSTOMER_FIRST_NAME + C +
            CUSTOMER_BIRTHDAY + C +
            CUSTOMER_EMAIL + C +
            CUSTOMER_DATE_OF_ENTRY + FROM + TABLE_CUSTOMER;
    public static final String CUSTOMER_SELECT_ADDRESS = SELECT +
            CUSTOMER_LAST_NAME + C +
            CUSTOMER_FIRST_NAME + C +
            CUSTOMER_STREET + C +
            CUSTOMER_HOUSE_NUM + C +
            CUSTOMER_HOUSE_EXTRA + C +
            CUSTOMER_POSTAL + C +
            CUSTOMER_CITY + C +
            CUSTOMER_COUNTRY + FROM + TABLE_CUSTOMER;
    public static final String CUSTOMER_SELECT_SIMPLE = SELECT +
            CUSTOMER_ID + C +
            CUSTOMER_LAST_NAME + C +
            CUSTOMER_FIRST_NAME + C +
            CUSTOMER_BIRTHDAY + FROM + TABLE_CUSTOMER;
    public static final String CUSTOMER_SELECT_BALANCE = SELECT + BONUS_CARD_BALANCE + FROM + TABLE_CUSTOMER + END;
    public static final String CUSTOMER_SELECT_POINTS = SELECT + BONUS_CARD_POINTS + FROM + TABLE_CUSTOMER + END;

    //update
    public static final String CUSTOMER_UPDATE = UPDATE + TABLE_CUSTOMER + " SET (" +
            CUSTOMER_LAST_NAME + C +
            CUSTOMER_FIRST_NAME + C +
            CUSTOMER_BIRTHDAY + C +
            CUSTOMER_EMAIL + C +
            CUSTOMER_DATE_OF_ENTRY + C +
            CUSTOMER_STREET + C +
            CUSTOMER_HOUSE_NUM + C +
            CUSTOMER_HOUSE_EXTRA + C +
            CUSTOMER_POSTAL + C +
            CUSTOMER_CITY + ") = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    //delete
    public static final String CUSTOMER_DELETE = DELETE + FROM + TABLE_CUSTOMER;

    //parameter
    public static final String CUSTOMER_PARAMETER_ID = CUSTOMER_ID + PARAMETER;
    public static final String CUSTOMER_PARAMETER_LAST_NAME = CUSTOMER_LAST_NAME + PARAMETER;
    public static final String CUSTOMER_PARAMETER_FIRST_NAME = CUSTOMER_FIRST_NAME + PARAMETER;
    public static final String CUSTOMER_PARAMETER_BIRTHDAY = CUSTOMER_BIRTHDAY + PARAMETER;
    public static final String CUSTOMER_PARAMETER_STREET = CUSTOMER_STREET + PARAMETER;
    public static final String CUSTOMER_PARAMETER_POSTAL = CUSTOMER_POSTAL + PARAMETER;
    public static final String CUSTOMER_PARAMETER_CITY = CUSTOMER_CITY + PARAMETER;
    public static final String CUSTOMER_PARAMETER_COUNTRY = CUSTOMER_COUNTRY + PARAMETER;

    //queries
    public static final String CUSTOMER_QUERY_INSERT = INSERT_INTO + TABLE_CUSTOMER + "(" +
            CUSTOMER_LAST_NAME + C +
            CUSTOMER_FIRST_NAME + C +
            CUSTOMER_BIRTHDAY + C +
            CUSTOMER_EMAIL + C +
            CUSTOMER_DATE_OF_ENTRY + C +
            CUSTOMER_STREET + C +
            CUSTOMER_HOUSE_NUM + C +
            CUSTOMER_HOUSE_EXTRA + C +
            CUSTOMER_POSTAL + C +
            CUSTOMER_CITY + C +
            CUSTOMER_COUNTRY + ") VALUES (?,?,?,?,?,?,?,?,?,?,?)" + RETURNING + CUSTOMER_ID + END;
    public static final String CUSTOMER_QUERY_UPDATE_WHERE_IP = CUSTOMER_UPDATE + WHERE + CUSTOMER_PARAMETER_ID + END;
    public static final String CUSTOMER_QUERY_DELETE_WHERE_IP = CUSTOMER_DELETE + WHERE + CUSTOMER_PARAMETER_ID + END;

    public static String customerQueryWhereID(String select) {
        return CREATE_JSON + FROM_START + select +
                WHERE + CUSTOMER_PARAMETER_ID + ORDER_BY + CUSTOMER_ID + FROM_END + AS + ROW + END;
    }
}
