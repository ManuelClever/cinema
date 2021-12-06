package de.manuelclever.cinema.database.data.theater;

import de.manuelclever.cinema.seatblock.BlockOfSeats;

public class StandardTheater extends Theater {

    public StandardTheater() {
    }

    public StandardTheater(String name, BlockOfSeats blockOfSeats) {
        super(name, blockOfSeats, "");
    }

    public StandardTheater(String name, BlockOfSeats blockOfSeats, String extras) {
        super(name, blockOfSeats, extras);
    }
}
