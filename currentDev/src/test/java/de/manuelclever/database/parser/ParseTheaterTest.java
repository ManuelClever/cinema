package de.manuelclever.database.parser;

import de.manuelclever.cinema.database.data.theater.StandardTheater;
import de.manuelclever.cinema.database.data.theater.Theater;
import de.manuelclever.cinema.database.parser.MyJsonParser;
import de.manuelclever.cinema.seatblock.BlockOfSeats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseTheaterTest {

    @Test
    public void withMandatoryInput() {
        String inputJson = "[{\"theater_id\":1," +
                "\"name\":\"Alpha\"," +
                "\"block\":{\"rows\":\"2\"," +
                        "\"columns\":\"2\"," +
                        "\"seats\":[{\"row\":0,\"column\":0,\"type\":\"S\",\"size\":1}," +
                                    "{\"row\":0,\"column\":1,\"type\":\"S\",\"size\":1}," +
                                    "{\"row\":1,\"column\":0,\"type\":\"S\",\"size\":1}," +
                                    "{\"row\":1,\"column\":1,\"type\":\"S\",\"size\":1}]}," +
                "\"extras\":\"\"}]";
        Theater expected = new StandardTheater("Alpha", new BlockOfSeats(2,2));

        Assertions.assertEquals(expected, MyJsonParser.parseTheater(inputJson));
    }

    @Test
    public void withoutMandatoryInputOfTypeString() {
        String inputJson =  "[{\"theater_id\":\"\"," +
                "\"name\":\"Alpha\"," +
                "\"block\":{\"rows\":\"\"," +
                            "\"columns\":\"\"," +
                            "\"seats\":[{}]}," +
                "\"extras\":\"\"}]";
        Theater expected = new StandardTheater("Alpha", null, "");

        Assertions.assertEquals(expected, MyJsonParser.parseTheater(inputJson));
    }
}
