package de.manuelclever.cinema.database.data.theater;

public interface TheaterDataRead {
    String getTheater(int id);
    String getTheaterID(String name);
    String getTheaterNoSpecial(int id);
    String getTheaterName(int id);
    String getTheaterSeats(int id);
    String getTheaterSeats(String name);
    String getTheater(String name);
    String getTheaters();
}
