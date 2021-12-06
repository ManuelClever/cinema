package de.manuelclever.cinema.database.data.screening;

import de.manuelclever.cinema.database.query.PSQL.PSQLQScreening;
import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class PSQLScreeningWriter implements ScreeningDataWrite {
    DataSource datasource;

    public PSQLScreeningWriter(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public int addScreening(Screening screening) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryInsertScreening =
                    prepareScreeningStatement(conn, PSQLQScreening.SCREENING_QUERY_INSERT, screening)) {

            if(queryInsertScreening != null) {
                ResultSet rs = queryInsertScreening.executeQuery();
                if(rs.next()) {
                    conn.commit();
                    return rs.getInt(PSQLQScreening.SCREENING_ID);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return 0;
    }

    private PreparedStatement prepareScreeningStatement(Connection conn, String sql, Screening screening) throws SQLException{
        try {
            PreparedStatement queryInsertScreening = conn.prepareStatement(sql);

            queryInsertScreening.setInt(1, screening.getMovieID());
            queryInsertScreening.setInt(2, screening.getTheaterID());
            queryInsertScreening.setObject(3, screening.getLocalDateTime());

            return queryInsertScreening;
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateScreening(Screening screening) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryUpdateScreening =
                    prepareScreeningStatement(conn, PSQLQScreening.SCREENING_QUERY_UPDATE_WHERE_IP, screening)) {

            if(queryUpdateScreening != null) {
                queryUpdateScreening.setInt(3, screening.getId());
                boolean success = queryUpdateScreening.execute();
                conn.commit();
                return success;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeScreening(int id) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryDeleteScreening = conn.prepareStatement(PSQLQScreening.SCREENING_QUERY_DELETE)) {

            queryDeleteScreening.setInt(1, id);
            boolean success = queryDeleteScreening.execute();
            conn.commit();
            return success;
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return false;
    }
}
