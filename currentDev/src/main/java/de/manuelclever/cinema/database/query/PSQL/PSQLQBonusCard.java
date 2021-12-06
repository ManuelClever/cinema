package de.manuelclever.cinema.database.query.PSQL;

public class PSQLQBonusCard extends PSQLQueries {

    //select
    public static final String BONUS_CARD_SELECT_BALANCE = SELECT + BONUS_CARD_BALANCE + FROM + TABLE_BONUS_CARD;
    public static final String BONUS_CARD_SELECT_POINTS = SELECT + BONUS_CARD_POINTS + FROM + TABLE_BONUS_CARD;

    //update
    public static final String BONUS_CARD_UPDATE = UPDATE + TABLE_BONUS_CARD + " SET (" +
            BONUS_CARD_BALANCE + C +
            BONUS_CARD_POINTS + ") = (?,?)";

    //delete
    public static final String BONUS_CARD_DELETE = DELETE + FROM + TABLE_BONUS_CARD;

    //parameter
    public static final String BONUS_CARD_PARAMETER_ID = BONUS_CARD_ID + PARAMETER;
    public static final String BONUS_CARD_PARAMETER_BALANCE =BONUS_CARD_BALANCE + PARAMETER;
    public static final String BONUS_CARD_PARAMETER_POINTS = BONUS_CARD_POINTS + PARAMETER;

    //queries
    public static final String BONUS_CARD_QUERY_INSERT = INSERT_INTO + TABLE_BONUS_CARD +
            " VALUES (?, ?, ?)" + RETURNING + BONUS_CARD_ID + END;
    public static final String BONUS_CARD_QUERY_DELETE_WHERE_IP =
            BONUS_CARD_DELETE + WHERE + BONUS_CARD_PARAMETER_ID + END;
}
