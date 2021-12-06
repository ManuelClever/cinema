package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.manuelclever.cinema.database.data.movie.Genre;
import de.manuelclever.cinema.database.data.movie.Movie;
import de.manuelclever.cinema.database.data.movie.StandardMovie;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MovieDeserializer extends StdDeserializer {

    public MovieDeserializer() {
        this(null);
    }

    public MovieDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Movie> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode moviesNode = parser.getCodec().readTree(parser);
        List<Movie> movies = new ArrayList<>();

        for (JsonNode movieNode : moviesNode) {
            try {
                Movie movie = createMovie(movieNode);
                movies.add(movie);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
                return null;
            }
        }
        return movies;
    }

    private Movie createMovie(JsonNode movieNode) {
        Movie movie = new StandardMovie();

        try {
            movie.setId(movieNode.get(PSQLQueries.MOVIE_ID).asInt());
            movie.setName(movieNode.get(PSQLQueries.MOVIE_NAME).asText(),
                    movieNode.get(PSQLQueries.MOVIE_ORIGINAL_NAME).asText());
            movie.setDescription(movieNode.get(PSQLQueries.MOVIE_DESCRIPTION).asText());
            movie.setLength(movieNode.get(PSQLQueries.MOVIE_LENGTH).asInt());
            movie.setStudio(movieNode.get(PSQLQueries.MOVIE_STUDIO).asText());

            int year = movieNode.get(PSQLQueries.MOVIE_YEAR).asInt();
            if(year != 0) {
                movie.setYear(year);
            }

            int ageRestr = movieNode.get(PSQLQueries.MOVIE_AGE_RESTR).asInt();
            movie.setAgeRestriction(ageRestr);

            String[] arr = movieNode.get(PSQLQueries.MOVIE_GENRE).asText().split(",");
            Arrays.stream(arr).forEach(string -> movie.addGenre(Genre.get(string)));
            
            arr = movieNode.get(PSQLQueries.MOVIE_OTHER).asText().split(",");
            Arrays.stream(arr).forEach(string -> movie.addOther(string));

            arr = movieNode.get(PSQLQueries.MOVIE_ACTORS).asText().split(",");
            Arrays.stream(arr).forEach(string -> movie.addActor(string));

            arr = movieNode.get(PSQLQueries.MOVIE_DIRECTORS).asText().split(",");
            Arrays.stream(arr).forEach(string -> movie.addDirector(string));

            arr = movieNode.get(PSQLQueries.MOVIE_TRAILER).asText().split(",");
            Arrays.stream(arr).forEach(string -> movie.addTrailer(string));

            arr = movieNode.get(PSQLQueries.MOVIE_TAGS).asText().split(",");
            Arrays.stream(arr).forEach(string -> movie.addTag(string));

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return movie;
    }
}
