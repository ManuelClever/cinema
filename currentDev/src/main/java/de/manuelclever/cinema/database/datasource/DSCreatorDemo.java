package de.manuelclever.cinema.database.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSCreatorDemo {

    public static void main(String[] args) {
        DataSource dataSource = DSCreator.getTestDataSource();

        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet set  = st.executeQuery("INSERT INTO cinema_schema.customer " +
                    "(lastName, firstName, birthday, dateOfEntry, street, house, postal, city, country) " +
                    "VALUES ('Mustermann', 'Maximilian',  '1994-07-05', '2021-01-01', 'Musterstr.', 37, '45871', " +
                    "'Wiesbaden', 'DE') RETURNING id;");
            if(set.next()) {

            }
            conn.commit();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DSCreatorDemo.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
