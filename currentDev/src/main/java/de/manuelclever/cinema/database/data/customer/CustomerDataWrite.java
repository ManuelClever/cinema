package de.manuelclever.cinema.database.data.customer;

public interface CustomerDataWrite {
    int addCustomer(Customer customer);
    int updateCustomer(Customer customer);
    void removeCustomer(int customerID);
}
