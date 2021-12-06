package de.manuelclever.database.parser;

import de.manuelclever.cinema.database.data.screening.Screening;
import de.manuelclever.cinema.database.parser.MyJsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseScreeningTest {

    @Test
    public void withAllInput() {
        Screening screening = new Screening();
        screening.setMovieID(15);
        screening.setTimeString("2021-08-01T20:30:00");
        screening.setTheaterID(3);

        String json =  "[{\"screening_id\":\"\"," +
                "\"movie_id\":15," +
                "\"screening_time\":\"2021-08-01T20:30:00\"," +
                "\"theater_id\":3}]";

        Assertions.assertEquals(screening, MyJsonParser.parseScreening(json));
    }
}
