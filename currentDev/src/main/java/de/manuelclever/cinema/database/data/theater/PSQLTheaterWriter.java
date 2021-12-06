package de.manuelclever.cinema.database.data.theater;

import de.manuelclever.cinema.database.query.PSQL.PSQLQTheater;
import de.manuelclever.cinema.util.LogGenerator;
import org.postgresql.util.PGobject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;

public class PSQLTheaterWriter implements TheaterDataWrite {
    DataSource datasource;

    public PSQLTheaterWriter(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public int addTheater(Theater theater) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryInsertTheater = conn.prepareStatement(PSQLQTheater.THEATER_QUERY_INSERT)) {

            PGobject seatsJsonObj = new PGobject();
            seatsJsonObj.setType("json");
            seatsJsonObj.setValue(theater.getSeatsJson());

            queryInsertTheater.setString(1, theater.getName());
            queryInsertTheater.setObject(2, seatsJsonObj);
            queryInsertTheater.setString(3, theater.getExtras().toString());

            ResultSet rs = queryInsertTheater.executeQuery();
            if(rs.next()) {
                conn.commit();
                return rs.getInt(PSQLQTheater.THEATER_ID);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean removeTheater(int theaterID) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryRemoveTheater = conn.prepareStatement(PSQLQTheater.THEATER_QUERY_DELETE)) {

            queryRemoveTheater.setInt(1, theaterID);
            boolean success = queryRemoveTheater.execute();
            conn.commit();
            return success;
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return false;
    }
}
