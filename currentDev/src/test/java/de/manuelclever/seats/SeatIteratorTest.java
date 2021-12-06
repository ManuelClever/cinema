package de.manuelclever.seats;

import de.manuelclever.cinema.seatblock.BlockOfSeats;
import de.manuelclever.cinema.seatblock.seats.Seat;
import de.manuelclever.cinema.seatblock.SeatIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeatIteratorTest {

    @Test
    public void iterate() {
        int rows = 4;
        int columns = 3;
        BlockOfSeats blockOfSeats = new BlockOfSeats(rows, columns);
        SeatIterator iter = blockOfSeats.iterator();

        Boolean error = false;
        int countSeats = 0;
        while(iter.hasNext()) {
            Seat seat = iter.next();
            Seat returned = blockOfSeats.getSeat(iter.getCurrentRow(), iter.getCurrentColumn());

            if(returned == null) {
                error = true;
            } else countSeats++;
        }

        if(countSeats != rows * columns) {
            error = true;
        }

        Assertions.assertFalse(error);
    }
}
