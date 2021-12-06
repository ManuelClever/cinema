package de.manuelclever.cinema.database.data.booking;

import java.sql.Date;

public interface BookingDataRead {
    String getBooking(int bookingID);
    String getBookingSimple(int bookingID);
    String getBookingsForScreening(int screeningID);
    String getBookingsBetween(Date start, Date end);
}
