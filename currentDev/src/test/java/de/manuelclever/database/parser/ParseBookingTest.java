package de.manuelclever.database.parser;

import de.manuelclever.cinema.database.data.booking.Booking;
import de.manuelclever.cinema.database.data.booking.SeatBill;
import de.manuelclever.cinema.database.parser.MyJsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseBookingTest {

    @Test
    public void withAllInput() {
        Booking booking = new Booking();
        booking.setCustomerID(18);
        booking.setScreeningID(11);
        booking.addBill(new SeatBill(1,2,7.00,0.9,0),
                new SeatBill(1,3,7.00,0.9,0));

        String json = "[{\"booking_id\":\"\"," +
                "\"customer_id\":18," +
                "\"screening_id\":11," +
                "\"bill\":[{\"row\":1," +
                            "\"column\":2," +
                            "\"price\":7.00," +
                            "\"multiplier\":0.9," +
                            "\"discount\":0}," +
                            "{\"row\":1," +
                            "\"column\":3," +
                            "\"price\":7.00," +
                            "\"multiplier\":0.9," +
                            "\"discount\":0}]," +
                            "\"discount\":0}]";
        Assertions.assertEquals(booking, MyJsonParser.parseBooking(json));
    }
}
