package de.manuelclever.cinema.seatblock.seats;

public class Seat {
    private final SeatType TYPE;

    public Seat() {
        this.TYPE = SeatType.STANDARD;
    }

    public Seat(SeatType type) {
        this.TYPE = type;
    }

    public int getSize() {
        return TYPE.SIZE;
    }

    public SeatType getType() {
        return TYPE;
    }

    public char getTypeChar() {
        return TYPE.TYPE;
    }

    @Override
    public int hashCode() {
        return TYPE.hashCode() * 37;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == this.getClass()) {
            return obj.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + TYPE.TYPE + "]";
    }
}
