package de.manuelclever.database.data.theater;

import de.manuelclever.cinema.database.data.theater.PSQLTheaterReader;
import de.manuelclever.cinema.database.data.theater.PSQLTheaterWriter;
import de.manuelclever.cinema.database.data.theater.StandardTheater;
import de.manuelclever.cinema.database.data.theater.Theater;
import de.manuelclever.cinema.database.datasource.DSCreator;
import de.manuelclever.cinema.seatblock.BlockOfSeats;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TheaterPSQLWriteReadTest {
    private static PSQLTheaterWriter theaterWriter;
    private static PSQLTheaterReader theaterReader;
    
    private static Map<String, Integer> validTheaterIds;

    @BeforeAll
    private static void writeValidTheaters() {
        initalize();
        validTheaters().forEach(theater -> {
            int id = theaterWriter.addTheater(theater);
            validTheaterIds.put(theater.getName(), id);
        });
    }
    
    private static void initalize() {
        theaterWriter = new PSQLTheaterWriter(DSCreator.getTestDataSource());
        theaterReader = new PSQLTheaterReader(DSCreator.getTestDataSource());
        
        validTheaterIds = new HashMap<>();
    }

    @AfterAll
    private static void removeAllTheaters() {
        validTheaterIds.forEach((birthday,id) -> theaterWriter.removeTheater(id));
    }

    @ParameterizedTest
    @MethodSource("validTheaters")
    public void readReturnedJson(Theater theater) {
        int id = validTheaterIds.get(theater.getName());

        String returnedTheater = theaterReader.getTheater(id);
        String expectedJson = "[{\"theater_id\":" + id + "," +
                "\"name\":\"" + theater.getName() + "\"," +
                "\"block\":" + theater.getSeatsJson() + "," +
                "\"extras\":\"" + theater.getExtras() + "\"}]";

        Assertions.assertEquals(expectedJson, returnedTheater);
    }

    public static Stream<Theater> validTheaters() {
        List<Theater> theaters = new ArrayList<>();
        theaters.add(new StandardTheater("Alpha", new BlockOfSeats(3,3), "HDR,3D,DOLBY"));
        theaters.add(new StandardTheater("Beta", new BlockOfSeats(5,4), "HDR"));
        theaters.add(new StandardTheater("Charlie", new BlockOfSeats(1,4), "HDR,4K,DOLBY"));
        theaters.add(new StandardTheater("Delta", new BlockOfSeats(2,3), "60hz,3D,HDR"));
        return theaters.stream();
    }

    @Test
    public void throwErrorWhenNegativeBlockSize() {
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> new StandardTheater("Alpha", new BlockOfSeats(-5,-7), "HDR,3D,DOLBY"));
    }

    @ParameterizedTest
    @MethodSource("invalidTheaters")
    public void writeAndReadTheaterInvalid(Theater theater) {
        int id = theaterWriter.addTheater(theater);

        String returnedTheater = theaterReader.getTheater(id);

        Assertions.assertEquals(null, returnedTheater);
    }

    public static Stream<Theater> invalidTheaters() {
        List<Theater> theaters = new ArrayList<>();
        theaters.add(new StandardTheater("Alpha", null, "HDR,3D,DOLBY"));

        return theaters.stream();
    }

    @ParameterizedTest
    @MethodSource("validTheaters")
    public void removeValidTheater(Theater theater) {
        int id = theaterWriter.addTheater(theater);
        theaterWriter.removeTheater(id);

        String returnedTheater = theaterReader.getTheater(id);

        Assertions.assertNull(returnedTheater);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, -33, 0, 285})
    public void removeInvalidTheater(int id) {
        boolean removed = theaterWriter.removeTheater(id);

        Assertions.assertFalse(removed);
    }
}
