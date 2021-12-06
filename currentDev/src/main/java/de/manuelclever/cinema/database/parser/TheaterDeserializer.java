package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.manuelclever.cinema.database.data.theater.StandardTheater;
import de.manuelclever.cinema.database.data.theater.Theater;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.seatblock.BlockOfSeats;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TheaterDeserializer extends StdDeserializer<List<Theater>> {

    public TheaterDeserializer() {
        this(null);
    }

    public TheaterDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Theater> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode theatersNode = parser.getCodec().readTree(parser);
        List<Theater> theaters = new ArrayList<>();

        for (JsonNode theaterNode : theatersNode) {
            try {
                Theater theater = createStandardTheater(theaterNode);
                theaters.add(theater);

            } catch (DateTimeParseException | IllegalArgumentException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
                return null;
            }
        }
        return theaters;
    }

    private Theater createStandardTheater(JsonNode theaterNode) {
        Theater theater = new StandardTheater();

        try {
            theater.setId(theaterNode.get(PSQLQueries.THEATER_ID).asInt());
            theater.setName(theaterNode.get(PSQLQueries.THEATER_NAME).asText());
            theater.setExtras(theaterNode.get(PSQLQueries.THEATER_EXTRAS).asText());

            String seatsNode = theaterNode.get(PSQLQueries.THEATER_BLOCK).toString();
            BlockOfSeats blockOfSeats = MyJsonParser.parseSeats(seatsNode);
            theater.setSeats(blockOfSeats);

        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return theater;
    }
}
