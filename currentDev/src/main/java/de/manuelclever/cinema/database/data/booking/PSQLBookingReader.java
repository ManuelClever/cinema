package de.manuelclever.cinema.database.data.booking;

import de.manuelclever.cinema.database.query.PSQL.PSQLQBooking;
import de.manuelclever.cinema.database.query.Query;

import javax.sql.DataSource;
import java.sql.Date;

public class PSQLBookingReader implements BookingDataRead {
    DataSource datasource;

    public PSQLBookingReader(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public String getBooking(int bookingID) {
        return Query.queryWhereOneInt(
                datasource, PSQLQBooking.bookingQueryWhereID(PSQLQBooking.BOOKING_SELECT_BOOKING), bookingID);
    }

    @Override
    public String getBookingSimple(int bookingID) {
        return Query.queryWhereOneInt(
                datasource, PSQLQBooking.bookingQueryWhereID(PSQLQBooking.BOOKING_SELECT_SIMPLE), bookingID);
    }

    @Override
    public String getBookingsForScreening(int screeningID) {
        return Query.queryWhereOneInt(datasource, PSQLQBooking.BOOKING_QUERY_WHERE_SCREENING_ID, screeningID);
    }

    @Override
    public String getBookingsBetween(Date start, Date end) {
        return Query.queryWhereTwoDates(datasource, PSQLQBooking.BOOKING_QUERY_WHERE_TIME, start, end);
    }
}
