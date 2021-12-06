package de.manuelclever.database.data.screening;

import de.manuelclever.cinema.database.data.movie.PSQLMovieReader;
import de.manuelclever.cinema.database.data.movie.PSQLMovieWriter;
import de.manuelclever.cinema.database.data.screening.PSQLScreeningReader;
import de.manuelclever.cinema.database.data.screening.PSQLScreeningWriter;
import de.manuelclever.cinema.database.data.screening.Screening;
import de.manuelclever.cinema.database.data.theater.PSQLTheaterReader;
import de.manuelclever.cinema.database.data.theater.PSQLTheaterWriter;
import de.manuelclever.cinema.database.datasource.DSCreator;
import de.manuelclever.cinema.database.query.PSQL.PSQLQScreening;
import de.manuelclever.cinema.database.query.Query;
import de.manuelclever.database.data.movie.MoviePSQLWriteReadTest;
import de.manuelclever.database.data.theater.TheaterPSQLWriteReadTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ScreeningPSQLWriteReadTest {
    private static PSQLMovieWriter movieWriter;
    private static PSQLTheaterWriter theaterWriter;
    private static PSQLScreeningWriter screeningWriter;

    private static PSQLMovieReader movieReader;
    private static PSQLTheaterReader theaterReader;
    private static PSQLScreeningReader screeningReader;
    
    private static List<Screening> screenings;
    private static Map<Integer, Integer> screeningIds;
    private static List<Integer> movieIds;
    private static List<Integer> theaterIds;

    @BeforeAll
    public static void addMoviesAndTheaters() {
        initialize();
        createDependencyEntriesInDatabase();

        validScreenings().forEach(screening -> {
            int id = screeningWriter.addScreening(screening);
            screeningIds.put(screening.hashCode(), id);
        });
    }
    
    private static void initialize() {
        movieWriter = new PSQLMovieWriter(DSCreator.getTestDataSource());
        theaterWriter = new PSQLTheaterWriter(DSCreator.getTestDataSource());
        screeningWriter = new PSQLScreeningWriter(DSCreator.getTestDataSource());

        movieReader = new PSQLMovieReader(DSCreator.getTestDataSource());
        theaterReader = new PSQLTheaterReader(DSCreator.getTestDataSource());
        screeningReader = new PSQLScreeningReader(DSCreator.getTestDataSource());

        movieIds = new ArrayList<>();
        theaterIds = new ArrayList<>();
        screeningIds = new HashMap<>();
    }

    private static void createDependencyEntriesInDatabase() {
        MoviePSQLWriteReadTest.validMovies().forEach(movie -> {
            int id = movieWriter.addMovie(movie);
            movieIds.add(id);
        });

        TheaterPSQLWriteReadTest.validTheaters().forEach(theater -> {
            int id = theaterWriter.addTheater(theater);
            theaterIds.add(id);
        });
    }

    @AfterAll
    public static void removeMoviesAndTheaters() {
        screeningIds.forEach((k, id) -> screeningWriter.removeScreening(id));
        removeDependencyEntriesInDatabase();
    }

    private static void removeDependencyEntriesInDatabase() {
        movieIds.forEach(movieWriter::removeMovie);
        theaterIds.forEach(theaterWriter::removeTheater);
    }

    @ParameterizedTest
    @MethodSource("validScreenings")
    public void writeAndReadScreeningValid(Screening screening) {
        int id = screeningIds.get(screening.hashCode());

        String movieName = movieReader.getMovieName(screening.getMovieID());
        String theaterName = theaterReader.getTheaterName(screening.getTheaterID());
        String returnedScreening = screeningReader.getScreening(id);
        String expectedJson = "[{\"screening_id\":" + id + "," +
                "\"screening_time\":\"" + screening.getLocalDateTime() + ":00\"," +
                "\"movie_id\":" + screening.getMovieID() + "," +
                "\"name\":\"" + movieName + "\"," +
                "\"theater_id\":" + screening.getTheaterID() + "," +
                "\"name\":\"" + theaterName + "\"" + "}]";

        Assertions.assertEquals(expectedJson, returnedScreening);
    }

    public static Stream<Screening> validScreenings() {
        screenings = new ArrayList<>();
        //index0
        screenings.add(new Screening(theaterIds.get(1), movieIds.get(0), "2021-08-14T18:00:00"));
        //index1
        screenings.add(new Screening(theaterIds.get(0), movieIds.get(2), "2021-07-14T20:30:00"));
        //index2
        screenings.add(new Screening(theaterIds.get(2), movieIds.get(1), "2021-08-08T16:00:00"));
        //index3
        screenings.add(new Screening(theaterIds.get(1), movieIds.get(2), "2021-08-01T23:00:00"));
        //index4
        screenings.add(new Screening(theaterIds.get(0), movieIds.get(2), "2021-08-03T15:00:00"));

        return screenings.stream();
    }

    @Disabled("toDo")
    @ParameterizedTest
    @MethodSource("invalidScreenings")
    public void writeAndReadScreeningInvalid(Screening screening) {
        Assertions.fail();
    }

    public static Stream<Screening> invalidScreenings() {
        List<Screening> screenings = new ArrayList<>();
        screenings.add(new Screening());
        return screenings.stream();
    }

    @Test
    public void readScreeningsBetween() {
        Date start = new Date(LocalDateTime.of(
                LocalDate.of(2021,8,1), 
                LocalTime.of(18,0)).toEpochSecond(ZoneOffset.UTC)*1000);
        Date end = new Date(LocalDateTime.of(
                LocalDate.of(2021,8,8),
                LocalTime.of(18,0)).toEpochSecond(ZoneOffset.UTC)*1000);
        String returnedScreening = screeningReader.getScreeningsBetween(start, end);
        
        String screening1 = getScreeningJsonOf(3);
        String screening2 = getScreeningJsonOf(4);
        String expectedJson = "[" + screening1 + "," + screening2 + "]";

        Assertions.assertEquals(expectedJson, returnedScreening);
    }

    private static String getScreeningJsonOf(int index) {
        int hashExpected = screenings.get(index).hashCode();
        int screeningId = screeningIds.get(hashExpected);
        String json = screeningReader.getScreening(screeningId);

        return removeBackslashes(json);
    }

    private static String removeBackslashes(String s) {
        return s.replaceAll("[\\[\\]]", "");
    }

    @ParameterizedTest
    @MethodSource("validScreenings")
    public void readScreeningOfMovie(Screening screening) {
        String returnedScreening = screeningReader.getScreeningsOfMovie(movieIds.get(2));

        String screening1 = getScreeningOfMovieOf(1);
        String screening2 = getScreeningOfMovieOf(3);
        String screening3 = getScreeningOfMovieOf(4);
        String expectedJson = "[" + screening1 + "," + screening2 + "," + screening3 + "]";

        Assertions.assertEquals(expectedJson, returnedScreening);
    }

    private static String getScreeningOfMovieOf(int index) {
        int hashExpected = screenings.get(index).hashCode();
        int screeningId = screeningIds.get(hashExpected);
        String queryJsonTimeTheater = PSQLQScreening.screeningQueryWhereID(PSQLQScreening.SCREENING_SELECT_TIME_THEATER);

        String screeningOfMovie = Query.queryWhereOneInt(DSCreator.getTestDataSource(), queryJsonTimeTheater, screeningId);

        return screeningOfMovie != null ? removeBackslashes(screeningOfMovie) : null;
    }

    @ParameterizedTest
    @MethodSource("validScreenings")
    public void readScreeningOfMovieBetween(Screening screening) {
        Date start = new Date(LocalDateTime.of(
                LocalDate.of(2021,8,1),
                LocalTime.of(18,0)).toEpochSecond(ZoneOffset.UTC)*1000);
        Date end = new Date(LocalDateTime.of(
                LocalDate.of(2021,8,5),
                LocalTime.of(18,0)).toEpochSecond(ZoneOffset.UTC)*1000);
        String returnedScreening = screeningReader.getScreeningsOfMovieBetween(movieIds.get(2), start, end);

        String screening1 = getScreeningOfMovieOf(3);
        String screening2 = getScreeningOfMovieOf(4);
        String expectedJson = "[" + screening1 + "," + screening2 + "]";

        Assertions.assertEquals(expectedJson, returnedScreening);
    }

    @ParameterizedTest
    @MethodSource("validScreenings")
    public void removeValidScreening(Screening screening) {
        int id = screeningWriter.addScreening(screening);
        screeningWriter.removeScreening(id);

        String returnedScreening = screeningReader.getScreening(id);

        Assertions.assertNull(returnedScreening);
    }
}
