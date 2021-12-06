package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.manuelclever.cinema.database.data.screening.Screening;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ScreeningDeserializer extends StdDeserializer {

    public ScreeningDeserializer() {
        this(null);
    }

    public ScreeningDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Screening> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode nodeOfScreenings = parser.getCodec().readTree(parser);
        List<Screening> screenings = new ArrayList<>();

        for (JsonNode screeningNode : nodeOfScreenings) {
            try {
                Screening screening = createScreening(screeningNode);
                screenings.add(screening);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
                return null;
            }
        }
        return screenings;
    }

    private Screening createScreening(JsonNode screeningNode) {
        Screening screening = new Screening();

        try {
            screening.setId(screeningNode.get(PSQLQueries.SCREENING_ID).asInt());
            screening.setMovieID(screeningNode.get(PSQLQueries.MOVIE_ID).asInt());
            screening.setTheaterID(screeningNode.get(PSQLQueries.THEATER_ID).asInt());

            String date = screeningNode.get(PSQLQueries.SCREENING_TIME).asText();
            if(date.equals("")) {
                return null;
            }
            screening.setTimeString(date);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return screening;
    }
}
