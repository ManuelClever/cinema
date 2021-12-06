package de.manuelclever.cinema.database.data.theater;

import de.manuelclever.cinema.database.query.PSQL.PSQLQTheater;
import de.manuelclever.cinema.database.query.Query;
import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;

public class PSQLTheaterReader implements TheaterDataRead {
    DataSource datasource;

    public PSQLTheaterReader(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public String getTheater(int id) {
        return Query.queryWhereOneInt(
                datasource, PSQLQTheater.theaterQueryWhereID(PSQLQTheater.THEATER_SELECT_THEATER), id);
    }

    @Override
    public String getTheater(String name) {
        return Query.queryWhereOneString(
                datasource, PSQLQTheater.theaterQueryWhereName(PSQLQTheater.THEATER_SELECT_THEATER), name);
    }

    @Override
    public String getTheaterID(String name) {
        return Query.queryWhereOneString(
            datasource, PSQLQTheater.theaterQueryWhereName(PSQLQTheater.THEATER_SELECT_ID), name);
    }

    @Override
    public String getTheaterNoSpecial(int id) {
        return Query.queryWhereOneInt(
                datasource, PSQLQTheater.theaterQueryWhereID(PSQLQTheater.THEATER_SELECT_NO_SPECIAL), id);
    }

    @Override
    public String getTheaterName(int id) {
        return Query.queryWhereOneInt(
                datasource, PSQLQTheater.THEATER_QUERY_SELECT_NAME_WHERE_ID, id);
    }

    @Override
    public String getTheaterSeats(int id) {
        return Query.queryWhereOneInt(
                datasource, PSQLQTheater.theaterQueryWhereID(PSQLQTheater.THEATER_SELECT_SEATS), id);
    }

    @Override
    public String getTheaterSeats(String name) {
        return Query.queryWhereOneString(
                datasource, PSQLQTheater.theaterQueryWhereID(PSQLQTheater.THEATER_SELECT_SEATS), name);
    }

    @Override
    public String getTheaters() {
        try(Connection conn = datasource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(PSQLQTheater.THEATER_QUERY_ALL)){

            if(rs.next()) {
                return rs.getString(PSQLQTheater.TABLE_THEATER);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }
}
