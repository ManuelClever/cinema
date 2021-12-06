package de.manuelclever.seats;

import de.manuelclever.cinema.seatblock.BlockOfSeats;
import de.manuelclever.cinema.seatblock.seats.Seat;
import de.manuelclever.cinema.seatblock.seats.SeatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockOfSeatsTest {
    private BlockOfSeats inputBlockOfSeats;

    @BeforeEach
    private void beforeEach() {
        inputBlockOfSeats = new BlockOfSeats(4, 3);
    }

    @Test
    public void setAtPositionSizeOne() {
        Seat seat = new Seat(SeatType.VIP);
        inputBlockOfSeats.setAtPosition(1,0, SeatType.VIP);

        Assertions.assertEquals(seat, inputBlockOfSeats.getSeat(1, 0));
    }

    @Test
    public void setAtPositionSizeTwo() {
        Seat seat = new Seat(SeatType.LOVE);
        inputBlockOfSeats.setAtPosition(1, 0, SeatType.LOVE);

        Assertions.assertEquals(seat, inputBlockOfSeats.getSeat(1, 1));
    }

    @Test
    public void createJson() {
        inputBlockOfSeats.setAtPosition(1, 1, SeatType.WHEELCHAIR);
        inputBlockOfSeats.setAtPosition(2, 1, SeatType.LOVE);
        inputBlockOfSeats.setAtPosition(3, 0, SeatType.VIP);
        inputBlockOfSeats.setAtPosition(3, 1, SeatType.VIP);
        inputBlockOfSeats.setAtPosition(3, 2, SeatType.VIP);

        String expected = "{\"rows\":4,\"columns\":3,\"seats\":" +
                                                "[{\"row\":0,\"column\":0,\"type\":\"S\",\"size\":1}," +
                                                "{\"row\":0,\"column\":1,\"type\":\"S\",\"size\":1}," +
                                                "{\"row\":0,\"column\":2,\"type\":\"S\",\"size\":1}," +
                                                "{\"row\":1,\"column\":0,\"type\":\"S\",\"size\":1}," +
                                                "{\"row\":1,\"column\":1,\"type\":\"W\",\"size\":2}," +
                                                "{\"row\":1,\"column\":2,\"type\":\"W\",\"size\":1}," +
                                                "{\"row\":2,\"column\":0,\"type\":\"S\",\"size\":1}," +
                                                "{\"row\":2,\"column\":1,\"type\":\"L\",\"size\":2}," +
                                                "{\"row\":2,\"column\":2,\"type\":\"L\",\"size\":1}," +
                                                "{\"row\":3,\"column\":0,\"type\":\"V\",\"size\":1}," +
                                                "{\"row\":3,\"column\":1,\"type\":\"V\",\"size\":1}," +
                                                "{\"row\":3,\"column\":2,\"type\":\"V\",\"size\":1}]}";

        Assertions.assertEquals(expected, inputBlockOfSeats.getJson());
    }

    @Test
    public void changeSizeBigger() {
        inputBlockOfSeats.setAtPosition(1, 1, SeatType.VIP);
        inputBlockOfSeats.changeSize(5, 5);

        BlockOfSeats expected = new BlockOfSeats(5, 5);
        expected.setAtPosition(1, 1, SeatType.VIP);

        Assertions.assertEquals(expected, inputBlockOfSeats);
    }

    @Test
    public void changeSizeSmaller() {
        inputBlockOfSeats.setAtPosition(1, 0, SeatType.VIP);
        inputBlockOfSeats.setAtPosition(3, 0, SeatType.STANDARD);
        inputBlockOfSeats.setAtPosition(3, 1, SeatType.WHEELCHAIR);
        inputBlockOfSeats.changeSize(2, 1);

        BlockOfSeats expected = new BlockOfSeats(2,1);
        expected.setAtPosition(1, 0, SeatType.VIP);

        Assertions.assertEquals(expected, inputBlockOfSeats);
    }
}
