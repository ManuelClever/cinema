package de.manuelclever.cinema.database.data.booking;

import de.manuelclever.cinema.database.query.PSQL.PSQLQBooking;
import de.manuelclever.cinema.util.LogGenerator;
import org.postgresql.util.PGobject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class PSQLBookingWriter implements BookingDataWrite {
    DataSource datasource;

    public PSQLBookingWriter(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public int book(Booking booking) {
        if(booking.getId() == 0) {
            try(Connection conn = datasource.getConnection();
                PreparedStatement queryInsertBooking =
                        prepareBookingStatement(conn, PSQLQBooking.BOOKING_QUERY_INSERT, booking)) {

                ResultSet resultSet = queryInsertBooking.executeQuery();
                if(resultSet.next()) {
                    conn.commit();
                    return resultSet.getInt(1);
                }
            } catch(SQLException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            }
        }
        return 0;
    }

    private PreparedStatement prepareBookingStatement(Connection conn, String sql, Booking booking) throws SQLException{
        PreparedStatement queryInsertBooking = conn.prepareStatement(sql);

        PGobject billJsonObject = new PGobject();
        billJsonObject.setType("json");
        billJsonObject.setValue(booking.getBillJson());

        queryInsertBooking.setInt(1, booking.getCustomerID());
        queryInsertBooking.setInt(2, booking.getScreeningID());
        queryInsertBooking.setObject(3, billJsonObject);

        return queryInsertBooking;
    }

    @Override
    public boolean removeBooking(int bookingID) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryDeleteWhereId = conn.prepareStatement(PSQLQBooking.BOOKING_QUERY_DELETE_WHERE_ID)) {

            queryDeleteWhereId.setInt(1, bookingID);

            boolean success = queryDeleteWhereId.execute();
            conn.commit();
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return false;
    }
}
