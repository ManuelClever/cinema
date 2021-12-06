package de.manuelclever.cinema.database.data.theater;

public interface TheaterDataWrite {
    int addTheater(Theater theater);
    boolean removeTheater(int theaterID);
}
