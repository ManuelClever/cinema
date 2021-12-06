package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.manuelclever.cinema.database.data.booking.Booking;
import de.manuelclever.cinema.database.data.booking.SeatBill;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BookingDeserializer extends StdDeserializer {

    public BookingDeserializer() {
        this(null);
    }

    public BookingDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Booking> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode nodeOfBookings = parser.getCodec().readTree(parser);
        List<Booking> bookings = new ArrayList<>();

        for (JsonNode bookingNode : nodeOfBookings) {
            try {
                Booking booking = createBooking(bookingNode);
                bookings.add(booking);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
                return null;
            }
        }
        return bookings;
    }

    private Booking createBooking(JsonNode bookingNode) {
        Booking booking = new Booking();

        try {
            booking.setBookingId(bookingNode.get(PSQLQueries.BOOKING_ID).asInt());
            booking.setCustomerID(bookingNode.get(PSQLQueries.CUSTOMER_ID).asInt());
            booking.setScreeningID(bookingNode.get(PSQLQueries.SCREENING_ID).asInt());
            booking.setDiscount(bookingNode.get(PSQLQueries.DISCOUNT).asInt());

            JsonNode nodeOfBills = bookingNode.get(PSQLQueries.BOOKING_BILL);
            booking.setBill(createBills(nodeOfBills));

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return booking;
    }

    private List<SeatBill> createBills(JsonNode nodeOfBills) {
        List<SeatBill> bills = new ArrayList<>();

        for(JsonNode billNode : nodeOfBills) {
            SeatBill bill = new SeatBill();
            bill.setRow(billNode.get(PSQLQueries.ROW).asInt());
            bill.setColumn(billNode.get(PSQLQueries.COLUMN).asInt());
            bill.setPrice(billNode.get(PSQLQueries.PRICE).asDouble());
            bill.setMultiplier(billNode.get(PSQLQueries.MULTIPLIER).asDouble());
            bill.setDiscount(billNode.get(PSQLQueries.DISCOUNT).asDouble());

            bills.add(bill);
        }
        return bills;
    }
}
