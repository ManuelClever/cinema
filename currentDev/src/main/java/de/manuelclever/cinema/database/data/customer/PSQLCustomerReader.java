package de.manuelclever.cinema.database.data.customer;

import de.manuelclever.cinema.database.query.PSQL.PSQLQCustomer;
import de.manuelclever.cinema.database.query.Query;
import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class PSQLCustomerReader implements CustomerDataRead {
    DataSource datasource;

    public PSQLCustomerReader(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public String getCustomer(int id) {
        return Query.queryWhereOneInt
                (datasource, PSQLQCustomer.customerQueryWhereID(PSQLQCustomer.CUSTOMER_SELECT_CUSTOMER), id);
    }

    @Override
    public String getCustomerNoAddress(int id) {
        return Query.queryWhereOneInt
                (datasource, PSQLQCustomer.customerQueryWhereID(PSQLQCustomer.CUSTOMER_SELECT_NO_ADDRESS), id);
    }

    @Override
    public String getCustomerAddress(int id) {
        return Query.queryWhereOneInt
                (datasource, PSQLQCustomer.customerQueryWhereID(PSQLQCustomer.CUSTOMER_SELECT_ADDRESS), id);
    }

    @Override
    public String getCustomerBalance(int id) {
        return Query.queryWhereOneInt
                (datasource, PSQLQCustomer.customerQueryWhereID(PSQLQCustomer.CUSTOMER_SELECT_BALANCE), id);
    }

    @Override
    public String getCustomerPoints(int id) {
        return Query.queryWhereOneInt
                (datasource, PSQLQCustomer.customerQueryWhereID(PSQLQCustomer.CUSTOMER_SELECT_POINTS), id);
    }

    @Override
    public String findCustomerSimple(Customer searchContent) {
        return customerQueryWhereChainSimple(searchContent);
    }

    private String customerQueryWhereChainSimple(Customer searchContent) {
        PreparedStatement querySelectWhereChain = createQuerySelectWhereChain(searchContent);
        try {

            ResultSet rs = querySelectWhereChain.executeQuery();
            if (rs.next()) {
                return rs.getString(PSQLQCustomer.TABLE_CUSTOMER);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }

    private PreparedStatement createQuerySelectWhereChain(Customer searchContent) {
        StringBuilder whereChainBuilder = new StringBuilder();
        final String OR = " OR ";

        Boolean lastName = false; Boolean firstName = false; Boolean birthday = false;
        Boolean street = false; Boolean postal = false; Boolean city = false;
        Boolean country = false;

        whereChainBuilder.append(" WHERE ");

        if(searchContent.getLastName() != null) {
            lastName = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_LAST_NAME).append(OR);
        }
        if(searchContent.getFirstName() != null) {
            firstName = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_FIRST_NAME).append(OR);
        }
        if(searchContent.getBirthday() != null) {
            birthday = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_BIRTHDAY).append(OR);
        }
        if(searchContent.getStreet() != null) {
            street = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_STREET).append(OR);
        }
        if(searchContent.getPostal() != null) {
            postal = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_POSTAL).append(OR);
        }
        if(searchContent.getCity() != null) {
            city = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_CITY).append(OR);
        }
        if(searchContent.getCountry() != null) {
            country = true;
            whereChainBuilder.append(PSQLQCustomer.CUSTOMER_PARAMETER_COUNTRY).append(OR);
        }

        int sizeWhereChain = whereChainBuilder.length();
        int lastOrIndex = sizeWhereChain - 5;
        whereChainBuilder.delete(lastOrIndex, sizeWhereChain - 1);
        try {
            Connection conn = datasource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    PSQLQCustomer.CREATE_JSON +
                            PSQLQCustomer.FROM_START +
                            PSQLQCustomer.CUSTOMER_SELECT_SIMPLE +
                            whereChainBuilder.toString() +
                            PSQLQCustomer.FROM_END +
                            PSQLQCustomer.AS +
                            PSQLQCustomer.ROW +
                            PSQLQCustomer.END);

            int index = 0;
            if (lastName) {
                preparedStatement.setString(index++, searchContent.getLastName());
            }
            if (firstName) {
                preparedStatement.setString(index++, searchContent.getFirstName());
            }
            if (birthday) {
                preparedStatement.setString(index++, searchContent.getBirthday().toString());
            }
            if (street) {
                preparedStatement.setString(index++, searchContent.getStreet());
            }
            if (postal) {
                preparedStatement.setString(index++, searchContent.getPostal());
            }
            if (city) {
                preparedStatement.setString(index++, searchContent.getCity());
            }
            if (country) {
                preparedStatement.setString(index++, searchContent.getCountry().getCountry());
            }
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }
}
