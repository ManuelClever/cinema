package de.manuelclever.cinema.database.data.customer;

import de.manuelclever.cinema.util.LogGenerator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.logging.Level;

public abstract class Person {
    private int id;
    private String lastName;
    private String firstName;
    private LocalDate birthday;
    private String email;
    private Address address;

    public Person() {
        this.lastName = "";
        this.firstName = "";
        this.birthday = LocalDate.parse("0000-01-01");
        this.email = "";
        this.address = new Address();
    }

    public Person(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Person(String lastName, String firstName, String birthday, String email, Address address) {
        this.lastName = lastName;
        this.firstName = firstName;
        try {
            LocalDate birthdayDate = LocalDate.parse(birthday);
            this.birthday = birthdayDate;
        } catch (DateTimeException e){
            e.printStackTrace();
            this.birthday = LocalDate.parse("0000-01-01");
            LogGenerator.log(Level.WARNING, Person.class, e.getMessage());
        }
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return address.getStreet();
    }

    public int getHouseNum() {
        return address.getNumber();
    }

    public String getHouseExtra() {
        return address.getExtra();
    }

    public String getPostal() {
        return address.getPostal();
    }

    public String getCity() {
        return address.getCity();
    }

    public Locale getCountry() {
        return address.getCountry();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isValid() {
        return lastName != null && !lastName.equals("") &&
                firstName != null && !firstName.equals("") &&
                birthday != null &&
                address.isValid();
    }
}
