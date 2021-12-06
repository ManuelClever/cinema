package de.manuelclever.cinema.seatblock;

import de.manuelclever.cinema.seatblock.seats.Seat;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SeatIterator implements Iterator<Seat> {
    private BlockOfSeats blockOfSeats;
    private final int ROWS;
    private final int COLUMNS;
    private int currentRow;
    private int currentColumn;

    public SeatIterator(BlockOfSeats blockOfSeats) {
        this.blockOfSeats = blockOfSeats;
        this.ROWS = blockOfSeats.getSize()[0];
        this.COLUMNS = blockOfSeats.getSize()[1];
        currentRow = 0;
        currentColumn = -1;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    @Override
    public boolean hasNext() {
        return isNotLastRow() || isNotLastColumnInLastRow();
    }

    private boolean isNotLastRow() {
        return currentRow < ROWS - 1;

    }

    private boolean isNotLastColumnInLastRow() {
        return currentRow == ROWS - 1 && currentColumn < COLUMNS - 1;
    }

    @Override
    public Seat next() {
         if(currentColumn < COLUMNS - 1) {
            currentColumn += 1;
            Seat seat = blockOfSeats.getSeat(currentRow, currentColumn);
            return seat;
        } else if (currentColumn == COLUMNS - 1 && isNotLastRow()) {
            currentRow += 1;
            currentColumn = 0;
            Seat seat = blockOfSeats.getSeat(currentRow, currentColumn);
            return seat;
        }
        throw new NoSuchElementException();
    }
}
