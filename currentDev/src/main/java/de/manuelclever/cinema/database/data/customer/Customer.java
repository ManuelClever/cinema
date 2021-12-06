package de.manuelclever.cinema.database.data.customer;

import de.manuelclever.cinema.util.LogGenerator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Level;

public class Customer extends Person implements Cloneable {
    Date dateOfEntry;

    public Customer() {
        super();
        this.dateOfEntry = Date.valueOf(LocalDate.parse("0000-01-01"));
    }

    public Customer(Customer customer) {
        super(customer.getLastName(), customer.getFirstName(), customer.getBirthday().toString(), customer.getEmail(),
                customer.getAddress());
        this.dateOfEntry = customer.getDateOfEntry();

    }

    public Customer(String lastName, String firstName) {
        super(lastName, firstName);
    }

    public Customer(String lastName, String firstName, String birthday, String email, String dateOfEntry, Address address) {
        super(lastName, firstName, birthday, email, address);
        try {
            Date entryDate = Date.valueOf(dateOfEntry);
            this.dateOfEntry = entryDate;
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            this.dateOfEntry = Date.valueOf("1970-01-01");
            LogGenerator.log(Level.WARNING, Customer.class, e.getMessage());
        }
    }

    public Date getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public boolean isValid() {
        return super.isValid() && dateOfEntry != null;
    }

    @Override
    public Customer clone() {
        return new Customer(this);
    }

    @Override
    public int hashCode() {
        return (getLastName().hashCode() + getFirstName().hashCode() + getBirthday().hashCode() + getAddress().hashCode()) * 37;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == Customer.class) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + getLastName() + ", " + getFirstName() + ", " + getBirthday() + ", " + getEmail() + ", " +
                getDateOfEntry() + ", " + getAddress().toString() + "]";
    }
}
