package de.manuelclever.cinema.database.data.customer;

import de.manuelclever.cinema.database.datasource.DSCreator;
import de.manuelclever.cinema.database.query.PSQL.PSQLQBonusCard;
import de.manuelclever.cinema.database.query.PSQL.PSQLQCustomer;
import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class PSQLCustomerWriter implements CustomerDataWrite {
    DataSource datasource;

    public PSQLCustomerWriter(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public int addCustomer(Customer customer) {
        if(customer.getId() == 0) {
            try(Connection conn = datasource.getConnection();
                PreparedStatement queryInsertCustomer =
                        prepareCustomerStatement(conn, PSQLQCustomer.CUSTOMER_QUERY_INSERT, customer)) {

                ResultSet resultSet = queryInsertCustomer.executeQuery();
                if(resultSet.next()) {
                    conn.commit();

                    int customerId = resultSet.getInt(1);
                    createBonusCard(customerId);
                    return customerId;
                }
            } catch(SQLException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            }
        } else {
            return updateCustomer(customer);
        }
        return 0;
    }

    private PreparedStatement prepareCustomerStatement(Connection conn, String sql, Customer customer)
            throws SQLException {
        PreparedStatement queryInsertCustomer = conn.prepareStatement(sql);

        if(customer.isValid()) {
            queryInsertCustomer.setString(1, customer.getLastName());
            queryInsertCustomer.setString(2, customer.getFirstName());
            queryInsertCustomer.setString(3, customer.getBirthday().toString());
            queryInsertCustomer.setString(4, customer.getEmail());
            queryInsertCustomer.setDate(5, customer.getDateOfEntry());
            queryInsertCustomer.setString(6, customer.getStreet());
            queryInsertCustomer.setInt(7, customer.getHouseNum());
            queryInsertCustomer.setString(8, customer.getHouseExtra());
            queryInsertCustomer.setString(9, customer.getPostal());
            queryInsertCustomer.setString(10, customer.getCity());
            queryInsertCustomer.setString(11, customer.getCountry().toString());
        }

        return queryInsertCustomer;
    }

    private void createBonusCard(int customerId) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryInsertBonusCard =
                    prepareBonusCardStatement(conn, PSQLQBonusCard.BONUS_CARD_QUERY_INSERT, customerId)) {

            queryInsertBonusCard.execute();
            conn.commit();
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
    }

    private PreparedStatement prepareBonusCardStatement(Connection conn, String sql, int customerId) throws SQLException {
        PreparedStatement queryInsertBonusCard = conn.prepareStatement(sql);

        queryInsertBonusCard.setInt(1, customerId);
        queryInsertBonusCard.setFloat(2, 0);
        queryInsertBonusCard.setInt(3, 0);

        return queryInsertBonusCard;
    }

    @Override
    public int updateCustomer(Customer customer) {
        try(Connection conn = DSCreator.getDataSource().getConnection();
            PreparedStatement queryUpdateCustomer =
                    prepareCustomerStatement(conn, PSQLQCustomer.CUSTOMER_QUERY_UPDATE_WHERE_IP, customer)) {

            if(queryUpdateCustomer != null) {
                queryUpdateCustomer.setInt(10, customer.getId());

                ResultSet resultSet = queryUpdateCustomer.executeQuery();
                if(resultSet.next()) {
                    conn.commit();
                    return resultSet.getInt(PSQLQCustomer.CUSTOMER_ID);
                }
            }
        } catch(SQLException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return 0;
    }

    @Override
    public void removeCustomer(int customerId) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryDeleteCustomer = conn.prepareStatement(PSQLQCustomer.CUSTOMER_QUERY_DELETE_WHERE_IP)) {

            queryDeleteCustomer.setInt(1, customerId);


            removeBonusCard(customerId);
            queryDeleteCustomer.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
    }

    private void removeBonusCard(int customerId) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryDeleteBonusCard =
                    conn.prepareStatement(PSQLQBonusCard.BONUS_CARD_QUERY_DELETE_WHERE_IP)) {

            queryDeleteBonusCard.setInt(1, customerId);

            queryDeleteBonusCard.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
    }
}
