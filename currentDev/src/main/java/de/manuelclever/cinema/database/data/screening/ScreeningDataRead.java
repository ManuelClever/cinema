package de.manuelclever.cinema.database.data.screening;

import java.sql.Date;

public interface ScreeningDataRead {
    //which return type?
    String getScreening(int theaterID);
    String getScreeningsBetween(Date start, Date end);

    String getScreeningsOfMovie(int movieID);
    String getScreeningsOfMovieBetween(int movieID, Date start, Date end);

    String getSeats(int screeningID);
}
