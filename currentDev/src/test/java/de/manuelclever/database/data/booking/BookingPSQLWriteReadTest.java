package de.manuelclever.database.data.booking;

import de.manuelclever.cinema.database.data.booking.Booking;
import de.manuelclever.cinema.database.data.booking.PSQLBookingReader;
import de.manuelclever.cinema.database.data.booking.PSQLBookingWriter;
import de.manuelclever.cinema.database.data.booking.SeatBill;
import de.manuelclever.cinema.database.data.customer.Customer;
import de.manuelclever.cinema.database.data.customer.PSQLCustomerReader;
import de.manuelclever.cinema.database.data.customer.PSQLCustomerWriter;
import de.manuelclever.cinema.database.data.movie.Movie;
import de.manuelclever.cinema.database.data.movie.PSQLMovieReader;
import de.manuelclever.cinema.database.data.movie.PSQLMovieWriter;
import de.manuelclever.cinema.database.data.screening.PSQLScreeningReader;
import de.manuelclever.cinema.database.data.screening.PSQLScreeningWriter;
import de.manuelclever.cinema.database.data.screening.Screening;
import de.manuelclever.cinema.database.data.theater.PSQLTheaterReader;
import de.manuelclever.cinema.database.data.theater.PSQLTheaterWriter;
import de.manuelclever.cinema.database.data.theater.Theater;
import de.manuelclever.cinema.database.datasource.DSCreator;
import de.manuelclever.cinema.database.parser.MyJsonParser;
import de.manuelclever.database.data.customer.CustomerPSQLWriteReadTest;
import de.manuelclever.database.data.movie.MoviePSQLWriteReadTest;
import de.manuelclever.database.data.screening.ScreeningPSQLWriteReadTest;
import de.manuelclever.database.data.theater.TheaterPSQLWriteReadTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BookingPSQLWriteReadTest {
    private static PSQLBookingWriter bookingWriter;
    private static PSQLCustomerWriter customerWriter;
    private static PSQLMovieWriter movieWriter;
    private static PSQLTheaterWriter theaterWriter;
    private static PSQLScreeningWriter screeningWriter;

    private static PSQLBookingReader bookingReader;
    private static PSQLCustomerReader customerReader;
    private static PSQLMovieReader movieReader;
    private static PSQLTheaterReader theaterReader;
    private static PSQLScreeningReader screeningReader;

    private static Map<Integer, Integer> validBookingIds;
    private static List<Integer> customerIds;
    private static List<Integer> movieIds;
    private static List<Integer> theaterIds;
    private static List<Integer> screeningIds;

    @BeforeAll
    private static void writeValidBookings() {
        initialize();
        createDependencyEntriesInDatabase();

        validBookings().forEach(booking -> {
            int id = bookingWriter.book(booking);
            validBookingIds.put(booking.hashCode(), id);
        });
    }
    
    private static void initialize() {
        bookingWriter = new PSQLBookingWriter(DSCreator.getTestDataSource());
        customerWriter = new PSQLCustomerWriter(DSCreator.getTestDataSource());
        movieWriter = new PSQLMovieWriter(DSCreator.getTestDataSource());
        theaterWriter = new PSQLTheaterWriter(DSCreator.getTestDataSource());
        screeningWriter = new PSQLScreeningWriter(DSCreator.getTestDataSource());

        bookingReader = new PSQLBookingReader(DSCreator.getTestDataSource());
        customerReader = new PSQLCustomerReader(DSCreator.getTestDataSource());
        screeningReader = new PSQLScreeningReader(DSCreator.getTestDataSource());
        movieReader = new PSQLMovieReader(DSCreator.getTestDataSource());
        theaterReader = new PSQLTheaterReader(DSCreator.getTestDataSource());

        validBookingIds = new HashMap<>();
        customerIds = new ArrayList<>();
        movieIds = new ArrayList<>();
        theaterIds = new ArrayList<>();
        screeningIds = new ArrayList<>();
    }

    private static void createDependencyEntriesInDatabase() {
        CustomerPSQLWriteReadTest.validCustomers().forEach(customer -> {
            int id = customerWriter.addCustomer(customer);
            customerIds.add(id);
        });
        MoviePSQLWriteReadTest.validMovies().forEach(movie -> {
            int id = movieWriter.addMovie(movie);
            movieIds.add(id);
        });
        TheaterPSQLWriteReadTest.validTheaters().forEach(theater -> {
            int id = theaterWriter.addTheater(theater);
            theaterIds.add(id);
        });

        // start the @BeforeAll method of ScreeningPSQLWriteReadTest, because of internal dependencies,
        // before adding the valid screenings
        ScreeningPSQLWriteReadTest.addMoviesAndTheaters();
        ScreeningPSQLWriteReadTest.validScreenings().forEach(screening -> {
            int id = screeningWriter.addScreening(screening);
            screeningIds.add(id);
        });
    }

    @AfterAll
    private static void removeAllBookings() {
        validBookingIds.forEach((hash,id) -> bookingWriter.removeBooking(id));
        removeDependencyEntriesInDatabase();
    }

    private static void removeDependencyEntriesInDatabase() {
        customerIds.forEach(customerWriter::removeCustomer);
        screeningIds.forEach(screeningWriter::removeScreening);
        movieIds.forEach(movieWriter::removeMovie);

        // start the @AfterAll method of ScreeningPSQLWriteReadTest, because of internal dependencies,
        // before removing the valid screenings
        ScreeningPSQLWriteReadTest.removeMoviesAndTheaters();
        theaterIds.forEach(theaterWriter::removeTheater);
    }

    @ParameterizedTest
    @MethodSource("validBookings")
    public void readReturnedJson(Booking booking) {
        int bookingId = validBookingIds.get(booking.hashCode());
        String returnedBooking = bookingReader.getBooking(bookingId);

        booking.setBookingId(bookingId);

        Customer customer = getCustomerDependency(booking);
        Screening screening = getScreeningDependency(booking);
        Movie movie = getMovieDependency(screening);
        Theater theater = getTheaterDependency(screening);
        String expectedJson = "[{\"booking_id\":" + booking.getId() + "," +
                "\"customer_id\":" + customer.getId() + "," +
                "\"last_name\":\"" + customer.getLastName() + "\"," +
                "\"first_name\":\"" + customer.getFirstName() + "\"," +
                "\"email\":\"" + customer.getEmail() + "\"," +
                "\"street\":\"" + customer.getStreet() + "\"," +
                "\"number\":" + customer.getHouseNum() + "," +
                "\"extra\":\"" + customer.getHouseExtra() + "\"," +
                "\"postal\":\"" + customer.getPostal() + "\"," +
                "\"city\":\"" + customer.getCity() + "\"," +
                "\"country\":\"" + customer.getCountry() + "\"," +
                "\"movie_id\":" + movie.getId() + "," +
                "\"name\":\"" + movie.getName() + "\"," +
                "\"length\":" + movie.getLength() + "," +
                "\"other\":\"" + movie.getOther() + "\"," +
                "\"screening_id\":" + screening.getId() + "," +
                "\"screening_time\":\"" + screening.getLocalDateTime() + ":00\"," +
                "\"theater_id\":" + theater.getID() + "," +
                "\"name\":\"" + theater.getName() + "\"," +
                "\"bill\":" + booking.getBillJson() + "}]";

        Assertions.assertEquals(expectedJson, returnedBooking);
    }

    private static Customer getCustomerDependency(Booking booking) {
        String json = customerReader.getCustomer(booking.getCustomerID());
        return MyJsonParser.parseCustomer(json);
    }

    private static Screening getScreeningDependency(Booking booking) {
        String json = screeningReader.getScreening(booking.getScreeningID());
        return MyJsonParser.parseScreening(json);
    }

    private static Movie getMovieDependency(Screening screening) {
        String json = movieReader.getMovie(screening.getMovieID());
        return MyJsonParser.parseMovie(json);
    }

    private static Theater getTheaterDependency(Screening screening) {
        String json = theaterReader.getTheater(screening.getTheaterID());
        return MyJsonParser.parseTheater(json);
    }

    public static Stream<Booking> validBookings() {
        List<Booking> bookings = new ArrayList<>();
        List<SeatBill> tempList = new ArrayList<>();

        tempList.add(new SeatBill(0,0,8.00,0.80,0.00));
        tempList.add(new SeatBill(0,1,8.00,0.80,0.00));
        bookings.add(new Booking(screeningIds.get(1),customerIds.get(1), tempList));

        tempList = new ArrayList<>();
        tempList.add(new SeatBill(1,1,8.00,1.00,0.30));
        tempList.add(new SeatBill(1,2,8.00,1.00,0.30));
        bookings.add(new Booking(screeningIds.get(2),customerIds.get(0), tempList));

        tempList = new ArrayList<>();
        tempList.add(new SeatBill(0,1,8.00,0.70,0.60));
        tempList.add(new SeatBill(0,2,8.00,0.70,0.40));
        tempList.add(new SeatBill(1,1,8.00,1.20,0.30));
        tempList.add(new SeatBill(1,2,8.00,1.20,0.75));
        bookings.add(new Booking(screeningIds.get(0),customerIds.get(1), tempList));

        return bookings.stream();
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("invalidBookings")
    public void writeInvalidBooking(Booking booking) {
        Assertions.assertTrue(true);
    }

    public static Stream<Booking> invalidBookings() {
        //toDo
        return null;
    }

    @ParameterizedTest
    @MethodSource("validBookings")
    public void removeValidBooking(Booking booking) {
        int bookingId = bookingWriter.book(booking);
        bookingWriter.removeBooking(bookingId);

        String returnedBooking = bookingReader.getBooking(bookingId);

        Assertions.assertNull(returnedBooking);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, -33, 0, 285})
    public void removeInvalidBooking(int id) {
        boolean removed = bookingWriter.removeBooking(id);

        Assertions.assertFalse(removed);
    }
}
