package de.manuelclever.cinema.database.data.booking;

public interface BookingDataWrite {
    int book(Booking booking);
    boolean removeBooking(int bookingID);
}
