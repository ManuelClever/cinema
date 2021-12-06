package de.manuelclever.cinema.database.data.screening;

public interface ScreeningDataWrite {
    int addScreening(Screening screening);
    boolean updateScreening(Screening screening);
    boolean removeScreening(int id);
}
