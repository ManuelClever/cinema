package de.manuelclever.database.data.movie;

import de.manuelclever.cinema.database.data.movie.*;
import de.manuelclever.cinema.database.datasource.DSCreator;
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

public class MoviePSQLWriteReadTest {
    private static PSQLMovieWriter movieWriter;
    private static PSQLMovieReader movieReader;

    private static Map<String, Integer> validMovieIds;

    @BeforeAll
    private static void writeValidMovies() {
        initialize();
        validMovies().forEach(movie -> {
            int id = movieWriter.addMovie(movie);
            validMovieIds.put(movie.getName(), id);
        });
    }

    private static void initialize() {
        movieWriter = new PSQLMovieWriter(DSCreator.getTestDataSource());
        movieReader = new PSQLMovieReader(DSCreator.getTestDataSource());

        validMovieIds = new HashMap<>();
    }

    @AfterAll
    private static void removeAllMovies() {
        validMovieIds.forEach((name,id) -> movieWriter.removeMovie(id));
    }

    @ParameterizedTest
    @MethodSource("validMovies")
    public void readReturnedJson(Movie movie) {
        int id = validMovieIds.get(movie.getName());

        String returnedMovie = movieReader.getMovie(id);
        String expectedJson = "[{\"movie_id\":" + id + "," +
                "\"name\":\"" + movie.getName() + "\"," +
                "\"original_name\":\"" + movie.getOriginalName() + "\"," +
                "\"genre\":\"" + movie.getGenre() + "\"," +
                "\"description\":\"" + movie.getDescription() + "\"," +
                "\"length\":" + movie.getLength() + "," +
                "\"other\":\"" + movie.getOther() + "\"," +
                "\"actors\":\"" + movie.getActors() + "\"," +
                "\"directors\":\"" + movie.getDirectors() + "\"," +
                "\"country\":\"" + movie.getCountry() + "\"," +
                "\"year\":" + movie.getYear() + "," +
                "\"age_restriction\":" + movie.getAgeRestriction() + "," +
                "\"studio\":\"" + movie.getStudio() + "\"," +
                "\"trailer\":\"" + movie.getTrailer() + "\"," +
                "\"tags\":\"" + movie.getTags() + "\"}]";

        Assertions.assertEquals(expectedJson, returnedMovie);
    }

    public static Stream<Movie> validMovies() {
        List<Movie> movies = new ArrayList<>();

        Movie temp = new StandardMovie("Der Unglaubliche Hulk", "The incredible Hulk");
        temp.addGenre(Genre.ACTION, Genre.ADVENTURE ,Genre.COMEDY);
        temp.setDescription("Grüner Mann AAAHHH");
        temp.setLength(124);
        temp.addOther("3D", "HDR", "8K");
        temp.addActor("Tom Mann", "Hulk", "Frau die gerettet werden muss");
        temp.addDirector("Hulk schreibt sich selbst");
        temp.setCountry("USA");
        temp.setYear(2005);
        temp.setAgeRestriction(12);
        temp.setStudio("Marvel");
        temp.addTrailer("youtube.com/hulkTrailer", "youtube.com/hulkTrailer2");
        temp.addTag("grün", "keine ahnung", "keine kreativität");
        movies.add(temp);

        temp = new StandardMovie("Prinzessin Lilifee", "Lily The Fairy Princess");
        temp.setDescription("Keine Ahnung wie mir das in den Sinn kam");
        movies.add(temp);

        temp = new StandardMovie("Rotkäppchen", "Red Riding Hood");
        temp.setDescription("Määärchen!");
        temp.setCountry("Deutschland");
        temp.setYear(1999);
        temp.setAgeRestriction(0);
        movies.add(temp);

        return movies.stream();
    }

    @Disabled("toDo")
    @ParameterizedTest
    @MethodSource("invalidMovies")
    public void writeAndReadMovieInvalid(Movie movie) {
        Assertions.fail();
    }

    public static Stream<Movie> invalidMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new StandardMovie());
        return movies.stream();
    }

    @ParameterizedTest
    @MethodSource("validMovies")
    public void removeValidMovie(Movie movie) {
        int id = movieWriter.addMovie(movie);
        movieWriter.removeMovie(id);

        String returnedMovie = movieReader.getMovie(id);
        Assertions.assertNull(returnedMovie);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, -33, 0, 285})
    public void removeInvalidMovie(int id) {
        boolean removed = movieWriter.removeMovie(id);

        Assertions.assertFalse(removed);
    }
}
