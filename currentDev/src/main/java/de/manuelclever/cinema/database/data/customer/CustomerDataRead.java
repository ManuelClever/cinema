package de.manuelclever.cinema.database.data.customer;

public interface CustomerDataRead {
    String getCustomer(int id);
    String getCustomerNoAddress(int id);
    String getCustomerAddress(int id);
    String getCustomerBalance(int id);
    String getCustomerPoints(int id);
    String findCustomerSimple(Customer searchContent);
}
