package de.manuelclever.cinema.seatblock.seats;

public enum SeatType {
    NULL('N', 1),
    STANDARD('S', 1),
    VIP('V', 1),
    LOVE('L', 2),
    WHEELCHAIR('W', 2);

    public final char TYPE;
    public final int SIZE;

    SeatType(char type, int size) {
        this.TYPE = type;
        this.SIZE = size;
    }

    public static SeatType get(char type) {
        switch(type) {
            case 'N':
                return NULL;
            case 'S':
                return STANDARD;
            case 'V':
                return VIP;
            case 'L':
                return LOVE;
            case 'W':
                return WHEELCHAIR;
            default:
                return STANDARD;
        }
    }
}
