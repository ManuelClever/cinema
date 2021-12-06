package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.seatblock.BlockOfSeats;
import de.manuelclever.cinema.seatblock.seats.SeatType;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.util.logging.Level;

public class SeatsDeserializer extends StdDeserializer {

    public SeatsDeserializer() {
        this(null);
    }

    public SeatsDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode blockNode = parser.getCodec().readTree(parser);

        int rows = blockNode.get(PSQLQueries.ROWS).asInt();
        int columns = blockNode.get(PSQLQueries.COLUMNS).asInt();
        JsonNode seatsNode = blockNode.get(PSQLQueries.COLUMN_SEATS);

        try {
            BlockOfSeats blockOfSeats = new BlockOfSeats(rows, columns);
            for (JsonNode seatNode : seatsNode) {
                blockOfSeats = addSeat(seatNode, blockOfSeats);
            }
            return blockOfSeats;
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    private BlockOfSeats addSeat(JsonNode seatNode, BlockOfSeats blockOfSeats) {
        try {
            int row = seatNode.get(PSQLQueries.ROW).asInt();
            int column = seatNode.get(PSQLQueries.COLUMN).asInt();
            char type = (seatNode.get(PSQLQueries.TYPE).asText()).charAt(0);
//            int size = (seatNode.get(PSQLQueries.SIZE).asInt());
            // calculate start of seat
            blockOfSeats.setAtPosition(row, column, SeatType.get(type));

        } catch(IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return blockOfSeats;
    }
}
