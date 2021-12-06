package de.manuelclever.cinema.database.data.screening;

import de.manuelclever.cinema.database.query.PSQL.PSQLQScreening;
import de.manuelclever.cinema.database.query.Query;

import javax.sql.DataSource;
import java.sql.Date;

public class PSQLScreeningReader implements ScreeningDataRead {
    DataSource datasource;

    public PSQLScreeningReader(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public String getScreening(int screeningID) {
        return Query.queryWhereOneInt(
                datasource, PSQLQScreening.screeningQueryWhereID(PSQLQScreening.SCREENING_SELECT_MOVIE_TIME_THEATER), screeningID);
    }

    @Override
    public String getScreeningsBetween(Date start, Date end) {
        return Query.queryWhereOneIntTwoDates(
                datasource, PSQLQScreening.SCREENING_QUERY_WHERE_TIME, 0, start, end);
    }

    @Override
    public String getScreeningsOfMovie(int movieID) {
        return Query.queryWhereOneInt(datasource, PSQLQScreening.SCREENING_QUERY_WHERE_MOVIE_ID, movieID);
    }

    @Override
    public String getScreeningsOfMovieBetween(int movieID, Date start, Date end) {
        return Query.queryWhereOneIntTwoDates(
                datasource, PSQLQScreening.SCREENING_QUERY_WHERE_MOVIE_ID_TIME, movieID, start, end);
    }

    @Override
    public String getSeats(int screeningID) {
        return Query.queryWhereOneInt(
                datasource, PSQLQScreening.screeningQueryWhereID(PSQLQScreening.SCREENING_SELECT_SEATS), screeningID);
    }
}
