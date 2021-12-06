package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.manuelclever.cinema.database.data.booking.Booking;
import de.manuelclever.cinema.database.data.customer.Customer;
import de.manuelclever.cinema.database.data.movie.Movie;
import de.manuelclever.cinema.database.data.screening.Screening;
import de.manuelclever.cinema.database.data.theater.Theater;
import de.manuelclever.cinema.seatblock.BlockOfSeats;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class MyJsonParser {

    public static Customer parseCustomer(String json) {
        List<Customer> customers = parseCustomers(json);
        return customers == null ? null : customers.get(0);
    }

    public static List<Customer> parseCustomers(String json) {
        try {
            ObjectMapper mapper = prepareMapper(List.class, new CustomerDeserializer());
            return mapper.readValue(json, List.class);

        } catch (IOException e) {
            LogGenerator.log(Level.WARNING, MyJsonParser.class, e.getMessage());
            return null;
        }
    }

    private static ObjectMapper prepareMapper(Class cl, StdDeserializer deserializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(cl, deserializer);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper;
    }

    public static Theater parseTheater(String json) {
        List<Theater> theaters = parseTheaters(json);

        return theaters == null ? null : theaters.get(0);
    }

    public static List<Theater> parseTheaters(String json) {
        try {
            ObjectMapper mapper = prepareMapper(List.class, new TheaterDeserializer());
            return mapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, MyJsonParser.class, e.getMessage());
            return null;
        }
    }

    public static BlockOfSeats parseSeats(String json) {
        try {
            ObjectMapper mapper = prepareMapper(BlockOfSeats.class, new SeatsDeserializer());
            return mapper.readValue(json, BlockOfSeats.class);
        } catch (IOException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, MyJsonParser.class, e.getMessage());
            return null;
        }
    }

    public static Movie parseMovie(String json) {
        List<Movie> movies = parseMovies(json);
        return movies == null ? null : movies.get(0);
    }

    public static List<Movie> parseMovies(String json) {
        try {
            ObjectMapper mapper = prepareMapper(List.class, new MovieDeserializer());
            return mapper.readValue(json, List.class);

        } catch (IOException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, MyJsonParser.class, e.getMessage());
            return null;
        }
    }

    public static Screening parseScreening(String json) {
        List<Screening> screenings = parseScreenings(json);
        return screenings == null ? null : screenings.get(0);
    }

    public static List<Screening> parseScreenings(String json) {
        try {
            ObjectMapper mapper = prepareMapper(List.class, new ScreeningDeserializer());
            return mapper.readValue(json, List.class);

        } catch (IOException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, MyJsonParser.class, e.getMessage());
            return null;
        }
    }

    public static Booking parseBooking(String json) {
        List<Booking> bookings = parseBookings(json);
        return bookings == null ? null : bookings.get(0);
    }

    public static List<Booking> parseBookings(String json) {
        try {
            ObjectMapper mapper = prepareMapper(List.class, new BookingDeserializer());
            return mapper.readValue(json, List.class);

        } catch (IOException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, MyJsonParser.class, e.getMessage());
            return null;
        }
    }
}
