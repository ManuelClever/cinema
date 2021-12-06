package de.manuelclever.cinema.database.query;

import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;

public class Query {

    public static String queryWhereOneInt(DataSource datasource, String statement, int id) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryJsonWhereId = conn.prepareStatement(statement)) {

            queryJsonWhereId.setInt(1, id);
            ResultSet rs = queryJsonWhereId.executeQuery();

            if(rs.next()) {
                String output = rs.getString(1);
                rs.close();
                return output;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, statement, e.getMessage());
        }
        return null;
    }

    public static String queryWhereOneString(DataSource datasource, String statement, String string) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryJsonWhereString = conn.prepareStatement(statement)) {

            queryJsonWhereString.setString(0, string);
            ResultSet rs = queryJsonWhereString.executeQuery();

            if(rs.next()) {
                String output = rs.getString(0);
                rs.close();
                return output;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, statement, e.getMessage());
        }
        return null;
    }

    public static String queryWhereTwoDates(DataSource datasource, String statement, Date dateStart, Date dateEnd) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryJsonWhereIntTwoDates = conn.prepareStatement(statement)){

            queryJsonWhereIntTwoDates.setDate(0, dateStart);
            queryJsonWhereIntTwoDates.setDate(1, dateEnd);
            ResultSet rs = queryJsonWhereIntTwoDates.executeQuery();

            if(rs.next()) {
                String output = rs.getString(0);
                rs.close();
                queryJsonWhereIntTwoDates.close();
                conn.close();
                return output;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, statement, e.getMessage());
        }
        return null;
    }

    public static String queryWhereOneIntTwoDates(
            DataSource datasource, String statement, int id, Date dateStart, Date dateEnd) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryJsonWhereIntTwoDates = conn.prepareStatement(statement)) {
            int index = 1;

            if(id != 0) {
                queryJsonWhereIntTwoDates.setInt(index++, id);
            }
            queryJsonWhereIntTwoDates.setDate(index++, dateStart);
            queryJsonWhereIntTwoDates.setDate(index, dateEnd);

            ResultSet rs = queryJsonWhereIntTwoDates.executeQuery();

            if(rs.next()) {
                String output = rs.getString(1);
                rs.close();
                queryJsonWhereIntTwoDates.close();
                conn.close();
                return output;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, statement, e.getMessage());
        }
        return null;
    }

    public static ResultSet executeQuery(DataSource datasource, String sql) {
        try(Connection con = datasource.getConnection();
            Statement st = con.createStatement()) {
            return st.executeQuery(sql);
        } catch (SQLException ignore) {}
        return null;
    }
}
